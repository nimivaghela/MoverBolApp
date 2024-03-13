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
import com.moverbol.adapters.MaterialAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.MaterialsListBinding;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.model.MaterialPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;
import com.moverbol.views.activities.AddMaterialActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.moverbol.views.activities.AddMaterialActivity.ADD_SUCCESS;

/**
 * Created by Admin on 21-09-2017.
 */

public class MaterialsListActivity extends BaseAppCompactActivity implements View.OnClickListener {
    private JobSummaryViewModel viewModel;
    private MaterialsListBinding materialsListBinding;
    private MaterialAdapter adapter;
    private Observer<JobDetailPojo> jobDetailPojoObserver;
    private List<MaterialPojo> mMaterialPojoList;
    private String mJobId;
    private String mMoveProcessJobId;
    private int mMoveStageDetailsPojoListPosition = 0;

    private String mCurrencySymbol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialsListBinding = DataBindingUtil.setContentView(this, R.layout.activity_materials_list);

        materialsListBinding.setOnClick(this);
        //setToolbar(crewListBinding.to);
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this).get(JobSummaryViewModel.class);
        }
        /*viewModel.loadData();
        materialsListBinding.setMateriallistVM(viewModel);
        materialsListBinding.rvMaterial.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));*/

        if (getIntent() != null) {
            if (getIntent().hasExtra(Constants.KEY_LIST_POSITION)) {
                mMoveStageDetailsPojoListPosition = getIntent().getIntExtra(Constants.KEY_LIST_POSITION, 0);
            }
        }

        setToolbar(materialsListBinding.toolbar.toolbar, "Materials", R.drawable.ic_arrow_back_white_24dp);

        mCurrencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();

        mMaterialPojoList = new ArrayList<>();
        adapter = new MaterialAdapter(mMaterialPojoList, this);
        materialsListBinding.setAdapter(adapter);

        jobDetailPojoObserver = new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {
//                crewListBinding.setJobDetail(jobDetailPojo);
                mMaterialPojoList = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getMaterials();
                mJobId = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getJobId();
                mMoveProcessJobId = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getMoveProcessJobId();

                adapter.setHomeList(mMaterialPojoList);
//                materialsListBinding.totalAmt.setText(mCurrencySymbol + viewModel.getTotalMaterialCost(jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getMaterials()));
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

        if (item.getItemId() == R.id.action_delete) {
            if (item.getTitle().equals(getString(R.string.remove_from_list))) {
                adapter.setType(Constants.ITEM_SELECTION_AVILABEL);
                item.setTitle(R.string.cancel_remove);
                materialsListBinding.txtDelete.setVisibility(View.VISIBLE);
                materialsListBinding.floatingActionButton.hide();
            } else if (item.getTitle().equals(getString(R.string.cancel_remove))) {
                adapter.setType("");
                adapter.unSelectAll();
                item.setTitle(R.string.remove_from_list);
                materialsListBinding.txtDelete.setVisibility(View.GONE);
                materialsListBinding.floatingActionButton.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton:
                Intent openAddMaterialIntent = new Intent(MaterialsListActivity.this, AddMaterialActivity.class);
                openAddMaterialIntent.putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition);
                openAddMaterialIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mMoveProcessJobId);
                startActivityForResult(openAddMaterialIntent,ADD_SUCCESS);
                break;

            case R.id.txt_delete:
                List<MaterialPojo> list = adapter.getHomeList();
                if (shouldMakeNetworkCall(materialsListBinding.getRoot())) {
                    deleteMaterialItems(list);
                }
                break;

            case R.id.img_edit:
                int adapterPosition = (int) v.getTag();

                Intent openEditMaterialIntent = new Intent(MaterialsListActivity.this, AddMaterialActivity.class);
                openEditMaterialIntent.putExtra(Constants.EXTRA_ITEM_ID_KEY, adapter.getHomeList().get(adapterPosition).getMaterialId());
                openEditMaterialIntent.putExtra(Constants.KEY_LIST_POSITION, mMoveStageDetailsPojoListPosition);
                openEditMaterialIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mMoveProcessJobId);
                openEditMaterialIntent.putExtra(Constants.KEY_ADAPTER_POSITION, adapterPosition + "");
                startActivityForResult(openEditMaterialIntent, ADD_SUCCESS);

                break;
        }
    }

    private void deleteMaterialItems(List<MaterialPojo> list) {
        showProgress();

        viewModel.deleteSelectedMaterialFromList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), mMoveProcessJobId, list, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
//                hideProgress();
//                Snackbar.make(materialsListBinding.getRoot(), R.string.deleted_selected_items, Snackbar.LENGTH_LONG).show();
                refreshJobDetails(MoversPreferences.getInstance(getApplicationContext()).getJobDetailsId());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MaterialsListActivity.this);
                    return;
                }

                Snackbar.make(materialsListBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(materialsListBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void refreshJobDetails(String jobId) {
        viewModel.setJobDetails(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), jobId, new ResponseListener<BaseResponseModel<JobDetailPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobDetailPojo> response, String usedUrlKey) {
                hideProgress();
//                Snackbar.make(materialsListBinding.getRoot(), "Data Refreshed", Snackbar.LENGTH_LONG).show();
                //To refresh the page
                jobDetailPojoObserver.onChanged(response.getData());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MaterialsListActivity.this);
                    return;
                }

                Snackbar.make(materialsListBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(materialsListBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ADD_SUCCESS) {
            refreshJobDetails(MoversPreferences.getInstance(this).getJobDetailsId());
        }
    }
}
