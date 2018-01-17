package com.wirelesskings.data.filter;


/**
 * Created by alberto on 17/01/18.
 */

public class PromotionFilter implements Filter {
    private String id;

    public PromotionFilter() {
    }

    public String getId() {
        return id;
    }

    public PromotionFilter setId(String id) {
        this.id = id;
        return this;
    }
}
