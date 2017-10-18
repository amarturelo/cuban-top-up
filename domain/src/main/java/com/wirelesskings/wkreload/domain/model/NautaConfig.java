package com.wirelesskings.wkreload.domain.model;

/**
 * Created by Alberto on 18/10/2017.
 */

public class NautaConfig {
    private String email;
    private String password;

    public NautaConfig(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
