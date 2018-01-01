package com.wirelesskings.wkreload.domain.model;

import java.util.Date;
import java.util.List;


/**
 * Created by Alberto on 23/10/2017.
 */

public class Promotion {

    private String id;

    private String promotion;

    private Date sdate;

    private Date edate;

    private String amount;

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

    public Date getSdate() {
        return sdate;
    }

    public Promotion setSdate(Date sdate) {
        this.sdate = sdate;
        return this;
    }

    public String getId() {
        return id;
    }

    public Promotion setId(String id) {
        this.id = id;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public Promotion setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public Date getEdate() {
        return edate;
    }

    public Promotion setEdate(Date edate) {
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
