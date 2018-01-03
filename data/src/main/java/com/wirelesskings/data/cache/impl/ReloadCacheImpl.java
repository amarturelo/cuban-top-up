package com.wirelesskings.data.cache.impl;

import android.os.Looper;

import com.wirelesskings.data.cache.ReloadCache;
import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.data.model.RealmReload;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

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
        return Observable.create(new ObservableOnSubscribe<List<RealmReload>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<RealmReload>> emitter) throws Exception {
                final Realm observableRealm = Realm.getDefaultInstance();
                final RealmResults<RealmReload> results = observableRealm.where(RealmReload.class)
                        .findAllAsync();

                final RealmChangeListener<RealmResults<RealmReload>> listener = new RealmChangeListener<RealmResults<RealmReload>>() {
                    @Override
                    public void onChange(RealmResults<RealmReload> realmPromotions) {
                        if (results.isValid() && results.isLoaded()) {
                            emitter.onNext(observableRealm.copyFromRealm(results));
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

