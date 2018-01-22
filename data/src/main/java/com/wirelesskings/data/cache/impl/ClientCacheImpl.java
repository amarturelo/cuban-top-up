package com.wirelesskings.data.cache.impl;

import com.wirelesskings.data.cache.ClientCache;
import com.wirelesskings.data.model.RealmClient;
import com.wirelesskings.data.model.RealmSeller;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by alberto on 22/01/18.
 */

public class ClientCacheImpl implements ClientCache {
    @Override
    public Observable<List<RealmClient>> getAll(String wkUser) {
        return Observable.create(new ObservableOnSubscribe<List<RealmClient>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<RealmClient>> emitter) throws Exception {
                final Realm observableRealm = Realm.getDefaultInstance();
                final RealmResults<RealmClient> results = observableRealm.where(RealmClient.class)
                        .findAllAsync();

                final RealmChangeListener<RealmResults<RealmClient>> listener = new RealmChangeListener<RealmResults<RealmClient>>() {
                    @Override
                    public void onChange(RealmResults<RealmClient> realmPromotions) {
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
