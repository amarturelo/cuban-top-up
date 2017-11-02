package com.wirelesskings.wkreload.domain.model;

/**
 * Created by Alberto on 01/11/2017.
 */

public class Father {
    private String name;
    private String amount;

    public Father() {
    }

    public String getName() {
        return name;
    }

    public Father setName(String name) {
        this.name = name;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public Father setAmount(String amount) {
        this.amount = amount;
        return this;
    }
}
