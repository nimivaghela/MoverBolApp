package com.moverbol.model;

/**
 * Created by sumeet on 18/9/17.
 */

public class CalendarPojo {

    public String time;
    public String timeFromTo;
    public String name;
    public String purpose;

    public CalendarPojo(String time, String timeFromTo, String name, String purpose) {
        this.time = time;
        this.timeFromTo = timeFromTo;
        this.name = name;
        this.purpose = purpose;
    }
}
