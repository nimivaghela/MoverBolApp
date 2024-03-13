package com.moverbol.views.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.CustomSpinnerAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.AddCrewBinding;
import com.moverbol.model.CrewMetadata;
import com.moverbol.model.CrewMetadataAssigned;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.model.ManPowerPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by sumeet on 12/9/17.
 */

public class AddCrewActivity extends BaseAppCompactActivity {

    public static final String EXTRA_MAN_POWER_POJO = "man_power_pojo";
    private static final String KEY_TYPE_SPIN_SELECTION = "type_spin_selection";
    private static final String KEY_NAME_SPIN_SELECTION = "name_spin_selection";
    private static final String KEY_NAME_EDTXT_SELECTION = "name_text";
    private static final String KEY_DESC_EDTXT_SELECTION = "description_text";
    private AddCrewBinding addCrewBinding;
    private JobSummaryViewModel viewModel;

    private CustomSpinnerAdapter<String> spinnerCrewNameAdapter;
    private CustomSpinnerAdapter<String> spinnerCrewTypeAdapter;

    private List<CrewMetadata> crewMetadataList;
    private List<String> crewTypeList;
    private List<String> crewNameList;
    private String temporaryText;
    private String title = "";
    private String listItemPosition = null;
    private ManPowerPojo mManPowerPojo = null;

    private List<ManPowerPojo> crewList;

    private String mMoveProcessJobId = null;
    private String mJobId = null;
    private int mMoveStageDetailsPojoListPosition = 0;
    private int spintypeSelectionPosition;
    private int spinNameSelectionPosition;

    private boolean mAreCrewDetailsSet = false;
    private final ArrayList<CrewMetadataAssigned> allNameModel = new ArrayList<CrewMetadataAssigned>(0);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpIntentData();

        initialise();

        setUpActionBar();

        setLiveDataObservers();

