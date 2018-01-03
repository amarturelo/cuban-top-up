package com.wirelesskings.data.cache.impl;

import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import com.wirelesskings.data.cache.PromotionCache;
import com.wirelesskings.data.model.RealmFather;
import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.wkreload.domain.model.Promotion;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.rx.CollectionChange;

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
    public Single<RealmPromotion> get(final String id) {
        return Single.create(new SingleOnSubscribe<RealmPromotion>() {
            @Override
            public void subscribe(SingleEmitter<RealmPromotion> emitter) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                emitter.onSuccess(realm.where(RealmPromotion.class)
                        .equalTo(RealmPromotion.ID, id)
                        .findAll().first());
            }
        });
    }

    @Override
    public Observable<List<RealmPromotion>> getAll(String wkUser) {
        return Observable.create(new ObservableOnSubscribe<List<RealmPromotion>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<RealmPromotion>> emitter) throws Exception {
                final Realm observableRealm = Realm.getDefaultInstance();
                final RealmResults<RealmPromotion> results = observableRealm.where(RealmPromotion.class)
                        .findAllAsync();

                final RealmChangeListener<RealmResults<RealmPromotion>> listener = new RealmChangeListener<RealmResults<RealmPromotion>>() {
                    @Override
                    public void onChange(RealmResults<RealmPromotion> realmPromotions) {
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

    /*@Override
    public Flowable<List<RealmPromotion>> getAll(final String wkUser) {

        return Flowable.defer(new Callable<Publisher<? extends List<RealmPromotion>>>() {
            @Override
            public Publisher<? extends List<RealmPromotion>> call() throws Exception {
                return Flowable.using(new Callable<Pair<Realm, Looper>>() {
                                          @Override
                                          public Pair<Realm, Looper> call() throws Exception {
                                              return new Pair<>(Realm.getDefaultInstance(), Looper.myLooper());
                                          }
                                      }
                        , new Function<Pair<Realm, Looper>, Publisher<? extends List<RealmPromotion>>>() {
                            @Override
                            public Publisher<? extends List<RealmPromotion>> apply(Pair<Realm, Looper> pair) throws Exception {
                                return pair.first.where(RealmPromotion.class).findAll()
                                        .asFlowable();
                            }
                        }
                        , new Consumer<Pair<Realm, Looper>>() {
                            @Override
                            public void accept(Pair<Realm, Looper> pair) throws Exception {
                                close(pair.first, pair.second);
                            }
                        });
            }
        })
                .unsubscribeOn(AndroidSchedulers.from(Looper.myLooper()));
    }*/

    protected void close(final Realm realm, Looper looper) {
        if (realm == null || looper == null) {
            return;
        }
        new Handler(looper).post(new Runnable() {
            @Override
            public void run() {
                if (!realm.isClosed())
                    realm.close();
            }
        });
    }

    private static Observable<Realm> getManagedRealm() {
        return Observable.create(new ObservableOnSubscribe<Realm>() {
            @Override
            public void subscribe(ObservableEmitter<Realm> emitter) throws Exception {
                final Realm realm = Realm.getDefaultInstance();

                emitter.setDisposable(new MainThreadDisposable() {
                    @Override
                    protected void onDispose() {
                        realm.close();
                    }
                });
                emitter.onNext(realm);
            }
        });
    }
   /* private static Observable<Realm> getManagedRealm() {
        return Observable.create(new Observable.OnSubscribe<Realm>() {
            @Override
            public void call(final Subscriber<? super Realm> subscriber) {
                final Realm realm = Realm.getDefaultInstance();
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        realm.close();
                    }
                }));
                subscriber.onNext(realm);
            }
        });
    }*/

    @Override
    public void put(List<RealmPromotion> realmPromotions) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmPromotions);
        realm.commitTransaction();
        realm.close();
    }
}
