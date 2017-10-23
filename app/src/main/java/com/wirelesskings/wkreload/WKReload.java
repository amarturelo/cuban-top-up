package com.wirelesskings.wkreload;

import android.app.Application;

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
    }
}
