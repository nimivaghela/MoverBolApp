package com.moverbol.views.activities.moveprocess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.StorageAgreementBinding;
import com.moverbol.model.rentalAgreement.RentalAgreementPojo;
import com.moverbol.model.rentalAgreement.RentalAgreementResponseModel;
import com.moverbol.model.rentalAgreement.RentalAgreementSubmittedDetailsPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.RentalAgreementViewModel;

import retrofit2.Call;

/**
 * Created by Admin on 10-10-2017.
 */

public class StorageAgreementActivity extends BaseAppCompactActivity {

    private StorageAgreementBinding binding;
    private RentalAgreementViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_storage_agreement);
        setToolbar(binding.toolbar.toolbar, getString(R.string.storage_agreement), R.drawable.ic_arrow_back_white_24dp);

        viewModel = new ViewModelProvider(this).get(RentalAgreementViewModel.class);

        binding.tvStorageDetails.setMovementMethod(new ScrollingMovementMethod());

        viewModel.rentalAgreementPojoLive.observe(this, rentalAgreementPojo ->
                binding.setObj(rentalAgreementPojo));

        viewModel.storageChargeModelLiveData.observe(this, storageChargeModel -> {
            binding.setStorageChargeModel(storageChargeModel);
        });

        binding.edtxtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    return;
                }

                Log.d("TextWatcher", "Editable is not empty");
                if (TextUtils.isDigitsOnly(editable) && editable.length() == 10) {
                    Log.d("TextWatcher", "length is 10");
                    editable.replace(0, editable.length(), Util.getFormatedPhoneNumber(editable.toString()));
                }

            }
        });

        binding.setIsBolStarted(MoversPreferences.getInstance(this).getBolStarted(MoversPreferences.getInstance(this).getCurrentJobId()));

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (viewModel.rentalAgreementPojoLive.getValue() == null) {
            callGetRentalAgreement();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void callGetRentalAgreement() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY)) {
            return;
        }

        showProgress();

        final String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        final String userId = MoversPreferences.getInstance(this).getUserId();
        final String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        final String opportunityId = getIntent().getStringExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY);

        viewModel.getRentalAgreement(subDomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<RentalAgreementResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<RentalAgreementResponseModel> response, String usedUrlKey) {
                hideProgress();
                callGetRentalAgreementSubmittedDetails(subDomain, opportunityId, userId);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(StorageAgreementActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<RentalAgreementResponseModel>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void callGetRentalAgreementSubmittedDetails(String subDomain, String opportunityId, String userId) {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            hideProgress();
            return;
        }

//        showProgress();

        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();

        viewModel.getRentalAgreementSubmittedDetails(subDomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<RentalAgreementSubmittedDetailsPojo> response, String usedUrlKey) {
                hideProgress();
                //binding.btnSubmit.setText(R.string.update);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(StorageAgreementActivity.this);
                    return;
                }

                if (!TextUtils.equals(serverResponseError.getMessage(), Constants.FORM_IS_EMPTY)) {
                    Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    public void submitAgreement(View view) {
        if (validate())
            callSubmitRentalAgreement();
    }

    public void clearSignature(View view) {
        binding.signaturePad.clear();
    }

    private void callSubmitRentalAgreement() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY)) {
            return;
        }

        showProgress();

        RentalAgreementPojo rentalAgreementPojo = viewModel.rentalAgreementPojoLive.getValue();

        String encoded = Util.getBase64EncodeStringFromBitmap(binding.signaturePad.getSignatureBitmap());

        String id = viewModel.r_id;
        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();

        String opportunityId = getIntent().getStringExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY);

        rentalAgreementPojo.setStorageSignature(encoded);
        rentalAgreementPojo.setUserId(userId);
        rentalAgreementPojo.setId(id);
        rentalAgreementPojo.setRentalRatePerMonth("");
        rentalAgreementPojo.setDateOfLease(Util.getFormattedCurrentDate(Constants.INPUT_DATE_FORMAT_JOBS));

        viewModel.submitRentalAgreementDetails(subDomain, userId, opportunityId, rentalAgreementPojo, jobId, Util.BitmapToFile(this, binding.signaturePad.getSignatureBitmap()),
                new ResponseListener<BaseResponseModel>() {
                    @Override
                    public void onResponse(BaseResponseModel response, String usedUrlKey) {
                        hideProgress();
//                showSubmitionSuccessfullDialog(TextUtils.equals(binding.btnSubmit.getText(), getText(R.string.update)));
                        if (!TextUtils.equals(binding.btnSubmit.getText(), getText(R.string.update)))
                            setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                        hideProgress();

                        if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                            Util.showLoginErrorDialog(StorageAgreementActivity.this);
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


    private boolean validate() {
        if (TextUtils.isEmpty(binding.edtxtName.getText().toString())) {
            binding.edtxtName.setError(getString(R.string.empty_field_error));
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtPhone.getText().toString())) {
            binding.edtxtPhone.setError(getString(R.string.empty_field_error));
            return false;
        } else if (!Patterns.PHONE.matcher(binding.edtxtPhone.getText().toString()).matches()) {
            binding.edtxtPhone.setError(getString(R.string.invalid_phone_error));
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtAddress.getText().toString())) {
            binding.edtxtAddress.setError(getString(R.string.empty_field_error));
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtZip.getText().toString())) {
            binding.edtxtZip.setError(getString(R.string.empty_field_error));
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtCity.getText().toString())) {
            binding.edtxtCity.setError(getString(R.string.empty_field_error));
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtState.getText().toString())) {
            binding.edtxtState.setError(getString(R.string.empty_field_error));
            return false;
        } else if (binding.signaturePad.isEmpty()) {
            Snackbar.make(binding.getRoot(), R.string.signature_required_error, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void showSubmitionSuccessfullDialog(boolean isUpdate) {

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
                        StorageAgreementActivity.this.finish();
                    }
                })
                .create();
        alertDialog.show();
    }

    public void openStorageChargesList(View view) {
      /*  StorageChargesBolActivity.startForResult(this, STORAGE_CHARGES_REQUEST_CODE, mBillOfLadingPojo.isStorageChargeChanged(),
                mBolFinalChargeId, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Volume(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Weight(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Miles(),
                mBillOfLadingPojo.getTotalCalculatedMoveCharges(),
                mBillOfLadingPojo.getTotalCalculatedMoveDiscounted(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveTypeId());*/
    }

    public void openStorageRecurringChargesList(View view) {
       /* StorageRecurringChargesBolActivity.startForResult(this, STORAGE_RECURRING_CHARGES_REQUEST_CODE, mBillOfLadingPojo.isStorageRecurringChargeChanged(),
                mBolFinalChargeId, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Volume(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Weight(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Miles(),
                mBillOfLadingPojo.getTotalCalculatedMoveCharges(),
                mBillOfLadingPojo.getTotalCalculatedMoveDiscounted(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveTypeId());*/

    }
}
