package com.moverbol.views.activities;

import static com.moverbol.constants.Constants.TRUE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.WalkThroughActivityBinding;
import com.moverbol.model.confirmationPage.JobConfirmationDetailsPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ConfirmationDetailsViewModel;
import com.moverbol.views.activities.confirmation_detail.ArticlesListActivity;
import com.moverbol.views.activities.confirmation_detail.EstimateConfirmationActivity;
import com.moverbol.views.activities.confirmation_detail.ValuationActivity;
import com.moverbol.views.activities.moveprocess.ReleaseFormNewActivity;
import com.moverbol.views.activities.moveprocess.StorageAgreementActivity;
import com.moverbol.views.activities.moveprocess.photos.PhotosActivity;

import retrofit2.Call;

/**
 * Created by Admin on 21-09-2017.
 */

public class WalkThroughActivity extends BaseAppCompactActivity {

    public static final String EXTRA_FAILED_TO_GET_DATA_MESSAGE = "extra_failed_to_get_data";
    public static final int RESULT_FAILED_TO_GET_DATA = 100;
    private static final int ESTIMATE_CONFIRMATION_CODE = 100;
    private static final int ARTICLE_LIST_CODE = 101;
    private static final int STORAGE_AGREEMENT_CODE = 102;
    private static final int VALUATION_CODE = 103;
    private static final int RELEASE_FORM_CODE = 104;
    private static final int PHOTOS_CODE = 105;
    private WalkThroughActivityBinding binding;
    private ConfirmationDetailsViewModel viewModel;
    private String mJobId;
    private String mOpportunityId;
    private String mMoveChargesPriceTypeIndex;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialise();

        setIntentData();

        setViewModelObservers();

