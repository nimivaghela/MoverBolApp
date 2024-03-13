package com.moverbol.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.moverbol.BuildConfig;
import com.moverbol.network.model.SignInData;

/**
 * Created by Admin on 06-11-2017.
 */

public class MoversPreferences {

    private static final String SUB_DOMAIN = "Subdomain";
    private static final String CLOCK_IN_TIME_MILLS = "clock_in_time_mills";
    private static final String START_BREAK_TIME_MILLS = "start_break_time_mills";
    private static final String STOP_BREAK_TIME_MILLS = "stop_break_time_mills";
    private static final String BREAK_TIME_MILLS = "break_time_mills";
    private static final String BREAK_TOTAL_TIME_MILLS = "break_total_time_mills";
    private static final String CLOCK_PAUSE_TIME_MILLS = "clock_pause_time_mills";
    private static final String CURRENCY_SYMBOL = "currency_symbol";
    private static final String USER_DESIGNATION_TYPE = "user_designation_type";
    private static final String ACTUAL_HOURS = "actual_hours";
    private static final String ACTUAL_TRAVEL_TIME = "actual_travel_time";
    private static final String INCREMENT_MIN_VALUE = "increment_min_value";
    private static final String DEFAULT_MIN_HOURS = "default_min_hours";
    private static final String CURRENT_JOB_OPPORTUNITY_ID = "opportunity_id";
    private static final String CURRENT_JOB_MOVE_TYPE = "move_type";
    private static final String APP_UNIQUE_ID = "app_unique_id";
    private static final String CLOCK_IN = "clock_in";
    private static final String CLOCK_OUT = "clock_out";
    private static final String CLOCK_IN_OCCURRENCE_NUM = "CLOCK_IN_OCCURRENCE_NUM";
    private static final String BREAK_OCCURRENCE_NUM = "BREAK_OCCURRENCE_NUM";
    private static final String JOB_ACTIVITY_NAME = "job_activity_name";
    private static final String JOB_ACTIVITY_ID = "job_activity_id";
    private static final String BOL_STATUS = "bol_status";
    private static final String CLOCK_IS_ON = "clock_is_on";
    private static final String BREAK_IS_ON = "break_is_on";
    private static final String NOTIFICATION_COUNT = "notification_count";
    private static final String JOB_HAS_SINGLE_ACTIVITY = "job_has_single_activity";
    private static final String JOB_HAS_SYNCED_CLOCK_AND_BREAK = "job_has_synced_clock_and_break";
    private static final String USER_PROFILE_IMAGE_URL = "user_profile_image_url";
    private static final String CURRENT_JOB_ID = "current_job_id";
    private static final String NOTIFICATION_ID = "notification_id";
    private static final String FIREBASE_TOKEN_ID = "firebase_token_id";
    private static final String TIP_SUBMITTED = "tip_submitted";
    private static final String REVIEWS_SUBMITTED = "reviews_submitted";
    private static final String BOL_SIGNATURE_SUBMITTED = "bol_signature_submitted";
    private static final String LAST_DELETED_JOB_ID = "last_deleted_job_id";
    private static final String KEY_IS_UPDATE_REQUIRED = "is_update_required";
    private static final String KEY_CURRENT_VERSION_NAME_IN_STORE = "current_version_name_in_store";
    private static final String KEY_UPDATE_URL = "update_url";
    private static final String KEY_DEVICE_ID = "device_id_key";
    private static final String IS_JOB_DELETED = "is_job_deleted";
    private static final String BOL_STARTED_KEY = "is_bol_started";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    public static String LOGIN = "isLogin";
    public static String USER_ID = "user_id";
    public static String ACCESSTOKEN = "accesstoken";
    public static String USER_DETAILS = "user_details";
    public static String X_API_KEY = "x_api_key";
    public static String PROFILE_PICTURE = "profile_picture";
    private static final String PRE_NAME = "clubSpot_Prefrence";
    private static final String KEY_DETAILS_JOB_ID = "detailsJobId";
    private static final String KEY_USER_DENY_LOCATION_PERMISSION = "denyLocationPermission";
    private static MoversPreferences moversPreferences;
    private final SharedPreferences sharedPrefrence;


