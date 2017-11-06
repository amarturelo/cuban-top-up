package com.wirelesskings.data.repositories;

import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.data.model.mapper.OwnerDataMapper;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.wkreload.domain.model.CollectionChange;
import com.wirelesskings.wkreload.domain.model.Owner;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.OwnerRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Alberto on 28/10/2017.
 */

public class OwnerRepositoryImpl implements OwnerRepository {

    private ReloadDataMapper reloadDataMapper;
    private OwnerDataMapper ownerDataMapper;

    RealmResults<RealmOwner> owner;


    public OwnerRepositoryImpl(ReloadDataMapper reloadDataMapper, OwnerDataMapper ownerDataMapper) {
        this.reloadDataMapper = reloadDataMapper;
        this.ownerDataMapper = ownerDataMapper;
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
    public Observable<Owner> owner() {
        return Observable.create(new ObservableOnSubscribe<RealmOwner>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<RealmOwner> emitter) throws Exception {
                Realm realm = Realm.getDefaultInstance();
                owner = realm.where(RealmOwner.class).findAllAsync();
                owner.addChangeListener(new RealmChangeListener<RealmResults<RealmOwner>>() {
                    @Override
                    public void onChange(RealmResults<RealmOwner> realmOwners) {
                        if (realmOwners.size() > 0)
                            emitter.onNext(realm.copyFromRealm(realmOwners.first()));
                        else
                            emitter.onNext(new RealmOwner());
                    }
                });
            }
        }).map(realmOwner -> ownerDataMapper.transform(realmOwner));
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
}
