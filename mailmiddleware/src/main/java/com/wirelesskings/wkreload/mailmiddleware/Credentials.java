package com.wirelesskings.wkreload.mailmiddleware;

import com.wirelesskings.wkreload.mailmiddleware.crypto.Crypto;

/**
 * Created by Alberto on 31/10/2017.
 */

public class Credentials {
    private String wk_user;
    private String wk_pass;
    private String user_nauta;
    private String token;

    public Credentials() {
    }

    public String getWk_user() {
        return wk_user;
    }

    public Credentials setWk_user(String wk_user) {
        this.wk_user = wk_user;
        return this;
    }

    public String getWk_pass() {
        return wk_pass;
    }

    public Credentials setWk_pass(String wk_pass) {
        this.wk_pass = wk_pass;
        return this;
    }

    public String getUser_nauta() {
        return user_nauta;
    }

    public Credentials setUser_nauta(String user_nauta) {
        this.user_nauta = user_nauta;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Credentials setToken(String token) {
        this.token = token;
        return this;
    }

    public String getSubject() {
        return "2bafc6c3270b0ee8f002e48f5f57773b";
    }

    public String getHashPass() throws Exception {
        return Crypto.hashPassword(wk_pass, token);
    }
}
