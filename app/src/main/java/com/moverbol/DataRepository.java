package com.moverbol;

import android.util.Log;

import androidx.annotation.Nullable;

import com.moverbol.constants.Constants;
import com.moverbol.database.JobDao;
import com.moverbol.model.ActivityTypeListResponseModel;
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
import com.moverbol.network.ResponseListener;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.BaseResponseModelSecond;
import com.moverbol.network.model.JobPojo;
import com.moverbol.network.model.SignInData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import retrofit2.Call;

import static com.moverbol.constants.Constants.BASE_LOG_TAG;

/**
 * Created by AkashM on 27/11/17.
 */

public class DataRepository {

    private static final String LOG_TAG = BASE_LOG_TAG + DataRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static DataRepository sInstance;
    private final WebServiceManager mWebServiceManager;

    private JobDetailPojo jobDetailPojo;
//    private boolean mInitialized = false;

//    private ArticleListResponseDetailsModel articleListResponseDetailsModel;

    private JobConfirmationDetailsPojo jobConfirmationDetailsPojo;

//    private BillOfLadingPojo billOfLadingPojo;

    private DataRepository() {
        this.mWebServiceManager = WebServiceManager.getInstance();
    }

    public synchronized static DataRepository getInstance() {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new DataRepository();
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }


    public void doLogin(Call<BaseResponseModel<SignInData>> call, final ResponseListener<BaseResponseModel<SignInData>> responseListener) {
        mWebServiceManager.doLogin(call, responseListener);
    }

    public void getRefreshedJobList(final JobDao jobDao, String subdomain, String user_id, final ResponseListener<BaseResponseModel<List<JobPojo>>> responseListener) {

//        final List<JobPojo> jobPojoList = new ArrayList<>();

        mWebServiceManager.getNewJobs(subdomain, user_id, new ResponseListener<BaseResponseModel<List<JobPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<JobPojo>> response, String usedUrlKey) {
                //TODO:Remove after webservice stops giving rejected jobs.
                for (int i = 0; i < response.getData().size(); i++) {
                    if (response.getData().get(i).jobStatus.equals(Constants.JOB_STATUS_REJECTED)) {
                        response.getData().remove(i);
                    }
                }
//                jobPojoList.addAll(response.getData());

//                jobDao.bulkInsert(response.getJobPojoList());//InsertData in Dao.
                //No method of responseListenerCalled
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<JobPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });

//        return getJobListFromDB(jobDao);
//        return jobPojoList;
    }

