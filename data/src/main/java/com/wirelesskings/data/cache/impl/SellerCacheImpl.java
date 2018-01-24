package com.wirelesskings.data.cache.impl;

import com.wirelesskings.data.cache.SellerCache;
import com.wirelesskings.data.model.RealmSeller;

import java.util.List;

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

public class SellerCacheImpl implements SellerCache {
    @Override
    public Observable<List<RealmSeller>> getAll() {
        return Observable.create(new ObservableOnSubscribe<List<RealmSeller>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<RealmSeller>> emitter) throws Exception {
                final Realm observableRealm = Realm.getDefaultInstance();
                final RealmResults<RealmSeller> results = observableRealm.where(RealmSeller.class)
                        .findAllAsync();

                final RealmChangeListener<RealmResults<RealmSeller>> listener = new RealmChangeListener<RealmResults<RealmSeller>>() {
                    @Override
                    public void onChange(RealmResults<RealmSeller> realmPromotions) {
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
