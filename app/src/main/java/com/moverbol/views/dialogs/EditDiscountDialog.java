package com.moverbol.views.dialogs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.databinding.DialogEditDiscountBinding;
import com.moverbol.util.Util;

import java.util.ArrayList;
import java.util.Objects;

public class EditDiscountDialog extends DialogFragment {

    private DialogEditDiscountBinding binding;
    private int discountType = 1;
    private double discountValue = 0;
    private OnEditDiscountSubmitListener onEditDiscountSubmitListener;

    public static EditDiscountDialog newInstance(int discountType, double discountValue) {
        EditDiscountDialog f = new EditDiscountDialog();
        Bundle args = new Bundle();
        args.putInt(Constants.DISCOUNT_TYPE, discountType);
        args.putDouble(Constants.DISCOUNT, discountValue);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.CustomDialog);
        discountType = requireArguments().getInt(Constants.DISCOUNT_TYPE, discountType);
        discountValue = requireArguments().getDouble(Constants.DISCOUNT, discountValue);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_discount, container, false);
        setDiscountTypeDropDown();

        binding.imgClose.setOnClickListener(v -> {
            dismiss();
        });

        binding.btnSubmit.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(Objects.requireNonNull(binding.txtEditDiscountValue.getText()).toString().trim())) {
                if (onEditDiscountSubmitListener != null) {
                    onEditDiscountSubmitListener.onEditDiscountSubmit(discountType, Double.parseDouble(binding.txtEditDiscountValue.getText().toString().trim()));
                }
            } else {
                Util.showToast(getContext(), getString(R.string.please_enter_discount_value));
            }
        });

        return binding.getRoot();
    }

    private void setDiscountTypeDropDown() {
        ArrayList<String> typeList = new ArrayList<>(0);
        typeList.add("$");
        typeList.add("%");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, typeList);
        binding.txtEditDiscountType.setAdapter(arrayAdapter);
        binding.txtEditDiscountType.setText(typeList.get(discountType - 1), false);
        binding.txtEditDiscountValue.setText(Util.getGeneralFormattedDecimalString(discountValue));
        binding.txtEditDiscountType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                discountType = position + 1;
            }
        });
    }

    public void setOnEditDiscountSubmitListener(OnEditDiscountSubmitListener onEditDiscountSubmitListener) {
        this.onEditDiscountSubmitListener = onEditDiscountSubmitListener;
    }

    public interface OnEditDiscountSubmitListener {
        void onEditDiscountSubmit(int discountType, double discountValue);
    }
}
