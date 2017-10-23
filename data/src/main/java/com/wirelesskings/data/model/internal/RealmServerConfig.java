package com.wirelesskings.data.model.internal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 18/10/2017.
 */

public class RealmServerConfig extends RealmObject {

    @PrimaryKey
    private String email;
    private String password;

    private RealmCredentials realmCredentials;

    public RealmServerConfig() {
    }

    public RealmServerConfig(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public RealmServerConfig setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RealmServerConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public RealmCredentials getRealmCredentials() {
        return realmCredentials;
    }

    public RealmServerConfig setRealmCredentials(RealmCredentials realmCredentials) {
        this.realmCredentials = realmCredentials;
        return this;
    }
}
