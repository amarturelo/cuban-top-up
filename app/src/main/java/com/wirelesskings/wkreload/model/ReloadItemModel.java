package com.wirelesskings.wkreload.model;

import java.util.Date;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ReloadItemModel {
    private String id;
    private String seller;
    private int amount;
    private int count;
    private String clientNumber;
    private String clientName;
    private STATUS status;

    private Date date;

    public ReloadItemModel() {
    }

    public String getId() {
        return id;
    }

    public ReloadItemModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getSeller() {
        return seller;
    }

    public ReloadItemModel setSeller(String seller) {
        this.seller = seller;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public ReloadItemModel setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public int getCount() {
        return count;
    }

    public ReloadItemModel setCount(int count) {
        this.count = count;
        return this;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public ReloadItemModel setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public ReloadItemModel setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public ReloadItemModel setDate(Date date) {
        this.date = date;
        return this;
    }

    public STATUS getStatus() {
        return status;
    }

    public ReloadItemModel setStatus(STATUS status) {
        this.status = status;
        return this;
    }

    public enum STATUS {
        INPROGRESS, SUCCESS, DENIED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReloadItemModel that = (ReloadItemModel) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
