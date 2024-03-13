package com.moverbol.views.activities.jobsummary;

import static com.moverbol.constants.Constants.KEY_CLOCK_ACTIVITY_MODEL;
import static com.moverbol.constants.Constants.KEY_MOVE_DATE;
import static com.moverbol.constants.Constants.KEY_WORKER_LIST;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.moverbol.DataRepository;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityClockInActivityBinding;
import com.moverbol.model.ActivityTypeListResponseModel;
import com.moverbol.model.ClockActivityModel;
import com.moverbol.model.CrewMetadata;
import com.moverbol.model.CrewMetadataAssigned;
import com.moverbol.model.RatePerHourModel;
import com.moverbol.model.moveProcess.WorkerModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.OnSingleClickListener;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ClockInVM;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ClockInActivity extends BaseAppCompactActivity {

    private final ArrayList<WorkerModel> workerList = new ArrayList<>(0);
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityClockInActivityBinding mBinding;
    private final Runnable rateRunnable = new Runnable() {
        @Override
        public void run() {
            callRatePerHour();
        }
    };
    private ArrayList<ClockActivityModel> clockActivityList;
    private ClockActivityModel clockActivityModel = new ClockActivityModel();
    private int showHourRate = 1;
    private ClockInVM clockInVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_clock_in_activity);
        setToolbar(mBinding.toolbar.toolbar, getString(R.string.seleck_clock_in), R.drawable.ic_arrow_back_white_24dp);
        clockInVM = new ViewModelProvider(this).get(ClockInVM.class);
        loadData();

    }

    private void showData() {
        for (ClockActivityModel clockActivityModel : clockActivityList) {
            AppCompatRadioButton rb = new AppCompatRadioButton(this);
            rb.setId(clockActivityList.indexOf(clockActivityModel));
            rb.setText(clockActivityModel.getActivityName());
            rb.setPadding(8, 8, 8, 8);
            rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            mBinding.rgActivity.addView(rb);
        }

        mBinding.rgActivity.setOnCheckedChangeListener((group, checkedId) -> {
            clockActivityModel = clockActivityList.get(checkedId);
            mBinding.setClockActivityModel(clockActivityModel);
            mBinding.txtEditWorker.setText(clockActivityModel.getCrew(), false);
            mBinding.txtEditEquipment.setText(clockActivityModel.getEquipment(), false);

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

        setWorkerDropDown();
        setEquipmentDropDown();
    }

    private void callClockActivityList() {
        showProgress();
        DataRepository.getInstance().getClockActivityList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getCurrentJobId(), MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<ActivityTypeListResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ActivityTypeListResponseModel> response, String usedUrlKey) {
                hideProgress();
                clockActivityList = response.getData().getActivityList();
                showHourRate = response.getData().getHourlyRateFlag();
                showData();
                mBinding.setShowHourRate(showHourRate);
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

    private void loadData() {
        callClockActivityList();
        mBinding.btnSubmit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (checkValidation()) {
                    clockActivityModel.setCrew(Objects.requireNonNull(mBinding.txtEditWorker.getText()).toString().trim());
                    clockActivityModel.setEquipment(Objects.requireNonNull(mBinding.txtEditEquipment.getText()).toString().trim());
                    clockActivityModel.setIsBilable(mBinding.chkBillable.isChecked() ? "1" : "0");
                    // clockActivityModel.setIsItemize(mBinding.chkItemize.isChecked() ? "1" : "0");
                    if (showHourRate == 0) {
                        clockActivityModel.setRateHour("0");
                    } else {
                        clockActivityModel.setRateHour(Objects.requireNonNull(mBinding.txtEditRate.getText()).toString().trim());
                    }
                    setResult(RESULT_OK, getIntent().putExtra(KEY_CLOCK_ACTIVITY_MODEL, clockActivityModel).putExtra(KEY_WORKER_LIST, workerList));
                    onBackPressed();

                }
            }
        });
        getCrewMetadata();


        mBinding.chkBillable.setOnCheckedChangeListener((buttonView, isChecked) -> {

            setRateValue();

        });


    }

    private void callRatePerHour() {

        DataRepository.getInstance().getRatePerHour(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getCurrentJobId(), Objects.requireNonNull(mBinding.txtEditWorker.getText()).toString().trim(), Objects.requireNonNull(mBinding.txtEditEquipment.getText()).toString().trim(), getIntent().getStringExtra(KEY_MOVE_DATE), new ResponseListener<BaseResponseModel<RatePerHourModel>>() {
            @Override
            public void onResponse(BaseResponseModel<RatePerHourModel> response, String usedUrlKey) {
                mBinding.txtEditRate.setText(response.getData().getRate());
                // hideProgress();
                //  clockActivityModel.setRateHour(response.getData().getRate());
                // setResult(RESULT_OK, getIntent().putExtra(KEY_CLOCK_ACTIVITY_MODEL, clockActivityModel));
                //onBackPressed();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                //hideProgress();
                // Util.showSnackBar(mBinding.getRoot(), serverResponseError.getMessage());
            }

            @Override
            public void onFailure(Call<BaseResponseModel<RatePerHourModel>> call, Throwable t, String message) {
                //hideProgress();
                //Util.showSnackBar(mBinding.getRoot(), message);
            }
        });
    }

    private void setWorkerDropDown() {
        ArrayList<String> workList = new ArrayList<String>(0);
        for (int i = 1; i <= 30; i++) {
            workList.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, workList);
        mBinding.txtEditWorker.setAdapter(arrayAdapter);
        mBinding.txtEditWorker.setText(clockActivityModel.getCrew(), false);
        mBinding.txtEditWorker.setOnItemClickListener((parent, view, position, id) -> {
           /* if (workList.get(position).equals(clockActivityModel.getCrew())) {
                mBinding.chkItemize.setChecked(clockActivityModel.getIsItemize().equals("1"));
            } else {
                mBinding.chkItemize.setChecked(true);
            }*/
            if (showHourRate == 1) {
                setRateValue();
            }

        });
    }

    private boolean checkValidation() {
        if (clockActivityModel.getId().isEmpty()) {
            Util.showSnackBar(mBinding.getRoot(), getString(R.string.please_select_at_least_one_activity));
            return false;
        } else if (showHourRate == 1 && TextUtils.isEmpty(Objects.requireNonNull(mBinding.txtEditRate.getText()).toString().trim())) {
            Util.showSnackBar(mBinding.getRoot(), getString(R.string.please_enter_rate));
            return false;
        } else if (showHourRate == 1 && mBinding.chkBillable.isChecked() && Double.parseDouble(Objects.requireNonNull(mBinding.txtEditRate.getText()).toString().trim()) <= 0) {
            Util.showSnackBar(mBinding.getRoot(), getString(R.string.please_enter_valid_rate));
            return false;
        } else if (workerList.isEmpty()) {
            Util.showSnackBar(mBinding.getRoot(), getString(R.string.please_select_any_one_supervisor));
            return false;
        }
        return true;
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
                    Util.showLoginErrorDialog(ClockInActivity.this);
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
    }

    private void addTemporaryWorkerName(WorkerModel workerModel) {
        mBinding.txtEditTemporaryWorker.getText().clear();
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


    private void allResourceList(List<CrewMetadata> list) {
        ArrayList<CrewMetadataAssigned> supervisorList = new ArrayList<>(0);

        for (CrewMetadata crewMetadata : list) {
            supervisorList.addAll(crewMetadata.getAssignedList());
        }
        supervisorDropDown(supervisorList, "0");
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

    private void setEquipmentDropDown() {
        ArrayList<String> equipmentList = new ArrayList<String>(0);
        for (int i = 0; i <= 15; i++) {
            equipmentList.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, equipmentList);
        mBinding.txtEditEquipment.setAdapter(arrayAdapter);
        mBinding.txtEditEquipment.setText(clockActivityModel.getEquipment(), false);
        mBinding.txtEditEquipment.setOnItemClickListener((parent, view, position, id) -> {
            if (showHourRate == 1) {
                setRateValue();
            }

        });
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
            mBinding.txtEditRate.setText("0");
        }

    }
}
