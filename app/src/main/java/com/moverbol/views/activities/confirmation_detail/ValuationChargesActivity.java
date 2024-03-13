package com.moverbol.views.activities.confirmation_detail;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.ValuationChargesAdapter;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ValuationChargesBinding;
import com.moverbol.model.confirmationPage.ValuationChargesPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by AkashM on 23/1/18.
 */

public class ValuationChargesActivity extends BaseAppCompactActivity {

    private ConfirmationDetailsViewModel viewModel;
    private ValuationChargesBinding binding;
    private ValuationChargesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_valuation_charges);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_valuation_charges);

        adapter = new ValuationChargesAdapter(false);
        binding.setAdapter(adapter);
        binding.layoutBottomTotal.setVisibility(View.GONE);

        //setToolbar(crewListBinding.to);
        viewModel = new ViewModelProvider(this).get(ConfirmationDetailsViewModel.class);

        viewModel.valuationChargesPojoListLive.observe(this, new Observer<ArrayList<ValuationChargesPojo>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ValuationChargesPojo> valuationChargesPojos) {
                adapter.setChargesList(valuationChargesPojos);
            }
        });

        setToolbar(binding.toolbar.toolbar, getString(R.string.valuation_charges), R.drawable.ic_arrow_back_white_24dp);

        //Set currency symbol on titles
        String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();
        String txtRateText = getText(R.string.rate) + "(" + currencySymbol + ")";
        String txtAmtText = getText(R.string.amt) + "(" + currencySymbol + ")";
        binding.rvHrader.txtRate.setText(txtRateText);
        binding.rvHrader.txtAmt.setText(txtAmtText);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (viewModel.valuationChargesPojoListLive.getValue() == null) {
            callGetValuationCharges();
        }
    }

    private void callGetValuationCharges() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        viewModel.getValuationChargesDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<ArrayList<ValuationChargesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ValuationChargesPojo>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ValuationChargesActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ValuationChargesPojo>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
