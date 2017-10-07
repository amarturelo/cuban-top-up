package com.wirelesskings.data.mail.rx;

import android.util.Log;

import com.wirelesskings.data.mail.async.CallReceiver;
import com.wirelesskings.data.mail.async.OnStateChangedListener;
import com.wirelesskings.data.mail.rx.funtions.RetryWithDelay;
import com.wirelesskings.data.mail.settings.Setting;

import java.util.ArrayList;

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

    public RxCallReceiver(int t, int time, Setting setting) {
        this.t = t;
        this.time = time;
        this.setting = setting;
        this.callReceiver = new CallReceiver(setting);
    }

    public Single receiver(String filter) {
        return doReceiver(filter).retryWhen(new RetryWithDelay(t, time));
    }

    private Single doReceiver(String filter) {
        return Single.create(e -> callReceiver.execute(filter, new OnStateChangedListener() {
            @Override
            public void onExecuting() {

            }

            @Override
            public void onSuccess(ArrayList<?> list) {
                Log.d("RxCallReceiver",list.toString());
            }

            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onCanceled() {

            }
        }));
    }
}
