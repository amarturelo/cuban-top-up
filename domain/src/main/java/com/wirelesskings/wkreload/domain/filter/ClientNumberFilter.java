package com.wirelesskings.wkreload.domain.filter;

import android.os.Parcel;

/**
 * Created by alberto on 17/01/18.
 */

public class ClientNumberFilter implements Filter {
    private String clientNumber;

    public ClientNumberFilter() {
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public ClientNumberFilter setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clientNumber);
    }

    protected ClientNumberFilter(Parcel in) {
        this.clientNumber = in.readString();
    }

    public static final Creator<ClientNumberFilter> CREATOR = new Creator<ClientNumberFilter>() {
        @Override
        public ClientNumberFilter createFromParcel(Parcel source) {
            return new ClientNumberFilter(source);
        }

        @Override
        public ClientNumberFilter[] newArray(int size) {
            return new ClientNumberFilter[size];
        }
    };
}
