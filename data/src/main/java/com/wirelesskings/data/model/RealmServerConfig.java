package com.wirelesskings.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 18/10/2017.
 */

public class RealmServerConfig extends RealmObject {

    @PrimaryKey
    private String email;
    private String password;

    public RealmServerConfig() {
    }

    public RealmServerConfig(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
