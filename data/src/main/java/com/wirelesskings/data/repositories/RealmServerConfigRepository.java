package com.wirelesskings.data.repositories;

import com.wirelesskings.data.model.internal.RealmServerConfig;
import com.wirelesskings.data.model.mapper.ServerConfigDataMapper;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.domain.repositories.ServerConfigRepositoy;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
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
        return Single.create(new SingleOnSubscribe<RealmServerConfig>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<RealmServerConfig> e) throws Exception {
                RealmResults<RealmServerConfig> configs = Realm.getDefaultInstance().where(RealmServerConfig.class).findAll();
                RealmServerConfig config = null;
                if (configs.size() > 0) {
                    config = configs.first();
                    e.onSuccess(Realm.getDefaultInstance().copyFromRealm(config));
                } else
                    e.onSuccess(new RealmServerConfig());
            }
        }).map(new Function<RealmServerConfig, ServerConfig>() {
            @Override
            public ServerConfig apply(@NonNull RealmServerConfig realmServerConfig) throws Exception {
                return serverConfigDataMapper.transform(realmServerConfig);
            }
        });
    }

    @Override
    public Completable save(final String username, final String password) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter e) throws Exception {
                final RealmServerConfig config = new RealmServerConfig(username, password);
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(config);
                    }
                });
            }
        });
    }
}
