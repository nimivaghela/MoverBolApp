package com.moverbol.network;

import com.moverbol.model.ActivityTypeListResponseModel;
import com.moverbol.model.CardReaderModel;
import com.moverbol.model.CarryForwardModel;
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
import com.moverbol.model.billOfLading.ClockHistoryResponse;
import com.moverbol.model.billOfLading.CouponDetailsModel;
import com.moverbol.model.billOfLading.RatingDiscountPercentagePojo;
import com.moverbol.model.billOfLading.RawBodyBOLSubmitRequestModel;
import com.moverbol.model.billOfLading.TipsModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.ChargesUnitModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesResponseModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.RawChargesSaveRequestModel;
import com.moverbol.model.billOfLading.payment.PaymentMethodsModel;
import com.moverbol.model.billOfLading.payment.SpreedlyInfoModel;
import com.moverbol.model.confirmationPage.AdditionalChargesPojo;
import com.moverbol.model.confirmationPage.CratingChargesPojo;
import com.moverbol.model.confirmationPage.JobConfirmationDetailsPojo;
import com.moverbol.model.confirmationPage.MoveChargesAutoPricingPojo;
import com.moverbol.model.confirmationPage.MoveChargesManualPricingPojo;
import com.moverbol.model.confirmationPage.PackingChargesPojo;
import com.moverbol.model.confirmationPage.StorageChargesPojo;
import com.moverbol.model.confirmationPage.ValuationChargesPojo;
import com.moverbol.model.confirmationPage.artialeList.ArticleListItemPojo;
import com.moverbol.model.confirmationPage.artialeList.ArticleRoomModel;
import com.moverbol.model.forETA.ETAResponseModel;
import com.moverbol.model.moveProcess.AddressListResponseModel;
import com.moverbol.model.moveProcess.ClockInfoPojo;
import com.moverbol.model.moveProcess.MoveProcessStatusPojo;
import com.moverbol.model.moveProcess.WorkerDetailsModel;
import com.moverbol.model.notes.NotesPojo;
import com.moverbol.model.notes.RawNotesRequest;
import com.moverbol.model.notification.NotificationListResponse;
import com.moverbol.model.photoes.ImagesModel;
import com.moverbol.model.photoes.PhotoModel;
import com.moverbol.model.releaseForm.ReleaseFormRequestModel;
import com.moverbol.model.releaseForm.ReleaseFormResponseModel;
import com.moverbol.model.rentalAgreement.RentalAgreementResponseModel;
import com.moverbol.model.rentalAgreement.RentalAgreementSubmittedDetailsPojo;
import com.moverbol.model.valuationPage.ValuationItemPojo;
import com.moverbol.model.valuationPage.ValuationSubmissionRequestModel;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.BaseResponseModelSecond;
import com.moverbol.network.model.JobPojo;
import com.moverbol.network.model.SignInData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Admin on 17-10-2017.
 */

