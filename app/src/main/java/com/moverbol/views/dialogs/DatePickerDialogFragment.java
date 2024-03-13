package com.moverbol.views.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moverbol.interfaces.SelectDateListener;

import java.util.Calendar;
import java.util.Objects;

import static com.moverbol.constants.Constants.KEY_DAY;
import static com.moverbol.constants.Constants.KEY_MONTH;
import static com.moverbol.constants.Constants.KEY_YEAR;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private SelectDateListener selectDateListener;


    static public DatePickerDialogFragment newInstance(int year, int month, int dayOfMonth) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_YEAR, year);
        bundle.putInt(KEY_MONTH, month);
        bundle.putInt(KEY_DAY, dayOfMonth);
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        datePickerDialogFragment.setArguments(bundle);
        return datePickerDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = Objects.requireNonNull(getArguments()).getInt(KEY_YEAR, calendar.get(Calendar.YEAR));
        int month = getArguments().getInt(KEY_MONTH, calendar.get(Calendar.MONTH));
        int day = getArguments().getInt(KEY_DAY, calendar.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), this, year, month, day);
        datePickerDialog.setTitle("");
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        selectDateListener.setDate(calendar);
    }

    public void setSelectDateListener(SelectDateListener selectDateListener) {
        this.selectDateListener = selectDateListener;
    }


}
