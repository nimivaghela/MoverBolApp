package com.moverbol.views.activities.moveprocess.bill_of_lading;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.bolAdapters.ValuationChargesBolAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.AddChargesItemDialog;
import com.moverbol.databinding.ValuationChargesBinding;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.ChargesUnitModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesResponseModel;
import com.moverbol.model.confirmationPage.ValuationChargesPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.BillOfLadingViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by AkashM on 5/3/18.
 */

public class ValuationChargesBolActivity extends BaseAppCompactActivity implements AddChargesItemDialog.OnNewItemSubmittedListener, AddChargesItemDialog.OnEditedItemSubmittedListener {

    private static final String VALUATION_CHARGE_BOL_LIST_MODEL_NAME = "bol_valuationcharge";
    private BillOfLadingViewModel viewModel;
    private ValuationChargesBinding binding;
    //    private ValuationChargesAdapter adapter;
    private ValuationChargesBolAdapter adapter;

    private ArrayList<ValuationChargesPojo> mValuationChargesPojoList;

    private CommonChargesRequestModel mSelectedChargesRequestModel;
    private MenuItem addMenuItem;
    private MenuItem editMenuItem;
    private MenuItem cancelEditMenuItem;

    private boolean valuationChargesChanged;
    private String mBolFinalChargeId;
    private ArrayList<CommonChargesRequestModel> mCommonChargesRequestModelList;
    private ArrayList<ChargesUnitModel> mUnitModelList;
    private boolean calculatedValuationAddedToList;
    private boolean mIsEditMenuItemSelected;
    private String moveTypeId;
    private int mSelectedAdapterPosition = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_valuation_charges);


        initialisation();
        setActionListeners();
        setIntentData();

        setLiveDataObservers();

        setToolbar(binding.toolbar.toolbar, getString(R.string.valuation_charges), R.drawable.ic_arrow_back_white_24dp);

        //Set currency symbol on titles
        String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();
        String txtRateText = getText(R.string.rate) + "(" + currencySymbol + ")";
        String txtAmtText = getText(R.string.amt) + "(" + currencySymbol + ")";
        binding.rvHrader.txtRate.setText(txtRateText);
        binding.rvHrader.txtAmt.setText(txtAmtText);
    }

    private void initialisation() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_valuation_charges);
        viewModel = new ViewModelProvider(this).get(BillOfLadingViewModel.class);

        adapter = new ValuationChargesBolAdapter();
        binding.setAdapter(adapter);

        mValuationChargesPojoList = new ArrayList<>();
        mCommonChargesRequestModelList = new ArrayList<>();
        adapter.setChargesList(mCommonChargesRequestModelList);
    }

    private void setActionListeners() {
        /*adapter.setOnEditClickedListener(new ValuationChargesAdapter.OnEditClickedListener() {
            @Override
            public void onEditClicked(ValuationChargesPojo valuationChargesPojo, int adapterPosition) {
                showEditChargesDialogue(valuationChargesPojo, adapterPosition);
            }
        });*/

        adapter.setOnEditClickedListener(new ValuationChargesBolAdapter.OnEditClickedListener() {
            @Override
            public void onEditClicked(CommonChargesRequestModel commonChargesRequestModel, int adapterPosition) {
                showEditChargesDialogue(commonChargesRequestModel, adapterPosition);
            }
        });
    }

    private void setIntentData() {
        if (getIntent().hasExtra(Constants.BOLStringConstants.VALUATION_CHARGES_ITEM)) {
            mSelectedChargesRequestModel = getIntent().getParcelableExtra(Constants.BOLStringConstants.VALUATION_CHARGES_ITEM);
        }
        if (getIntent().hasExtra(Constants.BOLStringConstants.VALUATION_CHARGE_LIST)) {
            mValuationChargesPojoList = getIntent().getParcelableArrayListExtra(Constants.BOLStringConstants.VALUATION_CHARGE_LIST);
        }
        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED)) {
            valuationChargesChanged = getIntent().getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
        }

        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID)) {
            mBolFinalChargeId = getIntent().getStringExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID);
        }

        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID)) {
            moveTypeId = getIntent().getStringExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID);
        }

    }

    private void setLiveDataObservers() {
        /*viewModel.valuationChargesPojoListLive.observe(this, new Observer<ArrayList<ValuationChargesPojo>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ValuationChargesPojo> valuationChargesPojos) {
                *//*if(DataRepository.getInstance().getBillOfLadingPojo().getValuationChargesPojoCalculated()!=null){
                    valuationChargesPojos.add(0,DataRepository.getInstance().getBillOfLadingPojo().getValuationChargesPojoCalculated());
                }*//*
                mValuationChargesPojoList = valuationChargesPojos;

                if (mValuationChargesCalculated != null) {
                    mValuationChargesPojoList.add(0, mValuationChargesCalculated);
                }
                adapter.setChargesList(mValuationChargesPojoList);
            }
        });*/


        viewModel.commonChargesResponseModelLive.observe(this, new Observer<CommonChargesResponseModel>() {
            @Override
            public void onChanged(@Nullable CommonChargesResponseModel commonChargesResponseModel) {
                if (commonChargesResponseModel != null) {
                    if (commonChargesResponseModel.getCommonChargesRequestModelArrayList() != null) {
                        mCommonChargesRequestModelList = commonChargesResponseModel.getCommonChargesRequestModelArrayList();
                    }
                    if (commonChargesResponseModel.getUnitModelList() != null) {
                        mUnitModelList = commonChargesResponseModel.getUnitModelList();
                    }


                    /**
                     * Kept just in case client changes the requirements again. Remove after Production Release
                     */
                    /*if (mValuationChargesCalculated != null) {

                        if (mCommonChargesRequestModelList == null) {
                            mCommonChargesRequestModelList = new ArrayList<>();
                        } else if (mCommonChargesRequestModelList.size() > 0) {

//                            if (TextUtils.equals(mCommonChargesRequestModelList.get(0).getUnit().replace(" ", ""), mValuationChargesCalculated.getUnit().replace(" ", "")) && TextUtils.equals(mCommonChargesRequestModelList.get(0).getDescription(), mValuationChargesCalculated.getDescription())) {
                                mCommonChargesRequestModelList.remove(0);
//                            }
                        }
                        mCommonChargesRequestModelList.add(0, mValuationChargesCalculated);
                    }*/

                    adapter.setChargesList(mCommonChargesRequestModelList);

                    if (editMenuItem != null) {
                        editMenuItem.setVisible(!mCommonChargesRequestModelList.isEmpty());
                    }

                    setBottomHeader();
                }
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (viewModel.commonChargesRequestModelLive.getValue() == null) {
            /*if(mValuationChargesPojoList==null) {
                callGetValuationCharges();
            }else {
                viewModel.valuationChargesPojoListLive.postValue(mValuationChargesPojoList);
            }*/
            callGetValuationChargesBolChargesList();
        }
    }


    private void callGetValuationChargesBolChargesList() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        viewModel.getBolChargesList(subdomain, userId, opportunityId, VALUATION_CHARGE_BOL_LIST_MODEL_NAME, jobId, new ResponseListener<BaseResponseModel<CommonChargesResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<CommonChargesResponseModel> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ValuationChargesBolActivity.this);
                    return;
                }

                if (!serverResponseError.getMessage().contains("Charge data empty")) {
                    Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_SHORT).show();
                } else {
                    viewModel.commonChargesResponseModelLive.postValue(new CommonChargesResponseModel());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<CommonChargesResponseModel>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Shows add charges dialog. On Submission of new charges item makes API call to save that item
     * on server.
     */
    private void showAddChargesDialogue() {
        AddChargesItemDialog.showForAdd(getSupportFragmentManager(), mUnitModelList, moveTypeId, Constants.MoveInfoModelTypes.VALUATION_MODEL_TEXT, false);
    }


    private void showEditChargesDialogue(final CommonChargesRequestModel commonChargesRequestModel, final int adapterPosition) {
        mSelectedChargesRequestModel = commonChargesRequestModel;
        mSelectedAdapterPosition = adapterPosition;

        AddChargesItemDialog.showForEdit(getSupportFragmentManager(), mUnitModelList, false, commonChargesRequestModel.getQuantity(), commonChargesRequestModel.getUnit(), commonChargesRequestModel.getRate(), null, true, moveTypeId, Constants.MoveInfoModelTypes.ADDITIONAL_MODEL_TEXT, commonChargesRequestModel.getSelectRate(), commonChargesRequestModel.getTaxable());

    }


    /**
     * Calls API to delete a charges item from server.
     * On success response removes the item from adapter.
     *
     * @param id              Id of the item to be deleted
     * @param adapterPosition position of item in the adapter
     */
    private void callDeleteBolChargesItems(String id, final int adapterPosition) {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();

        showProgress();

        viewModel.deleteBolChargesItems(subDomain, userId, opportunityId, jobId, id, VALUATION_CHARGE_BOL_LIST_MODEL_NAME, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                mCommonChargesRequestModelList.remove(adapterPosition);
                adapter.notifyItemRemoved(adapterPosition);

                valuationChargesChanged = true;

                if (editMenuItem != null) {
                    resetToolBar();
                }
                setBottomHeader();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ValuationChargesBolActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * Calls the API to add new or edit a charges item on server.
     * On success in response adds that item in the adapter to show it to user.
     *
     * @param commonChargesRequestModel the Item to be added to server.
     */
    private void callSaveMoveChargesItem(final CommonChargesRequestModel commonChargesRequestModel, final int adapterPosition) {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String modelName = Constants.BOLChargesModels.VALUATION_CHARGE_SAVE_MODEL;


        List<CommonChargesRequestModel> list = new ArrayList<>();
        list.add(commonChargesRequestModel);

        showProgress();
        viewModel.saveBOLCharges(subDomain, opportunityId, userId, modelName, jobId, list, new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                hideProgress();
                commonChargesRequestModel.setId(response.getData());
                adapter.addToHomeList(commonChargesRequestModel, adapterPosition);
                valuationChargesChanged = true;
                if (editMenuItem != null) {
                    resetToolBar();
                }
                setBottomHeader();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ValuationChargesBolActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    private double getTotalMoveCharges(ArrayList<CommonChargesRequestModel> list) {
        double sumToReturn = 0.00;
        if (list == null) {
            return sumToReturn;
        }
        for (int i = 0; i < list.size(); i++) {
            sumToReturn = sumToReturn + Util.getDoubleFromString(list.get(i).getAmount());
        }
        return sumToReturn;
    }


    @Override
    public void onBackPressed() {
//        BillOfLadingActivity.setChargesResult(ValuationChargesBolActivity.this, mValuationChargesPojoList, getTotalMoveCharges(mValuationChargesPojoList), valuationChargesChanged);

/*
        if (calculatedValuationAddedToList && valuationChargesChanged && mCommonChargesRequestModelList.size() > 0) {
            BillOfLadingActivity.setChargesResult(ValuationChargesBolActivity.this, mCommonChargesRequestModelList.get(0), getTotalMoveCharges(mCommonChargesRequestModelList), valuationChargesChanged, Constants.NumValueTypes.NUM_VALUE_TYPE_AMOUNT, 0);
        } else {
*/
        BillOfLadingActivity.setChargesResult(ValuationChargesBolActivity.this, getTotalMoveCharges(mCommonChargesRequestModelList), valuationChargesChanged, Constants.NumValueTypes.NUM_VALUE_TYPE_AMOUNT, 0);

        super.onBackPressed();
    }

    private void setBottomHeader() {
        double subTotal = 0.0;

        for (CommonChargesRequestModel commonChargesRequestModel :
                mCommonChargesRequestModelList) {
            subTotal = subTotal + Double.parseDouble(commonChargesRequestModel.getAmount());
        }
        binding.txtTotal.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(subTotal)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();

        if (MoversPreferences.getInstance(this).getCurrentJobBolStatus(jobId) == Constants.BolStatus.PENDING_APPROVAL ||
                !MoversPreferences.getInstance(this).getBolStarted(jobId)) {
            return true;
        }

        getMenuInflater().inflate(R.menu.menu_add_items, menu);
        addMenuItem = menu.findItem(R.id.action_add_new_item);
        editMenuItem = menu.findItem(R.id.action_edit_items);
        cancelEditMenuItem = menu.findItem(R.id.action_cancel_edit);

        cancelEditMenuItem.setVisible(false);

        if (mIsEditMenuItemSelected) {
            cancelEditMenuItem.setVisible(true);
            addMenuItem.setVisible(false);
            editMenuItem.setVisible(false);
        } else {
            editMenuItem.setVisible(!mCommonChargesRequestModelList.isEmpty());
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_new_item:
                showAddChargesDialogue();
                break;

            case R.id.action_edit_items:
                adapter.showEditOption();
                mIsEditMenuItemSelected = true;
                cancelEditMenuItem.setVisible(true);
                editMenuItem.setVisible(false);
                addMenuItem.setVisible(false);
                break;

            case R.id.action_cancel_edit:
                resetToolBar();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void resetToolBar() {
        adapter.hideEditOption();
        mIsEditMenuItemSelected = false;
        cancelEditMenuItem.setVisible(false);
        editMenuItem.setVisible(!mCommonChargesRequestModelList.isEmpty());
        addMenuItem.setVisible(true);
    }

    @Override
    public void onEditedItemSubmitted(double quantity, ChargesUnitModel unitModel, double rate, String days, String taxable) {
        mSelectedChargesRequestModel.setQuantity(quantity + "");
        mSelectedChargesRequestModel.setUnit(unitModel.getUnitName());
        mSelectedChargesRequestModel.setUnitId(unitModel.getUnitId());
        mSelectedChargesRequestModel.setRate(rate + "");
        mSelectedChargesRequestModel.setAmount((rate * (quantity / unitModel.getUnitNum())) + "");
        callSaveMoveChargesItem(mSelectedChargesRequestModel, mSelectedAdapterPosition);
    }

    @Override
    public void onDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(true)
                .setMessage(R.string.delete_item_alert_msg)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    dialog.dismiss();
                    callDeleteBolChargesItems(mSelectedChargesRequestModel.getId(), mSelectedAdapterPosition);
                })

                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    @Override
    public void onNewItemSubmitted(String description, double quantity, ChargesUnitModel unitModel, double rate, String days, int cId, String selectRate, String taxable) {
        CommonChargesRequestModel commonChargesRequestModel = new CommonChargesRequestModel();

        /**cancelEditMenuItem.isVisible() is used here because we need it to show edit option
         *when other items are showing edit option. cancelEditMenuItem is only visible that time.
         */
        commonChargesRequestModel.setFieldsForAdditionalCharges("", mBolFinalChargeId, description, quantity + "", unitModel.getUnitName(), unitModel.getUnitId(), rate + "", ((quantity / unitModel.getUnitNum()) * rate) + "", cancelEditMenuItem.isVisible(), cId, selectRate);
        callSaveMoveChargesItem(commonChargesRequestModel, mCommonChargesRequestModelList.size() + 1);

    }
}
