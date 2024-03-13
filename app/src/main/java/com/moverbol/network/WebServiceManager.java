package com.moverbol.network;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.moverbol.BuildConfig;
import com.moverbol.constants.Constants;
import com.moverbol.model.ActivityTypeListResponseModel;
import com.moverbol.model.CardReaderModel;
import com.moverbol.model.CarryForwardModel;
import com.moverbol.model.ClockActivityModel;
import com.moverbol.model.CrewMetadata;
import com.moverbol.model.ForceUpdateResponseModel;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.model.ManPowerPojo;
import com.moverbol.model.MaterialMetadata;
import com.moverbol.model.MaterialPojo;
import com.moverbol.model.RatePerHourModel;
import com.moverbol.model.RateTypePojo;
import com.moverbol.model.RawBodyLocationUpdateRequestModel;
import com.moverbol.model.TruckMetadata;
import com.moverbol.model.TruckPojo;
import com.moverbol.model.billOfLading.BillOfLadingRequestModel;
import com.moverbol.model.billOfLading.BolDetailsPojo;
import com.moverbol.model.billOfLading.CategoryModel;
import com.moverbol.model.billOfLading.ChargeRateTypeModel;
import com.moverbol.model.billOfLading.ClockHistoryModel;
import com.moverbol.model.billOfLading.ClockHistoryResponse;
import com.moverbol.model.billOfLading.CouponDetailsModel;
import com.moverbol.model.billOfLading.RatingDiscountPercentagePojo;
import com.moverbol.model.billOfLading.RawBodyBOLSignatureSubmitRequestModel;
import com.moverbol.model.billOfLading.RawBodyBOLSubmitRequestModel;
import com.moverbol.model.billOfLading.TipsModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.ChargesUnitModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesResponseModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.RawChargesSaveRequestModel;
import com.moverbol.model.billOfLading.payment.PaymentMethodsModel;
import com.moverbol.model.billOfLading.payment.SpreedlyInfoModel;
import com.moverbol.model.billOfLading.payment.SpreedlyPaymentTokenSubmitRequestModel;
import com.moverbol.model.confirmationPage.AdditionalChargesPojo;
import com.moverbol.model.confirmationPage.ConfirmationSubmitRequestModel;
import com.moverbol.model.confirmationPage.CratingChargesPojo;
import com.moverbol.model.confirmationPage.JobConfirmationDetailsPojo;
import com.moverbol.model.confirmationPage.MoveChargesAutoPricingPojo;
import com.moverbol.model.confirmationPage.MoveChargesManualPricingPojo;
import com.moverbol.model.confirmationPage.PackingChargesPojo;
import com.moverbol.model.confirmationPage.StorageChargesPojo;
import com.moverbol.model.confirmationPage.ValuationChargesPojo;
import com.moverbol.model.confirmationPage.artialeList.ArticleListItemPojo;
import com.moverbol.model.confirmationPage.artialeList.ArticleListResponseDetailsModel;
import com.moverbol.model.confirmationPage.artialeList.ArticleRoomModel;
import com.moverbol.model.confirmationPage.artialeList.RawBodyRequestModel;
import com.moverbol.model.forETA.ETAResponseModel;
import com.moverbol.model.moveProcess.AddressListResponseModel;
import com.moverbol.model.moveProcess.ClockInfoPojo;
import com.moverbol.model.moveProcess.MoveProcessStatusPojo;
import com.moverbol.model.moveProcess.WorkerDetailsModel;
import com.moverbol.model.moveProcess.WorkerModel;
import com.moverbol.model.notes.NotesPojo;
import com.moverbol.model.notes.RawNotesRequest;
import com.moverbol.model.notification.NotificationListResponse;
import com.moverbol.model.photoes.ImagesModel;
import com.moverbol.model.photoes.PhotoModel;
import com.moverbol.model.photoes.PhotoUploadPojo;
import com.moverbol.model.releaseForm.ReleaseFormRequestModel;
import com.moverbol.model.releaseForm.ReleaseFormResponseModel;
import com.moverbol.model.rentalAgreement.RentalAgreementPojo;
import com.moverbol.model.rentalAgreement.RentalAgreementResponseModel;
import com.moverbol.model.rentalAgreement.RentalAgreementSubmittedDetailsPojo;
import com.moverbol.model.valuationPage.ValuationItemPojo;
import com.moverbol.model.valuationPage.ValuationSubmissionRequestModel;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.BaseResponseModelSecond;
import com.moverbol.network.model.JobPojo;
import com.moverbol.network.model.SignInData;
import com.moverbol.util.Util;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 06-11-2017.
 */

public class WebServiceManager {

    private static final String TAG = "Error";
    private static final Object LOCK = new Object();
    private static WebServiceManager sInstance;

    private WebServiceManager() {
    }

