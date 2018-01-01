package com.wirelesskings.data.repositories;

import com.wirelesskings.data.cache.ReloadCache;
import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.data.model.mapper.ReloadDataMapper;
import com.wirelesskings.wkreload.domain.model.Reload;
import com.wirelesskings.wkreload.domain.repositories.ReloadRepository;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.realm.Realm;

/**
 * Created by alberto on 30/12/17.
 */

public class ReloadRepositoryImpl implements ReloadRepository {

    private ReloadCache reloadCache;

    public ReloadRepositoryImpl(ReloadCache reloadCache) {
        this.reloadCache = reloadCache;
    }

    @Override
    public Observable<List<Reload>> reloads() {
        return reloadCache.get()
                .map(new Function<List<RealmReload>, List<Reload>>() {
                    @Override
                    public List<Reload> apply(List<RealmReload> realmReloads) throws Exception {
                        return ReloadDataMapper.transform(realmReloads);
                    }
                });
    }
}