        setActionListener();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (viewModel.jobConfirmationLive.getValue() == null) {
            callSetJobConfirmationDetails();
        }

    }

    private void initialise() {
        binding = DataBindingUtil.setContentView(this, R.layout.layout_walk_through);
        setToolbar(binding.toolbar.toolbar, getString(R.string.walk_through), R.drawable.ic_arrow_back_white_24dp);

        viewModel = new ViewModelProvider(this).get(ConfirmationDetailsViewModel.class);

        binding.text.setMovementMethod(new ScrollingMovementMethod());

        binding.setCurrencySymbol(MoversPreferences.getInstance(this).getCurrencySymbol());

        //String moveTypeId = MoversPreferences.getInstance(this).getCurrentJobMoveTypeId();
        //binding.setIsLocal(TextUtils.equals(moveTypeId, Constants.MoveTypeIds.MOVE_TYPE_LOCAL));
    }


    private void setIntentData() {
        if (getIntent() != null && getIntent().hasExtra(Constants.EXTRA_JOB_ID_KEY)) {
            mJobId = getIntent().getStringExtra(Constants.EXTRA_JOB_ID_KEY);
        }
    }

    private void setViewModelObservers() {
        viewModel.jobConfirmationLive.observe(this, new Observer<JobConfirmationDetailsPojo>() {
            @Override
            public void onChanged(@Nullable JobConfirmationDetailsPojo jobConfirmationDetailsPojo) {
                binding.setObj(jobConfirmationDetailsPojo);
                mOpportunityId = jobConfirmationDetailsPojo.getOpportunityId();
                mMoveChargesPriceTypeIndex = jobConfirmationDetailsPojo.getMoveChargePriceType();
                MoversPreferences.getInstance(WalkThroughActivity.this).setBolStarted(jobConfirmationDetailsPojo.getBolStarted(), mJobId);
                //showAlertDialog();
                showEstimationStatus(jobConfirmationDetailsPojo);
                showValuationStatus(jobConfirmationDetailsPojo);
            }
        });
    }

    private void setActionListener() {

        binding.txtPickupAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = binding.txtPickupAddress.getText().toString();
                Util.openMapForGivenAddress(address, v.getContext());
            }
        });

        binding.txtDropAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = binding.txtDropAddress.getText().toString();
                Util.openMapForGivenAddress(address, v.getContext());
            }
        });

    }


    private void callSetJobConfirmationDetails() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        viewModel.setJobConfirmation(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), mJobId, new ResponseListener<BaseResponseModel<JobConfirmationDetailsPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobConfirmationDetailsPojo> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(WalkThroughActivity.this);
                    return;
                }
                setActivityResult(serverResponseError.getMessage());
                finish();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobConfirmationDetailsPojo>> call, Throwable t, String message) {
                hideProgress();
                setActivityResult(message);
                finish();
            }
        });
    }


    public void openExtraStops(View view) {
        if (view.getId() == R.id.pickup_dropdown) {
            String title = getString(R.string.pickup_extra_stops);

            Intent openExtraStopsIntent = new Intent(WalkThroughActivity.this, PickupExtraStopActivity.class);
            openExtraStopsIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mJobId);
            openExtraStopsIntent.putExtra(Constants.EXTRA_TITLE, title);

            startActivity(openExtraStopsIntent);

        } else if (view.getId() == R.id.drop_dropdown) {
            String title = getString(R.string.delivery_extra_stops);

            Intent openExtraStopsIntent = new Intent(WalkThroughActivity.this, PickupExtraStopActivity.class);
            openExtraStopsIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mJobId);
            openExtraStopsIntent.putExtra(Constants.EXTRA_TITLE, title);

            startActivity(openExtraStopsIntent);
        }
    }


    public void openEstimateActivity(View view) {
        startActivityForResult(new Intent(this, EstimateConfirmationActivity.class), ESTIMATE_CONFIRMATION_CODE);
    }

    public void openArticlesList(View view) {
        startActivityForResult(new Intent(this, ArticlesListActivity.class).putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, mOpportunityId), ARTICLE_LIST_CODE);
    }

    public void openStorageAgreementActivity(View view) {
        startActivityForResult(new Intent(this, StorageAgreementActivity.class).putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, mOpportunityId), STORAGE_AGREEMENT_CODE);
    }

    public void openReleaseFormActivity(View view) {
        //TODO:Check this
//        startActivityForResult(new Intent(this, ReleaseFormActivity.class).putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, mOpportunityId), RELEASE_FORM_CODE);
        startActivityForResult(new Intent(this, ReleaseFormNewActivity.class).putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, mOpportunityId), RELEASE_FORM_CODE);
    }

    public void openValuationActivity(View view) {
        startActivityForResult(new Intent(this, ValuationActivity.class).putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, mOpportunityId), VALUATION_CODE);
    }

    public void openPhotos(View view) {
        startActivityForResult(new Intent(this, PhotosActivity.class).putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, mOpportunityId), PHOTOS_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ESTIMATE_CONFIRMATION_CODE || requestCode == RELEASE_FORM_CODE || requestCode == ARTICLE_LIST_CODE
                || requestCode == VALUATION_CODE || requestCode == STORAGE_AGREEMENT_CODE || requestCode == PHOTOS_CODE) {
            if (resultCode == RESULT_OK) {
                callSetJobConfirmationDetails();
            }
        }

    }

    private void setActivityResult(String message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FAILED_TO_GET_DATA_MESSAGE, message);
        setResult(RESULT_FAILED_TO_GET_DATA, intent);
    }

    public static void start(Context context, String jobId) {
        Intent starter = new Intent(context, WalkThroughActivity.class);
        starter.putExtra(Constants.EXTRA_JOB_ID_KEY, jobId);
        context.startActivity(starter);
    }

    private void showAlertDialog() {
        String msg = Util.getEstimationValuationAlert(this, binding.getObj().getIsEstimateUpdated(), binding.getObj().getValuationFlag());
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog.setTitle(getString(R.string.app_name));
        dialog.setMessage(msg);
        dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        if (!msg.isEmpty()) {
            dialog.show();
        }

    }

    private void showEstimationStatus(JobConfirmationDetailsPojo jobConfirmationDetailsPojo) {

        if (jobConfirmationDetailsPojo.getSubmitFlag()) {
            if (jobConfirmationDetailsPojo.getIsEstimateUpdated().equalsIgnoreCase(TRUE)) {
                binding.txtCompletedConfi.setText(R.string.signature_required);
                binding.txtCompletedConfi.setTextColor(Color.RED);
            } else {
                binding.txtCompletedConfi.setText(R.string.completed);
                binding.txtCompletedConfi.setTextColor(Color.GRAY);
            }
        } else {
            binding.txtCompletedConfi.setText(R.string.signature_required);
            binding.txtCompletedConfi.setTextColor(Color.RED);
        }

    }

    private void showValuationStatus(JobConfirmationDetailsPojo jobConfirmationDetailsPojo) {

        if (jobConfirmationDetailsPojo.getValuationFlag().equalsIgnoreCase(TRUE)) {
            binding.txtCompletedValuation.setText(R.string.completed);
            binding.txtCompletedValuation.setTextColor(Color.GRAY);
        } else {
            binding.txtCompletedValuation.setText(R.string.signature_required);
            binding.txtCompletedValuation.setTextColor(Color.RED);
        }

    }

}