package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.model.billOfLading.ClockHistoryModel;
import com.moverbol.model.billOfLading.ClockHistoryResponse;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import java.util.ArrayList;

public class ClockHistoryVM extends ViewModel {

    public MutableLiveData<ArrayList<ClockHistoryModel>> clockHistoryList = new MutableLiveData<>();

    public void callClockHistoryList(String domain, String userId, String jobId, String opportunityId, ResponseListener<BaseResponseModel<ClockHistoryResponse>> responseListener) {
        DataRepository.getInstance().getClockHistoryList(domain, userId, jobId, opportunityId, responseListener);
    }




}
