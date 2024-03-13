package com.moverbol.custom.dialogs;


import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.EditValueDialogBinding;

/**
 * Created by AkashM on 2/4/18.
 */
public class EditValueDialog extends DialogFragment {


    private EditValueDialogBinding binding;
    private OnValueSubmittedListener onValueSubmittedListener;

    private String title;
    private String valueToShow;

    public interface OnValueSubmittedListener{
        void onValueSubmitted(String value);
        void onSkip();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_edit_value, container, false);
        setCancelable(false);

        if(title!=null){
            binding.textView140.setText(title);
        }
        if(valueToShow!=null){
            binding.edtxtNote.setText(valueToShow);
        }
        setActionListeners();

        return binding.getRoot();

    }

    private void setActionListeners() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onValueSubmittedListener!=null){
                    onValueSubmittedListener.onSkip();
                }
                dismiss();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onValueSubmittedListener!=null){
                    if(TextUtils.isEmpty(binding.edtxtNote.getText().toString())){
                        binding.edtxtNote.setError(getString(R.string.empty_field_error));
                    }else {
                        onValueSubmittedListener.onValueSubmitted(binding.edtxtNote.getText().toString());
                    }

                }
                dismiss();
            }
        });
    }

    public void setOnValueSubmittedListener(OnValueSubmittedListener onValueSubmittedListener) {
        this.onValueSubmittedListener = onValueSubmittedListener;
    }

    public static void start(FragmentManager supportFragmentManager) {
        RatingDialog ratingDialog = new RatingDialog();
        ratingDialog.show(supportFragmentManager, "dialog");
    }

    public static void startWithActionListeners(FragmentManager supportFragmentManager, @Nullable String valueToShow, @Nullable String title, OnValueSubmittedListener onValueSubmittedListener) {
        EditValueDialog editValueDialog = new EditValueDialog();
        editValueDialog.setOnValueSubmittedListener(onValueSubmittedListener);

        editValueDialog.title = title;
        editValueDialog.valueToShow = valueToShow;

        editValueDialog.show(supportFragmentManager, "dialog");
    }

}
