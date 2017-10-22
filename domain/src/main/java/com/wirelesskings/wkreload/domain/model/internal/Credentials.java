package com.wirelesskings.wkreload.domain.model.internal;

/**
 * Created by Alberto on 23/10/2017.
 */

public class Credentials {
    private String username;
    private String password;

    public Credentials() {
    }

    public String getUsername() {
        return username;
    }

    public Credentials setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Credentials setPassword(String password) {
        this.password = password;
        return this;
    }
}
