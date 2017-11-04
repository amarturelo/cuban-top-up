package com.wirelesskings.data.cache;

import com.wirelesskings.data.model.RealmOwner;

import io.realm.Realm;

/**
 * Created by Alberto on 04/11/2017.
 */

public interface OwnerCache {
    void put(RealmOwner realmOwner);

    void clear();

}
