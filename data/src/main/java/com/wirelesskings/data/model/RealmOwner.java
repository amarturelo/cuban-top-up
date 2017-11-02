package com.wirelesskings.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 01/11/2017.
 */

public class RealmOwner extends RealmObject {
    @PrimaryKey
    private String user_nauta;
    private String nauta_active;
    private RealmPromotion promotion;
    private RealmFather father;

    public RealmOwner() {
    }

    public String getUser_nauta() {
        return user_nauta;
    }

    public RealmOwner setUser_nauta(String user_nauta) {
        this.user_nauta = user_nauta;
        return this;
    }

    public String getNauta_active() {
        return nauta_active;
    }

    public RealmOwner setNauta_active(String nauta_active) {
        this.nauta_active = nauta_active;
        return this;
    }

    public RealmPromotion getPromotion() {
        return promotion;
    }

    public RealmOwner setPromotion(RealmPromotion promotion) {
        this.promotion = promotion;
        return this;
    }

    public RealmFather getFather() {
        return father;
    }

    public RealmOwner setFather(RealmFather father) {
        this.father = father;
        return this;
    }
}
