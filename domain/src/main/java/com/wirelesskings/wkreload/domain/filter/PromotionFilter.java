package com.wirelesskings.wkreload.domain.filter;


import android.os.Parcel;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
