package com.wirelesskings.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alberto on 23/10/2017.
 */

public class RealmReload extends RealmObject {
    public static final String ID = "id";
    public static final String CLIENT = "realmClient";
    public static final String SELLER = "realmSeller";
    public static final String COUNT = "count";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String STATUS = "status";
    public static final String APP = "app";


    @PrimaryKey
    private String id;
    private RealmClient realmClient;
    private RealmSeller realmSeller;
    private int count;
    private int amount;
    private long date;
    private String status;
    private String app;

    public RealmReload() {
    }

    public String getId() {
        return id;
    }

    public RealmReload setId(String id) {
        this.id = id;
        return this;
    }

    public RealmClient getRealmClient() {
        return realmClient;
    }

    public RealmReload setRealmClient(RealmClient realmClient) {
        this.realmClient = realmClient;
        return this;
    }

    public RealmSeller getRealmSeller() {
        return realmSeller;
    }

    public RealmReload setRealmSeller(RealmSeller realmSeller) {
        this.realmSeller = realmSeller;
        return this;
    }

    public int getCount() {
        return count;
    }

    public RealmReload setCount(int count) {
        this.count = count;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public RealmReload setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public long getDate() {
        return date;
    }

    public RealmReload setDate(long date) {
        this.date = date;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public RealmReload setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getApp() {
        return app;
    }

    public RealmReload setApp(String app) {
        this.app = app;
        return this;
    }
}
