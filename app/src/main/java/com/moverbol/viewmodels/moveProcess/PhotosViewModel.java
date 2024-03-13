package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.model.confirmationPage.artialeList.RawBodyRequestModel;
import com.moverbol.model.photoes.ImagesModel;
import com.moverbol.model.photoes.PhotoModel;
import com.moverbol.model.photoes.PhotoUploadPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;

/**
 * Created by AkashM on 5/2/18.
 */

public class PhotosViewModel extends ViewModel {

    public MutableLiveData<List<String>> urlListLive = new MutableLiveData<>();
    public MutableLiveData<List<PhotoModel>> photoPojoList = new MutableLiveData<>();

    public void uploadPhoto(String subdomain, String userId, String opportunityId, PhotoUploadPojo photoUploadPojoList, String jobId, MultipartBody.Part file, final ResponseListener<BaseResponseModel<ImagesModel>> responseListener) {
        RawBodyRequestModel<PhotoUploadPojo> rawBodyRequestModel = new RawBodyRequestModel<>(subdomain, userId, opportunityId, jobId, photoUploadPojoList);
        DataRepository.getInstance().uploadPhoto(rawBodyRequestModel, file, responseListener);
    }

    public void getSubmittedPhotosUrls(String subDomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<PhotoModel>>> responseListener) {

        DataRepository.getInstance().getSubmittedPhotosUrls(subDomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<List<PhotoModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<PhotoModel>> response, String usedUrlKey) {

                /*List<String> urlList = new ArrayList<>();

                if(response.getData() != null && response.getData().size()>0){
                    for (int i = 0; i < response.getData().size(); i++) {
                        urlList.add(response.getData().get(i).getLink());
                    }
                }

                urlListLive.postValue(urlList);*/
                photoPojoList.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<PhotoModel>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }

    public void deletePhotoes(String subdomain, String userId, String opportunityId, String jobId, String concatenatedIds, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().deleteItems(subdomain, userId, opportunityId, jobId, concatenatedIds, Constants.DELETE_ITEM_MODEL_PHOTOES, responseListener);
    }

}
