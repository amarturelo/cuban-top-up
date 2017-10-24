package com.wirelesskings.wkreload.mailmiddleware;

import com.wirelesskings.wkreload.mailmiddleware.callback.Callback;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallReceiver;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.RxCallSender;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Alberto on 24/10/2017.
 */

public class Middleware {

    private RxCallReceiver receiver;

    private RxCallSender sender;

    private String key;

    private AtomicInteger atomicInteger;

    private CompositeDisposable compositeSubscription = new CompositeDisposable();

    public Middleware(Setting out, Setting in, String key) {
        receiver = new RxCallReceiver(in, 5, 5000);
        sender = new RxCallSender(out, 5, 5000);
        this.key = key;
        atomicInteger = new AtomicInteger(1);
    }

    private void addSubscription(Disposable subscription) {
        compositeSubscription.add(subscription);
    }

    private void clearSubscriptions() {
        compositeSubscription.clear();
    }

    public void call(Object... params) throws JSONException {
        JSONObject node = new JSONObject();
        int id = nextId();
        node.put("id", id);

    }

    private int nextId() {
        return atomicInteger.getAndIncrement();
    }

}
