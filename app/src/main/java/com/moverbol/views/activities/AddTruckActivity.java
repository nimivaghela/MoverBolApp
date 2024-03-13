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
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.CustomSpinnerAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.AddTruckBinding;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.model.TruckMetadata;
import com.moverbol.model.TruckMetadataAssigned;
import com.moverbol.model.TruckPojo;
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

public class AddTruckActivity extends BaseAppCompactActivity {

    private static final String KEY_TYPE_SPIN_POSITION = "type_spin_position";
    private static final String KEY_NAME_SPIN_POSITION = "type_spin_position";
    private static final String KEY_DESC_EDTXT_TEXT = "description_edtxt_text";
    private static final String KEY_NAME_EDTXT_SELECTION = "name_text";

    private AddTruckBinding addTruckBinding;
    private String temporaryText;

    private JobSummaryViewModel viewModel;

    private CustomSpinnerAdapter<String> spinnerTruckNameAdapter;
    private CustomSpinnerAdapter<String> spinnerTruckTypeAdapter;

    private List<TruckMetadata> truckMetadataList;
    private List<String> truckTypeList;
    private List<String> truckNameList;
    private List<TruckPojo> truckPojoList;

    private String mMoveProcessJobId = null;
    private String mJobId = null;
    private int mMoveStageDetailsPojoListPosition = 0;
    private int spintypeSelectionPosition;
    private int spinNameSelectionPosition;
    private String title = "";
    private String listItemPosition = null;
    private TruckPojo mTruckPojo = null;

    private boolean mAreTruckDetailsSet = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialise();

        setUpIntentData();

        setUpActionBar();

        setLiveDataObservers();

        viewModel.setJobDetailLive();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        addTruckBinding.spinTruckName.setAdapter(spinnerTruckNameAdapter);

        addTruckBinding.spinTruckType.setAdapter(spinnerTruckTypeAdapter);

        setUpActionListeners();

        if (shouldMakeNetworkCall(addTruckBinding.getRoot())) {

            /*if(viewModel.truckPojoLive.getValue() == null){
                callGetTruckById();
            }*/


            if (!getIntent().hasExtra(Constants.KEY_ADAPTER_POSITION) && viewModel.truckMetadataListLive.getValue() == null)
                getTruckMetadata();
        }

