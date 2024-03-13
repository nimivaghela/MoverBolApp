package com.moverbol.views.activities.moveprocess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.moverbol.R;
import com.moverbol.adapters.ActivityTotalAdapter;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityShowTotalBinding;
import com.moverbol.model.billOfLading.ClockHistoryResponse;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ClockHistoryVM;

import java.util.Collections;

import retrofit2.Call;

import static com.moverbol.views.activities.moveprocess.ClockBreakHistoryActivity.EXTRA_ACTIVITY_FLAG;

public class ShowTotalActivity extends BaseAppCompactActivity {
    ActivityShowTotalBinding binding;
    private ClockHistoryVM clockHistoryVM;
    private ActivityTotalAdapter adapter;
    private String mActivityFlag = "1";

    public static void start(Context context, String activityFlag) {
        Intent starter = new Intent(context, ShowTotalActivity.class);
        starter.putExtra(EXTRA_ACTIVITY_FLAG, activityFlag);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_total);
        setToolbar(binding.toolbar.toolbar, getString(R.string.activity_total), R.drawable.ic_arrow_back_white_24dp);
        setIntentData();
        initialisation();
        observeLiveData();
    }

    private void setIntentData() {
     /*   if (getIntent().hasExtra(EXTRA_JOB_ID)) {
            mJobId = getIntent().getStringExtra(EXTRA_JOB_ID);
        }

        if (getIntent().hasExtra(Constants.KEY_MOVE_DATE)) {
            mMoveDate = getIntent().getStringExtra(Constants.KEY_MOVE_DATE);
        }*/

        if (getIntent().hasExtra(EXTRA_ACTIVITY_FLAG)) {
            mActivityFlag = getIntent().getStringExtra(EXTRA_ACTIVITY_FLAG);
        }
    }


    private void initialisation() {
        clockHistoryVM = new ViewModelProvider(this).get(ClockHistoryVM.class);
        adapter = new ActivityTotalAdapter(false, mActivityFlag);
        binding.recyclerView.setAdapter(adapter);

        if (clockHistoryVM.clockHistoryList.getValue() == null) {
            callClockHistoryList();
        }
    }

    private void observeLiveData() {
        clockHistoryVM.clockHistoryList.observe(this, clockHistoryList -> {
            binding.setIsEmpty(clockHistoryList.isEmpty());
            Collections.sort(clockHistoryList, (o1, o2) -> o1.getStartTimeApp().compareTo(o2.getStartTimeApp()));
            adapter.setHomeList(clockHistoryList);
        });
    }


    private void callClockHistoryList() {
        showProgress();
        clockHistoryVM.callClockHistoryList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getCurrentJobId(), MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<ClockHistoryResponse>>() {
            @Override
            public void onResponse(BaseResponseModel<ClockHistoryResponse> response, String usedUrlKey) {
                hideProgress();
                adapter.setShowChargesFlag(response.getData().getChargesMoveTypeFlag());
                clockHistoryVM.clockHistoryList.postValue(response.getData().getHistory());
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
}