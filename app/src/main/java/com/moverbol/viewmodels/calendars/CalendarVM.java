package com.moverbol.viewmodels.calendars;

import androidx.databinding.ObservableArrayList;

import com.moverbol.model.CalendarPojo;

/**
 * Created by sumeet on 18/9/17.
 */

public class CalendarVM {

    public ObservableArrayList<CalendarPojo> calendarPojos = new ObservableArrayList<>();

    public void loadData() {
        //for (int i = 9; i <= 10; i++) {
        calendarPojos.add(new CalendarPojo("9AM", "09:00AM - 10:00AM", "Carmack Demo", "Kalamazoo, MI To Kalamazoo < MI"));
        calendarPojos.add(new CalendarPojo("10AM", "09:00AM - 10:00AM", "Carmack Demo", "Kalamazoo, MI To Kalamazoo < MI"));
        calendarPojos.add(new CalendarPojo("11AM", "09:00AM - 10:00AM", "Carmack Demo", "Kalamazoo, MI To Kalamazoo < MI"));
        calendarPojos.add(new CalendarPojo("12AM", "09:00AM - 10:00AM", "Carmack Demo", "Kalamazoo, MI To Kalamazoo < MI"));
        for (int i = 1; i <= 12; i++) {
            calendarPojos.add(new CalendarPojo(i + "PM", "09:00AM - 10:00AM", "Carmack Demo", "Kalamazoo, MI To Kalamazoo < MI"));
        }
        /*calendarPojos.add(new CalendarPojo("2PM", "09:00AM - 10:00AM", "Carmack Demo", "Kalamazoo, MI To Kalamazoo < MI"));
        calendarPojos.add(new CalendarPojo("3PM", "09:00AM - 10:00AM", "Carmack Demo", "Kalamazoo, MI To Kalamazoo < MI"));
        calendarPojos.add(new CalendarPojo("4PM", "09:00AM - 10:00AM", "Carmack Demo", "Kalamazoo, MI To Kalamazoo < MI"));*/
        //}
    }

}
