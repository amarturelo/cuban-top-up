package com.wirelesskings.wkreload.model;

import java.util.Date;

/**
 * Created by alberto on 31/12/17.
 */

public class PromotionItemModel {
    private String id;
    private Date startDate;
    private Date endDate;
    private String title;
    private String amount;

    public PromotionItemModel() {
    }

    public String getId() {
        return id;
    }

    public PromotionItemModel setId(String id) {
        this.id = id;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public PromotionItemModel setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public PromotionItemModel setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PromotionItemModel setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PromotionItemModel setTitle(String title) {
        this.title = title;
        return this;
    }
}
