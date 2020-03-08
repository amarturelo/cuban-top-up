package com.wirelesskings.wkreload.domain.model.internal;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ServerConfig {

    private String email;
    private String password;

    private boolean active = false;


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

    public boolean isActive() {
        return active;
    }

    public ServerConfig setActive(boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerConfig that = (ServerConfig) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null)
            return false;
        return credentials != null ? credentials.equals(that.credentials) : that.credentials == null;

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
        return result;
    }
}
