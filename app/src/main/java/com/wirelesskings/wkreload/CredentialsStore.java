package com.wirelesskings.wkreload;

import com.wirelesskings.data.model.internal.RealmServerConfig;

import io.realm.Realm;

/**
 * Created by Alberto on 03/11/2017.
 */

public class CredentialsStore {
    public boolean hasCredentials() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(RealmServerConfig.class).count() != 0;
    }

    public RealmServerConfig getCredentials() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(realm.where(RealmServerConfig.class).findFirst());
    }

    public void put(RealmServerConfig realmServerConfig) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmServerConfig);
        realm.commitTransaction();
        realm.close();
    }

    public void clear() {
        Realm realm = Realm.getDefaultInstance();
        realm.delete(RealmServerConfig.class);
        realm.close();
    }
}
