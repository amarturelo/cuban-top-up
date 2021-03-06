package com.wirelesskings.data.model.internal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 23/10/2017.
 */

public class RealmCredentials extends RealmObject {

    @PrimaryKey
    private String username;
    private String password;
    private String token;

    public RealmCredentials() {
    }

    public RealmCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public RealmCredentials setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RealmCredentials setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getToken() {
        return token;
    }

    public RealmCredentials setToken(String token) {
        this.token = token;
        return this;
    }
}
