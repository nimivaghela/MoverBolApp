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
import com.moverbol.adapters.bolAdapters.CratingChargeBolAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.AddChargesItemDialog;
import com.moverbol.databinding.CratingChargesBinding;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.ChargesUnitModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesResponseModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.BillOfLadingViewModel;
import com.moverbol.views.dialogs.EditDiscountDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class CratingChargesBolActivity extends BaseAppCompactActivity
        implements AddChargesItemDialog.OnNewItemSubmittedListener,
        AddChargesItemDialog.OnEditedItemSubmittedListener,
        AddChargesItemDialog.OnUnitSelectedListener {

    private static final String CRATING_CHARGE_BOL_LIST_MODEL_NAME = "bol_cratingcharge";
    private static final String IS_EDIT_MENU_ITEM_SELECTED_KEY = "is_edit_menu_item_selected";
    private CratingChargeBolAdapter adapter;
    private BillOfLadingViewModel viewModel;
    private CratingChargesBinding binding;
    private ArrayList<CommonChargesRequestModel> mCommonChargesRequestModelList;
    private ArrayList<ChargesUnitModel> mUnitModelList;
    private MenuItem addMenuItem;
    private MenuItem editMenuItem;
    private MenuItem cancelEditMenuItem;
    private boolean mCratingChargesChanged;
    private String mBolFinalChargeId;
    private boolean mIsEditMenuItemSelected;

    private CommonChargesRequestModel mSelectedChargesRequestModel = null;
    private int mSelectedAdapterPosition = -1;

    private static final String SELECTED_CHARGES_REQUEST_MODEL_KEY = "selected_charges_request_model";
    private static final String SELECTED_ADAPTER_POSITION_KEY = "selected_adapter_position_key";
    private String moveTypeId = "0";
    private double discount = 0.0;
    private int discountType = Constants.NumValueTypes.NUM_VALUE_TYPE_AMOUNT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialisation();
        setActionListeners();
        setIntentData();

        setLiveDataObservers();

        setToolbar(binding.toolbar.toolbar, getString(R.string.crating_charges), R.drawable.ic_arrow_back_white_24dp);

        //Set currency symbol on titles
        String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();
        String txtRateText = getText(R.string.rate) + "(" + currencySymbol + ")";
        String txtAmtText = getText(R.string.amt) + "(" + currencySymbol + ")";
        binding.rvHrader.txtRate.setText(txtRateText);
        binding.rvHrader.txtAmt.setText(txtAmtText);

        if (savedInstanceState != null) {
            mIsEditMenuItemSelected = savedInstanceState.getBoolean(IS_EDIT_MENU_ITEM_SELECTED_KEY);

            mSelectedChargesRequestModel = savedInstanceState.getParcelable(SELECTED_CHARGES_REQUEST_MODEL_KEY);
            mSelectedAdapterPosition = savedInstanceState.getInt(SELECTED_ADAPTER_POSITION_KEY);
        }

    }

    private void initialisation() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crating_charges);
        viewModel = new ViewModelProvider(this).get(BillOfLadingViewModel.class);

        adapter = new CratingChargeBolAdapter();
        binding.setAdapter(adapter);

        mCommonChargesRequestModelList = new ArrayList<>();
        mUnitModelList = new ArrayList<>();

        adapter.setChargesList(mCommonChargesRequestModelList);
        binding.setIsBolStarted(MoversPreferences.getInstance(this).getBolStarted(MoversPreferences.getInstance(this).getCurrentJobId()));
    }

    private void setActionListeners() {

        adapter.setOnEditClickedListener(new CratingChargeBolAdapter.OnEditClickedListener() {

            @Override
            public void onEditClicked(CommonChargesRequestModel commonChargesRequestModel, int adapterPosition) {
                showEditChargesDialogue(commonChargesRequestModel, adapterPosition);
            }
        });

        binding.imgEditDiscount.setOnClickListener(v -> {
            showEditDiscountDialog();
        });
    }

    private void setIntentData() {
        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED)) {
            mCratingChargesChanged = getIntent().getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
        }

        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID)) {
            mBolFinalChargeId = getIntent().getStringExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID);
        }

        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID)) {
            moveTypeId = getIntent().getStringExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID);
        }

        if (getIntent().hasExtra(Constants.DISCOUNT)) {
            discount = getIntent().getDoubleExtra(Constants.DISCOUNT, 0.0);
        }

        if (getIntent().hasExtra(Constants.DISCOUNT_TYPE)) {
            discountType = getIntent().getIntExtra(Constants.DISCOUNT_TYPE, Constants.NumValueTypes.NUM_VALUE_TYPE_AMOUNT);
        }
    }

    private void setLiveDataObservers() {

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
            callGetCratingChargesBolChargesList();
        }
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
        }else {
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


    private void callGetCratingChargesBolChargesList() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        viewModel.getBolChargesList(subdomain, userId, opportunityId, CRATING_CHARGE_BOL_LIST_MODEL_NAME, jobId, new ResponseListener<BaseResponseModel<CommonChargesResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<CommonChargesResponseModel> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(CratingChargesBolActivity.this);
                    return;
                }

                if (!serverResponseError.getMessage().contains("Charge data empty")) {
                    Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_SHORT).show();
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

//        AddChargesItemDialog.showForAdd(getSupportFragmentManager(), mUnitModelList, false, new AddChargesItemDialog.OnNewItemSubmittedListener() {
        AddChargesItemDialog.showForAdd(getSupportFragmentManager(), mUnitModelList, moveTypeId, Constants.MoveInfoModelTypes.CRATING_MODEL_TEXT, false/*, new AddChargesItemDialog.OnNewItemSubmittedListener() {
            @Override
            public void onNewItemSubmitted(String description, double quantity, String unit, String unitId, double rate, String days) {

                CommonChargesRequestModel commonChargesRequestModel = new CommonChargesRequestModel();

                *//**cancelEditMenuItem.isVisible() is used here because we need it to show edit option
                 *when other items are showing edit option. cancelEditMenuItem is only visible that time.
                 *//*
                commonChargesRequestModel.setFieldsForCratingCharges("", mBolFinalChargeId, description, quantity + "", unit, unitId, rate + "", (quantity * rate) + "", null, cancelEditMenuItem.isVisible());
                callSaveMoveChargesItem(commonChargesRequestModel, mCommonChargesRequestModelList.size() + 1);
            }
        }, new AddChargesItemDialog.OnUnitSelectedListener() {
            @Override
            public String getQuantityToReplace(ChargesUnitModel unitModel, String currentQuantity) {
                return currentQuantity;
            }

            @Override
            public String getRateToRepalce(ChargesUnitModel unitModel, String currentRate) {
                return currentRate;
            }
        }*/);
    }


    private void showEditChargesDialogue(final CommonChargesRequestModel commonChargesRequestModel, final int adapterPosition) {

        mSelectedChargesRequestModel = commonChargesRequestModel;
        mSelectedAdapterPosition = adapterPosition;

        AddChargesItemDialog.showForEdit(getSupportFragmentManager(), mUnitModelList, false, commonChargesRequestModel.getQuantity(), commonChargesRequestModel.getUnit(), commonChargesRequestModel.getRate(), null, true, moveTypeId, Constants.MoveInfoModelTypes.CRATING_MODEL_TEXT, commonChargesRequestModel.getSelectRate(), commonChargesRequestModel.getTaxable());

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

        viewModel.deleteBolChargesItems(subDomain, userId, opportunityId, jobId, id, CRATING_CHARGE_BOL_LIST_MODEL_NAME, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                mCommonChargesRequestModelList.remove(adapterPosition);
                adapter.notifyItemRemoved(adapterPosition);

                mCratingChargesChanged = true;
                resetToolBar();
                setBottomHeader();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(CratingChargesBolActivity.this);
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
        String modelName = Constants.BOLChargesModels.CRATING_CHARGE_SAVE_MODEL;
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();

        List<CommonChargesRequestModel> list = new ArrayList<>();
        list.add(commonChargesRequestModel);

        showProgress();
        viewModel.saveBOLCharges(subDomain, opportunityId, userId, modelName, jobId, list, new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                hideProgress();
                commonChargesRequestModel.setId(response.getData());
                adapter.addToHomeList(commonChargesRequestModel, adapterPosition);

                /*if (!editMenuItem.isVisible()) {
                    editMenuItem.setVisible(true);
                }*/

                mCratingChargesChanged = true;

                if (editMenuItem != null) {
                    editMenuItem.setVisible(!mCommonChargesRequestModelList.isEmpty());
                    adapter.hideEditOption();
                    cancelEditMenuItem.setVisible(false);
                }

                setBottomHeader();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(CratingChargesBolActivity.this);
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
        BillOfLadingActivity.setChargesResult(CratingChargesBolActivity.this, getTotalMoveCharges(mCommonChargesRequestModelList), mCratingChargesChanged, discountType, discount);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(IS_EDIT_MENU_ITEM_SELECTED_KEY, mIsEditMenuItemSelected);
        outState.putParcelable(SELECTED_CHARGES_REQUEST_MODEL_KEY, mSelectedChargesRequestModel);
        outState.putInt(SELECTED_ADAPTER_POSITION_KEY, mSelectedAdapterPosition);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onNewItemSubmitted(String description, double quantity, ChargesUnitModel unitModel, double rate, String days, int cId, String selectRate, String taxable) {

        CommonChargesRequestModel commonChargesRequestModel = new CommonChargesRequestModel();

        /**cancelEditMenuItem.isVisible() is used here because we need it to show edit option
         *when other items are showing edit option. cancelEditMenuItem is only visible that time.
         */
        commonChargesRequestModel.setFieldsForCratingCharges("", mBolFinalChargeId, description, quantity + "", unitModel.getUnitName(), unitModel.getUnitId(), rate + "", ((quantity / unitModel.getUnitNum()) * rate) + "", null, cancelEditMenuItem.isVisible(), cId, selectRate);
        callSaveMoveChargesItem(commonChargesRequestModel, mCommonChargesRequestModelList.size() + 1);
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
    public String getQuantityToReplace(ChargesUnitModel unitModel, String currentQuantity) {
        return currentQuantity;
    }

    @Override
    public String getRateToRepalce(ChargesUnitModel unitModel, String currentRate) {
        return currentRate;
    }

    private void setBottomHeader() {
        double subTotal = 0.0;
        double discountValue = 0.0;
        for (CommonChargesRequestModel commonChargesRequestModel :
                mCommonChargesRequestModelList) {
            subTotal = subTotal + Double.parseDouble(commonChargesRequestModel.getAmount());
        }
        binding.txtSubTotal.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(subTotal)));

        if (discountType == Constants.NumValueTypes.NUM_VALUE_TYPE_PERCENTAGE) {
            binding.txtDiscount.setText(String.format(getString(R.string.percentage_value), Util.getGeneralFormattedDecimalString(discount)));
            discountValue = ((subTotal * discount) / 100);
        } else {
            discountValue = discount;
            binding.txtDiscount.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(discount)));
        }
        double total = subTotal - discountValue;
        binding.txtTotal.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(total)));
    }


    private void showEditDiscountDialog() {
        EditDiscountDialog dialogFragment = EditDiscountDialog.newInstance(discountType, discount);
        dialogFragment.setOnEditDiscountSubmitListener(new EditDiscountDialog.OnEditDiscountSubmitListener() {
            @Override
            public void onEditDiscountSubmit(int type, double discountValue) {
                dialogFragment.dismiss();
                callUpdateDiscount(type, discountValue);
            }
        });
        Util.showDialog(getSupportFragmentManager(), dialogFragment);
    }


    private void callUpdateDiscount(int type, double discountValue) {
        showProgress();
        WebServiceManager.getInstance().updateDiscount(MoversPreferences.getInstance(this).getSubdomain(), Constants.CRATING_CHARGE_ID, String.valueOf(discountValue), String.valueOf(type), mBolFinalChargeId, new ResponseListener<BaseResponseModel<Objects>>() {
            @Override
            public void onResponse(BaseResponseModel<Objects> response, String usedUrlKey) {
                hideProgress();
                discountType = type;
                discount = discountValue;
                setBottomHeader();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(CratingChargesBolActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<Objects>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void resetToolBar() {
        adapter.hideEditOption();
        mIsEditMenuItemSelected = false;
        cancelEditMenuItem.setVisible(false);
        editMenuItem.setVisible(!mCommonChargesRequestModelList.isEmpty());
        addMenuItem.setVisible(true);
    }

}
