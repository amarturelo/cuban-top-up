package com.wirelesskings.data.repositories;

import com.wirelesskings.data.model.RealmServerConfig;
import com.wirelesskings.data.model.mapper.ServerConfigDataMapper;
import com.wirelesskings.wkreload.domain.model.ServerConfig;
import com.wirelesskings.wkreload.domain.repositories.ServerConfigRepositoy;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Alberto on 18/10/2017.
 */

public class RealmServerConfigRepository implements ServerConfigRepositoy {

    private ServerConfigDataMapper serverConfigDataMapper;

    public RealmServerConfigRepository(ServerConfigDataMapper serverConfigDataMapper) {
        this.serverConfigDataMapper = serverConfigDataMapper;
    }

    @Override
    public Single<ServerConfig> get() {
        return Single.create((SingleOnSubscribe<RealmServerConfig>) e -> {
            RealmResults<RealmServerConfig> configs = Realm.getDefaultInstance().where(RealmServerConfig.class).findAll();
            RealmServerConfig config = null;
            if (configs.size() > 0) {
                config = configs.first();
                e.onSuccess(Realm.getDefaultInstance().copyFromRealm(config));
            }
        }).map(realmServerConfig -> serverConfigDataMapper.transform(realmServerConfig));
    }

    @Override
    public Completable save(String username, String password) {
        return Completable.create(e -> {
            RealmServerConfig config = new RealmServerConfig(username, password);
            Realm.getDefaultInstance().executeTransaction(realm -> realm.insertOrUpdate(config));
        });
    }
}