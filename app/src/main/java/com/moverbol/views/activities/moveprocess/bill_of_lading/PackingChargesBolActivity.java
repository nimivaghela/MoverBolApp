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
import com.moverbol.adapters.bolAdapters.PackingChargesBolAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.AddChargesItemDialog;
import com.moverbol.databinding.PackingChargesBinding;
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
 * Created by AkashM on 5/3/18.
 */

public class PackingChargesBolActivity extends BaseAppCompactActivity implements
        AddChargesItemDialog.OnNewItemSubmittedListener,
        AddChargesItemDialog.OnEditedItemSubmittedListener,
        AddChargesItemDialog.OnUnitSelectedListener {

    private static final String PACKING_CHARGE_BOL_LIST_MODEL_NAME = "bol_packingcharge";
    private static final String IS_EDIT_MENU_ITEM_SELECTED_KEY = "is_edit_menu_item_selected";
    private static final String SELECTED_CHARGES_REQUEST_MODEL_KEY = "selected_charges_request_model";
    private static final String SELECTED_ADAPTER_POSITION_KEY = "selected_adapter_position_key";
    private PackingChargesBinding binding;
    private PackingChargesBolAdapter adapter;
    private BillOfLadingViewModel viewModel;
    private MenuItem addMenuItem;
    private MenuItem editMenuItem;
    private MenuItem cancelEditMenuItem;
    private boolean packingChargesChanged;
    private String mBolFinalChargeId;
    private String moveTypeId;
    private List<CommonChargesRequestModel> mCommonChargesRequestModelList;
    private ArrayList<ChargesUnitModel> mUnitModelList;
    private boolean mIsEditMenuItemSelected;
    private CommonChargesRequestModel mSelectedChargesRequestModel = null;
    private int mSelectedAdapterPosition = -1;
    private double discount = 0.0;
    private int discountType = Constants.NumValueTypes.NUM_VALUE_TYPE_AMOUNT;
    private double salesTax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialisation();
        setActionListeners();
        setIntentData();

        setLiveDataObservers();

        setToolbar(binding.toolbar.toolbar, getString(R.string.packing_material_charges), R.drawable.ic_arrow_back_white_24dp);


        //Set currency symbol on titles
        String currencySymbol = MoversPreferences.getInstance(this).getCurrencySymbol();
        String txtRateText = getText(R.string.rate) + "(" + currencySymbol + ")";
        String txtAmtText = getText(R.string.amt) + "(" + currencySymbol + ")";
        binding.includeView.txtRate.setText(txtRateText);
        binding.includeView.txtAmt.setText(txtAmtText);

        if (savedInstanceState != null) {
            mIsEditMenuItemSelected = savedInstanceState.getBoolean(IS_EDIT_MENU_ITEM_SELECTED_KEY);

            mSelectedChargesRequestModel = savedInstanceState.getParcelable(SELECTED_CHARGES_REQUEST_MODEL_KEY);
            mSelectedAdapterPosition = savedInstanceState.getInt(SELECTED_ADAPTER_POSITION_KEY);
        }

    }

    private void initialisation() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_packing_charges);
        viewModel = new ViewModelProvider(this).get(BillOfLadingViewModel.class);

        adapter = new PackingChargesBolAdapter();
        binding.setAdapter(adapter);
