package com.moverbol.views.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.CustomSpinnerAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.AddMaterialBinding;
import com.moverbol.model.JobDetailPojo;
import com.moverbol.model.MaterialMetadatAssigned;
import com.moverbol.model.MaterialMetadata;
import com.moverbol.model.MaterialPojo;
import com.moverbol.model.RateTypePojo;
import com.moverbol.model.billOfLading.CategoryModel;
import com.moverbol.model.billOfLading.ChargeRateTypeModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.BaseResponseModelSecond;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.jobsummary.JobSummaryViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static android.widget.AdapterView.INVALID_POSITION;

/**
 * Created by sumeet on 12/9/17.
 */

public class AddMaterialActivity extends BaseAppCompactActivity {
    public static final int ADD_SUCCESS = 101;
    private static final String KEY_NAME_SPINNER_SELECTION = "name_spinner_selection";
    private static final String KEY_RATE_TYPE_SPINNER_SELECTION = "rate_spinner_selection";
    private static final String KEY_QUANTITY_EDTXT_TEXT = "quantity_text";
    private static final String KEY_REMARKS_EDTXT_TEXT = "remarks_text";
    private static final String KEY_RATE_EDTXT_TEXT = "rate_text";
    private static final int RATE_TYPE_UNIT_RATE = 1;
    private static final int RATE_TYPE_PACK_RATE = 2;
    private static final int RATE_TYPE_UNPACK_RATE = 3;

    private AddMaterialBinding addMaterialBinding;
    private JobSummaryViewModel viewModel;
    private List<String> materialNameList;
    private List<String> rateTypeList;
    private List<MaterialMetadata> materialMatadataList;
    private List<MaterialMetadatAssigned> metadatAssignedList;
    private List<RateTypePojo> rateTypePojoList;

    private CustomSpinnerAdapter<String> spinnerMaterialNameAdapter;
    private CustomSpinnerAdapter<String> spinnerMaterialRateTypeAdapter;

    private MaterialPojo mMaterialPojo;

    private String moveType;

    private String title = "";
    private String mMoveProcessJobId;
    private int mMoveStageDetailsPojoListPosition = 0;
    private String listItemPosition;
    private int spinTypeSelectionPosition = 0;
    private int spinRateTypeSelectionPosition = 0;
    private List<MaterialPojo> mMaterialList = null;
    private String mJobId;
    private CategoryModel selectedCategoryModel = null;
    private ChargeRateTypeModel selectedMaterialModel = null;
    private String mainMaterialId = "";

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
        setUpActionListeners();


        if (shouldMakeNetworkCall(addMaterialBinding.getRoot())) {
            if (viewModel.categoryTypeListLiveData.getValue() == null) {
                callCategoryList();
            }
        }


