package com.moverbol.views.activities.moveprocess;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.moverbol.R;
import com.moverbol.adapters.ClockHistoryDetailsAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ClockBreakHistoryBinding;
import com.moverbol.interfaces.OnClickChildAdpter;
import com.moverbol.model.ClockActivityModel;
import com.moverbol.model.billOfLading.ClockHistoryModel;
import com.moverbol.model.billOfLading.ClockHistoryResponse;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ClockHistoryVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;

public class ClockBreakHistoryActivity extends BaseAppCompactActivity implements OnClickChildAdpter {

    static final String EXTRA_ACTIVITY_FLAG = "extra_activity_flag";
    static final String EXTRA_JOB_ID = "extra_job_id";
    private ClockBreakHistoryBinding binding;
    private ClockHistoryDetailsAdapter adapter;
    private MoversPreferences mMoversPreferences;
    private String mJobId;
    private String mMoveDate;
    private MenuItem showTotalMenuItem;
    private MenuItem hideTotalMenuItem;
    private ClockHistoryVM clockHistoryVM;
    private String mActivityFlag = "1";
    private ClockHistoryModel consolidationModel;


    public static void start(Context context, String jobId, String moveDate, String activityFlag) {
        Intent starter = new Intent(context, ClockBreakHistoryActivity.class);
        starter.putExtra(EXTRA_JOB_ID, jobId);
        starter.putExtra(Constants.KEY_MOVE_DATE, moveDate);
        starter.putExtra(EXTRA_ACTIVITY_FLAG, activityFlag);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_clock_break_history);
        setToolbar(binding.toolbar.toolbar, getString(R.string.totals), R.drawable.ic_arrow_back_white_24dp);
        setIntentData();
        initialisation();
        setActionListeners();
        observeLiveData();


    }

    private void observeLiveData() {
        clockHistoryVM.clockHistoryList.observe(this, clockHistoryList -> {
            binding.setIsEmpty(clockHistoryList.isEmpty());
            Collections.sort(clockHistoryList, (o1, o2) -> o1.getStartTimeApp().compareTo(o2.getStartTimeApp()));
            adapter.setHomeList(clockHistoryList);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clock_history_total, menu);
        showTotalMenuItem = menu.findItem(R.id.action_show_total);
        hideTotalMenuItem = menu.findItem(R.id.action_activity_details);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_show_total:
               /* if (showTotalMenuItem != null) {
                    showTotalMenuItem.setVisible(false);
                }
                if (hideTotalMenuItem != null) {
                    hideTotalMenuItem.setVisible(true);
                }
                binding.setShouldShowTotals(true);
                adapter.showTotal(true);*/
                ShowTotalActivity.start(this, mActivityFlag);
                break;

            case R.id.action_activity_details:
                if (showTotalMenuItem != null) {
                    showTotalMenuItem.setVisible(true);
                }
                if (hideTotalMenuItem != null) {
                    hideTotalMenuItem.setVisible(false);
                }
                binding.setShouldShowTotals(false);
                adapter.showTotal(false);
                break;

            case R.id.action_worker_details:
                WorkerDetailsActivity.start(this, mActivityFlag);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialisation() {
        clockHistoryVM = new ViewModelProvider(this).get(ClockHistoryVM.class);

        adapter = new ClockHistoryDetailsAdapter(false, mActivityFlag);
        adapter.setOnClickChildAdapter(this);
        binding.setAdapter(adapter);
        if (clockHistoryVM.clockHistoryList.getValue() == null) {
            callClockHistoryList();
        }

        if (MoversPreferences.getInstance(this).isClockIsOn(mJobId)) {
            binding.fabAdd.setVisibility(View.GONE);
        } else {
            binding.fabAdd.setVisibility(View.VISIBLE);
        }

        binding.setIsBolStart(MoversPreferences.getInstance(this).getBolStarted(mJobId));

    }


    private void callClockHistoryList() {
        showProgress();
        clockHistoryVM.callClockHistoryList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getCurrentJobId(), MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<ClockHistoryResponse>>() {
            @Override
            public void onResponse(BaseResponseModel<ClockHistoryResponse> response, String usedUrlKey) {
                hideProgress();
                consolidationModel = response.getData().getConsolidation();
                clockHistoryVM.clockHistoryList.postValue(response.getData().getHistory());
                adapter.setShowChargesFlag(response.getData().getChargesMoveTypeFlag());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Util.showSnackBar(binding.getRoot(), serverResponseError.getMessage());
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ClockHistoryResponse>> call, Throwable t, String message) {
                hideProgress();
                Util.showSnackBar(binding.getRoot(), message);
            }
        });
    }




/*
    private List<ClockHistoryModel> getDriveJobDetailsList(ArrayList<ClockEntityModel> clockEntityModelList) {

        ArrayList<ClockHistoryModel> listToReturn = new ArrayList<>();

        for (int i = 0; i < clockEntityModelList.size(); i++) {
            ClockEntityModel clockFromDB = clockEntityModelList.get(i);
            ClockHistoryModel clockHistoryModel = new ClockHistoryModel();

            clockHistoryModel.setJobActivity(clockFromDB.getJobActivity());
            clockHistoryModel.setClockActivity(clockFromDB.getClockActivity());
            clockHistoryModel.setTimeStamp(Util.getFormattedDate(new Date(clockFromDB.getTimeStampMillis()), Constants.OUTPUT_DATE_FORMAT_TIMINGS_DETAILS));

            listToReturn.add(clockHistoryModel);
        }

        return listToReturn;
    }*/


   /* private List<ClockHistoryModel> getTotalsList() {

        ArrayList<ClockHistoryModel> listToReturn = new ArrayList<>();

        ArrayList<ClockEntityModel> listComplete = (ArrayList<ClockEntityModel>) MoverDatabase.getInstance(this).
                clockHistoryDao().getAllForGivenJob(mJobId);

        ArrayList<ClockEntityModel> listForPacking = (ArrayList<ClockEntityModel>) MoverDatabase.getInstance(this).
                clockHistoryDao().getAllForGivenJobActivityName(getString(R.string.packing), mJobId);

        ArrayList<ClockEntityModel> listForloading = (ArrayList<ClockEntityModel>) MoverDatabase.getInstance(this).
                clockHistoryDao().getAllForGivenJobActivityName(getString(R.string.loading), mJobId);

        ArrayList<ClockEntityModel> listForDrive = (ArrayList<ClockEntityModel>) MoverDatabase.getInstance(this).
                clockHistoryDao().getAllForGivenJobActivityName(getString(R.string.drive), mJobId);

        ArrayList<ClockEntityModel> listForUnpacking = (ArrayList<ClockEntityModel>) MoverDatabase.getInstance(this).
                clockHistoryDao().getAllForGivenJobActivityName(getString(R.string.unpacking), mJobId);

        ArrayList<ClockEntityModel> listForUnLoading = (ArrayList<ClockEntityModel>) MoverDatabase.getInstance(this).
                clockHistoryDao().getAllForGivenJobActivityName(getString(R.string.unloading), mJobId);


        if(MoversPreferences.getInstance(this).isJobHasSingleActivity(mJobId)){
            ClockHistoryModel totalForMove = getTotalModelFromList(listComplete, "Move");
            listToReturn.add(totalForMove);
        } else {
            ClockHistoryModel totalForPacking = getTotalModelFromList(listForPacking, getString(R.string.packing));
            ClockHistoryModel totalForLoading = getTotalModelFromList(listForloading, getString(R.string.loading));
            ClockHistoryModel totalForDrive = getTotalModelFromList(listForDrive, getString(R.string.drive));
            ClockHistoryModel totalForUnpacking = getTotalModelFromList(listForUnpacking, getString(R.string.unpacking));
            ClockHistoryModel totalForUnloading = getTotalModelFromList(listForUnLoading, getString(R.string.unloading));

            ClockHistoryModel totalForAll = getTotalModelFromList(listComplete, getString(R.string.total));

            listToReturn.add(totalForPacking);
            listToReturn.add(totalForLoading);
            listToReturn.add(totalForDrive);
            listToReturn.add(totalForUnpacking);
            listToReturn.add(totalForUnloading);
            listToReturn.add(totalForAll);
        }

        return listToReturn;
    }*/

   /* private ClockHistoryModel getTotalModelFromList(ArrayList<ClockEntityModel> listForPacking, String jobActivityName) {
        ClockHistoryModel clockHistoryModelToReturn = new ClockHistoryModel();
        clockHistoryModelToReturn.setJobActivity(jobActivityName);
        long totalClockTime = 0;
        long totalBreakTime = 0;
        long totalClockTimePerJob = 0;
        long totalBreakTimePerJob = 0;
        long clockInTime = 0;
        long clockOutTime = 0;
        long breakInTime = 0;
        long breakOutTime = 0;

        for (int i = 0; i < listForPacking.size(); i++) {
            ClockEntityModel clockEntityModel = listForPacking.get(i);
            if(clockEntityModel.getClockActivity().equals(Constants.ClockProcessConstants.CLOCK_IN)){
                clockInTime = clockEntityModel.getTimeStampMillis();
            } else if(clockEntityModel.getClockActivity().equals(Constants.ClockProcessConstants.BREAK_IN)){
                breakInTime = clockEntityModel.getTimeStampMillis();
            } else if(clockEntityModel.getClockActivity().equals(Constants.ClockProcessConstants.BREAK_OUT)){
                breakOutTime = clockEntityModel.getTimeStampMillis();
                totalBreakTimePerJob = totalBreakTimePerJob + breakOutTime - breakInTime;
            } else if(clockEntityModel.getClockActivity().equals(Constants.ClockProcessConstants.CLOCK_OUT)){
                clockOutTime = clockEntityModel.getTimeStampMillis();
                totalClockTimePerJob = clockOutTime - clockInTime + totalClockTimePerJob;

                totalBreakTime = totalBreakTime + totalBreakTimePerJob;
                totalClockTime = totalClockTime + totalClockTimePerJob - totalBreakTime;

                totalBreakTimePerJob = 0;
                totalClockTimePerJob = 0;
            }
        }

        clockHistoryModelToReturn.setClockActivity( Util.getTimeFormattedStringFromMillis(totalClockTime) );
        clockHistoryModelToReturn.setTimeStamp(Util.getTimeFormattedStringFromMillis(totalBreakTime));

        return clockHistoryModelToReturn;
    }*/


  /*  private void calculateTotalHourBreak(ArrayList<ClockActivityModel> activityList) {
        for (ClockHistoryModel clockHistoryModel : adapter.getHomeList()) {
            for (ClockActivityModel clockActivityModel : activityList) {
                Util.showLog("###", "clockActivityId" + clockActivityModel.getId());
                if (clockActivityModel.getId().equals(clockHistoryModel.getActivityId())) {
                    if (clockHistoryModel.getEventType().equalsIgnoreCase(getString(R.string.clock))) {
                        clockActivityModel.setTotalJobTime(clockActivityModel.getTotalJobTime() + clockHistoryModel.calculateDifference());
                    } else {
                        clockActivityModel.setTotalBreakTime(clockActivityModel.getTotalBreakTime() + clockHistoryModel.calculateDifference());
                    }
                    break;
                }
            }
        }

        ClockActivityModel footerModel = new ClockActivityModel();
        footerModel.setActivityName(getString(R.string.total));
        footerModel.setTotalJobTime(calculateListOfTotalHours(activityList));
        footerModel.setTotalBreakTime(calculateListOfTotalBreakTime(activityList));
        activityList.add(footerModel);

    }*/

    private long calculateListOfTotalBreakTime(ArrayList<ClockActivityModel> activityList) {
        long total = 0;
        for (ClockActivityModel activityModel : activityList) {
            total = total + activityModel.getTotalBreakTime();
        }
        return total;
    }

    private long calculateListOfTotalHours(ArrayList<ClockActivityModel> activityList) {
        long total = 0;
        for (ClockActivityModel activityModel : activityList) {
            total = total + (activityModel.getTotalJobTime() - activityModel.getTotalBreakTime());
        }
        return total;
    }

    private void setActionListeners() {
        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpdateClockHistoryActivity.class);
           /* if (mActivityFlag.equalsIgnoreCase(Constants.TRUE)) {
                intent.putExtra(Constants.KEY_CLOCK_HISTORY_MODEL, consolidationModel);
            }*/
            intent.putExtra(Constants.KEY_MOVE_DATE, mMoveDate);
            intent.putExtra(EXTRA_ACTIVITY_FLAG, mActivityFlag);
            startActivityForResult(intent, Constants.RQ_CLOCK_UPDATE_HISTORY);
        });
    }

    private void setIntentData() {
        if (getIntent().hasExtra(EXTRA_JOB_ID)) {
            mJobId = getIntent().getStringExtra(EXTRA_JOB_ID);
        }

        if (getIntent().hasExtra(Constants.KEY_MOVE_DATE)) {
            mMoveDate = getIntent().getStringExtra(Constants.KEY_MOVE_DATE);
        }

        if (getIntent().hasExtra(EXTRA_ACTIVITY_FLAG)) {
            mActivityFlag = getIntent().getStringExtra(EXTRA_ACTIVITY_FLAG);
        }
    }

    @Override
    public void onclickAdapter(View view, int position) {
        if (view.getId() == R.id.img_edit) {
            Intent intent = new Intent(this, UpdateClockHistoryActivity.class);
            intent.putExtra(Constants.KEY_CLOCK_HISTORY_MODEL, adapter.getItem(position));
            intent.putExtra(Constants.KEY_MOVE_DATE, mMoveDate);
            intent.putExtra(EXTRA_ACTIVITY_FLAG, mActivityFlag);
            startActivityForResult(intent, Constants.RQ_CLOCK_UPDATE_HISTORY);
        } else if (view.getId() == R.id.img_delete) {
            showDeleteAlert(adapter.getItem(position));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RQ_CLOCK_UPDATE_HISTORY && resultCode == Activity.RESULT_OK) {
            callClockHistoryList();
        }
    }

    private void showDeleteAlert(ClockHistoryModel clockHistoryModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(true)
                .setMessage(R.string.delete_activity_alert_msg)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        callDeleteApi(clockHistoryModel);
                    }
                })

                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void callDeleteApi(ClockHistoryModel clockHistoryModel) {
        showProgress();
        WebServiceManager.getInstance().deleteActivity(MoversPreferences.getInstance(this).getSubdomain(), clockHistoryModel.getStartId() + "," + clockHistoryModel.getEndId(), MoversPreferences.getInstance(this).getOpportunityId(), MoversPreferences.getInstance(this).getCurrentJobId(), MoversPreferences.getInstance(this).getUserId(), new ResponseListener<BaseResponseModel<Objects>>() {
            @Override
            public void onResponse(BaseResponseModel<Objects> response, String usedUrlKey) {
                callClockHistoryList();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Util.showSnackBar(binding.getRoot(), serverResponseError.getMessage());
            }

            @Override
            public void onFailure(Call<BaseResponseModel<Objects>> call, Throwable t, String message) {
                hideProgress();
                Util.showSnackBar(binding.getRoot(), message);
            }
        });
    }
}
