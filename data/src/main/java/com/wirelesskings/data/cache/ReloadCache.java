package com.wirelesskings.data.cache;

import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.wkreload.domain.filter.Filter;
import com.wirelesskings.wkreload.domain.model.Reload;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by alberto on 30/12/17.
 */

public interface ReloadCache {
    void put(RealmReload realmReload);

    void put(List<RealmReload> realmReloads);

    Observable<List<RealmReload>> get();

    Single<RealmReload> getById(String id);

    Flowable<List<RealmReload>> getByFilters(List<Filter> filters);
}