//    public List<JobPojo> getJobListFromDB(JobDao jobDao) {
//        return jobDao.getNewJobList();
//    }

    public void setJobDetails(String subDomain, String userId, String opportunityId, String job_id, final ResponseListener<BaseResponseModel<JobDetailPojo>> responseListener) {
        mWebServiceManager.getJobDetails(subDomain, userId, opportunityId, job_id, new ResponseListener<BaseResponseModel<JobDetailPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobDetailPojo> response, String usedUrlKey) {
                jobDetailPojo = response.getData();
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public JobDetailPojo getJobDetailPojo() {
        return jobDetailPojo;
    }

    public void getJobConfirmation(String subDomain, String user_id, String opportunityId, String job_id, final ResponseListener<BaseResponseModel<JobConfirmationDetailsPojo>> responseListener) {
        mWebServiceManager.getJobConfirmation(subDomain, user_id, opportunityId, job_id, new ResponseListener<BaseResponseModel<JobConfirmationDetailsPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobConfirmationDetailsPojo> response, String usedUrlKey) {
                jobConfirmationDetailsPojo = response.getData();
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobConfirmationDetailsPojo>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public JobConfirmationDetailsPojo getJobConfirmationDetailsPojo() {
        return jobConfirmationDetailsPojo;
    }

    /*public BillOfLadingPojo getBillOfLadingPojo() {
        return billOfLadingPojo;
    }

    public void setBillOfLadingPojo(BillOfLadingPojo billOfLadingPojo) {
        this.billOfLadingPojo = billOfLadingPojo;
    }*/

    public void setJobStatus(String subDomain, String userId, String opportunityId, String job_id, String status, @Nullable String comments, String totalCharges, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.setJobStatus(subDomain, userId, opportunityId, job_id, status, comments, totalCharges, responseListener);
    }

    public void getCrewMetadat(String subDomain, String userId, String opportunityId, String jobId, ResponseListener<BaseResponseModel<List<CrewMetadata>>> responseListener) {
        mWebServiceManager.getCrewMetadat(subDomain, userId, opportunityId, jobId, responseListener);
    }

    public void getTruckMetadat(String subdomain, String userId, String opportunityId, ResponseListener<BaseResponseModel<List<TruckMetadata>>> responseListener) {
        mWebServiceManager.getTruckMetadat(subdomain, userId, opportunityId, responseListener);
    }

    public void getMaterialMetadat(String subdomain, String userId, String opportunityId, ResponseListener<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> responseListener) {
        mWebServiceManager.getMaterialMetadat(subdomain, userId, opportunityId, responseListener);
    }

    public void getWorkerById(String subDomain, String userId, String opportunityId, String workerId, String jobId, final ResponseListener<BaseResponseModel<ManPowerPojo>> responseListener) {
        mWebServiceManager.getWorkerById(subDomain, userId, opportunityId, workerId, jobId, responseListener);
    }

    public void getTruckById(String subDomain, String userId, String opportunityId, String truckId, String jobId, final ResponseListener<BaseResponseModel<TruckPojo>> responseListener) {
        mWebServiceManager.getTruckById(subDomain, userId, opportunityId, truckId, jobId, responseListener);
    }

    public void getMaterialById(String subDomain, String userId, String opportunityId, String materialId, String jobId, final ResponseListener<BaseResponseModel<MaterialPojo>> responseListener) {
        mWebServiceManager.getMaterialById(subDomain, userId, opportunityId, materialId, jobId, responseListener);
    }

    public void deleteCrewItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.deleteCrewItems(subdomain, userId, opportunityId, jobId, concatinatedIds, responseListener);
    }

    public void deleteTruckItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.deleteTruckItems(subdomain, userId, opportunityId, jobId, concatinatedIds, responseListener);
    }

    public void deleteMaterialItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.deleteMaterialItems(subdomain, userId, opportunityId, jobId, concatinatedIds, responseListener);
    }

    public void addCrewItems(String subdomain, String jobId, String opportunityId, String workertype, String responsibleUser, String tempWorker, String remarks, String manpowerId, String userId, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.addCrewItems(subdomain, jobId, opportunityId, workertype, responsibleUser, tempWorker, remarks, manpowerId, userId, responseListener);
    }

    public void addTruckItems(String subdomain, String jobId, String opportunityId, String vehicleType, String vehicleNumber, String tempVehicle, String remarks, String vehicleId, String userId, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.addTruckItems(subdomain, jobId, opportunityId, vehicleType, vehicleNumber, tempVehicle, remarks, vehicleId, userId, responseListener);
    }

    public void addMaterialItems(String subdomain, String userId, String opportunityId, String jobId, String material, String quantity, String materialUnit, String materialRate, String totalMaterialRate, String remarks, String materialId, int cId, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.addMaterialItems(subdomain, userId, opportunityId, jobId, material, quantity, materialUnit, materialRate, totalMaterialRate, remarks, materialId, cId, responseListener);
    }

    public void getMoveProcessStatus(String subDomain, String job_id, String opportunityId, String userId, final ResponseListener<BaseResponseModel<MoveProcessStatusPojo>> responseListener) {
        mWebServiceManager.getMoveProcessStatus(subDomain, job_id, opportunityId, userId, responseListener);
    }

    public void startStopClock(String subdomain, String jobId, String opportunityId, String userId, String currentLatitude, String currentLongitude, String currentProcessId, String startStopStatusId, ClockActivityModel clockActivityModel, ArrayList<WorkerModel> workerList, final ResponseListener<BaseResponseModel<ClockInfoPojo>> responseListener) {
        mWebServiceManager.startStopClock(subdomain, jobId, opportunityId, userId, currentLatitude, currentLongitude, currentProcessId, startStopStatusId, clockActivityModel, workerList, responseListener);
    }

    public void startStopBreak(String subdomain, String jobId, String opportunityId, String userId, String jobCockId, String startStopStatusId, final ResponseListener<BaseResponseModel<String>> responseListener) {
        mWebServiceManager.startStopBreak(subdomain, jobId, opportunityId, userId, jobCockId, startStopStatusId, responseListener);
    }

    public void getStorageChargesDetails(String subdomain, String userId, String opportunityId, String model, String jobId, final ResponseListener<BaseResponseModel<ArrayList<StorageChargesPojo>>> responseListener) {
        mWebServiceManager.getStorageChargesDetails(subdomain, userId, opportunityId, model, jobId, responseListener);
    }

    public void getPackingChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<PackingChargesPojo>>> responseListener) {
        mWebServiceManager.getPackingChargesDetails(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void getAdditionalChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<AdditionalChargesPojo>>> responseListener) {
        mWebServiceManager.getAdditionalChargesDetails(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void getCratingChargesDetails(String subdomain, String userId, String opportunityId, String jobId, ResponseListener<BaseResponseModel<ArrayList<CratingChargesPojo>>> responseListener) {
        mWebServiceManager.getCratingChargesDetails(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void getValuationChargesDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<ValuationChargesPojo>>> responseListener) {
        mWebServiceManager.getValuationChargesDetails(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void getMoveChargesAutoPricingDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ArrayList<MoveChargesAutoPricingPojo>>> responseListener) {
        mWebServiceManager.getMoveChargesAutoPricingDetails(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void getMoveChargesManualPricingDetails(String subdomain, String userId, String opportunityId, String jobId, String priceType, final ResponseListener<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>> responseListener) {
        mWebServiceManager.getMoveChargesManualPricingDetails(subdomain, userId, opportunityId, jobId, priceType, responseListener);
    }

    public void getArticleItemList(String subDomain, String inventoryId, String roomId, final ResponseListener<BaseResponseModel<ArrayList<ArticleListItemPojo>>> responseListener) {
        mWebServiceManager.getArticleItemList(subDomain, inventoryId, roomId, new ResponseListener<BaseResponseModel<ArrayList<ArticleListItemPojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ArticleListItemPojo>> response, String usedUrlKey) {
//                articleListResponseDetailsModel = response.getData();
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ArticleListItemPojo>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    /*public ArticleListResponseDetailsModel getArticleListResponseDetailsModel() {
        return articleListResponseDetailsModel;
    }

    public void setArticleListResponseDetailsModel(ArticleListResponseDetailsModel articleListResponseDetailsModel) {
        this.articleListResponseDetailsModel = articleListResponseDetailsModel;
    }*/

    public void addOrUpdateArticleList(RawBodyRequestModel<ArticleListResponseDetailsModel> rawBodyRequestModel, File file, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.addOrUpdateArticleList(rawBodyRequestModel, file, responseListener);
    }

    public void getValuationMetadat(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModelSecond<List<ValuationItemPojo>, String>> responseListener) {
        mWebServiceManager.getValuationMetadat(subdomain, userId, opportunityId, responseListener);
    }

    public void submitValuationDetails(RawBodyRequestModel<ValuationSubmissionRequestModel> rawBodyRequestModel, File file, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.submitValuationDetails(rawBodyRequestModel, file, responseListener);
    }

    public void getValuationSubmittedDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<ValuationSubmissionRequestModel>> responseListener) {
        mWebServiceManager.getValuationSubmittedDetails(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void getRentalAgreementMetadata(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<RentalAgreementResponseModel>> responseListener) {
        mWebServiceManager.getRentalAgreementMetadata(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void submitRentalAgreementDetails(RawBodyRequestModel<RentalAgreementPojo> rawBodyRequestModel, File file, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.submitRentalAgreementDetails(rawBodyRequestModel, file, responseListener);
    }

    public void uploadPhoto(RawBodyRequestModel<PhotoUploadPojo> rawBodyRequestModel, MultipartBody.Part file, final ResponseListener<BaseResponseModel<ImagesModel>> responseListener) {
        mWebServiceManager.uploadPhoto(rawBodyRequestModel, file, responseListener);
    }


    public void getRentalAgreementSubmittedDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> responseListener) {
        mWebServiceManager.getRentalAgreementSubmittedDetails(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void getReleaseFormMetadata(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<ReleaseFormResponseModel>> responseListener) {
        mWebServiceManager.getReleaseFormMetadata(subdomain, userId, opportunityId, responseListener);
    }

    public void submitReleaseFormDetails(RawBodyRequestModel<ReleaseFormRequestModel> rawBodyRequestModel, File file, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.submitReleaseFormDetails(rawBodyRequestModel, file, responseListener);
    }

    public void getReleaseFormSubmittedDetails(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<ReleaseFormRequestModel>>> responseListener) {
        mWebServiceManager.getReleaseFormSubmittedDetails(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void getSubmittedPhotosUrls(String subDomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<PhotoModel>>> responseListener) {
        mWebServiceManager.getSubmittedPhotosUrls(subDomain, userId, opportunityId, jobId, responseListener);
    }

    public void submitConfirmationDetails(RawBodyRequestModel<ConfirmationSubmitRequestModel> rawBodyRequestModel, MultipartBody.Part file, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.submitConfirmationDetails(rawBodyRequestModel, file, responseListener);
    }

    public void deleteArticle(String subdomain, String userId, String opportunityId, String itemId, String id, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.deleteArticle(subdomain, userId, opportunityId, itemId, id, responseListener);
    }

    public void deleteItems(String subdomain, String userId, String opportunityId, String jobId, String concatenatedIds, String modelName, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.deleteItems(subdomain, userId, opportunityId, concatenatedIds, jobId, modelName, responseListener);
    }

    public void addNote(RawNotesRequest rawNotesRequest, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.addNote(rawNotesRequest, responseListener);
    }

    public void getSubmittedNotes(String subdomain, String userId, String opportunityId, String jobId, final ResponseListener<BaseResponseModel<List<NotesPojo>>> responseListener) {
        mWebServiceManager.getSubmittedNotes(subdomain, userId, opportunityId, jobId, responseListener);
    }

    public void submitCouponCode(String subdomain, String userId, String opportunityId, String couponCode, final ResponseListener<BaseResponseModel<CouponDetailsModel>> responseListener) {
        mWebServiceManager.submitCouponCode(subdomain, userId, opportunityId, couponCode, responseListener);
    }

    public void getBolChargesList(String subdomain, String userId, String opportunityId, String model, String jobId, final ResponseListener<BaseResponseModel<CommonChargesResponseModel>> responseListener) {
        mWebServiceManager.getBolChargesList(subdomain, userId, opportunityId, model, jobId, responseListener);
    }

    public void saveBOLCharges(RawChargesSaveRequestModel<CommonChargesRequestModel> rawChargesSaveRequestModel, final ResponseListener<BaseResponseModel<String>> responseListener) {
        mWebServiceManager.saveBOLCharges(rawChargesSaveRequestModel, responseListener);
    }

    public void deleteBolChargesItems(String subdomain, String userId, String opportunityId, String jobId, String concatinatedIds, String model, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.deleteBolChargesItems(subdomain, userId, opportunityId, jobId, concatinatedIds, model, responseListener);
    }

    public void submitBOLDetails(RawBodyBOLSubmitRequestModel<BillOfLadingRequestModel> rawBodyBOLSubmitRequestModel, final ResponseListener<BaseResponseModel<String>> responseListener) {
        mWebServiceManager.submitBOLDetails(rawBodyBOLSubmitRequestModel, responseListener);
    }


    public void getBOLJobConfirmation(String subDomain, String user_id, String opportunityId, String job_id, long totalTime, double totalHour, final ResponseListener<BaseResponseModel<BolDetailsPojo>> responseListener) {
        mWebServiceManager.getBOLJobConfirmation(subDomain, user_id, opportunityId, job_id, totalTime, totalHour, responseListener);
    }

    public void saveBolTipsOrDiscount(String subdomain, String userId, String opportunityId, String bolChargesId, String discountType, String amount, String jobId, String calculatedAmount, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.saveBolTipsOrDiscount(subdomain, userId, opportunityId, bolChargesId, discountType, amount, jobId, calculatedAmount, responseListener);
    }

    public void submitRating(String subdomain, String userId, String opportunityId, String bolChargesId, String rating, String message, String jobId, final ResponseListener<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> responseListener) {
        mWebServiceManager.submitRating(subdomain, userId, opportunityId, bolChargesId, rating, message, jobId, responseListener);
    }

    public void submitReviewDiscount(String subdomain, String userId, String opportunityId, String bolChargesId, String amount, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.submitReviewDiscount(subdomain, userId, opportunityId, bolChargesId, amount, jobId, responseListener);
    }

    public void submitBOLSignature(RawBodyBOLSignatureSubmitRequestModel submitRequestModel, File file, final ResponseListener<BaseResponseModel<String>> responseListener) {
        mWebServiceManager.submitBOLSignature(submitRequestModel, file, responseListener);
    }

    public void getPaymentMethods(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<List<PaymentMethodsModel>>> responseListener) {
        mWebServiceManager.getPaymentMethods(subdomain, userId, opportunityId, responseListener);
    }

    public void getSpreedlySetup(String subdomain, String userId, String jobId, String opportunityId, final ResponseListener<BaseResponseModel<SpreedlyInfoModel>> responseListener) {
        mWebServiceManager.getSpreedlySetup(subdomain, userId, jobId, opportunityId, responseListener);
    }

    public void submitPayment(SpreedlyPaymentTokenSubmitRequestModel submitRequestModel, File paymentSignature, File additionalInfo, final ResponseListener<BaseResponseModel<String>> responseListener) {
        mWebServiceManager.submitPayment(submitRequestModel, paymentSignature, additionalInfo, responseListener);
    }

    public void getTipDiscountList(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<ArrayList<TipsModel>>> responseListener) {
        mWebServiceManager.getTipDiscountList(subdomain, userId, opportunityId, responseListener);
    }

    public void getNotificationList(String subDomain, String user_id, final ResponseListener<BaseResponseModel<NotificationListResponse>> responseListener) {
        mWebServiceManager.getNotificationList(subDomain, user_id, responseListener);
    }

    public void notificationUpdate(String subDomain, String r_id, String user_id, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.notificationUpdate(subDomain, r_id, user_id, responseListener);
    }


    public void logout(String subdomain, String userId, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.logout(subdomain, userId, responseListener);
    }

    public void setBolStarted(String subdomain, String userId, String opportunityId, String bolFinalChargeId, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.setBolStarted(subdomain, userId, opportunityId, bolFinalChargeId, jobId, responseListener);
    }


    public void setCardConvenienceFee(String subdomain, String userId, String opportunityId, String bolChargesId, String discountType, String amount, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.setCardConvenienceFee(subdomain, userId, opportunityId, bolChargesId, discountType, amount, jobId, responseListener);
    }

    public void getETA(String originLocation, String destinationLocation, final ResponseListener<ETAResponseModel> responseListener) {
        mWebServiceManager.getETA(originLocation, destinationLocation, responseListener);
    }

    public void getAddressList(String subdomain, String userId, String opportunityId, final ResponseListener<BaseResponseModel<AddressListResponseModel>> responseListener) {
        mWebServiceManager.getAddressList(subdomain, userId, opportunityId, responseListener);
    }

    public void sendETA(String subdomain, String userId, String opportunityId, String phoneNumber, String etaMessage, String destinationAddress, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.sendETA(subdomain, userId, opportunityId, phoneNumber, etaMessage, destinationAddress, responseListener);
    }

    public void checkIfUpdateNeeded(String subdomain, final ResponseListener<BaseResponseModel<ForceUpdateResponseModel>> responseListener) {
        mWebServiceManager.checkIfUpdateNeeded(subdomain, responseListener);
    }

    public void sendCurrentLocation(RawBodyLocationUpdateRequestModel locationUpdateRequestModel, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.sendCurrentLocation(locationUpdateRequestModel, responseListener);
    }

    public void getClockActivityList(String subDomain, String jobId, String opportunityId, ResponseListener<BaseResponseModel<ActivityTypeListResponseModel>> responseListener) {
        mWebServiceManager.getClockActivityList(subDomain, jobId, opportunityId, responseListener);
    }

    public void getRatePerHour(String subDomain, String jobId, String men, String equipment, String moveDate, ResponseListener<BaseResponseModel<RatePerHourModel>> responseListener) {
        mWebServiceManager.getRatePerHour(subDomain, jobId, men, equipment, moveDate, responseListener);
    }

    public void getClockHistoryList(String subDomain, String userId, String jobId, String opportunityId, ResponseListener<BaseResponseModel<ClockHistoryResponse>> responseListener) {
        mWebServiceManager.getClockHistoryList(subDomain, userId, jobId, opportunityId, responseListener);
    }

    public void updateClockHistory(String subDomain, String userId, String jobId, String opportunityId, ClockHistoryModel clockHistoryModel, ResponseListener<BaseResponseModel<Objects>> responseModelResponseListener) {
        mWebServiceManager.updateClockHistory(subDomain, userId, jobId, opportunityId, clockHistoryModel, responseModelResponseListener);
    }

    public void getArticleRoomList(String subDomain, String opportunityId, final ResponseListener<BaseResponseModel<ArrayList<ArticleRoomModel>>> responseListener) {
        mWebServiceManager.getArticleRoomList(subDomain, opportunityId, new ResponseListener<BaseResponseModel<ArrayList<ArticleRoomModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ArticleRoomModel>> response, String usedUrlKey) {
//                articleListResponseDetailsModel = response.getData();
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ArticleRoomModel>>> call, Throwable t, String message) {
                responseListener.onFailure(call, t, message);
            }
        });
    }

    public void getChargeRateTypeList(String subDomain, String opportunityId, String moveType, String model, int cId, final ResponseListener<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> responseListener) {
        mWebServiceManager.getChargeRateTypeList(subDomain, opportunityId, moveType, model, cId, new ResponseListener<BaseResponseModel<ArrayList<ChargeRateTypeModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ChargeRateTypeModel>> response, String usedUrlKey) {
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
        mWebServiceManager.getRateValue(subDomain, name, type, opportunityId, unitType, new ResponseListener<BaseResponseModel<ChargesUnitModel>>() {
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

    public void getWorkerDetailsList(String subDomain, String userId, String jobId, String opportunityId, ResponseListener<BaseResponseModel<ArrayList<WorkerDetailsModel>>> responseListener) {
        mWebServiceManager.getWorkerDetailsList(subDomain, userId, jobId, opportunityId, responseListener);
    }

    public void paymentCarryForward(String subdomain, String bolFinalChargeId, int status, String carryForwardAmount, String userId, String jobId, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.carryForwardPayment(subdomain, bolFinalChargeId, status, carryForwardAmount, userId, jobId, responseListener);
    }

    public void sendBOLToCustomer(String subdomain, String jobId, String opportunityId, String email, final ResponseListener<BaseResponseModel> responseListener) {
        mWebServiceManager.sendBOLToCustomer(subdomain,  jobId, opportunityId, email, responseListener);
    }
}
