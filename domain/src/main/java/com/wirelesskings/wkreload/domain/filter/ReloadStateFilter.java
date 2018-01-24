package com.wirelesskings.wkreload.domain.filter;

import android.os.Parcel;

/**
 * Created by alberto on 23/01/18.
 */

public class ReloadStateFilter implements Filter {

    private String state;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.state);
    }

    public ReloadStateFilter() {
    }

    protected ReloadStateFilter(Parcel in) {
        this.state = in.readString();
    }

    public static final Creator<ReloadStateFilter> CREATOR = new Creator<ReloadStateFilter>() {
        @Override
        public ReloadStateFilter createFromParcel(Parcel source) {
            return new ReloadStateFilter(source);
        }

        @Override
        public ReloadStateFilter[] newArray(int size) {
            return new ReloadStateFilter[size];
        }
    };

    public String getState() {
        return state;
    }

    public ReloadStateFilter setState(String state) {
        this.state = state;
        return this;
    }
}
