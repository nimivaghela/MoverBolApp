package com.moverbol.views.activities.moveprocess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.moverbol.R;
import com.moverbol.adapters.WorkerDetailsAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityWorkerDetailsBinding;
import com.moverbol.model.moveProcess.ActivityItem;
import com.moverbol.model.moveProcess.WorkerDetailsModel;
import com.moverbol.model.moveProcess.WorkerItemType;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.WorkerDetailsVM;

import java.util.ArrayList;

import retrofit2.Call;

import static com.moverbol.views.activities.moveprocess.ClockBreakHistoryActivity.EXTRA_ACTIVITY_FLAG;

public class WorkerDetailsActivity extends BaseAppCompactActivity {

    private final WorkerDetailsAdapter adapter = new WorkerDetailsAdapter();
    private ActivityWorkerDetailsBinding mBinding;
    private WorkerDetailsVM workerDetailsVM;
    private String mActivityFlag = "1";


    public static void start(Context context, String activityFlag) {
        Intent starter = new Intent(context, WorkerDetailsActivity.class);
        starter.putExtra(EXTRA_ACTIVITY_FLAG, activityFlag);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_worker_details);
        setToolbar(mBinding.toolbar.toolbar, getString(R.string.worker_details), R.drawable.ic_arrow_back_white_24dp);
        workerDetailsVM = new ViewModelProvider(this).get(WorkerDetailsVM.class);

        if (getIntent().hasExtra(EXTRA_ACTIVITY_FLAG)) {
            mActivityFlag = getIntent().getStringExtra(EXTRA_ACTIVITY_FLAG);
        }

        loadData();

    }


    private void loadData() {
        mBinding.rcvWorkerList.setAdapter(adapter);
        if (workerDetailsVM.workerDetailsList.getValue() == null) {
            callWorkerDetailsList();
        }

        workerDetailsVM.workerDetailsList.observe(this, new Observer<ArrayList<ActivityItem>>() {
            @Override
            public void onChanged(ArrayList<ActivityItem> activityItems) {
                adapter.setHomeList(activityItems);
            }
        });
    }


    private void callWorkerDetailsList() {
        showProgress();
        workerDetailsVM.callWorkerDetailsList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getCurrentJobId(), MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<ArrayList<WorkerDetailsModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<WorkerDetailsModel>> response, String usedUrlKey) {
                hideProgress();
                setWorkerList(response.getData());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Util.showSnackBar(mBinding.getRoot(), serverResponseError.getMessage());
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<WorkerDetailsModel>>> call, Throwable t, String message) {
                hideProgress();
                Util.showSnackBar(mBinding.getRoot(), message);
            }
        });
    }

    private void setWorkerList(ArrayList<WorkerDetailsModel> list) {
        ArrayList<ActivityItem> activityItems = new ArrayList<ActivityItem>(0);
        for (WorkerDetailsModel workerModel : list) {

            // Add WorkerName Model
            ActivityItem workerNameModel = new ActivityItem();
            workerNameModel.setName(workerModel.getWorkername());
            workerNameModel.setItemType(WorkerItemType.WorkerName);
            activityItems.add(workerNameModel);

            // Add All Item
            activityItems.addAll(workerModel.getActivity());

            // Add Total Hours Model
            ActivityItem totalModel = new ActivityItem();
            totalModel.setTotalWorkedHours(workerModel.getTotalWorkedHours());
            totalModel.setItemType(WorkerItemType.TotalHours);
            activityItems.add(totalModel);

            // Add Total Billable Hours Model
            ActivityItem totalBillable = new ActivityItem();
            totalBillable.setTotalBillableHours(workerModel.getTotalCalBillableHours());
            totalBillable.setItemType(WorkerItemType.TotalBillableHours);
            activityItems.add(totalBillable);
        }
        workerDetailsVM.workerDetailsList.postValue(activityItems);
    }

    public long calculateTotalHours(WorkerDetailsModel workerModel) {
        long totalHours = 0;
        for (ActivityItem activityItem : workerModel.getActivity()) {
            totalHours = totalHours + activityItem.getTotalHours();
        }
        return totalHours;
    }


    public long calculateTotalBillableHours(WorkerDetailsModel workerModel) {
        long totalHours = 0;
        for (ActivityItem activityItem : workerModel.getActivity()) {
            if (activityItem.getIsBillable().equalsIgnoreCase(Constants.TRUE)) {
                totalHours = totalHours + activityItem.getTotalHours();
            }

        }
        return totalHours;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clock_history_total, menu);
        MenuItem workDetailsItem = menu.findItem(R.id.action_worker_details);
        workDetailsItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_show_total) {
            ShowTotalActivity.start(this, mActivityFlag);
        }

        return super.onOptionsItemSelected(item);
    }


}