public interface ApiEndPoints {
    String SIGNIN_URL = "login";
    String LOGOUT_URL = "logout";
    String FORGOT_PASSWORD_URL = "forgetpassword";
    String SET_PASSWORD_URL = "setpassword";
    String GET_NEW_JOB_URL = "newjobs";
    String GET_JOB_DETAILS_URL = "jobdetails";
    String GET_JOB_CONFIRMATION_DETAILS_URL = "confirmation";
    String SET_JOB_STATUS = "jobactivity";
    String GET_METADATA_URL = "getmetadata";
    String DELETE_ITEMS = "delete";
    String ADD_ITEMS = "dispatchadd";
    String GET_MOVE_PROCESS_STATUS = "startmove";
    String START_STOP_CLOCK = "clockactivity";
    String START_STOP_BREAK = "breakactivity";
    String MOVE_INFO = "moveinfo";
    String ADD_UPDATE_ARTICLE = "articlesactivity";
    String SUBMIT_VALUATION_DETAILS = "valuationactivity";
    String SUBMIT_STORAGE_AGREEMENT_DETAILS = "storageactivity";
    String SUBMIT_RELEASE_FORM_DETAILS = "releaseformactivity";
    String UPLOAD_PHOTOES = "photosinsert";
    String GET_SUBMITTED_DETAILS = "dispatchretirve";
    String GET_SUBMITTED_PHOTOES = "photodisplay";
    String SUBMIT_CONFIRMATION_DETAILS = "confirmationupdate";
    String ARTICLE_DELETE = "articledelete";
    String ADD_NOTES = "notesactivity";
    String SUBMIT_COUPON = "couponavailability";
    String SUBMIT_BOL = "bolconfirmation";
    String SAVE_BOL_CHARGES = "bolconfirmationcommonsave";
    String GET_BOL_JOB_CONFIRMATION_DETAILS_URL = "bolconfirmationretrieve";
    String BOL_TIPS_AND_DISCOUNTS = "boldiscountsave";
    String BOL_SIGNATURE = "bolsignature";
    String SUBMIT_SPREEDLY_TOKEN = "bolgatewaytoken";
    String GET_NOTIFICATION_LIST = "notificationlist";
    String BOL_STARTED = "bolstartactivity";
    String ADDRESS_LIST_URL = "addresslisting";
    String SEND_ETA_API = "textservices";
    String VERSION_DETAIL = "version-details";
    String LOCATION_UPDATE = "bol-triptracking";
    String GET_ETA_TEST = "https://maps.googleapis.com/maps/api/distancematrix/json";
    String NOTIFICATION_UPDATE = "notification-update";
    String RATE_PER_HOUR = "rate-perhour";
    String CLOCK_HISTORY_UPDATE = "clock-update";
    String ARTICLE_ROOM_LIST = "articles-room";
    String ARTICLE_ITEM_LIST = "articles-item";
    String CHARGE_RATE_LIST = "get-charges-rate";
    String GET_RATE_VALUE = "get-rate-value";
    String DELETE_ACTIVITY_URL = "delete";
    String UPDATE_DISCOUNT_URL = "boldiscount-update";
    String CATEGORY_LIST_URL = "get-category-list";
    String PAYMENT_CARRY_FORWARD_URL = "paymentcarry-forward";
    String CARRY_FORWARD_LIST = "paymentcarry-forward-list";
    String SEND_BOL_TO_CUSTOMER = "download-boldocument";

//    String GET_ETA_TEST = "https://maps.googleapis.com/maps/api/directions/json";
    //?origin=Disneyland&destination=Universal+Studios+Hollywood&key=AIzaSyAQM4yQcUOlz8ITEZuW1xqTduaJH8OYdPM


    @FormUrlEncoded
    @POST(SIGNIN_URL)
    Call<BaseResponseModel<SignInData>> signIn(
            @Field("subdomain") String subdomain,
            @Field("username") String username,
            @Field("password") String password,
            @Field("firebase_id") String firebaseId,
            @Field("device_id") String deviceId
    );

