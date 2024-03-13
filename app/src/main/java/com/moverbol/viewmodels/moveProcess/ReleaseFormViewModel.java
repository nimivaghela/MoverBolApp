package com.moverbol.viewmodels.moveProcess;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.model.confirmationPage.artialeList.RawBodyRequestModel;
import com.moverbol.model.releaseForm.ReleaseFormMetadataPojo;
import com.moverbol.model.releaseForm.ReleaseFormRequestModel;
import com.moverbol.model.releaseForm.ReleaseFormResponseModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by AkashM on 7/2/18.
 */

public class ReleaseFormViewModel extends ViewModel {

    public MutableLiveData<ReleaseFormResponseModel> releaseFormResponseModelLive = new MutableLiveData<>();
    public MutableLiveData<ReleaseFormMetadataPojo> releaseFormMetadataPojoLive = new MutableLiveData<>();

    public String r_id = "";


    public void getReleaseFormMetadata(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<ReleaseFormResponseModel>> responseListener) {
        DataRepository.getInstance().getReleaseFormMetadata(subdomain, userId, opportunityId, new ResponseListener<BaseResponseModel<ReleaseFormResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ReleaseFormResponseModel> response, String usedUrlKey) {
                releaseFormResponseModelLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ReleaseFormResponseModel>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    /*public void submitReleaseFormDetails(String subdomain, String userId, String opportunityId, ReleaseFormRequestModel releaseFormRequestModel, String signatureCode, final ResponseListener<BaseResponseModel> responseListener) {
        RawBodyRequestModel<ReleaseFormRequestModel> rawBodyRequestModel = new RawBodyRequestModel<>(subdomain, userId, opportunityId, releaseFormRequestModel);
        rawBodyRequestModel.setSignature(signatureCode);
        DataRepository.getInstance().submitReleaseFormDetails(rawBodyRequestModel, responseListener);
    }*/


    public void submitReleaseFormDetails(String subdomain, String userId, String opportunityId, ReleaseFormRequestModel releaseFormRequestModelList, String signatureCode, String jobId, File file, final ResponseListener<BaseResponseModel> responseListener) {
        RawBodyRequestModel<ReleaseFormRequestModel> rawBodyRequestModel = new RawBodyRequestModel<ReleaseFormRequestModel>(subdomain, userId, opportunityId, jobId, releaseFormRequestModelList);
        rawBodyRequestModel.setSignature(signatureCode);
        DataRepository.getInstance().submitReleaseFormDetails(rawBodyRequestModel, file, responseListener);
    }


    public void getReleaseFormSubmittedDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<ReleaseFormRequestModel>>> responseListener) {
        DataRepository.getInstance().getReleaseFormSubmittedDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<List<ReleaseFormRequestModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<ReleaseFormRequestModel>> response, String usedUrlKey) {
                List<ReleaseFormRequestModel> requestModelList = response.getData();


                ReleaseFormResponseModel responseModel = releaseFormResponseModelLive.getValue();

//                r_id = requestModel.getId();
                for (int j = 0; j < requestModelList.size(); j++) {

                    ReleaseFormRequestModel requestModel = requestModelList.get(j);

                    if (responseModel.getReleaseFormMetadataPojoList() != null && responseModel.getReleaseFormMetadataPojoList().size() > 0) {

                        for (int i = 0; i < responseModel.getReleaseFormMetadataPojoList().size(); i++) {
//                            responseModel.getReleaseFormMetadataPojoList().get(i).setSelected(false);
                            if (TextUtils.equals(responseModel.getReleaseFormMetadataPojoList().get(i).getReleaseFormId(), requestModel.getReleaseFormId())) {
                                responseModel.getReleaseFormMetadataPojoList().get(i).setSelected(true);
                                responseModel.getReleaseFormMetadataPojoList().get(i).setAddress(requestModel.getAddress());
                                responseModel.getReleaseFormMetadataPojoList().get(i).setAddressList(requestModel.getAddressList());
                                responseModel.getReleaseFormMetadataPojoList().get(i).setDescription(requestModel.getDescription());
                                responseModel.getReleaseFormMetadataPojoList().get(i).setReleaseFormId(requestModel.getReleaseFormId());
                                responseModel.getReleaseFormMetadataPojoList().get(i).setSelectionId(requestModel.getSelectionId());
                            }
                        }
                    }
                }

                releaseFormResponseModelLive.postValue(responseModel);

                responseListener.onResponse(response, usedUrlKey);

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<ReleaseFormRequestModel>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void cancelReleaseFormSelection(String subdomain, String userId, String jobId, String opportunityId, final String releaseFormId, final ResponseListener<BaseResponseModel> responseListener){
        DataRepository.getInstance().deleteItems(subdomain, userId, opportunityId, jobId, releaseFormId, Constants.GET_METADATA_MODEL_RELEASE_FORM, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                if(releaseFormMetadataPojoLive.getValue()!=null){
                    releaseFormMetadataPojoLive.getValue().setSelected(false);
                    releaseFormMetadataPojoLive.getValue().setSelectionId("");
                }

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public ReleaseFormMetadataPojo getReleaseFormMetadataPojoByLabel(String labelHosting, ReleaseFormResponseModel releaseFormResponseModel) {

        for (int i = 0; i < releaseFormResponseModel.getReleaseFormMetadataPojoList().size(); i++) {
            if (TextUtils.equals(labelHosting, releaseFormResponseModel.getReleaseFormMetadataPojoList().get(i).getLabel())) {
                return releaseFormResponseModel.getReleaseFormMetadataPojoList().get(i);
            }
        }

        return new ReleaseFormMetadataPojo();
    }

    public List<ReleaseFormMetadataPojo> getSelectedMetadataObject(ReleaseFormResponseModel releaseFormResponseModel) {

        if (releaseFormResponseModel == null) {
            return null;
        }

        List<ReleaseFormMetadataPojo> listToReturn = new ArrayList<>();

        for (int i = 0; i < releaseFormResponseModel.getReleaseFormMetadataPojoList().size(); i++) {
            if (releaseFormResponseModel.getReleaseFormMetadataPojoList().get(i).isSelected()) {
                listToReturn.add(releaseFormResponseModel.getReleaseFormMetadataPojoList().get(i));
            }
        }

        return listToReturn;
    }


}
