package com.moverbol.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.model.ForceUpdateResponseModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import retrofit2.Call;

public class SplashViewModel extends ViewModel {

    public MutableLiveData<Integer> shouldShowUpdateDialogLive = new MutableLiveData<>();

    public void checkIfUpdateNeeded(String subdomain, final ResponseListener<BaseResponseModel<ForceUpdateResponseModel>> responseListener) {
        DataRepository.getInstance().checkIfUpdateNeeded(subdomain, new ResponseListener<BaseResponseModel<ForceUpdateResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ForceUpdateResponseModel> response, String usedUrlKey) {

                if (response.getData() != null && response.getData().getForceUpdateStatus() != null &&
                        response.getData().getForceUpdateStatus().equalsIgnoreCase("Update New Version") &&
                        response.getData().shouldForceUpdate()) {
                    if (response.getData().shouldClosePopup()) {
                        shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_SOFT_NEDDED);
                    } else {
                        shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_FORCE_NEEDED);
                    }
                } else {
                    shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_NOT_NEEDED);
                }
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_CHECK_ERROR);
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ForceUpdateResponseModel>> call, Throwable t, String message) {
                shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_CHECK_ERROR);
                responseListener.onFailure(call, t, message);
            }
        });
    }
}
