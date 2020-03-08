package com.wirelesskings.wkreload.model;

/**
 * Created by alberto on 1/01/18.
 */

public class PreReloadItemModel {
    private int amount;
    private int count;
    private String clientNumber;
    private String clientName;

    public PreReloadItemModel() {
    }

    public int getAmount() {
        return amount;
    }

    public PreReloadItemModel setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public int getCount() {
        return count;
    }

    public PreReloadItemModel setCount(int count) {
        this.count = count;
        return this;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public PreReloadItemModel setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public PreReloadItemModel setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }
}
