package com.wirelesskings.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 23/10/2017.
 */

public class RealmClient extends RealmObject {
    @PrimaryKey
    private String number;
    private String name;

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
}
