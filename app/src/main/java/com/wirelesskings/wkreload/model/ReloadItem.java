package com.wirelesskings.wkreload.model;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ReloadItem {
    private String id;
    private String seller;
    private int amount;
    private int count;
    private String clientNumber;
    private String clientName;
    private STATUS status;

    public ReloadItem() {
    }

    public String getId() {
        return id;
    }

    public ReloadItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getSeller() {
        return seller;
    }

    public ReloadItem setSeller(String seller) {
        this.seller = seller;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public ReloadItem setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public int getCount() {
        return count;
    }

    public ReloadItem setCount(int count) {
        this.count = count;
        return this;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public ReloadItem setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public ReloadItem setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public STATUS getStatus() {
        return status;
    }

    public ReloadItem setStatus(STATUS status) {
        this.status = status;
        return this;
    }

    public enum STATUS{
        SEND,INPROGRESS,SUCCESS
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReloadItem that = (ReloadItem) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
