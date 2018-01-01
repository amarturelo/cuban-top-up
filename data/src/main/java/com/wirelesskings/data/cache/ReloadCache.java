package com.wirelesskings.data.cache;

import com.wirelesskings.data.model.RealmReload;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by alberto on 30/12/17.
 */

public interface ReloadCache {
    void put(RealmReload realmReload);

    void put(List<RealmReload> realmReloads);

    Observable<List<RealmReload>> get();
}