    @FormUrlEncoded
    @POST(FORGOT_PASSWORD_URL)
    Call<BaseResponseModel> forgotpassword(
            @Field("subdomain") String subdomain,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST(SET_PASSWORD_URL)
    Call<BaseResponseModel> setPassword(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(GET_NEW_JOB_URL)
    Call<BaseResponseModel<List<JobPojo>>> getNewJobs(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id
    );


    /*@Headers("X-DEVICE-ID : 'some'")
    @GET(GET_ETA_TEST)
    Call<BaseResponseModel<List<JobPojo>>> getNewJobs(
            *//*@Header("X-DEVICE-ID") String aa,
            @Header("X-USER-ID") String dd*//*
    );*/

    @FormUrlEncoded
    @POST(GET_JOB_DETAILS_URL)
    Call<BaseResponseModel<JobDetailPojo>> getJobDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String job_id
    );

    @FormUrlEncoded
    @POST(GET_JOB_CONFIRMATION_DETAILS_URL)
    Call<BaseResponseModel<JobConfirmationDetailsPojo>> getJobConfirmation(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String job_id
    );

    @FormUrlEncoded
    @POST(SET_JOB_STATUS)
    Call<BaseResponseModel> setJobStattus(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String job_id,
            @Field("status") String status,
            @Field("comments") String reasonToReject,
            @Field("total_charges") String totalCharges
    );

    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModel<List<CrewMetadata>>> getCrewMetadata(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model,
            @Field("job_id") String jobId
    );

    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModel<List<TruckMetadata>>> getTruckMetadata(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> getMaterialMetadata(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(GET_SUBMITTED_DETAILS)
    Call<BaseResponseModel<ManPowerPojo>> getWorkerById(
            @Field("subdomain") String subDomain,
            @Field("model") String model,
            @Field("opportunity_id") String opportunityId,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("id") String id
    );


    @FormUrlEncoded
    @POST(GET_SUBMITTED_DETAILS)
    Call<BaseResponseModel<TruckPojo>> getTruckById(
            @Field("subdomain") String subDomain,
            @Field("model") String model,
            @Field("opportunity_id") String opportunityId,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST(GET_SUBMITTED_DETAILS)
    Call<BaseResponseModel<MaterialPojo>> getMaterialById(
            @Field("subdomain") String subDomain,
            @Field("model") String model,
            @Field("opportunity_id") String opportunityId,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST(DELETE_ITEMS)
    Call<BaseResponseModel> deleteItems(
            @Field("subdomain") String subdomain,
            @Field("model") String model,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String jobId,
            @Field("user_id") String user_id,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST(ADD_ITEMS)
    Call<BaseResponseModel> addCrewItems(
            @Field("subdomain") String subdomain,
            @Field("model") String model,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String jobId,
            @Field("user_id") String userId,
            @Field("worker_type") String workerType,
            @Field("responsible_user") String responsibleUser,
            @Field("temp_worker") String tempWorker,
            @Field("remarks") String remarks,
            @Field("manpower_id") String manpowerId
    );


    /*@FormUrlEncoded
    @POST(ADD_ITEMS)
    Call<BaseResponseModel> addCrewItems(
            @Field("subdomain") String subdomain,
            @Field("model") String model,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String jobId,
            @Field("user_id") String userId,
            @Field("worker_type") String workerType,
            @Field("r_bol_responsible_user") String responsibleUser,
            @Field("r_bol_temp_worker") String tempWorker,
            @Field("r_bol_remarks") String remarks,
            @Field("manpower_id") String manpowerId
    );*/


    @FormUrlEncoded
    @POST(ADD_ITEMS)
    Call<BaseResponseModel> addTruckItems(
            @Field("subdomain") String subdomain,
            @Field("model") String model,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String jobId,
            @Field("vehicle_type") String vehicleType,
            @Field("vehicle_no") String vehicleNumber,
            @Field("temp_vehicle") String tempVehicle,
            @Field("remarks") String remarks,
            @Field("vehicle_id") String vehicleId,
            @Field("user_id") String userId
    );


    @FormUrlEncoded
    @POST(ADD_ITEMS)
    Call<BaseResponseModel> addMaterialItems(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model,
            @Field("job_id") String jobId,
            @Field("material") String material,
            @Field("quantity") String quantity,
            @Field("material_unit") String materialUnit,
            @Field("rate") String materialRate,
            @Field("total") String totalMaterialRate,
            @Field("remarks") String remarks,
            @Field("material_id") String materialId,
            @Field("c_id") int categoryId
    );

    @FormUrlEncoded
    @POST(GET_MOVE_PROCESS_STATUS)
    Call<BaseResponseModel<MoveProcessStatusPojo>> getMoveProcessStatus(
            @Field("subdomain") String subdomain,
            @Field("job_id") String job_id,
            @Field("opportunity_id") String opportunityId,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(START_STOP_CLOCK)
    Call<BaseResponseModel<ClockInfoPojo>> startStopClock(
            @Field("subdomain") String subdomain,
            @Field("job_id") String job_id,
            @Field("opportunity_id") String opportunityId,
            @Field("user_id") String user_id,
            @Field("latitude") String currentLatitude,
            @Field("longitude") String currentLongitude,
            @Field("activity") String currentProcessId,
            @Field("status") String startStopStatusId,
            @Field("timestamp") String currentTime,
            @Field("timestamp_app") String currentTimeMills,
            @Field("crew") String crew,
            @Field("equipment") String equipment,
            @Field("is_billable") String isBillable,
            @Field("is_itemize") String is_itemize,
            @Field("c_id") String cId,
            @Field("rate_hour") String rateHours,
            @Field("double_drive") String doubleDrive,
            @Field("workers") String worker
    );

    /**
     * @param jobClockId The response of startClock. That is the id of a clock which gets
     *                   started on the call of startClock. You have to break that clock
     *                   hence use the id of that clock.
     */
    @FormUrlEncoded
    @POST(START_STOP_BREAK)
    Call<BaseResponseModel<String>> startStopBreak(
            @Field("subdomain") String subdomain,
            @Field("job_id") String job_id,
            @Field("opportunity_id") String opportunityId,
            @Field("user_id") String user_id,
            @Field("job_clock_id") String jobClockId,
            @Field("status") String startStopStatusId,
            @Field("timestamp") String currentTime,
            @Field("timestamp_app") String currentTimeMills
    );


    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<ArrayList<StorageChargesPojo>>> getStorageChargesDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<ArrayList<PackingChargesPojo>>> getPackingChargesDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<ArrayList<AdditionalChargesPojo>>> getAdditionalChargesDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<ArrayList<CratingChargesPojo>>> getCratingChargesDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<ArrayList<ValuationChargesPojo>>> getValuationChargesDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );


    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<ArrayList<MoveChargesAutoPricingPojo>>> getMoveChargesAutoPricingDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model,
            @Field("price_type") String priceType
    );

    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<ArrayList<MoveChargesManualPricingPojo>>> getMoveChargesManualPricingDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model,
            @Field("price_type") String priceType
    );


    @FormUrlEncoded
    @POST(ARTICLE_ITEM_LIST)
    Call<BaseResponseModel<ArrayList<ArticleListItemPojo>>> getArticleItemList(
            @Field("subdomain") String subDomain,
            @Field("inventory_id") String inventoryId,
            @Field("room_id") String roomId
    );


    @Multipart
    @POST(ADD_UPDATE_ARTICLE)
    Call<BaseResponseModel> addOrUpdateArticleList(
            @Part("subdomain") RequestBody subdomain,
            @Part("opportunity_id") RequestBody opportunity_id,
            @Part("user_id") RequestBody user_id,
            @Part("job_id") RequestBody job_id,
            @Part("total_volume") RequestBody total_volume,
            @Part("total_weight") RequestBody total_weight,
            @Part("item") RequestBody item,
            @Part("custom") RequestBody custom,
            @Part MultipartBody.Part file

    );

    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModelSecond<List<ValuationItemPojo>, String>> getValuationMetadata(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @Multipart
    @POST(SUBMIT_VALUATION_DETAILS)
    Call<BaseResponseModel> submitValuationDetails(
            @Part("subdomain") RequestBody subdomain,
            @Part("user_id") RequestBody user_id,
            @Part("job_id") RequestBody job_id,
            @Part("opportunity_id") RequestBody opportunity_id,
            @Part("valuation_settings_id") RequestBody valuation_settings_id,
            @Part("valuation_rate") RequestBody valuation_rate,
            @Part("valuation_unit") RequestBody valuation_unit,
            @Part("declared_amount") RequestBody declared_amount,
            @Part("r_id") RequestBody r_id,
            @Part("created_by") RequestBody created_by,
            @Part("valuation_charges_calculated[amount]") RequestBody amount,
            @Part("valuation_charges_calculated[quantity]") RequestBody quantity,
            @Part("valuation_charges_calculated[description]") RequestBody description,
            @Part("valuation_charges_calculated[rate]") RequestBody rate,
            @Part("valuation_charges_calculated[unit]") RequestBody unit,
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST(GET_SUBMITTED_DETAILS)
    Call<BaseResponseModel<ValuationSubmissionRequestModel>> getValuationSubmittedDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<RentalAgreementResponseModel>> getRentalAgreementMetadata(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @Multipart
    @POST(SUBMIT_STORAGE_AGREEMENT_DETAILS)
    Call<BaseResponseModel> submitStorageAgreementDetails(
            @Part("subdomain") RequestBody subdomain,
            @Part("user_id") RequestBody userId,
            @Part("opportunity_id") RequestBody opportunityId,
            @Part("job_id") RequestBody jobId,
            @Part("description") RequestBody description,
            @Part("city") RequestBody city,
            @Part("r_id") RequestBody rId,
            @Part("created_by") RequestBody createdBy,
            @Part("occupant_name") RequestBody occupantName,
            @Part("date_of_lease") RequestBody dateOfLease,
            @Part("state") RequestBody state,
            @Part("phone") RequestBody phone,
            @Part("address") RequestBody address,
            @Part("zip_code") RequestBody zipCode,
            @Part("est_volume") RequestBody estVolume,
            @Part("est_weight") RequestBody estWeight,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST(UPLOAD_PHOTOES)
    Call<BaseResponseModel<ImagesModel>> uploadPhoto(
            @Part("subdomain") RequestBody subdomain,
            @Part("job_id") RequestBody jobId,
            @Part("opportunity_id") RequestBody opportunityId,
            @Part("user_id") RequestBody userId,
            @Part("photo_description") RequestBody photoDescription,
            @Part("photo_title") RequestBody photoTitle,
            @Part("created_by") RequestBody createdBy,
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST(GET_SUBMITTED_DETAILS)
    Call<BaseResponseModel<RentalAgreementSubmittedDetailsPojo>> getRentalAgreementSubmittedDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModel<ReleaseFormResponseModel>> getReleaseFormMetadata(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );


    @Multipart
    @POST(SUBMIT_RELEASE_FORM_DETAILS)
    Call<BaseResponseModel> submitReleaseFormDetails(
            @PartMap Map<String, RequestBody> map,
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<List<ReleaseFormRequestModel>>> getReleaseFormSubmittedDetails(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(GET_SUBMITTED_PHOTOES)
    Call<BaseResponseModel<List<PhotoModel>>> getSubmittedPhotosUrls(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId
    );


    @Multipart
    @POST(SUBMIT_CONFIRMATION_DETAILS)
    Call<BaseResponseModel> submitConfirmationDetails(
            @Part("subdomain") RequestBody subDomain,
            @Part("job_id") RequestBody jobId,
            @Part("opportunity_id") RequestBody opportunityId,
            @Part("agree_reschedule") RequestBody agreeReschedule,
            @Part("agree_policy") RequestBody agree_policy,
            @Part("created_by") RequestBody created_by,
            @Part("r_id") RequestBody r_id,
            @Part MultipartBody.Part file

    );

    @FormUrlEncoded
    @POST(ARTICLE_DELETE)
    Call<BaseResponseModel> deleteArticle(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("item_id") String itemId,
            @Field("r_id") String id
    );

    @POST(ADD_NOTES)
    Call<BaseResponseModel> addNote(
            @Body RawNotesRequest rawNotesRequest
    );

    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<List<NotesPojo>>> getSubmittedNotes(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(SUBMIT_COUPON)
    Call<BaseResponseModel<CouponDetailsModel>> submitCouponCode(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("coupon_code") String coupon_code
    );

    @POST(SAVE_BOL_CHARGES)
    Call<BaseResponseModel<String>> saveBOLCharges(
            @Body RawChargesSaveRequestModel<CommonChargesRequestModel> rawNotesRequest
    );


    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<CommonChargesResponseModel>> getBolChargesList(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(GET_BOL_JOB_CONFIRMATION_DETAILS_URL)
    Call<BaseResponseModel<BolDetailsPojo>> getBOLJobConfirmation(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String job_id,
            @Field("total_time") String totalTimeForMoveChargesCalculation,
            @Field("total_hours") String totalTimeForMoveChargesCalculationInHours
//            @Body RawBodyBOLRequestModel rawBodyBOLRequestModel
    );

    @POST(SUBMIT_BOL)
    Call<BaseResponseModel<String>> submitBOLDetails(
            @Body RawBodyBOLSubmitRequestModel<BillOfLadingRequestModel> rawBodyBOLSubmitRequestModel
    );


    @FormUrlEncoded
    @POST(BOL_TIPS_AND_DISCOUNTS)
    Call<BaseResponseModel> saveBolTipsOrDiscount(
            @Field("subdomain") String subdomain,
            @Field("model") String model,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("bol_charge_id") String bolChargeId,
            @Field("discount_type") String discountType,
            @Field("amount") String amount,
            @Field("calculated_amount") String calculatedAmount
    );


    @FormUrlEncoded
    @POST(BOL_TIPS_AND_DISCOUNTS)
    Call<BaseResponseModel> setCardConvenienceFee(
            @Field("subdomain") String subdomain,
            @Field("model") String model,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("bol_charge_id") String bolChargeId,
            @Field("discount_type") String discountType,
            @Field("discount_value") String amount
    );

    @FormUrlEncoded
    @POST(BOL_TIPS_AND_DISCOUNTS)
    Call<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> submitRating(
            @Field("subdomain") String subdomain,
            @Field("model") String model,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("bol_charge_id") String bolChargeId,
            @Field("ratings") String ratings,
            @Field("message") String message
    );


    @FormUrlEncoded
    @POST(BOL_TIPS_AND_DISCOUNTS)
    Call<BaseResponseModel> submitReviewDiscount(
            @Field("subdomain") String subdomain,
            @Field("model") String model,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("bol_charge_id") String bolChargeId,
            @Field("amount") String ratings
    );

    @Multipart
    @POST(BOL_SIGNATURE)
    Call<BaseResponseModel<String>> submitBOLSignature(
            @Part("subdomain") RequestBody subDomain,
            @Part("job_id") RequestBody jobId,
            @Part("user_id") RequestBody userId,
            @Part("opportunity_id") RequestBody opportunityId,
            @Part("bol_finalcharge_id") RequestBody bolFinalChargeId,
            @Part MultipartBody.Part file

    );


    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModel<List<PaymentMethodsModel>>> getPaymentMethods(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );


    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModel<SpreedlyInfoModel>> getSpreedlySetup(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );


    @Multipart
    @POST(SUBMIT_SPREEDLY_TOKEN)
    Call<BaseResponseModel<String>> submitPayment(
            @Part("subdomain") RequestBody subDomain,
            @Part("job_id") RequestBody jobId,
            @Part("user_id") RequestBody userId,
            @Part("opportunity_id") RequestBody opportunityId,
            @Part("credit_card_fee_value") RequestBody creditCardFeeValue,
            @Part("amount") RequestBody amount,
            @Part("deposit_amount") RequestBody depositAmount,
            @Part("actual_amount") RequestBody actualAmount,
            @Part("payment_type") RequestBody paymentType,
            @Part("original_amount") RequestBody originalAmount,
            @Part("credit_card_fee_type") RequestBody creditCardFeeType,
            @Part("payment_method") RequestBody paymentMethod,
            @Part("bol_finalcharge_id") RequestBody bolFinalChargeId,
            @Part("payment_token") RequestBody paymentToken,
            @Part MultipartBody.Part paymentSignature,
            @Part MultipartBody.Part additionalImage
    );

    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModel<ArrayList<TipsModel>>> getTipDiscountList(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(GET_NOTIFICATION_LIST)
    Call<BaseResponseModel<NotificationListResponse>> getNotificationList(
            @Field("subdomain") String subdomain,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(LOGOUT_URL)
    Call<BaseResponseModel> logout(
            @Field("subdomain") String subdomain,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST(BOL_STARTED)
    Call<BaseResponseModel> setBolStarted(
            @Field("subdomain") String subdomain,
            @Field("opportunity_id") String opportunityId,
            @Field("bol_finalcharge_id") String bolFinalChargeId,
            @Field("job_id") String jobId,
            @Field("user_id") String userId
    );


    @FormUrlEncoded
    @POST(SEND_ETA_API)
    Call<BaseResponseModel> sendEta(
            @Field("subdomain") String subdomain,
            @Field("opportunity_id") String opportunityId,
            @Field("user_id") String userId,
            @Field("phone") String phoneNumber,
            @Field("eta_msg") String etaMessage,
            @Field("destination_address") String destinationAddress
    );


    @GET(GET_ETA_TEST)
    Call<ETAResponseModel> getETA(
            @Query("origins") String originLocation,
            @Query("destinations") String destinationLocation,
            @Query("key") String key
    );


    @FormUrlEncoded
    @POST(ADDRESS_LIST_URL)
    Call<BaseResponseModel<AddressListResponseModel>> getAddressList(
            @Field("subdomain") String subdomain,
            @Field("opportunity_id") String opportunityId,
            @Field("user_id") String userId
    );


    @FormUrlEncoded
    @POST(VERSION_DETAIL)
    Call<BaseResponseModel<ForceUpdateResponseModel>> checkIfUpdateNeeded(
            @Field("subdomain") String subdomain,
            @Field("version_number") String versionNumber,
            @Field("version_name") String versionName,
            @Field("device_type") String deviceTypeId
    );


    @POST(LOCATION_UPDATE)
    Call<BaseResponseModel> sendCurrentLocation(
            @Body RawBodyLocationUpdateRequestModel locationUpdateRequestModel
    );


    @FormUrlEncoded
    @POST(NOTIFICATION_UPDATE)
    Call<BaseResponseModel> notificationUpdate(
            @Field("subdomain") String subdomain,
            @Field("r_id") String rId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModel<ActivityTypeListResponseModel>> getClockActivityList(
            @Field("subdomain") String subDomain,
            @Field("job_id") String job_id,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(RATE_PER_HOUR)
    Call<BaseResponseModel<RatePerHourModel>> getRatePerHour(
            @Field("subdomain") String subDomain,
            @Field("job_id") String job_id,
            @Field("men") String men,
            @Field("equipment") String equipment,
            @Field("move_date") String moveDate
    );

    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<ClockHistoryResponse>> getClockHistoryList(
            @Field("subdomain") String subDomain,
            @Field("user_id") String userId,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model

    );

    @FormUrlEncoded
    @POST(CLOCK_HISTORY_UPDATE)
    Call<BaseResponseModel<Objects>> updateClockHistory(
            @Field("subdomain") String subDomain,
            @Field("user_id") String userId,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("activity") String activity,
            @Field("crew") String crew,
            @Field("equipment") String equipment,
            @Field("start_id") String startId,
            @Field("start_timestamp") String startTimestamp,
            @Field("start_timestamp_app") String startTimestampAPP,
            @Field("stop_id") String stopId,
            @Field("stop_timestamp") String stopTimestamp,
            @Field("stop_timestamp_app") String stopTimestampApp,
            @Field("is_billable") String isBillable,
            @Field("is_itemize") String isItemize,
            @Field("rate_hour") String rate_hour,
            @Field("event_type") String eventType,
            @Field("total_break_time") String totalBreakTime,
            @Field("workers") String workers
    );


    @FormUrlEncoded
    @POST(ARTICLE_ROOM_LIST)
    Call<BaseResponseModel<ArrayList<ArticleRoomModel>>> getArticleRoomList(
            @Field("subdomain") String subDomain,
            @Field("opportunity_id") String opportunityId
    );

    @FormUrlEncoded
    @POST(CHARGE_RATE_LIST)
    Call<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> getChargeRateTypeList(
            @Field("subdomain") String subDomain,
            @Field("opportunity_id") String opportunityId,
            @Field("move_type") String moveType,
            @Field("model") String model,
            @Field("c_id") int cId
    );

    @FormUrlEncoded
    @POST(GET_RATE_VALUE)
    Call<BaseResponseModel<ChargesUnitModel>> getRateValue(
            @Field("subdomain") String subDomain,
            @Field("name") String name,
            @Field("type") String type,
            @Field("opportunity_id") String opportunityId,
            @Field("unit_type") String unitType
    );

    @FormUrlEncoded
    @POST(DELETE_ACTIVITY_URL)
    Call<BaseResponseModel<Objects>> deleteActivity(
            @Field("subdomain") String subDomain,
            @Field("model") String model,
            @Field("id") String id,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String jobId,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST(UPDATE_DISCOUNT_URL)
    Call<BaseResponseModel<Objects>> discountUpdate(
            @Field("subdomain") String subDomain,
            @Field("type") String type,
            @Field("discount_value") String discountValue,
            @Field("discount_type") String discount_type,
            @Field("bol_final_charge_id") String bolFinalChargeId
    );


    @FormUrlEncoded
    @POST(CATEGORY_LIST_URL)
    Call<BaseResponseModel<ArrayList<CategoryModel>>> getCategoryTypeList(
            @Field("subdomain") String subDomain,
            @Field("opportunity_id") String opportunityId,
            @Field("move_type") String moveType,
            @Field("model") String model
    );


    @FormUrlEncoded
    @POST(MOVE_INFO)
    Call<BaseResponseModel<ArrayList<WorkerDetailsModel>>> getWorkerDetails(
            @Field("subdomain") String subDomain,
            @Field("user_id") String userId,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("model") String model
    );

    @FormUrlEncoded
    @POST(PAYMENT_CARRY_FORWARD_URL)
    Call<BaseResponseModel> carryForwardPayment(
            @Field("subdomain") String subDomain,
            @Field("bol_final_charge_id") String bolFinalChargeId,
            @Field("status") int Status,
            @Field("total_amount") String carryForwardAmount,
            @Field("user_id") String userId,
            @Field("job_id") String jobId);


    @FormUrlEncoded
    @POST(CARRY_FORWARD_LIST)
    Call<BaseResponseModel<ArrayList<CarryForwardModel>>> carryForwardList(
            @Field("subdomain") String subDomain,
            @Field("user_id") String userId,
            @Field("opportunity_id") String opportunityId
    );

    @FormUrlEncoded
    @POST(GET_METADATA_URL)
    Call<BaseResponseModel<CardReaderModel>> cardReaderDetails(
            @Field("subdomain") String subDomain,
            @Field("model") String model,
            @Field("opportunity_id") String opportunityId,
            @Field("job_id") String jobId
    );

    @FormUrlEncoded
    @POST(SEND_BOL_TO_CUSTOMER)
    Call<BaseResponseModel> sendBOLToCustomer(
            @Field("subdomain") String subDomain,
            @Field("job_id") String jobId,
            @Field("opportunity_id") String opportunityId,
            @Field("email_address") String email
    );
}

