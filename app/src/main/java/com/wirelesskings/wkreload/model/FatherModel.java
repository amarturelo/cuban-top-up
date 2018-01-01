package com.wirelesskings.wkreload.model;

/**
 * Created by alberto on 31/12/17.
 */

public class FatherModel {
    private String name;
    private String count;

    public FatherModel() {
    }

    public String getName() {
        return name;
    }

    public FatherModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getCount() {
        return count;
    }

    public FatherModel setCount(String count) {
        this.count = count;
        return this;
    }
}
