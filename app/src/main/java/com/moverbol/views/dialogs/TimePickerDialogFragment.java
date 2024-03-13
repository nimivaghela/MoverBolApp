package com.moverbol.views.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moverbol.interfaces.SelectDateListener;

import java.util.Calendar;
import java.util.Objects;

import static com.moverbol.constants.Constants.KEY_HOUR;
import static com.moverbol.constants.Constants.KEY_MINUTE;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private SelectDateListener selectDateListener;

    public static TimePickerDialogFragment newInstance(int hours, int minute) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_HOUR, hours);
        bundle.putInt(KEY_MINUTE, minute);
        TimePickerDialogFragment timePickerDialogFragment = new TimePickerDialogFragment();
        timePickerDialogFragment.setArguments(bundle);
        return timePickerDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = Objects.requireNonNull(getArguments()).getInt(KEY_HOUR, c.get(Calendar.HOUR_OF_DAY));
        int minute = getArguments().getInt(KEY_MINUTE, c.get(Calendar.MINUTE));

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), this, hour, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        selectDateListener.setDate(calendar);
    }


    public void setSelectDateListener(SelectDateListener selectDateListener) {
        this.selectDateListener = selectDateListener;
    }
}