        if (savedInstanceState != null) {
            int nameSelectionPosition = savedInstanceState.getInt(KEY_NAME_SPINNER_SELECTION);
//            int rateTypeSelectionPosition = savedInstanceState.getInt(KEY_NAME_SPINNER_SELECTION);
            String quantityText = savedInstanceState.getString(KEY_QUANTITY_EDTXT_TEXT);
            /*String remarkText = savedInstanceState.getString(KEY_REMARKS_EDTXT_TEXT);
            String rateText = savedInstanceState.getString(KEY_RATE_EDTXT_TEXT);*/

            if (nameSelectionPosition > 0) {
                addMaterialBinding.spinMaterialName.setSelection(nameSelectionPosition);
            }
            /*if(rateTypeSelectionPosition>0){
                addMaterialBinding.spinMaterialRateType.setSelection(rateTypeSelectionPosition);
            }*/
            if (quantityText != null) {
                addMaterialBinding.edtxtMaterialQuantity.setText(quantityText);
            }
/*            if(remarkText!=null){
                addMaterialBinding.edtxtMaterialRemarks.setText(remarkText);
            }
            if(rateText!=null){
                addMaterialBinding.edtxtMaterialRate.setText(rateText);
            }*/
        }


    }


    private void setLiveDataObservers() {
        viewModel.getJobDetailLive().observe(this, jobDetailPojo -> {
            mMaterialList = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getMaterials();
            moveType = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getMoveTypeName();
            mJobId = jobDetailPojo.getMoveStageDetailsPojoList().get(mMoveStageDetailsPojoListPosition).getJobId();
            if (listItemPosition != null) {
                mMaterialPojo = mMaterialList.get(Integer.parseInt(listItemPosition));
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setcId(mMaterialPojo.getcId());
                categoryModel.setCategoryName(mMaterialPojo.getCategoryName());
                selectedCategoryModel = categoryModel;
                ChargeRateTypeModel materialModel = new ChargeRateTypeModel();
                materialModel.setcId(mMaterialPojo.getBolMaterialNameId());
                materialModel.setcStockMaterialName(mMaterialPojo.displayMaterialName());
                selectedMaterialModel = materialModel;
                addMaterialBinding.edtxtMaterialQuantity.setText(mMaterialPojo.displayQuantity());
            }
        });

        viewModel.chargeRateTypeListLiveData.observe(this, this::setMaterialSpinner);
        viewModel.categoryTypeListLiveData.observe(this, this::setCategorySpinner);

    }


    private void setUpActionBar() {
        setSupportActionBar(addMaterialBinding.toolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //getSupportActionBar().setDisplayShowTitleEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    private void setCategorySpinner(ArrayList<CategoryModel> categoryList) {
        ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<>(this, R.layout.item_spinner_charge_rate_type, categoryList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addMaterialBinding.spinnerCategory.setAdapter(adapter);

        addMaterialBinding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryModel = categoryList.get(position);
                callChargeRateTypeList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (selectedCategoryModel != null) {
            Util.showLog("Select Category name", selectedCategoryModel.getCategoryName());
            int index = categoryList.indexOf(selectedCategoryModel);
            if (index > 0) {
                addMaterialBinding.spinnerCategory.setSelection(index);
            }
        }
    }

    private void setMaterialSpinner(ArrayList<ChargeRateTypeModel> chargeRateTypeList) {
        ArrayAdapter<ChargeRateTypeModel> adapter = new ArrayAdapter<>(this, R.layout.item_spinner_charge_rate_type, chargeRateTypeList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addMaterialBinding.spinMaterialName.setAdapter(adapter);

        addMaterialBinding.spinMaterialName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMaterialModel = chargeRateTypeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (selectedMaterialModel != null) {
            Util.showLog("Select Material name", selectedMaterialModel.getcStockMaterialName());
            int index = chargeRateTypeList.indexOf(selectedMaterialModel);
            if (index > 0) {
                addMaterialBinding.spinMaterialName.setSelection(index);
            }
        }
    }

    private void setUpIntentData() {
        if (getIntent() != null) {

            if (getIntent().hasExtra(Constants.EXTRA_JOB_ID_KEY)) {
                mMoveProcessJobId = getIntent().getStringExtra(Constants.EXTRA_JOB_ID_KEY);
            }

            if (getIntent().hasExtra(Constants.KEY_LIST_POSITION)) {
                mMoveStageDetailsPojoListPosition = getIntent().getIntExtra(Constants.KEY_LIST_POSITION, 0);
                Util.showLog("DetailsPojoListPosition", mMoveStageDetailsPojoListPosition + "");
            }

            if (getIntent().hasExtra(Constants.EXTRA_ITEM_ID_KEY)) {
                mainMaterialId = getIntent().getStringExtra(Constants.EXTRA_ITEM_ID_KEY);
            }

            if (getIntent().hasExtra(Constants.KEY_ADAPTER_POSITION)) {
                listItemPosition = getIntent().getStringExtra(Constants.KEY_ADAPTER_POSITION);
                Util.showLog("ListItemPosition", listItemPosition + "");


                title = getString(R.string.edit_material);
            } else {
                title = getString(R.string.add_material);
            }
        }
    }

    private void initialise() {
        addMaterialBinding = DataBindingUtil.setContentView(this, R.layout.activity_material_add_new);
        viewModel = new ViewModelProvider(this).get(JobSummaryViewModel.class);
        String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();
        addMaterialBinding.setCurrencySymbol(currencySymbol);
    }


    private void setUpActionListeners() {
        addMaterialBinding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (shouldMakeNetworkCall(addMaterialBinding.getRoot())) {
                        callAddMaterialItem();
                    }
                }
            }
        });
    }

    private void callAddMaterialItem() {
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        showProgress();
        viewModel.addMaterialItems(subdomain, userId, opportunityId, mMoveProcessJobId, selectedMaterialModel.getcId(), String.valueOf(addMaterialBinding.edtxtMaterialQuantity.getText()), "", "", "", "", mainMaterialId, selectedCategoryModel.getcId(), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                setResult(RESULT_OK);
                finish();
                //callGetJobDetails();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(AddMaterialActivity.this);
                    return;
                }
                Snackbar.make(addMaterialBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addMaterialBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate() {
        if (selectedCategoryModel == null || addMaterialBinding.spinnerCategory.getSelectedItemPosition() == INVALID_POSITION) {
            Util.showToast(this, getString(R.string.please_select_category));
            return false;
        } else if (selectedMaterialModel == null || addMaterialBinding.spinMaterialName.getSelectedItemPosition() == INVALID_POSITION) {
            Util.showToast(this, getString(R.string.please_select_material_name));
            return false;
        } else if (TextUtils.isEmpty(addMaterialBinding.edtxtMaterialQuantity.getText().toString())) {
            addMaterialBinding.edtxtMaterialQuantity.setError(getString(R.string.empty_field_error));
            return false;
        }
        return true;

    }

    private void getMaterialMetadata() {
        showProgress();

        viewModel.getMaterialMetadata(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>>() {
            @Override
            public void onResponse(BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(AddMaterialActivity.this);
                    return;
                }
                Snackbar.make(addMaterialBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModelSecond<List<MaterialMetadata>, List<RateTypePojo>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addMaterialBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
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
                    Util.showLoginErrorDialog(AddMaterialActivity.this);
                    return;
                }
                Snackbar.make(addMaterialBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<JobDetailPojo>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(addMaterialBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void callCategoryList() {
        showProgress();
        viewModel.getCategoryTypeList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getOpportunityId(), MoversPreferences.getInstance(this).getCurrentJobMoveTypeId(), Constants.MoveInfoModelTypes.PACKING_MODEL_TEXT, new ResponseListener<BaseResponseModel<ArrayList<CategoryModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<CategoryModel>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showToast(getBaseContext(), serverResponseError.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CategoryModel>>> call, Throwable t, String message) {
                hideProgress();
                Util.showToast(getBaseContext(), message);
            }
        });
    }

    private void callChargeRateTypeList() {
        showProgress();
        viewModel.getChargeRateTypeList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getOpportunityId(), MoversPreferences.getInstance(this).getCurrentJobMoveTypeId(), Constants.MoveInfoModelTypes.PACKING_MODEL_TEXT, selectedCategoryModel.getcId(), new ResponseListener<BaseResponseModel<ArrayList<ChargeRateTypeModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ChargeRateTypeModel>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showToast(getBaseContext(), serverResponseError.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> call, Throwable t, String message) {
                hideProgress();
                Util.showToast(getBaseContext(), message);
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NAME_SPINNER_SELECTION, addMaterialBinding.spinMaterialName.getSelectedItemPosition());
        outState.putString(KEY_QUANTITY_EDTXT_TEXT, addMaterialBinding.edtxtMaterialQuantity.getText().toString());
    }
}
