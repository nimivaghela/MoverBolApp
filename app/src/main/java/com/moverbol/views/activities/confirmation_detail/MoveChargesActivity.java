package com.moverbol.views.activities.confirmation_detail;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.MoveChargesManualPricingAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.MoveChargesBinding;
import com.moverbol.model.confirmationPage.MoveChargesManualPricingPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

import java.util.ArrayList;

import retrofit2.Call;


/**
 * Created by Admin on 27-09-2017.
 */

public class MoveChargesActivity extends BaseAppCompactActivity {

    private ConfirmationDetailsViewModel viewModel;
    private MoveChargesBinding binding;
    private MoveChargesManualPricingAdapter adapter;
    private String mMoveChargesPriceType = "";
    private double discount = 0.0;
    private double discountType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_move_charges);
        setToolbar(binding.toolbar.toolbar, getString(R.string.move_charges), R.drawable.ic_arrow_back_white_24dp);
        binding.imgEditDiscount.setVisibility(View.GONE);


        if (getIntent().hasExtra(Constants.EXTRA_MOVE_CHARGES_PRICE_TYPE_KEY)) {
            mMoveChargesPriceType = getIntent().getStringExtra(Constants.EXTRA_MOVE_CHARGES_PRICE_TYPE_KEY);
        }

        if (getIntent().hasExtra(Constants.DISCOUNT)) {
            discount = getIntent().getDoubleExtra(Constants.DISCOUNT, 0.0);
        }

        if (getIntent().hasExtra(Constants.DISCOUNT_TYPE)) {
            discountType = getIntent().getIntExtra(Constants.DISCOUNT_TYPE, 1);
        }

        String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();

        String txtRateText = getText(R.string.rate) + "(" + currencySymbol + ")";
        String txtAmtText = getText(R.string.amt) + "(" + currencySymbol + ")";

        binding.includeViewManualPricing.txtRate.setText(txtRateText);
        binding.includeViewManualPricing.txtAmt.setText(txtAmtText);

        //setToolbar(crewListBinding.to);
        viewModel = new ViewModelProvider(this).get(ConfirmationDetailsViewModel.class);

        viewModel.moveChargesPricingPojoListLive.observe(this, moveChargesManualPricingPojos -> {
            adapter = new MoveChargesManualPricingAdapter(false);
            adapter.setChargesList(moveChargesManualPricingPojos);
            binding.setAdapter(adapter);
            if (moveChargesManualPricingPojos != null) {
                setBottomHeader(moveChargesManualPricingPojos);
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Util.showLog("MoverChargesActivity onPostCreate", "MoverChargesType :" + mMoveChargesPriceType);

        if (viewModel.moveChargesPricingPojoListLive.getValue() == null) {
            callGetMoveChargesDetailsCharges();
        }
    }

    private void callGetMoveChargesDetailsCharges() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        viewModel.getMoveChargesDetails(subdomain, userId, opportunityId, mMoveChargesPriceType, jobId, new ResponseListener() {
            @Override
            public void onResponse(Object response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MoveChargesActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setBottomHeader(ArrayList<MoveChargesManualPricingPojo> list) {
        double subTotal = 0.0;
        double discountValue = 0.0;
        for (MoveChargesManualPricingPojo moveChargesManualPricingPojo :
                list) {
            subTotal = subTotal + Double.parseDouble(moveChargesManualPricingPojo.getTotalAmount());
        }
        binding.txtSubTotal.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(subTotal)));

        if (discountType == 2) {
            binding.txtDiscount.setText(String.format(getString(R.string.percentage_value), Util.getGeneralFormattedDecimalString(discount)));
            discountValue = ((subTotal * discount) / 100);
        } else {
            discountValue = discount;
            binding.txtDiscount.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(discount)));
        }
        double total = subTotal - discountValue;
        binding.txtTotal.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(total)));
    }
}
