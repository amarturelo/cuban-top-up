package com.wirelesskings.data.repositories;

import android.util.Log;

import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.wkreload.domain.model.CollectionChange;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.ReloadRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Alberto on 28/10/2017.
 */

public class ReloadRepositoryImpl implements ReloadRepository {

    private ReloadDataMapper reloadDataMapper;


    public ReloadRepositoryImpl(ReloadDataMapper reloadDataMapper) {
        this.reloadDataMapper = reloadDataMapper;
    }

    @Override
    public Observable<CollectionChange<Reload>> reloads() {
        return Observable.create(new ObservableOnSubscribe<CollectionChange<Reload>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<CollectionChange<Reload>> emitter) throws Exception {
                CollectionChange<Reload> collectionChange = new CollectionChange<Reload>();


                Realm realm = Realm.getDefaultInstance();
                RealmResults<RealmReload> result = realm.where(RealmReload.class).findAllAsync();

                realm.where(RealmReload.class).findAllAsync().addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<RealmReload>>() {
                    @Override
                    public void onChange(RealmResults<RealmReload> realmReloads, OrderedCollectionChangeSet changeSet) {
                        if (changeSet != null) {

                            if (changeSet.getInsertions() != null && changeSet.getInsertions().length > 0) {
                                List<Reload> insertions = new ArrayList<>();
                                for (int i :
                                        changeSet.getInsertions()) {
                                    insertions.add(reloadDataMapper.transform(realm.copyFromRealm(realmReloads.get(i))));
                                }
                                collectionChange.setInserted(insertions);
                            }

                            if (changeSet.getChanges() != null && changeSet.getChanges().length > 0) {
                                List<Reload> changed = new ArrayList<>();
                                for (int i :
                                        changeSet.getChanges()) {
                                    changed.add(reloadDataMapper.transform(realm.copyFromRealm(realmReloads.get(i))));
                                }
                                collectionChange.setInserted(changed);
                            }

                            if (changeSet.getDeletions() != null && changeSet.getDeletions().length > 0) {
                                List<Reload> deletions = new ArrayList<>();
                                for (int i :
                                        changeSet.getDeletions()) {
                                    deletions.add(reloadDataMapper.transform(realm.copyFromRealm(realmReloads.get(i))));
                                }
                                collectionChange.setInserted(deletions);
                            }
                        } else {
                            List<Reload> insertions = new ArrayList<>();
                            insertions.addAll(reloadDataMapper.transform(realm.copyFromRealm(realmReloads)));
                            collectionChange.setInserted(insertions);
                        }
                        emitter.onNext(collectionChange);
                    }
                });
            }
        });
    }

    @Override
    public Observable<Long> debit() {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<RealmReload> result = realm.where(RealmReload.class).findAllAsync();

                result.addChangeListener(new RealmChangeListener<RealmResults<RealmReload>>() {
                    @Override
                    public void onChange(RealmResults<RealmReload> realmReloads) {
                        long debit = 0;

                        for (RealmReload realmReload :
                                realmReloads) {
                            debit += realmReload.getAmount() * realmReload.getCount();
                        }
                        emitter.onNext(debit);
                    }
                });
            }
        });
    }

    @Override
    public Single<Reload> reloadById(String id) {
        return Single.create((SingleOnSubscribe<RealmReload>) emitter -> {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<RealmReload> result = realm.where(RealmReload.class).findAll();
            emitter.onSuccess(realm.copyFromRealm(result.first()));
        }).map(realmReload -> reloadDataMapper.transform(realmReload));
    }

    @Override
    public Completable reload(String client_name, String client_number, int count, int amount) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Exception {
                emitter.onComplete();
            }
        });
    }
}
