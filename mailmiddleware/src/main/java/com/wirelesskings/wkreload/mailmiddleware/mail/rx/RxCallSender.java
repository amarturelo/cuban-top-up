package com.wirelesskings.wkreload.mailmiddleware.mail.rx;

import com.wirelesskings.wkreload.mailmiddleware.mail.async.CallSender;
import com.wirelesskings.wkreload.mailmiddleware.mail.async.OnStateChangedListener;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.funtions.RetryWithDelay;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

import org.reactivestreams.Publisher;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by Alberto on 7/10/2017.
 */

public class RxCallSender {
    private Setting setting;

    private CallSender mSender;

    private int t;
    private int time;

    public RxCallSender(Setting setting, int t, int time) {
        this.setting = setting;
        this.t = t;
        this.time = time;

        mSender = new CallSender(setting);

    }

    public Completable sender(String subject, String body, String receiver) {
        return doSender(subject, body, receiver).retryWhen(new RetryWithDelay(t, time));
    }

    private Completable doSender(final String subject, final String body, final String receiver) {
        return Completable.create(e -> mSender.execute(subject, body, receiver, new OnStateChangedListener() {
            @Override
            public void onExecuting() {
            }

            @Override
            public void onSuccess(ArrayList<?> list) {
                e.onComplete();
            }

            @Override
            public void onError(int code, String msg) {
                e.onError(new Exception(msg));
            }

            @Override
            public void onCanceled() {
                e.onError(new Exception("Canceled"));
            }
        }));
    }
}
