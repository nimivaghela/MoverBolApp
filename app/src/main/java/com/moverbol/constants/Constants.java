package com.moverbol.constants;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mitesh on 25-05-2017.
 */

public class Constants {

    public static final long SPLASH_SCREEN_TIMEOUT = 1500;
    public static final String CREW_CHECKBOX = "crew_checkbox";
    public static final String CREW_WITHOUT_CHECKBOX = "crew_without_checkbox";

    public static final String ITEM_SELECTION_AVILABEL = "selection_avilable";

    public static final int HOME = 0;
    public static final int CALENDAR = 1;
    public static final int JOBS_IN_PROGRESS = 2;
    /*public static final int MY_JOBS = 3;
    public static final int MY_PROFILE = 4;*/
    public static final int NOTIFICATIONS = 3;
    public static final int SETTINGS = 4;
    public static final int LOGOUT = 5;
    public static final String INPUT_DATE_FORMAT_COMMENTS = "yyyy-MM-dd' 'HH:mm:ss";
    //    public static final String OUTPUT_DATE_FORMAT_COMMENTS = "MMM dd, yyyy";
    public static final String OUTPUT_DATE_FORMAT_COMMENTS = "MM/dd/yyyy";

    public static final String OUTPUT_DATE_FORMAT_TIME = "HH:mm";
    public static final String OUTPUT_DATE_FORMAT_TIME_TEST = "HH:mm:ss";
    public static final String OUTPUT_DATE_FORMAT_DAY_TIME = "'EEE' 'HH:mm'";
    public static final String OUTPUT_DATE_FORMAT_DATE_TIME = "MM/dd/yyyy' 'HH:mm";
    public static final String OUTPUT_DATE_FORMAT_DATE_TIME_AM_PM = "MM/dd/yyyy' 'hh:mm a";
    public static final String OUTPUT_DATE_FORMAT_TIMINGS_DETAILS = "MM/dd/yyyy' 'HH:mm:ss";
    public static final String DD_MMM_YYYY_HH_MM_SS = "dd MMM, yyyy HH:mm:ss";
    public static final String DD_MMM_YYYY = "dd MMM, yyyy";
    public static final String NOTE_DATE_FORMAT = "dd/MM/yyyy hh:mm aa";
    public static final String DD_MM_YYYY_HH_MM = "dd-MM-yyyy HH:mm";
    public static final String DD_MM_YYYY_HH_MM_AA = "dd MMM, yyyy hh:mm aa";
    public static final String HH_MM_AA = "hh:mm aa";


    //    public static final String OUTPUT_DATE_FORMAT_JOBS = "EEE',' dd MMM',' yy";
    //    public static final String OUTPUT_DATE_FORMAT_JOBS = "EEE',' MM/dd/yyyy";
    public static final String INPUT_DATE_FORMAT_JOBS = "yyyy-MM-dd";
    public static final String OUTPUT_DATE_FORMAT_JOBS = "MM/dd/yyyy";
    public static final String EXTRA_MATERIAL_ID_KEY = "material_id";
    public static final String KEY_LIST_POSITION = "list_position";
    public static final String FORM_IS_EMPTY = "Form is empty";
    public static final String VALUACTION_LABEL_TO_IGNORE = "A";
    public static final String EXTRA_CALLED_FROM_HOME = "called_from_home";
    public static final String EXTRA_ARTICLE_LIST = "article_list";
    public static final String EXTRA_NOTIFICATION_INTENT = "notification_intent";
    public static final String MY_BROADCAST_ACTION = "my_broadcast_action";
    public static final String MY_BROADCAST_ACTION_FOR_BOL_APPROVAL = "my_broadcast_action_for_bol_approval";

