package com.moverbol.views.activities.confirmation_detail;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.CratingChargesAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.CratingChargesBinding;
import com.moverbol.model.confirmationPage.CratingChargesPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

import java.util.ArrayList;

import retrofit2.Call;


/**
 * Created by AkashM on 09/5/18.
 */
public class CratingChargesActivity extends BaseAppCompactActivity {

    private CratingChargesAdapter adapter;
    private ConfirmationDetailsViewModel viewModel;
    private CratingChargesBinding binding;
    private double discount = 0.0;
    private double discountType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crating_charges);
        setToolbar(binding.toolbar.toolbar, getString(R.string.crating_charges), R.drawable.ic_arrow_back_white_24dp);
        binding.imgEditDiscount.setVisibility(View.GONE);

        if (getIntent().hasExtra(Constants.DISCOUNT)) {
            discount = getIntent().getDoubleExtra(Constants.DISCOUNT, 0.0);
        }

        if (getIntent().hasExtra(Constants.DISCOUNT_TYPE)) {
            discountType = getIntent().getIntExtra(Constants.DISCOUNT_TYPE, 1);
        }

        viewModel = new ViewModelProvider(this).get(ConfirmationDetailsViewModel.class);

        viewModel.cratingChargesPojoListLive.observe(this, new Observer<ArrayList<CratingChargesPojo>>() {
            @Override
            public void onChanged(@Nullable ArrayList<CratingChargesPojo> cratingChargesPojos) {
                adapter.setChargesList(cratingChargesPojos);
                setBottomHeader();
            }
        });

        adapter = new CratingChargesAdapter();
        binding.setAdapter(adapter);

        //Set currency symbol on titles
        String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();
        //ToDo:
        /*String txtRateText = getText(R.string.rate) + "(" + currencySymbol + ")";
        String txtAmtText = getText(R.string.amt) + "(" + currencySymbol + ")";
        binding.txtRate.setText(txtRateText);
        binding.txtAmt.setText(txtAmtText);*/

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(viewModel.cratingChargesPojoListLive.getValue()==null){
            callGetCratinglCharges();
        }
    }


    private void callGetCratinglCharges() {

        if(!shouldMakeNetworkCall(binding.getRoot())){
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        viewModel.getCratingChargesDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<ArrayList<CratingChargesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<CratingChargesPojo>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                    Util.showLoginErrorDialog(CratingChargesActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CratingChargesPojo>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

    }


    private void setBottomHeader() {
        double subTotal = 0.0;
        double discountValue = 0.0;
        for (CratingChargesPojo cratingChargesPojo : adapter.getChargesList()) {
            subTotal = subTotal + Double.parseDouble(cratingChargesPojo.getTotal());
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
