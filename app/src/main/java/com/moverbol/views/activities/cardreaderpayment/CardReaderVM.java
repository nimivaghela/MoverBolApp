package com.moverbol.views.activities.cardreaderpayment;

import androidx.lifecycle.ViewModel;

import com.moverbol.model.CardReaderModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;

public class CardReaderVM extends ViewModel {

    public void callAuthorizeNetDetails(String domain, String userId, String opportunityId, ResponseListener<BaseResponseModel<CardReaderModel>> responseListener) {
        WebServiceManager.getInstance().authorizeNetCredentials(domain, userId, opportunityId, responseListener);
    }
}
