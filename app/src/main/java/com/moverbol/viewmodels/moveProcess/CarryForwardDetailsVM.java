package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.model.CarryForwardModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;

import java.util.ArrayList;

public class CarryForwardDetailsVM extends ViewModel {

    public MutableLiveData<ArrayList<CarryForwardModel>> listLiveData;


    public void callCarryForwardList(String domain, String userId, String opportunityId, ResponseListener<BaseResponseModel<ArrayList<CarryForwardModel>>> responseListener) {
        listLiveData = new MutableLiveData<>();
        WebServiceManager.getInstance().carryForwardList(domain, userId, opportunityId, responseListener);
    }

}