    private MoversPreferences(Context mContext) {
        sharedPrefrence = mContext.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
    }

    public static MoversPreferences getInstance(Context mContext) {
        if (moversPreferences == null) {
            moversPreferences = new MoversPreferences(mContext);
        }
        return moversPreferences;
    }

    public void loggedOut() {
        sharedPrefrence.edit().clear().apply();
    }

    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getValue(String key) {
        return sharedPrefrence.getString(key, null);
    }

    public void removeValue(String key) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.remove(key);
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPrefrence.getBoolean(LOGIN, false);
    }

    public void setLogin(boolean isLogin) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(LOGIN, isLogin);
        editor.apply();
    }

    public String getUserId() {
        return sharedPrefrence.getString(USER_ID, null);
    }

    public void setUserId(String id) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(USER_ID, id);
        editor.apply();
    }

    public void setAccessToken(String token) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(ACCESSTOKEN, token);
        editor.apply();
    }

    public String getAccesToken() {
        return sharedPrefrence.getString(ACCESSTOKEN, "");
    }


    public void setXapiKey(String key) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(X_API_KEY, key);
        editor.apply();
    }

    public String getXapikey(Context context) {
        return sharedPrefrence.getString(X_API_KEY, "");
    }

    public SignInData getUserDetails() {
        return new Gson().fromJson(getSharedPrefrence().getString(USER_DETAILS, ""), SignInData.class);
    }

    public void setUserDetails(SignInData signInData) {
//        signInData = signInPojo;
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(USER_DETAILS, new Gson().toJson(signInData));
        editor.apply();
    }

    private SharedPreferences getSharedPrefrence() {
        return sharedPrefrence;
    }

    public String getUserName() {
        return sharedPrefrence.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPrefrence.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(USER_EMAIL, userEmail);
        editor.apply();
    }

    public String getSubdomain() {
        return sharedPrefrence.getString(SUB_DOMAIN, "");
    }

    public void setSubdomain(String subDomain) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(SUB_DOMAIN, subDomain);
        editor.apply();
    }

    public void setClockIsOn(String jobId, boolean value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(CLOCK_IS_ON + jobId, value);
        editor.apply();
    }

    public boolean isClockIsOn(String jobId) {
        return sharedPrefrence.getBoolean(CLOCK_IS_ON + jobId, false);
    }

    public void setBreakIsOn(String jobId, boolean value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(BREAK_IS_ON + jobId, value);
        editor.apply();
    }

    public boolean isBreakIsOn(String jobId) {
        return sharedPrefrence.getBoolean(BREAK_IS_ON + jobId, false);
    }

    public void setClockInTime(String jobId, long value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putLong(CLOCK_IN_TIME_MILLS + jobId, value);
        editor.apply();
    }

    public long getClockInTime(String jobId) {
        return sharedPrefrence.getLong(CLOCK_IN_TIME_MILLS + jobId, 0);
    }

    public void setStartBreakTime(String jobId, long value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putLong(START_BREAK_TIME_MILLS + jobId, value);
        editor.apply();
    }

    public long getStartBreakTime(String jobId) {
        return sharedPrefrence.getLong(START_BREAK_TIME_MILLS + jobId, 0);
    }

    public void setCurrentActivityTotalBreakTime(String jobId, long value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putLong(BREAK_TOTAL_TIME_MILLS + jobId, value);
        editor.apply();
    }

    public long getCurrentActivityTotalBreakTime(String jobId) {
        return sharedPrefrence.getLong(BREAK_TOTAL_TIME_MILLS + jobId, 0);
    }

    public String getCurrencySymbol() {
        return sharedPrefrence.getString(CURRENCY_SYMBOL, "");
    }

    public void setCurrencySymbol(String value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(CURRENCY_SYMBOL, value);
        editor.apply();
    }

    public String getUserDesignationType() {
        return sharedPrefrence.getString(USER_DESIGNATION_TYPE, "");
    }

    public void setUserDesignationType(String value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(USER_DESIGNATION_TYPE, value);
        editor.apply();
    }

    public String getProfileImageUrl() {
        return sharedPrefrence.getString(USER_PROFILE_IMAGE_URL, "");
    }

    public void setProfileImageUrl(String profileImageUrl) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(USER_PROFILE_IMAGE_URL, profileImageUrl);
        editor.apply();
    }

    public void setJobActivityClockInOccurrenceNum(String jobId, int occurrenceNum) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putInt(jobId + CLOCK_IN_OCCURRENCE_NUM, occurrenceNum);
        editor.apply();
    }

    public int getJobActivityClockInOccurrenceNum(String jobId) {
        return sharedPrefrence.getInt(jobId + CLOCK_IN_OCCURRENCE_NUM, 0);
    }

    public void setJobActivityBreakOccurrenceNum(String jobId, int clockOccurrenceNum, int breakOccurrenceNum) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putInt(jobId + clockOccurrenceNum + BREAK_OCCURRENCE_NUM, breakOccurrenceNum);
        editor.apply();
    }

    public int getJobActivityBreakOccurrenceNum(String jobId, int clockOccurrenceNum) {
        return sharedPrefrence.getInt(jobId + clockOccurrenceNum + BREAK_OCCURRENCE_NUM, 0);
    }

    public void setCurrentActivityId(String jobId, String activityId) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(jobId + JOB_ACTIVITY_ID, activityId);
        editor.apply();
    }

    public String getCurrentActivityId(String jobId) {
        return sharedPrefrence.getString(jobId + JOB_ACTIVITY_ID, "");
    }

    public void setJobActivityName(String jobId, String jobActivity) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
