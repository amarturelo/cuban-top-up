package com.wirelesskings.wkreload.domain.model;

import java.util.List;


/**
 * Created by Alberto on 23/10/2017.
 */

public class Promotion {

    private String promotion;

    private String sdate;

    private String edate;

    private List<Reload> reloads;

    public Promotion() {
    }

    public String getPromotion() {
        return promotion;
    }

    public Promotion setPromotion(String promotion) {
        this.promotion = promotion;
        return this;
    }

    public String getSdate() {
        return sdate;
    }

    public Promotion setSdate(String sdate) {
        this.sdate = sdate;
        return this;
    }

    public String getEdate() {
        return edate;
    }

    public Promotion setEdate(String edate) {
        this.edate = edate;
        return this;
    }

    public List<Reload> getReloads() {
        return reloads;
    }

    public Promotion setReloads(List<Reload> reloads) {
        this.reloads = reloads;
        return this;
    }
}
