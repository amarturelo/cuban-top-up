package com.wirelesskings.data.cache.impl;

import android.os.Looper;

import com.wirelesskings.data.cache.ReloadCache;
import com.wirelesskings.data.model.RealmReload;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;

/**
 * Created by alberto on 30/12/17.
 */

public class ReloadCacheImpl implements ReloadCache {

    public ReloadCacheImpl() {
    }

    @Override
    public void put(RealmReload realmReload) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmReload);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void put(List<RealmReload> realmReloads) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmReloads);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public Observable<List<RealmReload>> get() {
        return Observable.defer(new Callable<ObservableSource<? extends List<RealmReload>>>() {
            @Override
            public ObservableSource<? extends List<RealmReload>> call() throws Exception {
                return Observable.using(new Callable<Realm>() {
                                            @Override
                                            public Realm call() throws Exception {
                                                return Realm.getDefaultInstance();
                                            }
                                        }
                        , new Function<Realm, ObservableSource<? extends List<RealmReload>>>() {
                            @Override
                            public ObservableSource<? extends List<RealmReload>> apply(Realm realm) throws Exception {
                                return realm.where(RealmReload.class).findAllAsync().asFlowable().toObservable();
                            }
                        }
                        , new Consumer<Realm>() {
                            @Override
                            public void accept(Realm realm) throws Exception {
                                realm.close();
                            }
                        });
            }
        })
                .unsubscribeOn(AndroidSchedulers.from(Looper.myLooper()));
    }
}
