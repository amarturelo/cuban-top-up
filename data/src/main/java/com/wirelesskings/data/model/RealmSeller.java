package com.wirelesskings.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 23/10/2017.
 */

public class RealmSeller extends RealmObject {

    @PrimaryKey
    private String name;
    private String amount;

    private String wkUser;

    public RealmSeller() {
    }

    public String getName() {
        return name;
    }

    public RealmSeller setName(String name) {
        this.name = name;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public RealmSeller setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public String getWkUser() {
        return wkUser;
    }

    public RealmSeller setWkUser(String wkUser) {
        this.wkUser = wkUser;
        return this;
    }
}
