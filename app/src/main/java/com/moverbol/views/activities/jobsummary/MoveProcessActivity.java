package com.moverbol.views.activities.jobsummary;

import static com.moverbol.constants.Constants.KEY_CLOCK_ACTIVITY_MODEL;
import static com.moverbol.constants.Constants.KEY_MOVE_DATE;
import static com.moverbol.constants.Constants.KEY_WORKER_LIST;
import static com.moverbol.constants.Constants.RQ_CLOCK_ACTIVITY;
import static com.moverbol.constants.Constants.START_PROCESS_STATUS;
import static com.moverbol.constants.Constants.STOP_PROCESS_STATUS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.moverbol.BuildConfig;
import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.BolSignatureDialog;
import com.moverbol.custom.dialogs.CalculateETADialog;
import com.moverbol.custom.dialogs.RatingDialog;
import com.moverbol.custom.dialogs.TermsAndPoliciesDialog;
import com.moverbol.database.ClockHistoryDao;
import com.moverbol.database.MoverDatabase;
import com.moverbol.databinding.MoveProcessBinding;
import com.moverbol.model.ClockActivityModel;
import com.moverbol.model.ClockEntityModel;
import com.moverbol.model.billOfLading.RatingDiscountPercentagePojo;
import com.moverbol.model.billOfLading.RawBodyBOLSignatureSubmitRequestModel;
import com.moverbol.model.forETA.ETAResponseModel;
import com.moverbol.model.moveProcess.AddressListResponseModel;
import com.moverbol.model.moveProcess.BreakInfoPojo;
import com.moverbol.model.moveProcess.ClockInfoPojo;
import com.moverbol.model.moveProcess.MoveProcessStatusPojo;
import com.moverbol.model.moveProcess.WorkerModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.OnSingleClickListener;
import com.moverbol.util.PermissionHelper;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.MoveProcessViewModel;
import com.moverbol.views.activities.JobSummaryActivity;
import com.moverbol.views.activities.WalkThroughActivity;
import com.moverbol.views.activities.moveprocess.ClockBreakHistoryActivity;
import com.moverbol.views.activities.moveprocess.InventoryActivity;
import com.moverbol.views.activities.moveprocess.NotesActivity;
import com.moverbol.views.activities.moveprocess.bill_of_lading.BillOfLadingActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Admin on 22-09-2017.
 */

public class MoveProcessActivity extends BaseAppCompactActivity implements CalculateETADialog.ETACalculationClickListener, BolSignatureDialog.OnSignatureSubmittedListener, RatingDialog.RatingActionListener {

    public static final String EXTRA_FAILED_TO_GET_DATA_MESSAGE = "extra_failed_to_get_data";
    public static final int RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR = 100;
    public static final int RESULT_FAILED_TO_GET_DATA = 100;
    private static final String SHOULD_SHOW_PAYMENT_SUCCESSFULL_DIALOG = "should_show_payment_successfull_dialog";
    private static final String SELECTED_ADDRESS_FOR_ETA = "selected_address_for_eta";
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final String CLOCK_TIMER = "clock_timer";
    private static final String BREAK_TIMER = "break_timer";
    private static final long F_INTERVAL = (120 * 1000);
    private static final long FASTEST_INTERVAL = F_INTERVAL / 2;
    AlertDialog sendForAprovalDialog;
    private MoveProcessBinding moveProcessBinding;
    private MoveProcessViewModel viewModel;
    private String mJobId;
    //    private ClockActionListener clockActionListener;
    private PermissionHelper permissionHelper;
    private FusedLocationProviderClient mFusedLocationClient;
    private MoveProcessStatusPojo mMoveProcessStatusPojo;
    private LocationRequest mLocationRequest;
    private MoversPreferences mMoversPreferences;
    private boolean mChronClockIsRunning;
    private boolean mChronBreakIsRunning;
    private boolean mIsClockRequired;
    private MoverDatabase mDB;
    private AlertDialog mAlertDialog;
    private ClockHistoryDao mClockHistoryDao;

    private List<String> mAddressList;
    private String mSelectedDestinationAddressForETA = "";

    private boolean mShouldShowPaymentSuccessFullDialog = false;
    private ClockActivityModel clockActivityModel = new ClockActivityModel();
    private ArrayList<WorkerModel> workerList = new ArrayList<>(0);
    boolean isStartLocation = true;

//    private CalculateETADialog mCalculateETADialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialisation();
        setIntentData();

        setActionListeners();
        setViewModelObserver();

        if (viewModel.moveProcessStatusPojoLive.getValue() == null) {
            callGetMoveProcessStatus();
        }

//        moveProcessBinding.setIsWaitingOnApproval(MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL);

