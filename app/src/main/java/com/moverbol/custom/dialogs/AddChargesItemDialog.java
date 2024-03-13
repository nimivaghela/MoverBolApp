package com.moverbol.custom.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.CustomSpinnerBolAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.databinding.AddChargesItemBinding;
import com.moverbol.model.billOfLading.AddChargesModel;
import com.moverbol.model.billOfLading.CategoryModel;
import com.moverbol.model.billOfLading.ChargeRateTypeModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.ChargesUnitModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.BillOfLadingViewModel;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by AkashM on 5/3/18.
 */

public class AddChargesItemDialog extends BaseDialogFragment {


    private static final String FRAGMENT_TRANSACTION_TAG = "dialog";
    private static final String SELECT_UNIT_HINT = "--Select Unit--";
    private AddChargesItemBinding binding;

    private OnNewItemSubmittedListenerForStorageRecurring newItemSubmittedListenerForStorageRecurring;
    private OnNewItemSubmittedListener newItemSubmittedListener;
    private OnEditedItemSubmittedListenerForStorageRecurring editedItemSubmittedListenerForStorageRecurring;
    private OnEditedItemSubmittedListener editedItemSubmittedListener;
    private OnUnitSelectedListener onUnitSelectedListener;


    private BillOfLadingViewModel viewModel;
    private AddChargesModel mAddChargesModel;
    private String moveTypeId = "0";
    private String model = "";
    private String taxable = "0";
    private ChargeRateTypeModel selectedChargeRateTypeModel = new ChargeRateTypeModel();
    private CategoryModel selectedCategoryModel = new CategoryModel();
    private ChargesUnitModel chargesUnitModel;


    public static void showForAdd(FragmentManager fragmentManager, @NonNull ArrayList<ChargesUnitModel> unitModelList, String moveTypeId, String model, boolean showDays/*, OnNewItemSubmittedListener onNewItemSubmittedListener, OnUnitSelectedListener onUnitSelectedListener*/) {
        AddChargesItemDialog addChargesItemDialog = new AddChargesItemDialog();
        AddChargesModel addChargesModel = new AddChargesModel();
        addChargesModel.showDays = showDays;


        if (unitModelList != null && unitModelList.size() > 0) {
            addChargesModel.unitModelList = unitModelList;
            if (!TextUtils.equals(unitModelList.get(0).getUnitName(), SELECT_UNIT_HINT)) {
                ChargesUnitModel selectionHintModel = new ChargesUnitModel();
                selectionHintModel.setUnitName(SELECT_UNIT_HINT);
                addChargesModel.unitModelList.add(0, selectionHintModel);
            }
        }

        /*addChargesModel.setNewItemSubmittedListener(onNewItemSubmittedListener);
        addChargesModel.setOnUnitSelectedListener(onUnitSelectedListener);*/
        addChargesItemDialog.mAddChargesModel = addChargesModel;
        addChargesItemDialog.moveTypeId = moveTypeId;
        addChargesItemDialog.model = model;

        addChargesItemDialog.show(fragmentManager, FRAGMENT_TRANSACTION_TAG);
    }


    public static void showForAddForStorageRecurring(FragmentManager fragmentManager, @NonNull ArrayList<ChargesUnitModel> unitModelList, String moveTypeId, String model) {
        AddChargesItemDialog addChargesItemDialog = new AddChargesItemDialog();

        AddChargesModel addChargesModel = new AddChargesModel();

        if (unitModelList != null && unitModelList.size() > 0) {
            addChargesModel.unitModelList = unitModelList;
            if (!TextUtils.equals(unitModelList.get(0).getUnitName(), SELECT_UNIT_HINT)) {
                ChargesUnitModel selectionHintModel = new ChargesUnitModel();
                selectionHintModel.setUnitName(SELECT_UNIT_HINT);
                addChargesModel.unitModelList.add(0, selectionHintModel);
            }
        }

        addChargesModel.isForStorageRecurring = true;

        /*addChargesModel.setNewItemSubmittedListenerForStorageRecurring(onNewItemSubmittedListener);
        addChargesModel.setOnUnitSelectedListener(onUnitSelectedListener);*/

        addChargesItemDialog.mAddChargesModel = addChargesModel;
        addChargesItemDialog.moveTypeId = moveTypeId;
        addChargesItemDialog.model = model;

        addChargesItemDialog.show(fragmentManager, FRAGMENT_TRANSACTION_TAG);
    }