//        int occurrenceNum = getJobActivityClockInOccurrenceNum(jobId);
        editor.putString(jobId + JOB_ACTIVITY_NAME, jobActivity);
        editor.apply();
    }

    public String getJobActivityName(String jobId) {
        return sharedPrefrence.getString(jobId + JOB_ACTIVITY_NAME, "");
    }

    public void setJobActivityClockIn(String jobId, long clockInTime, int occurrenceNum) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
//        int occurrenceNum = getJobActivityClockInOccurrenceNum(jobId) + 1;
        setJobActivityClockInOccurrenceNum(jobId, occurrenceNum);

        editor.putLong(jobId + occurrenceNum + CLOCK_IN, clockInTime);
        editor.apply();
    }


    public long getJobActivityClockIn(String jobId, int occurrenceNum) {
        return sharedPrefrence.getLong(jobId + occurrenceNum + CLOCK_IN, 0);
    }

    public void setJobActivityClockOut(String jobId, long clockOutTime, int occurrenceNum) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
//        int occurrenceNum = getJobActivityClockInOccurrenceNum(jobId);
        editor.putLong(jobId + occurrenceNum + CLOCK_OUT, clockOutTime);
        editor.apply();
    }

    public long getJobActivityClockOut(String jobId, int occurrenceNum) {
        return sharedPrefrence.getLong(jobId + occurrenceNum + CLOCK_OUT, 0);
    }

    public void setJobActivityBreakIn(String jobId, long breakInTime, int clockOccurrenceNum, int breakOccurrenceNum) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putLong(jobId + clockOccurrenceNum + breakOccurrenceNum + START_BREAK_TIME_MILLS, breakInTime);
        editor.apply();
    }

    public long getJobActivityBreakIn(String jobId, int clockOccurrenceNum, int breakOccurrenceNum) {
        return sharedPrefrence.getLong(jobId + clockOccurrenceNum + breakOccurrenceNum + START_BREAK_TIME_MILLS, 0);
    }

    public void setJobActivityBreakOut(String jobId, long breakOutTime, int clockOccurrenceNum, int breakOccurrenceNum) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putLong(jobId + clockOccurrenceNum + breakOccurrenceNum + STOP_BREAK_TIME_MILLS, breakOutTime);
        editor.apply();
    }

    public long getJobActivityBreakOut(String jobId, int clockOccurrenceNum, int breakOccurrenceNum) {
        return sharedPrefrence.getLong(jobId + clockOccurrenceNum + breakOccurrenceNum + STOP_BREAK_TIME_MILLS, 0);
    }

    public void setJobActivityBreakTotalTime(String jobId, long breakTotalTime, int occurrenceNum) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
