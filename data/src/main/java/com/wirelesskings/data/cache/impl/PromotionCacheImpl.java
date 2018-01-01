package com.wirelesskings.data.cache.impl;

import android.os.Looper;

import com.wirelesskings.data.cache.PromotionCache;
import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.wkreload.domain.model.Promotion;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.realm.Realm;

/**
 * Created by alberto on 30/12/17.
 */

public class PromotionCacheImpl implements PromotionCache {

    public PromotionCacheImpl() {
    }

    @Override
    public void put(RealmPromotion realmPromotion) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmPromotion);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public Single<RealmPromotion> get(String id) {
        return Single.create(new SingleOnSubscribe<RealmPromotion>() {
            @Override
            public void subscribe(SingleEmitter<RealmPromotion> emitter) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                emitter.onSuccess(realm.where(RealmPromotion.class).findAll().first());
            }
        });
    }

    @Override
    public Observable<List<RealmPromotion>> getAll(final String wkUser) {
        return Observable.defer(new Callable<ObservableSource<? extends List<RealmPromotion>>>() {
            @Override
            public ObservableSource<? extends List<RealmPromotion>> call() throws Exception {
                return Observable.using(new Callable<Realm>() {
                                            @Override
                                            public Realm call() throws Exception {
                                                return Realm.getDefaultInstance();
                                            }
                                        }
                        , new Function<Realm, ObservableSource<? extends List<RealmPromotion>>>() {
                            @Override
                            public ObservableSource<? extends List<RealmPromotion>> apply(Realm realm) throws Exception {
                                return realm.where(RealmPromotion.class)
                                        .equalTo(RealmPromotion.WK_USER, wkUser)
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
                        });
            }
        })
                .unsubscribeOn(AndroidSchedulers.from(Looper.myLooper()));
    }

    @Override
    public void put(List<RealmPromotion> realmPromotions) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmPromotions);
        realm.commitTransaction();
        realm.close();
    }
}