        moveProcessBinding.setIsBolStarted(MoversPreferences.getInstance(this).getBolStarted(mJobId));

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SHOULD_SHOW_PAYMENT_SUCCESSFULL_DIALOG)) {
                mShouldShowPaymentSuccessFullDialog = savedInstanceState.getBoolean(SHOULD_SHOW_PAYMENT_SUCCESSFULL_DIALOG);
                if (mShouldShowPaymentSuccessFullDialog) {
                    showPaymentSuccessFullDialog();
                }
            }

            if (savedInstanceState.containsKey(SELECTED_ADDRESS_FOR_ETA)) {
                mSelectedDestinationAddressForETA = savedInstanceState.getString(SELECTED_ADDRESS_FOR_ETA);
            }
        }

    }

    @Override
    protected void onResume() {
//        moveProcessBinding.setIsWaitingOnApproval(MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL);

        moveProcessBinding.setIsBolStarted(MoversPreferences.getInstance(this).getBolStarted(mJobId));

        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (viewModel.addressListRespinseModelLive.getValue() == null) {
            callGetAddressList(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_move_process, menu);

        MenuItem calculateETAMenuItem = menu.findItem(R.id.calculate_eta);
      /*  if(mMoveProcessStatusPojo!=null &&
                mMoveProcessStatusPojo.getMoveInfoPojo()!=null &&
                mMoveProcessStatusPojo.getMoveInfoPojo().getPhoneNumber()!=null &&
                !mMoveProcessStatusPojo.getMoveInfoPojo().getPhoneNumber().isEmpty()
                ){
            calculateETAMenuItem.setVisible(true);
        } else {
            calculateETAMenuItem.setVisible(false);
        }*/

        //TODO: Remove to enable ETA.
//        calculateETAMenuItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.calculate_eta:
                showCalculateETADialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("MissingPermission")
    private void initialisation() {
        moveProcessBinding = DataBindingUtil.setContentView(this, R.layout.activity_process);
        setToolbar(moveProcessBinding.toolbar.toolbar, "", R.drawable.ic_arrow_back_white_24dp);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        viewModel = new ViewModelProvider(this).get(MoveProcessViewModel.class);
        mMoversPreferences = MoversPreferences.getInstance(this);

        mAddressList = new ArrayList<>();

        /*clockActionListener = new ClockActionListener() {
            @Override
            public void clockIn(String currentProcessId) {
                if (currentProcessId == null) {
                    currentProcessId = "";
                }
                callStartStopCLock(currentProcessId, START_PROCESS_STATUS);
            }
        };*/


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(F_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mDB = MoverDatabase.getInstance(this);
        mClockHistoryDao = mDB.clockHistoryDao();

//        mCalculateETADialog = new CalculateETADialog();
    }

    private void setIntentData() {
        if (getIntent() != null) {

            if (getIntent().hasExtra(Constants.EXTRA_JOB_ID_KEY)) {
                mJobId = getIntent().getStringExtra(Constants.EXTRA_JOB_ID_KEY);
            } else {
                mJobId = MoversPreferences.getInstance(this).getCurrentJobId();
            }

            /*if (getIntent().hasExtra(Constants.EXTRA_IS_MOVE_INTERNATIONAL_KEY)) {
                moveProcessBinding.setMoveTypeInternational(getIntent().getBooleanExtra(Constants.EXTRA_IS_MOVE_INTERNATIONAL_KEY, false));
            }*/
            if (getIntent().hasExtra(Constants.EXTRA_MOVE_HAS_STORAGE_KEY)) {
                moveProcessBinding.setStorage(getIntent().getBooleanExtra(Constants.EXTRA_MOVE_HAS_STORAGE_KEY, false));
            }
        }
    }

    private void setActionListeners() {

        moveProcessBinding.imgCall.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(moveProcessBinding.txtNumber.getText())) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + moveProcessBinding.txtNumber.getText()));
                startActivity(intent);
                Util.startDialer(v.getContext(), moveProcessBinding.txtNumber.getText().toString().trim());
            }
        });

        moveProcessBinding.txtNumber.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(moveProcessBinding.txtNumber.getText())) {
                    Util.startDialer(v.getContext(), moveProcessBinding.txtNumber.getText().toString().trim());
                }
            }
        });

        moveProcessBinding.btnShowHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockBreakHistoryActivity.start(MoveProcessActivity.this, mJobId, mMoveProcessStatusPojo.getMoveInfoPojo().getStartDate(), mMoveProcessStatusPojo.getMoveInfoPojo().getActivityFlag());
            }
        });

        moveProcessBinding.btnCompleteMove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompleteMoveDialog();
            }
        });

        moveProcessBinding.btnClockIn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                openClockDialog();
            }
        });

        moveProcessBinding.btnStartBreak.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onStartBreakClicked();

            }
        });

    }

    private void setViewModelObserver() {

        Observer mMoveProcessObserver = (Observer<MoveProcessStatusPojo>) moveProcessStatusPojo -> {

            if (moveProcessStatusPojo == null) {
                moveProcessStatusPojo = new MoveProcessStatusPojo();
            }

            mMoveProcessStatusPojo = moveProcessStatusPojo;

            moveProcessBinding.setIsBolNotAllowed(!moveProcessStatusPojo.isBolAllowed() || moveProcessStatusPojo.getBolMoveTypeFlag() == 0);
//                moveProcessBinding.setIsBolAllowed(true);

            setClockAndBreakInfoInDatabase(moveProcessStatusPojo);

            mMoversPreferences.setCurrentJobBolStatus(mJobId, (int) Util.getLongFromString(moveProcessStatusPojo.getBolSentForApprovalStatus()));

            if (moveProcessStatusPojo.getMoveInfoPojo() != null) {
                mMoversPreferences.setIsJobHasSingleActivity(mJobId, moveProcessStatusPojo.getMoveInfoPojo().jobHasSingleActivity());
            }


            if (mMoversPreferences.isClockIsOn(mJobId)) {
                resumeChroneTimer(CLOCK_TIMER);
                moveProcessStatusPojo.getLastClockInfo().setStatus(START_PROCESS_STATUS);
                if (!TextUtils.isEmpty(mMoversPreferences.getCurrentActivityId(mJobId))) {
                    if (!mMoversPreferences.isJobHasSingleActivity(mJobId)) {
                        moveProcessBinding.txtJobActivity.setText(getString(R.string.job_activity));
                        moveProcessBinding.txtBoxDeliver.setText(moveProcessStatusPojo.getLastClockInfo().getActivityName());
                        moveProcessBinding.txtStartTime.setText(R.string.activity_start_time);
                    }
                }
            } else {
                moveProcessStatusPojo.getLastClockInfo().setStatus(STOP_PROCESS_STATUS);
            }

            if (mMoversPreferences.isBreakIsOn(mJobId)) {
                moveProcessStatusPojo.getLastBreakInfo().setBreakStatus(START_PROCESS_STATUS);
                resumeChroneTimer(BREAK_TIMER);
                showClockAsPaused();
            } else {
                moveProcessStatusPojo.getLastBreakInfo().setBreakStatus(STOP_PROCESS_STATUS);
            }

            //Change Page Title
            String pageTitle = getString(R.string.request) + " " + moveProcessStatusPojo.getMoveInfoPojo().getJobSubId();
            getSupportActionBar().setTitle(pageTitle);


            //Set activity start time
            setActivityStartTime();

            moveProcessBinding.setObj(moveProcessStatusPojo);

            MoversPreferences.getInstance(MoveProcessActivity.this).setBolStarted(moveProcessStatusPojo.getBolStarted(), mJobId);
            moveProcessBinding.setIsBolStarted(moveProcessStatusPojo.getBolStarted());
            MoversPreferences.getInstance(MoveProcessActivity.this).setDefaultMinHours(mJobId, moveProcessStatusPojo.getMinHours());
            MoversPreferences.getInstance(MoveProcessActivity.this).setIncrementMinValue(mJobId, moveProcessStatusPojo.getIncrementMinValue());
            mMoversPreferences.setActualHours(mJobId, moveProcessStatusPojo.getActualHourApp());
            mMoversPreferences.setActualTravelTime(mJobId, moveProcessStatusPojo.getActualDriveTimeApp());

          /*  if (calculateETAMenuItem != null){
                if ((moveProcessStatusPojo.getMoveInfoPojo().getPhoneNumber() == null ||
                        moveProcessStatusPojo.getMoveInfoPojo().getPhoneNumber().isEmpty())) {
                    calculateETAMenuItem.setVisible(false);
                } else {
                    calculateETAMenuItem.setVisible(true);
                }
            }*/
        };

        viewModel.moveProcessStatusPojoLive.observe(this, mMoveProcessObserver);

        /*viewModel.etaResponseModelLive.observe(this, new Observer<ETAResponseModel>() {
            @Override
            public void onChanged(@Nullable ETAResponseModel etaResponseModel) {
                if(etaResponseModel!=null && etaResponseModel.isStatusSuccess() && etaResponseModel.getFirstRowEta()!=null){
                    CalculateETADialog.startToSendETA( getSupportFragmentManager(), mAddressList,
                            mSelectedDestinationAddressForETA, etaResponseModel.getFirstRowEta());

                    viewModel.etaResponseModelLive.setValue(null);
                }
            }
        });*/


        viewModel.addressListRespinseModelLive.observe(this, new Observer<AddressListResponseModel>() {
            @Override
            public void onChanged(@Nullable AddressListResponseModel addressListResponseModel) {
                mAddressList = addressListResponseModel.getAddressStringList();
            }
        });
    }

    private void setActivityStartTime() {
        if (mMoversPreferences.getClockInTime(mJobId) > 0) {
            long startTimeMilis;
            if (mMoversPreferences.getStartBreakTime(mJobId) > 0) {
                startTimeMilis = mMoversPreferences.getStartBreakTime(mJobId);
                moveProcessBinding.txtStartTime.setText(R.string.break_start_time);
            } else {
                moveProcessBinding.txtStartTime.setText(R.string.activity_start_time);
                startTimeMilis = mMoversPreferences.getClockInTime(mJobId);
            }
            Date activityStartTime = new Date(startTimeMilis);
            String outPutDateFormat = "";
            if (DateUtils.isToday(activityStartTime.getTime())) {
                outPutDateFormat = Constants.OUTPUT_DATE_FORMAT_TIME;
            } else {
                outPutDateFormat = Constants.OUTPUT_DATE_FORMAT_DATE_TIME;
            }
            String activityStartTimeFormatted = Util.getFormattedDate(activityStartTime, outPutDateFormat);

            moveProcessBinding.txtTime.setText(activityStartTimeFormatted);
            moveProcessBinding.txtStartTime.setVisibility(View.VISIBLE);
        } else {
            moveProcessBinding.txtStartTime.setVisibility(View.INVISIBLE);
            moveProcessBinding.txtTime.setText("");
        }
    }


    private void callGetMoveProcessStatus() {

        if (!shouldMakeNetworkCall(moveProcessBinding.getRoot())) {
            return;
        }
        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        showProgress();
        viewModel.getMoveProcessStatus(subDomain, mJobId, opportunityId, userId, new ResponseListener<BaseResponseModel<MoveProcessStatusPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<MoveProcessStatusPojo> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MoveProcessActivity.this);
                    return;
                }

                if ((MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) != Constants.BolStatus.PENDING_APPROVAL)) {
                    setActivityResult(serverResponseError.getMessage());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<MoveProcessStatusPojo>> call, Throwable t, String message) {
                hideProgress();
//                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                if ((MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) != Constants.BolStatus.PENDING_APPROVAL)) {
                    setActivityResult(message);
                    finish();
                }
            }
        });
    }


    private void checkLocationSettings(OnSuccessListener<LocationSettingsResponse> onSuccessListener) {

        /*
        Clients needs to run demo on ARC welder(Chrome Extension). ARC welder does not support google play services.
        Hence in that case app sends location as 00.
        */
        if (!BuildConfig.usePlayServicesForLoction) {
            onSuccessListener.onSuccess(null);
            return;
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);

        //Check if location settings are as per requirement or not.
        client.checkLocationSettings(builder.build()).addOnSuccessListener(this, onSuccessListener).addOnFailureListener(this, e -> {
            hideProgress();
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(MoveProcessActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    private void callStartStopCLock(final String currentProcessId, final String startStopStatusId) {

        Util.showLog("###", "---------------call startStop Clock----------------");
        isStartLocation = true;
        checkLocationSettings(locationSettingsResponse -> getUserLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (!shouldMakeNetworkCall(moveProcessBinding.getRoot())) {
                    return;
                }


                String subDomain = mMoversPreferences.getSubdomain();
                String userId = mMoversPreferences.getUserId();
                String opportunityId = mMoversPreferences.getOpportunityId();


                double latitude = 0;
                double longitude = 0;

                //TODO: Remove comments for release build
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                Util.showLog("###", "--------------- Get Location ----------------");

                //TODO:Remove this
//                        Toast.makeText(MoveProcessActivity.this, location + "" +longitude, Toast.LENGTH_SHORT).show();

                if (isStartLocation) {
                    showProgress();
                    viewModel.startStopClock(subDomain, mJobId, opportunityId, userId, latitude + "", longitude + "", currentProcessId, startStopStatusId, clockActivityModel, workerList, new ResponseListener<BaseResponseModel<ClockInfoPojo>>() {
                        @Override
                        public void onResponse(BaseResponseModel<ClockInfoPojo> response, String usedUrlKey) {
                            hideProgress();
                            if (response.getData().getStatus().equals(START_PROCESS_STATUS)) {

                                //Set job activity name
                                if (!mMoversPreferences.isJobHasSingleActivity(mJobId)) {
                                    moveProcessBinding.txtJobActivity.setText(getString(R.string.job_activity));
                                    moveProcessBinding.txtBoxDeliver.setText(response.getData().getActivityName());
                                    moveProcessBinding.txtStartTime.setText(getText(R.string.activity_start_time));
                                }

                                mMoversPreferences.setCurrentActivityId(mJobId, currentProcessId);

                                startChroneTimer(CLOCK_TIMER, response.getData().getActivityName());
                                Util.showLog("###", "---------------Clock Start----------------");
                            } else {
                                stopChroneTimer(CLOCK_TIMER, response.getData().getActivityName());

                                Util.showLog("###", "---------------Clock stop----------------");

                                moveProcessBinding.txtJobActivity.setText("");
                                moveProcessBinding.txtBoxDeliver.setText("");
                                moveProcessBinding.txtStartTime.setText("");

                                mMoversPreferences.setCurrentActivityId(mJobId, "");
                                mMoversPreferences.setJobActivityName(mJobId, "");
                            }
                            moveProcessBinding.getObj().getMoveInfoPojo().setClockId(response.getData().getId());
                            moveProcessBinding.getObj().getLastClockInfo().setStatus(startStopStatusId);
                            moveProcessBinding.getObj().getLastClockInfo().setActivity(currentProcessId);
                            moveProcessBinding.setObj(moveProcessBinding.getObj());

                            setActivityStartTime();
                        }

                        @Override
                        public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                            hideProgress();

                            if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                                Util.showLoginErrorDialog(MoveProcessActivity.this);
                                return;
                            }

                            Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<BaseResponseModel<ClockInfoPojo>> call, Throwable t, String message) {
                            hideProgress();
                            Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                        }
                    });

                    isStartLocation = false;
                }
            }


        }));


    }


    public void openClockDialog() {

        /*if (MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL) {
            showSentForApprovalDialog();
            return;
        }*/


        if (moveProcessBinding.txtClockIn.getText().equals(getString(R.string.clock_in))) {
           /* if (mMoversPreferences.isJobHasSingleActivity(mJobId)) {
                callStartStopCLock("0", START_PROCESS_STATUS);
                return;
            }*/

            // ClockInDialog.start(getSupportFragmentManager());
            Intent intent = new Intent(this, ClockInActivity.class);
            intent.putExtra(KEY_MOVE_DATE, mMoveProcessStatusPojo.getMoveInfoPojo().getStartDate());
            startActivityForResult(intent, RQ_CLOCK_ACTIVITY);
        } else {
            callStartStopCLock(mMoversPreferences.getCurrentActivityId(mJobId), STOP_PROCESS_STATUS);
        }

    }

    private void showCalculateETADialog() {
        viewModel.etaResponseModelLive.setValue(null);
        checkLocationSettings(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                String phoneNumber = "";
                if (mMoveProcessStatusPojo != null && mMoveProcessStatusPojo.getMoveInfoPojo() != null && mMoveProcessStatusPojo.getMoveInfoPojo().getPhoneNumber() != null) {
                    phoneNumber = mMoveProcessStatusPojo.getMoveInfoPojo().getPhoneNumber();
                }
                CalculateETADialog.startTOCalculateETA(getSupportFragmentManager(), phoneNumber, mAddressList);
            }
        });
    }
