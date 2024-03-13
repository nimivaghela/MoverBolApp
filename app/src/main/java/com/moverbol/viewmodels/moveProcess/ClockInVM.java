package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.model.CrewMetadata;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import java.util.List;

import retrofit2.Call;

public class ClockInVM extends ViewModel {
    public MutableLiveData<List<CrewMetadata>> crewMetadataListLive = new MutableLiveData<>();

    public void getCrewMetadata(String subDomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<CrewMetadata>>> responseListener) {

        DataRepository.getInstance().getCrewMetadat(subDomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<List<CrewMetadata>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<CrewMetadata>> response, String usedUrlKey) {
                crewMetadataListLive.setValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<CrewMetadata>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }
}
