package com.moverbol.viewmodels.moveProcess;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.model.billOfLading.AddChargesModel;
import com.moverbol.model.billOfLading.BillOfLadingPojo;
import com.moverbol.model.billOfLading.BillOfLadingRequestModel;
import com.moverbol.model.billOfLading.BolDetailsPojo;
import com.moverbol.model.billOfLading.CategoryModel;
import com.moverbol.model.billOfLading.ChargeRateTypeModel;
import com.moverbol.model.billOfLading.CouponDetailsModel;
import com.moverbol.model.billOfLading.RatingDiscountPercentagePojo;
import com.moverbol.model.billOfLading.RawBodyBOLSignatureSubmitRequestModel;
import com.moverbol.model.billOfLading.RawBodyBOLSubmitRequestModel;
import com.moverbol.model.billOfLading.TipsModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.ChargesUnitModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesResponseModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.RawChargesSaveRequestModel;
import com.moverbol.model.valuationPage.ValuationSubmissionRequestModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by AkashM on 19/2/18.
 */

public class BillOfLadingViewModel extends ViewModel {

    public MutableLiveData<ValuationSubmissionRequestModel> valuationSubmissionRequestModelLive = new MutableLiveData<>();

    public MutableLiveData<ArrayList<CommonChargesRequestModel>> commonChargesRequestModelLive = new MutableLiveData<>();
    public MutableLiveData<CommonChargesResponseModel> commonChargesResponseModelLive = new MutableLiveData<>();


    public MutableLiveData<BillOfLadingPojo> billOfLadingPojoLive = new MutableLiveData<>();
    public MutableLiveData<AddChargesModel> addChargesModelLive = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ChargeRateTypeModel>> chargeRateTypeListLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<CategoryModel>> categoryTypeListLiveData = new MutableLiveData<>();