//        int occurrenceNum = getJobActivityClockInOccurrenceNum(jobId);
        editor.putLong(jobId + occurrenceNum + BREAK_TOTAL_TIME_MILLS, breakTotalTime);
        editor.apply();
    }

    public long getJobActivityBreakTotalTime(String jobId, int occurrenceNum) {
        return sharedPrefrence.getLong(jobId + occurrenceNum + BREAK_TOTAL_TIME_MILLS, 0);
    }

    public void setActualHours(String jobId, long value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putLong(ACTUAL_HOURS + jobId, value);
        editor.apply();
    }

    public long getActualHours(String jobId) {
        return sharedPrefrence.getLong(ACTUAL_HOURS + jobId, 0);
    }

    public void setActualTravelTime(String jobId, long value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putLong(ACTUAL_TRAVEL_TIME + jobId, value);
        editor.apply();
    }

    public void setIncrementMinValue(String jobId, long value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putLong(INCREMENT_MIN_VALUE + jobId, value);
        editor.apply();
    }

    public long getIncrementMinValue(String jobId) {
        return sharedPrefrence.getLong(INCREMENT_MIN_VALUE + jobId, 1);
    }

    public void setDefaultMinHours(String jobId, String value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(DEFAULT_MIN_HOURS + jobId, value);
        editor.apply();
    }

    public String getDefaultMinHours(String jobId) {
        return sharedPrefrence.getString(DEFAULT_MIN_HOURS + jobId, "0");
    }

    public long getActualTravelTime(String jobId) {
        return sharedPrefrence.getLong(ACTUAL_TRAVEL_TIME + jobId, 0);
    }

    public void setIsJobDeleted(String jobId, boolean isJobDeleted) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(IS_JOB_DELETED + jobId, isJobDeleted);
        editor.apply();
    }

    public boolean isJobDeleted(String jobId) {
        return sharedPrefrence.getBoolean(IS_JOB_DELETED + jobId, false);
    }

    public String getCurrentJobId() {
        return sharedPrefrence.getString(CURRENT_JOB_ID, "");
    }

    public void setCurrentJobId(String value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(CURRENT_JOB_ID, value);
        editor.apply();
    }

    public String getOpportunityId() {
        return sharedPrefrence.getString(CURRENT_JOB_OPPORTUNITY_ID, "");
    }

    public void setOpportunityId(String value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(CURRENT_JOB_OPPORTUNITY_ID, value);
        editor.apply();
    }

    public String getCurrentJobMoveTypeId() {
        return sharedPrefrence.getString(CURRENT_JOB_MOVE_TYPE, "");
    }

    public void setCurrentJobMoveTypeId(String value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(CURRENT_JOB_MOVE_TYPE, value);
        editor.apply();
    }

    public String getUniqueAppId() {
        return sharedPrefrence.getString(APP_UNIQUE_ID, null);
    }

    public void setUniqueAppId(String uniqueID) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(APP_UNIQUE_ID, uniqueID);
        editor.apply();
    }

    public void setCurrentJobBolStatus(String mJobId, int statusCode) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putInt(mJobId + BOL_STATUS, statusCode);
        editor.apply();
    }

    public int getCurrentJobBolStatus(String mJobId) {
        return sharedPrefrence.getInt(mJobId + BOL_STATUS, 0);
    }

    public int getNotificationCount() {
        return sharedPrefrence.getInt(NOTIFICATION_COUNT, 0);
    }

    public void setNotificationCount(int count) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putInt(NOTIFICATION_COUNT, count);
        editor.apply();
    }

    public void setIsJobHasSingleActivity(String jobId, boolean value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(jobId + JOB_HAS_SINGLE_ACTIVITY, value);
        editor.apply();
    }

    public boolean isJobHasSingleActivity(String jobId) {
        return sharedPrefrence.getBoolean(jobId + JOB_HAS_SINGLE_ACTIVITY, false);
    }

    public void setHasSyncedClockAndBreak(String jobId, boolean value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(jobId + JOB_HAS_SYNCED_CLOCK_AND_BREAK, value);
        editor.apply();
    }

    public boolean hasSyncedClockAndBreak(String jobId) {
        return sharedPrefrence.getBoolean(jobId + JOB_HAS_SYNCED_CLOCK_AND_BREAK, false);
    }


    /**
     * To get defferent Id of notification as same id notifications replaces the older one
     *
     * @param notificationTypeIndex
     * @param notificationId
     */
    public void setNotificationId(int notificationTypeIndex, int notificationId) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putInt(notificationTypeIndex + NOTIFICATION_ID, notificationId);
        editor.apply();
    }

    public int getNotificationId(int notificationTypeIndex) {
        return sharedPrefrence.getInt(notificationTypeIndex + NOTIFICATION_ID, 0);
    }

    public String getFirebaseToken() {
        return sharedPrefrence.getString(FIREBASE_TOKEN_ID, "");
    }

    public void setFirebaseToken(String firebaseToken) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(FIREBASE_TOKEN_ID, firebaseToken);
        editor.apply();
    }

    public void setTipHasBeenSubmittedOrSkipped(String mJobId, boolean hasTipBeenSubmittedOrSkipped) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(TIP_SUBMITTED + mJobId, hasTipBeenSubmittedOrSkipped);
        editor.apply();
    }

    public boolean getTipHasBeenSubmittedOrSkipped(String mJobId) {
        return sharedPrefrence.getBoolean(TIP_SUBMITTED + mJobId, false);
    }

    public void setReviewHasBeenSubmittedOrSkipped(String mJobId, boolean hasReviewBeenSubmittedOrSkipped) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(REVIEWS_SUBMITTED + mJobId, hasReviewBeenSubmittedOrSkipped);
        editor.apply();
    }

    public boolean getReviewHasBeenSubmittedOrSkipped(String mJobId) {
        return sharedPrefrence.getBoolean(REVIEWS_SUBMITTED + mJobId, false);
    }

    public void setBolSignatureHasBeenSubmitted(String mJobId, boolean hasBolSignatureBeenSubmitted) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(BOL_SIGNATURE_SUBMITTED + mJobId, hasBolSignatureBeenSubmitted);
        editor.apply();
    }

    public boolean getBolSignatureHasBeenSubmitted(String mJobId) {
        return sharedPrefrence.getBoolean(BOL_SIGNATURE_SUBMITTED + mJobId, false);
    }

    public void setBolStarted(boolean bolStarted, String jobId) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(BOL_STARTED_KEY + jobId, bolStarted);
        editor.apply();
    }

    public boolean getBolStarted(String jobId) {
        return sharedPrefrence.getBoolean(BOL_STARTED_KEY + jobId, false);
    }

    public boolean getIsUpdateRequired() {
        return sharedPrefrence.getBoolean(KEY_IS_UPDATE_REQUIRED, true);
    }

    public void setIsUpdateRequired(boolean isUpdateRequired) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(KEY_IS_UPDATE_REQUIRED, isUpdateRequired);
        editor.apply();
    }

    public String getCurrentVersionNameInStore() {
//        return sharedPrefrence.getString(KEY_CURRENT_VERSION_NAME_IN_STORE, BuildConfig.VERSION_NAME);
        return sharedPrefrence.getString(KEY_CURRENT_VERSION_NAME_IN_STORE, "3.0.0");
    }

    public void setCurrentVersionNameInStore(String currentVersionNameInStore) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(KEY_CURRENT_VERSION_NAME_IN_STORE, currentVersionNameInStore);
        editor.apply();
    }

    public String getUpdateURL() {
        return sharedPrefrence.getString(KEY_UPDATE_URL, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
    }

    public void setUpdateURL(String updateURL) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(KEY_UPDATE_URL, updateURL);
        editor.apply();
    }

    public String getJobDetailsId() {
        return sharedPrefrence.getString(KEY_DETAILS_JOB_ID, "");
    }

    public void setJobDetailsId(String value) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putString(KEY_DETAILS_JOB_ID, value);
        editor.apply();
    }


    public void setDenyLocationPermission(boolean denyLocationPermission) {
        SharedPreferences.Editor editor = sharedPrefrence.edit();
        editor.putBoolean(KEY_USER_DENY_LOCATION_PERMISSION, denyLocationPermission);
        editor.apply();
    }

    public boolean getUserDenyLocationPermission() {
        return sharedPrefrence.getBoolean(KEY_USER_DENY_LOCATION_PERMISSION, false);
    }

}
