package com.moverbol.custom.dialogs;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.ReviewDiscountDialogBinding;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;


/**
 * Created by AkashM on 14/3/18.
 */

public class ReviewDiscountDialog extends BaseDialogFragment {

    private ReviewDiscountDialogBinding binding;
    private OnReviewDiscountSubmittedListener reviewDiscountSubmittedListener;

    public static void start(FragmentManager supportFragmentManager) {
        ReviewDiscountDialog reviewDiscountDialog = new ReviewDiscountDialog();
        reviewDiscountDialog.show(supportFragmentManager, "dialog");
    }

    public static void startWithActionListeners(FragmentManager supportFragmentManager, OnReviewDiscountSubmittedListener reviewDiscountSubmittedListener) {
        ReviewDiscountDialog reviewDiscountDialog = new ReviewDiscountDialog();

        reviewDiscountDialog.setReviewDiscountSubmittedListener(reviewDiscountSubmittedListener);

        reviewDiscountDialog.show(supportFragmentManager, "dialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_rating_discount_dialogue, container, false);
        setCancelable(false);

        initialisation();

        setActionListeners();

        return binding.getRoot();
    }

    private void initialisation() {
        String currencySymbol = MoversPreferences.getInstance(getContext()).getCurrencySymbol();
        binding.spinner.setHint( getString(R.string.discount_amount) + "(" + currencySymbol + ")");
    }

    private void setActionListeners() {

        /*binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){

                }
            }
        });*/


        /*binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    binding.btnDone.setEnabled(false);
                    binding.btnDone.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_login_gray));
                } else {
                    mSelectedDiscountPercentage = ((RatingDiscountPercentagePojo) adapterView.getSelectedItem()).getDiscountPercentageDoubleValue();
                    binding.btnDone.setEnabled(true);
                    binding.btnDone.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewDiscountSubmittedListener != null) {
                    reviewDiscountSubmittedListener.onSkip(ReviewDiscountDialog.this);
                }
            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    if (reviewDiscountSubmittedListener != null) {
                        double discountAmount = Util.getDoubleFromString(binding.edtxtDiscount.getText().toString());
                        reviewDiscountSubmittedListener.onDiscountSubmitted(discountAmount, ReviewDiscountDialog.this);
                    }
                }
            }
        });

    }

    private boolean validate() {
        if(TextUtils.isEmpty(binding.edtxtDiscount.getText())){
            binding.edtxtDiscount.requestFocus();
            binding.edtxtDiscount.setError(getText(R.string.empty_field_error));
            return false;
        }
        return true;
    }

    private void setReviewDiscountSubmittedListener(OnReviewDiscountSubmittedListener reviewDiscountSubmittedListener) {
        this.reviewDiscountSubmittedListener = reviewDiscountSubmittedListener;
    }

    public interface OnReviewDiscountSubmittedListener {
        void onDiscountSubmitted(double discountAmount, ReviewDiscountDialog reviewDiscountDialog);

        void onSkip(ReviewDiscountDialog reviewDiscountDialog);
    }
}
