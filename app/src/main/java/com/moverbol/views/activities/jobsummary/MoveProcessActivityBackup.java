package com.moverbol.views.activities.jobsummary;

/*public class MoveProcessActivityBackup {
}*/

/*public class MoveProcessActivityBackup extends BaseAppCompactActivity {

    public static final String EXTRA_FAILED_TO_GET_DATA_MESSAGE = "extra_failed_to_get_data";
    public static final int RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR = 100;
    public static final int RESULT_FAILED_TO_GET_DATA = 100;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final String CLOCK_TIMER = "clock_timer";
    private static final String BREAK_TIMER = "break_timer";
    private static final long F_INTERVAL = (120 * 1000);
    private static final long FASTEST_INTERVAL = F_INTERVAL / 2;
    AlertDialog sendForAprovalDialog;
    private MoveProcessBinding moveProcessBinding;
    private MoveProcessViewModel viewModel;
    private String mJobId;
    private ClockActionListener clockActionListener;
    private PermissionHelper permissionHelper;
    private FusedLocationProviderClient mFusedLocationClient;
    //    private MoveProcessStatusPojo mMoveProcessStatusPojo;
    private LocationRequest mLocationRequest;
    private MoversPreferences mMoversPreferences;
    private boolean mChronClockIsRunning;
    private boolean mChronBreakIsRunning;
    private boolean mIsClockRequired;

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

        moveProcessBinding.setIsWaitingOnApproval(MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL);

        moveProcessBinding.setIsBolStarted(MoversPreferences.getInstance(this).getBolStarted(mJobId));

//        if (MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL) {
//            showSentForApprovalDialog();
//        }

    }

    @Override
    protected void onResume() {
        *//*if (MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL) {
            showSentForApprovalDialog();
        }*//*

        moveProcessBinding.setIsWaitingOnApproval(MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL);

        moveProcessBinding.setIsBolStarted(MoversPreferences.getInstance(this).getBolStarted(mJobId));

        super.onResume();
    }

    @SuppressLint("MissingPermission")
    private void initialisation() {
        moveProcessBinding = DataBindingUtil.setContentView(this, R.layout.activity_process);
        setToolbar(moveProcessBinding.toolbar.toolbar, "", R.drawable.ic_arrow_back_white_24dp);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        viewModel = ViewModelProviders.of(this).get(MoveProcessViewModel.class);
        mMoversPreferences = MoversPreferences.getInstance(this);

        clockActionListener = new ClockActionListener() {
            @Override
            public void clockIn(String currentProcessId) {
                if (currentProcessId == null) {
                    currentProcessId = "";
                }
                callStartStopCLock(currentProcessId, START_PROCESS_STATUS);
            }
        };

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(F_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void setIntentData() {
        if (getIntent() != null) {

            if (getIntent().hasExtra(Constants.EXTRA_JOB_ID_KEY)) {
                mJobId = getIntent().getStringExtra(Constants.EXTRA_JOB_ID_KEY);
            } else {
                mJobId = MoversPreferences.getInstance(this).getCurrentJobId();
            }

            if (getIntent().hasExtra(Constants.EXTRA_IS_MOVE_INTERNATIONAL_KEY)) {
                moveProcessBinding.setMoveTypeInternational(getIntent().getBooleanExtra(Constants.EXTRA_IS_MOVE_INTERNATIONAL_KEY, false));
            }
            if (getIntent().hasExtra(Constants.EXTRA_MOVE_HAS_STORAGE_KEY)) {
                moveProcessBinding.setStorage(getIntent().getBooleanExtra(Constants.EXTRA_MOVE_HAS_STORAGE_KEY, false));
            }
        }
    }

    private void setActionListeners() {

        moveProcessBinding.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(moveProcessBinding.txtNumber.getText())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + moveProcessBinding.txtNumber.getText()));
                    startActivity(intent);
                    Util.startDialer(v.getContext(), moveProcessBinding.txtNumber.getText().toString().trim());
                }
            }
        });

        moveProcessBinding.txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(moveProcessBinding.txtNumber.getText())) {
                    Util.startDialer(v.getContext(), moveProcessBinding.txtNumber.getText().toString().trim());
                }
            }
        });

        moveProcessBinding.btnShowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockBreakHistoryActivity.start(MoveProcessActivity.this, mJobId);
            }
        });


    }

    private void setViewModelObserver() {

        Observer mMoveProcessObserver = new Observer<MoveProcessStatusPojo>() {
            @Override
            public void onChanged(@Nullable MoveProcessStatusPojo moveProcessStatusPojo) {

                if (moveProcessStatusPojo == null) {
                    moveProcessStatusPojo = new MoveProcessStatusPojo();
                }

                setClockAndBreakInfoInPreferences(moveProcessStatusPojo);

                mMoversPreferences.setCurrentJobBolStatus(mJobId, (int) Util.getLongFromString(moveProcessStatusPojo.getBolSentForApprovalStatus()));

                *//*boolean isBOLApprovalPending = TextUtils.equals(moveProcessStatusPojo.getBolSentForApprovalStatus(),
                        Constants.BolStatus.PENDING_APPROVAL + "");
                if (isBOLApprovalPending) {
                    mMoversPreferences.setCurrentJobBolStatus(mJobId, Constants.BolStatus.PENDING_APPROVAL);
                    showSentForApprovalDialog();
                } else {
                    if (sendForAprovalDialog != null) {
                        sendForAprovalDialog.dismiss();
                    }
                }*//*

                if (moveProcessStatusPojo.getMoveInfoPojo() != null) {
                    mMoversPreferences.setIsJobHasSingleActivity(mJobId,
                            moveProcessStatusPojo.getMoveInfoPojo().jobHasSingleActivity());
                }


                if (mMoversPreferences.isClockIsOn(mJobId)) {
                    resumeChroneTimer(CLOCK_TIMER);
                    moveProcessStatusPojo.getLastClockInfo().setStatus(Constants.START_PROCESS_STATUS);
                    if (!TextUtils.isEmpty(mMoversPreferences.getCurrentActivityId(mJobId))) {
                        String activityIndex = mMoversPreferences.getCurrentActivityId(mJobId);
                        if (!mMoversPreferences.isJobHasSingleActivity(mJobId)) {
                            moveProcessBinding.txtJobActivity.setText(getString(R.string.job_activity));
                            moveProcessBinding.txtBoxDeliver.setText(getJobActivityNameFromIndex(activityIndex));
                            moveProcessBinding.txtStartTime.setText(R.string.activity_start_time);
                        }
                    }
                } else {
                    moveProcessStatusPojo.getLastClockInfo().setStatus(Constants.STOP_PROCESS_STATUS);
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
            }
        };

        viewModel.moveProcessStatusPojoLive.observe(this, mMoveProcessObserver);
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

    private String getJobActivityNameFromIndex(String activityIndex) {
        String jobActivity = null;
        if (activityIndex.equals(Constants.MoveProcessStages.MOVE_PROCESS_PACKING)) {
            jobActivity = getString(R.string.packing);
        } else if (activityIndex.equals(Constants.MoveProcessStages.MOVE_PROCESS_LOADING)) {
            jobActivity = getString(R.string.loading);
        } else if (activityIndex.equals(Constants.MoveProcessStages.MOVE_PROCESS_DRIVE)) {
            jobActivity = getString(R.string.drive);
        } else if (activityIndex.equals(Constants.MoveProcessStages.MOVE_PROCESS_UNLOADING)) {
            jobActivity = getString(R.string.unloading);
        } else if (activityIndex.equals(Constants.MoveProcessStages.MOVE_PROCESS_UNPACKING)) {
            jobActivity = getString(R.string.unpacking);
        }
        return jobActivity;
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
//                Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                if (!(MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL)) {
                    setActivityResult(serverResponseError.getMessage());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<MoveProcessStatusPojo>> call, Throwable t, String message) {
                hideProgress();
//                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                if (!(MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL)) {
                    setActivityResult(message);
                    finish();
                }
            }
        });
    }


    private void checkLocationSettings(OnSuccessListener<LocationSettingsResponse> onSuccessListener) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);

        //Check if location settings are as per requirement or not.
        client.checkLocationSettings(builder.build())
                .addOnSuccessListener(this, onSuccessListener)
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgress();
                        if (e instanceof ResolvableApiException) {
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(MoveProcessActivity.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                        }
                    }
                });
    }

    private void callStartStopCLock(final String currentProcessId, final String startStopStatusId) {

        checkLocationSettings(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                getUserLocation(new OnSuccessListener<Location>() {
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
                        *//*if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }*//*


                        showProgress();

                        viewModel.startStopClock(subDomain, mJobId, opportunityId, userId, latitude + "", longitude + "", currentProcessId, startStopStatusId, new ResponseListener<BaseResponseModel<String>>() {
                            @Override
                            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                                hideProgress();

                                if (moveProcessBinding.txtClockIn.getText().equals(getString(R.string.clock_in))) {
                                    startChroneTimer(CLOCK_TIMER);

                                    //Set job activity name
                                    if (!mMoversPreferences.isJobHasSingleActivity(mJobId)) {
                                        moveProcessBinding.txtJobActivity.setText(getString(R.string.job_activity));
                                        moveProcessBinding.txtBoxDeliver.setText(getJobActivityNameFromIndex(currentProcessId));
                                        moveProcessBinding.txtStartTime.setText(getText(R.string.activity_start_time));
                                    }

                                    mMoversPreferences.setCurrentActivityId(mJobId, currentProcessId);
                                } else {
                                    stopChroneTimer(CLOCK_TIMER);

                                    moveProcessBinding.txtJobActivity.setText("");
                                    moveProcessBinding.txtBoxDeliver.setText("");
                                    moveProcessBinding.txtStartTime.setText("");

                                    mMoversPreferences.setCurrentActivityId(mJobId, "");
                                }
                                moveProcessBinding.getObj().getMoveInfoPojo().setClockId(response.getData());
                                moveProcessBinding.getObj().getLastClockInfo().setStatus(startStopStatusId);
                                moveProcessBinding.getObj().getLastClockInfo().setActivity(currentProcessId);
                                moveProcessBinding.setObj(moveProcessBinding.getObj());

                                setActivityStartTime();
                            }

                            @Override
                            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                                hideProgress();
                                Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                                hideProgress();
                                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

    }

    public void openClockDialog(View view) {

        if (MoversPreferences.getInstance(MoveProcessActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL) {
//            moveProcessBinding.btnClockIn.setBackgroundColor(ContextCompat.getColor(this, R.color.text_login_gray));
            showSentForApprovalDialog();
            return;
        }

        if (moveProcessBinding.txtClockIn.getText().equals(getString(R.string.clock_in))) {
            if (mMoversPreferences.isJobHasSingleActivity(mJobId)) {
                callStartStopCLock("0", START_PROCESS_STATUS);
                return;
            }
            ClockInDialog clockInDialog = new ClockInDialog();
            clockInDialog.setClockActionListener(clockActionListener);
            clockInDialog.show(getSupportFragmentManager(), "dialog");
        } else {
            callStartStopCLock(mMoversPreferences.getCurrentActivityId(mJobId), STOP_PROCESS_STATUS);
//            callStartStopCLock("0", STOP_PROCESS_STATUS);
        }

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

    public void openBillofLaddingActivity(View view) {
        BillOfLadingActivity.startForResult(this, RESULT_CODE_MOVE_PROCESS_API_CALL_ERROR,
                moveProcessBinding.getObj().getMoveInfoPojo().getClockRequired());
    }

    private void showSentForApprovalDialog() {
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
    }

    public void openPhotosActivity(View view) {
        startActivity(new Intent(this, PhotosActivity.class));
    }

    public void onStartBreakClicked(View view) {
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
                    startChroneTimer(BREAK_TIMER);
                } else {
                    stopChroneTimer(BREAK_TIMER);
                    resumeChroneTimer(CLOCK_TIMER);
                }
                moveProcessBinding.getObj().getLastBreakInfo().setBreakStatus(startStopStatusId);
                moveProcessBinding.setObj(moveProcessBinding.getObj());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Snackbar.make(moveProcessBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(moveProcessBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

    }



    @SuppressLint("MissingPermission")
    private void getUserLocation(final OnSuccessListener<Location> onLocationSucessListener) {
        checkPermission(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {

                showProgress();
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MoveProcessActivity.this, onLocationSucessListener);
            }

            @Override
            public void onPermissionDenied() {
                *//*
                We do not need any action here now.
                 *//*
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


    private void startChroneTimer(String timerType) {
        long currentTimeMillis = new Date().getTime();


        if (timerType.equals(CLOCK_TIMER)) {
            if (mChronClockIsRunning)
                return;

            *//**
 * We have to identify different clock_in by their occurrence to show clock history.
 * Hence as the clock_in happens we increase the occurrence num in sharedPrefs which will
 * be used in storing other data.
 * We have to identify different break_in by their occurrence to show break history.
 * Hence as the break_in happens we increase the occurrence num in sharedPrefs which will
 * be used in storing other data.
 * <p>
 * We here take
 * <p>
 * Method checks if job has synced the clock. If yes then returns else puts all the clock values
 * in the sharedPreferences
 * <p>
 * Method checks if job has synced the clock. If yes then returns else puts all the clock values
 * in the sharedPreferences
 *//*
            int newClockOccurrenceNum = mMoversPreferences.getJobActivityClockInOccurrenceNum(mJobId) + 1;
            mMoversPreferences.setJobActivityClockInOccurrenceNum(mJobId, newClockOccurrenceNum);


            mMoversPreferences.setClockIsOn(mJobId, true);
            mMoversPreferences.setClockInTime(mJobId, currentTimeMillis);
            moveProcessBinding.chronClock.setBase(SystemClock.elapsedRealtime());

            moveProcessBinding.chronClock.start();
            mChronClockIsRunning = true;

        } else if (timerType.equals(BREAK_TIMER)) {
            if (mChronBreakIsRunning)
                return;
            mMoversPreferences.setStartBreakTime(mJobId, currentTimeMillis);

            *//**We have to identify different break_in by their occurrence to show break history.
 * Hence as the break_in happens we increase the occurrence num in sharedPrefs which will
 * be used in storing other data.
 *//*
            int clockOccurrenceNum = mMoversPreferences.getJobActivityClockInOccurrenceNum(mJobId);
            int breakOccurrenceNum = mMoversPreferences.getJobActivityBreakOccurrenceNum(mJobId, clockOccurrenceNum) + 1;
            mMoversPreferences.setJobActivityBreakOccurrenceNum(mJobId, clockOccurrenceNum, breakOccurrenceNum);

            moveProcessBinding.chronBreak.setBase(SystemClock.elapsedRealtime());
            moveProcessBinding.chronBreak.start();
            mChronBreakIsRunning = true;
            mMoversPreferences.setBreakIsOn(mJobId, true);
        }

    }


    private void stopChroneTimer(String timerType) {

        if (timerType.equals(CLOCK_TIMER)) {
            if (!mChronClockIsRunning)
                return;

            long clockInTime = mMoversPreferences.getClockInTime(mJobId);
            long clockOutTime = (new Date().getTime());
            setActualAndTravelTimeInPreference(clockInTime, clockOutTime);
            mMoversPreferences.setClockInTime(mJobId, 0);
            mMoversPreferences.setCurrentActivityTotalBreakTime(mJobId, 0);

            moveProcessBinding.chronClock.stop();
            mMoversPreferences.setClockIsOn(mJobId, false);
            mChronClockIsRunning = false;
        } else if (timerType.equals(BREAK_TIMER)) {
            if (!mChronBreakIsRunning)
                return;

            long breakInTime = mMoversPreferences.getStartBreakTime(mJobId);
            long breakOutTime = (new Date().getTime());
            setBreakTimeInPreference(breakInTime, breakOutTime);

            mMoversPreferences.setStartBreakTime(mJobId, 0);
            moveProcessBinding.chronBreak.stop();
            mChronBreakIsRunning = false;
            mMoversPreferences.setBreakIsOn(mJobId, false);
        }

    }

    private void pauseChroneTimer(String timerType) {
        if (timerType.equals(CLOCK_TIMER)) {
            if (!mChronClockIsRunning)
                return;
            moveProcessBinding.chronClock.stop();
            mChronClockIsRunning = false;
        } else if (timerType.equals(BREAK_TIMER)) {
            if (!mChronBreakIsRunning)
                return;

            moveProcessBinding.chronBreak.stop();
            mChronBreakIsRunning = false;
        }
    }

    private void resumeChroneTimer(String timerType) {
        long baseTimeMillis;
        long currentTimeMillis = new Date().getTime();

        if (timerType.equals(CLOCK_TIMER)) {

            if (mChronClockIsRunning)
                return;

            long clockElapsedTime = currentTimeMillis - mMoversPreferences.getClockInTime(mJobId);
            long timeToShowOnClock = clockElapsedTime - mMoversPreferences.getCurrentActivityTotalBreakTime(mJobId);
            baseTimeMillis = SystemClock.elapsedRealtime() - timeToShowOnClock;

            moveProcessBinding.chronClock.setBase(baseTimeMillis);
            moveProcessBinding.chronClock.start();
            mChronClockIsRunning = true;
        } else if (timerType.equals(BREAK_TIMER)) {
            if (mChronBreakIsRunning)
                return;

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


    private void setActualAndTravelTimeInPreference(long clockInTime, long clockOutTime) {
//        long currentTimeMills = (new Date().getTime());
        int occurrenceNum = mMoversPreferences.getJobActivityClockInOccurrenceNum(mJobId);
        mMoversPreferences.setJobActivityClockIn(mJobId, clockInTime, occurrenceNum);
        mMoversPreferences.setJobActivityBreakTotalTime(mJobId, mMoversPreferences.getCurrentActivityTotalBreakTime(mJobId), occurrenceNum);
        mMoversPreferences.setJobActivityClockOut(mJobId, clockOutTime, occurrenceNum);
        String activityName = "Move";
        if (!TextUtils.isEmpty(moveProcessBinding.txtBoxDeliver.getText().toString())) {
            activityName = moveProcessBinding.txtBoxDeliver.getText().toString();
        }
        mMoversPreferences.setJobActivityName(mJobId, activityName, occurrenceNum);

        long timeElapsed = clockOutTime - clockInTime - mMoversPreferences.getCurrentActivityTotalBreakTime(mJobId);

        if (TextUtils.equals(moveProcessBinding.getObj().getLastClockInfo().getActivity(), Constants.MoveProcessStages.MOVE_PROCESS_DRIVE)) {
            mMoversPreferences.setActualTravelTime(mJobId, timeElapsed + mMoversPreferences.getActualTravelTime(mJobId));
        } else {
            mMoversPreferences.setActualHours(mJobId, (timeElapsed + mMoversPreferences.getActualHours(mJobId)));
        }
    }

    *//**
 * We here take
 *//*
    private void setBreakTimeInPreference(long breakInTime, long breakOutTime) {
        *//*long currentTimeMills = (new Date().getTime());
        long breakInTime = mMoversPreferences.getStartBreakTime(mJobId);*//*
        long currentBreakTime = breakOutTime - mMoversPreferences.getStartBreakTime(mJobId);
        int clockOccurrenceNum = mMoversPreferences.getJobActivityClockInOccurrenceNum(mJobId);
        int breakOccurrenceNum = mMoversPreferences.getJobActivityBreakOccurrenceNum(mJobId, clockOccurrenceNum);
        long totalBreakTime = currentBreakTime + mMoversPreferences.getCurrentActivityTotalBreakTime(mJobId);
        mMoversPreferences.setCurrentActivityTotalBreakTime(mJobId, totalBreakTime);

        mMoversPreferences.setJobActivityBreakIn(mJobId, breakInTime, clockOccurrenceNum, breakOccurrenceNum);
        mMoversPreferences.setJobActivityBreakOut(mJobId, breakOutTime, clockOccurrenceNum, breakOccurrenceNum);
    }

    *//**
 * Method checks if job has synced the clock. If yes then returns else puts all the clock values
 * in the sharedPreferences
 *//*
 *//*private void setClockAndBreakInfoInPreferences(MoveProcessStatusPojo moveProcessStatusPojo) {

        if (mMoversPreferences.hasSyncedClockAndBreak(mJobId)) {
            return;
        }

        List<ClockInfoPojo> allClockInfoPojos = moveProcessStatusPojo.getClockInfoPojoList();
        ClockInfoPojo lastClockInfo = moveProcessStatusPojo.getLastClockInfo();
        BreakInfoPojo lastBreakInfo = moveProcessStatusPojo.getLastBreakInfo();

        mMoversPreferences.setActualTravelTime(mJobId, 0);
        mMoversPreferences.setActualHours(mJobId, 0);

        for (int i = 0; i < allClockInfoPojos.size(); i++) {
            ClockInfoPojo clockInfoPojo = allClockInfoPojos.get(i);
            long clockInTime = Util.getLongFromString(clockInfoPojo.getClockInTimeMillis());
            long clockOutTime = Util.getLongFromString(clockInfoPojo.getClockOutTimeMillis());
            long totalBreakTime = Util.getLongFromString(clockInfoPojo.getTotalBreakTime());
            long totalActivityTime = clockOutTime - clockInTime - totalBreakTime;
            String jobActivityName = "Move";
            if (!TextUtils.isEmpty(getJobActivityNameFromIndex(clockInfoPojo.getActivity()))) {
                jobActivityName = getJobActivityNameFromIndex(clockInfoPojo.getActivity());
            }
            mMoversPreferences.setJobActivityClockIn(mJobId, clockInTime, i);
            mMoversPreferences.setJobActivityBreakTotalTime(mJobId, totalBreakTime, i);
            mMoversPreferences.setJobActivityClockOut(mJobId, clockOutTime, i);
            mMoversPreferences.setJobActivityName(mJobId, jobActivityName, i);

            boolean isJobActivityDrive = TextUtils.equals(clockInfoPojo.getActivity(), Constants.MoveProcessStages.MOVE_PROCESS_DRIVE);

            if (isJobActivityDrive) {
                mMoversPreferences.setActualTravelTime(mJobId, totalActivityTime + mMoversPreferences.getActualTravelTime(mJobId));
            } else {
                mMoversPreferences.setActualHours(mJobId, (totalActivityTime + mMoversPreferences.getActualHours(mJobId)));
            }

        }

        boolean isClockOn = TextUtils.equals(lastClockInfo.getStatus(), Constants.START_PROCESS_STATUS);
        boolean isBreakOn = TextUtils.equals(lastBreakInfo.getBreakStatus(), Constants.START_PROCESS_STATUS);

        long clockInTime = Util.getLongFromString(lastClockInfo.getTimestampAppMilis());
        long startBreakTime = Util.getLongFromString(lastBreakInfo.getTimestampAppMilis());
        long currentActivityTotalBreakTime = Util.getLongFromString(lastBreakInfo.getTotalBreakTime());

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
        }

        mMoversPreferences.setHasSyncedClockAndBreak(mJobId, true);
    }*//*


 *//**
 * Method checks if job has synced the clock. If yes then returns else puts all the clock values
 * in the sharedPreferences
 *//*
    private void setClockAndBreakInfoInPreferences(MoveProcessStatusPojo moveProcessStatusPojo) {

        if (mMoversPreferences.hasSyncedClockAndBreak(mJobId)) {
            return;
        }

        mMoversPreferences.setJobActivityClockInOccurrenceNum(mJobId, 0);

        List<ClockInfoPojo> allClockInfoPojos = moveProcessStatusPojo.getAllClockList();
        List<BreakInfoPojo> allBreakInfoPojos = moveProcessStatusPojo.getAllBreakList();
        ClockInfoPojo lastClockInfo = moveProcessStatusPojo.getLastClockInfo();
        BreakInfoPojo lastBreakInfo = moveProcessStatusPojo.getLastBreakInfo();

        mMoversPreferences.setActualTravelTime(mJobId, 0);
        mMoversPreferences.setActualHours(mJobId, 0);

        for (int i = 0; i < allClockInfoPojos.size(); i++) {

            ClockInfoPojo clockInfoPojo = allClockInfoPojos.get(i);

            if (clockInfoPojo.getStatus().equals(Constants.START_PROCESS_STATUS)) {

                mMoversPreferences.setJobActivityClockInOccurrenceNum(mJobId,
                        mMoversPreferences.getJobActivityClockInOccurrenceNum(mJobId) + 1);

                int clockOccurrenceNum = mMoversPreferences.getJobActivityClockInOccurrenceNum(mJobId);

                mMoversPreferences.setJobActivityBreakOccurrenceNum(mJobId, clockOccurrenceNum, 0);

                long clockInTime = Util.getLongFromString(clockInfoPojo.getTimestampAppMilis());
                String jobActivityName = "Move";
                if (!TextUtils.isEmpty(getJobActivityNameFromIndex(clockInfoPojo.getActivity()))) {
                    jobActivityName = getJobActivityNameFromIndex(clockInfoPojo.getActivity());
                }

                mMoversPreferences.setJobActivityClockIn(mJobId, clockInTime, clockOccurrenceNum);
                mMoversPreferences.setJobActivityName(mJobId, jobActivityName, clockOccurrenceNum);

                if (allBreakInfoPojos != null && !allBreakInfoPojos.isEmpty()) {


                    for (int j = 0; j < allBreakInfoPojos.size(); j++) {

                        BreakInfoPojo breakInfoPojo = allBreakInfoPojos.get(j);
                        if (clockInfoPojo.getId().equals(breakInfoPojo.getJobClockId())) {
                            long time = Util.getLongFromString(breakInfoPojo.getTimestampAppMilis());
                            if (breakInfoPojo.getBreakStatus().equals(Constants.START_PROCESS_STATUS)) {
                                mMoversPreferences.setJobActivityBreakOccurrenceNum(mJobId, clockOccurrenceNum,
                                        mMoversPreferences.getJobActivityBreakOccurrenceNum(mJobId, clockOccurrenceNum) + 1);

                                int breakOccurrenceNum = mMoversPreferences.getJobActivityBreakOccurrenceNum(mJobId, clockOccurrenceNum);
                                mMoversPreferences.setJobActivityBreakIn(mJobId, time, clockOccurrenceNum, breakOccurrenceNum);
                            } else if (breakInfoPojo.getBreakStatus().equals(Constants.STOP_PROCESS_STATUS)) {
                                int breakOccurrenceNum = mMoversPreferences.getJobActivityBreakOccurrenceNum(mJobId, clockOccurrenceNum);
                                mMoversPreferences.setJobActivityBreakOut(mJobId, time, clockOccurrenceNum, breakOccurrenceNum);


                                long totalBreakTime = time - mMoversPreferences.getJobActivityBreakIn(mJobId, clockOccurrenceNum, breakOccurrenceNum);
                                totalBreakTime = totalBreakTime + mMoversPreferences.getJobActivityBreakTotalTime(mJobId, clockOccurrenceNum);
                                mMoversPreferences.setJobActivityBreakTotalTime(mJobId, totalBreakTime, clockOccurrenceNum);
                            }
                        }
                    }
                }
            } else if (clockInfoPojo.getStatus().equals(Constants.STOP_PROCESS_STATUS)) {

                int clockOccurrenceNum = mMoversPreferences.getJobActivityClockInOccurrenceNum(mJobId);

                long clockOutTime = Util.getLongFromString(clockInfoPojo.getTimestampAppMilis());
                long clockInTime = Util.getLongFromString(allClockInfoPojos.get(i - 1).getTimestampAppMilis());
//                long totalBreakTime = Util.getLongFromString(clockInfoPojo.getTotalBreakTime());
                long totalBreakTime = mMoversPreferences.getJobActivityBreakTotalTime(mJobId, clockOccurrenceNum);
                long totalActivityTime = clockOutTime - clockInTime - totalBreakTime;


                mMoversPreferences.setJobActivityClockOut(mJobId, clockOutTime, clockOccurrenceNum);

                boolean isJobActivityDrive = TextUtils.equals(clockInfoPojo.getActivity(), Constants.MoveProcessStages.MOVE_PROCESS_DRIVE);

                if (isJobActivityDrive) {
                    mMoversPreferences.setActualTravelTime(mJobId, totalActivityTime + mMoversPreferences.getActualTravelTime(mJobId));
                } else {
                    mMoversPreferences.setActualHours(mJobId, (totalActivityTime + mMoversPreferences.getActualHours(mJobId)));
                }
            }
        }

        boolean isClockOn = TextUtils.equals(lastClockInfo.getStatus(), Constants.START_PROCESS_STATUS);
        boolean isBreakOn = TextUtils.equals(lastBreakInfo.getBreakStatus(), Constants.START_PROCESS_STATUS);

        long clockInTime = Util.getLongFromString(lastClockInfo.getTimestampAppMilis());
        long startBreakTime = Util.getLongFromString(lastBreakInfo.getTimestampAppMilis());
        long currentActivityTotalBreakTime = Util.getLongFromString(lastBreakInfo.getTotalBreakTime());

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
        }

        mMoversPreferences.setHasSyncedClockAndBreak(mJobId, true);
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
        }
    }

    private void setActivityResult(String message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FAILED_TO_GET_DATA_MESSAGE, message);
        setResult(RESULT_FAILED_TO_GET_DATA, intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sendForAprovalDialog != null)
            sendForAprovalDialog.dismiss();
    }

    public interface ClockActionListener {
        void clockIn(String currentProcessId);
    }

}*/

