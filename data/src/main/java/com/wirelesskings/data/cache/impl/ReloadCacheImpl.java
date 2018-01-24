package com.wirelesskings.data.cache.impl;

import android.os.Looper;
import android.util.Pair;

import com.annimon.stream.Stream;
import com.wirelesskings.data.cache.ReloadCache;
import com.wirelesskings.data.model.RealmClient;
import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.wkreload.domain.filter.ClientNameFilter;
import com.wirelesskings.wkreload.domain.filter.ClientNumberFilter;
import com.wirelesskings.wkreload.domain.filter.DateFilter;
import com.wirelesskings.wkreload.domain.filter.Filter;
import com.wirelesskings.wkreload.domain.filter.ReloadStateFilter;
import com.wirelesskings.wkreload.domain.model.Reload;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by alberto on 30/12/17.
 */

public class ReloadCacheImpl extends RealmCache implements ReloadCache {

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

    @Override
    public Flowable<List<RealmReload>> getByFilters(final List<Filter> filters) {
        return Flowable.defer(new Callable<Publisher<? extends List<RealmReload>>>() {
            @Override
            public Publisher<? extends List<RealmReload>> call() throws Exception {
                return Flowable.using(new Callable<Pair<Realm, Looper>>() {
                                          @Override
                                          public Pair<Realm, Looper> call() throws Exception {
                                              return new Pair<>(Realm.getDefaultInstance(), Looper.myLooper());
                                          }
                                      }
                        , new Function<Pair<Realm, Looper>, Publisher<? extends List<RealmReload>>>() {
                            @Override
                            public Publisher<? extends List<RealmReload>> apply(final Pair<Realm, Looper> pair) throws Exception {
                                final RealmQuery<RealmReload> query = pair.first.where(RealmReload.class);
                                Stream.of(filters)
                                        .forEach(new com.annimon.stream.function.Consumer<Filter>() {
                                            @Override
                                            public void accept(Filter filter) {
                                                if (filter instanceof ReloadStateFilter)
                                                    query.equalTo(RealmReload.STATUS, ((ReloadStateFilter) filter).getState());
                                                if (filter instanceof ClientNameFilter)
                                                    query.equalTo(RealmReload.CLIENT + "." + RealmClient.NAME, ((ClientNameFilter) filter).getName());
                                                if (filter instanceof ClientNumberFilter)
                                                    query.equalTo(RealmReload.CLIENT + "." + RealmClient.NUMBER, ((ClientNumberFilter) filter).getClientNumber());
                                                if (filter instanceof DateFilter)
                                                    if (((DateFilter) filter).getStart() != null && ((DateFilter) filter).getEnd() != null)
                                                        query.between(RealmReload.DATE, ((DateFilter) filter).getStart(), ((DateFilter) filter).getEnd());
                                                    else if (((DateFilter) filter).getStart() != null)
                                                        query.greaterThanOrEqualTo(RealmReload.DATE, ((DateFilter) filter).getStart());
                                                    else if (((DateFilter) filter).getEnd() != null)
                                                        query.lessThanOrEqualTo(RealmReload.DATE, ((DateFilter) filter).getEnd());
                                            }
                                        });

                                RealmResults<RealmReload> realmProfiles = query
                                        .findAll();

                                return realmProfiles.<RealmReload>asFlowable()
                                        .map(new Function<RealmResults<RealmReload>, List<RealmReload>>() {
                                            @Override
                                            public List<RealmReload> apply(RealmResults<RealmReload> realmReloads) throws Exception {
                                                return pair.first.copyFromRealm(realmReloads);
                                            }
                                        })
                                        ;
                            }
                        }
                        , new Consumer<Pair<Realm, Looper>>() {
                            @Override
                            public void accept(Pair<Realm, Looper> pair) throws Exception {
                                close(pair.first, pair.second);
                            }
                        })
                        .unsubscribeOn(AndroidSchedulers.from(Looper.myLooper()));
            }
        })
                ;
    }

    @Override
    public Single<RealmReload> getById(final String id) {
        return Single.create(new SingleOnSubscribe<RealmReload>() {
            @Override
            public void subscribe(SingleEmitter<RealmReload> emitter) throws Exception {
                final Realm observableRealm = Realm.getDefaultInstance();
                emitter.onSuccess(observableRealm.copyFromRealm(observableRealm
                        .where(RealmReload.class)
                        .equalTo(RealmReload.ID, id)
                        .findFirst()));
            }
        });
    }
}

