package com.wirelesskings.wkreload.domain.model;

/**
 * Created by Alberto on 01/11/2017.
 */

public class Owner {
    private String user_nauta;
    private String nauta_active;
    private Promotion promotion;
    private Father father;

    public Owner() {
    }

    public String getUser_nauta() {
        return user_nauta;
    }

    public Owner setUser_nauta(String user_nauta) {
        this.user_nauta = user_nauta;
        return this;
    }

    public String getNauta_active() {
        return nauta_active;
    }

    public Owner setNauta_active(String nauta_active) {
        this.nauta_active = nauta_active;
        return this;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public Owner setPromotion(Promotion promotion) {
        this.promotion = promotion;
        return this;
    }

    public Father getFather() {
        return father;
    }

    public Owner setFather(Father father) {
        this.father = father;
        return this;
    }
}
