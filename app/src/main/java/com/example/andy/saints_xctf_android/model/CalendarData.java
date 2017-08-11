package com.example.andy.saints_xctf_android.model;

import org.joda.time.DateTime;

/**
 * POJO representing calendar data to be stored for the monthly calendar
 * @author Andrew Jarombek
 * @since 7/29/2017
 */

public class CalendarData {

    private DateTime start_date;
    private DateTime end_date;
    private DateTime month_date;

    public DateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(DateTime start_date) {
        this.start_date = start_date;
    }

    public DateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(DateTime end_date) {
        this.end_date = end_date;
    }

    public DateTime getMonth_date() {
        return month_date;
    }

    public void setMonth_date(DateTime month_date) {
        this.month_date = month_date;
    }
}
