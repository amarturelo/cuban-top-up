package com.wirelesskings.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 01/11/2017.
 */

public class RealmFather extends RealmObject {
    @PrimaryKey
    private String name;
    private String amount;

    public RealmFather() {
    }

    public String getName() {
        return name;
    }

    public RealmFather setName(String name) {
        this.name = name;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public RealmFather setAmount(String amount) {
        this.amount = amount;
        return this;
    }
}