//        mPackingChargesPojosList = new ArrayList<>();
        mCommonChargesRequestModelList = new ArrayList<>();
        mUnitModelList = new ArrayList<>();
        adapter.setChargesList(mCommonChargesRequestModelList);
        binding.setIsBolStarted(MoversPreferences.getInstance(this).getBolStarted(MoversPreferences.getInstance(this).getCurrentJobId()));

    }

    private void setActionListeners() {
        /*adapter.setOnEditClickedListener(new PackingChargesAdapter.OnEditClickedListener() {
            @Override
            public void onEditClicked(PackingChargesPojo packingChargesPojo, int adapterPosition) {
                showEditChargesDialogue(packingChargesPojo, adapterPosition);
            }
        });*/

        adapter.setOnEditClickedListener(new PackingChargesBolAdapter.OnEditClickedListener() {
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
        /*if (getIntent().hasExtra(Constants.BOLStringConstants.PACKING_CHARGES_LIST)) {
            mPackingChargesPojosList = getIntent().getParcelableArrayListExtra(Constants.BOLStringConstants.PACKING_CHARGES_LIST);
        }
*/
        if (getIntent().hasExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED)) {
            packingChargesChanged = getIntent().getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
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
        /*viewModel.packingChargesPojoListLive.observe(this, new Observer<ArrayList<PackingChargesPojo>>() {
            @Override
            public void onChanged(@Nullable ArrayList<PackingChargesPojo> packingChargesPojos) {
                mPackingChargesPojosList = packingChargesPojos;
                adapter.setChargesList(mPackingChargesPojosList);
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

                    adapter.setChargesList(mCommonChargesRequestModelList);

                    if (editMenuItem != null) {
                        resetToolBar();
                    }

                    salesTax = commonChargesResponseModel.getPackingSalesTax();

                    setBottomHeader();
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (viewModel.commonChargesRequestModelLive.getValue() == null) {
            /*if(mPackingChargesPojosList==null) {
                callGetPackingChargesDetails();
            }else {
                viewModel.packingChargesPojoListLive.postValue(mPackingChargesPojosList);
            }*/
            callGetPackingChargesBolChargesList();
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


    private void callGetPackingChargesBolChargesList() {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        viewModel.getBolChargesList(subdomain, userId, opportunityId, PACKING_CHARGE_BOL_LIST_MODEL_NAME, jobId, new ResponseListener<BaseResponseModel<CommonChargesResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<CommonChargesResponseModel> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PackingChargesBolActivity.this);
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

        AddChargesItemDialog.showForAdd(getSupportFragmentManager(), mUnitModelList, moveTypeId, Constants.MoveInfoModelTypes.PACKING_MODEL_TEXT, false/*, new AddChargesItemDialog.OnNewItemSubmittedListener() {
            @Override
            public void onNewItemSubmitted(String description, double quantity, String unit, String unitId, double rate, String days) {

                CommonChargesRequestModel commonChargesRequestModel = new CommonChargesRequestModel();

                *//**cancelEditMenuItem.isVisible() is used here because we need it to show edit option
         *when other items are showing edit option. cancelEditMenuItem is only visible that time.
         *//*
                commonChargesRequestModel.setFieldsForPackingCharges("", mBolFinalChargeId, description, quantity + "", unit, unitId, rate + "", (quantity * rate) + "", cancelEditMenuItem.isVisible());
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

        AddChargesItemDialog.showForEdit(getSupportFragmentManager(), mUnitModelList, false, commonChargesRequestModel.getQuantity(), commonChargesRequestModel.getUnit(), commonChargesRequestModel.getRate(), null, true, moveTypeId, Constants.MoveInfoModelTypes.PACKING_MODEL_TEXT, commonChargesRequestModel.getSelectRate(), commonChargesRequestModel.getTaxable());
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

        viewModel.deleteBolChargesItems(subDomain, userId, opportunityId, jobId, id, PACKING_CHARGE_BOL_LIST_MODEL_NAME, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
              /*  mCommonChargesRequestModelList.remove(adapterPosition);
                adapter.notifyItemRemoved(adapterPosition);*/

                callGetPackingChargesBolChargesList();

                packingChargesChanged = true;

                if (editMenuItem != null) {
                    resetToolBar();
                }


                setBottomHeader();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PackingChargesBolActivity.this);
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
        String modelName = Constants.BOLChargesModels.PACKING_CHARGE_SAVE_MODEL;


        List<CommonChargesRequestModel> list = new ArrayList<>();
        list.add(commonChargesRequestModel);

        showProgress();
        viewModel.saveBOLCharges(subDomain, opportunityId, userId, modelName, jobId, list, new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                hideProgress();
                commonChargesRequestModel.setId(response.getData());
//                commonChargesRequestModel.setUnit(unit);
                // adapter.addToHomeList(commonChargesRequestModel, adapterPosition);

                callGetPackingChargesBolChargesList();

                if (editMenuItem != null) {
                    resetToolBar();
                }
                packingChargesChanged = true;
                setBottomHeader();

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PackingChargesBolActivity.this);
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


    private double getTotalMoveCharges(List<CommonChargesRequestModel> list) {
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
//        BillOfLadingActivity.setChargesResult(PackingChargesBolActivity.this, mPackingChargesPojosList, getTotalMoveCharges(mPackingChargesPojosList), packingChargesChanged);
        BillOfLadingActivity.setChargesResult(PackingChargesBolActivity.this, getTotalMoveCharges(mCommonChargesRequestModelList), packingChargesChanged, discountType, discount);
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
        commonChargesRequestModel.setFieldsForPackingCharges("", mBolFinalChargeId, description, quantity + "", unitModel.getUnitName(), unitModel.getUnitId(), rate + "", ((quantity / unitModel.getUnitNum()) * rate) + "", cancelEditMenuItem.isVisible(), cId, selectRate, taxable);
        callSaveMoveChargesItem(commonChargesRequestModel, mCommonChargesRequestModelList.size() + 1);
    }


    @Override
    public void onEditedItemSubmitted(double quantity, ChargesUnitModel unitModel, double rate, String days, String taxable) {

        mSelectedChargesRequestModel.setQuantity(quantity + "");
        mSelectedChargesRequestModel.setUnit(unitModel.getUnitName());
        mSelectedChargesRequestModel.setUnitId(unitModel.getUnitId());
        mSelectedChargesRequestModel.setRate(rate + "");

        mSelectedChargesRequestModel.setAmount((rate * (quantity / unitModel.getUnitNum())) + "");
        mSelectedChargesRequestModel.setTaxable(taxable);

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


    /*    double totalAmountForSalesTax = 0;
        for (CommonChargesRequestModel commonChargesRequestModel :
                mCommonChargesRequestModelList) {
            if (commonChargesRequestModel.getTaxable().equalsIgnoreCase(Constants.TRUE)) {
                totalAmountForSalesTax = totalAmountForSalesTax + Double.parseDouble(commonChargesRequestModel.getAmount());
            }
        }*/


        binding.txtSalesTax.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.getGeneralFormattedDecimalString(salesTax)));

        double total = (subTotal - discountValue) + salesTax;
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
        WebServiceManager.getInstance().updateDiscount(MoversPreferences.getInstance(this).getSubdomain(), Constants.PACKING_MATERIAL_CHARGE_ID, String.valueOf(discountValue), String.valueOf(type), mBolFinalChargeId, new ResponseListener<BaseResponseModel<Objects>>() {
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
                    Util.showLoginErrorDialog(PackingChargesBolActivity.this);
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

