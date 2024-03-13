package com.moverbol.views.activities.confirmation_detail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.ValuationAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ValuationActivityBinding;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;
import com.moverbol.model.valuationPage.ValuationItemPojo;
import com.moverbol.model.valuationPage.ValuationSubmissionRequestModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.BaseResponseModelSecond;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ValuationViewModel;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Admin on 25-09-2017.
 */

public class ValuationActivity extends BaseAppCompactActivity {
    private ValuationActivityBinding binding;
    private ValuationViewModel viewModel;
    private ValuationAdapter adapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialisation();

        setActionListeners();
        setViewModelObserver();

        if (viewModel.valuationItemPojoListLive.getValue() == null) {
            callGetValuation();
        }

        binding.setIsBolStarted(MoversPreferences.getInstance(this).getBolStarted(MoversPreferences.getInstance(this).getCurrentJobId()));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_complete_process, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initialisation() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_valuation);
        setToolbar(binding.toolbar.toolbar, getString(R.string.valuation), R.drawable.ic_arrow_back_white_24dp);
        viewModel = new ViewModelProvider(this).get(ValuationViewModel.class);
        adapter = new ValuationAdapter();
        binding.setAdapter(adapter);

        binding.nestedScroll.setOnTouchListener((v, event) -> {
            binding.scrollText.getParent()
                    .requestDisallowInterceptTouchEvent(false);
            return false;
        });

        binding.scrollText.setOnTouchListener((v, event) -> {
            // Disallow the touch request for parent scroll on touch of  child view
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
    }

    private void setViewModelObserver() {

        viewModel.valuationItemPojoListLive.observe(this, new Observer<List<ValuationItemPojo>>() {
            @Override
            public void onChanged(@Nullable List<ValuationItemPojo> valuationItemPojos) {
                adapter.setHomeList(valuationItemPojos);
            }
        });

        viewModel.valuationDescriptionLive.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.setDescription(s);
            }
        });

        viewModel.valuationSubmissionRequestModelLive.observe(this, new Observer<ValuationSubmissionRequestModel>() {
            @Override
            public void onChanged(@Nullable ValuationSubmissionRequestModel valuationSubmissionRequestModel) {
                if (valuationSubmissionRequestModel != null) {
                    viewModel.setSelectedArticleListItem(valuationSubmissionRequestModel.getValuationSettingsId(), valuationSubmissionRequestModel.getDeclaredAmount());
                }
            }
        });

    }


    private void setActionListeners() {
        binding.txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.signaturePad.clear();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    callSubmitValuationDetails();
                }
            }
        });
    }

    private void callSubmitValuationDetails() {
        if (!shouldMakeNetworkCall(binding.getRoot()) || getSelectedOneValuationItem() == null) {
            return;
        }

        showProgress();

        ValuationItemPojo valuationItemPojo = getSelectedOneValuationItem();

        String encoded = Util.getBase64EncodeStringFromBitmap(binding.signaturePad.getSignatureBitmap());


        String id = "";
        if (viewModel.valuationSubmissionRequestModelLive.getValue() != null) {
            id = viewModel.valuationSubmissionRequestModelLive.getValue().getId();
        }
        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();

        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        CommonChargesRequestModel valuationChargesCalculated = new CommonChargesRequestModel();

        //We have to send unit in unit id here. It is just a workaround to get the serialised name correct.
        valuationChargesCalculated.setUnitId(valuationItemPojo.getUnit());
        valuationChargesCalculated.setAmount(valuationItemPojo.getAmount());
        valuationChargesCalculated.setDescription(valuationItemPojo.getDescription());
        valuationChargesCalculated.setQuantity(valuationItemPojo.getDeclaredValue());
        valuationChargesCalculated.setRate(valuationItemPojo.getRate());

        ValuationSubmissionRequestModel valuationSubmissionRequestModel = new ValuationSubmissionRequestModel(id,
                valuationItemPojo.getId(), valuationItemPojo.getDeclaredValue(), valuationItemPojo.getRate(), valuationItemPojo.getUnit(), userId, encoded, valuationChargesCalculated);

        viewModel.submitValuationDetails(subDomain, userId, opportunityId, valuationSubmissionRequestModel, jobId, Util.BitmapToFile(this, binding.signaturePad.getSignatureBitmap()), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
//                showSubmissionSuccessfulDialog(TextUtils.equals(binding.btnSubmit.getText(), getText(R.string.update)));

//                Toast.makeText(ValuationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                if (!TextUtils.equals(binding.btnSubmit.getText(), getText(R.string.update)))
                    setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ValuationActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

    }


    private void callGetValuation() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        viewModel.getValuationMetadat(subDomain, userId, opportunityId, new ResponseListener<BaseResponseModelSecond<List<ValuationItemPojo>, String>>() {
            @Override
            public void onResponse(BaseResponseModelSecond<List<ValuationItemPojo>, String> response, String usedUrlKey) {
                hideProgress();
                callGetSubmittedValuation();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ValuationActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModelSecond<List<ValuationItemPojo>, String>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void callGetSubmittedValuation() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY)) {
            return;
        }

        showProgress();

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        viewModel.getValuationSubmittedDetails(subDomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<ValuationSubmissionRequestModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ValuationSubmissionRequestModel> response, String usedUrlKey) {
                hideProgress();
                binding.btnSubmit.setText(R.string.update);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ValuationActivity.this);
                    return;
                }

                if (!TextUtils.equals(serverResponseError.getMessage(), Constants.FORM_IS_EMPTY)) {
                    Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<BaseResponseModel<ValuationSubmissionRequestModel>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate() {
        if (getSelectedOneValuationItem() == null) {
            Snackbar.make(binding.getRoot(), getText(R.string.please_select_an_option), Snackbar.LENGTH_LONG).show();
            return false;
        }/*else if(TextUtils.isEmpty(getSelectedOneValuationItem().getDeclaredValue())){
            Snackbar.make(binding.getRoot(), "Declared value cannot be empty", Snackbar.LENGTH_LONG).show();
            return false;
        }*/ else if (binding.signaturePad.isEmpty()) {
            Snackbar.make(binding.getRoot(), R.string.signature_required_error, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private ValuationItemPojo getSelectedOneValuationItem() {
        try {
            for (int i = 0; i < viewModel.valuationItemPojoListLive.getValue().size(); i++) {
                if (viewModel.valuationItemPojoListLive.getValue().get(i).isSelected()) {
                    return viewModel.valuationItemPojoListLive.getValue().get(i);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getSelectedOneValuationItemPosition() {
        for (int i = 0; i < viewModel.valuationItemPojoListLive.getValue().size(); i++) {
            if (viewModel.valuationItemPojoListLive.getValue().get(i).isSelected()) {
                return i;
            }
        }
        return 0;
    }


    private void showSubmissionSuccessfulDialog(boolean isUpdate) {

        String message = "Details submitted successfully";

        if (isUpdate) {
            message = "Details updated successfully";
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ValuationActivity.this.finish();
                    }
                })
                .create();
        alertDialog.show();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ValuationActivity.class);
        context.startActivity(starter);
    }

    public static void startForResult(Activity context, int requestCode) {
        Intent starter = new Intent(context, ValuationActivity.class);
        context.startActivityForResult(starter, requestCode);
    }

}