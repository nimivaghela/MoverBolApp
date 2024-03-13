package com.moverbol.views.fragments.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.moverbol.R;
import com.moverbol.adapters.CalendarAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.CalendarBinding;
import com.moverbol.network.model.JobPojo;
import com.moverbol.util.MoversPreferences;
import com.moverbol.viewmodels.HomeViewModel;
import com.moverbol.views.activities.JobSummaryActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Date;
import java.util.List;

/**
 * Created by sumeet on 14/9/17.
 */

public class CalendarFragment extends BaseFragment {

    private static final String SELECTED_DATE_KEY = "selected_dates";
    private CalendarBinding binding;
    private CalendarAdapter adapter;
    private HomeViewModel viewModel;

    private Date selectedDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);

        if (savedInstanceState != null && savedInstanceState.getLong(SELECTED_DATE_KEY) > 0) {
            selectedDate = new Date(savedInstanceState.getLong(SELECTED_DATE_KEY));
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialisation();
        setActionListeners();

        setLiveDataObservers();
    }

    private void initialisation() {
        adapter = new CalendarAdapter(new CalendarAdapter.OnJobEventClickListener() {
            @Override
            public void onClick(JobPojo jobPojo) {
                MoversPreferences.getInstance(CalendarFragment.this.getContext()).setOpportunityId(jobPojo.opportunityId);
                /*viewModel.setPositionToScrollHomeList(jobPojo.jobId);
                if(getActivity()!=null) {
                    getActivity().onBackPressed();
                }*/
                startActivity(new Intent(CalendarFragment.this.getActivity(), JobSummaryActivity.class).putExtra(Constants.EXTRA_JOB_ID_KEY, jobPojo.jobId));
            }
        });


        binding.materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {

                return viewModel.hasJobOnGivenDate(day.getDate());
            }

            @Override
            public void decorate(DayViewFacade view) {

                Drawable drawable = ContextCompat.getDrawable(CalendarFragment.this.getContext(), R.drawable.circular_background_transparent);
                view.setBackgroundDrawable(drawable);
            }
        });

        binding.setAdapter(adapter);
    }

    private void setActionListeners() {
        binding.materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                adapter.setCalendarPojos(viewModel.getFilteredJobList(date.getDate(), date.getDate()));
            }
        });

        binding.materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                binding.materialCalendarView.clearSelection();
            }
        });
    }

    private void setLiveDataObservers() {
        viewModel.jobPojoListLive.observe(this.getActivity(), new Observer<List<JobPojo>>() {
            @Override
            public void onChanged(@Nullable List<JobPojo> jobPojoList) {
                if (selectedDate != null) {
                    binding.materialCalendarView.setSelectedDate(selectedDate);
                }
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (binding.materialCalendarView.getSelectedDate() != null) {
            outState.putLong(SELECTED_DATE_KEY, binding.materialCalendarView.getSelectedDate().getDate().getTime());
        }
    }
}