    /* Declare the @IntDef for these constants*/
    @IntDef({HOME, CALENDAR, JOBS_IN_PROGRESS, /*MY_JOBS, MY_PROFILE,*/ NOTIFICATIONS, SETTINGS, LOGOUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NavigationItems {
    }

    public static final int CALENDAR_VIEW = 0;
    public static final int WEEK_VIEW = 1;

    public static final String STOCK_IMAGE = "stock_image";
    public static final String EXTRA_TITLE = "extra_title";

   /* public static final String SUCCESS = "200";
    public static final String FAILURE = "0";*/


    public static final String BASE_LOG_TAG = "@Mover_";

    public static final String SUCCESS = "Success";
    public static final String UNAUTHENTICATED = "Unauthenticated";
    public static final String FAIL = "Failed";
    public static final String SERVER_ERROR = "";


    public static final String EXTRA_JOB_ID_KEY = "job_id";
    public static final String EXTRA_ITEM_ID_KEY = "item_id";
    public static final String EXTRA_MOVE_HAS_STORAGE_KEY = "move_has_storage";
    public static final String EXTRA_IS_MOVE_INTERNATIONAL_KEY = "is_move_international";
    public static final String EXTRA_OPPORTUNITY_ID_KEY = "opportunity_id";
    public static final String EXTRA_MOVE_CHARGES_PRICE_TYPE_KEY = "move_charges_type";
    public static final String EXTRA_LIST_ITEM_POSITION = "list_item_position";

    public static final String JOB_STATUS_NEW = "0";
    public static final String JOB_STATUS_ACCEPTED = "1";
    public static final String JOB_STATUS_INPROGRESS = "2";
    public static final String JOB_STATUS_COMPLETED = "3";
    public static final String JOB_STATUS_REJECTED = "4";


    public static final String KEY_ADAPTER_POSITION = "adapter_position";

    public static final String RESPONSE_FAILURE_MESSAGE = "Failed to get data. Server maybe down";

    public static final String GET_METADATA_MODEL_WORKER = "worker";
    public static final String GET_METADATA_MODEL_TRUCK = "truck";
    public static final String GET_METADATA_MODEL_MATERIAL = "material";
    public static final String GET_METADATA_MODEL_VALUATION = "valuation";
    public static final String GET_METADATA_MODEL_RELEASE_FORM = "releaseform";
    public static final String GET_METADATA_MODEL_CLOCK_ACTIVITY = "clockactivity";


    public static final String GET_SUBMITTED_DETAILS_MODEL_VALUATION = "valuation";
    public static final String GET_SUBMITTED_DETAILS_MODEL_STORAGE = "storage";
    public static final String GET_SUBMITTED_DETAILS_MODEL_RELEASE_FORM = "leaseform";

    public static final String DELETE_ITEM_MODEL_PHOTOES = "photos";
    public static final String DELETE_ITEM_MODEL_WORKER = "worker";
    public static final String DELETE_ITEM_MODEL_TRUCK = "truck";
    public static final String DELETE_ITEM_MODEL_MATERIAL = "material";

    public static final String KEY_MOVE_DATE = "move_date";
    public static final String KEY_CLOCK_HISTORY_MODEL = "clockHistoryModel";
    public static final String KEY_CLOCK_ACTIVITY_MODEL = "clockActivityModel";
    public static final String KEY_WORKER_LIST = "workerList";
    public static final int RQ_CLOCK_ACTIVITY = 111;
    public static final int RQ_CLOCK_UPDATE_HISTORY = 222;


    public static final String ADDRESS_TYPE_PICK_UP = "pickup";
    public static final String ADDRESS_TYPE_DELIVERY = "delivery";
    public static final String KEY_HOUR = "hours";
    public static final String KEY_MINUTE = "minute";
    public static final String KEY_YEAR = "year";
    public static final String KEY_MONTH = "month";
    public static final String KEY_DAY = "day";


    public interface ReleaseFormMetadataLabes {
        String LABEL_HOSTING = "Hosting Services";
        String LABEL_FORCING = "Forcing an item in or out of the premises";
        String LABEL_WRAPPED = "Not Wrapped Item";
        String LABEL_FURNITURE = "Press Wood Furniture";
    }

    static public String TRUE = "1";
    static public String FALSE = "0";

    static public String DISCOUNT = "discount";
    static public String DISCOUNT_TYPE = "discount_type";
    static public String SALES_TAX = "sales_tax";
    static public String MOVE_TYPE_ID = "move_type_id";
    static public String PAYMENT_RESULT = "paymentResult";
    static public String PAYMENT_ERROR = "paymentError";

    public interface MoveInfoModelTypes {
        String MOVE_CHARGES_MODEL_TEXT = "movecharge";
        String STORAGE_MODEL_TEXT = "storageone";
        String STORAGE_RECURRING_MODEL_TEXT = "storagerec";
        String CRATING_MODEL_TEXT = "crating";
        String PACKING_MODEL_TEXT = "packing";
        String ADDITIONAL_MODEL_TEXT = "additional";
        String VALUATION_MODEL_TEXT = "valuation";
        String ARTICLES_MODEL_TEXT = "articles";
        String RENTAL_AGREEMENT_TEXT = "rentalagreement";
        String NOTES_TEXT = "notes";
        String CLOCK_HISTORY = "clockhistory";
        String CLOCK_ACTIVITY = "clockactivity";
        String WORKER_DETAILS = "workerdetails";
    }

    public static final String START_PROCESS_STATUS = "1";
    public static final String STOP_PROCESS_STATUS = "2";


    public interface UserDesignationTypes {
        String DESIGNATION_TYPE_DRIVER = "1";
        String DESIGNATION_TYPE_CONTRACTOR = "2";
    }

    public interface BOLStringConstants {
        String EXTRA_MOVE_RATE = "move_rate";
        String EXTRA_MOVE_TYPE_ID = "move_type_id";
        String EXTRA_CHARGES_CHANGED = "charges_changed";
        String MOVE_CHARGE_LIST_AUTO = "move_charge_list_auto";
        String MOVE_CHARGE_LIST_MANUAL = "move_charge_list_manual";
        String PACKING_CHARGES_LIST = "packing_charge_list";
        String ADDITIONAL_CHARGES_LIST = "additional_charge_list";
        String STORAGE_CHARGES_LIST = "storage_charge_list";
        String VALUATION_CHARGE_LIST = "valuation_charge_list";
        String VALUATION_CHARGES_ITEM = "valuation_charges_item";
        String MOVE_CHARGE_LIST = "move_charges_list";
        String ACTUAL_HOURS = "actual_hours";
        String ACTUAL_TRAVEL_TIME = "actual_travel_time";
        String MOVE_CHARGE_CALCULATED = "move_charge_calculated";
        String EXTRA_BOL_FINAL_CHARGE_ID = "bol_final_charge_id";
        String SUBMIT_RATING_MODEL = "ratings";
        String SUBMIT_TIP_MODEL = "tips_discount";
        String SET_CARD_CONVINIENCE_FEE_MODEL = "credit_card";
        String SUBMIT_REVIEW_DISCOUNT_MODEL = "review_discount";
        String CARD_READER_SETUP_MODEL = "cardreader_setup";
    }

    public interface DoubleFormats {
        String FORMAT_FOR_MILLI_TO_HOURS = ".00";
        String FORMAT_FOR_DIGITS = "#0.00";
        String FORMAT_FOR_QUANTITY = "0";

        String STRING_FORMAT_FOR_MILLI_TO_HOURS = "%.02f";
        String STRING_FORMAT_FOR_DIGITS = "%.02f";
        String STRING_FORMAT_FOR_QUANTITY = "%.00f";
    }

    public interface CouponTypes {
        String COUPON_TYPE_AMOUNT = "1";
        CharSequence COUPON_TYPE_PERCENTAGE = "2";
    }


    public interface MoveTypeIds {
        String MOVE_TYPE_INTERNATIONAL = "1";
        String MOVE_TYPE_LOCAL = "2";
        String MOVE_TYPE_INTER_STATE = "3";
        String MOVE_TYPE_INTRA_STATE = "4";
        String MOVE_TYPE_COMMERCIAL = "5";
    }


    public interface BolStatus {
        int NOT_SENT_YET = 0;
        int PENDING_APPROVAL = 1;
        int APPROVED = 2;
        int REJECTED = 3;
        int COMPLETED = 4;
    }

    public interface NumValueTypes {
        int NUM_VALUE_TYPE_AMOUNT = 1;
        int NUM_VALUE_TYPE_PERCENTAGE = 2;
    }

    public interface BolDiscountFlags {
        int DISCOUNT_FLAG_CURENCY_AMOUNT = 1;
        int DISCOUNT_FLAG_PERCENTAGE = 2;
    }

    public interface MoveChargesPriceTypes {
        String MOVE_CHARGES_AUTO_PRICING = "auto_pricing";
        String MOVE_CHARGES_MANUAL_PRICING = "manual_pricing";
        String MOVE_CHARGES_SPOT_PRICING = "spot_pricing";
        int MOVE_CHARGES_AUTO_PRICING_INDEX = 1;
        int MOVE_CHARGES_MANUAL_PRICING_INDEX = 2;
        int MOVE_CHARGES_SPOT_PRICING_INDEX = 5;
    }


    public interface JobClosedIndexes {
        String JOB_CLOSED = "1";
    }

    public interface BOLStatusForArticleList {
        String STATUS_NORMAL = "1";
        String STATUS_DELETE = "2";
    }

    public interface ArticleListItemTypes {
        //Means the type is yet to be set as the data comes from api and we set it in the app
        int NOT_ASSIGNED_TYPE = 0;
        int NORMAL_TYPE = 1;
        /**
         * The difference from normal type is that it's name can also be changed and it does not
         * have fields "item_id" and "actual_qty"
         */
        int CUSTOM_TYPE = 2;
    }


    public interface ActivityFlags {
        String JOB_HAS_MULTIPLE_ACTIVITIES = "0";
        String JOB_HAS_SINGLE_ACTIVITY = "1";
    }

    public interface BooleanFlags {
        String FALSE_VALUE = "0";
        String TRUE_VALUE = "1";
    }

    public interface SomeUnits {

        String CBM = "CBM";
        String CFT = "CFT";
        String KGS = "KGS";
        String LBS = "LBS";
        String CWT = "CWT";
        String MILES = "miles";
        String UNIT_RATE = "Unit Rate";
        String PACK_RATE = "Pack Rate";
        String UNPACK_RATE = "Unpack Rate";
        String PERCENTAGE_AFTER_DISCOUNT = "Percentage  After Discount";
        String PERCENTAGE_BEFORE_DISCOUNT = "Percentage  Before Discount";
    }

    public interface ClockProcessConstants {
        String CLOCK_IN = "Clock In";
        String CLOCK_OUT = "Clock Out";
        String BREAK_IN = "Break In";
        String BREAK_OUT = "Break Out";
    }

    public interface PaymentMethodIds {
        String CAS_METHOD_ID = "0";
        String CHEQUE_METHOD_ID = "0";
        String CREDIT_CARD_METHOD_ID = "1";
        String DEBIT_CARD_METHOD_ID = "1";
        String CHASHIERS_CHEQUE_METHOD_ID = "0";
        String OTHERS_METHOD_ID = "4";
        String CARD_READER_METHOD_ID = "2";
        String PAYMENT_CARRY_FORWARD_METHOD_ID = "3";
    }

    public interface StringFormats {
        String phoneNumberFormat = "###'('#######";
    }

    public interface DeviceTypeIds {
        String ANDROID_ID = "1";
        String IOS_ID = "2";
    }

    public interface UpdateRequiredCheckFlags {
        int UPDATE_FORCE_NEEDED = 1;
        int UPDATE_SOFT_NEDDED = 3;
        int UPDATE_NOT_NEEDED = 2;
        int UPDATE_CHECK_ERROR = -1;
    }

    public interface BOLChargesModels {
        String MOVE_CHARGE_SAVE_MODEL = "move_charge";
        String PACKING_CHARGE_SAVE_MODEL = "packing_charge";
        String ADDITIONAL_CHARGE_SAVE_MODEL = "additional_charge";
        String CRATING_CHARGE_SAVE_MODEL = "crating_charge";
        String VALUATION_CHARGE_SAVE_MODEL = "valuation_charge";
        String STORAGE_CHARGE_SAVE_MODEL = "storage_charge";
        String STORAGE_RECURRING_CHARGE_SAVE_MODEL = "storage_charge";
    }

    public interface PaymentMethodIndexs {
        String PAYMENT_METHOD_CASH = "1";
        String PAYMENT_METHOD_CHEQUE = "2";
        String PAYMENT_METHOD_CREDIT_CARD = "3";
        String PAYMENT_METHOD_DEBIT_CARD = "4";
        String PAYMENT_METHOD_CASHIERS_CHECK = "5";
        String PAYMENT_METHOD_OTHER = "9";
    }

    public interface PaymentTypeIndexs {
        String PAYMENT_TYPE_SPREEDLY = "1";
        String PAYMENT_TYPE_CASH_CHECK_OTHERS = "2";
    }

    public interface NotificationTypeIndexes {
        int TYPE_BOL_CONFIRMATION = 1;
        int TYPE_BOL_NEW_JOB = 2;
        int TYPE_NOTES = 3;
        int TYPE_JOB_DELETE = 4;
        int TYPE_ESTIMATE_AND_BOL_CHANGED = 5;
    }

    public interface RemoteConfigKeys {
        String KEY_UPDATE_REQUIRED = "force_update_required";
        String KEY_CURRENT_VERSION_NAME_IN_STORE = "force_update_current_version_name_in_store";
        String KEY_UPDATE_URL = "force_update_store_url";
    }

    static public String CLOCK = "Clock";

    // Type Id
    public static String MOVE_CHARGE_ID = "1";
    public static String PACKING_MATERIAL_CHARGE_ID = "2";
    public static String CRATING_CHARGE_ID = "3";
    public static String ADDITION_CHARGE_ID = "4";
    public static String STORAGE_CHARGE_ID = "5";
    public static String STORAGE_RECURRING_CHARGE_ID = "6";

    public static final String CRATING_PHOTOS = "Crating Info Photos";
    public static final String INVENTORY_PHOTOS = "Inventory Photos";
    public static final String BOL_APP_PHOTOS = "BOL app Photos";


    public interface PhotoDetailsIntentExtras {
        String EXTRA_PHOTO_ID = "photo_id";
        String EXTRA_PHOTO_TITLE = "photo_title";
        String EXTRA_PHOTO_URL = "photo_url";
        String EXTRA_PHOTO_DESCRIPTION = "photo_description";
        String EXTRA_PHOTO_MODEL = "photoModel";
        String EXTRA_IMAGE_MODEL = "imageModel";
    }

}
