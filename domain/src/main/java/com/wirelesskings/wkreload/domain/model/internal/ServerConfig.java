package com.wirelesskings.wkreload.domain.model.internal;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ServerConfig {

    private String email;
    private String password;

    private Credentials credentials;

    public ServerConfig() {
    }

    public ServerConfig(String email, String password, Credentials credentials) {
        this.email = email;
        this.password = password;
        this.credentials = credentials;
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

    public Credentials getCredentials() {
        return credentials;
    }

    public ServerConfig setCredentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public boolean isEmpty() {
        return email == null && password == null;
    }
}
