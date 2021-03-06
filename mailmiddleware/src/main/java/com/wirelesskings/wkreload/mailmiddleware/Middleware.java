package com.wirelesskings.wkreload.mailmiddleware;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorException;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorToReceivedException;
import com.wirelesskings.wkreload.mailmiddleware.exceptions.NetworkErrorToSendException;
import com.wirelesskings.wkreload.mailmiddleware.mail.model.Email;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallReceiver;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallSender;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Constants;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Alberto on 24/10/2017.
 */

public class Middleware {

    protected final Map<String, Listener> mListeners;

    public static String me = "amarturelo@gmail.com";
    public static String wk = "reload@wirelesskingsllc.com";
    public static String RECEIVER = wk;

    private RxCallReceiver receiver;

    private RxCallSender sender;

    private Gson gson;


    private CompositeDisposable compositeSubscription = new CompositeDisposable();

    protected void addSubscription(Disposable subscription) {
        compositeSubscription.add(subscription);
    }

    protected void clearSubscriptions() {
        compositeSubscription.clear();
    }

    public Middleware(String user_nauta, String pass_nauta, String token) {
        Setting out = new Setting(user_nauta, pass_nauta);
        out.setServerType(Constants.SMTP_PLAIN); //0 for plain , 1 for ssl
        out.setHost("smtp.nauta.cu");
        out.setPort(Constants.SMTP_PLAIN_PORT); //25 for smtp plain,465 for smtp ssl.


        Setting in = new Setting(user_nauta, pass_nauta);
        in.setServerType(Constants.IMAP_PLAIN);
        in.setHost("imap.nauta.cu");
        in.setPort(Constants.IMAP_PLAIN_PORT);


        receiver = new RxCallReceiver(in, 0, 5000);
        sender = new RxCallSender(out, 5, 5000);
        gson = new Gson();
        mListeners = new HashMap<>();
        this.token = token;
    }

    private void addListener(String callId, Listener listener) {
        mListeners.put(callId, listener);
        if (!compositeSubscription.isDisposed()) {
            addSubscription(receiver());
        }
    }

    private Disposable receiver() {
        return receiver.receiver(RECEIVER)
                .delay(6000, TimeUnit.MILLISECONDS)
                .repeat()
                .flatMapIterable(new Function<List<Email>, Iterable<Email>>() {
                    @Override
                    public Iterable<Email> apply(@NonNull List<Email> emails) throws Exception {
                        return emails;
                    }
                })
                .forEach(new Consumer<Email>() {
                    @Override
                    public void accept(Email email) throws Exception {
                        emailReceived(email);
                    }
                });
    }

    private void emailReceived(Email email) {

        JsonElement node = gson.fromJson(email.getBody(), JsonElement.class);
        Log.v(this.getClass().getSimpleName(), email.getBody());

        if (checkMD5(node, email.getSubject())) {
            String id = node.getAsJsonObject().get(WKField.ID).getAsString();

            final Listener listener = mListeners.get(id);

            if (listener != null && listener instanceof ResultListener) {
                removedListener(id);
                if (node.getAsJsonObject().get(WKField.SUCCESS).toString().equals("false")) {
                    ((ResultListener) listener).onError(new Exception(node.getAsJsonObject().get(WKField.ERRORS).toString()));
                } else
                    ((ResultListener) listener).onSuccess(
                            gson.toJson(node.getAsJsonObject().get(WKField.RESULT)));
            }
        }
    }

    private boolean checkMD5(JsonElement node, String subject) {

        String test1 = Crypto.md5(Util.scraper(node) + Crypto.md5(token));

        return test1.equals(subject);
    }

    private void removedListener(String id) {
        mListeners.remove(id);
        if (mListeners.size() == 0) {
            clearSubscriptions();
        }
    }

    private String token;

    public String call(String name, Map<String, Object> params, final ResultListener listener) {
        final String callId = nextId();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(WKField.ACTION, name);
        data.put(WKField.ID, callId);
        data.put(WKField.PARAMS, params);

        send(data, token, new SuccessListened() {
            @Override
            public void onSuccess() {
                if (listener != null) {
                    addListener(callId, listener);
                }
            }

            @Override
            public void onError(Exception e) {
                if (listener != null)
                    listener.onError(new NetworkErrorToSendException(e));
            }
        });

        return callId;
    }

    private void send(Map<String, Object> data, String token, final SuccessListened listened) {

        String subject = Crypto.md5(Util.scraper(gson.toJsonTree(data)) + Crypto.md5(token));

        //String subject = Crypto.md5(Util.fetch(data) + token);
        addSubscription(sender.sender(subject, toJson(data), RECEIVER)
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        listened.onSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        listened.onError(new NetworkErrorToReceivedException(throwable));
                    }
                }));
    }

    private String toJson(Object o) {
        return gson.toJson(o);
    }

    public String nextId() {
        return UUID.randomUUID().toString();
    }

    public void cancel(String id) {
        removedListener(id);
    }
}
