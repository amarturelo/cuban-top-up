package com.wirelesskings.data.cache.impl;

import android.os.Handler;
import android.os.Looper;

import io.realm.Realm;

public class RealmCache {
    protected void close(final Realm realm, Looper looper) {
        if (realm == null || looper == null) {
            return;
        }
        new Handler(looper).post(new Runnable() {
            @Override
            public void run() {
                realm.close();
            }
        });
        //realm.close();
    }
}
