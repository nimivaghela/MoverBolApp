package com.moverbol.views.activities.moveprocess;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;

import com.moverbol.R;
import com.moverbol.adapters.ReleaseFormListAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ReleaseFormNewActivityBinding;
import com.moverbol.model.releaseForm.ReleaseFormMetadataPojo;
import com.moverbol.model.releaseForm.ReleaseFormRequestModel;
import com.moverbol.model.releaseForm.ReleaseFormResponseModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ReleaseFormViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ReleaseFormNewActivity extends BaseAppCompactActivity {

    private static final int RELEASE_FORM_DETAILS_REQUEST_CODE = 100;
    private ReleaseFormNewActivityBinding binding;
    private ReleaseFormViewModel viewModel;
    private ReleaseFormListAdapter adapter;
    private ReleaseFormResponseModel mReleaseFormResponseModel;
    private String mOpportunityId;
    private List<ReleaseFormMetadataPojo> mReleaseFormMetadataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_release_form_new);

        setToolbar(binding.toolbar.toolbar, getString(R.string.release_form), R.drawable.ic_arrow_back_white_24dp);

        viewModel = ViewModelProviders.of(this).get(ReleaseFormViewModel.class);
        mReleaseFormMetadataList = new ArrayList<>();
        adapter = new ReleaseFormListAdapter();

        binding.setAdapter(adapter);

        setIntentData();
        setActionListeners();
        setLiveDataObservers();

        binding.setIsBolStarted(MoversPreferences.getInstance(this).getBolStarted(MoversPreferences.getInstance(this).getCurrentJobId()));
    }

    private void setIntentData() {
        if(getIntent()!=null && getIntent().hasExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY)){
            mOpportunityId = getIntent().getStringExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY);
        }
    }

    private void setActionListeners() {
        adapter.setListner(new ReleaseFormListAdapter.ReleaseFormTitleClickListener() {
            @Override
            public void onItemClicked(ReleaseFormMetadataPojo releaseFormMetadataPojo, int position) {
                if(releaseFormMetadataPojo==null){
                    return;
                }

                ReleaseFormDetailActivity.startForResult(ReleaseFormNewActivity.this,
                        releaseFormMetadataPojo, mReleaseFormResponseModel, mOpportunityId, RELEASE_FORM_DETAILS_REQUEST_CODE);
            }
        });
    }

    private void setLiveDataObservers() {
        viewModel.releaseFormResponseModelLive.observe(this, new Observer<ReleaseFormResponseModel>() {
            @Override
            public void onChanged(@Nullable ReleaseFormResponseModel releaseFormResponseModel) {

                mReleaseFormResponseModel = releaseFormResponseModel;
                if(mReleaseFormResponseModel!=null &&
                        mReleaseFormResponseModel.getReleaseFormMetadataPojoList()!=null) {

                    mReleaseFormMetadataList = mReleaseFormResponseModel.getReleaseFormMetadataPojoList();

                    adapter.setHomeList(mReleaseFormMetadataList);
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (viewModel.releaseFormResponseModelLive.getValue() == null) {
            callGetReleaseFormMetadata();
        }

    }


    private void callGetReleaseFormMetadata() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY)) {
            return;
        }

        showProgress();

        final String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        final String userId = MoversPreferences.getInstance(this).getUserId();
        final String opportunityId = getIntent().getStringExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY);

        viewModel.getReleaseFormMetadata(subDomain, userId, opportunityId, new ResponseListener<BaseResponseModel<ReleaseFormResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ReleaseFormResponseModel> response, String usedUrlKey) {
//                hideProgress();
                callGetReleaseFormSubmittedDetails(subDomain, userId, opportunityId, false);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                    Util.showLoginErrorDialog(ReleaseFormNewActivity.this);
                    return;
                }
                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ReleaseFormResponseModel>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void callGetReleaseFormSubmittedDetails(String subDomain, String userId, String opportunityId, boolean shouldStartProgress) {
        if(!shouldMakeNetworkCall(binding.getRoot())){
            return;
        }

        if(shouldStartProgress){
            showProgress();
        }

        String jobId= MoversPreferences.getInstance(this).getCurrentJobId();

        viewModel.getReleaseFormSubmittedDetails(subDomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<List<ReleaseFormRequestModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<ReleaseFormRequestModel>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                    Util.showLoginErrorDialog(ReleaseFormNewActivity.this);
                    return;
                }

                if(!TextUtils.equals(serverResponseError.getMessage(), Constants.FORM_IS_EMPTY)) {
                    Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<ReleaseFormRequestModel>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RELEASE_FORM_DETAILS_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                setResult(RESULT_OK);
                callGetReleaseFormMetadata();
            }
        }
    }
}

