package com.wirelesskings.wkreload.mailmiddleware.mail.rx;

import android.util.Log;

import com.wirelesskings.wkreload.mailmiddleware.mail.async.CallReceiver;
import com.wirelesskings.wkreload.mailmiddleware.mail.async.OnStateChangedListener;
import com.wirelesskings.wkreload.mailmiddleware.mail.model.Email;
import com.wirelesskings.wkreload.mailmiddleware.mail.rx.funtions.RetryWithDelay;
import com.wirelesskings.wkreload.mailmiddleware.mail.settings.Setting;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
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

    public Maybe<List<Email>> receiver(String filter) {
        return doReceiver(filter).retryWhen(new RetryWithDelay(t, time));
    }

    private Maybe<List<Email>> doReceiver(String filter) {
        return Maybe.create(e -> callReceiver.execute(filter, new OnStateChangedListener() {
            @Override
            public void onExecuting() {

            }

            @Override
            public void onSuccess(ArrayList<?> list) {
                if (list != null && list.size() > 0)
                    e.onSuccess((List<Email>) list);
                else
                    e.onComplete();

            }

            @Override
            public void onError(int code, String msg) {
                e.onError(new Exception(msg));
            }

            @Override
            public void onCanceled() {
                e.onError(new Exception("onCanceled"));
            }
        }));
    }
}
