package com.wirelesskings.data.cache;

import com.wirelesskings.data.model.RealmClient;
import com.wirelesskings.data.model.RealmFather;
import com.wirelesskings.data.model.RealmOwner;
import com.wirelesskings.data.model.RealmPromotion;
import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.data.model.RealmSeller;

import io.realm.Realm;

/**
 * Created by Alberto on 04/11/2017.
 */

public class OwnerCacheImp implements OwnerCache {
    @Override
    public void put(RealmOwner realmOwner) {
        //clear();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(realmOwner);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void clear() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(RealmOwner.class);
        realm.delete(RealmPromotion.class);
        realm.delete(RealmFather.class);
        realm.delete(RealmReload.class);
        realm.delete(RealmClient.class);
        realm.delete(RealmSeller.class);
        realm.commitTransaction();
        realm.close();
    }
}