    public static void showForEdit(FragmentManager fragmentManager, @NonNull ArrayList<ChargesUnitModel> unitModelList, boolean showDays, String quantity, String unit, String rate, @Nullable String days, boolean shouldShowDeleteButton, String moveTypeId, String model, String selectRate, String taxable) {
        AddChargesItemDialog addChargesItemDialog = new AddChargesItemDialog();

        AddChargesModel addChargesModel = new AddChargesModel();
        addChargesModel.showDays = showDays;

        if (unitModelList != null && unitModelList.size() > 0) {
            addChargesModel.unitModelList = unitModelList;
            if (!TextUtils.equals(unitModelList.get(0).getUnitName(), SELECT_UNIT_HINT)) {
                ChargesUnitModel selectionHintModel = new ChargesUnitModel();
                selectionHintModel.setUnitName(SELECT_UNIT_HINT);
                addChargesModel.unitModelList.add(0, selectionHintModel);
            }
        }

        addChargesModel.isEdit = true;
        addChargesModel.setQuantity(quantity);
        addChargesModel.setDays(days);
        addChargesModel.setRate(rate);
        addChargesModel.setUnit(unit);
        addChargesModel.isFirstTimeUnitSelected = true;
        addChargesModel.shouldShowDeleteButton = shouldShowDeleteButton;


        /*addChargesModel.setEditedItemSubmittedListener(onEditedItemSubmittedListener);
        addChargesModel.setOnUnitSelectedListener(onUnitSelectedListener);*/


        addChargesItemDialog.mAddChargesModel = addChargesModel;
        addChargesItemDialog.moveTypeId = moveTypeId;
        addChargesItemDialog.model = model;
        addChargesItemDialog.taxable = taxable;
        addChargesItemDialog.setSelectRate(selectRate);
        addChargesItemDialog.show(fragmentManager, FRAGMENT_TRANSACTION_TAG);


    }

    public static void showForEditForStorageRecurring(FragmentManager fragmentManager, @NonNull ArrayList<ChargesUnitModel> unitModelList, String quantity, String unit, String rate, String days, String billing, boolean shouldShowDeleteButton, String moveTypeId, String model, String selectedRate) {
        AddChargesItemDialog addChargesItemDialog = new AddChargesItemDialog();

        AddChargesModel addChargesModel = new AddChargesModel();

        addChargesModel.isForStorageRecurring = true;

        addChargesModel.shouldShowDeleteButton = shouldShowDeleteButton;

        if (unitModelList != null && unitModelList.size() > 0) {
            addChargesModel.unitModelList = unitModelList;
            if (!TextUtils.equals(unitModelList.get(0).getUnitName(), SELECT_UNIT_HINT)) {
                ChargesUnitModel selectionHintModel = new ChargesUnitModel();
                selectionHintModel.setUnitName(SELECT_UNIT_HINT);
                addChargesModel.unitModelList.add(0, selectionHintModel);
            }
        }

        addChargesModel.isEdit = true;

        addChargesModel.setQuantity(quantity);
        addChargesModel.setDays(days);
        addChargesModel.setRate(rate);
        addChargesModel.setUnit(unit);
        addChargesModel.setBilling(billing);

        addChargesModel.isFirstTimeUnitSelected = true;


        /*addChargesModel.setEditedItemSubmittedListenerForStorageRecurring(onEditedItemSubmittedListener);
        addChargesModel.setOnUnitSelectedListener(onUnitSelectedListener);*/

        addChargesItemDialog.mAddChargesModel = addChargesModel;
        addChargesItemDialog.moveTypeId = moveTypeId;
        addChargesItemDialog.model = model;
        addChargesItemDialog.setSelectRate(selectedRate);
        addChargesItemDialog.show(fragmentManager, FRAGMENT_TRANSACTION_TAG);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_chrges_items, container, true);

