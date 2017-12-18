package com.wirelesskings.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Alberto on 23/10/2017.
 */

public class RealmPromotion extends RealmObject {

    private String title;

    private String id;

    private String amount;

    private String sdate;

    private String edate;

    private RealmList<RealmReload> reloads;

    public RealmPromotion() {
    }

    public String getPromotion() {
        return title;
    }

    public RealmPromotion setPromotion(String promotion) {
        this.title = promotion;
        return this;
    }

    public String getSdate() {
        return sdate;
    }

    public RealmPromotion setSdate(String sdate) {
        this.sdate = sdate;
        return this;
    }

    public String getEdate() {
        return edate;
    }

    public RealmPromotion setEdate(String edate) {
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
