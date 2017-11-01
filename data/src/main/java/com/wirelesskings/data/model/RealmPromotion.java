package com.wirelesskings.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Alberto on 23/10/2017.
 */

public class RealmPromotion extends RealmObject {

    private String title;

    private long sdate;

    private long edate;

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
        return reloads;
    }

    public RealmPromotion setRealmReloads(RealmList<RealmReload> realmReloads) {
        this.reloads = realmReloads;
        return this;
    }
}
