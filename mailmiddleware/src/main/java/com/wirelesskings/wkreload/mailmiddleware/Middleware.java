package com.wirelesskings.wkreload.mailmiddleware;

import com.google.gson.Gson;
import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;
import com.wirelesskings.wkreload.mailmiddleware.mail.model.Email;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallReceiver;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallSender;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Function;

/**
 * Created by Alberto on 24/10/2017.
 */

public class Middleware {

    protected final Map<String, Listener> mListeners;

    public static String RECEIVER = "reload@wirelesskingsllc.com";

    private RxCallReceiver receiver;

    private RxCallSender sender;

    private Gson gson;

    private String tokenMD5 = "";

    private Disposable disposable;

    private CompositeDisposable compositeSubscription = new CompositeDisposable();

    private static Middleware ourInstance = null;

    public static Middleware getInstance() {
        return ourInstance;
    }

    public static void init(Setting in, Setting out, String tokenMD5) {
        if (ourInstance != null) {
            ourInstance.clearSubscriptions();
        }
        ourInstance = new Middleware(in, out, tokenMD5);
    }

    protected void addSubscription(Disposable subscription) {
        compositeSubscription.add(subscription);
    }

    protected void clearSubscriptions() {
        compositeSubscription.clear();
    }


    public Middleware(Setting in, Setting out, String tokenMD5) {
        this.tokenMD5 = tokenMD5;
        receiver = new RxCallReceiver(in, 0, 5000);
        sender = new RxCallSender(out, 5, 5000);
        gson = new Gson();
        mListeners = new HashMap<>();
    }

    private void addListener(String callId, Listener listener) {
        mListeners.put(callId, listener);
        if (disposable == null || disposable.isDisposed()) {
            disposable = receiver();
            addSubscription(disposable);
        }
    }

    private Disposable receiver() {
        return receiver.receiver(RECEIVER)
                .delay(6000, TimeUnit.MILLISECONDS)
                .repeat()
                .subscribe(emails -> {
                    for (Email email :
                            emails) {
                        emailReceived(email);
                    }
                });
    }

    private void emailReceived(Email email) {
        Map<String, Object> node = gson.fromJson(email.getBody(), LinkedHashMap.class);

        String id = (String) node.get(WKField.ID);

        final Listener listener = mListeners.get(id);

        //TODO falta comprobar el md5 del asunto
        if (listener != null && listener instanceof ResultListener) {
            removedListener(id);
            ((ResultListener) listener).onSuccess(gson.toJson(node.get(WKField.RESULT)));
        }
    }

    private void removedListener(String id) {
        mListeners.remove(id);
        if (mListeners.size() == 0 && disposable != null || !disposable.isDisposed()) {
            disposable.dispose();
            compositeSubscription.remove(disposable);
        }
    }

    public void call(String name, Map<String, Object> params, ResultListener listener) {
        final String callId = nextId();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(WKField.ACTION, name);
        data.put(WKField.ID, callId);
        data.put(WKField.PARAMS, params);

        send(data, new SuccessListened() {
            @Override
            public void onSuccess() {
                if (listener != null) {
                    addListener(callId, listener);
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                listener.onError(error, reason, details);
            }
        });
    }

    private void send(Map<String, Object> data, SuccessListened listened) {
        String subject = Crypto.md5(Structure.fetch(data) + tokenMD5);
        addSubscription(sender.sender(subject, toJson(data), RECEIVER)
                .subscribe(() -> listened.onSuccess(),
                        throwable -> listened.onError(throwable.getMessage(), throwable.getCause().getMessage(), "")));
    }

    /*public Maybe<Map<String, Object>> call(String name, Map<String, Object> params) {
        String id = String.valueOf(nextId());

        Map<String, Object> node = new LinkedHashMap<>();
        node.put(WKField.ACTION, name);
        node.put(WKField.ID, id);
        node.put(WKField.PARAMS, params);


        String subject = Crypto.md5(Structure.fetch(node) + tokenMD5);

        return sender.sender(subject, gson.toJson(node), RECEIVER)
                .andThen(receiver.receiver(RECEIVER)).map(new Function<List<Email>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(@NonNull List<Email> emails) throws Exception {
                        for (Email email :
                                emails) {
                            Map<String, Object> node = gson.fromJson(email.getBody(), LinkedHashMap.class);
                            if (node.get(WKField.ID).equals(id)) {
                                String subject = Crypto.md5(Structure.fetch(node) + tokenMD5);
                                //if (subject.equals(email.getSubject()))
                                return (Map<String, Object>) node.get(WKField.RESULT);
                            }
                        }
                        return new LinkedHashMap<>();
                    }
                });
    }*/

    private String toJson(Object o) {
        return gson.toJson(o);
    }

    public String nextId() {
        return UUID.randomUUID().toString();
    }

}
