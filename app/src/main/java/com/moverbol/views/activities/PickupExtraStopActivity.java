package com.moverbol.views.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.PickupStopBinding;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;

import retrofit2.Call;

/**
 * Created by sumeet on 13/9/17.
 */

public class PickupExtraStopActivity extends BaseAppCompactActivity {

    private JobSummaryViewModel viewModel;
    private PickupStopBinding pickupStopBinding;
    private String title;
    private String jobId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(JobSummaryViewModel.class);

        pickupStopBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_up_stop);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString(Constants.EXTRA_TITLE);
            setToolbar(pickupStopBinding.toolbar.toolbar, title, R.drawable.ic_arrow_back_white_24dp);
        }

        if(getIntent().hasExtra(Constants.EXTRA_JOB_ID_KEY)){
            jobId = bundle.getString(Constants.EXTRA_JOB_ID_KEY);
        }

        viewModel.getJobDetailLive().observe(this, new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {
                pickupStopBinding.setJobDetail(viewModel.getJobDetailLive().getValue());
            }
        });

        //Just in case
        viewModel.getJobDetailLive().observe(this, new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {
                pickupStopBinding.setJobDetail(jobDetailPojo);
            }
        });

        if(jobId==null){
            viewModel.setJobDetailLive();
        }

        if(jobId!= null && viewModel.getJobDetailLive().getValue()==null){
            callGetJobDetails(jobId);
        }

        String deliveryTitle = getString(R.string.delivery_extra_stops);

        if(title.equals(deliveryTitle)){
            pickupStopBinding.setShouldTakeDeliveryAddresses(true);
        }else {
            pickupStopBinding.setShouldTakeDeliveryAddresses(false);
        }
    }

    private void callGetJobDetails(String jobId) {
        if(!shouldMakeNetworkCall(pickupStopBinding.getRoot())){
            return;
        }

        showProgress();
        viewModel.setJobDetails(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), jobId, new ResponseListener<BaseResponseModel<JobDetailPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobDetailPojo> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                    Util.showLoginErrorDialog(PickupExtraStopActivity.this);
                    return;
                }
                Snackbar.make(pickupStopBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(pickupStopBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
