package com.moverbol.views.fragments.home;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.fragment.BaseFragment;
import com.moverbol.databinding.CalendarHolderBinding;

/**
 * Created by sumeet on 20/9/17.
 */

public class CalendarHolderFragment extends BaseFragment {

    private FragmentTransaction fragmentTransaction;
    private CalendarHolderBinding calendarHolderBinding;
    private static final String BACK_STACK_CALENDER_TAG = "calender_fragment";
    private static final String BACK_STACK_WEEK_TAG = "week_fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarHolderBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar_holder, container, false);
        changeFragment(Constants.CALENDAR_VIEW);
        return calendarHolderBinding.getRoot();
    }

    public void changeFragment(int i) {
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (i == Constants.CALENDAR_VIEW) {
            fragmentTransaction.replace(calendarHolderBinding.clContainer.getId(), new CalendarFragment());
            fragmentTransaction.addToBackStack(BACK_STACK_CALENDER_TAG);
        } else if (i == Constants.WEEK_VIEW) {
            fragmentTransaction.replace(calendarHolderBinding.clContainer.getId(), new WeekCalendarFragment());
            fragmentTransaction.addToBackStack(BACK_STACK_WEEK_TAG);
        }
        fragmentTransaction.commit();
    }


}
