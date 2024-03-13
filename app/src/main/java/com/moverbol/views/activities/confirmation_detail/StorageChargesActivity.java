package com.moverbol.views.activities.confirmation_detail;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.StorageAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.StorageListBinding;
import com.moverbol.model.confirmationPage.StorageChargesPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Admin on 23-09-2017.
 */

public class StorageChargesActivity extends BaseAppCompactActivity {
    public static final String STORAGE_RECURRING_MODEL = "storage_recuring";
    public static final String STORAGE_MODEL = "storage";
    public static final String EXTRA_MODEL_NAME = "model_name";
    public static final String EXTRA_TITLE = "extra_title";

    private String mSelectedModelName;
    private ConfirmationDetailsViewModel viewModel;
    private StorageListBinding binding;
    private StorageAdapter adapter;
    private double discount = 0.0;
    private double discountType = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_storage_charges);

        if (getIntent().hasExtra(Constants.DISCOUNT)) {
            discount = getIntent().getDoubleExtra(Constants.DISCOUNT, 0.0);
        }

        if (getIntent().hasExtra(Constants.DISCOUNT_TYPE)) {
            discountType = getIntent().getIntExtra(Constants.DISCOUNT_TYPE, 1);
        }

        adapter = new StorageAdapter();
        binding.setAdapter(adapter);
        binding.imgEditDiscount.setVisibility(View.GONE);

        //setToolbar(crewListBinding.to);
        viewModel = new ViewModelProvider(this).get(ConfirmationDetailsViewModel.class);
        viewModel.storageChargesPojoListLive.observe(this, new Observer<ArrayList<StorageChargesPojo>>() {
            @Override
            public void onChanged(@Nullable ArrayList<StorageChargesPojo> storageChargesPojos) {
                adapter.setHomeList(storageChargesPojos);
                setBottomHeader();
            }
        });

        String title = getString(R.string.storage_charges);

        if (getIntent().hasExtra(EXTRA_TITLE)) {
            title = getIntent().getStringExtra(EXTRA_TITLE);
        }

        if (getIntent().hasExtra(EXTRA_MODEL_NAME)) {
            mSelectedModelName = STORAGE_RECURRING_MODEL;
        } else {
            mSelectedModelName = STORAGE_MODEL;
        }

        setToolbar(binding.toolbar.toolbar, title, R.drawable.ic_arrow_back_white_24dp);

        //ToDo: Uncomment
        //Set currency symbol on titles
       /* String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();
        String txtRateText = getText(R.string.rate) + "(" + currencySymbol + ")";
        String txtAmtText = getText(R.string.amt) + "(" + currencySymbol + ")";
        binding.txtRate.setText(txtRateText);
        binding.txtAmt.setText(txtAmtText);*/
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (viewModel.storageChargesPojoListLive.getValue() == null) {
            callGetStorageCharges();
        }
    }

    private void callGetStorageCharges() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        viewModel.getStorageChargesDetails(subdomain, userId, opportunityId, mSelectedModelName, jobId, new ResponseListener<BaseResponseModel<ArrayList<StorageChargesPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<StorageChargesPojo>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(StorageChargesActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<StorageChargesPojo>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void setBottomHeader() {
        double subTotal = 0.0;
        double discountValue = 0.0;
        for (StorageChargesPojo storageChargesPojo : adapter.getList()) {
            subTotal = subTotal + Double.parseDouble(storageChargesPojo.getTotal());
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
