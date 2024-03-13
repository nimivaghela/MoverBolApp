package com.moverbol.views.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekViewEvent;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.WeekCalendarBinding;
import com.moverbol.network.model.JobPojo;
import com.moverbol.util.MoversPreferences;
import com.moverbol.viewmodels.HomeViewModel;
import com.moverbol.views.activities.JobSummaryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by sumeet on 20/9/17.
 */

public class WeekCalendarFragment extends BaseFragment {

    private WeekCalendarBinding mBinding;

    private HomeViewModel viewModel;

    private List<JobPojo> jobList;
    private MonthLoader.MonthChangeListener mMonthChangeListener = (newYear, newMonth) -> {
        // Populate the week view with some events.
        List<WeekViewEvent> events = getEvents(newYear, newMonth);
        return events;
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_week_calendar, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        // Set an action when any event is clicked.
        mBinding.weekView.setOnEventClickListener((event, eventRect) -> {
            MoversPreferences.getInstance(WeekCalendarFragment.this.getContext()).setOpportunityId(jobList.get((int) event.getId()).opportunityId);
            startActivity(new Intent(WeekCalendarFragment.this.getActivity(), JobSummaryActivity.class).putExtra(Constants.EXTRA_JOB_ID_KEY, jobList.get((int) event.getId()).jobId));
         /*   viewModel.setPositionToScrollHomeList(event.getmJobId());
            if(getActivity()!=null) {
                getActivity().onBackPressed();
            }*/
        });

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mBinding.weekView.setMonthChangeListener(mMonthChangeListener);

        // Set long press listener for events.
        //mWeekView.setEventLongPressListener(mEventLongPressListener);


        return mBinding.getRoot();
    }

    private List<WeekViewEvent> getEvents(int newYear, int newMonth) {
        List<WeekViewEvent> eventList = new ArrayList<>();
        //for (int i = 0; i < 2; i++) {
        //eventList.add(new WeekViewEvent(101, "Sunny One", newYear, newMonth, 20, 9, 25, newYear, 01, 20, 10, 40));

        /*Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 6);
        startTime.set(Calendar.MINUTE, 15);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        //endTime.set(Calendar.MINUTE, 20);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(1, "Event One", startTime, endTime);
        event.setColor(getResources().getColor(R.color.light_day_green));
        event.setLocation("\nNew Jersey");
        eventList.add(event);

        Calendar startTime1 = Calendar.getInstance();
        startTime1.set(Calendar.HOUR_OF_DAY, 8);
        startTime1.set(Calendar.MINUTE, 15);
        startTime1.set(Calendar.MONTH, newMonth - 1);
        startTime1.set(Calendar.YEAR, newYear);
        Calendar endTime1 = (Calendar) startTime1.clone();
        endTime1.add(Calendar.HOUR, 1);
        //endTime.set(Calendar.MINUTE, 20);
        endTime1.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event1 = new WeekViewEvent(2, "Event Two", startTime1, endTime1);
        event1.setColor(getResources().getColor(R.color.colorPrimary));
        event1.setLocation("\nPlace\nkalamazoo, MI to Kalamazoo<MI uiyuiyyiy");

        eventList.add(event1);*/


        //}

        Log.d(Constants.BASE_LOG_TAG + " old ", newYear + "  " + newMonth);


        jobList = getFilteredJobListForGivenMonthAndYear(newYear, newMonth);

        if (jobList != null) {

            if(jobList.size()>0)
            Log.d(Constants.BASE_LOG_TAG + "yearMonth", newYear + "  " + newMonth);

            for (int i = 0; i < jobList.size(); i++) {
                WeekViewEvent event = getWeekViewEventFromJob(jobList.get(i), i);
                eventList.add(event);
            }
        }

        return eventList;
    }

    private WeekViewEvent getWeekViewEventFromJob(JobPojo jobPojo, int jobIdToSet) {

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(jobPojo.getDateObject());
        startTime.set(Calendar.HOUR_OF_DAY, jobPojo.getStartTimeHourIn24Format());


        Log.d(Constants.BASE_LOG_TAG + "startTime", jobPojo.getStartTimeHourIn24Format() + "");

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(jobPojo.getDateObject());
        endTime.set(Calendar.HOUR_OF_DAY, jobPojo.getStartTimeHourIn24Format() + 1);
        //endTime.set(Calendar.MINUTE, 20);


        WeekViewEvent event = new WeekViewEvent(jobIdToSet, jobPojo.opportunityName, startTime, endTime);
        event.setColor(getResources().getColor(R.color.colorPrimary));
        event.setLocation(jobPojo.originalCity + " To " + jobPojo.destinationCity);
        return event;
    }

    private List<JobPojo> getFilteredJobListForGivenMonthAndYear(int newYear, int newMonth) {
        Calendar startDateCalender = Calendar.getInstance(Locale.getDefault());

        Calendar endDateCalender = Calendar.getInstance(Locale.getDefault());

        startDateCalender.set(newYear, newMonth - 1, 1);

        endDateCalender.set(newYear, newMonth - 1, startDateCalender.getActualMaximum(Calendar.DAY_OF_MONTH));

        Log.d(Constants.BASE_LOG_TAG + "MaxDate", startDateCalender.getActualMaximum(Calendar.DAY_OF_MONTH) + "");

        return viewModel.getFilteredJobList(startDateCalender.getTime(), endDateCalender.getTime());
    }

}
