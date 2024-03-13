package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.model.confirmationPage.artialeList.RawBodyRequestModel;
import com.moverbol.model.rentalAgreement.RentalAgreementPojo;
import com.moverbol.model.rentalAgreement.RentalAgreementResponseModel;
import com.moverbol.model.rentalAgreement.RentalAgreementSubmittedDetailsPojo;
import com.moverbol.model.rentalAgreement.StorageChargeModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import java.io.File;

import retrofit2.Call;

/**
 * Created by AkashM on 5/2/18.
 */

public class RentalAgreementViewModel extends ViewModel {

    public MutableLiveData<RentalAgreementPojo> rentalAgreementPojoLive = new MutableLiveData<>();
    public MutableLiveData<StorageChargeModel> storageChargeModelLiveData = new MutableLiveData<>();

    public String r_id = "";

    /*public MutableLiveData<RentalAgreementSubmittedDetailsPojo> submittedrentalAgreementPojoLive = new MutableLiveData<>();
    public MutableLiveData<RentalAgreementMetadataPojo> rentalAgreementMetadataPojoLive = new MutableLiveData<>();*/

    public void getRentalAgreement(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<RentalAgreementResponseModel>> responseListener) {
        DataRepository.getInstance().getRentalAgreementMetadata(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<RentalAgreementResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<RentalAgreementResponseModel> response, String usedUrlKey) {

                rentalAgreementPojoLive.postValue(response.getData().getAgreement());
                storageChargeModelLiveData.postValue(response.getData().getCharges());

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<RentalAgreementResponseModel>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void submitRentalAgreementDetails(String subdomain, String userId, String opportunityId, RentalAgreementPojo rentalAgreementPojo, String jobId, File file, final ResponseListener<BaseResponseModel> responseListener) {
        RawBodyRequestModel<RentalAgreementPojo> rawBodyRequestModel = new RawBodyRequestModel<>(subdomain, userId, opportunityId, jobId, rentalAgreementPojo);

        DataRepository.getInstance().submitRentalAgreementDetails(rawBodyRequestModel, file, responseListener);
    }

    public void getRentalAgreementSubmittedDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> responseListener){
        DataRepository.getInstance().getRentalAgreementSubmittedDetails(subdomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<RentalAgreementSubmittedDetailsPojo> response, String usedUrlKey) {

                RentalAgreementPojo rentalAgreementPojo = rentalAgreementPojoLive.getValue();

                r_id = response.getData().getId();

                if(rentalAgreementPojo==null){
                    rentalAgreementPojo = new RentalAgreementPojo();
                }

                rentalAgreementPojo.setValuesFromRentalAgreementSubmittedDetails(response.getData());

                rentalAgreementPojoLive.postValue(rentalAgreementPojo);
                responseListener.onResponse(response, usedUrlKey);

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

}
