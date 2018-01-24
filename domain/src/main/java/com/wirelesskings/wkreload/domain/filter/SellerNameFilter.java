package com.wirelesskings.wkreload.domain.filter;

import android.os.Parcel;

/**
 * Created by alberto on 23/01/18.
 */

public class SellerNameFilter implements Filter {

    private String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public SellerNameFilter() {
    }

    protected SellerNameFilter(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<SellerNameFilter> CREATOR = new Creator<SellerNameFilter>() {
        @Override
        public SellerNameFilter createFromParcel(Parcel source) {
            return new SellerNameFilter(source);
        }

        @Override
        public SellerNameFilter[] newArray(int size) {
            return new SellerNameFilter[size];
        }
    };

    public String getName() {
        return name;
    }

    public SellerNameFilter setName(String name) {
        this.name = name;
        return this;
    }
}
