package com.wirelesskings.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Alberto on 23/10/2017.
 */

public class RealmPromotion extends RealmObject {

    private String promotion;

    private long sdate;

    private long edate;

    private RealmList<RealmReload> realmReloads;

    public RealmPromotion() {
    }

    public String getPromotion() {
        return promotion;
    }

    public RealmPromotion setPromotion(String promotion) {
        this.promotion = promotion;
        return this;
    }

    public long getSdate() {
        return sdate;
    }

    public RealmPromotion setSdate(long sdate) {
        this.sdate = sdate;
        return this;
    }

    public long getEdate() {
        return edate;
    }

    public RealmPromotion setEdate(long edate) {
        this.edate = edate;
        return this;
    }

    public RealmList<RealmReload> getRealmReloads() {
        return realmReloads;
    }

    public RealmPromotion setRealmReloads(RealmList<RealmReload> realmReloads) {
        this.realmReloads = realmReloads;
        return this;
    }
}
