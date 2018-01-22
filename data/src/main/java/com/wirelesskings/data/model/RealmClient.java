package com.wirelesskings.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 23/10/2017.
 */

public class RealmClient extends RealmObject {
    public static final String NAME = "name";
    public static final String NUMBER = "number";

    @PrimaryKey
    private String number;
    private String name;

    private String wkUser;

    public RealmClient() {
    }

    public String getNumber() {
        return number;
    }

    public RealmClient setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getName() {
        return name;
    }

    public RealmClient setName(String name) {
        this.name = name;
        return this;
    }

    public String getWkUser() {
        return wkUser;
    }

    public RealmClient setWkUser(String wkUser) {
        this.wkUser = wkUser;
        return this;
    }
}
