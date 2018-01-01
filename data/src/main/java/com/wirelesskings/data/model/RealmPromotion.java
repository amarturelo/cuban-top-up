package com.wirelesskings.data.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Alberto on 23/10/2017.
 */

public class RealmPromotion extends RealmObject {

    public static final String WK_USER = "wkUser";

    private String wkUser;

    private String title;

    private String id;

    private String amount;

    private Date sdate;

    private Date edate;

    private RealmList<RealmReload> reloads;

    public RealmPromotion() {
    }

    public String getWkUser() {
        return wkUser;
    }

    public RealmPromotion setWkUser(String wkUser) {
        this.wkUser = wkUser;
        return this;
    }

    public String getPromotion() {
        return title;
    }

    public RealmPromotion setPromotion(String promotion) {
        this.title = promotion;
        return this;
    }

    public Date getSdate() {
        return sdate;
    }

    public RealmPromotion setSdate(Date sdate) {
        this.sdate = sdate;
        return this;
    }

    public Date getEdate() {
        return edate;
    }

    public RealmPromotion setEdate(Date edate) {
        this.edate = edate;
        return this;
    }

    public RealmList<RealmReload> getRealmReloads() {
        return reloads;
    }

    public RealmPromotion setRealmReloads(RealmList<RealmReload> realmReloads) {
        this.reloads = realmReloads;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RealmPromotion setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getId() {
        return id;
    }

    public RealmPromotion setId(String id) {
        this.id = id;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public RealmPromotion setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public RealmList<RealmReload> getReloads() {
        return reloads;
    }

    public RealmPromotion setReloads(RealmList<RealmReload> reloads) {
        this.reloads = reloads;
        return this;
    }
}
