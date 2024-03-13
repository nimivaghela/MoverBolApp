package com.moverbol.views.activities.confirmation_detail;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.PackingChargesAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.PackingChargesBinding;
import com.moverbol.model.confirmationPage.PackingChargesPojo;
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

public class PackingChargesActivity extends BaseAppCompactActivity {

    private PackingChargesBinding binding;
    private PackingChargesAdapter adapter;
    private ConfirmationDetailsViewModel viewModel;
    private double discount = 0.0;
    private double discountType = 1;
    private double salesTax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packing_charges);
        setToolbar(binding.toolbar.toolbar, getString(R.string.packing_material_charges), R.drawable.ic_arrow_back_white_24dp);

        if (getIntent().hasExtra(Constants.DISCOUNT)) {
            discount = getIntent().getDoubleExtra(Constants.DISCOUNT, 0.0);
        }

        if (getIntent().hasExtra(Constants.DISCOUNT_TYPE)) {
            discountType = getIntent().getIntExtra(Constants.DISCOUNT_TYPE, 1);
        }

        if (getIntent().hasExtra(Constants.SALES_TAX)) {
            salesTax = getIntent().getDoubleExtra(Constants.SALES_TAX, 0);
        }

        adapter = new PackingChargesAdapter(false);
        binding.setAdapter(adapter);
        binding.imgEditDiscount.setVisibility(View.GONE);
        binding.includeView.txtTaxable.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this).get(ConfirmationDetailsViewModel.class);

        viewModel.packingChargesPojoListLive.observe(this, new Observer<ArrayList<PackingChargesPojo>>() {
            @Override
            public void onChanged(@Nullable ArrayList<PackingChargesPojo> packingChargesPojos) {
                adapter.setChargesList(packingChargesPojos);
                setBottomHeader();
            }
        });

        //Set currency symbol on titles
        String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();
        String txtRateText = getText(R.string.rate) + "(" + currencySymbol + ")";
        String txtAmtText = getText(R.string.amt) + "(" + currencySymbol + ")";
        binding.includeView.txtRate.setText(txtRateText);
        binding.includeView.txtAmt.setText(txtAmtText);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (viewModel.packingChargesPojoListLive.getValue() == null) {
            callGetPackingChargesDetails();
        }

    }

    private void callGetPackingChargesDetails() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        viewModel.getPackingChargesDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<ArrayList<PackingChargesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<PackingChargesPojo>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PackingChargesActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<PackingChargesPojo>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setBottomHeader() {
        double subTotal = 0.0;
        double discountValue = 0.0;
        for (PackingChargesPojo packingChargesPojo : adapter.getChargeList()) {
            if (packingChargesPojo.getPackTotal() != null) {
                subTotal = subTotal + Double.parseDouble(packingChargesPojo.getPackTotal());
            }
        }
        binding.txtSubTotal.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(subTotal)));

        if (discountType == Constants.NumValueTypes.NUM_VALUE_TYPE_PERCENTAGE) {
            binding.txtDiscount.setText(String.format(getString(R.string.percentage_value), Util.getGeneralFormattedDecimalString(discount)));
            discountValue = ((subTotal * discount) / 100);
        } else {
            discountValue = discount;
            binding.txtDiscount.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(discount)));
        }

        binding.txtSalesTax.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(salesTax)));

        double total = (subTotal - discountValue) + salesTax;
        binding.txtTotal.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(total)));
    }
}