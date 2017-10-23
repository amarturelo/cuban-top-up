package com.wirelesskings.wkreload.domain.model;


/**
 * Created by Alberto on 23/10/2017.
 */

public class Client  {
    private String number;
    private String name;

    public Client() {
    }

    public String getNumber() {
        return number;
    }

    public Client setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getName() {
        return name;
    }

    public Client setName(String name) {
        this.name = name;
        return this;
    }
}