/*
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey("AIzaSyCGJwFW_UqBrD98_9X8Aht1H7AF4ef_R6s")
                .setConnectTimeout(1, TimeUnit.MINUTES)
                .setReadTimeout(1, TimeUnit.MINUTES)
                .setWriteTimeout(1, TimeUnit.MINUTES);
    }

    private DirectionsResult getDirectionsDetails(LatLng origin, String destination, TravelMode mode) {
        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .custom()
                    .await();
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/


    public void callGetETA(Location currentLocation, String destinationAddres) {

        double latitude = 0.00;
        double longitude = 0.00;

        if (currentLocation != null) {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
        }

        String originLocationLatLong = latitude + "," + longitude;
//        String originLocationLatLong = "40.724540,-74.000070";


        showProgress();

     /*   DirectionsResult results = getDirectionsDetails(new LatLng(latitude, longitude), destinationAddres, TravelMode.DRIVING);
        if (results != null) {
            Util.showLog("Result Location",""+results.routes[0].legs[0].duration);
        }*/

        viewModel.getETA(originLocationLatLong, destinationAddres, new ResponseListener<ETAResponseModel>() {
            @Override
            public void onResponse(ETAResponseModel response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ETAResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void openJobSummary(View view) {
        Intent jobSummaryIntent = new Intent(MoveProcessActivity.this, JobSummaryActivity.class);
        jobSummaryIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mJobId);

        startActivity(jobSummaryIntent);
    }

    public void openWalkThrough(View view) {
        Intent intent = new Intent(this, WalkThroughActivity.class).putExtra(Constants.EXTRA_JOB_ID_KEY, mJobId);
        startActivityForResult(intent, RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR);
    }

    public void openInventoryActivity(View view) {
        startActivity(new Intent(this, InventoryActivity.class));
    }

    public void openNotesActivity(View view) {
        startActivity(new Intent(this, NotesActivity.class));
    }

    public void openBillOfLadingActivity(View view) {
        try {

            BillOfLadingActivity.startForResult(this, RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR, moveProcessBinding.getObj().getMoveInfoPojo().getClockRequired());

        } catch (Exception e) {
            Util.showLog("openBillOfLoadingActivity", e.getLocalizedMessage());
        }

    }

    /*private void showSentForApprovalDialog() {
        sendForAprovalDialog = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(false)
                .setMessage("Waiting for server to approve the Bill of Lading")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();

        sendForAprovalDialog.show();
    }*/


    public void onStartBreakClicked() {
        String startStopStatusId = "";
        String clockId = moveProcessBinding.getObj().getMoveInfoPojo().getClockId();
        if (moveProcessBinding.txtBreak.getText().equals(getString(R.string.start_break))) {
            startStopStatusId = START_PROCESS_STATUS;
        } else if (moveProcessBinding.txtBreak.getText().equals(getString(R.string.stop_break))) {
            startStopStatusId = STOP_PROCESS_STATUS;
        }
        callStartStopBreak(clockId, startStopStatusId);
    }

    private void callStartStopBreak(String clockId, final String startStopStatusId) {

        if (!shouldMakeNetworkCall(moveProcessBinding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        showProgress();


        viewModel.startStopBreak(subDomain, mJobId, opportunityId, userId, clockId, startStopStatusId, new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                hideProgress();
                if (moveProcessBinding.txtBreak.getText().equals(getString(R.string.start_break))) {
                    pauseChroneTimer(CLOCK_TIMER);
                    startChroneTimer(BREAK_TIMER, moveProcessBinding.txtBoxDeliver.getText().toString());
                } else {
                    stopChroneTimer(BREAK_TIMER, moveProcessBinding.txtBoxDeliver.getText().toString());
                    resumeChroneTimer(CLOCK_TIMER);
                }
                moveProcessBinding.getObj().getLastBreakInfo().setBreakStatus(startStopStatusId);
                moveProcessBinding.setObj(moveProcessBinding.getObj());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MoveProcessActivity.this);
                    return;
                }

                Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

    }


    public void callCompleteMove() {

        if (!shouldMakeNetworkCall(moveProcessBinding.getRoot())) {
            return;
        }

        showProgress();

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        viewModel.completeMove(subdomain, userId, opportunityId, jobId, " ", new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                showPaymentSuccessFullDialog();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MoveProcessActivity.this);
                    return;
                }
                Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

    }


    public void callGetAddressList(final boolean shouldShowProgress) {

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        if (shouldShowProgress) {
            showProgress();
        }

        viewModel.getAddressList(subdomain, userId, opportunityId, new ResponseListener<BaseResponseModel<AddressListResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<AddressListResponseModel> response, String usedUrlKey) {
                if (shouldShowProgress) {
                    hideProgress();
                }
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                if (shouldShowProgress) {
                    hideProgress();
                    if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                        Util.showLoginErrorDialog(MoveProcessActivity.this);
                        return;
                    }
                    Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<AddressListResponseModel>> call, Throwable t, String message) {
                if (shouldShowProgress) {
                    hideProgress();
                    Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getUserLocation(final OnSuccessListener<Location> onLocationSucessListener) {

        /*
        Clients needs to run demo on ARC welder(Chrome Extension). ARC welder does not support google play services.
        Hence in that case app sends location as 00.
        */
        if (!BuildConfig.usePlayServicesForLoction) {
            onLocationSucessListener.onSuccess(null);
            return;
        }

        checkPermission(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                showProgress();
                /*mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MoveProcessActivity.this, onLocationSucessListener);*/

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        hideProgress();
                        if (locationResult != null && locationResult.getLastLocation() != null) {
                            onLocationSucessListener.onSuccess(locationResult.getLastLocation());
                            mFusedLocationClient.removeLocationUpdates(this);
                        } else {
                            Util.showToast(getApplicationContext(), getString(R.string.location_not_found));
                        }
                    }
                }, null);
            }

            @Override
            public void onPermissionDenied() {
                /*
                We do not need any action here now.
                 */
            }

            @Override
            public void onPermissionDeniedBySystem() {
                permissionHelper.showGoToSettingsDialog(getString(R.string.goto_setting_dialoge_message), getString(R.string.setting_toast_message_location));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkPermission(PermissionHelper.PermissionCallback permissionCallback) {
        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100, getString(R.string.permanently_deny_all));
        permissionHelper.request(permissionCallback);
    }


    private void startChroneTimer(String timerType, String jobActivityName) {
        long currentTimeMillis = new Date().getTime();

        if (TextUtils.isEmpty(jobActivityName)) {
            jobActivityName = "Move";
        }

        if (timerType.equals(CLOCK_TIMER)) {
            if (mChronClockIsRunning) return;

            mMoversPreferences.setClockIsOn(mJobId, true);
            mMoversPreferences.setClockInTime(mJobId, currentTimeMillis);
            moveProcessBinding.chronClock.setBase(SystemClock.elapsedRealtime());

            moveProcessBinding.chronClock.start();
            mChronClockIsRunning = true;

            int newClockOccurrenceNum = mClockHistoryDao.getLastOccurrenceNumForJob(mJobId) + 1;

            ClockEntityModel clockInModel = new ClockEntityModel();
            clockInModel.setOccurrenceNum(newClockOccurrenceNum);
            clockInModel.setJobActivity(jobActivityName);
            clockInModel.setClockActivity(Constants.ClockProcessConstants.CLOCK_IN);
            clockInModel.setTimeStampMillis(currentTimeMillis);
            clockInModel.setIsBreak(false);
            clockInModel.setJobId(mJobId);

            mClockHistoryDao.insertAll(clockInModel);

        } else if (timerType.equals(BREAK_TIMER)) {
            if (mChronBreakIsRunning) return;
            mMoversPreferences.setStartBreakTime(mJobId, currentTimeMillis);

            moveProcessBinding.chronBreak.setBase(SystemClock.elapsedRealtime());
            moveProcessBinding.chronBreak.start();
            mChronBreakIsRunning = true;
            mMoversPreferences.setBreakIsOn(mJobId, true);

            int newBreakOccurrenceNum = mClockHistoryDao.getLastBreakOccurrenceNum(mClockHistoryDao.getLastOccurrenceNumForJob(mJobId), mJobId) + 1;

            ClockEntityModel breakInModel = new ClockEntityModel();
            breakInModel.setOccurrenceNum(mClockHistoryDao.getLastOccurrenceNumForJob(mJobId));
            breakInModel.setJobActivity(jobActivityName);
            breakInModel.setClockActivity(Constants.ClockProcessConstants.BREAK_IN);
            breakInModel.setTimeStampMillis(currentTimeMillis);
            breakInModel.setBreakOccurrenceNum(newBreakOccurrenceNum);
            breakInModel.setIsBreak(true);
            breakInModel.setJobId(mJobId);

            mClockHistoryDao.insertAll(breakInModel);

        }

    }


    private void stopChroneTimer(String timerType, String jobActivityName) {

        if (TextUtils.isEmpty(jobActivityName)) {
            jobActivityName = "Move";
        }

        if (timerType.equals(CLOCK_TIMER)) {
            if (!mChronClockIsRunning) return;

            long clockInTime = mMoversPreferences.getClockInTime(mJobId);
            long clockOutTime = (new Date().getTime());
            //setActualAndTravelTimeInPreference(clockInTime, clockOutTime);
            mMoversPreferences.setClockInTime(mJobId, 0);
//            mMoversPreferences.setCurrentActivityTotalBreakTime(mJobId, 0);

            moveProcessBinding.chronClock.stop();
            mMoversPreferences.setClockIsOn(mJobId, false);
            mChronClockIsRunning = false;

            ClockEntityModel clockOutModel = new ClockEntityModel();
            clockOutModel.setOccurrenceNum(mClockHistoryDao.getLastOccurrenceNumForJob(mJobId));
            clockOutModel.setJobActivity(jobActivityName);
            clockOutModel.setClockActivity(Constants.ClockProcessConstants.CLOCK_OUT);
            clockOutModel.setTimeStampMillis(clockOutTime);
            clockOutModel.setIsBreak(false);
            clockOutModel.setJobId(mJobId);

            mClockHistoryDao.insertAll(clockOutModel);

            mMoversPreferences.setCurrentActivityTotalBreakTime(mJobId, 0);

        } else if (timerType.equals(BREAK_TIMER)) {
            if (!mChronBreakIsRunning) return;

            long breakInTime = mMoversPreferences.getStartBreakTime(mJobId);
            long breakOutTime = (new Date().getTime());
            setBreakTimeInPreference(breakInTime, breakOutTime);

            mMoversPreferences.setStartBreakTime(mJobId, 0);
            moveProcessBinding.chronBreak.stop();
            mChronBreakIsRunning = false;
            mMoversPreferences.setBreakIsOn(mJobId, false);


            ClockEntityModel breakInModel = new ClockEntityModel();
            breakInModel.setOccurrenceNum(mClockHistoryDao.getLastOccurrenceNumForJob(mJobId));
            breakInModel.setJobActivity(jobActivityName);
            breakInModel.setClockActivity(Constants.ClockProcessConstants.BREAK_OUT);
            breakInModel.setTimeStampMillis(breakOutTime);
            breakInModel.setBreakOccurrenceNum(mClockHistoryDao.getLastBreakOccurrenceNum(breakInModel.getOccurrenceNum(), mJobId));
            breakInModel.setIsBreak(true);
            breakInModel.setJobId(mJobId);

            mClockHistoryDao.insertAll(breakInModel);

        }

    }

    private void pauseChroneTimer(String timerType) {
        if (timerType.equals(CLOCK_TIMER)) {
            if (!mChronClockIsRunning) return;

            moveProcessBinding.chronClock.stop();
            mChronClockIsRunning = false;
        } else if (timerType.equals(BREAK_TIMER)) {
            if (!mChronBreakIsRunning) return;

            moveProcessBinding.chronBreak.stop();
            mChronBreakIsRunning = false;
        }
    }

    private void resumeChroneTimer(String timerType) {
        long baseTimeMillis;
        long currentTimeMillis = new Date().getTime();

        if (timerType.equals(CLOCK_TIMER)) {

            if (mChronClockIsRunning) return;

            long clockElapsedTime = currentTimeMillis - mMoversPreferences.getClockInTime(mJobId);
            long timeToShowOnClock = clockElapsedTime - mMoversPreferences.getCurrentActivityTotalBreakTime(mJobId);
            baseTimeMillis = SystemClock.elapsedRealtime() - timeToShowOnClock;

            moveProcessBinding.chronClock.setBase(baseTimeMillis);
            moveProcessBinding.chronClock.start();
            mChronClockIsRunning = true;
        } else if (timerType.equals(BREAK_TIMER)) {
            if (mChronBreakIsRunning) return;

            long breakElapsedTime = currentTimeMillis - mMoversPreferences.getStartBreakTime(mJobId);
            moveProcessBinding.chronBreak.setBase(SystemClock.elapsedRealtime() - breakElapsedTime);
            moveProcessBinding.chronBreak.start();
            mChronBreakIsRunning = true;
        }
    }


    private void showClockAsPaused() {
        long currentTimeMillis = new Date().getTime();
        long clockElapsedTime = currentTimeMillis - mMoversPreferences.getClockInTime(mJobId);
        long pauseElapsedTime = currentTimeMillis - mMoversPreferences.getStartBreakTime(mJobId);
        long timeToShowOnClock = clockElapsedTime - mMoversPreferences.getCurrentActivityTotalBreakTime(mJobId) - pauseElapsedTime;
        long baseTimeMillis = SystemClock.elapsedRealtime() - timeToShowOnClock;

        if (mChronClockIsRunning) {
            moveProcessBinding.chronClock.stop();
        }
        moveProcessBinding.chronClock.setBase(baseTimeMillis);

        //This sets the pause time on chronometer
        moveProcessBinding.chronClock.start();
        moveProcessBinding.chronClock.stop();
        mChronClockIsRunning = false;
    }


 /*   private void setActualAndTravelTimeInPreference(long clockInTime, long clockOutTime) {

        long timeElapsed = clockOutTime - clockInTime - mMoversPreferences.getCurrentActivityTotalBreakTime(mJobId);

        if (TextUtils.equals(moveProcessBinding.getObj().getLastClockInfo().getActivity(), Constants.MoveProcessStages.MOVE_PROCESS_DRIVE)) {
            mMoversPreferences.setActualTravelTime(mJobId, timeElapsed + mMoversPreferences.getActualTravelTime(mJobId));
        } else {
            mMoversPreferences.setActualHours(mJobId, (timeElapsed + mMoversPreferences.getActualHours(mJobId)));
        }
    }*/


    private void setBreakTimeInPreference(long breakInTime, long breakOutTime) {
        long currentBreakTime = breakOutTime - breakInTime;
        long totalBreakTime = currentBreakTime + mMoversPreferences.getCurrentActivityTotalBreakTime(mJobId);
        mMoversPreferences.setCurrentActivityTotalBreakTime(mJobId, totalBreakTime);

    }


    /**
     * Method checks if job has synced the clock. If yes then returns else puts all the clock values
     * in the db
     */
    private void setClockAndBreakInfoInDatabase(MoveProcessStatusPojo moveProcessStatusPojo) {

        if (mMoversPreferences.hasSyncedClockAndBreak(mJobId)) {
            return;
        }

        mClockHistoryDao.deleteAllForGivenJob(mJobId);


        List<ClockInfoPojo> allClockInfoPojos = moveProcessStatusPojo.getAllClockList();
        List<BreakInfoPojo> allBreakInfoPojos = moveProcessStatusPojo.getAllBreakList();
        ClockInfoPojo lastClockInfo = moveProcessStatusPojo.getLastClockInfo();
        BreakInfoPojo lastBreakInfo = null;

        //Check if last break was of current clock i.e. last break out or break in was after last clock in.
        //Only if this is true then we have to consider last break.
        if (lastClockInfo != null && lastClockInfo.getId() != null && lastClockInfo.getId().equals(moveProcessStatusPojo.getLastBreakInfo().getJobClockId())) {
            lastBreakInfo = moveProcessStatusPojo.getLastBreakInfo();
        }

        long actualHour = 0;
        long actualTravelTime = 0;
        long totalBreakTime = 0;
        long clockInTimeMillis = 0;
        long clockOutTimeMillis = 0;
        long breakInTimeMillis = 0;
        long breakOutTimeMillis = 0;
        long totalJobTime = 0;


        mMoversPreferences.setActualTravelTime(mJobId, 0);
        mMoversPreferences.setActualHours(mJobId, 0);

        for (int i = 0; i < allClockInfoPojos.size(); i++) {

            ClockInfoPojo clockInfoPojo = allClockInfoPojos.get(i);

            long clockTime = Util.getLongFromString(clockInfoPojo.getTimestampAppMilis());
            String jobActivityName = "Move";
            if (!TextUtils.isEmpty(clockInfoPojo.getActivityName())) {
                jobActivityName = clockInfoPojo.getActivity();
            }
            if (clockInfoPojo.getStatus().equals(Constants.START_PROCESS_STATUS)) {

                int clockOccurrenceNum = mClockHistoryDao.getLastOccurrenceNumForJob(mJobId) + 1;

                clockInTimeMillis = clockTime;
                totalBreakTime = 0;
                totalJobTime = 0;


                ClockEntityModel clockEntityModel = new ClockEntityModel();
                clockEntityModel.setOccurrenceNum(clockOccurrenceNum);
                clockEntityModel.setJobActivity(jobActivityName);
                clockEntityModel.setClockActivity(Constants.ClockProcessConstants.CLOCK_IN);
                clockEntityModel.setIsBreak(false);
                clockEntityModel.setTimeStampMillis(clockTime);
                clockEntityModel.setJobId(mJobId);

                mClockHistoryDao.insertAll(clockEntityModel);


                //Checking from break list if there are breaks for current clock. i.e. if there are
                // break in and break out instances after current clock in.
                if (allBreakInfoPojos != null && !allBreakInfoPojos.isEmpty()) {

                    for (int j = 0; j < allBreakInfoPojos.size(); j++) {

                        BreakInfoPojo breakInfoPojo = allBreakInfoPojos.get(j);
                        if (clockInfoPojo.getId().equals(breakInfoPojo.getJobClockId())) {
                            long time = Util.getLongFromString(breakInfoPojo.getTimestampAppMilis());
                            if (breakInfoPojo.getBreakStatus().equals(Constants.START_PROCESS_STATUS)) {
                                int breakOccurrenceNum = mClockHistoryDao.getLastBreakOccurrenceNum(clockOccurrenceNum, mJobId) + 1;

                                breakInTimeMillis = time;


                                ClockEntityModel breakInClockEntityModel = clockEntityModel;
                                breakInClockEntityModel.setOccurrenceNum(clockOccurrenceNum);
                                breakInClockEntityModel.setBreakOccurrenceNum(breakOccurrenceNum);
                                breakInClockEntityModel.setIsBreak(true);
                                breakInClockEntityModel.setJobActivity(jobActivityName);
                                breakInClockEntityModel.setClockActivity(Constants.ClockProcessConstants.BREAK_IN);
                                breakInClockEntityModel.setTimeStampMillis(time);
                                breakInClockEntityModel.setJobId(mJobId);

                                mClockHistoryDao.insertAll(breakInClockEntityModel);

                            } else if (breakInfoPojo.getBreakStatus().equals(Constants.STOP_PROCESS_STATUS)) {
                                int breakOccurrenceNum = mClockHistoryDao.getLastBreakOccurrenceNum(clockOccurrenceNum, mJobId) + 1;

                                breakOutTimeMillis = time;

                                totalBreakTime = totalBreakTime + breakOutTimeMillis - breakInTimeMillis;

                                ClockEntityModel breakOutClockEntityModel = clockEntityModel;
                                breakOutClockEntityModel.setOccurrenceNum(clockOccurrenceNum);
                                breakOutClockEntityModel.setBreakOccurrenceNum(breakOccurrenceNum);
                                breakOutClockEntityModel.setIsBreak(true);
                                breakOutClockEntityModel.setJobActivity(jobActivityName);
                                breakOutClockEntityModel.setClockActivity(Constants.ClockProcessConstants.BREAK_OUT);
                                breakOutClockEntityModel.setTimeStampMillis(time);
                                breakOutClockEntityModel.setJobId(mJobId);

                                mClockHistoryDao.insertAll(breakOutClockEntityModel);
                            }
                        }
                    }
                }
            } else if (clockInfoPojo.getStatus().equals(Constants.STOP_PROCESS_STATUS)) {

                int clockOccurrenceNum = mClockHistoryDao.getLastOccurrenceNumForJob(mJobId);

                clockOutTimeMillis = clockTime;
                totalJobTime = clockOutTimeMillis - clockInTimeMillis - totalBreakTime;

                if (jobActivityName.equals(getString(R.string.drive))) {
                    actualTravelTime = actualTravelTime + totalJobTime;
                } else {
                    actualHour = actualHour + totalJobTime;
                }


                ClockEntityModel clockEntityModel = new ClockEntityModel();
                clockEntityModel.setOccurrenceNum(clockOccurrenceNum);
                clockEntityModel.setJobActivity(jobActivityName);
                clockEntityModel.setClockActivity(Constants.ClockProcessConstants.CLOCK_OUT);
                clockEntityModel.setIsBreak(false);
                clockEntityModel.setTimeStampMillis(clockTime);
                clockEntityModel.setJobId(mJobId);

                mClockHistoryDao.insertAll(clockEntityModel);
            }
        }


        boolean isClockOn = TextUtils.equals(lastClockInfo.getStatus(), Constants.START_PROCESS_STATUS);
        boolean isBreakOn = false;

        long clockInTime = Util.getLongFromString(lastClockInfo.getTimestampAppMilis());
        long startBreakTime = 0;
        long currentActivityTotalBreakTime = 0;

        if (lastBreakInfo != null) {
            isBreakOn = TextUtils.equals(lastBreakInfo.getBreakStatus(), Constants.START_PROCESS_STATUS);
            currentActivityTotalBreakTime = Util.getLongFromString(lastBreakInfo.getTotalBreakTime());
            startBreakTime = Util.getLongFromString(lastBreakInfo.getTimestampAppMilis());
        }

        String activityId = lastClockInfo.getActivity();

        mMoversPreferences.setClockIsOn(mJobId, isClockOn);
        mMoversPreferences.setBreakIsOn(mJobId, isBreakOn);

        if (isClockOn) {
            mMoversPreferences.setClockInTime(mJobId, clockInTime);
            if (isBreakOn) {
                mMoversPreferences.setStartBreakTime(mJobId, startBreakTime);
            }
            mMoversPreferences.setCurrentActivityTotalBreakTime(mJobId, currentActivityTotalBreakTime);
            mMoversPreferences.setCurrentActivityId(mJobId, activityId);
            mMoversPreferences.setJobActivityName(mJobId, lastClockInfo.getActivityName());
        }

        mMoversPreferences.setHasSyncedClockAndBreak(mJobId, true);
    }


    private void showCompleteMoveDialog() {
        String message = getString(R.string.complete_move_alert_message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme).setCancelable(true).setMessage(message).setPositiveButton(R.string.yes, (dialogInterface, i) -> {
//                        logoutDueToUnauthentication(false);
            callCompleteMove();
        }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

//        AlertDialog dialog = builder.create();
//        dialog.show();
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void showPaymentSuccessFullDialog() {

        mShouldShowPaymentSuccessFullDialog = true;

        String message = getString(R.string.payment_done_message);
        /*String message = "Done. This Job will be deleted.";
        if(isCardPayment){
            message = "Payment Successful. This Job will be removed from the deleted.";
        }*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme).setCancelable(false).setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                        logoutDueToUnauthentication(false);
                Intent intent = new Intent(MoveProcessActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        /*AlertDialog dialog = builder.create();
        dialog.show();*/

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR) {
            if (resultCode == WalkThroughActivity.RESULT_FAILED_TO_GET_DATA) {
                String message = Constants.RESPONSE_FAILURE_MESSAGE;
                if (data.hasExtra(WalkThroughActivity.EXTRA_FAILED_TO_GET_DATA_MESSAGE)) {
                    message = data.getStringExtra(WalkThroughActivity.EXTRA_FAILED_TO_GET_DATA_MESSAGE);
                }

                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }

            if (resultCode == BillOfLadingActivity.RESULT_FAILED_TO_GET_DATA) {
                String message = Constants.RESPONSE_FAILURE_MESSAGE;
                if (data.hasExtra(BillOfLadingActivity.EXTRA_FAILED_TO_GET_DATA_MESSAGE)) {
                    message = data.getStringExtra(BillOfLadingActivity.EXTRA_FAILED_TO_GET_DATA_MESSAGE);
                }

                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        } else if (requestCode == RQ_CLOCK_ACTIVITY && resultCode == RESULT_OK) {
            clockActivityModel = data.getParcelableExtra(KEY_CLOCK_ACTIVITY_MODEL);
            workerList = data.getParcelableArrayListExtra(KEY_WORKER_LIST);
            Util.showLog("WorkerList", new Gson().toJson(workerList));
            callStartStopCLock(clockActivityModel.getId(), START_PROCESS_STATUS);
        }
    }

    private void setActivityResult(String message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FAILED_TO_GET_DATA_MESSAGE, message);
        setResult(RESULT_FAILED_TO_GET_DATA, intent);
    }


    @Override
    public void onCalculateETAClicked(final String destinationAddress, final CalculateETADialog dialog) {
//        dialog.dismiss();

        mSelectedDestinationAddressForETA = destinationAddress;

        checkLocationSettings(locationSettingsResponse -> getUserLocation(location -> {
            //Util.showLog("###","Location : "+location.getLatitude() +location.getLongitude());
            callGetETA(location, mSelectedDestinationAddressForETA);
//                callGetETA(location, "Sarkhej - Gandhinagar Hwy, Bodakdev, Ahmedabad, Gujarat");
        }));


    }

    @Override
    public void onSendETAClicked(String etaString, String selectedAddress, String phoneNumber, CalculateETADialog dialog) {
//        dialog.dismiss();
        callSendEta(etaString, selectedAddress, phoneNumber, dialog);
    }

    private void callSendEta(String etaString, String selectedAddress, String phoneNumber, final DialogFragment dialog) {

        String subdomain = mMoversPreferences.getSubdomain();
        String userId = mMoversPreferences.getUserId();
        String opportunityId = mMoversPreferences.getOpportunityId();
//        String phoneNumber = "+918490005798";

        showProgress();

        viewModel.sendETA(subdomain, userId, opportunityId, phoneNumber, etaString, selectedAddress, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                dialog.dismiss();
                showETASentSuccessfullyDialog();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MoveProcessActivity.this);
                    return;
                }
                Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void showETASentSuccessfullyDialog() {
        String message = "ETA message sent successfully.";

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme).setCancelable(false).setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }


    private void showNoRouteFoundDialog() {
        String message = "Could not find any driving routs for this address.";

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme).setCancelable(false).setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (sendForAprovalDialog != null) sendForAprovalDialog.dismiss();

        /*if(mClockInDialog!=null){
            mClockInDialog.dismiss();
        }*/

        outState.putBoolean(SHOULD_SHOW_PAYMENT_SUCCESSFULL_DIALOG, mShouldShowPaymentSuccessFullDialog);
        outState.putString(SELECTED_ADDRESS_FOR_ETA, mSelectedDestinationAddressForETA);

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.cancel();
        }

        super.onSaveInstanceState(outState);
    }

    public void onCompleteJob(View view) {
        showBolSignatureDialog();
    }


    private void showBolSignatureDialog() {
        BolSignatureDialog.start(getSupportFragmentManager(), "", mMoveProcessStatusPojo.getTermsAndConditionPojo(), mMoveProcessStatusPojo.getCompanyName());
    }

    @Override
    public void onSignatureSubmitted(Bitmap signatureFile, BolSignatureDialog bolSignatureDialog) {
        callSubmitSignature(signatureFile, bolSignatureDialog);
    }

    @Override
    public void showReschedulePolicies() {
        openTermsDialog(mMoveProcessStatusPojo.getTermsAndConditionPojo().getBolCancelTitle(), mMoveProcessStatusPojo.getTermsAndConditionPojo().getcancellationAndReschedulePolicyBOL());

    }

    @Override
    public void showCompanyPolicies() {
        openTermsDialog(mMoveProcessStatusPojo.getTermsAndConditionPojo().getBolCompanyTitle(), mMoveProcessStatusPojo.getTermsAndConditionPojo().getCompanyPolicyBOL());

    }

    private void callSubmitSignature(Bitmap signatureFile, final BolSignatureDialog bolSignatureDialog) {
        if (!shouldMakeNetworkCall(moveProcessBinding.getRoot())) {
            return;
        }

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String userId = MoversPreferences.getInstance(this).getUserId();

        showProgress();

        RawBodyBOLSignatureSubmitRequestModel submitRequestModel = new RawBodyBOLSignatureSubmitRequestModel(subdomain, opportunityId, userId, mJobId, mMoveProcessStatusPojo.getrId(), "");
        viewModel.submitBOLSignature(submitRequestModel, Util.BitmapToFile(this, signatureFile), new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                hideProgress();
                bolSignatureDialog.dismiss();
                if (mMoveProcessStatusPojo.getReviewMoveTypeFlag() == 1 && mMoveProcessStatusPojo.getTermsAndConditionPojo().getRatingsFlag() == 1) {
                    showRatingDialog();
                } else {
                    showCompleteMoveDialog();
                }
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(getApplicationContext());
                    return;
                }

                Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void openTermsDialog(final String title, final String message) {
        final TermsAndPoliciesDialog termsAndPoliciesDialog = new TermsAndPoliciesDialog();
        termsAndPoliciesDialog.setTitleAndMessage(title, message);
        termsAndPoliciesDialog.show(getSupportFragmentManager(), "dialog");
    }

    private void showRatingDialog() {
        RatingDialog.start(getSupportFragmentManager());
    }

    @Override
    public void onRatingDialogSkipped(RatingDialog ratingDialog) {
        ratingDialog.dismiss();
        showCompleteMoveDialog();
    }

    @Override
    public void onRatingSubmitted(float rating, String message, RatingDialog ratingDialog) {
        callSubmitRating(rating, message, ratingDialog);
    }


    private void callSubmitRating(float rating, String message, final DialogFragment dialogFragment) {
        if (!shouldMakeNetworkCall(moveProcessBinding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        showProgress();

        viewModel.submitRating(subDomain, userId, opportunityId, mMoveProcessStatusPojo.getrId(), rating + "", message, mJobId, new ResponseListener<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>> response, String usedUrlKey) {
                hideProgress();
                dialogFragment.dismiss();
                showCompleteMoveDialog();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MoveProcessActivity.this);
                    return;
                }
                Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
