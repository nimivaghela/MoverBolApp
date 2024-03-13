package com.moverbol.views.activities.moveprocess;

import static com.moverbol.constants.Constants.CLOCK;
import static com.moverbol.constants.Constants.FALSE;
import static com.moverbol.constants.Constants.KEY_CLOCK_HISTORY_MODEL;
import static com.moverbol.constants.Constants.KEY_MOVE_DATE;
import static com.moverbol.constants.Constants.TRUE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.moverbol.DataRepository;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityUpdateClockHistoryBinding;
import com.moverbol.model.ActivityTypeListResponseModel;
import com.moverbol.model.ClockActivityModel;
import com.moverbol.model.CrewMetadata;
import com.moverbol.model.CrewMetadataAssigned;
import com.moverbol.model.RatePerHourModel;
import com.moverbol.model.billOfLading.ClockHistoryModel;
import com.moverbol.model.moveProcess.WorkerModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ClockInVM;
import com.moverbol.views.dialogs.DatePickerDialogFragment;
import com.moverbol.views.dialogs.TimePickerDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

public class UpdateClockHistoryActivity extends BaseAppCompactActivity {

    private ActivityUpdateClockHistoryBinding mBinding;
    private ClockHistoryModel clockHistoryModel = new ClockHistoryModel();
    private String moveDate = "";
    private String mActivityFlag = "1";
    private final ArrayList<WorkerModel> workerList = new ArrayList<>(0);
    private ClockInVM clockInVM;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int showHourRate = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moveDate = getIntent().getStringExtra(KEY_MOVE_DATE);
        mActivityFlag = getIntent().getStringExtra(ClockBreakHistoryActivity.EXTRA_ACTIVITY_FLAG);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_clock_history);
        clockInVM = new ViewModelProvider(this).get(ClockInVM.class);
        if (getIntent().hasExtra(KEY_CLOCK_HISTORY_MODEL)) {
            clockHistoryModel = getIntent().getParcelableExtra(KEY_CLOCK_HISTORY_MODEL);
            setToolbar(mBinding.toolbar.toolbar, clockHistoryModel.getEventType(), R.drawable.ic_arrow_back_white_24dp);
            mBinding.setIsEditable(true);
            setData();
            if (clockHistoryModel.getResourceData() != null) {
                for (WorkerModel workerModel : clockHistoryModel.getResourceData()) {
                    addChip(workerModel);
                }
            }
        } else {
            setToolbar(mBinding.toolbar.toolbar, getString(R.string.clock), R.drawable.ic_arrow_back_white_24dp);
            Calendar currentCalender = Calendar.getInstance();
            currentCalender.set(Calendar.SECOND, 0);
            currentCalender.set(Calendar.MILLISECOND, 0);
            long currentTime = currentCalender.getTimeInMillis();
            clockHistoryModel.setStartTimeApp(String.valueOf(currentTime));
            clockHistoryModel.setEndTimeApp(String.valueOf(currentTime));
            clockHistoryModel.setEventType(CLOCK);
        }

        loadData();
    }

    private final Runnable rateRunnable = new Runnable() {
        @Override
        public void run() {
            callRatePerHour();
        }
    };

    private void disableField() {
        if (mBinding.getIsEditable() && mActivityFlag.equalsIgnoreCase(Constants.TRUE)) {
            //  mBinding.txtInputActivityName.setEnabled(false);
            mBinding.txtInputWorker.setEnabled(false);
            mBinding.txtInputEquipment.setEnabled(false);
            mBinding.txtInputRate.setEnabled(false);
        }
    }

    public void loadData() {
        callClockActivityList();
        onClick();
        setWorkerDropDown();
        setEquipmentDropDown();
        setBreakTimeDropDown();
        disableField();
        getCrewMetadata();

        mBinding.chkBillable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String isBillable;
            if (isChecked) {
                isBillable = TRUE;
            } else {
                isBillable = FALSE;
            }
            clockHistoryModel.setIsBillable(isBillable);
            setRateValue();
            if (!isChecked) {
                setData();
            }

        });

        if (mBinding.getIsEditable() && clockHistoryModel.getIsBillable().equalsIgnoreCase(FALSE)) {
            mBinding.txtEditRate.setText(clockHistoryModel.getRate_hour());
        }


    }

    private void setWorkerDropDown() {
        ArrayList<String> workList = new ArrayList<String>(0);
        for (int i = 1; i <= 30; i++) {
            workList.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, workList);
        mBinding.txtEditWorker.setAdapter(arrayAdapter);
        mBinding.txtEditWorker.setText(clockHistoryModel.getMen(), false);
        mBinding.txtEditWorker.setOnItemClickListener((parent, view, position, id) -> {
           /* if (workList.get(position).equals(clockHistoryModel.getMen())) {
                mBinding.chkItemize.setChecked(clockHistoryModel.getItemize().equals("1"));
            } else {
                mBinding.chkItemize.setChecked(true);
            }*/
            clockHistoryModel.setMen(workList.get(position));
            if (showHourRate == 1) {
                setRateValue();
            }
        });
    }


    private void setBreakTimeDropDown() {
        ArrayList<String> breakList = new ArrayList<String>(0);
        for (int i = 0; i <= 60; i += 5) {
            breakList.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, breakList);
        mBinding.txtEditBreakTime.setAdapter(arrayAdapter);
        mBinding.txtEditBreakTime.setText(clockHistoryModel.getBreakTimeInMinute(), false);
        mBinding.txtEditBreakTime.setOnItemClickListener((parent, view, position, id) -> {
            clockHistoryModel.setTotalBreakTime(String.valueOf(TimeUnit.MINUTES.toMillis(Long.parseLong(breakList.get(position)))));
            setData();
        });
    }


    private void onClick() {
        /*   mBinding.txtEditStartDate.setOnClickListener(v -> showDatePickerDialog(mBinding.txtEditStartDate));*/
        mBinding.txtEditDate.setOnClickListener(v -> showDatePickerDialog(mBinding.txtEditDate));
        mBinding.txtEditStartTime.setOnClickListener(v -> showTimePickerDialog(true));
        mBinding.txtEditEndTime.setOnClickListener(v -> showTimePickerDialog(false));
        mBinding.btnUpdate.setOnClickListener(v -> {
            if (checkValidation()) {
                clockHistoryModel.setEventType(String.valueOf(mBinding.txtEditType.getText()).trim());
                clockHistoryModel.setMen(String.valueOf(mBinding.txtEditWorker.getText()).trim());
                clockHistoryModel.setTruck(String.valueOf(mBinding.txtEditEquipment.getText()).trim());
                clockHistoryModel.setStartTime(Util.getFormattedTimeFromMillis(Long.parseLong(clockHistoryModel.getStartTimeApp()), Constants.DD_MM_YYYY_HH_MM));
                clockHistoryModel.setEndTime(Util.getFormattedTimeFromMillis(Long.parseLong(clockHistoryModel.getEndTimeApp()), Constants.DD_MM_YYYY_HH_MM));
                clockHistoryModel.setIsBillable(mBinding.chkBillable.isChecked() ? "1" : "0");
                //clockHistoryModel.setItemize(mBinding.chkItemize.isChecked() ? "1" : "0");
                if (showHourRate == 1) {
                    clockHistoryModel.setRate_hour(Objects.requireNonNull(mBinding.txtEditRate.getText()).toString().trim());
                } else {
                    clockHistoryModel.setRate_hour("0");
                }

                clockHistoryModel.setResourceData(workerList);
                updateClockHistory(clockHistoryModel);


            }
        });
    }


    private void callClockActivityList() {
        showProgress();
        DataRepository.getInstance().getClockActivityList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getCurrentJobId(), MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<ActivityTypeListResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ActivityTypeListResponseModel> response, String usedUrlKey) {
                hideProgress();
                setActivityDropDown(response.getData().getActivityList());
                showHourRate = response.getData().getHourlyRateFlag();
                mBinding.setShowHourRate(showHourRate == 1);

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Util.showSnackBar(mBinding.getRoot(), serverResponseError.getMessage());
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ActivityTypeListResponseModel>> call, Throwable t, String message) {
                hideProgress();
                Util.showSnackBar(mBinding.getRoot(), message);
            }
        });
    }

    public void setActivityDropDown(ArrayList<ClockActivityModel> clockActivityList) {
        ArrayAdapter<ClockActivityModel> arrayAdapter = new ArrayAdapter<ClockActivityModel>(this, android.R.layout.simple_spinner_dropdown_item, clockActivityList);
        mBinding.txtEditActivityName.setAdapter(arrayAdapter);
        mBinding.txtEditActivityName.setOnItemClickListener((parent, view, position, id) -> {
            ClockActivityModel clockActivityModel = clockActivityList.get(position);
            clockHistoryModel.setActivityId(clockActivityModel.getId());
            clockActivityModel.setActivityName(clockActivityModel.getActivityName());
            if (!mBinding.getIsEditable()) {
                clockHistoryModel.setMen(clockActivityModel.getCrew());
                clockHistoryModel.setTruck(clockActivityModel.getEquipment());
                clockHistoryModel.setRate_hour(clockActivityModel.getRateHour());
                clockHistoryModel.setIsBillable(clockActivityModel.getIsBilable());
                mBinding.txtEditWorker.setText(clockHistoryModel.getMen(), false);
                mBinding.txtEditEquipment.setText(clockHistoryModel.getTruck(), false);
                setData();
            }
            // reset Workers
            workerList.clear();
            mBinding.cgSupervisorName.removeAllViews();
            mBinding.txtEditResourceType.getText().clear();
            mBinding.txtEditSupervisor.getText().clear();
            if (clockActivityModel.getResourceData() != null) {

                for (WorkerModel workerModel : clockActivityModel.getResourceData()) {
                    addChip(workerModel);
                }
            }

        });

        if (mBinding.getIsEditable()) {
            mBinding.txtEditActivityName.setText(clockHistoryModel.getActivityName(), false);
            //mBinding.txtInputActivityName.setEnabled(false);
        }

    }

    /*public void setSpinnerData(ArrayList<ClockActivityModel> clockActivityList) {
        ArrayAdapter adapter = new ArrayAdapter<ClockActivityModel>(this, R.layout.item_spinner_clock_activity, clockActivityList) {

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (position == mBinding.spinnerActivityName.getSelectedItemPosition()) {
                    v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.divider_grey));
                }
                return v;
            }
        };

        adapter.setDropDownViewResource(R.layout.item_spinner_clock_activity);
        mBinding.spinnerActivityName.setAdapter(adapter);

        mBinding.spinnerActivityName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ClockActivityModel clockActivityModel = clockActivityList.get(position);
                clockHistoryModel.setActivityId(clockActivityModel.getId());
                if (!mBinding.getIsEditable()) {
                    //  mBinding.chkItemize.setChecked(clockActivityModel.getIsBilable().equalsIgnoreCase("1"));
                    mBinding.chkBillable.setChecked(clockActivityModel.getIsItemize().equalsIgnoreCase("1"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mBinding.getIsEditable()) {
            for (ClockActivityModel clockActivityModel : clockActivityList) {
                if (clockActivityModel.getId().equals(clockHistoryModel.getActivityId())) {
                    mBinding.spinnerActivityName.setSelection(clockActivityList.indexOf(clockActivityModel));
                }
            }
            mBinding.spinnerActivityName.setEnabled(false);
        }

    }*/

    private void showDatePickerDialog(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(editText.getText())) {
            calendar = Util.dateToCalender(String.valueOf(editText.getText()), Constants.DD_MMM_YYYY);
        }
        DatePickerDialogFragment datePickerFragment = DatePickerDialogFragment.newInstance(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerFragment.setSelectDateListener(calendar1 ->
                {
                    // set StartTime
                    Calendar startTime = Calendar.getInstance();
                    startTime.setTimeInMillis(Long.parseLong(clockHistoryModel.getStartTimeApp()));
                    startTime.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                    startTime.set(Calendar.SECOND, 0);
                    startTime.set(Calendar.MILLISECOND, 0);


                    clockHistoryModel.setStartTimeApp(String.valueOf(startTime.getTimeInMillis()));
                    clockHistoryModel.setStartTime(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(startTime.getTimeInMillis())));

                    // set endTime
                    Calendar endTime = Calendar.getInstance();
                    endTime.setTimeInMillis(Long.parseLong(clockHistoryModel.getEndTimeApp()));
                    endTime.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                    endTime.set(Calendar.SECOND, 0);
                    endTime.set(Calendar.MILLISECOND, 0);

                    clockHistoryModel.setEndTimeApp(String.valueOf(endTime.getTimeInMillis()));
                    clockHistoryModel.setEndTime(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(endTime.getTimeInMillis())));

                    setData();

                }

        );
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog(Boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        if (isStartTime) {
            calendar.setTimeInMillis(Long.parseLong(clockHistoryModel.getStartTimeApp()));
        } else {
            calendar.setTimeInMillis(Long.parseLong(clockHistoryModel.getEndTimeApp()));
        }

        TimePickerDialogFragment timePickerFragment = TimePickerDialogFragment.newInstance(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
        );
        timePickerFragment.setSelectDateListener(calendar1 ->
                {
                    calendar.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY));
                    calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    if (isStartTime) {
                        clockHistoryModel.setStartTimeApp(String.valueOf(calendar.getTimeInMillis()));
                        clockHistoryModel.setStartTime(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis())));
                    } else {
                        clockHistoryModel.setEndTimeApp(String.valueOf(calendar.getTimeInMillis()));
                        clockHistoryModel.setEndTime(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis())));
                    }
                    setData();
                }
        );
        timePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void updateClockHistory(ClockHistoryModel clockHistoryModel) {
        showProgress();
        DataRepository.getInstance().updateClockHistory(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getCurrentJobId(), MoversPreferences.getInstance(this).getOpportunityId(), clockHistoryModel, new ResponseListener<BaseResponseModel<Objects>>() {
            @Override
            public void onResponse(BaseResponseModel<Objects> response, String usedUrlKey) {
                hideProgress();
                setResult(RESULT_OK);
                onBackPressed();


            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Util.showSnackBar(mBinding.getRoot(), serverResponseError.getMessage());
            }

            @Override
            public void onFailure(Call<BaseResponseModel<Objects>> call, Throwable t, String message) {
                hideProgress();
                Util.showSnackBar(mBinding.getRoot(), message);
            }
        });
    }

    private void setEquipmentDropDown() {
        ArrayList<String> equipmentList = new ArrayList<String>(0);
        for (int i = 0; i <= 15; i++) {
            equipmentList.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, equipmentList);
        mBinding.txtEditEquipment.setAdapter(arrayAdapter);
        mBinding.txtEditEquipment.setText(clockHistoryModel.getTruck(), false);
        mBinding.txtEditEquipment.setOnItemClickListener((parent, view, position, id) -> {
            if (showHourRate == 1) {
                setRateValue();
            }
        });
    }

    public boolean checkValidation() {
        mBinding.txtInputWorker.setErrorEnabled(false);
        mBinding.txtInputEquipment.setErrorEnabled(false);
        mBinding.txtInputStartTime.setErrorEnabled(false);
        mBinding.txtInputEndTime.setErrorEnabled(false);
        mBinding.txtInputRate.setErrorEnabled(false);

        if (TextUtils.isEmpty(Objects.requireNonNull(mBinding.txtEditActivityName.getText().toString().trim()))) {
            Util.showToast(this, getString(R.string.please_select_at_least_one_activity));
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mBinding.txtEditWorker.getText()).toString().trim())) {
            mBinding.txtInputWorker.setError(getString(R.string.please_enter_worker));
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mBinding.txtEditEquipment.getText()).toString().trim())) {
            mBinding.txtInputEquipment.setError(getString(R.string.please_enter_equipment));
            return false;
        } else if (showHourRate == 1 && TextUtils.isEmpty(Objects.requireNonNull(mBinding.txtEditRate.getText()).toString().trim())) {
            mBinding.txtInputRate.setError(getString(R.string.please_enter_rate));
            return false;
        }
        if (showHourRate == 1 && mBinding.chkBillable.isChecked() && Double.parseDouble(Objects.requireNonNull(mBinding.txtEditRate.getText()).toString().trim()) <= 0) {
            Util.showSnackBar(mBinding.getRoot(), getString(R.string.please_enter_valid_rate));
            return false;
        } else if (workerList.isEmpty()) {
            Util.showSnackBar(mBinding.getRoot(), getString(R.string.please_select_any_one_supervisor));
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mBinding.txtEditStartTime.getText()).toString().trim())) {
            mBinding.txtInputStartTime.setError(getString(R.string.please_select_start_time));
            return false;
        } else if (TextUtils.isEmpty(Objects.requireNonNull(mBinding.txtEditEndTime.getText()).toString().trim())) {
            mBinding.txtInputEndTime.setError(getString(R.string.please_select_end_time));
            return false;
        } else if (Double.parseDouble(Objects.requireNonNull(mBinding.txtEditTotalTime.getText()).toString().trim()) < 0) {
            Util.showSnackBar(mBinding.getRoot(), getString(R.string.please_enter_valid_time));
            return false;
        }

        return true;
    }

    private void setData() {
        mBinding.setClockHistoryModel(clockHistoryModel);
        mBinding.txtEditTotalTime.setText(clockHistoryModel.totalHour(this));
    }

    private void getCrewMetadata() {
        showProgress();
        clockInVM.getCrewMetadata(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), MoversPreferences.getInstance(this).getCurrentJobId(), new ResponseListener<BaseResponseModel<List<CrewMetadata>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<CrewMetadata>> response, String usedUrlKey) {
                hideProgress();
                if (MoversPreferences.getInstance(getApplicationContext()).getCurrentJobMoveTypeId().equalsIgnoreCase(Constants.MoveTypeIds.MOVE_TYPE_COMMERCIAL)) {
                    resourceTypeDropDown(response.getData());
                    mBinding.txtInputResourceType.setVisibility(View.VISIBLE);
                } else {
                    mBinding.txtInputResourceType.setVisibility(View.GONE);
                    allResourceList(response.getData());
                }
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(UpdateClockHistoryActivity.this);
                    return;
                }
                Snackbar.make(mBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<CrewMetadata>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void resourceTypeDropDown(List<CrewMetadata> list) {
        ArrayAdapter<CrewMetadata> adapter = new ArrayAdapter<CrewMetadata>(this, android.R.layout.select_dialog_item, list);
        mBinding.txtEditResourceType.setAdapter(adapter);
        mBinding.txtEditResourceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                supervisorDropDown(list.get(i).getAssignedList(), list.get(i).getId());
            }
        });
    }

    private void supervisorDropDown(List<CrewMetadataAssigned> list, String resourceId) {
        mBinding.txtEditSupervisor.getText().clear();
        mBinding.txtInputTemporaryWorker.setVisibility(View.GONE);
        ArrayAdapter<CrewMetadataAssigned> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, list);
        mBinding.txtEditSupervisor.setAdapter(adapter);
        mBinding.txtEditSupervisor.setOnItemClickListener((adapterView, view, i, l) ->
                {
                    CrewMetadataAssigned model = list.get(i);
                    WorkerModel workerModel = new WorkerModel();
                    workerModel.setWorkerId(model.getCrewId());
                    workerModel.setName(model.getCrewName());
                    workerModel.setResourceId(resourceId);

                    // IF select Temporary so visible temporary field
                    if (workerModel.getName().equalsIgnoreCase(getString(R.string.temporary))) {
                        mBinding.txtInputTemporaryWorker.setVisibility(View.VISIBLE);
                        addTemporaryWorkerName(workerModel);
                    } else {
                        addWorkerChip(workerModel);
                    }
                }

        );

    }


    private void addWorkerChip(WorkerModel workerModel) {
        mBinding.txtInputTemporaryWorker.setVisibility(View.GONE);
        if (!workerList.contains(workerModel)) {
            addChip(workerModel);

        }
        mBinding.txtEditSupervisor.getText().clear();
        mBinding.txtEditTemporaryWorker.getText().clear();
    }

    private void addTemporaryWorkerName(WorkerModel workerModel) {

        mBinding.txtEditTemporaryWorker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (v.getText().toString().isEmpty()) {
                        Util.showToast(getApplicationContext(), getString(R.string.please_enter_worker));
                    } else {
                        workerModel.setWorkerId(String.valueOf(System.currentTimeMillis()));
                        workerModel.setName("Temp-" + mBinding.txtEditTemporaryWorker.getText().toString());
                        addWorkerChip(workerModel);
                    }
                }
                return false;
            }
        });
    }

    private void addChip(WorkerModel workerModel) {
        Chip chip = new Chip(this);
        chip.setText(workerModel.getName());
        chip.setCloseIconVisible(true);
        chip.setTag(workerList.size() + 1);
        chip.setOnCloseIconClickListener(view -> {
                    mBinding.cgSupervisorName.removeView(view);
                    workerList.remove(workerModel);
                    updateWorker();
                }
        );
        mBinding.cgSupervisorName.addView(chip);
        workerList.add(workerModel);
        updateWorker();

    }

    private void callRatePerHour() {
        if (mBinding.chkBillable.isChecked()) {
            DataRepository.getInstance().getRatePerHour(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getCurrentJobId(), Objects.requireNonNull(mBinding.txtEditWorker.getText()).toString().trim(), Objects.requireNonNull(mBinding.txtEditEquipment.getText()).toString().trim(), moveDate, new ResponseListener<BaseResponseModel<RatePerHourModel>>() {
                @Override
                public void onResponse(BaseResponseModel<RatePerHourModel> response, String usedUrlKey) {
                    mBinding.txtEditRate.setText(response.getData().getRate());
                    clockHistoryModel.setRate_hour(response.getData().getRate());
                    setData();
                }

                @Override
                public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                    //Util.showSnackBar(mBinding.getRoot(), serverResponseError.getMessage());
                }

                @Override
                public void onFailure(Call<BaseResponseModel<RatePerHourModel>> call, Throwable t, String message) {
                    //Util.showSnackBar(mBinding.getRoot(), message);
                }
            });
        }

    }

    private void allResourceList(List<CrewMetadata> list) {
        ArrayList<CrewMetadataAssigned> supervisorList = new ArrayList<>(0);

        for (CrewMetadata crewMetadata : list) {
            supervisorList.addAll(crewMetadata.getAssignedList());
        }
        supervisorDropDown(supervisorList, "0");
    }

    private void updateWorker() {
        mBinding.txtEditWorker.setText(String.valueOf(workerList.size()));
        if (showHourRate == 1) {
            setRateValue();
        }
    }

    private void setRateValue() {
        handler.removeCallbacks(rateRunnable);
        if (mBinding.chkBillable.isChecked()) {
            handler.postDelayed(rateRunnable, 500);
        } else {
            clockHistoryModel.setRate_hour("0");
            mBinding.txtEditRate.setText(clockHistoryModel.getRate_hour());
        }
    }
}
