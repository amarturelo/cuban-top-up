package com.wirelesskings.wkreload.domain.model;


/**
 * Created by Alberto on 23/10/2017.
 */

public class Seller {

    private String name;
    private String amount;

    public Seller() {
    }

    public String getName() {
        return name;
    }

    public Seller setName(String name) {
        this.name = name;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public Seller setAmount(String amount) {
        this.amount = amount;
        return this;
    }
}
