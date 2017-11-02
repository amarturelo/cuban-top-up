package com.wirelesskings.wkreload.domain.model;


/**
 * Created by Alberto on 23/10/2017.
 */

public class Reload {
    private String id;
    private Client client;
    private Seller seller;
    private int count;
    private int amount;
    private String date;
    private String status;
    private String app;

    public Reload() {
    }

    public String getId() {
        return id;
    }

    public Reload setId(String id) {
        this.id = id;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public Reload setClient(Client client) {
        this.client = client;
        return this;
    }

    public Seller getSeller() {
        return seller;
    }

    public Reload setSeller(Seller seller) {
        this.seller = seller;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Reload setCount(int count) {
        this.count = count;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Reload setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Reload setDate(String date) {
        this.date = date;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Reload setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getApp() {
        return app;
    }

    public Reload setApp(String app) {
        this.app = app;
        return this;
    }
}
