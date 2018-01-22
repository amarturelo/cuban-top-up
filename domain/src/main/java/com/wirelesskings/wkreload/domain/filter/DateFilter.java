package com.wirelesskings.wkreload.domain.filter;

import android.os.Parcel;

import java.util.Date;

/**
 * Created by alberto on 17/01/18.
 */

public class DateFilter implements Filter {
    private Date start;
    private Date end;

    public DateFilter() {
    }

    public Date getStart() {
        return start;
    }

    public DateFilter setStart(Date start) {
        this.start = start;
        return this;
    }

    public Date getEnd() {
        return end;
    }

    public DateFilter setEnd(Date end) {
        this.end = end;
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.start != null ? this.start.getTime() : -1);
        dest.writeLong(this.end != null ? this.end.getTime() : -1);
    }

    protected DateFilter(Parcel in) {
        long tmpStart = in.readLong();
        this.start = tmpStart == -1 ? null : new Date(tmpStart);
        long tmpEnd = in.readLong();
        this.end = tmpEnd == -1 ? null : new Date(tmpEnd);
    }

    public static final Creator<DateFilter> CREATOR = new Creator<DateFilter>() {
        @Override
        public DateFilter createFromParcel(Parcel source) {
            return new DateFilter(source);
        }

        @Override
        public DateFilter[] newArray(int size) {
            return new DateFilter[size];
        }
    };
}
