package com.moverbol.views.activities.moveprocess.bill_of_lading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.moverbol.R;
import com.moverbol.adapters.ClockHistoryDetailsAdapter;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityBillingSummaryBinding;
import com.moverbol.model.billOfLading.ClockHistoryResponse;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ClockHistoryVM;

import java.util.Collections;

import retrofit2.Call;

public class BillingSummaryActivity extends BaseAppCompactActivity {
    private ActivityBillingSummaryBinding binding;
    private ClockHistoryVM clockHistoryVM;
    private ClockHistoryDetailsAdapter adapter;
    private static final String EXTRA_ACTIVITY_FLAG = "activity_flag";
    private String mActivityFlag = "1";

    public static void start(Context context, String activityFlag) {
        Intent starter = new Intent(context, BillingSummaryActivity.class);
        starter.putExtra(EXTRA_ACTIVITY_FLAG, activityFlag);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_billing_summary);
        setToolbar(binding.toolbar.toolbar, getString(R.string.activity_billing_summary), R.drawable.ic_arrow_back_white_24dp);
        initialisation();
        observeLiveData();
    }


    private void initialisation() {
        mActivityFlag = getIntent().getStringExtra(EXTRA_ACTIVITY_FLAG);

        clockHistoryVM = new ViewModelProvider(this).get(ClockHistoryVM.class);
        adapter = new ClockHistoryDetailsAdapter(true, mActivityFlag);
        adapter.showTotal(true);
        adapter.setShowChargesFlag(1);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.recyclerView.setHasFixedSize(true);
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
            adapter.showTotal(true);

        });

    }

    private void callClockHistoryList() {
        showProgress();
        clockHistoryVM.callClockHistoryList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getCurrentJobId(), MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<ClockHistoryResponse>>() {
            @Override
            public void onResponse(BaseResponseModel<ClockHistoryResponse> response, String usedUrlKey) {
                hideProgress();
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
