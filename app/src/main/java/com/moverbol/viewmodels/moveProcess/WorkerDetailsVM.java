package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.model.moveProcess.ActivityItem;
import com.moverbol.model.moveProcess.WorkerDetailsModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import java.util.ArrayList;

public class WorkerDetailsVM extends ViewModel {

    public MutableLiveData<ArrayList<ActivityItem>> workerDetailsList = new MutableLiveData<>();


    public void callWorkerDetailsList(String domain, String userId, String jobId, String opportunityId, ResponseListener<BaseResponseModel<ArrayList<WorkerDetailsModel>>> responseListener) {
        DataRepository.getInstance().getWorkerDetailsList(domain, userId, jobId, opportunityId, responseListener);
    }
}
