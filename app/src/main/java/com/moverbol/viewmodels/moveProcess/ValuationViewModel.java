package com.moverbol.viewmodels.moveProcess;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.model.confirmationPage.artialeList.RawBodyRequestModel;
import com.moverbol.model.valuationPage.ValuationItemPojo;
import com.moverbol.model.valuationPage.ValuationSubmissionRequestModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.BaseResponseModelSecond;

import java.io.File;
import java.util.List;

import retrofit2.Call;

/**
 * Created by AkashM on 31/1/18.
 */

public class ValuationViewModel extends ViewModel {

    public MutableLiveData<List<ValuationItemPojo>> valuationItemPojoListLive = new MutableLiveData<>();
    public MutableLiveData<String> valuationDescriptionLive = new MutableLiveData<>();
    public MutableLiveData<ValuationSubmissionRequestModel> valuationSubmissionRequestModelLive = new MutableLiveData<>();

    public void getValuationMetadat(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModelSecond<List<ValuationItemPojo>, String>> responseListener) {

        DataRepository.getInstance().getValuationMetadat(subdomain, userId, opportunityId, new ResponseListener<BaseResponseModelSecond<List<ValuationItemPojo>, String>>() {
            @Override
            public void onResponse(BaseResponseModelSecond<List<ValuationItemPojo>, String> response, String usedUrlKey) {
                valuationItemPojoListLive.postValue(response.getData());
                valuationDescriptionLive.postValue(response.getSecondData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModelSecond<List<ValuationItemPojo>, String>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }

    public void submitValuationDetails(String subdomain, String userId, String opportunityId, ValuationSubmissionRequestModel valuationSubmissionRequestModel, String jobId, File file, final ResponseListener<BaseResponseModel> responseListener) {

        RawBodyRequestModel<ValuationSubmissionRequestModel> rawBodyRequestModel = new RawBodyRequestModel<>(subdomain, userId, opportunityId, jobId, valuationSubmissionRequestModel);

        DataRepository.getInstance().submitValuationDetails(rawBodyRequestModel, file, responseListener);
    }

    public void getValuationSubmittedDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ValuationSubmissionRequestModel>> responseListener) {
        DataRepository.getInstance().getValuationSubmittedDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<ValuationSubmissionRequestModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ValuationSubmissionRequestModel> response, String usedUrlKey) {
                valuationSubmissionRequestModelLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ValuationSubmissionRequestModel>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void setSelectedArticleListItem(String id, String declaredValue) {
        if (valuationItemPojoListLive.getValue() != null) {
            for (int i = 0; i < valuationItemPojoListLive.getValue().size(); i++) {
                if (TextUtils.equals(valuationItemPojoListLive.getValue().get(i).getId(), id)) {
                    valuationItemPojoListLive.getValue().get(i).setSelected(true);
                    valuationItemPojoListLive.getValue().get(i).setDeclaredValue(declaredValue);
                } else {
                    valuationItemPojoListLive.getValue().get(i).setSelected(false);
                }
            }
        }
    }
}