    public void setJobConfirmation(final String subDomain, String user_id, String opportunityId, String jobId, long totalTime, double totalHour, final ResponseListener<BaseResponseModel<BolDetailsPojo>> responseListener) {

        DataRepository.getInstance().getBOLJobConfirmation(subDomain, user_id, opportunityId, jobId, totalTime, totalHour, new ResponseListener<BaseResponseModel<BolDetailsPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<BolDetailsPojo> response, String usedUrlKey) {

                BillOfLadingPojo billOfLadingPojo = new BillOfLadingPojo();

                billOfLadingPojo.setJobConfirmationDetailsPojo(response.getData());

                billOfLadingPojo.setMoveChargesPriceType((int) Util.getDoubleFromString(response.getData().getMoveChargePriceType()));

                billOfLadingPojoLive.setValue(billOfLadingPojo);

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<BolDetailsPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void submitCouponCode(String subdomain, String userId, String opportunityId, String couponCode, final ResponseListener<BaseResponseModel<CouponDetailsModel>> responseListener) {

        DataRepository.getInstance().submitCouponCode(subdomain, userId, opportunityId, couponCode, new ResponseListener<BaseResponseModel<CouponDetailsModel>>() {
            @Override
            public void onResponse(BaseResponseModel<CouponDetailsModel> response, String usedUrlKey) {

                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<CouponDetailsModel>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void getBolChargesList(String subdomain, String userId, String opportunityId, String model, String jobId, final ResponseListener<BaseResponseModel<CommonChargesResponseModel>> responseListener) {

        DataRepository.getInstance().getBolChargesList(subdomain, userId, opportunityId, model, jobId, new ResponseListener<BaseResponseModel<CommonChargesResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<CommonChargesResponseModel> response, String usedUrlKey) {

                commonChargesResponseModelLive.postValue(response.getData());

                commonChargesRequestModelLive.postValue(response.getData().getCommonChargesRequestModelArrayList());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<CommonChargesResponseModel>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }


    public void saveBOLCharges(String subDomain, String opportunityId, String userId, String modelName, String jobId, List<CommonChargesRequestModel> commonChargesRequestModelList, final ResponseListener<BaseResponseModel<String>> responseListener) {

        RawChargesSaveRequestModel<CommonChargesRequestModel> requestModel = new RawChargesSaveRequestModel<CommonChargesRequestModel>(subDomain, opportunityId, userId, modelName, jobId, commonChargesRequestModelList);

        DataRepository.getInstance().saveBOLCharges(requestModel, new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }


    public void deleteBolChargesItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, String model, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().deleteBolChargesItems(subdomain, userId, opportunityId, jobId, concatinatedIds, model, responseListener);
    }


    public void submitBOLDetails(String subDomain, String opportunityId, String userId, BillOfLadingRequestModel billOfLadingRequestModel, String jobId, final ResponseListener<BaseResponseModel<String>> responseListener) {

        RawBodyBOLSubmitRequestModel<BillOfLadingRequestModel> rawBodyBOLSubmitRequestModel = new RawBodyBOLSubmitRequestModel<>(subDomain, opportunityId, userId, jobId, billOfLadingRequestModel);

        DataRepository.getInstance().submitBOLDetails(rawBodyBOLSubmitRequestModel, new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

    }

    public void getChargeRateTypeList(String subDomain, String opportunityId, String moveType, String model, int cId, final ResponseListener<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> responseListener) {

        DataRepository.getInstance().getChargeRateTypeList(subDomain, opportunityId, moveType, model, cId, new ResponseListener<BaseResponseModel<ArrayList<ChargeRateTypeModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ChargeRateTypeModel>> response, String usedUrlKey) {
                chargeRateTypeListLiveData.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void getRateValue(String subDomain, String name, String type, String opportunityId, String unitType, final ResponseListener<BaseResponseModel<ChargesUnitModel>> responseListener) {

        DataRepository.getInstance().getRateValue(subDomain, name, type, opportunityId, unitType, new ResponseListener<BaseResponseModel<ChargesUnitModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ChargesUnitModel> response, String usedUrlKey) {
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ChargesUnitModel>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void getCategoryTypeList(String subDomain, String opportunityId, String moveType, String model, final ResponseListener<BaseResponseModel<ArrayList<CategoryModel>>> responseListener) {

        WebServiceManager.getInstance().getCategoryTypeList(subDomain, opportunityId, moveType, model, new ResponseListener<BaseResponseModel<ArrayList<CategoryModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<CategoryModel>> response, String usedUrlKey) {
                categoryTypeListLiveData.postValue(response.getData());
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CategoryModel>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void saveBolTipsOrDiscount(String subdomain, String userId, String opportunityId, String bolChargesId, String discountType, String amount, String jobId, String calculatedAmount, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().saveBolTipsOrDiscount(subdomain, userId, opportunityId, bolChargesId, discountType, amount, jobId, calculatedAmount, responseListener);
    }

    public void submitRating(String subdomain, String userId, String opportunityId, String bolChargesId, String rating, String message, String jobId, final ResponseListener<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> responseListener) {
        DataRepository.getInstance().submitRating(subdomain, userId, opportunityId, bolChargesId, rating, message, jobId, responseListener);
    }

    public void submitReviewDiscount(String subdomain, String userId, String opportunityId, String bolChargesId, String amount, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().submitReviewDiscount(subdomain, userId, opportunityId, bolChargesId, amount, jobId, responseListener);
    }

    public void submitBOLSignature(RawBodyBOLSignatureSubmitRequestModel submitRequestModel, File file, final ResponseListener<BaseResponseModel<String>> responseListener) {
        DataRepository.getInstance().submitBOLSignature(submitRequestModel, file, responseListener);
    }

    public void getTipDiscountList(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<ArrayList<TipsModel>>> responseListener) {
        DataRepository.getInstance().getTipDiscountList(subdomain, userId, opportunityId, responseListener);
    }

    public void setBolStarted(String subdomain, String userId, String opportunityId, String bolFinalChargeId, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        DataRepository.getInstance().setBolStarted(subdomain, userId, opportunityId, bolFinalChargeId, jobId, responseListener);
    }


    /**
     * Subtracts the discount from Given amount and returns the calculated value
     *
     * @param discountType Should be 1 or 2. 1 here stands for exact amount, 2 here stands for %
     */
    public double calculateTotalWithDiscount(double amountToSubtractDiscountFrom, int discountType, double discountValue) {

        if (discountType == Constants.NumValueTypes.NUM_VALUE_TYPE_AMOUNT) {
            return amountToSubtractDiscountFrom - discountValue;
        } else if (discountType == Constants.NumValueTypes.NUM_VALUE_TYPE_PERCENTAGE) {
            return amountToSubtractDiscountFrom - ((discountValue * amountToSubtractDiscountFrom) / 100);
        }

        return 0;
    }


    /**
     * Adds the tax to given amount and returns the calculated value
     */
    public double calculateTotalWithTaxes(double amountToAddTaxesTo, double taxValue) {
        return amountToAddTaxesTo + ((taxValue * amountToAddTaxesTo) / 100);
    }

}
