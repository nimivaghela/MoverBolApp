package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.model.ClockActivityModel;
import com.moverbol.model.billOfLading.RatingDiscountPercentagePojo;
import com.moverbol.model.billOfLading.RawBodyBOLSignatureSubmitRequestModel;
import com.moverbol.model.forETA.ETAResponseModel;
import com.moverbol.model.moveProcess.AddressListResponseModel;
import com.moverbol.model.moveProcess.ClockInfoPojo;
import com.moverbol.model.moveProcess.MoveProcessStatusPojo;
import com.moverbol.model.moveProcess.WorkerModel;
import com.moverbol.model.valuationPage.ValuationSubmissionRequestModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by AkashM on 5/1/18.
 */

public class MoveProcessViewModel extends ViewModel {

    public MutableLiveData<MoveProcessStatusPojo> moveProcessStatusPojoLive = new MutableLiveData<>();

    public MutableLiveData<ETAResponseModel> etaResponseModelLive = new MutableLiveData<>();
    public MutableLiveData<AddressListResponseModel> addressListRespinseModelLive = new MutableLiveData<>();


    public void getMoveProcessStatus(String subDomain, String jobId, String opportunityId, String userId, final ResponseListener<BaseResponseModel<MoveProcessStatusPojo>> responseListener) {

        DataRepository.getInstance().getMoveProcessStatus(subDomain, jobId, opportunityId, userId, new ResponseListener<BaseResponseModel<MoveProcessStatusPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<MoveProcessStatusPojo> response, String usedUrlKey) {
                moveProcessStatusPojoLive.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<MoveProcessStatusPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void startStopClock(String subDomain, String jobId, String opportunityId, String userId, String curentLatitude, String curentLongitude, String currentProcessId, String startStopStatusId, ClockActivityModel clockActivityModel, ArrayList<WorkerModel> workerList, ResponseListener<BaseResponseModel<ClockInfoPojo>> responseModelResponseListener) {
        DataRepository.getInstance().startStopClock(subDomain, jobId, opportunityId, userId, curentLatitude, curentLongitude, currentProcessId, startStopStatusId, clockActivityModel, workerList, responseModelResponseListener);
    }

    public void getAddressList(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<AddressListResponseModel>> responseListener) {
        DataRepository.getInstance().getAddressList(subdomain, userId, opportunityId, new ResponseListener<BaseResponseModel<AddressListResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<AddressListResponseModel> response, String usedUrlKey) {
                addressListRespinseModelLive.setValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<AddressListResponseModel>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void startStopBreak(String subDomain, String jobId, String opportunityId, String userId, String jobClockId, String startStopStatusId, ResponseListener<BaseResponseModel<String>> responseModelResponseListener){

        DataRepository.getInstance().startStopBreak(subDomain, jobId, opportunityId, userId, jobClockId, startStopStatusId, responseModelResponseListener);
    }

    public void getValuationSubmittedDetails(String subDomain, String userId, String opportunityId, String jobId, ResponseListener<BaseResponseModel<ValuationSubmissionRequestModel>> responseListener) {
        DataRepository.getInstance().getValuationSubmittedDetails(subDomain, userId, opportunityId, jobId, responseListener);
    }


    public void completeMove(String subdomain, String userId, String opportunityId, String jobId, String totalCharges, final ResponseListener<BaseResponseModel> responseListener) {

        DataRepository.getInstance().setJobStatus(subdomain, userId, opportunityId, jobId, Constants.JOB_STATUS_COMPLETED, "", totalCharges, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {

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


    public void getETA(String originLocation, String destinationLocation, final ResponseListener<ETAResponseModel> responseListener) {{

        DataRepository.getInstance().getETA(originLocation, destinationLocation, new ResponseListener<ETAResponseModel>() {
            @Override
            public void onResponse(ETAResponseModel response, String usedUrlKey) {
                responseListener.onResponse(response, usedUrlKey);
                etaResponseModelLive.setValue(response);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<ETAResponseModel> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }
    }


    public void sendETA(String subdomain, String userId, String opportunityId, String phoneNumber, String etaMessage, String destinationAddress, final ResponseListener<BaseResponseModel> responseListener) {

        DataRepository.getInstance().sendETA(subdomain, userId, opportunityId, phoneNumber, etaMessage, destinationAddress, responseListener);
    }


    public void submitBOLSignature(RawBodyBOLSignatureSubmitRequestModel submitRequestModel, File file, final ResponseListener<BaseResponseModel<String>> responseListener) {
        DataRepository.getInstance().submitBOLSignature(submitRequestModel, file, responseListener);
    }

    public void submitRating(String subdomain, String userId, String opportunityId, String bolChargesId, String rating, String message, String jobId, final ResponseListener<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> responseListener) {
        DataRepository.getInstance().submitRating(subdomain, userId, opportunityId, bolChargesId, rating, message, jobId, responseListener);
    }



    /*public CommonChargesRequestModel getValuationChargesCalculated(ValuationSubmissionRequestModel data) {

        CommonChargesRequestModel valuationChargesCalculated = new CommonChargesRequestModel();

        double total = 0.00;

        if (TextUtils.equals(data.getLabel().trim().toLowerCase(), Constants.VALUACTION_LABEL_TO_IGNORE.trim().toLowerCase())) {
            return valuationChargesCalculated;
        }

        //Returning unit as one in default case of because unit is used here for division.
//        double unitNum = unit.contains("1000") ? 1000.00 : unit.contains("100") ? 100.00 : 0.00;
        double unitNum = data.getValuationUnit().contains("1000") ? 1000.00 : (data.getValuationUnit().contains("100") ? 100.00 : 1.00);
        double rateNum = Util.getDoubleFromString(data.getValuationRate());

        double quantNum = Util.getDoubleFromString(data.getDeclaredAmount());

        *//**
         * 3 type of units are possible for unit. If unit is percentage then rate is to be converted
         * into percentage. Other two are handled in unitNum object assignment.
         *//*
        if(data.getValuationUnit().trim().equalsIgnoreCase("Percentage".trim())){
            rateNum = (quantNum * rateNum) / 100;
        }

        total = (rateNum * quantNum) / unitNum;

        String bolFinalChargeId = "";

        if (this.jobConfirmationDetailsPojo != null) {
            if(TextUtils.isEmpty(jobConfirmationDetailsPojo.getBolFinalChargesPojo().getValuationChargesCalculated())){
                totalCalculatedValuationCharges = this.jobConfirmationDetailsPojo.getValuation() + total
                        - Util.getLongFromString(jobConfirmationDetailsPojo.getBolFinalChargesPojo().getValuationChargesCalculated()*//*.getAmount()*//*);
            }else {
//            totalCalculatedValuationCharges = this.jobConfirmationDetailsPojo.getValuation() + total;
                totalCalculatedValuationCharges = total;
            }
            bolFinalChargeId = this.jobConfirmationDetailsPojo.getBolFinalChargesPojo().getId();
        }

        valuationChargesCalculated = new CommonChargesRequestModel();

        String id = "";
        if(jobConfirmationDetailsPojo.getBolFinalChargesPojo()!=null) {
            id = jobConfirmationDetailsPojo.getBolFinalChargesPojo().getValuationChargesCalculatedId();
        }

        valuationChargesCalculated.setFieldsForValuationCharges(id, bolFinalChargeId, description, quantity + "", unit, "", rate + "", total + "", false);
        return valuationChargesCalculated;
    }*/
}
