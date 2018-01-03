package com.wirelesskings.data.cache.impl;

import android.os.Looper;

import com.wirelesskings.data.cache.FatherCache;
import com.wirelesskings.data.model.RealmFather;
import com.wirelesskings.data.model.RealmPromotion;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by alberto on 31/12/17.
 */

public class FatherCacheImpl implements FatherCache {

    @Override
    public void put(RealmFather realmFather) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmFather);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public Observable<RealmFather> fatherByUser(final String wkUser) {
        return Observable.create(new ObservableOnSubscribe<RealmFather>() {
            @Override
            public void subscribe(final ObservableEmitter<RealmFather> emitter) throws Exception {
                final Realm observableRealm = Realm.getDefaultInstance();
                final RealmResults<RealmFather> results = observableRealm.where(RealmFather.class)
                        .equalTo(RealmFather.WK_USER, wkUser)
                        .findAllAsync();

                final RealmChangeListener<RealmResults<RealmFather>> listener = new RealmChangeListener<RealmResults<RealmFather>>() {
                    @Override
                    public void onChange(RealmResults<RealmFather> realmFathers) {
                        if (results.isValid() && results.isLoaded()) {
                            RealmFather realmFather = realmFathers.first();
                            if (realmFather != null)
                                emitter.onNext(observableRealm.copyFromRealm(realmFather));
                        }
                    }
                };

                emitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (results.isValid()) {
                            results.removeChangeListener(listener);
                        }
                        observableRealm.close();
                    }
                }));

                results.addChangeListener(listener);
            }
        });
    }
}
