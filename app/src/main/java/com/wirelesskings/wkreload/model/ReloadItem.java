package com.wirelesskings.wkreload.model;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ReloadItem {
    private String id;
    private String seller;
    private int ammount;
    private int count;
    private String clientNumber;
    private String clientName;

    public ReloadItem() {
    }

    public ReloadItem(String id, String seller, int amount, int count, String clientNumber, String clientName) {
        this.id = id;
        this.seller = seller;
        this.ammount = amount;
        this.count = count;
        this.clientNumber = clientNumber;
        this.clientName = clientName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
