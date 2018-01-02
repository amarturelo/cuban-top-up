package com.wirelesskings.data.cache.impl;

import android.os.Looper;

import com.wirelesskings.data.cache.FatherCache;
import com.wirelesskings.data.model.RealmFather;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;

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
        return Observable.defer(
                new Callable<ObservableSource<? extends RealmFather>>() {
                    @Override
                    public ObservableSource<? extends RealmFather> call() throws Exception {
                        return Observable.using(
                                new Callable<Realm>() {
                                    @Override
                                    public Realm call() throws Exception {
                                        return Realm.getDefaultInstance();
                                    }
                                }
                                , new Function<Realm, ObservableSource<? extends List<RealmFather>>>() {
                                    @Override
                                    public ObservableSource<? extends List<RealmFather>> apply(Realm realm) throws Exception {
                                        return realm.where(RealmFather.class)
                                                .equalTo(RealmFather.WK_USER, wkUser)
                                                .findAllAsync()
                                                .asFlowable()
                                                .toObservable();
                                    }
                                }
                                , new Consumer<Realm>() {
                                    @Override
                                    public void accept(Realm realm) throws Exception {
                                        realm.close();
                                    }
                                })
                                .map(new Function<List<RealmFather>, RealmFather>() {
                                    @Override
                                    public RealmFather apply(List<RealmFather> realmFathers) throws Exception {
                                        return realmFathers.get(0);
                                    }
                                });
                    }
                }
        )
                .unsubscribeOn(AndroidSchedulers.from(Looper.myLooper()));
    }
}
