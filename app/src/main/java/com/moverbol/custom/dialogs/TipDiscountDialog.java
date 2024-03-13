package com.moverbol.custom.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.moverbol.R;
import com.moverbol.databinding.TipDialogBinding;
import com.moverbol.model.billOfLading.TipsModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;

import java.util.ArrayList;

/**
 * Created by AkashM on 18/4/18.
 */
public class TipDiscountDialog extends BaseDialogFragment {

    private TipDialogBinding binding;
    private OnTipSubmittedListener onTipSubmittedListener;
    private Context context;
    private double mTipAmount;
    private boolean isPercentage;
    private String calculatedAmount;
    private ArrayList<TipsModel> tipsModelList;
    private double subTotal;

    public static void start(Context context, FragmentManager supportFragmentManager, ArrayList<TipsModel> tipsModelList, double subTotal) {
        TipDiscountDialog tipDiscountDialog = new TipDiscountDialog();
        tipDiscountDialog.context = context;
        tipDiscountDialog.tipsModelList = tipsModelList;
        tipDiscountDialog.subTotal = subTotal;
        tipDiscountDialog.show(supportFragmentManager, "dialog");
    }

    /*public static void startWithActionListeners(Context context, FragmentManager supportFragmentManager, ArrayList<TipsModel> tipsModelList, OnTipSubmittedListener onTipSubmittedListener) {
        TipDiscountDialog tipDiscountDialog = new TipDiscountDialog();
        tipDiscountDialog.context = context;
        tipDiscountDialog.tipsModelList = tipsModelList;
        tipDiscountDialog.setOnTipSubmittedListener(onTipSubmittedListener);
        tipDiscountDialog.show(supportFragmentManager, "dialog");
    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_tip_dialog, container, false);
        setCancelable(false);

        initialise();

        setActionListeners();

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onTipSubmittedListener = (OnTipSubmittedListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString()
                    + " must implement TipDiscountDialog.OnTipSubmittedListener");
        }
    }

    private void initialise() {
        if (context != null) {
            String currencySymbol = MoversPreferences.getInstance(context).getCurrencySymbol();
            binding.radioOther.setText(String.format("Other Amount (%s)", currencySymbol));
        }
        if (tipsModelList == null) {
            tipsModelList = new ArrayList<>();
        }

        for (int i = (tipsModelList.size() - 1); i >= 0; i--) {
            RadioButton radioButton = new RadioButton(context);

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = getResources().getDimensionPixelOffset(R.dimen._20sdp);
            layoutParams.setMargins(0, margin, 0, 0);
            radioButton.setLayoutParams(layoutParams);
            int padding = getResources().getDimensionPixelOffset(R.dimen._20sdp);
            radioButton.setPadding(padding, 0, 0, 0);
            radioButton.setId(i);
            radioButton.setButtonDrawable(getResources().getDrawable(R.drawable.crew_checkbox));
            radioButton.setTextColor(ContextCompat.getColor(context, R.color.home_text_black));
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            radioButton.setText(String.format("%s%% ($%S)", tipsModelList.get(i).getTipPercentage(), getTipAmount(tipsModelList.get(i).getTipPercentage())));
            radioButton.setTag(Util.getDoubleFromString(tipsModelList.get(i).getTipPercentage()));


            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        binding.btnDone.setEnabled(true);
                        binding.btnDone.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));

                        isPercentage = true;
                        mTipAmount = (double) buttonView.getTag();
                        calculatedAmount = getTipAmount(tipsModelList.get(buttonView.getId()).getTipPercentage());
                    }
                }
            });

            binding.radioGroup.addView(radioButton, 0);
        }
    }

    private void setActionListeners() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTipSubmittedListener != null) {
                    onTipSubmittedListener.onTipSkip(TipDiscountDialog.this);
                }
            }
        });


       /* binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                binding.btnDone.setEnabled(true);
                binding.btnDone.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));


                switch (i) {
                    case R.id.radio_one:
                        isPercentage = true;
                        mTipAmount = 10;
                        break;

                    case R.id.radio_two:
                        isPercentage = true;
                        mTipAmount = 15;
                        break;

                    case R.id.radio_three:
                        isPercentage = true;
                        mTipAmount = 20;
                        break;

                    case R.id.radio_other:
                        isPercentage = false;
                        break;
                }
            }
        });*/

        binding.btnDone.setOnClickListener(v -> {

            if (onTipSubmittedListener != null) {
                if (validate()) {
                    if (!isPercentage) {
                        mTipAmount = Double.parseDouble(binding.editText3.getText().toString());
                        calculatedAmount = binding.editText3.getText().toString().trim();
                    }
                    onTipSubmittedListener.onTipSubmitted(TipDiscountDialog.this, mTipAmount, isPercentage, calculatedAmount);
                }
            }
        });

        binding.radioOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.editText3.setVisibility(View.VISIBLE);
                    isPercentage = false;
                    binding.btnDone.setEnabled(true);
                    binding.btnDone.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                } else {
                    binding.editText3.setVisibility(View.GONE);
                }
            }
        });

    }

    /*private void setOnTipSubmittedListener(OnTipSubmittedListener onTipSubmittedListener) {
        this.onTipSubmittedListener = onTipSubmittedListener;
    }*/

    private boolean validate() {
        if (binding.radioGroup.getCheckedRadioButtonId() == -1) {
            return false;
        }

        if (binding.radioGroup.getCheckedRadioButtonId() == R.id.radio_other && TextUtils.isEmpty(binding.editText3.getText())) {
            binding.editText3.requestFocus();
            binding.editText3.setError(getString(R.string.empty_field_error));
            return false;
        }
        return true;
    }

    private String getTipAmount(String percentage) {
        return Util.getGeneralFormattedDecimalString((subTotal * Double.parseDouble(percentage)) / 100);
    }

    public interface OnTipSubmittedListener {
        void onTipSubmitted(TipDiscountDialog tipDiscountDialog, double tip, boolean isPercentage, String calculatedAmount);

        void onTipSkip(TipDiscountDialog tipDiscountDialog);
    }

}
