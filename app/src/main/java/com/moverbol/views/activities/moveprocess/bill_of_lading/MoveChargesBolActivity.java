package com.moverbol.views.activities.moveprocess.bill_of_lading;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.bolAdapters.MoveChargesBolAdapter;
import com.moverbol.adapters.bolAdapters.MoveChargesBolWorkerAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.AddChargesItemDialog;
import com.moverbol.databinding.MoveChargesBinding;
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

/**
 * Created by AkashM on 20/2/18.
 */

public class MoveChargesBolActivity extends BaseAppCompactActivity implements
        AddChargesItemDialog.OnNewItemSubmittedListener,
        AddChargesItemDialog.OnEditedItemSubmittedListener,
        AddChargesItemDialog.OnUnitSelectedListener {

    private static final String MOVE_CHARGE_BOL_LIST_MODEL_NAME = "bol_movecharge";
    private static final String EXTRA_TEMP_VOLUME = "extra_temp_volume";
    private static final String EXTRA_TEMP_MILES = "extra_temp_miles";
    private static final String EXTRA_TEMP_WEIGHT = "extra_temp_weight";
    private static final String IS_EDIT_MENU_ITEM_SELECTED_KEY = "is_edit_menu_item_selected";
    private MoveChargesBinding binding;
    private BillOfLadingViewModel viewModel;
    private MoveChargesBolAdapter mMoveChargesBolAdapter;
    private MoveChargesBolWorkerAdapter moveChargesBolWorkerAdapter;
    private String mJobId;

    private ArrayList<CommonChargesRequestModel> mCommonChargesList;
    private ArrayList<ChargesUnitModel> mUnitModelList;

    //    private MoveChargesManualPricingPojo mManualPricingPojo;
//    private MoveChargesAutoPricingPojo mAutoPricingPojo;
    private CommonChargesRequestModel mMoveChargesCalculated;

    private MenuItem addMenuItem;
    private MenuItem editMenuItem;
    private MenuItem cancelEditMenuItem;
    private MenuItem submitMenuItem;

    private double temp_Volume;
    private double temp_Weight;
    private double temp_Miles;
    private double discount = 0.0;
    private int discountType = 1;

    private boolean moveChargesChanged;
    private String mBolFinalChargeId;
    private boolean calculatedMoveChargeAddedToList;
    private boolean mIsEditMenuItemSelected;

    private static final String SELECTED_CHARGES_REQUEST_MODEL_KEY = "selected_charges_request_model";
    private static final String SELECTED_ADAPTER_POSITION_KEY = "selected_adapter_position_key";
    private CommonChargesRequestModel mSelectedChargesRequestModel = null;
    private int mSelectedAdapterPosition = -1;
    private String moveTypeId = "0";

    public static void startForResult(Activity context, int requestCode, /*CommonChargesRequestModel moveChargesCalculated,*/
                                      boolean moveChargesChanged, String bolFinalChargeId,
                                      String moveChargesPriceType, double temp_Volume, double temp_Weight,
                                      double temp_Miles, String moveTypeId, double moveChargeDiscountValue, int moveChargeDiscountType) {
        Intent starter = new Intent(context, MoveChargesBolActivity.class);
//        starter.putExtra(Constants.BOLStringConstants.MOVE_CHARGE_CALCULATED, moveChargesCalculated);
        starter.putExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, moveChargesChanged);
        starter.putExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID, bolFinalChargeId);
        starter.putExtra(Constants.EXTRA_MOVE_CHARGES_PRICE_TYPE_KEY, moveChargesPriceType);
        starter.putExtra(EXTRA_TEMP_VOLUME, temp_Volume);
        starter.putExtra(EXTRA_TEMP_WEIGHT, temp_Weight);
        starter.putExtra(EXTRA_TEMP_MILES, temp_Miles);
        starter.putExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID, moveTypeId);
        starter.putExtra(Constants.DISCOUNT, moveChargeDiscountValue);
        starter.putExtra(Constants.DISCOUNT_TYPE, moveChargeDiscountType);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (viewModel.commonChargesRequestModelLive.getValue() == null) {
            getMoveChargesBolChargesList();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setIntentData();
        initialisation();
        setActionListeners();
        setViewModelObserver();

        if (savedInstanceState != null) {
            mIsEditMenuItemSelected = savedInstanceState.getBoolean(IS_EDIT_MENU_ITEM_SELECTED_KEY);

            mSelectedChargesRequestModel = savedInstanceState.getParcelable(SELECTED_CHARGES_REQUEST_MODEL_KEY);
            mSelectedAdapterPosition = savedInstanceState.getInt(SELECTED_ADAPTER_POSITION_KEY);
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
        } else {
            editMenuItem.setVisible(!mCommonChargesList.isEmpty());
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
                mMoveChargesBolAdapter.showEditOption();
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

    @SuppressLint("SourceLockedOrientationActivity")
    private void initialisation() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_move_charges);
        setToolbar(binding.toolbar.toolbar, getString(R.string.move_charges), R.drawable.ic_arrow_back_white_24dp);

        viewModel = new ViewModelProvider(this).get(BillOfLadingViewModel.class);

        String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();

        String txtRateText = getText(R.string.rate) + "(" + currencySymbol + ")";
        String txtAmtText = getText(R.string.amt) + "(" + currencySymbol + ")";

//        if (getIntent().hasExtra(Constants.EXTRA_JOB_ID_KEY))
        mJobId = MoversPreferences.getInstance(this).getCurrentJobId();

        binding.includeViewManualPricing.txtRate.setText(txtRateText);
        binding.includeViewManualPricing.txtAmt.setText(txtAmtText);

        mCommonChargesList = new ArrayList<>();
        mUnitModelList = new ArrayList<>();

        mMoveChargesBolAdapter = new MoveChargesBolAdapter();
        moveChargesBolWorkerAdapter = new MoveChargesBolWorkerAdapter();
        binding.rvMoveWorker.setAdapter(moveChargesBolWorkerAdapter);

        //if start clock and job is local so show worker and equipment recyclerView
        binding.setIsShowWorker(!MoversPreferences.getInstance(this).getCurrentActivityId(mJobId).isEmpty() && moveTypeId.equalsIgnoreCase(Constants.MoveTypeIds.MOVE_TYPE_LOCAL));

        if (binding.getIsShowWorker()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        binding.setIsBolStart(MoversPreferences.getInstance(this).getBolStarted(mJobId));
    }

    private void setIntentData() {
//        if (getIntent().hasExtra(Constants.EXTRA_MOVE_CHARGES_PRICE_TYPE_KEY)) {
        if (getIntent().hasExtra(Constants.BOLStringConstants.MOVE_CHARGE_CALCULATED)) {
            mMoveChargesCalculated = getIntent().getParcelableExtra(Constants.BOLStringConstants.MOVE_CHARGE_CALCULATED);
        }
//        }

        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED)) {
            moveChargesChanged = getIntent().getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
        }

        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID)) {
            mBolFinalChargeId = getIntent().getStringExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID);
        }

        if (getIntent().hasExtra(EXTRA_TEMP_VOLUME)) {
            temp_Volume = getIntent().getDoubleExtra(EXTRA_TEMP_VOLUME, 0);
        }

        if (getIntent().hasExtra(EXTRA_TEMP_VOLUME)) {
            temp_Weight = getIntent().getDoubleExtra(EXTRA_TEMP_WEIGHT, 0);
        }

        if (getIntent().hasExtra(EXTRA_TEMP_VOLUME)) {
            temp_Miles = getIntent().getDoubleExtra(EXTRA_TEMP_MILES, 0);
        }

       /* if (getIntent().hasExtra(Constants.EXTRA_MOVE_CHARGES_PRICE_TYPE_KEY)) {

            String moveChargesPriceType = getIntent().getStringExtra(Constants.EXTRA_MOVE_CHARGES_PRICE_TYPE_KEY);
            mIsMoveTypeAutoPricing =
                    (TextUtils.equals(moveChargesPriceType, Constants.MoveChargesPriceTypes.MOVE_CHARGES_AUTO_PRICING_INDEX + "") ||
                            TextUtils.equals(moveChargesPriceType, Constants.MoveChargesPriceTypes.MOVE_CHARGES_SPOT_PRICING_INDEX + ""));
        }*/

        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID)) {
            moveTypeId = getIntent().getStringExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID);
        }

        if (getIntent().hasExtra(Constants.DISCOUNT)) {
            discount = getIntent().getDoubleExtra(Constants.DISCOUNT, 0.0);
        }

        if (getIntent().hasExtra(Constants.DISCOUNT_TYPE)) {
            discountType = getIntent().getIntExtra(Constants.DISCOUNT_TYPE, 1);
        }
    }

    private void setActionListeners() {

        mMoveChargesBolAdapter.setOnEditClickedListener(new MoveChargesBolAdapter.OnEditClickedListener() {
            @Override
            public void onEditClicked(CommonChargesRequestModel commonChargesRequestModel, int adapterPosition) {
                showEditChargesDialogue(commonChargesRequestModel, adapterPosition);
            }
        });

        binding.imgEditDiscount.setOnClickListener(v -> showEditDiscountDialog());
    }

    private void setViewModelObserver() {

        viewModel.commonChargesResponseModelLive.observe(this, new Observer<CommonChargesResponseModel>() {
            @Override
            public void onChanged(@Nullable CommonChargesResponseModel commonChargesResponseModel) {
                if (commonChargesResponseModel != null) {
                    if (commonChargesResponseModel.getCommonChargesRequestModelArrayList() != null) {
                        /**
                         * If move is autopricing then we do not want to show estimated move charges.
                         * We only want to show calculated move charges which we are adding later in
                         * this function.
                         * (Consider this comment as commented. Not relevant now.)
                         */
//                        if(!mIsMoveTypeAutoPricing){
                        mCommonChargesList = commonChargesResponseModel.getCommonChargesRequestModelArrayList();
//                        }
                    }
                    if (commonChargesResponseModel.getUnitModelList() != null) {
                        mUnitModelList = commonChargesResponseModel.getUnitModelList();
                    }
                }

                if (mMoveChargesCalculated != null) {

                    if (mCommonChargesList == null) {
                        mCommonChargesList = new ArrayList<>();
                    } else if (mCommonChargesList.size() > 0) {
                        if (TextUtils.equals(mCommonChargesList.get(0).getUnit(), mMoveChargesCalculated.getUnit()) && TextUtils.equals(mCommonChargesList.get(0).getDescription(), mMoveChargesCalculated.getDescription())) {
                            mCommonChargesList.remove(0);
                        }
                    }

                    mCommonChargesList.add(0, mMoveChargesCalculated);
                    calculatedMoveChargeAddedToList = true;
                }

                mMoveChargesBolAdapter.setChargesList(mCommonChargesList);
                moveChargesBolWorkerAdapter.setChargesList(mCommonChargesList);
                binding.setAdapter(mMoveChargesBolAdapter);


                if (editMenuItem != null) {
                    resetToolBar();
                }

                setBottomHeader();
            }
        });


    }


    private void setBottomHeader() {
        double subTotal = 0.0;
        double discountValue = 0.0;
        for (CommonChargesRequestModel commonChargesRequestModel :
                mCommonChargesList) {
            subTotal = subTotal + Double.parseDouble(commonChargesRequestModel.getAmount());
        }
        binding.txtSubTotal.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(subTotal)));

        if (discountType == 2) {
            binding.txtDiscount.setText(String.format(getString(R.string.percentage_value), Util.getGeneralFormattedDecimalString(discount)));
            discountValue = ((subTotal * discount) / 100);
        } else {
            discountValue = discount;
            binding.txtDiscount.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(discount)));
        }
        double total = subTotal - discountValue;
        if (total < 0) {
            total = 0;
        }
        binding.txtTotal.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(total)));
    }


    private void getMoveChargesBolChargesList() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        if (!getIntent().hasExtra(Constants.EXTRA_MOVE_CHARGES_PRICE_TYPE_KEY)) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        viewModel.getBolChargesList(subdomain, userId, opportunityId, MOVE_CHARGE_BOL_LIST_MODEL_NAME, mJobId, new ResponseListener<BaseResponseModel<CommonChargesResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<CommonChargesResponseModel> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MoveChargesBolActivity.this);
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

        AddChargesItemDialog.showForAdd(getSupportFragmentManager(), mUnitModelList, moveTypeId, Constants.MoveInfoModelTypes.MOVE_CHARGES_MODEL_TEXT, false/*, new AddChargesItemDialog.OnNewItemSubmittedListener() {
            @Override
            public void onNewItemSubmitted(String description, double quantity, String unit, String unitId, double rate, String days) {

                CommonChargesRequestModel commonChargesRequestModel = new CommonChargesRequestModel();

                *//**cancelEditMenuItem.isVisible() is used here because we need it to show edit option
         *when other items are showing edit option. cancelEditMenuItem is only visible that time.
         *//*
                commonChargesRequestModel.setFieldsForMoveCharges("", mBolFinalChargeId, description, quantity + "", unit, unitId, rate + "", (quantity * rate) + "", cancelEditMenuItem.isVisible());
                callSaveMoveChargesItem(commonChargesRequestModel,mCommonChargesList.size() + 1);
            }
        }, new AddChargesItemDialog.OnUnitSelectedListener() {
            @Override
            public String getQuantityToReplace(ChargesUnitModel unitModel, String currentQuantity) {
                return getRequiredQuantyty(unitModel.getUnitName(), currentQuantity);
            }

            @Override
            public String getRateToRepalce(ChargesUnitModel unitModel, String currentRate) {
                return currentRate;
            }
        }*/);
    }


    private void showEditChargesDialogue(final Object object, final int adapterPosition) {

        mSelectedChargesRequestModel = (CommonChargesRequestModel) object;
        mSelectedAdapterPosition = adapterPosition;

        if (object instanceof CommonChargesRequestModel) {
            CommonChargesRequestModel commonChargesRequestModel = (CommonChargesRequestModel) object;

            boolean shouldShowDeleteOption = (adapterPosition >= 0);

            AddChargesItemDialog.showForEdit(getSupportFragmentManager(), mUnitModelList, false, commonChargesRequestModel.getQuantity(), commonChargesRequestModel.getUnit(), commonChargesRequestModel.getRate(), null, shouldShowDeleteOption, moveTypeId, Constants.MoveInfoModelTypes.MOVE_CHARGES_MODEL_TEXT, commonChargesRequestModel.getSelectRate(), commonChargesRequestModel.getTaxable());

        }
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

        showProgress();

        viewModel.deleteBolChargesItems(subDomain, userId, opportunityId, mJobId, id, MOVE_CHARGE_BOL_LIST_MODEL_NAME, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                mCommonChargesList.remove(adapterPosition);
                mMoveChargesBolAdapter.notifyItemRemoved(adapterPosition);

                moveChargesChanged = true;

                if (editMenuItem != null) {
                    resetToolBar();
                }
                setBottomHeader();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MoveChargesBolActivity.this);
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
        String modelName = Constants.BOLChargesModels.MOVE_CHARGE_SAVE_MODEL;


        List<CommonChargesRequestModel> list = new ArrayList<>();
        list.add(commonChargesRequestModel);

        showProgress();
        viewModel.saveBOLCharges(subDomain, opportunityId, userId, modelName, mJobId, list, new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                hideProgress();
                commonChargesRequestModel.setId(response.getData());
                mMoveChargesBolAdapter.addToHomeList(commonChargesRequestModel, adapterPosition);

                moveChargesChanged = true;

              /*  if (editMenuItem != null) {
                    editMenuItem.setVisible(!mCommonChargesList.isEmpty());
                }*/


                setBottomHeader();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(MoveChargesBolActivity.this);
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


    private double getTotalMoveCharges(ArrayList list) {

        double sumToReturn = 0.00;

        if (list == null || list.isEmpty()) {
            return sumToReturn;
        }

        if (list.get(0) instanceof CommonChargesRequestModel) {
            for (int i = 0; i < ((ArrayList<CommonChargesRequestModel>) list).size(); i++) {
                sumToReturn = sumToReturn + Util.getDoubleFromString(((ArrayList<CommonChargesRequestModel>) list).get(i).getAmount());
            }
        }

        return sumToReturn;
    }

    private String getRequiredQuantyty(String unitName, String currentQuantity) {
        if (unitName.equalsIgnoreCase(Constants.SomeUnits.CBM)
                || unitName.equalsIgnoreCase(Constants.SomeUnits.CFT)) {
            return temp_Volume + "";
        } else if (unitName.equalsIgnoreCase(Constants.SomeUnits.KGS)
                || unitName.equalsIgnoreCase(Constants.SomeUnits.LBS)) {
            return temp_Weight + "";
        } else if (unitName.equalsIgnoreCase(Constants.SomeUnits.CWT)) {
            return (temp_Weight / 100) + "";
        } else if (unitName.equalsIgnoreCase(Constants.SomeUnits.MILES)) {
            return temp_Miles + "";
        }
        return currentQuantity;
    }


    @Override
    public void onBackPressed() {
        if (calculatedMoveChargeAddedToList && moveChargesChanged && !mCommonChargesList.isEmpty()) {
            mCommonChargesList.get(0).setShowEditOption(false);
            BillOfLadingActivity.setChargesResult(MoveChargesBolActivity.this, mCommonChargesList.get(0), getTotalMoveCharges(mCommonChargesList), moveChargesChanged, discountType, discount);
        } else {
            BillOfLadingActivity.setChargesResult(MoveChargesBolActivity.this, getTotalMoveCharges(mCommonChargesList), moveChargesChanged, discountType, discount);
        }

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
        commonChargesRequestModel.setFieldsForMoveCharges("", mBolFinalChargeId, description, quantity + "", unitModel.getUnitName(), unitModel.getUnitId(), rate + "", ((quantity / unitModel.getUnitNum()) * rate) + "", cancelEditMenuItem.isVisible(), cId, selectRate);
        callSaveMoveChargesItem(commonChargesRequestModel, mCommonChargesList.size() + 1);
    }


    @Override
    public void onEditedItemSubmitted(double quantity, ChargesUnitModel unitModel, double rate, String days, String taxable) {

        mSelectedChargesRequestModel.setQuantity(quantity + "");
        mSelectedChargesRequestModel.setUnitId(unitModel.getUnitId());
        mSelectedChargesRequestModel.setUnit(unitModel.getUnitName());
        mSelectedChargesRequestModel.setRate(rate + "");

        mSelectedChargesRequestModel.setAmount((rate * (quantity / unitModel.getUnitNum())) + "");

                    /*if (mMoveChargesCalculated != null && adapterPosition == 0) {
                        mMoveChargesBolAdapter.addToHomeList(commonChargesRequestModel, adapterPosition);
                        if (!editMenuItem.isVisible()) {
                            editMenuItem.setVisible(true);
                        }
                        moveChargesChanged = true;
                    } else {*/
        callSaveMoveChargesItem(mSelectedChargesRequestModel, mSelectedAdapterPosition);
//                    }


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
        return getRequiredQuantyty(unitModel.getUnitName(), currentQuantity);
    }

    @Override
    public String getRateToRepalce(ChargesUnitModel unitModel, String currentRate) {
        return currentRate;
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
        WebServiceManager.getInstance().updateDiscount(MoversPreferences.getInstance(this).getSubdomain(), Constants.MOVE_CHARGE_ID, String.valueOf(discountValue), String.valueOf(type), mBolFinalChargeId, new ResponseListener<BaseResponseModel<Objects>>() {
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
                    Util.showLoginErrorDialog(MoveChargesBolActivity.this);
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
        mMoveChargesBolAdapter.hideEditOption();
        mIsEditMenuItemSelected = false;
        cancelEditMenuItem.setVisible(false);
        editMenuItem.setVisible(!mCommonChargesList.isEmpty());
        addMenuItem.setVisible(true);
    }
}
