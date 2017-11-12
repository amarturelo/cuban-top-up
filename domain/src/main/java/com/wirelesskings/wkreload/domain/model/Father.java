package com.wirelesskings.wkreload.domain.model;

/**
 * Created by Alberto on 01/11/2017.
 */

public class Father {
    private String name;
    private String amount;
    private String cost;

    public Father() {
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public Father setCost(String cost) {
        this.cost = cost;
        return this;
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