        viewModel = new ViewModelProvider(this).get(BillOfLadingViewModel.class);

        setCancelable(true);

        setActionListeners();
        setViewModelObserver();

        if (viewModel.addChargesModelLive.getValue() == null) {
            viewModel.addChargesModelLive.postValue(mAddChargesModel);
        }


        if (!model.isEmpty()) {
            callCategoryList();
        }

        if (model.equalsIgnoreCase(Constants.MoveInfoModelTypes.PACKING_MODEL_TEXT)) {
            binding.checkTaxable.setChecked(taxable.equalsIgnoreCase("1"));
            binding.checkTaxable.setVisibility(View.VISIBLE);
        }


        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        boolean isNewItemSubmittedListener = context instanceof OnNewItemSubmittedListener;
        boolean isNewItemSubmittedListenerForRecurring = context instanceof OnNewItemSubmittedListenerForStorageRecurring;
        boolean isOnEditItemSubmittedListener = context instanceof OnEditedItemSubmittedListener;
        boolean isOnEditItemSubmittedListenerRecurring = context instanceof OnEditedItemSubmittedListenerForStorageRecurring;
        boolean isUnitSelectedListener = context instanceof OnUnitSelectedListener;

        if (!isNewItemSubmittedListener && !isNewItemSubmittedListenerForRecurring
                && !isOnEditItemSubmittedListener
                && !isOnEditItemSubmittedListenerRecurring
                && !isUnitSelectedListener) {

            throw new ClassCastException(context.toString()
                    + " must implement one of five listeners from AddChargesItemDialog");

        } else {

            if (context instanceof OnNewItemSubmittedListener) {
                newItemSubmittedListener = (OnNewItemSubmittedListener) context;
            }
            if (context instanceof OnNewItemSubmittedListenerForStorageRecurring) {
                newItemSubmittedListenerForStorageRecurring = (OnNewItemSubmittedListenerForStorageRecurring) context;
            }
            if (context instanceof OnEditedItemSubmittedListener) {
                editedItemSubmittedListener = (OnEditedItemSubmittedListener) context;
            }
            if (context instanceof OnEditedItemSubmittedListenerForStorageRecurring) {
                editedItemSubmittedListenerForStorageRecurring = (OnEditedItemSubmittedListenerForStorageRecurring) context;
            }
            if (context instanceof OnUnitSelectedListener) {
                onUnitSelectedListener = (OnUnitSelectedListener) context;
            }
        }
    }

    private void setViewModelObserver() {
        viewModel.addChargesModelLive.observe(getViewLifecycleOwner(), addChargesModel -> {
            if (addChargesModel == null) {
                return;
            }

            mAddChargesModel = addChargesModel;

            binding.setObj(mAddChargesModel);

            if (mAddChargesModel.isEdit) {
                binding.txtTitle.setText(R.string.edit_item);
            }

            binding.edtxtDescription.setText(mAddChargesModel.getDescription());
            binding.edtxtQuantity.setText(mAddChargesModel.getQuantity());

            binding.edtxtRate.setText(mAddChargesModel.getRate());
            binding.edtxtDays.setText(mAddChargesModel.getDays());

            if (mAddChargesModel.unitModelList == null) {
                mAddChargesModel.unitModelList = new ArrayList<>();
            }

            CustomSpinnerBolAdapter<ChargesUnitModel> spinnerBolAdapter = new CustomSpinnerBolAdapter<>(getContext(), R.layout.layout_spinner_item, mAddChargesModel.unitModelList);
            binding.spinUnit.setAdapter(spinnerBolAdapter);
            binding.spinUnit.setSelection(getItemPositionInArray(mAddChargesModel.getUnit(), mAddChargesModel.unitModelList));

            if (mAddChargesModel.isForStorageRecurring) {
                binding.lilBilling.setVisibility(View.VISIBLE);
                ArrayList<String> billingList = new ArrayList<>();
                billingList.add("--Select Billing Type--");
                billingList.add("Daily");
                billingList.add("Monthly");
                CustomSpinnerBolAdapter<String> spinnerBolAdapterBilling = new CustomSpinnerBolAdapter<>(getContext(), R.layout.layout_spinner_item, billingList);
                binding.spinBilling.setAdapter(spinnerBolAdapterBilling);
                if (mAddChargesModel.getBilling() != null) {
                    binding.spinBilling.setSelection(getItemPositionInArrayForBilling(mAddChargesModel.getBilling(), billingList));
                }
            }

        });

        viewModel.chargeRateTypeListLiveData.observe(getViewLifecycleOwner(), this::setSpinnerData);
        viewModel.categoryTypeListLiveData.observe(getViewLifecycleOwner(), this::setCategorySpinner);
    }


    private void setActionListeners() {
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChargesItemDialog.this.dismiss();
            }
        });

        View.OnClickListener onClickListenerSubmit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }

                String description = binding.edtxtDescription.getText().toString();
                double quantity = Double.parseDouble(Util.removeFormatAmount(binding.edtxtQuantity.getText().toString()));
                ChargesUnitModel unitModel = new ChargesUnitModel();
                if (binding.spinUnit.getSelectedItemPosition() != 0) {
                    if (binding.spinUnit.getSelectedItem() instanceof ChargesUnitModel)
                        unitModel = ((ChargesUnitModel) binding.spinUnit.getSelectedItem());
                }
                double rate = Util.getDoubleFromString(binding.edtxtRate.getText().toString());
                String days = binding.edtxtDays.getText().toString();

                if (mAddChargesModel.isForStorageRecurring) {
                    String billing = binding.spinBilling.getSelectedItemId() + "";
                    if (newItemSubmittedListenerForStorageRecurring != null && !mAddChargesModel.isEdit) {
                        newItemSubmittedListenerForStorageRecurring.onNewItemSubmitted(description, quantity, unitModel, rate, days, billing, selectedCategoryModel.getcId(), getSelectRate());
                    } else if (editedItemSubmittedListenerForStorageRecurring != null) {
                        editedItemSubmittedListenerForStorageRecurring.onEditedItemSubmitted(quantity, unitModel, rate, days, billing);
                    }
                } else {
                    if (newItemSubmittedListener != null && !mAddChargesModel.isEdit) {

                        newItemSubmittedListener.onNewItemSubmitted(description, quantity, unitModel, rate, days, selectedCategoryModel.getcId(), getSelectRate(), binding.checkTaxable.isChecked() ? "1" : "0");
                    } else if (editedItemSubmittedListener != null) {
                        editedItemSubmittedListener.onEditedItemSubmitted(quantity, unitModel, rate, days, binding.checkTaxable.isChecked() ? "1" : "0");
                    }
                }

                dismiss();
            }
        };

        binding.txtSubmit.setOnClickListener(onClickListenerSubmit);

        binding.txtSubmitEdit.setOnClickListener(onClickListenerSubmit);

        binding.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editedItemSubmittedListener != null) {
                    editedItemSubmittedListener.onDelete();
                    dismiss();
                } else if (editedItemSubmittedListenerForStorageRecurring != null) {
                    editedItemSubmittedListenerForStorageRecurring.onDelete();
                    dismiss();
                }
            }
        });

        binding.spinBilling.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    binding.tilDays.setVisibility(View.VISIBLE);
                } else {
                    binding.edtxtDays.setText("");
                    binding.tilDays.setVisibility(View.GONE);
                }

                if (position != 0) {
                    mAddChargesModel.setBilling((String) parent.getAdapter().getItem(position));
                } else {
                    mAddChargesModel.setBilling("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * For some cases on selection of certain units change of quantity or rate is required.
         * This listener does that.
         */
        binding.spinUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mAddChargesModel == null) {
                    return;
                }

                if (mAddChargesModel.isFirstTimeUnitSelected) {
                    mAddChargesModel.isFirstTimeUnitSelected = false;
                    return;
                }

                if (position != 0) {
                    mAddChargesModel.setUnit(parent.getAdapter().getItem(position).toString());
                } else {
                    mAddChargesModel.setUnit("");
                }


                chargesUnitModel = (ChargesUnitModel) binding.spinUnit.getAdapter().getItem(position);
                if (onUnitSelectedListener != null) {
                    String currentQuantity = binding.edtxtQuantity.getText().toString();
                    String currentRate = binding.edtxtRate.getText().toString();

                    String quantityToReplace = onUnitSelectedListener.getQuantityToReplace(chargesUnitModel, currentQuantity);
                    String rateToReplace = onUnitSelectedListener.getRateToRepalce(chargesUnitModel, currentRate);

                    //binding.edtxtQuantity.setText(quantityToReplace);
                    binding.edtxtRate.setText(rateToReplace);
                }


                //// get Rate Value......
                if (mAddChargesModel.isEdit) {
                    if (position != 0) {
                        getRateValue();
                    }
                } else {
                    if (position != 0 && binding.spinnerRateType.getSelectedItemPosition() != 0) {
                        getRateValue();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private boolean validate() {

        if (!mAddChargesModel.isEdit && binding.spinnerRateType.getSelectedItemPosition() < 1) {
            Util.showToast(requireContext(), getString(R.string.please_select_rate_type));
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtDescription.getText()) && !mAddChargesModel.isEdit) {
            binding.edtxtDescription.setError(getString(R.string.empty_field_error));
            binding.edtxtDescription.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtQuantity.getText())) {
            binding.edtxtQuantity.setError(getString(R.string.empty_field_error));
            binding.edtxtQuantity.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtRate.getText())) {
            binding.edtxtRate.setError(getString(R.string.empty_field_error));
            binding.edtxtRate.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(binding.edtxtDays.getText()) && binding.tilDays.getVisibility() == View.VISIBLE) {
            binding.edtxtDays.setError(getString(R.string.empty_field_error));
            binding.edtxtDays.requestFocus();
            return false;
        } else if (binding.lilBilling.getVisibility() == View.VISIBLE && binding.spinBilling.getSelectedItemPosition() == 0) {
            Snackbar.make(binding.getRoot(), R.string.please_select_billing_type, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private int getItemPositionInArray(String item, ArrayList<ChargesUnitModel> list) {
        if (list != null && item != null && !item.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUnitName().equals(item)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getItemPositionInArrayForBilling(String item, ArrayList<String> list) {
        if (list != null && item != null && !item.isEmpty()) {
            if (TextUtils.isDigitsOnly(item)) {
                return Integer.parseInt(item);
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(item)) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.addChargesModelLive.postValue(mAddChargesModel);
    }

    private void setSpinnerData(ArrayList<ChargeRateTypeModel> chargeRateTypeList) {
        ChargeRateTypeModel placeHolder = new ChargeRateTypeModel();
        placeHolder.setcStockMaterialName(getString(R.string.select_rate_type));
        chargeRateTypeList.add(0, placeHolder);
        CustomSpinnerBolAdapter<ChargeRateTypeModel> adapter = new CustomSpinnerBolAdapter<>(requireContext(), R.layout.layout_spinner_item, chargeRateTypeList);
        binding.spinnerRateType.setAdapter(adapter);

        binding.spinnerRateType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    selectedChargeRateTypeModel = chargeRateTypeList.get(position);
                    binding.checkTaxable.setChecked(chargeRateTypeList.get(position).getTaxable().equalsIgnoreCase(Constants.TRUE));
                    getRateValue();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (binding.spinUnit.getSelectedItemPosition() == 0) {
            binding.spinUnit.setSelection(getItemPositionInArray("Each", mAddChargesModel.unitModelList));
        }

        if (binding.spinUnit.getSelectedItemPosition() < 1) {
            binding.spinUnit.setSelection(1);
        }

    }

    private void setCategorySpinner(ArrayList<CategoryModel> categoryList) {
        ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<>(requireContext(), R.layout.item_spinner_charge_rate_type, categoryList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapter);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryModel = categoryList.get(position);
                callChargeRateTypeList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public interface OnEditedItemSubmittedListener {
        void onEditedItemSubmitted(double quantity, ChargesUnitModel unitModel, double rate, String days, String taxable);

        void onDelete();
    }

    public interface OnEditedItemSubmittedListenerForStorageRecurring {
        void onEditedItemSubmitted(double quantity, ChargesUnitModel unitModel, double rate, String days, String billing);

        void onDelete();
    }


    /**
     * For some cases on selection of certain units change of quantity or rate is required.
     * This interface gets the rate and quantity to replace.
     * For the cases when no change in quantity or rate is required then functions can return the
     * original value. Hence sending original values as parameters.
     */
    public interface OnUnitSelectedListener {
        String getQuantityToReplace(ChargesUnitModel unitModel, String currentQuantity);

        String getRateToRepalce(ChargesUnitModel unitModel, String currentRate);
    }

    private void callCategoryList() {
        viewModel.getCategoryTypeList(MoversPreferences.getInstance(getContext()).getSubdomain(), MoversPreferences.getInstance(getContext()).getOpportunityId(), moveTypeId, model, new ResponseListener<BaseResponseModel<ArrayList<CategoryModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<CategoryModel>> response, String usedUrlKey) {

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showToast(requireContext(), serverResponseError.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CategoryModel>>> call, Throwable t, String message) {
                Util.showToast(getContext(), message);
            }
        });
    }

    private void callChargeRateTypeList() {
        viewModel.getChargeRateTypeList(MoversPreferences.getInstance(getContext()).getSubdomain(), MoversPreferences.getInstance(getContext()).getOpportunityId(), moveTypeId, model, selectedCategoryModel.getcId(), new ResponseListener<BaseResponseModel<ArrayList<ChargeRateTypeModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<ChargeRateTypeModel>> response, String usedUrlKey) {

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showToast(requireContext(), serverResponseError.getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<ChargeRateTypeModel>>> call, Throwable t, String message) {
                Util.showToast(getContext(), message);
            }
        });


    }

    private String getSelectRate() {
        String selectRate;
        if (model.equals(Constants.MoveInfoModelTypes.PACKING_MODEL_TEXT)) {
            selectRate = selectedChargeRateTypeModel.getcId();
        } else {
            selectRate = selectedChargeRateTypeModel.getcStockMaterialName();
        }
        return selectRate;
    }

    private void setSelectRate(String selectedRate) {
        if (model.equals(Constants.MoveInfoModelTypes.PACKING_MODEL_TEXT)) {
            selectedChargeRateTypeModel.setcId(selectedRate);
        } else {
            selectedChargeRateTypeModel.setcStockMaterialName(selectedRate);
        }
    }


    public interface OnNewItemSubmittedListener {
        void onNewItemSubmitted(String description, double quantity, ChargesUnitModel chargesUnitModel, double rate, String days, int cId, String selectRate, String taxable);
    }

    public interface OnNewItemSubmittedListenerForStorageRecurring {
        void onNewItemSubmitted(String description, double quantity, ChargesUnitModel chargesUnitModel, double rate, String days, String billing, int cId, String selectRate);
    }

    public void getRateValue() {
        String name = selectedChargeRateTypeModel.getcStockMaterialName();
        String type = "1";

        if (model.equals(Constants.MoveInfoModelTypes.PACKING_MODEL_TEXT)) {
            name = selectedChargeRateTypeModel.getcId();
            type = "2";
        }

        viewModel.getRateValue(MoversPreferences.getInstance(getContext()).getSubdomain(), name, type, MoversPreferences.getInstance(getContext()).getOpportunityId(), chargesUnitModel.getUnitId(), new ResponseListener<BaseResponseModel<ChargesUnitModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ChargesUnitModel> response, String usedUrlKey) {
                binding.edtxtRate.setText(response.getData().getRate());
                binding.edtxtDescription.setText(response.getData().getDescription());
                binding.edtxtQuantity.setText(response.getData().getQty());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                Util.showToast(requireContext(), serverResponseError.getMessage());
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ChargesUnitModel>> call, Throwable t, String message) {
                Util.showToast(requireContext(), message);
            }
        });
    }

}
