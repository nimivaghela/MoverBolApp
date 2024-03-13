package com.moverbol.views.activities.confirmation_detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.TermsAndPoliciesDialog;
import com.moverbol.databinding.EstimateConfirmationBinding;
import com.moverbol.model.confirmationPage.ConfirmationSubmitRequestModel;
import com.moverbol.model.confirmationPage.JobConfirmationDetailsPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;

import retrofit2.Call;

/**
 * Created by AkashM on 14/2/18.
 */

public class EstimateConfirmationActivity extends BaseAppCompactActivity {

    private EstimateConfirmationBinding binding;

    private ConfirmationDetailsViewModel viewModel;
    private String mMoveChargesPriceTypeIndex;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialise();

        setActionListener();
        setViewModelObservers();

        callSetJobConfirmationDetails();
    }

    private void initialise() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_estimate_confirmation);
        setToolbar(binding.toolbar.toolbar, getString(R.string.estimate_order_for_service), R.drawable.ic_arrow_back_white_24dp);


        viewModel = new ViewModelProvider(this).get(ConfirmationDetailsViewModel.class);


        binding.setCurrencySymbol(MoversPreferences.getInstance(this).getCurrencySymbol());


    }


    private void setActionListener() {

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
                    callSubmitConfirmationDetails();
                }
            }
        });

        /*binding.txtPolicies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTermsDialog("Company Policies", "Company Policies");
            }
        });

        binding.txtReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTermsDialog("Reschedule Policies", "Reschedule Policies");
            }
        });*/


    }


    private void setViewModelObservers() {
        viewModel.jobConfirmationLive.observe(this, new Observer<JobConfirmationDetailsPojo>() {
            @Override
            public void onChanged(@Nullable JobConfirmationDetailsPojo jobConfirmationDetailsPojo) {
                if (jobConfirmationDetailsPojo == null) {
                    return;
                }
                binding.setObj(jobConfirmationDetailsPojo);
                mMoveChargesPriceTypeIndex = jobConfirmationDetailsPojo.getMoveChargePriceType();

                setUpTextLinks(jobConfirmationDetailsPojo.getRescheduleTitleText(), jobConfirmationDetailsPojo.getCompanyPolicyTitleText());
            }
        });
    }


    private boolean validate() {
        if ((binding.getObj().getTermsAndConditionPojo().getTcShowFlag1() == 1 && !binding.chkReschedule.isChecked()) || (binding.getObj().getTermsAndConditionPojo().getTcShowFlag2() == 1 && !binding.chkPolicies.isChecked())) {
            Snackbar.make(binding.getRoot(), getText(R.string.policy_agreement_request_message), Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.signaturePad.isEmpty()) {
            Snackbar.make(binding.getRoot(), R.string.signature_required_error, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void callSubmitConfirmationDetails() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }


        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        String rescheduleAgreed = binding.chkReschedule.isChecked() ? "1" : "0";
        String policyAgreed = binding.chkPolicies.isChecked() ? "1" : "0";

        String signatureImageBase64String = "";

        if (!binding.signaturePad.isEmpty()) {
            signatureImageBase64String = Util.getBase64EncodeStringFromBitmap(binding.signaturePad.getSignatureBitmap());
        }


        ConfirmationSubmitRequestModel requestModel = new ConfirmationSubmitRequestModel("", rescheduleAgreed, policyAgreed, userId, signatureImageBase64String);

        showProgress();


        viewModel.submitConfirmationDetails(subDomain, userId, opportunityId, requestModel, jobId, Util.prepareFilePart("confirmation_signature", Util.BitmapToFile(this, binding.signaturePad.getSignatureBitmap()).toURI()), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                setResult(RESULT_OK, new Intent());
                EstimateConfirmationActivity.this.finish();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(EstimateConfirmationActivity.this);
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


    private void callSetJobConfirmationDetails() {
        if (viewModel.jobConfirmationLive.getValue() == null) {

            viewModel.setStoredJobConfirmation();
        }
    }

    private void openTermsDialog(final String title, final String message) {
        final TermsAndPoliciesDialog termsAndPoliciesDialog = new TermsAndPoliciesDialog();
        termsAndPoliciesDialog.setTitleAndMessage(title, message);
        termsAndPoliciesDialog.show(getSupportFragmentManager(), "dialog");
    }

    private void setUpTextLinks(String rescheduleTitleText, String companyPolicyTitleText) {
        SpannableString rescheduleSpan = new SpannableString(rescheduleTitleText);
        SpannableString companyPolicySpan = new SpannableString(companyPolicyTitleText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                openTermsDialog(binding.getObj().getTermsAndConditionPojo().getRescheduleTittle(), binding.getObj().getTermsAndConditionPojo().getcancellationAndReschedulePolicy());
            }
        };

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                openTermsDialog(binding.getObj().getTermsAndConditionPojo().getCompanyPolicyTittle(), binding.getObj().getTermsAndConditionPojo().getCompanyPolicy());
            }
        };

        rescheduleSpan.setSpan(clickableSpan, 0, rescheduleSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rescheduleSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#51b2a9")), 0, rescheduleSpan.length(), 0);

        companyPolicySpan.setSpan(clickableSpan1, 0, companyPolicySpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        companyPolicySpan.setSpan(new ForegroundColorSpan(Color.parseColor("#51b2a9")), 0, companyPolicySpan.length(), 0);
        binding.txtReschedule.setText(rescheduleSpan, TextView.BufferType.SPANNABLE);
        binding.txtReschedule.setMovementMethod(LinkMovementMethod.getInstance());

        binding.txtPolicies.setText(companyPolicySpan, TextView.BufferType.SPANNABLE);
        binding.txtPolicies.setMovementMethod(LinkMovementMethod.getInstance());


        SpannableString s1 = new SpannableString(getString(R.string.Important_notes));
        ClickableSpan clickable = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                openTermsDialog(getString(R.string.important_notes_normal), binding.getObj().getImportantNotes());
            }
        };
        s1.setSpan(clickable, 0, s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        s1.setSpan(new ForegroundColorSpan(Color.parseColor("#343434")), 0, s1.length(), 0);
        s1.setSpan(new ForegroundColorSpan(Color.parseColor("#51b2a9")), 0, s1.length(), 0);
        binding.impNotes.setText(s1, TextView.BufferType.SPANNABLE);
        binding.impNotes.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public void openStorageChargesList(View view) {
        if (view.getId() == R.id.constraint_storage_recurring_charges) {
            if (binding.getObj().getStorageChargesRecurring() > 0.00) {
                Intent intent = new Intent(this, StorageChargesActivity.class);
                intent.putExtra(StorageChargesActivity.EXTRA_TITLE, getString(R.string.storage_recurring_charges));
                intent.putExtra(StorageChargesActivity.EXTRA_MODEL_NAME, StorageChargesActivity.STORAGE_RECURRING_MODEL);
                intent.putExtra(Constants.DISCOUNT, binding.getObj().getStorageRecurringChargeDiscountValue());
                intent.putExtra(Constants.DISCOUNT_TYPE, binding.getObj().getStorageRecurringChargeDiscountType());
                startActivity(intent);
            }
            return;
        }
        if (binding.getObj().getStorageCharges() > 0.00) {
            Intent intent = new Intent(this, StorageChargesActivity.class);
            intent.putExtra(Constants.DISCOUNT, binding.getObj().getStorageChargeDiscountValue());
            intent.putExtra(Constants.DISCOUNT_TYPE, binding.getObj().getStorageChargeDiscountType());
            startActivity(intent);
        }

    }

    public void openValuationActivity(View view) {
        startActivity(new Intent(this, ValuationChargesActivity.class));
    }

    public void openAdditionalChargesActivity(View view) {
            Intent intent = new Intent(this, AdditionalChargesActivity.class);
            intent.putExtra(Constants.DISCOUNT, binding.getObj().getAdditionalChargeDiscountValue());
            intent.putExtra(Constants.DISCOUNT_TYPE, binding.getObj().getAdditionalChargeDiscountType());
            startActivity(intent);
    }

    public void openMoveChargesActivity(View view) {
        if (binding.getObj().getMoveCharges() > 0.00) {
            Intent intent = new Intent(this, MoveChargesActivity.class);
            intent.putExtra(Constants.EXTRA_MOVE_CHARGES_PRICE_TYPE_KEY, mMoveChargesPriceTypeIndex);
            intent.putExtra(Constants.DISCOUNT, binding.getObj().getMoveChargeDiscountValue());
            intent.putExtra(Constants.DISCOUNT_TYPE, binding.getObj().getMoveChargeDiscountType());
            startActivity(intent);

        }
    }

    public void openPackingChargesActivity(View view) {
        if (binding.getObj().getPackingMaterialCharges() > 0.00) {
            Intent intent = new Intent(this, PackingChargesActivity.class);
            intent.putExtra(Constants.DISCOUNT, binding.getObj().getPackingChargeDiscountValue());
            intent.putExtra(Constants.DISCOUNT_TYPE, binding.getObj().getPackingChargeDiscountType());
            intent.putExtra(Constants.SALES_TAX, binding.getObj().getPackingChargeSalesTax());
            startActivity(intent);
        }
    }

    public void openCratingChargesActivity(View view) {
        if (binding.getObj().getCratingCharges() > 0.00) {
            Intent intent = new Intent(this, CratingChargesActivity.class);
            intent.putExtra(Constants.DISCOUNT, binding.getObj().getCratingChargeDiscountValue());
            intent.putExtra(Constants.DISCOUNT_TYPE, binding.getObj().getCratingChargeDiscountType());
            startActivity(intent);
        }

    }
}