    public synchronized static WebServiceManager getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new WebServiceManager();
            }
        }
        return sInstance;
    }

    /**
     * Enqueue's the call and give's responces.
     * On Success Writes data in SharedPreferences
     *
     * @param call object of Call<SignInPojo>
     */
    public void doLogin(Call<BaseResponseModel<SignInData>> call, final ResponseListener<BaseResponseModel<SignInData>> responseListener) {

        call.enqueue(new Callback<BaseResponseModel<SignInData>>() {

            BaseResponseModel<SignInData> signInDataBaseResponseModel = null;

            @Override
            public void onResponse(Call<BaseResponseModel<SignInData>> call, Response<BaseResponseModel<SignInData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    signInDataBaseResponseModel = response.body();
                    responseListener.onResponse(signInDataBaseResponseModel, ApiEndPoints.SIGNIN_URL);
                } else {
                    signInDataBaseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(signInDataBaseResponseModel, ApiEndPoints.SIGNIN_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<SignInData>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void forgotPwd(Call<BaseResponseModel> call, final ResponseListener responseListener) {

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.FORGOT_PASSWORD_URL);
                    } else {
                        responseListener.onResponseError(response.body(), ApiEndPoints.FORGOT_PASSWORD_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.FORGOT_PASSWORD_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }


    public void setPwd(Call<BaseResponseModel> call, final ResponseListener responseListener) {

        call.enqueue(new Callback<BaseResponseModel>() {


            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SET_PASSWORD_URL);
                    } else {
                        BaseResponseModel baseResponseModel = Util.parseError(response);
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.SET_PASSWORD_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.SET_PASSWORD_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);

            }
        });

    }


    public void getNewJobs(String subDomain, String user_id, final ResponseListener<BaseResponseModel<List<JobPojo>>> responseListener) {


        Call<BaseResponseModel<List<JobPojo>>> call = RetrofitClient.getInstance().getApiInterface().getNewJobs(subDomain, user_id);

        call.enqueue(new Callback<BaseResponseModel<List<JobPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<List<JobPojo>>> call, Response<BaseResponseModel<List<JobPojo>>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
//                        if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_NEW_JOB_URL);
                        /*} else {
                            BaseResponseModel baseResponseModel = Util.parseError(response);
                            responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_NEW_JOB_URL);
                        }*/
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.GET_NEW_JOB_URL);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_NEW_JOB_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<JobPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void getJobDetails(String subDomain, String userId, String opportunityId, String job_id, final ResponseListener<BaseResponseModel<JobDetailPojo>> responseListener) {

        Call<BaseResponseModel<JobDetailPojo>> call = RetrofitClient.getInstance().getApiInterface().getJobDetails(subDomain, userId, opportunityId, job_id);

        call.enqueue(new Callback<BaseResponseModel<JobDetailPojo>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<JobDetailPojo>> call, Response<BaseResponseModel<JobDetailPojo>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_JOB_DETAILS_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_JOB_DETAILS_URL);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_JOB_DETAILS_URL);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);

            }
        });
    }


    public void getJobConfirmation(String subDomain, String user_id, String opportunityId, String job_id, final ResponseListener<BaseResponseModel<JobConfirmationDetailsPojo>> responseListener) {

        Call<BaseResponseModel<JobConfirmationDetailsPojo>> call = RetrofitClient.getInstance().getApiInterface().getJobConfirmation(subDomain, user_id, opportunityId, job_id);

        call.enqueue(new Callback<BaseResponseModel<JobConfirmationDetailsPojo>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<JobConfirmationDetailsPojo>> call, Response<BaseResponseModel<JobConfirmationDetailsPojo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_JOB_CONFIRMATION_DETAILS_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_JOB_CONFIRMATION_DETAILS_URL);
                    }


                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_JOB_CONFIRMATION_DETAILS_URL);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<JobConfirmationDetailsPojo>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);

            }
        });
    }


    public void setJobStatus(String subDomain, String userId, String opportunityId, String job_id, String status, @Nullable String comments, String totalCharges, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().setJobStattus(subDomain, userId, opportunityId, job_id, status, comments, totalCharges);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SET_JOB_STATUS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.SET_JOB_STATUS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.SET_JOB_STATUS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void getCrewMetadat(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<CrewMetadata>>> responseListener) {

        Call<BaseResponseModel<List<CrewMetadata>>> call = RetrofitClient.getInstance().getApiInterface().getCrewMetadata(subdomain, userId, opportunityId, Constants.GET_METADATA_MODEL_WORKER, jobId);

        call.enqueue(new Callback<BaseResponseModel<List<CrewMetadata>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<List<CrewMetadata>>> call, Response<BaseResponseModel<List<CrewMetadata>>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }


            }


            @Override
            public void onFailure(Call<BaseResponseModel<List<CrewMetadata>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }

    public void getTruckMetadat(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<List<TruckMetadata>>> responseListener) {

        Call<BaseResponseModel<List<TruckMetadata>>> call = RetrofitClient.getInstance().getApiInterface().getTruckMetadata(subdomain, userId, opportunityId, Constants.GET_METADATA_MODEL_TRUCK);

        call.enqueue(new Callback<BaseResponseModel<List<TruckMetadata>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<List<TruckMetadata>>> call, Response<BaseResponseModel<List<TruckMetadata>>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }

            }


            @Override
            public void onFailure(Call<BaseResponseModel<List<TruckMetadata>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });


    }

    public void getMaterialMetadat(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> responseListener) {

        Call<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> call = RetrofitClient.getInstance().getApiInterface().getMaterialMetadata(subdomain, userId, opportunityId, Constants.GET_METADATA_MODEL_MATERIAL);

        call.enqueue(new Callback<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> call, Response<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }

            }


            @Override
            public void onFailure(Call<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void getWorkerById(String subDomain, String userId, String opportunityId, String workerId, String jobId, final ResponseListener<BaseResponseModel<ManPowerPojo>> responseListener) {

        Call<BaseResponseModel<ManPowerPojo>> call = RetrofitClient.getInstance().getApiInterface().getWorkerById(subDomain, Constants.GET_METADATA_MODEL_WORKER, opportunityId, userId, jobId, workerId);

        call.enqueue(new Callback<BaseResponseModel<ManPowerPojo>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ManPowerPojo>> call, Response<BaseResponseModel<ManPowerPojo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_SUBMITTED_DETAILS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ManPowerPojo>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }

    public void getTruckById(String subDomain, String userId, String opportunityId, String truckId, String jobId, final ResponseListener<BaseResponseModel<TruckPojo>> responseListener) {

        Call<BaseResponseModel<TruckPojo>> call = RetrofitClient.getInstance().getApiInterface().getTruckById(subDomain, Constants.GET_METADATA_MODEL_TRUCK, opportunityId, userId, jobId, truckId);

        call.enqueue(new Callback<BaseResponseModel<TruckPojo>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<TruckPojo>> call, Response<BaseResponseModel<TruckPojo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_SUBMITTED_DETAILS);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<TruckPojo>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }

    public void getMaterialById(String subDomain, String userId, String opportunityId, String materialId, String jobId, final ResponseListener<BaseResponseModel<MaterialPojo>> responseListener) {

        Call<BaseResponseModel<MaterialPojo>> call = RetrofitClient.getInstance().getApiInterface().getMaterialById(subDomain, Constants.GET_METADATA_MODEL_MATERIAL, opportunityId, userId, jobId, materialId);

        call.enqueue(new Callback<BaseResponseModel<MaterialPojo>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<MaterialPojo>> call, Response<BaseResponseModel<MaterialPojo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_SUBMITTED_DETAILS);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<MaterialPojo>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }

    public void deleteCrewItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().deleteItems(subdomain, Constants.GET_METADATA_MODEL_WORKER, opportunityId, jobId, userId, concatinatedIds);
//        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().deleteItems("demo", "worker", "4509");

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.DELETE_ITEMS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ITEMS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ITEMS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void deleteTruckItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().deleteItems(subdomain, Constants.GET_METADATA_MODEL_TRUCK, opportunityId, jobId, userId, concatinatedIds);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.DELETE_ITEMS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ITEMS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ITEMS);
                }

            }


            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void deleteMaterialItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().deleteItems(subdomain, Constants.GET_METADATA_MODEL_MATERIAL, opportunityId, jobId, userId, concatinatedIds);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.DELETE_ITEMS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ITEMS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ITEMS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void addTruckItems(String subdomain, String jobId, String opportunityId, String vehicleType, String vehicleNumber, String tempVehicle, String remarks, String vehicleId, String userId, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface()
                .addTruckItems(subdomain, Constants.GET_METADATA_MODEL_TRUCK, opportunityId, jobId, vehicleType, vehicleNumber, tempVehicle, remarks, vehicleId, userId);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.ADD_ITEMS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.ADD_ITEMS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.ADD_ITEMS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void addCrewItems(String subdomain, String jobId, String opportunityId, String workertype, String responsibleUser, String tempWorker, String remarks, String manpowerId, String userId, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface()
                .addCrewItems(subdomain, Constants.GET_METADATA_MODEL_WORKER, opportunityId, jobId, userId, workertype, responsibleUser, tempWorker, remarks, manpowerId);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.ADD_ITEMS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.ADD_ITEMS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.ADD_ITEMS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void addMaterialItems(String subdomain, String userId, String opportunityId, String jobId, String material, String quantity, String materialUnit, String materialRate, String totalMaterialRate, String remarks, String materialId, int cId, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface()
                .addMaterialItems(subdomain, userId, opportunityId, Constants.GET_METADATA_MODEL_MATERIAL, jobId, material, quantity, materialUnit, materialRate, totalMaterialRate, remarks, materialId, cId);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.ADD_ITEMS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.ADD_ITEMS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.ADD_ITEMS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getMoveProcessStatus(String subDomain, String job_id, String opportunityId, String userId, final ResponseListener<BaseResponseModel<MoveProcessStatusPojo>> responseListener) {

        Call<BaseResponseModel<MoveProcessStatusPojo>> call = RetrofitClient.getInstance().getApiInterface().getMoveProcessStatus(subDomain, job_id, opportunityId, userId);

        call.enqueue(new Callback<BaseResponseModel<MoveProcessStatusPojo>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<MoveProcessStatusPojo>> call, Response<BaseResponseModel<MoveProcessStatusPojo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_MOVE_PROCESS_STATUS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_MOVE_PROCESS_STATUS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_MOVE_PROCESS_STATUS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<MoveProcessStatusPojo>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);

            }
        });
    }


    public void startStopClock(String subdomain, String jobId, String opportunityId, String userId, String currentLatitude, String currentLongitude, String currentProcessId, String startStopStatusId, ClockActivityModel clockActivityModel, ArrayList<WorkerModel> workerList, final ResponseListener<BaseResponseModel<ClockInfoPojo>> responseListener) {

        String currentTime = Util.getFormattedCurrentDate(Constants.INPUT_DATE_FORMAT_COMMENTS);
        String currentTimeMills = (new Date()).getTime() + "";

        Call<BaseResponseModel<ClockInfoPojo>> call = RetrofitClient.getInstance().getApiInterface().startStopClock(subdomain, jobId, opportunityId, userId, currentLatitude, currentLongitude, currentProcessId, startStopStatusId, currentTime, currentTimeMills, clockActivityModel.getCrew(), clockActivityModel.getEquipment(), clockActivityModel.getIsBilable(), clockActivityModel.getIsItemize(), "", clockActivityModel.getRateHour(), clockActivityModel.getIsDoubleDrive(), new Gson().toJson(workerList));

        call.enqueue(new Callback<BaseResponseModel<ClockInfoPojo>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ClockInfoPojo>> call, Response<BaseResponseModel<ClockInfoPojo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.START_STOP_CLOCK);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.START_STOP_CLOCK);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.START_STOP_CLOCK);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ClockInfoPojo>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void startStopBreak(String subdomain, String jobId, String opportunityId, String userId, String jobCockId, String startStopStatusId, final ResponseListener<BaseResponseModel<String>> responseListener) {

        String currentTime = Util.getFormattedCurrentDate(Constants.INPUT_DATE_FORMAT_COMMENTS);

        String currentTimeMills = (new Date()).getTime() + "";

        Call<BaseResponseModel<String>> call = RetrofitClient.getInstance().getApiInterface().startStopBreak(subdomain, jobId, opportunityId, userId, jobCockId, startStopStatusId, currentTime, currentTimeMills);

        call.enqueue(new Callback<BaseResponseModel<String>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<String>> call, Response<BaseResponseModel<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.START_STOP_BREAK);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.START_STOP_BREAK);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.START_STOP_BREAK);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void getStorageChargesDetails(String subdomain, String userId, String opportunityId, String model, String jobId, final ResponseListener<BaseResponseModel<ArrayList<StorageChargesPojo>>> responseListener) {

        Call<BaseResponseModel<ArrayList<StorageChargesPojo>>> call = RetrofitClient.getInstance().getApiInterface().getStorageChargesDetails(subdomain, userId, jobId, opportunityId, model);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<StorageChargesPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<StorageChargesPojo>>> call, Response<BaseResponseModel<ArrayList<StorageChargesPojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<StorageChargesPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getPackingChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<PackingChargesPojo>>> responseListener) {

        Call<BaseResponseModel<ArrayList<PackingChargesPojo>>> call = RetrofitClient.getInstance().getApiInterface().getPackingChargesDetails(subdomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.PACKING_MODEL_TEXT);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<PackingChargesPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<PackingChargesPojo>>> call, Response<BaseResponseModel<ArrayList<PackingChargesPojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                }

            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<PackingChargesPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getAdditionalChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<AdditionalChargesPojo>>> responseListener) {

        Call<BaseResponseModel<ArrayList<AdditionalChargesPojo>>> call = RetrofitClient.getInstance().getApiInterface().getAdditionalChargesDetails(subdomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.ADDITIONAL_MODEL_TEXT);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<AdditionalChargesPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<AdditionalChargesPojo>>> call, Response<BaseResponseModel<ArrayList<AdditionalChargesPojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<AdditionalChargesPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getCratingChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<CratingChargesPojo>>> responseListener) {

        Call<BaseResponseModel<ArrayList<CratingChargesPojo>>> call = RetrofitClient.getInstance().getApiInterface().getCratingChargesDetails(subdomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.CRATING_MODEL_TEXT);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<CratingChargesPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<CratingChargesPojo>>> call, Response<BaseResponseModel<ArrayList<CratingChargesPojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CratingChargesPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getValuationChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<ValuationChargesPojo>>> responseListener) {

        Call<BaseResponseModel<ArrayList<ValuationChargesPojo>>> call = RetrofitClient.getInstance().getApiInterface().getValuationChargesDetails(subdomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.VALUATION_MODEL_TEXT);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<ValuationChargesPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<ValuationChargesPojo>>> call, Response<BaseResponseModel<ArrayList<ValuationChargesPojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ValuationChargesPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void getMoveChargesAutoPricingDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<MoveChargesAutoPricingPojo>>> responseListener) {

        Call<BaseResponseModel<ArrayList<MoveChargesAutoPricingPojo>>> call = RetrofitClient.getInstance().getApiInterface().getMoveChargesAutoPricingDetails(subdomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.MOVE_CHARGES_MODEL_TEXT, Constants.MoveChargesPriceTypes.MOVE_CHARGES_AUTO_PRICING_INDEX + "");

        call.enqueue(new Callback<BaseResponseModel<ArrayList<MoveChargesAutoPricingPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<MoveChargesAutoPricingPojo>>> call, Response<BaseResponseModel<ArrayList<MoveChargesAutoPricingPojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<MoveChargesAutoPricingPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getMoveChargesManualPricingDetails(String subdomain, String userId, String opportunityId, String jobId, String priceType, final ResponseListener<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>> responseListener) {

        Call<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>> call = RetrofitClient.getInstance().getApiInterface().getMoveChargesManualPricingDetails(subdomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.MOVE_CHARGES_MODEL_TEXT, priceType);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>> call, Response<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getArticleItemList(String subDomain, String inventoryId, String roomId, final ResponseListener<BaseResponseModel<ArrayList<ArticleListItemPojo>>> responseListener) {

        Call<BaseResponseModel<ArrayList<ArticleListItemPojo>>> call = RetrofitClient.getInstance().getApiInterface().getArticleItemList(subDomain, inventoryId, roomId);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<ArticleListItemPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<ArticleListItemPojo>>> call, Response<BaseResponseModel<ArrayList<ArticleListItemPojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.ARTICLE_ITEM_LIST);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.ARTICLE_ITEM_LIST);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.ARTICLE_ITEM_LIST);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ArticleListItemPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void addOrUpdateArticleList(RawBodyRequestModel<ArticleListResponseDetailsModel> rawBodyRequestModel, File file, final ResponseListener<BaseResponseModel> responseListener) {

//        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().addOrUpdateArticleList(subdomain, opportunityId, articleListResponseDetailsModelJsonString);
        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().addOrUpdateArticleList(
                Util.getRequestBody(rawBodyRequestModel.getSubdomain()),
                Util.getRequestBody(rawBodyRequestModel.getOpportunityId()),
                Util.getRequestBody(rawBodyRequestModel.getUserId()),
                Util.getRequestBody(rawBodyRequestModel.getJobId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getTotalVolume()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getTotalWeight()),
                Util.getRequestBody(new Gson().toJson(rawBodyRequestModel.getInputDetails().getNormalItems())),
                Util.getRequestBody(new Gson().toJson(rawBodyRequestModel.getInputDetails().getCustomItems())),
                Util.prepareFilePart("article_signature", file.toURI())
        );

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.MOVE_INFO);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void getValuationMetadat(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModelSecond<List<ValuationItemPojo>, String>> responseListener) {

        Call<BaseResponseModelSecond<List<ValuationItemPojo>, String>> call = RetrofitClient.getInstance().getApiInterface().getValuationMetadata(subdomain, userId, opportunityId, Constants.GET_METADATA_MODEL_VALUATION);

        call.enqueue(new Callback<BaseResponseModelSecond<List<ValuationItemPojo>, String>>() {
            @Override
            public void onResponse(Call<BaseResponseModelSecond<List<ValuationItemPojo>, String>> call, Response<BaseResponseModelSecond<List<ValuationItemPojo>, String>> response) {

                /*if (response.isSuccessful() && response.body() != null) {
                    responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }

            }*/

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModelSecond<List<ValuationItemPojo>, String>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void submitValuationDetails(RawBodyRequestModel<ValuationSubmissionRequestModel> rawBodyRequestModel, File file, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().submitValuationDetails(
                Util.getRequestBody(rawBodyRequestModel.getSubdomain()),
                Util.getRequestBody(rawBodyRequestModel.getUserId()),
                Util.getRequestBody(rawBodyRequestModel.getJobId()),
                Util.getRequestBody(rawBodyRequestModel.getOpportunityId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getValuationSettingsId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getValuationRate()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getValuationUnit()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getDeclaredAmount()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getUserId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getValuationChargesCalculated().getAmount()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getValuationChargesCalculated().getQuantity()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getValuationChargesCalculated().getDescription()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getValuationChargesCalculated().getRate()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getValuationChargesCalculated().getUnit()),
                Util.prepareFilePart("valuation_signature", file.toURI())
        );

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SUBMIT_VALUATION_DETAILS);
                    } else {
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.SUBMIT_VALUATION_DETAILS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.SUBMIT_VALUATION_DETAILS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getValuationSubmittedDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ValuationSubmissionRequestModel>> responseListener) {

        Call<BaseResponseModel<ValuationSubmissionRequestModel>> call = RetrofitClient.getInstance().getApiInterface().getValuationSubmittedDetails(subdomain, userId, jobId, opportunityId, Constants.GET_METADATA_MODEL_VALUATION);

        call.enqueue(new Callback<BaseResponseModel<ValuationSubmissionRequestModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ValuationSubmissionRequestModel>> call, Response<BaseResponseModel<ValuationSubmissionRequestModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_SUBMITTED_DETAILS);
                    } else {
                        BaseResponseModel serverResponseError = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverResponseError, ApiEndPoints.GET_SUBMITTED_DETAILS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                }

            }

            @Override
            public void onFailure(Call<BaseResponseModel<ValuationSubmissionRequestModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);

            }
        });
    }


    public void getRentalAgreementMetadata(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<RentalAgreementResponseModel>> responseListener) {

        Call<BaseResponseModel<RentalAgreementResponseModel>> call = RetrofitClient.getInstance().getApiInterface().getRentalAgreementMetadata(subdomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.RENTAL_AGREEMENT_TEXT);

        call.enqueue(new Callback<BaseResponseModel<RentalAgreementResponseModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<RentalAgreementResponseModel>> call, Response<BaseResponseModel<RentalAgreementResponseModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);

                } else {
                    BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.MOVE_INFO);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<RentalAgreementResponseModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void submitRentalAgreementDetails(RawBodyRequestModel<RentalAgreementPojo> rawBodyRequestModel, File file, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().submitStorageAgreementDetails(
                Util.getRequestBody(rawBodyRequestModel.getSubdomain()),
                Util.getRequestBody(rawBodyRequestModel.getUserId()),
                Util.getRequestBody(rawBodyRequestModel.getOpportunityId()),
                Util.getRequestBody(rawBodyRequestModel.getJobId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getDescription()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getCity()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getUserId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getOccupantsName()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getDateOfLease()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getState()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getPhoneNumber()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getAddress()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getZipCode()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getEstimateVolume()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getEstimateWeight()),
                Util.prepareFilePart("storage_signature", file.toURI())
        );

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SUBMIT_STORAGE_AGREEMENT_DETAILS);
                    } else {
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.SUBMIT_STORAGE_AGREEMENT_DETAILS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.SUBMIT_STORAGE_AGREEMENT_DETAILS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void uploadPhoto(RawBodyRequestModel<PhotoUploadPojo> rawBodyRequestModel, MultipartBody.Part file, final ResponseListener<BaseResponseModel<ImagesModel>> responseListener) {

        Call<BaseResponseModel<ImagesModel>> call = RetrofitClient.getInstance().getApiInterface().uploadPhoto(
                Util.getRequestBody(rawBodyRequestModel.getSubdomain()),
                Util.getRequestBody(rawBodyRequestModel.getJobId()),
                Util.getRequestBody(rawBodyRequestModel.getOpportunityId()),
                Util.getRequestBody(rawBodyRequestModel.getUserId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getDescription()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getTitle()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getUserId()),
                file
        );

        call.enqueue(new Callback<BaseResponseModel<ImagesModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ImagesModel>> call, Response<BaseResponseModel<ImagesModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.UPLOAD_PHOTOES);
                    } else {
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.UPLOAD_PHOTOES);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.UPLOAD_PHOTOES);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ImagesModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getRentalAgreementSubmittedDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> responseListener) {

        Call<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> call = RetrofitClient.getInstance().getApiInterface().getRentalAgreementSubmittedDetails(subdomain, userId, jobId, opportunityId, Constants.GET_SUBMITTED_DETAILS_MODEL_STORAGE);

        call.enqueue(new Callback<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> call, Response<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_SUBMITTED_DETAILS);
                    } else {
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                }

            }

            @Override
            public void onFailure(Call<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);

            }
        });
    }


    public void getReleaseFormMetadata(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<ReleaseFormResponseModel>> responseListener) {

        Call<BaseResponseModel<ReleaseFormResponseModel>> call = RetrofitClient.getInstance().getApiInterface().getReleaseFormMetadata(subdomain, userId, opportunityId, Constants.GET_METADATA_MODEL_RELEASE_FORM);

        call.enqueue(new Callback<BaseResponseModel<ReleaseFormResponseModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ReleaseFormResponseModel>> call, Response<BaseResponseModel<ReleaseFormResponseModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }

            }

            @Override
            public void onFailure(Call<BaseResponseModel<ReleaseFormResponseModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);

            }
        });
    }


    public void submitReleaseFormDetails(RawBodyRequestModel<ReleaseFormRequestModel> rawBodyRequestModel, File file, final ResponseListener<BaseResponseModel> responseListener) {

        HashMap<String, RequestBody> map = new HashMap<>(0);
        map.put("subdomain", Util.getRequestBody(rawBodyRequestModel.getSubdomain()));
        map.put("job_id", Util.getRequestBody(rawBodyRequestModel.getJobId()));
        map.put("user_id", Util.getRequestBody(rawBodyRequestModel.getUserId()));
        map.put("opportunity_id", Util.getRequestBody(rawBodyRequestModel.getOpportunityId()));
        //  map.put("r_id", Util.getRequestBody(rawBodyRequestModel.getInputDetails().getSelectionId()));
        map.put("r_id", Util.getRequestBody(""));
        for (int i = 0; i < rawBodyRequestModel.getInputDetails().getAddressList().size(); i++) {
            map.put("address[" + i + "]", Util.getRequestBody(rawBodyRequestModel.getInputDetails().getAddressList().get(i)));
        }
        map.put("description", Util.getRequestBody(rawBodyRequestModel.getInputDetails().getDescription()));
        map.put("releaseform_id", Util.getRequestBody(rawBodyRequestModel.getInputDetails().getReleaseFormId()));

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().submitReleaseFormDetails(map, Util.prepareFilePart("releaseform_signature", file.toURI()));

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SUBMIT_RELEASE_FORM_DETAILS);
                    } else {
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.SUBMIT_RELEASE_FORM_DETAILS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.SUBMIT_RELEASE_FORM_DETAILS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getReleaseFormSubmittedDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<ReleaseFormRequestModel>>> responseListener) {

        Call<BaseResponseModel<List<ReleaseFormRequestModel>>> call = RetrofitClient.getInstance().getApiInterface().getReleaseFormSubmittedDetails(subdomain, userId, jobId, opportunityId, Constants.GET_SUBMITTED_DETAILS_MODEL_RELEASE_FORM);

        call.enqueue(new Callback<BaseResponseModel<List<ReleaseFormRequestModel>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<List<ReleaseFormRequestModel>>> call, Response<BaseResponseModel<List<ReleaseFormRequestModel>>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_SUBMITTED_DETAILS);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_SUBMITTED_DETAILS);
                }

            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<ReleaseFormRequestModel>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);

            }
        });
    }

    public void getSubmittedPhotosUrls(String subDomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<PhotoModel>>> responseListener) {

        Call<BaseResponseModel<List<PhotoModel>>> call = RetrofitClient.getInstance().getApiInterface().getSubmittedPhotosUrls(subDomain, userId, jobId, opportunityId);

        call.enqueue(new Callback<BaseResponseModel<List<PhotoModel>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<List<PhotoModel>>> call, Response<BaseResponseModel<List<PhotoModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_SUBMITTED_PHOTOES);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.GET_SUBMITTED_PHOTOES);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.GET_SUBMITTED_PHOTOES);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<List<PhotoModel>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void submitConfirmationDetails(RawBodyRequestModel<ConfirmationSubmitRequestModel> rawBodyRequestModel, MultipartBody.Part file, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().submitConfirmationDetails(
                Util.getRequestBody(rawBodyRequestModel.getSubdomain()),
                Util.getRequestBody(rawBodyRequestModel.getJobId()),
                Util.getRequestBody(rawBodyRequestModel.getOpportunityId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getRescheduleAgreed()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getPolicyAgreed()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getUserId()),
                Util.getRequestBody(rawBodyRequestModel.getInputDetails().getId()),
                file);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SUBMIT_CONFIRMATION_DETAILS);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SUBMIT_CONFIRMATION_DETAILS);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SUBMIT_CONFIRMATION_DETAILS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void deleteArticle(String subdomain, String userId, String opportunityId, String itemId, String id, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().deleteArticle(subdomain, userId, opportunityId, itemId, id);
        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.ARTICLE_DELETE);
                    } else {
                        BaseResponseModel serverResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverResponseModel, ApiEndPoints.ARTICLE_DELETE);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.ARTICLE_DELETE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }


    public void deleteItems(String subdomain, String userId, String opportunityId, String concatenatedIds, String jobId, String modelName, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().deleteItems(subdomain, modelName, opportunityId, jobId, userId, concatenatedIds);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                /*if (response.isSuccessful() && response.body() != null) {

                    responseListener.onResponse(response.body(), ApiEndPoints.DELETE_ITEMS);

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ITEMS);
                }
            }*/

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.DELETE_ITEMS);
                    } else {
                        BaseResponseModel serverResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverResponseModel, ApiEndPoints.DELETE_ITEMS);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.DELETE_ITEMS);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void addNote(RawNotesRequest rawNotesRequest, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().addNote(rawNotesRequest);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.ADD_NOTES);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.ADD_NOTES);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.ADD_NOTES);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getSubmittedNotes(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<NotesPojo>>> responseListener) {

        Call<BaseResponseModel<List<NotesPojo>>> call = RetrofitClient.getInstance().getApiInterface().getSubmittedNotes(subdomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.NOTES_TEXT);

        call.enqueue(new Callback<BaseResponseModel<List<NotesPojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<List<NotesPojo>>> call, Response<BaseResponseModel<List<NotesPojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.ADD_NOTES);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.ADD_NOTES);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.ADD_NOTES);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<NotesPojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void submitCouponCode(String subdomain, String userId, String opportunityId, String couponCode, final ResponseListener<BaseResponseModel<CouponDetailsModel>> responseListener) {
        Call<BaseResponseModel<CouponDetailsModel>> call = RetrofitClient.getInstance().getApiInterface().submitCouponCode(subdomain, userId, opportunityId, couponCode);

        call.enqueue(new Callback<BaseResponseModel<CouponDetailsModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<CouponDetailsModel>> call, Response<BaseResponseModel<CouponDetailsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SUBMIT_COUPON);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SUBMIT_COUPON);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SUBMIT_COUPON);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<CouponDetailsModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getBolChargesList(String subdomain, String userId, String opportunityId, String model, String jobId, final ResponseListener<BaseResponseModel<CommonChargesResponseModel>> responseListener) {

        Call<BaseResponseModel<CommonChargesResponseModel>> call = RetrofitClient.getInstance().getApiInterface().getBolChargesList(subdomain, userId, jobId, opportunityId, model);

        call.enqueue(new Callback<BaseResponseModel<CommonChargesResponseModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<CommonChargesResponseModel>> call, Response<BaseResponseModel<CommonChargesResponseModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.MOVE_INFO);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.MOVE_INFO);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.MOVE_INFO);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<CommonChargesResponseModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void saveBOLCharges(RawChargesSaveRequestModel<CommonChargesRequestModel> rawChargesSaveRequestModel, final ResponseListener<BaseResponseModel<String>> responseListener) {

        Call<BaseResponseModel<String>> call = RetrofitClient.getInstance().getApiInterface().saveBOLCharges(rawChargesSaveRequestModel);

        call.enqueue(new Callback<BaseResponseModel<String>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<String>> call, Response<BaseResponseModel<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SAVE_BOL_CHARGES);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SAVE_BOL_CHARGES);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SAVE_BOL_CHARGES);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void deleteBolChargesItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, String model, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().deleteItems(subdomain, model, opportunityId, jobId, userId, concatinatedIds);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.DELETE_ITEMS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ITEMS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ITEMS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void submitBOLDetails(RawBodyBOLSubmitRequestModel<BillOfLadingRequestModel> rawBodyBOLSubmitRequestModel, final ResponseListener<BaseResponseModel<String>> responseListener) {

        Call<BaseResponseModel<String>> call = RetrofitClient.getInstance().getApiInterface().submitBOLDetails(rawBodyBOLSubmitRequestModel);

        call.enqueue(new Callback<BaseResponseModel<String>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<String>> call, Response<BaseResponseModel<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    /*In other responses we are getting status as Success or failure. In this case we get it as 1, 2 where
                     * 1 means BOL has been sent for approval and 2 means it has been submitted. Hence here instead of comparing
                     * status I am comparing response code to check if response is successful or not.
                     */
                    if (TextUtils.equals(response.body().getResponseCode() + "", 200 + "")) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SUBMIT_BOL);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SUBMIT_BOL);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SUBMIT_BOL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getBOLJobConfirmation(String subDomain, String user_id, String opportunityId, String job_id, long totaltime, double totalHour, final ResponseListener<BaseResponseModel<BolDetailsPojo>> responseListener) {

        //   long totalTimeInMillis = Util.getLongFromString(totalTimeForMoveChargesCalculation);
        //   double hours = (totalTimeInMillis / (1000.00 * 60.00 * 60.00));
        double time = totaltime / (1000.00 * 60.00 * 60.00);
        double hour = totalHour / (1000.00 * 60.00 * 60.00);

        Call<BaseResponseModel<BolDetailsPojo>> call = RetrofitClient.getInstance().getApiInterface().getBOLJobConfirmation(subDomain, user_id, opportunityId, job_id, String.valueOf(time), String.valueOf(hour));

        call.enqueue(new Callback<BaseResponseModel<BolDetailsPojo>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<BolDetailsPojo>> call, Response<BaseResponseModel<BolDetailsPojo>> response) {
                if (response.isSuccessful() && response.body() != null) {
//                    try { // Had put it here to catch any JSON parse exception. But was told that it will never happen.
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_BOL_JOB_CONFIRMATION_DETAILS_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_BOL_JOB_CONFIRMATION_DETAILS_URL);
                    }
                    /*}catch (Exception e){
                        responseListener.onFailure(call, e.getCause());
                    }*/

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_BOL_JOB_CONFIRMATION_DETAILS_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<BolDetailsPojo>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void saveBolTipsOrDiscount(String subdomain, String userId, String opportunityId, String bolChargesId, String discountType, String amount, String jobId, String calculatedAmount, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().saveBolTipsOrDiscount(subdomain, Constants.BOLStringConstants.SUBMIT_TIP_MODEL, userId, jobId, opportunityId, bolChargesId, discountType, amount, calculatedAmount);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }


    public void submitRating(String subdomain, String userId, String opportunityId, String bolChargesId, String rating, String message, String jobId, final ResponseListener<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> responseListener) {

        Call<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> call = RetrofitClient.getInstance().getApiInterface().submitRating(subdomain, Constants.BOLStringConstants.SUBMIT_RATING_MODEL, userId, jobId, opportunityId, bolChargesId, rating, message);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> call, Response<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }


    public void submitReviewDiscount(String subdomain, String userId, String opportunityId, String bolChargesId, String amount, String jobId, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().submitReviewDiscount(subdomain, Constants.BOLStringConstants.SUBMIT_REVIEW_DISCOUNT_MODEL, userId, jobId, opportunityId, bolChargesId, amount);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void submitBOLSignature(RawBodyBOLSignatureSubmitRequestModel submitRequestModel, File file, final ResponseListener<BaseResponseModel<String>> responseListener) {

        Call<BaseResponseModel<String>> call = RetrofitClient.getInstance().getApiInterface().submitBOLSignature(
                Util.getRequestBody(submitRequestModel.getSubdomain()),
                Util.getRequestBody(submitRequestModel.getJobId()),
                Util.getRequestBody(submitRequestModel.getUserId()),
                Util.getRequestBody(submitRequestModel.getOpportunityId()),
                Util.getRequestBody(submitRequestModel.getBolFinalChargeId()),
                Util.prepareFilePart("bol_signature", file.toURI())
        );

        call.enqueue(new Callback<BaseResponseModel<String>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<String>> call, Response<BaseResponseModel<String>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.BOL_SIGNATURE);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void getPaymentMethods(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<List<PaymentMethodsModel>>> responseListener) {

        Call<BaseResponseModel<List<PaymentMethodsModel>>> call = RetrofitClient.getInstance().getApiInterface().getPaymentMethods(subdomain, userId, opportunityId, "payment_mode");

        call.enqueue(new Callback<BaseResponseModel<List<PaymentMethodsModel>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<List<PaymentMethodsModel>>> call, Response<BaseResponseModel<List<PaymentMethodsModel>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<PaymentMethodsModel>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }


    public void getSpreedlySetup(String subdomain, String userId, String jobId, String opportunityId, final ResponseListener<BaseResponseModel<SpreedlyInfoModel>> responseListener) {
        // Call<BaseResponseModel<SpreedlyInfoModel>> call = RetrofitClient.getInstance().getApiInterface().getSpreedlySetup(subdomain, userId, jobId, opportunityId, "spreedly_setup_new"); server side payment  not include in payment History.
        Call<BaseResponseModel<SpreedlyInfoModel>> call = RetrofitClient.getInstance().getApiInterface().getSpreedlySetup(subdomain, userId, jobId, opportunityId, "spreedly_setup");

        call.enqueue(new Callback<BaseResponseModel<SpreedlyInfoModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<SpreedlyInfoModel>> call, Response<BaseResponseModel<SpreedlyInfoModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<SpreedlyInfoModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void submitPayment(SpreedlyPaymentTokenSubmitRequestModel submitRequestModel, File paymentSignature, File additionalInfo, final ResponseListener<BaseResponseModel<String>> responseListener) {
        URI paymentUri = null;
        URI additionalImageUri = null;

        if (paymentSignature != null) {
            paymentUri = paymentSignature.toURI();
        }

        if (additionalInfo != null) {
            additionalImageUri = additionalInfo.toURI();
        }

        Call<BaseResponseModel<String>> call = RetrofitClient.getInstance().getApiInterface().submitPayment(
                Util.getRequestBody(submitRequestModel.getSubdomain()),
                Util.getRequestBody(submitRequestModel.getJobId()),
                Util.getRequestBody(submitRequestModel.getUserId()),
                Util.getRequestBody(submitRequestModel.getOpportunityId()),
                Util.getRequestBody(submitRequestModel.getCardConvenienceFeeValue()),
                Util.getRequestBody(submitRequestModel.getAmount()),
                Util.getRequestBody(submitRequestModel.getDepositeAmount()),
                Util.getRequestBody(submitRequestModel.getAmountWithCardConvenienceFeeNotAdded()),
                Util.getRequestBody(submitRequestModel.getPaymentType()),
                Util.getRequestBody(submitRequestModel.getOriginalAmount()),
                Util.getRequestBody(submitRequestModel.getCardConvenienceFeeType()),
                Util.getRequestBody(submitRequestModel.getPaymentMethod()),
                Util.getRequestBody(submitRequestModel.getBolFinalChargeId()),
                Util.getRequestBody(submitRequestModel.getPaymentToken()),
                Util.prepareFilePart("payment_signature", paymentUri),
                Util.prepareFilePart("additional_image", additionalImageUri)
        );

        call.enqueue(new Callback<BaseResponseModel<String>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<String>> call, Response<BaseResponseModel<String>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.BOL_SIGNATURE);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getTipDiscountList(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<ArrayList<TipsModel>>> responseListener) {
        Call<BaseResponseModel<ArrayList<TipsModel>>> call = RetrofitClient.getInstance().getApiInterface().getTipDiscountList(subdomain, userId, opportunityId, "tips_discount");

        call.enqueue(new Callback<BaseResponseModel<ArrayList<TipsModel>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<TipsModel>>> call, Response<BaseResponseModel<ArrayList<TipsModel>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<TipsModel>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getNotificationList(String subDomain, String user_id, final ResponseListener<BaseResponseModel<NotificationListResponse>> responseListener) {


        Call<BaseResponseModel<NotificationListResponse>> call = RetrofitClient.getInstance().getApiInterface().getNotificationList(subDomain, user_id);

        call.enqueue(new Callback<BaseResponseModel<NotificationListResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<NotificationListResponse>> call, Response<BaseResponseModel<NotificationListResponse>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
//                        if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_NOTIFICATION_LIST);
                        /*} else {
                            BaseResponseModel baseResponseModel = Util.parseError(response);
                            responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_NOTIFICATION_LIST);
                        }*/
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.GET_NOTIFICATION_LIST);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_NOTIFICATION_LIST);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<NotificationListResponse>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void notificationUpdate(String subDomain, String r_id, String user_id, final ResponseListener<BaseResponseModel> responseListener) {


        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().notificationUpdate(subDomain, r_id, user_id);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
//                        if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.NOTIFICATION_UPDATE);
                        /*} else {
                            BaseResponseModel baseResponseModel = Util.parseError(response);
                            responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_NOTIFICATION_LIST);
                        }*/
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.NOTIFICATION_UPDATE);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.NOTIFICATION_UPDATE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void logout(String subdomain, String userId, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().logout(subdomain, userId);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.LOGOUT_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.LOGOUT_URL);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.LOGOUT_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void setBolStarted(String subdomain, String userId, String opportunityId, String bolFinalChargeId, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().setBolStarted(subdomain, opportunityId, bolFinalChargeId, jobId, userId);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.BOL_STARTED);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_STARTED);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_STARTED);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void setCardConvenienceFee(String subdomain, String userId, String opportunityId, String bolChargesId, String discountType, String amount, String jobId, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().setCardConvenienceFee(subdomain, Constants.BOLStringConstants.SET_CARD_CONVINIENCE_FEE_MODEL, userId, jobId, opportunityId, bolChargesId, discountType, amount);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.BOL_TIPS_AND_DISCOUNTS);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getETA(String originLocation, String destinationLocation, final ResponseListener<ETAResponseModel> responseListener) {

        Call<ETAResponseModel> call = RetrofitClient.getInstance().getApiInterface().getETA(originLocation, destinationLocation, BuildConfig.GOOGLE_KEY_FOR_DIRECTION_API);

        call.enqueue(new Callback<ETAResponseModel>() {
            @Override
            public void onResponse(Call<ETAResponseModel> call, Response<ETAResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatusSuccess()) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(404, response.body().getStatus(), response.body().getErrorMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }
            }

            @Override
            public void onFailure(Call<ETAResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getAddressList(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<AddressListResponseModel>> responseListener) {
        Call<BaseResponseModel<AddressListResponseModel>> call = RetrofitClient.getInstance().getApiInterface().getAddressList(subdomain, opportunityId, userId);

        call.enqueue(new Callback<BaseResponseModel<AddressListResponseModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<AddressListResponseModel>> call, Response<BaseResponseModel<AddressListResponseModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.BOL_STARTED);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_STARTED);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_STARTED);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<AddressListResponseModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void sendETA(String subdomain, String userId, String opportunityId, String phoneNumber, String etaMessage, String destinationAddress, final ResponseListener<BaseResponseModel> responseListener) {
        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().sendEta(subdomain, opportunityId, userId, phoneNumber, etaMessage, destinationAddress);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SEND_ETA_API);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SEND_ETA_API);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.SEND_ETA_API);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void checkIfUpdateNeeded(String subdomain, final ResponseListener<BaseResponseModel<ForceUpdateResponseModel>> responseListener) {

        Call<BaseResponseModel<ForceUpdateResponseModel>> call = RetrofitClient.getInstance().getApiInterface().checkIfUpdateNeeded(subdomain, String.valueOf(BuildConfig.VERSION_CODE), BuildConfig.VERSION_NAME, Constants.DeviceTypeIds.ANDROID_ID);

        call.enqueue(new Callback<BaseResponseModel<ForceUpdateResponseModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ForceUpdateResponseModel>> call, Response<BaseResponseModel<ForceUpdateResponseModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.VERSION_DETAIL);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.VERSION_DETAIL);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.VERSION_DETAIL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ForceUpdateResponseModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void sendCurrentLocation(RawBodyLocationUpdateRequestModel locationUpdateRequestModel, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().sendCurrentLocation(locationUpdateRequestModel);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.BOL_SIGNATURE);
                    } else {
                        BaseResponseModel serverErrorResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                    }
                } else {
                    BaseResponseModel serverErrorResponseModel = Util.parseError(response);
                    responseListener.onResponseError(serverErrorResponseModel, ApiEndPoints.BOL_SIGNATURE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getClockActivityList(String subdomain, String jobId, String opportunityId, final ResponseListener<BaseResponseModel<ActivityTypeListResponseModel>> responseListener) {

        Call<BaseResponseModel<ActivityTypeListResponseModel>> call = RetrofitClient.getInstance().getApiInterface().getClockActivityList(subdomain, jobId, opportunityId, Constants.GET_METADATA_MODEL_CLOCK_ACTIVITY);

        call.enqueue(new Callback<BaseResponseModel<ActivityTypeListResponseModel>>() {

            @Override
            public void onResponse(Call<BaseResponseModel<ActivityTypeListResponseModel>> call, Response<BaseResponseModel<ActivityTypeListResponseModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ActivityTypeListResponseModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }


    public void getRatePerHour(String subDomain, String jobId, String men, String equipment, String moveDate, final ResponseListener<BaseResponseModel<RatePerHourModel>> responseListener) {

        Call<BaseResponseModel<RatePerHourModel>> call = RetrofitClient.getInstance().getApiInterface().getRatePerHour(subDomain, jobId, men, equipment, moveDate);

        call.enqueue(new Callback<BaseResponseModel<RatePerHourModel>>() {

            @Override
            public void onResponse(Call<BaseResponseModel<RatePerHourModel>> call, Response<BaseResponseModel<RatePerHourModel>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<RatePerHourModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }


    public void getClockHistoryList(String subDomain, String userId, String jobId, String opportunityId, final ResponseListener<BaseResponseModel<ClockHistoryResponse>> responseListener) {

        Call<BaseResponseModel<ClockHistoryResponse>> call = RetrofitClient.getInstance().getApiInterface().getClockHistoryList(subDomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.CLOCK_HISTORY);

        call.enqueue(new Callback<BaseResponseModel<ClockHistoryResponse>>() {

            @Override
            public void onResponse(Call<BaseResponseModel<ClockHistoryResponse>> call, Response<BaseResponseModel<ClockHistoryResponse>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ClockHistoryResponse>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }

    public void updateClockHistory(String subDomain, String userId, String jobId, String opportunityId, ClockHistoryModel clockHistoryModel, final ResponseListener<BaseResponseModel<Objects>> responseListener) {

        Call<BaseResponseModel<Objects>> call = RetrofitClient.getInstance().getApiInterface().updateClockHistory(subDomain, userId, jobId, opportunityId, clockHistoryModel.getActivityId(), clockHistoryModel.getMen(), clockHistoryModel.getTruck(), clockHistoryModel.getStartId(), clockHistoryModel.getStartTime(), clockHistoryModel.getStartTimeApp(), clockHistoryModel.getEndId(), clockHistoryModel.getEndTime(), clockHistoryModel.getEndTimeApp(), clockHistoryModel.getIsBillable(), clockHistoryModel.getItemize(), clockHistoryModel.getRate_hour(), clockHistoryModel.getEventType(), clockHistoryModel.getTotalBreakTime(), new Gson().toJson(clockHistoryModel.getResourceData()));

        call.enqueue(new Callback<BaseResponseModel<Objects>>() {

            @Override
            public void onResponse(Call<BaseResponseModel<Objects>> call, Response<BaseResponseModel<Objects>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<Objects>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }

    public void getArticleRoomList(String subDomain, String opportunityId, final ResponseListener<BaseResponseModel<ArrayList<ArticleRoomModel>>> responseListener) {

        Call<BaseResponseModel<ArrayList<ArticleRoomModel>>> call = RetrofitClient.getInstance().getApiInterface().getArticleRoomList(subDomain, opportunityId);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<ArticleRoomModel>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<ArticleRoomModel>>> call, Response<BaseResponseModel<ArrayList<ArticleRoomModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.ARTICLE_ROOM_LIST);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.ARTICLE_ROOM_LIST);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.ARTICLE_ROOM_LIST);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ArticleRoomModel>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getChargeRateTypeList(String subDomain, String opportunityId, String moveType, String model, int cId, final ResponseListener<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> responseListener) {

        Call<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> call = RetrofitClient.getInstance().getApiInterface().getChargeRateTypeList(subDomain, opportunityId, moveType, model, cId);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<ChargeRateTypeModel>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> call, Response<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.CHARGE_RATE_LIST);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.CHARGE_RATE_LIST);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.CHARGE_RATE_LIST);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void getRateValue(String subDomain, String name, String type, String opportunityId, String unitType, final ResponseListener<BaseResponseModel<ChargesUnitModel>> responseListener) {

        Call<BaseResponseModel<ChargesUnitModel>> call = RetrofitClient.getInstance().getApiInterface().getRateValue(subDomain, name, type, opportunityId, unitType);

        call.enqueue(new Callback<BaseResponseModel<ChargesUnitModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ChargesUnitModel>> call, Response<BaseResponseModel<ChargesUnitModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_RATE_VALUE);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_RATE_VALUE);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_RATE_VALUE);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ChargesUnitModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void deleteActivity(String subDomain, String id, String opportunityId, String jobId, String userId, final ResponseListener<BaseResponseModel<Objects>> responseListener) {

        Call<BaseResponseModel<Objects>> call = RetrofitClient.getInstance().getApiInterface().deleteActivity(subDomain, Constants.MoveInfoModelTypes.CLOCK_ACTIVITY, id, opportunityId, jobId, userId);

        call.enqueue(new Callback<BaseResponseModel<Objects>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<Objects>> call, Response<BaseResponseModel<Objects>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.DELETE_ACTIVITY_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ACTIVITY_URL);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.DELETE_ACTIVITY_URL);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<Objects>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void updateDiscount(String subDomain, String type, String discountValue, String discountType, String bolFinalChargeId, final ResponseListener<BaseResponseModel<Objects>> responseListener) {

        Call<BaseResponseModel<Objects>> call = RetrofitClient.getInstance().getApiInterface().discountUpdate(subDomain, type, discountValue, discountType, bolFinalChargeId);

        call.enqueue(new Callback<BaseResponseModel<Objects>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponseModel<Objects>> call, @NonNull Response<BaseResponseModel<Objects>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.UPDATE_DISCOUNT_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.UPDATE_DISCOUNT_URL);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.UPDATE_DISCOUNT_URL);
                }
            }


            @Override
            public void onFailure(@NonNull Call<BaseResponseModel<Objects>> call, @NonNull Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void getCategoryTypeList(String subDomain, String opportunityId, String moveType, String model, final ResponseListener<BaseResponseModel<ArrayList<CategoryModel>>> responseListener) {

        Call<BaseResponseModel<ArrayList<CategoryModel>>> call = RetrofitClient.getInstance().getApiInterface().getCategoryTypeList(subDomain, opportunityId, moveType, model);
        call.enqueue(new Callback<BaseResponseModel<ArrayList<CategoryModel>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<CategoryModel>>> call, Response<BaseResponseModel<ArrayList<CategoryModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.CHARGE_RATE_LIST);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.CHARGE_RATE_LIST);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.CHARGE_RATE_LIST);
                }
            }


            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CategoryModel>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void getWorkerDetailsList(String subDomain, String userId, String jobId, String opportunityId, final ResponseListener<BaseResponseModel<ArrayList<WorkerDetailsModel>>> responseListener) {

        Call<BaseResponseModel<ArrayList<WorkerDetailsModel>>> call = RetrofitClient.getInstance().getApiInterface().getWorkerDetails(subDomain, userId, jobId, opportunityId, Constants.MoveInfoModelTypes.WORKER_DETAILS);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<WorkerDetailsModel>>>() {

            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<WorkerDetailsModel>>> call, Response<BaseResponseModel<ArrayList<WorkerDetailsModel>>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.GET_METADATA_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                    }
                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.GET_METADATA_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<WorkerDetailsModel>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });

    }


    public void carryForwardPayment(String subdomain, String bolFinalChargeId, int status, String carryForwardAmount, String userId, String jobId, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().carryForwardPayment(subdomain, bolFinalChargeId, status, carryForwardAmount, userId, jobId);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.PAYMENT_CARRY_FORWARD_URL);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.PAYMENT_CARRY_FORWARD_URL);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.PAYMENT_CARRY_FORWARD_URL);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void sendBOLToCustomer(String subdomain, String jobId, String opportunityId, String email, final ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> call = RetrofitClient.getInstance().getApiInterface().sendBOLToCustomer(subdomain, jobId, opportunityId, email);

        call.enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.SEND_BOL_TO_CUSTOMER);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.SEND_BOL_TO_CUSTOMER);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.SEND_BOL_TO_CUSTOMER);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }


    public void carryForwardList(String subDomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<ArrayList<CarryForwardModel>>> responseListener) {

        Call<BaseResponseModel<ArrayList<CarryForwardModel>>> call = RetrofitClient.getInstance().getApiInterface().carryForwardList(subDomain, userId, opportunityId);

        call.enqueue(new Callback<BaseResponseModel<ArrayList<CarryForwardModel>>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<ArrayList<CarryForwardModel>>> call, Response<BaseResponseModel<ArrayList<CarryForwardModel>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.CARRY_FORWARD_LIST);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.CARRY_FORWARD_LIST);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.CARRY_FORWARD_LIST);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CarryForwardModel>>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }

    public void authorizeNetCredentials(String subDomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<CardReaderModel>> responseListener) {

        Call<BaseResponseModel<CardReaderModel>> call = RetrofitClient.getInstance().getApiInterface().cardReaderDetails(subDomain, Constants.BOLStringConstants.CARD_READER_SETUP_MODEL, userId, opportunityId);

        call.enqueue(new Callback<BaseResponseModel<CardReaderModel>>() {
            @Override
            public void onResponse(Call<BaseResponseModel<CardReaderModel>> call, Response<BaseResponseModel<CardReaderModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (TextUtils.equals(response.body().getStatus(), Constants.SUCCESS)) {
                        responseListener.onResponse(response.body(), ApiEndPoints.CARRY_FORWARD_LIST);
                    } else {
                        //BaseResponseModel baseResponseModel = Util.parseError(response);
                        BaseResponseModel baseResponseModel = new BaseResponseModel(response.body().getResponseCode(), response.body().getStatus(), response.body().getMessage());
                        responseListener.onResponseError(baseResponseModel, ApiEndPoints.CARRY_FORWARD_LIST);
                    }

                } else {
                    BaseResponseModel baseResponseModel = Util.parseError(response);
                    responseListener.onResponseError(baseResponseModel, ApiEndPoints.CARRY_FORWARD_LIST);
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<CardReaderModel>> call, Throwable t) {
                Log.e(Constants.BASE_LOG_TAG + "onFailure: ", t.getLocalizedMessage());
                responseListener.onFailure(call, t, Constants.RESPONSE_FAILURE_MESSAGE);
            }
        });
    }
}

