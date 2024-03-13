package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.model.CarryForwardModel;
import com.moverbol.model.billOfLading.payment.PaymentMethodsModel;
import com.moverbol.model.billOfLading.payment.SpreedlyInfoModel;
import com.moverbol.model.billOfLading.payment.SpreedlyPaymentTokenSubmitRequestModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PaymentViewModel extends ViewModel{

    public MutableLiveData<List<PaymentMethodsModel>> paymentMethodModelListLive = new MutableLiveData<>();
    public MutableLiveData<SpreedlyInfoModel> spreedlyInfoModelLive = new MutableLiveData<>();

    public void getPaymentMethods(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<List<PaymentMethodsModel>>> responseListener){
        DataRepository.getInstance().getPaymentMethods(subdomain, userId, opportunityId, new ResponseListener<BaseResponseModel<List<PaymentMethodsModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<PaymentMethodsModel>> response, String usedUrlKey) {
                paymentMethodModelListLive.setValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<PaymentMethodsModel>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void getSpreedlySetup(String subdomain, String userId, String jobId, String opportunityId, final ResponseListener<BaseResponseModel<SpreedlyInfoModel>> responseListener){
        DataRepository.getInstance().getSpreedlySetup(subdomain, userId, jobId, opportunityId, new ResponseListener<BaseResponseModel<SpreedlyInfoModel>>() {
            @Override
            public void onResponse(BaseResponseModel<SpreedlyInfoModel> response, String usedUrlKey) {
                spreedlyInfoModelLive.setValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<SpreedlyInfoModel>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
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


    public void submitPayment(String subdomain, String userId, String opportunityId, String jobId, String bolFinalChargeId, String paymentToken, String amountWithCardConvenienceFeeNotAdded, String cardConvenienceFeeType, String cardConvenienceFee, String amount, String originalAmount, String paymentType, String paymentMethod, String paymentSignature, String additionalImage, String deposite, File paymentSignatureImage, File additionalImageFile, final ResponseListener<BaseResponseModel<String>> responseListener) {

        SpreedlyPaymentTokenSubmitRequestModel submitRequestModel = new SpreedlyPaymentTokenSubmitRequestModel(subdomain, userId, opportunityId, jobId, bolFinalChargeId, paymentToken, amountWithCardConvenienceFeeNotAdded, cardConvenienceFeeType, cardConvenienceFee, amount, originalAmount, paymentType, paymentMethod, paymentSignature, additionalImage, deposite);

        DataRepository.getInstance().submitPayment(submitRequestModel, paymentSignatureImage, additionalImageFile, responseListener);
    }


    public void saveBolTipsOrDiscount(String subdomain, String userId, String opportunityId, String bolChargesId, String discountType, String amount, String jobId, String calculatedAmount, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().saveBolTipsOrDiscount(subdomain, userId, opportunityId, bolChargesId, discountType, amount, jobId, calculatedAmount, responseListener);
    }

    public void setCardConvenienceFee(String subdomain, String userId, String opportunityId, String bolChargesId, String discountType, String amount, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().setCardConvenienceFee(subdomain, userId, opportunityId, bolChargesId, discountType, amount, jobId, responseListener);
    }

    public void carryForwardPayment(String subdomain, String bolFinalChargeId, int status, String carryForwardAmount, String userId, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().paymentCarryForward(subdomain, bolFinalChargeId, status, carryForwardAmount, userId, jobId, responseListener);
    }


    public void callCarryForwardList(String domain, String userId, String opportunityId, ResponseListener<BaseResponseModel<ArrayList<CarryForwardModel>>> responseListener) {
        WebServiceManager.getInstance().carryForwardList(domain, userId, opportunityId, responseListener);
    }

    public void sendBOLToCustomer(String subdomain, String jobId, String opportunityId, String email, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().sendBOLToCustomer(subdomain, jobId, opportunityId, email, responseListener);
    }

}
