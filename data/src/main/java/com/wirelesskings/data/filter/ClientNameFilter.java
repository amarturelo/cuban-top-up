package com.wirelesskings.data.filter;

/**
 * Created by alberto on 17/01/18.
 */

public class ClientNameFilter implements Filter {
    private String name;

    public ClientNameFilter() {
    }

    public String getName() {
        return name;
    }

    public ClientNameFilter setName(String name) {
        this.name = name;
        return this;
    }
}
