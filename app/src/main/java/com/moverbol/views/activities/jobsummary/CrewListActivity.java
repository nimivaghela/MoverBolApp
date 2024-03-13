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
import com.moverbol.adapters.CrewAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.CrewListBinding;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.model.ManPowerPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;
import com.moverbol.views.activities.AddCrewActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by sumeet on 14/9/17.
 */

public class CrewListActivity extends BaseAppCompactActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 102;
    private JobSummaryViewModel viewModel;
    private CrewAdapter adapter;
    private CrewListBinding crewListBinding;
    private Observer<JobDetailPojo> jobDetailPojoObserver;
    private int mMoveStageDetailsPojoListPosition = 0;
    private String mJobId;
    private String mMoveProcessJobId;
    private boolean shouldRefresh = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crewListBinding = DataBindingUtil.setContentView(this, R.layout.activity_crew_list);
        crewListBinding.setOnClick(this);
        //setToolbar(crewListBinding.to);
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this).get(JobSummaryViewModel.class);
        }
        /*viewModel.setJobDetailLive();
        crewListBinding.setCrewVM(viewModel);
        crewListBinding.rvCrew.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));*/

        setToolbar(crewListBinding.toolbar.toolbar, getString(R.string.crew), R.drawable.ic_arrow_back_white_24dp);

        List<ManPowerPojo> crews = new ArrayList<>();
        adapter = new CrewAdapter(crews, this);
        crewListBinding.setAdapter(adapter);

        if (getIntent() != null) {
            if (getIntent().hasExtra(Constants.KEY_LIST_POSITION)) {
                mMoveStageDetailsPojoListPosition = getIntent().getIntExtra(Constants.KEY_LIST_POSITION, 0);
            }
        }

        jobDetailPojoObserver = new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {
                adapter.setHomeList(jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getCrews());
                mJobId = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getJobId();
                mMoveProcessJobId = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getMoveProcessJobId();
            }
        };
        //Just in case
        viewModel.getJobDetailLive().observe(this, jobDetailPojoObserver);


        /*if(viewModel.jobDetailPojoLive.getValue()!=null)
        adapter.setHomeList(viewModel.jobDetailPojoLive.getValue().getCrews());*/

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
                    crewListBinding.txtDelete.setVisibility(View.VISIBLE);
                    crewListBinding.floatingActionButton.hide();
                } else if (item.getTitle().equals(getString(R.string.cancel_remove))) {
                    adapter.setType("");
                    adapter.unSelectAll();
                    item.setTitle(R.string.remove_from_list);
                    crewListBinding.txtDelete.setVisibility(View.GONE);
                    crewListBinding.floatingActionButton.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton:

                Intent openAddCrewIntent = new Intent(CrewListActivity.this, AddCrewActivity.class);
                openAddCrewIntent.putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition);
                openAddCrewIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mMoveProcessJobId);
                startActivityForResult(openAddCrewIntent, REQUEST_CODE);
                break;

            case R.id.txt_delete:
                List<ManPowerPojo> list = adapter.getHomeList();
                if (shouldMakeNetworkCall(crewListBinding.getRoot())) {
                    deleteCrewItems(list);
                }
                break;

            case R.id.img_edit:

                int adapterPosition = (int) v.getTag();
                Intent openEditCrewIntent = new Intent(CrewListActivity.this, AddCrewActivity.class);
                openEditCrewIntent.putExtra(Constants.EXTRA_ITEM_ID_KEY, adapter.getHomeList().get(adapterPosition).getManPowerId());
                openEditCrewIntent.putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition);
                openEditCrewIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mMoveProcessJobId);
                openEditCrewIntent.putExtra(Constants.KEY_ADAPTER_POSITION, adapterPosition + "");
                openEditCrewIntent.putExtra(AddCrewActivity.EXTRA_MAN_POWER_POJO, adapter.getHomeList().get(adapterPosition));
                startActivityForResult(openEditCrewIntent, REQUEST_CODE);

                break;
        }
    }

    private void deleteCrewItems(List<ManPowerPojo> list) {
        showProgress();

        viewModel.deleteSelectedCrewFromList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), mMoveProcessJobId, list, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
//                hideProgress();
//                Snackbar.make(crewListBinding.getRoot(), R.string.deleted_selected_items, Snackbar.LENGTH_LONG).show();
                refreshJobDetails(MoversPreferences.getInstance(getApplicationContext()).getJobDetailsId());
//                refreshJobDetails(mJobId);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(CrewListActivity.this);
                    return;
                }

                Snackbar.make(crewListBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(crewListBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void refreshJobDetails(String jobId) {
        viewModel.setJobDetails(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), jobId, new ResponseListener<BaseResponseModel<JobDetailPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobDetailPojo> response, String usedUrlKey) {
                hideProgress();
//                Snackbar.make(crewListBinding.getRoot(), "Data Refreshed", Snackbar.LENGTH_LONG).show();
                //To refresh the page
                jobDetailPojoObserver.onChanged(response.getData());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(CrewListActivity.this);
                    return;
                }

                Snackbar.make(crewListBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(crewListBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            refreshJobDetails(MoversPreferences.getInstance(this).getCurrentJobId());
        }
    }
}