        if (savedInstanceState != null) {
            int typeSpinPosition = savedInstanceState.getInt(KEY_TYPE_SPIN_POSITION);
            spintypeSelectionPosition = savedInstanceState.getInt(KEY_TYPE_SPIN_POSITION);
            String descriptionText = savedInstanceState.getString(KEY_DESC_EDTXT_TEXT);
            String nameText = savedInstanceState.getString(KEY_NAME_EDTXT_SELECTION);

            if (typeSpinPosition != 0) {
                addTruckBinding.spinTruckType.setSelection(typeSpinPosition);
            }
            /*if(nameSpinPosition!=0){
                addTruckBinding.spinTruckName.setSelection(nameSpinPosition);
            }
            */
            if (descriptionText != null) {
                addTruckBinding.edtxtDescription.setText(descriptionText);
            }
            if (nameText != null) {
                addTruckBinding.edtxtUnresisteredName.setText(nameText);
            }
        }
    }


    private void setUpActionListeners() {
        addTruckBinding.spinTruckType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addTruckBinding.spinTruckName.setClickable(true);
                addTruckBinding.spinTruckName.setEnabled(true);

                if (position != 0) {
                    //position -1 because we have added a placeholder "Select Crew Type" in the list
                    setNameSpinnerData(truckMetadataList.get(position - 1));
                    addTruckBinding.spinTruckName.setVisibility(View.VISIBLE);
                    addTruckBinding.tilUnresisteredName.setVisibility(View.GONE);
                } else {
                    addTruckBinding.spinTruckName.setVisibility(View.GONE);
                    addTruckBinding.tilUnresisteredName.setVisibility(View.GONE);
                    addTruckBinding.txtSubmit.setBackgroundColor(ContextCompat.getColor(AddTruckActivity.this, R.color.text_login_gray));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addTruckBinding.spinTruckName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((parent.getSelectedItem()).equals(temporaryText)) {
                    addTruckBinding.tilUnresisteredName.setVisibility(View.VISIBLE);
                    addTruckBinding.tilUnresisteredName.requestFocus();
                } else {
                    addTruckBinding.tilUnresisteredName.setVisibility(View.GONE);
                }

                if (position != 0) {
                    addTruckBinding.txtSubmit.setBackgroundColor(ContextCompat.getColor(AddTruckActivity.this, R.color.colorPrimary));
                } else {
                    addTruckBinding.txtSubmit.setBackgroundColor(ContextCompat.getColor(AddTruckActivity.this, R.color.text_login_gray));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addTruckBinding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (shouldMakeNetworkCall(addTruckBinding.getRoot())) {
                        callAddTruckItem();
                    }
                }
            }
        });
    }

    private boolean validate() {
        if (addTruckBinding.tilUnresisteredName.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(addTruckBinding.edtxtUnresisteredName.getText().toString())) {
                addTruckBinding.edtxtUnresisteredName.setError(getString(R.string.empty_field_error));
                return false;
            }
        }

        return addTruckBinding.spinTruckType.getSelectedItemPosition() != 0 && addTruckBinding.spinTruckName.getSelectedItemPosition() != 0;

    }

    private void callAddTruckItem() {

        // (-1) because we have added first item (--select this--) from our side.
        int selectedTypePosition = (addTruckBinding.spinTruckType.getSelectedItemPosition() - 1);
        int selectedNamePosition = (addTruckBinding.spinTruckName.getSelectedItemPosition() - 1);
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
//        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String vehicleType = truckMetadataList.get(selectedTypePosition).getId();
        String vehicleNumbner = "";
        String tempVehicle = addTruckBinding.edtxtUnresisteredName.getText().toString();
        String remarks = addTruckBinding.edtxtDescription.getText().toString();
        String vehicleId = "";

        if (mTruckPojo != null) {
            vehicleId = mTruckPojo.getVehicleId();
        }

        if (selectedNamePosition < truckMetadataList.get(selectedTypePosition).getTruckMetadataAssignedList().size()) {
            vehicleNumbner = truckMetadataList.get(selectedTypePosition).getTruckMetadataAssignedList().get(selectedNamePosition).getId();
        }

        showProgress();
        viewModel.addTruckItems(subdomain, mMoveProcessJobId, opportunityId, vehicleType, vehicleNumbner, tempVehicle, remarks, vehicleId, userId, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                //callGetJobDetails();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(AddTruckActivity.this);
                    return;
                }
                Snackbar.make(addTruckBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addTruckBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setLiveDataObservers() {
        viewModel.truckMetadataListLive.observe(this, new Observer<List<TruckMetadata>>() {
            @Override
            public void onChanged(@Nullable List<TruckMetadata> truckMetadata) {
                truckMetadataList = truckMetadata;
                setTypeSpinnerData(truckMetadataList);
                if (mTruckPojo != null /*&& !mAreTruckDetailsSet*/) {
                    setTruckDetailsOnView(mTruckPojo);
                }
            }
        });

        viewModel.getJobDetailLive().observe(this, new Observer<JobDetailPojo>() {
            @Override
            public void onChanged(@Nullable JobDetailPojo jobDetailPojo) {
                truckPojoList = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getTrucks();
                mJobId = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getJobId();
                if (listItemPosition != null) {
                    mTruckPojo = truckPojoList.get(Integer.parseInt(listItemPosition));
                }
                getTruckMetadata();
            }
        });

        /*viewModel.truckPojoLive.observe(this, new Observer<TruckPojo>() {
            @Override
            public void onChanged(@Nullable TruckPojo truckPojo) {
                mTruckPojo = truckPojo;
                *//*if(mTruckPojo != null && !mAreTruckDetailsSet){
                    setTruckDetailsOnView(mTruckPojo);
                }*//*
                getTruckMetadata();
            }
        });*/
    }

    private void setTruckDetailsOnView(TruckPojo mTruckPojo) {

        spintypeSelectionPosition = viewModel.getMetadatListPosition(mTruckPojo.getVehicleTypeId(), Constants.GET_METADATA_MODEL_TRUCK) + 1;
        spinNameSelectionPosition = viewModel.getMetadataAssignedPosition((spintypeSelectionPosition - 1), mTruckPojo.getVehicleNo(), Constants.GET_METADATA_MODEL_TRUCK) + 1;
        if (spintypeSelectionPosition != 0) {
            addTruckBinding.spinTruckType.setSelection(spintypeSelectionPosition);
        }
        if (mTruckPojo.getTempName() != null) {
            addTruckBinding.edtxtUnresisteredName.setText(mTruckPojo.getTempName());
        }
        if (mTruckPojo.getRemarks() != null) {
            addTruckBinding.edtxtDescription.setText(mTruckPojo.getRemarks());
        }
    }

    private void setUpActionBar() {
        setSupportActionBar(addTruckBinding.toolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //getSupportActionBar().setDisplayShowTitleEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
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

                title = getString(R.string.edit_truck);
            } else {
                title = getString(R.string.add_truck);
            }
        }
    }

    private void initialise() {
        addTruckBinding = DataBindingUtil.setContentView(this, R.layout.activity_truck_add_new);

        truckMetadataList = new ArrayList<>();
        truckTypeList = new ArrayList<>();
        truckNameList = new ArrayList<>();
        temporaryText = getString(R.string.temporary);

        spinnerTruckNameAdapter = new CustomSpinnerAdapter<>(this, R.layout.layout_spinner_item, truckNameList);
        spinnerTruckTypeAdapter = new CustomSpinnerAdapter<>(this, R.layout.layout_spinner_item, truckTypeList);

        viewModel = ViewModelProviders.of(this).get(JobSummaryViewModel.class);
    }


    private void setTypeSpinnerData(List<TruckMetadata> truckMetadataList) {
        truckTypeList.clear();

        truckTypeList.add(getString(R.string.dashed_select_truck_type));
        for (int i = 0; i < truckMetadataList.size(); i++) {
            truckTypeList.add(truckMetadataList.get(i).toString());
        }

        spinnerTruckTypeAdapter.setDataList(truckTypeList);
    }

    private void setNameSpinnerData(TruckMetadata truckMetadata) {
        List<TruckMetadataAssigned> assignedList = truckMetadata.getTruckMetadataAssignedList();
        if (assignedList == null) {
            return;
        }
        truckNameList.clear();
        truckNameList.add(getString(R.string.dashed_select_truck_name));
        for (int i = 0; i < assignedList.size(); i++) {
            truckNameList.add(assignedList.get(i).toString());
        }
//        truckNameList.add(temporaryText);
        spinnerTruckNameAdapter.setDataList(truckNameList);
        addTruckBinding.spinTruckName.setSelection(0);
        addTruckBinding.spinTruckName.setVisibility(View.VISIBLE);

        if (spinNameSelectionPosition != 0) {
            addTruckBinding.spinTruckName.setSelection(spinNameSelectionPosition);
//            spinNameSelectionPosition = 0;
        }

        addTruckBinding.tilUnresisteredName.setVisibility(View.GONE);
    }


    private void getTruckMetadata() {
        showProgress();

        viewModel.getTruckMetadata(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<List<TruckMetadata>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<TruckMetadata>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(AddTruckActivity.this);
                    return;
                }
                Snackbar.make(addTruckBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<TruckMetadata>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addTruckBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void callGetJobDetails() {

        showProgress();
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
                    Util.showLoginErrorDialog(AddTruckActivity.this);
                    return;
                }
                Snackbar.make(addTruckBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addTruckBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void callGetTruckById() {
        if (!shouldMakeNetworkCall(addTruckBinding.getRoot())) {
            return;
        }

        if (!getIntent().hasExtra(Constants.EXTRA_ITEM_ID_KEY)) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        String truckId = getIntent().getStringExtra(Constants.EXTRA_ITEM_ID_KEY);

        viewModel.getTruckById(subdomain, userId, opportunityId, truckId, mMoveProcessJobId, new ResponseListener() {

            @Override
            public void onResponse(Object response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(AddTruckActivity.this);
                    return;
                }
                Snackbar.make(addTruckBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addTruckBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TYPE_SPIN_POSITION, addTruckBinding.spinTruckType.getSelectedItemPosition());
        outState.putInt(KEY_NAME_SPIN_POSITION, addTruckBinding.spinTruckName.getSelectedItemPosition());
        outState.putString(KEY_DESC_EDTXT_TEXT, addTruckBinding.edtxtDescription.getText().toString());
        outState.putString(KEY_NAME_EDTXT_SELECTION, addTruckBinding.edtxtUnresisteredName.getText().toString());
    }

}
