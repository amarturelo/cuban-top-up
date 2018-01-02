package com.wirelesskings.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 01/11/2017.
 */

public class RealmFather extends RealmObject {
    public static final String WK_USER = "wkUser";
    @PrimaryKey
    private String name;
    private String cost;

    private String wkUser;


    public RealmFather() {
    }

    public String getName() {
        return name;
    }

    public RealmFather setName(String name) {
        this.name = name;
        return this;
    }

    public String getCost() {
        return cost;
    }

    public RealmFather setCost(String cost) {
        this.cost = cost;
        return this;
    }

    public String getWkUser() {
        return wkUser;
    }

    public RealmFather setWkUser(String wkUser) {
        this.wkUser = wkUser;
        return this;
    }
}
