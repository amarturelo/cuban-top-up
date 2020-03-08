package com.wirelesskings.wkreload;

import android.app.Application;

import com.wirelesskings.data.model.RealmClient;
import com.wirelesskings.data.model.RealmReload;
import com.wirelesskings.data.model.RealmSeller;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Alberto on 18/10/2017.
 */

public class WKReload extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        realmInit();
    }

    private void realmInit() {
        Realm.init(getApplicationContext());

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("wic")
                //.encryptionKey(getKey())
                .schemaVersion(42)
                .deleteRealmIfMigrationNeeded()
                //.modules(new MySchemaModule())
                //.migration(new MyMigration())
                .build();

        Realm.setDefaultConfiguration(config);

        //insertData();
    }

    private void insertData() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmReload realmReload = new RealmReload()
                .setAmount(50)
                .setId("20")
                .setCount(2)
                .setDate(new Date())
                .setRealmClient(new RealmClient()
                        .setName("Alberto")
                        .setNumber("52950107"))
                .setRealmSeller(new RealmSeller()
                        .setName("Denis")
                        .setAmount("2100"));

        realm.insertOrUpdate(realmReload);
        realm.commitTransaction();
        realm.close();

    }
}
