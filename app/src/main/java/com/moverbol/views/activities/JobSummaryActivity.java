package com.moverbol.views.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.moverbol.R;
import com.moverbol.adapters.HomePagerAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.JobSummaryBinding;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;
import com.moverbol.views.fragments.jobsummary.BoxDeliveryFragment;
import com.moverbol.views.fragments.jobsummary.CustomerInfoFragment;

import retrofit2.Call;

/**
 * Created by sumeet on 12/9/17.
 */

public class JobSummaryActivity extends BaseAppCompactActivity implements TabLayout.OnTabSelectedListener {

    private JobSummaryBinding jobSummaryBinding;
    private HomePagerAdapter adapter;
    private JobSummaryViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobSummaryBinding = DataBindingUtil.setContentView(this, R.layout.activity_job_summary);
        viewModel = new ViewModelProvider(this).get(JobSummaryViewModel.class);

        setSupportActionBar(jobSummaryBinding.toolbar.toolbar);
        adapter = new HomePagerAdapter(getSupportFragmentManager());


        /*
            Sets all fragments once as we do not know how many fragments we need to show before we have
            the JobDetailPojo. But after we know number of fragments them again we do not need to set them.
            Hence after first observation we stop observing.
         */
        viewModel.getJobDetailLive().observe(this, new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {
                setFragments(jobDetailPojo);

                viewModel.getJobDetailLive().removeObservers(JobSummaryActivity.this);
            }
        });


        jobSummaryBinding.tabLayout2.addOnTabSelectedListener(this);

        onTabSelected(jobSummaryBinding.tabLayout2.getTabAt(0));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.job_summary);
        }

        if(viewModel.getJobDetailLive().getValue()==null)
            loadData();
    }

    private void setFragments(JobDetailPojo jobDetailPojo) {
        CustomerInfoFragment customerInfoFragment = new CustomerInfoFragment();
        adapter.addFragment(customerInfoFragment, getString(R.string.customer_info));

        if(jobDetailPojo!=null) {
            if (jobDetailPojo.getMoveStageDetailsPojoList() != null) {
                if(jobDetailPojo.getMoveStageDetailsPojoList().size()==1){
                    BoxDeliveryFragment boxDeliveryFragment = new BoxDeliveryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.KEY_LIST_POSITION, 0);
                    boxDeliveryFragment.setArguments(bundle);
                    adapter.addFragment(boxDeliveryFragment, getString(R.string.crew_and_suplies));
                } else {
                    for (int i = 0; i < jobDetailPojo.getMoveStageDetailsPojoList().size(); i++) {
                        BoxDeliveryFragment boxDeliveryFragment = new BoxDeliveryFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.KEY_LIST_POSITION, i);
                        boxDeliveryFragment.setArguments(bundle);
                        adapter.addFragment(boxDeliveryFragment, jobDetailPojo.getMoveStageDetailsPojoList().get(i).getMoveStageName());
                    }
                }
            }
        }

        jobSummaryBinding.vpJobSummary.setAdapter(adapter);
        jobSummaryBinding.tabLayout2.setupWithViewPager(jobSummaryBinding.vpJobSummary);
    }

    private void loadData() {
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String job_id = getIntent().getStringExtra(Constants.EXTRA_JOB_ID_KEY);
        MoversPreferences.getInstance(this).setJobDetailsId(job_id);
        if(!shouldMakeNetworkCall(jobSummaryBinding.getRoot())){
            return;
        }
        showProgress();
        viewModel.setJobDetails(subdomain, userId, opportunityId, job_id, new ResponseListener<BaseResponseModel<JobDetailPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobDetailPojo> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                    Util.showLoginErrorDialog(JobSummaryActivity.this);
                    return;
                }
                Snackbar.make(jobSummaryBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(jobSummaryBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
