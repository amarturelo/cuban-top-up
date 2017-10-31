package com.wirelesskings.wkreload.mailmiddleware;

import com.google.gson.Gson;
import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;
import com.wirelesskings.wkreload.mailmiddleware.mail.model.Email;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallReceiver;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallSender;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.operators.single.SingleAmb;
import io.reactivex.internal.operators.single.SingleCreate;

/**
 * Created by Alberto on 24/10/2017.
 */

public class Middleware {

    public static String RECEIVER = "reload@wirelesskingsllc.com";

    private RxCallReceiver receiver;

    private RxCallSender sender;

    private Gson gson;

    private String tokenMD5 = "";


    private AtomicInteger atomicInteger;


    public Middleware(Setting in, Setting out, String tokenMD5) {
        this.tokenMD5 = tokenMD5;
        receiver = new RxCallReceiver(in, 5, 5000);
        sender = new RxCallSender(out, 5, 5000);
        gson = new Gson();
        atomicInteger = new AtomicInteger(1);
    }

    public Completable call(Structure structure) throws JSONException {
        return sender.sender(structure.getConcat(), gson.toJson(structure.getNode()), RECEIVER);
    }

    public Maybe<Map<String, Object>> call(String name, Map<String, Object> params) {
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
                                if (subject.equals(email.getSubject()))
                                    return (Map<String, Object>) node.get(WKField.RESULT);
                            }
                        }
                        return new LinkedHashMap<>();
                    }
                });
    }

    /**/

    /*public Maybe<List<Structure>> receiverMail() {
        return receiver.receiver(RECEIVER).map(new Function<List<Email>, List<Structure>>() {
            @Override
            public List<Structure> apply(@NonNull List<Email> emails) throws Exception {
                List<Structure> structures = new ArrayList<Structure>();

                for (Email email :
                        emails) {
                    Structure structure = new Structure();
                    structure.setNode(gson.fromJson(email.getBody(), LinkedHashMap.class));

                    String subject = DigestUtils.md5Hex(structure.getConcat() + tokenMD5);

                    if (email.getSubject().equals(subject)) {
                        structures.add(structure);
                    }
                }

                return structures;
            }
        });
    }*/

    public int nextId() {
        return atomicInteger.getAndIncrement();
    }

}
