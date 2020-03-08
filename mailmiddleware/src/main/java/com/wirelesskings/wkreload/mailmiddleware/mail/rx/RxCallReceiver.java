package com.wirelesskings.wkreload.mailmiddleware.mail.rx;

import android.util.Log;

import com.wirelesskings.wkreload.mailmiddleware.mail.async.CallReceiver;
import com.wirelesskings.wkreload.mailmiddleware.mail.async.OnStateChangedListener;
import com.wirelesskings.wkreload.mailmiddleware.mail.model.Email;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.funtions.ReceivedRetryWithDelay;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.funtions.SenderRetryWithDelay;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Alberto on 7/10/2017.
 */

public class RxCallReceiver {
    private int t;
    private int time;

    private Setting setting;

    private CallReceiver callReceiver;

    public RxCallReceiver(Setting setting, int t, int time) {
        this.t = t;
        this.time = time;
        this.setting = setting;
        this.callReceiver = new CallReceiver(setting);
    }

    public Single<List<Email>> receiver(String filter) {
        return doReceiver(filter).retryWhen(new SenderRetryWithDelay(t, time));
    }

    private Single<List<Email>> doReceiver(final String filter) {
        return Single.create(new SingleOnSubscribe<List<Email>>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<List<Email>> emitter) throws Exception {
                callReceiver.execute(filter, new OnStateChangedListener() {
                    @Override
                    public void onExecuting() {
                        Log.d(RxCallReceiver.class.getSimpleName(), "onExecuting");
                    }

                    @Override
                    public void onSuccess(ArrayList<?> list) {
                        Log.d(RxCallReceiver.class.getSimpleName(), "onSuccess " + list.size());
                        if (!emitter.isDisposed())
                            emitter.onSuccess((List<Email>) list);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        Log.d(RxCallReceiver.class.getSimpleName(), "onError " + msg);
                        if (!emitter.isDisposed())
                            emitter.onError(new Exception(msg));
                    }

                    @Override
                    public void onCanceled() {
                        if (!emitter.isDisposed())
                            emitter.onError(new Exception("onCanceled"));
                        Log.d(RxCallReceiver.class.getSimpleName(), "onCanceled");

                    }
                });
            }
        });
    }
}
