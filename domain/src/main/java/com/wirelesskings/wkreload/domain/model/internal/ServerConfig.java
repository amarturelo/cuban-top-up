package com.wirelesskings.wkreload.domain.model.internal;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ServerConfig {
    private String email;
    private String password;

    public ServerConfig() {
    }

    public ServerConfig(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public ServerConfig setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ServerConfig setPassword(String password) {
        this.password = password;
        return this;
    }
}