        viewModel.setJobDetailLive();


    }

    private void setUpIntentData() {
        if (getIntent() != null) {

            if (getIntent().hasExtra(Constants.EXTRA_JOB_ID_KEY)) {
                mMoveProcessJobId = getIntent().getStringExtra(Constants.EXTRA_JOB_ID_KEY);
            }

            if (getIntent().hasExtra(Constants.KEY_LIST_POSITION)) {
                mMoveStageDetailsPojoListPosition = getIntent().getIntExtra(Constants.KEY_LIST_POSITION, 0);
            }

            if (getIntent().hasExtra(Constants.KEY_ADAPTER_POSITION)) {
                listItemPosition = getIntent().getStringExtra(Constants.KEY_ADAPTER_POSITION);

                title = getString(R.string.edit_crew);
            } else {
                title = getString(R.string.add_crew);
            }

            if (getIntent().hasExtra(EXTRA_MAN_POWER_POJO)) {
                mManPowerPojo = getIntent().getParcelableExtra(EXTRA_MAN_POWER_POJO);
            }
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(addCrewBinding.toolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    private void setLiveDataObservers() {
        viewModel.crewMetadataListLive.observe(this, new Observer<List<CrewMetadata>>() {
            @Override
            public void onChanged(@Nullable List<CrewMetadata> crewMetadata) {
                crewMetadataList = crewMetadata;
                if (MoversPreferences.getInstance(getApplicationContext()).getCurrentJobMoveTypeId().equalsIgnoreCase(Constants.MoveTypeIds.MOVE_TYPE_COMMERCIAL)) {
                    addCrewBinding.spinCrewType.setVisibility(View.VISIBLE);
                    setTypeSpinnerData(crewMetadataList);
                    if (mManPowerPojo != null && !mAreCrewDetailsSet) {
                        setCrewDetailsOnView(mManPowerPojo);
                    }
                } else {
                    addCrewBinding.spinCrewType.setVisibility(View.GONE);
                    setAllCrewName();
                }


            }
        });

        viewModel.getJobDetailLive().observe(this, new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {
                crewList = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getCrews();
                mJobId = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getJobId();
            }
        });

    }

    private void initialise() {
        addCrewBinding = DataBindingUtil.setContentView(this, R.layout.activity_crew_add_new);

        viewModel = new ViewModelProvider(this).get(JobSummaryViewModel.class);

        crewMetadataList = new ArrayList<>();
        crewTypeList = new ArrayList<>();
        crewNameList = new ArrayList<>();
        temporaryText = getString(R.string.temporary);
        spinnerCrewNameAdapter = new CustomSpinnerAdapter<>(this, R.layout.layout_spinner_item, crewNameList);
        spinnerCrewTypeAdapter = new CustomSpinnerAdapter<>(this, R.layout.layout_spinner_item, crewTypeList);


    }

    // If resource Type hide so we show all Name from type.
    private void setAllCrewName() {
        allNameModel.clear();
        for (CrewMetadata crewModel : crewMetadataList) {
            allNameModel.addAll(crewModel.getAssignedList());
        }
        if (mManPowerPojo != null && !mAreCrewDetailsSet) {
            setCrewDetailsOnView(mManPowerPojo);
        }
        CrewMetadata crewMetadata = new CrewMetadata();
        crewMetadata.setId("0");
        crewMetadata.setAssignedList(allNameModel);

        setNameSpinnerData(crewMetadata);
    }

    private void setCrewDetailsOnView(ManPowerPojo manPowerPojo) {

        spintypeSelectionPosition = viewModel.getMetadatListPosition(mManPowerPojo.getDesignationId(), Constants.GET_METADATA_MODEL_WORKER) + 1;

        if (!allNameModel.isEmpty()) {
            spinNameSelectionPosition = (viewModel.getAssignedCrewNamePosition(allNameModel, manPowerPojo.getUserId()) + 1);
        } else {
            spinNameSelectionPosition = viewModel.getMetadataAssignedPosition((spintypeSelectionPosition - 1), manPowerPojo.getUserId(), Constants.GET_METADATA_MODEL_WORKER) + 1;
        }

        if (spintypeSelectionPosition != 0) {
            addCrewBinding.spinCrewType.setSelection(spintypeSelectionPosition);
        }
        if (mManPowerPojo.getIsTempWorker() != null) {
            addCrewBinding.edtxtUnresisteredName.setText(mManPowerPojo.getIsTempWorker());
        }
        if (mManPowerPojo.getDescription() != null) {
            addCrewBinding.edtxtDescription.setText(manPowerPojo.getDescription());
        }

        mAreCrewDetailsSet = true;

    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        addCrewBinding.spinCrewName.setAdapter(spinnerCrewNameAdapter);

        addCrewBinding.spinCrewType.setAdapter(spinnerCrewTypeAdapter);

        setUpActionListeners();


        if (shouldMakeNetworkCall(addCrewBinding.getRoot())) {
            getCrewMetadata();
        }

        if (savedInstanceState != null) {
            int typeSelectionPosition = savedInstanceState.getInt(KEY_TYPE_SPIN_SELECTION);
            String nameText = savedInstanceState.getString(KEY_NAME_EDTXT_SELECTION);
            String descriptionText = savedInstanceState.getString(KEY_DESC_EDTXT_SELECTION);


            if (typeSelectionPosition != 0) {
                addCrewBinding.spinCrewType.setSelection(typeSelectionPosition);
            }

            if (nameText != null) {
                addCrewBinding.edtxtUnresisteredName.setText(nameText);
            }
            if (descriptionText != null) {
                addCrewBinding.edtxtDescription.setText(descriptionText);
            }

        }


    }

    private void setUpActionListeners() {
        addCrewBinding.spinCrewType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {

                    //position -1 because we have added a placeholder "Select Crew Type" in the list
                    setNameSpinnerData(crewMetadataList.get(position - 1));
                    addCrewBinding.spinCrewName.setVisibility(View.VISIBLE);
                    addCrewBinding.tilUnresisteredName.setVisibility(View.GONE);

                } else {
                    addCrewBinding.spinCrewName.setVisibility(View.GONE);
                    addCrewBinding.tilUnresisteredName.setVisibility(View.GONE);
                    addCrewBinding.txtSubmit.setBackgroundColor(ContextCompat.getColor(AddCrewActivity.this, R.color.text_login_gray));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addCrewBinding.spinCrewName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((parent.getSelectedItem()).equals(temporaryText)) {
                    addCrewBinding.tilUnresisteredName.setVisibility(View.VISIBLE);
                    addCrewBinding.tilUnresisteredName.requestFocus();
                } else {
                    addCrewBinding.tilUnresisteredName.setVisibility(View.GONE);
                }

                if (position != 0) {
                    addCrewBinding.txtSubmit.setBackgroundColor(ContextCompat.getColor(AddCrewActivity.this, R.color.colorPrimary));
                } else {
                    addCrewBinding.txtSubmit.setBackgroundColor(ContextCompat.getColor(AddCrewActivity.this, R.color.text_login_gray));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addCrewBinding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (shouldMakeNetworkCall(addCrewBinding.getRoot())) {
                        callAddCrewItem();
                    }
                }
            }
        });
    }

    private boolean validate() {

        if (addCrewBinding.tilUnresisteredName.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(addCrewBinding.edtxtUnresisteredName.getText().toString())) {
                addCrewBinding.edtxtUnresisteredName.setError(getString(R.string.empty_field_error));
                return false;
            }
        }

        return addCrewBinding.spinCrewType.getSelectedItemPosition() != 0 && addCrewBinding.spinCrewName.getSelectedItemPosition() != 0;

    }

    private void callAddCrewItem() {

        // (-1) because we have added first item (--select this--) from our side.
        int selectedTypePosition = (addCrewBinding.spinCrewType.getSelectedItemPosition() - 1);
        int selectedNamePosition = (addCrewBinding.spinCrewName.getSelectedItemPosition() - 1);
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String workertype;
        if (selectedTypePosition >= 0) {
            workertype = crewMetadataList.get(selectedTypePosition).getId();
        } else {
            workertype = "0";
        }

        String responsibleUser = "";
        String tempWorker = "";
        String remarks = addCrewBinding.edtxtDescription.getText().toString();
        String manpowerId = "";

        if (addCrewBinding.tilUnresisteredName.getVisibility() == View.VISIBLE) {
            tempWorker = addCrewBinding.edtxtUnresisteredName.getText().toString();
        }

        if (mManPowerPojo != null) {
            manpowerId = mManPowerPojo.getManPowerId();
        }

        if (allNameModel.isEmpty()) {
            if (selectedNamePosition < crewMetadataList.get(selectedTypePosition).getAssignedList().size()) {
                responsibleUser = crewMetadataList.get(selectedTypePosition).getAssignedList().get(selectedNamePosition).getCrewId();
            }
        } else {
            responsibleUser = allNameModel.get(selectedNamePosition).getCrewId();
        }


        showProgress();
        viewModel.addCrewItems(subdomain, mMoveProcessJobId, opportunityId, workertype, responsibleUser, tempWorker, remarks, manpowerId, userId, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                //callGetJobDetails();
                setResult(RESULT_OK);
                hideProgress();
                finish();

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(AddCrewActivity.this);
                    return;
                }
                Snackbar.make(addCrewBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addCrewBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void setTypeSpinnerData(List<CrewMetadata> crewMetadata) {
        crewTypeList.clear();
        crewTypeList.add(getString(R.string.dashed_select_crew_type));
        for (int i = 0; i < crewMetadata.size(); i++) {
            crewTypeList.add(crewMetadata.get(i).toString());
        }
        spinnerCrewTypeAdapter.setDataList(crewTypeList);
    }

    private void setNameSpinnerData(CrewMetadata crewMetadata) {
        List<CrewMetadataAssigned> assignedList = crewMetadata.getAssignedList();
        if (assignedList == null) {
            return;
        }
        crewNameList.clear();
        crewNameList.add(getString(R.string.dashed_select_worker_name));
        for (int i = 0; i < assignedList.size(); i++) {
            crewNameList.add(assignedList.get(i).toString());
        }
        crewNameList.add(temporaryText);
        spinnerCrewNameAdapter.setDataList(crewNameList);
        addCrewBinding.spinCrewName.setSelection(0);
        addCrewBinding.spinCrewName.setVisibility(View.VISIBLE);

        if (spinNameSelectionPosition != 0) {
            addCrewBinding.spinCrewName.setSelection(spinNameSelectionPosition);
        }

        addCrewBinding.tilUnresisteredName.setVisibility(View.GONE);
    }


    private void getCrewMetadata() {

        showProgress();

        viewModel.getCrewMetadata(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), MoversPreferences.getInstance(this).getCurrentJobId(), new ResponseListener<BaseResponseModel<List<CrewMetadata>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<CrewMetadata>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(AddCrewActivity.this);
                    return;
                }
                Snackbar.make(addCrewBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<CrewMetadata>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addCrewBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_TYPE_SPIN_SELECTION, addCrewBinding.spinCrewType.getSelectedItemPosition());
        outState.putInt(KEY_NAME_SPIN_SELECTION, addCrewBinding.spinCrewName.getSelectedItemPosition());
        outState.putString(KEY_NAME_EDTXT_SELECTION, addCrewBinding.edtxtUnresisteredName.getText().toString());
        outState.putString(KEY_DESC_EDTXT_SELECTION, addCrewBinding.edtxtDescription.getText().toString());
    }


    private void callGetJobDetails() {
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();


        viewModel.setJobDetails(subdomain, userId, opportunityId, mJobId, new ResponseListener<BaseResponseModel<JobDetailPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<JobDetailPojo> response, String usedUrlKey) {
                hideProgress();
                finish();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(AddCrewActivity.this);
                    return;
                }
                Snackbar.make(addCrewBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addCrewBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


}
