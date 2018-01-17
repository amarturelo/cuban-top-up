package com.wirelesskings.data.filter;

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
}
