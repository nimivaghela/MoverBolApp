package com.moverbol.views.activities.jobsummary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.TruckAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.TruckListBinding;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.model.TruckPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;
import com.moverbol.views.activities.AddTruckActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by sumeet on 14/9/17.
 */

public class TrucksListActivity extends BaseAppCompactActivity implements View.OnClickListener {

    //    private TruckVM viewModel;
    private JobSummaryViewModel viewModel;
    private TruckListBinding truckBinding;
    private TruckAdapter adapter;
    private Observer<JobDetailPojo> jobDetailPojoObserver;

    private String mJobId;
    private String mMoveProcessJobId;
    private int mMoveStageDetailsPojoListPosition = 0;
    private List<TruckPojo> mTruckPojoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        truckBinding = DataBindingUtil.setContentView(this, R.layout.activity_truck_list);
        truckBinding.setOnClick(this);


        //setToolbar(crewListBinding.to);
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this).get(JobSummaryViewModel.class);
        }

        mTruckPojoList = new ArrayList<>();
        adapter = new TruckAdapter(mTruckPojoList, this);
        truckBinding.setAdapter(adapter);

        if (getIntent() != null) {
            if (getIntent().hasExtra(Constants.KEY_LIST_POSITION)) {
                mMoveStageDetailsPojoListPosition = getIntent().getIntExtra(Constants.KEY_LIST_POSITION, 0);
            }
        }

        setToolbar(truckBinding.toolbar.toolbar, getString(R.string.trucks), R.drawable.ic_arrow_back_white_24dp);

        jobDetailPojoObserver = new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {

                mTruckPojoList = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getTrucks();
                mJobId = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getJobId();
                mMoveProcessJobId = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getMoveProcessJobId();
                adapter.setHomeList(mTruckPojoList);
            }
        };
        //Just in case
        viewModel.getJobDetailLive().observe(this, jobDetailPojoObserver);

        viewModel.setJobDetailLive();

    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.setJobDetailLive();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                if (item.getTitle().equals(getString(R.string.remove_from_list))) {
                    adapter.setType(Constants.ITEM_SELECTION_AVILABEL);
                    item.setTitle(R.string.cancel_remove);
                    truckBinding.txtDelete.setVisibility(View.VISIBLE);
                    truckBinding.floatingActionButton.hide();
                } else if (item.getTitle().equals(getString(R.string.cancel_remove))) {
                    adapter.setType("");
                    adapter.unSelectAll();
                    item.setTitle(R.string.remove_from_list);
                    truckBinding.txtDelete.setVisibility(View.GONE);
                    truckBinding.floatingActionButton.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteCrewItems(List<TruckPojo> list) {
        showProgress();

        viewModel.deleteSelectedTruckFromList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), mMoveProcessJobId, list, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
//                hideProgress();
                refreshJobDetails(MoversPreferences.getInstance(getApplicationContext()).getJobDetailsId());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(TrucksListActivity.this);
                    return;
                }

                Snackbar.make(truckBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(truckBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void refreshJobDetails(String jobId) {
        viewModel.setJobDetails(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), jobId, new ResponseListener<BaseResponseModel<JobDetailPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobDetailPojo> response, String usedUrlKey) {
                hideProgress();
//                Snackbar.make(truckBinding.getRoot(), "Data Refreshed", Snackbar.LENGTH_LONG).show();
                //To refresh the page
                jobDetailPojoObserver.onChanged(response.getData());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(TrucksListActivity.this);
                    return;
                }

                Snackbar.make(truckBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(truckBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton:

                Intent openAddTruckIntent = new Intent(TrucksListActivity.this, AddTruckActivity.class);
                openAddTruckIntent.putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition);
                openAddTruckIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mMoveProcessJobId);
                startActivityForResult(openAddTruckIntent, 101);

                break;
            case R.id.txt_delete:
                List<TruckPojo> list = adapter.getHomeList();
                if (shouldMakeNetworkCall(truckBinding.getRoot())) {
                    deleteCrewItems(list);
                }
                break;

            case R.id.img_edit:
                int adapterPosition = (int) v.getTag();

                Intent openEditTruckIntent = new Intent(TrucksListActivity.this, AddTruckActivity.class);
                openEditTruckIntent.putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition);
                openEditTruckIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mMoveProcessJobId);
                openEditTruckIntent.putExtra(Constants.KEY_ADAPTER_POSITION, adapterPosition + "");
                openEditTruckIntent.putExtra(Constants.EXTRA_ITEM_ID_KEY, adapter.getHomeList().get(adapterPosition).getVehicleId());
                startActivityForResult(openEditTruckIntent, 101);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            refreshJobDetails(MoversPreferences.getInstance(this).getJobDetailsId());
        }

    }
}
