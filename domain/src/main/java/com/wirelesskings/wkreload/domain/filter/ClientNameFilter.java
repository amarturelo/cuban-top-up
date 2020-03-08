package com.wirelesskings.wkreload.domain.filter;

import android.os.Parcel;

/**
 * Created by alberto on 17/01/18.
 */

public class ClientNameFilter implements Filter {
    private String name;

    public ClientNameFilter() {
    }

    public String getName() {
        return name;
    }

    public ClientNameFilter setName(String name) {
        this.name = name;
        return this;
    }

    protected ClientNameFilter(Parcel in) {
        name = in.readString();
    }

    public static final Creator<ClientNameFilter> CREATOR = new Creator<ClientNameFilter>() {
        @Override
        public ClientNameFilter createFromParcel(Parcel in) {
            return new ClientNameFilter(in);
        }

        @Override
        public ClientNameFilter[] newArray(int size) {
            return new ClientNameFilter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
