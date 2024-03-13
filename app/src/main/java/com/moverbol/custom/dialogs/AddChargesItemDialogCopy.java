package com.moverbol.custom.dialogs;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddChargesItemDialogCopy  extends BaseDialogFragment {

    /*private static final String FRAGMENT_TRANSACTION_TAG = "dialog";
    private static final String SELECT_UNIT_HINT = "--Select Unit--";
    private AddChargesItemBinding binding;


    private BillOfLadingViewModel viewModel;
    private AddChargesModel mAddChargesModel;


    public static void showForAdd(FragmentManager fragmentManager, @NonNull ArrayList<ChargesUnitModel> unitModelList, boolean showDays, OnNewItemSubmittedListener onNewItemSubmittedListener, OnUnitSelectedListener onUnitSelectedListener) {
        AddChargesItemDialogCopy addChargesItemDialog = new AddChargesItemDialogCopy();

        AddChargesModel addChargesModel = new AddChargesModel();
        addChargesModel.showDays = showDays;

        if (unitModelList != null && unitModelList.size()>0) {
            addChargesModel.unitModelList = unitModelList;
            if (!TextUtils.equals(unitModelList.get(0).getUnitName(), SELECT_UNIT_HINT)) {
                ChargesUnitModel selectionHintModel = new ChargesUnitModel();
                selectionHintModel.setUnitName(SELECT_UNIT_HINT);
                addChargesModel.unitModelList.add(0, selectionHintModel);
            }
        }


        addChargesModel.setNewItemSubmittedListener(onNewItemSubmittedListener);

        addChargesModel.setOnUnitSelectedListener(onUnitSelectedListener);

        addChargesItemDialog.mAddChargesModel = addChargesModel;

        addChargesItemDialog.show(fragmentManager, FRAGMENT_TRANSACTION_TAG);
    }


    public static void showForAddForStorageRecurring(FragmentManager fragmentManager, @NonNull ArrayList<ChargesUnitModel> unitModelList, OnNewItemSubmittedListenerForStorageRecurring onNewItemSubmittedListener, OnUnitSelectedListener onUnitSelectedListener) {
        AddChargesItemDialogCopy addChargesItemDialog = new AddChargesItemDialogCopy();

        AddChargesModel addChargesModel = new AddChargesModel();

        if (unitModelList != null && unitModelList.size()>0) {
            addChargesModel.unitModelList = unitModelList;
            if (!TextUtils.equals(unitModelList.get(0).getUnitName(), SELECT_UNIT_HINT)) {
                ChargesUnitModel selectionHintModel = new ChargesUnitModel();
                selectionHintModel.setUnitName(SELECT_UNIT_HINT);
                addChargesModel.unitModelList.add(0, selectionHintModel);
            }
        }

        addChargesModel.isForStorageRecurring = true;

        addChargesModel.setNewItemSubmittedListenerForStorageRecurring(onNewItemSubmittedListener);
        addChargesModel.setOnUnitSelectedListener(onUnitSelectedListener);

        addChargesItemDialog.mAddChargesModel = addChargesModel;

        addChargesItemDialog.show(fragmentManager, FRAGMENT_TRANSACTION_TAG);
    }

    public static void showForEdit(FragmentManager fragmentManager, @NonNull ArrayList<ChargesUnitModel> unitModelList, boolean showDays, String quantity, String unit, String rate, @Nullable String days, boolean shouldShowDeleteButton, OnEditedItemSubmittedListener onEditedItemSubmittedListener, OnUnitSelectedListener onUnitSelectedListener) {
        AddChargesItemDialogCopy addChargesItemDialog = new AddChargesItemDialogCopy();

        AddChargesModel addChargesModel = new AddChargesModel();
        addChargesModel.showDays = showDays;

        if (unitModelList != null && unitModelList.size()>0) {
            addChargesModel.unitModelList = unitModelList;
            if (!TextUtils.equals(unitModelList.get(0).getUnitName(), SELECT_UNIT_HINT)) {
                ChargesUnitModel selectionHintModel = new ChargesUnitModel();
                selectionHintModel.setUnitName(SELECT_UNIT_HINT);
                addChargesModel.unitModelList.add(0, selectionHintModel);
            }
        }

        addChargesModel.isEdit = true;
        addChargesModel.quantity = quantity;
        addChargesModel.days = days;
        addChargesModel.rate = rate;
        addChargesModel.unit = unit;
        addChargesModel.isFirstTimeUnitSelected = true;
        addChargesModel.shouldShowDeleteButton = shouldShowDeleteButton;


        addChargesModel.setEditedItemSubmittedListener(onEditedItemSubmittedListener);
        addChargesModel.setOnUnitSelectedListener(onUnitSelectedListener);

        addChargesItemDialog.mAddChargesModel = addChargesModel;

        addChargesItemDialog.show(fragmentManager, FRAGMENT_TRANSACTION_TAG);


    }

    public static void showForEditForStorageRecurring(FragmentManager fragmentManager, @NonNull ArrayList<ChargesUnitModel> unitModelList, String quantity, String unit, String rate, String days, String billing, boolean shouldShowDeleteButton, OnEditedItemSubmittedListenerForStorageRecurring onEditedItemSubmittedListener, OnUnitSelectedListener onUnitSelectedListener) {
        AddChargesItemDialogCopy addChargesItemDialog = new AddChargesItemDialogCopy();

        AddChargesModel addChargesModel = new AddChargesModel();

        addChargesModel.isForStorageRecurring = true;

        addChargesModel.shouldShowDeleteButton = shouldShowDeleteButton;

        if (unitModelList != null && unitModelList.size()>0) {
            addChargesModel.unitModelList = unitModelList;
            if (!TextUtils.equals(unitModelList.get(0).getUnitName(), SELECT_UNIT_HINT)) {
                ChargesUnitModel selectionHintModel = new ChargesUnitModel();
                selectionHintModel.setUnitName(SELECT_UNIT_HINT);
                addChargesModel.unitModelList.add(0, selectionHintModel);
            }
        }

        addChargesModel.isEdit = true;

        addChargesModel.quantity = quantity;
        addChargesModel.days = days;
        addChargesModel.rate = rate;
        addChargesModel.unit = unit;
        addChargesModel.billing = billing;
        addChargesModel.isFirstTimeUnitSelected = true;




        addChargesModel.setEditedItemSubmittedListenerForStorageRecurring(onEditedItemSubmittedListener);
        addChargesModel.setOnUnitSelectedListener(onUnitSelectedListener);

        addChargesItemDialog.mAddChargesModel = addChargesModel;

        addChargesItemDialog.show(fragmentManager, FRAGMENT_TRANSACTION_TAG);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_chrges_items, container, true);

        if(getActivity()!=null) {
            viewModel = ViewModelProviders.of(getActivity()).get(BillOfLadingViewModel.class);
        } else {
            viewModel = ViewModelProviders.of(this).get(BillOfLadingViewModel.class);
        }

        setCancelable(true);

        setActionListeners();
        setViewModelObserver();

        if(viewModel.addChargesModelLive.getValue()==null)
        viewModel.addChargesModelLive.postValue(mAddChargesModel);

        return binding.getRoot();
    }

    private void setViewModelObserver() {
        viewModel.addChargesModelLive.observe(this, new Observer<AddChargesModel>() {
            @Override
            public void onChanged(@Nullable AddChargesModel addChargesModel) {
                if(addChargesModel==null){
                    return;
                }

                mAddChargesModel = addChargesModel;

                binding.setShowDays(mAddChargesModel.showDays);
                binding.setIsEdit(mAddChargesModel.isEdit);
                binding.setShowDelete(mAddChargesModel.shouldShowDeleteButton);

                if (mAddChargesModel.isEdit) {
                    binding.txtTitle.setText(R.string.edit_item);
                }

                binding.edtxtDescription.setText(mAddChargesModel.description);
                binding.edtxtQuantity.setText(mAddChargesModel.quantity);

                binding.edtxtRate.setText(mAddChargesModel.rate);
                binding.edtxtDays.setText(mAddChargesModel.days);

                if (mAddChargesModel.unitModelList == null) {
                    mAddChargesModel.unitModelList = new ArrayList<>();
                }

                CustomSpinnerBolAdapter<ChargesUnitModel> spinnerBolAdapter = new CustomSpinnerBolAdapter<>(getContext(), R.layout.layout_spinner_item, mAddChargesModel.unitModelList);
                binding.spinUnit.setAdapter(spinnerBolAdapter);
                binding.spinUnit.setSelection(getItemPositionInArray(mAddChargesModel.unit, mAddChargesModel.unitModelList));

                if (mAddChargesModel.isForStorageRecurring) {
                    binding.lilBilling.setVisibility(View.VISIBLE);
                    ArrayList<String> billingList = new ArrayList<>();
                    billingList.add("--Select Billing Type--");
                    billingList.add("Daily");
                    billingList.add("Monthly");
                    CustomSpinnerBolAdapter<String> spinnerBolAdapterBilling = new CustomSpinnerBolAdapter<>(getContext(), R.layout.layout_spinner_item, billingList);
                    binding.spinBilling.setAdapter(spinnerBolAdapterBilling);
                    if(mAddChargesModel.billing!=null) {
                        binding.spinBilling.setSelection(getItemPositionInArrayForBilling(mAddChargesModel.billing, billingList));
                    }
                }

            }
        });
    }


    private int getItemPositionInArray(String item, ArrayList<ChargesUnitModel> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUnitName().equals(item)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int getItemPositionInArrayForBilling(String item, ArrayList<String> list) {
        if (list != null) {
            if(TextUtils.isDigitsOnly(item)){
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


    private void setActionListeners() {
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChargesItemDialogCopy.this.dismiss();
            }
        });

        View.OnClickListener onClickListenerSubmit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }

                String description = binding.edtxtDescription.getText().toString();
                double quantity = Double.parseDouble(binding.edtxtQuantity.getText().toString());
                String unit = "";
                String unitId = "";
                if (binding.spinUnit.getSelectedItemPosition() != 0) {
                    unit = binding.spinUnit.getSelectedItem().toString();
                    if(binding.spinUnit.getSelectedItem() instanceof ChargesUnitModel)
                        unitId = ((ChargesUnitModel)binding.spinUnit.getSelectedItem()).getUnitId();
                }
                double rate = Util.getDoubleFromString(binding.edtxtRate.getText().toString());
                String days = binding.edtxtDays.getText().toString();

                if(mAddChargesModel.isForStorageRecurring){
                    String billing = binding.spinBilling.getSelectedItemId() + "";
                    if (mAddChargesModel.newItemSubmittedListenerForStorageRecurring != null && !mAddChargesModel.isEdit) {
                        mAddChargesModel.newItemSubmittedListenerForStorageRecurring.onNewItemSubmitted(description, quantity, unit, unitId, rate, days, billing);
                    } else if (mAddChargesModel.editedItemSubmittedListenerForStorageRecurring != null) {
                        mAddChargesModel.editedItemSubmittedListenerForStorageRecurring.onEditedItemSubmitted(quantity, unit, unitId, rate, days, billing);
                    }
                } else {
                    if (mAddChargesModel.newItemSubmittedListener != null && !mAddChargesModel.isEdit) {
                        mAddChargesModel.newItemSubmittedListener.onNewItemSubmitted(description, quantity, unit, unitId, rate, days);
                    } else if (mAddChargesModel.editedItemSubmittedListener != null) {
                        mAddChargesModel.editedItemSubmittedListener.onEditedItemSubmitted(quantity, unit, unitId, rate, days);
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
                if (mAddChargesModel.editedItemSubmittedListener != null) {
                    mAddChargesModel.editedItemSubmittedListener.onDelete();
                    dismiss();
                } else if(mAddChargesModel.editedItemSubmittedListenerForStorageRecurring!=null){
                    mAddChargesModel.editedItemSubmittedListenerForStorageRecurring.onDelete();
                    dismiss();
                }
            }
        });

        binding.spinBilling.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==1){
                    binding.tilDays.setVisibility(View.VISIBLE);
                } else {
                    binding.edtxtDays.setText("");
                    binding.tilDays.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        *//**
         * For some cases on selection of certain units change of quantity or rate is required.
         * This listener does that.
         *//*
        binding.spinUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(mAddChargesModel==null){
                    return;
                }

                if(mAddChargesModel.isFirstTimeUnitSelected) {
                    mAddChargesModel.isFirstTimeUnitSelected = false;
                    return;
                }

                ChargesUnitModel chargesUnitModel = (ChargesUnitModel) binding.spinUnit.getAdapter().getItem(position);
                if(mAddChargesModel.onUnitSelectedListener!=null){
                    String currentQuantity = binding.edtxtQuantity.getText().toString();
                    String currentRate = binding.edtxtRate.getText().toString();

                    String quantityToReplace = mAddChargesModel.onUnitSelectedListener.getQuantityToReplace(chargesUnitModel, currentQuantity);
                    String rateToReplace = mAddChargesModel.onUnitSelectedListener.getRateToRepalce(chargesUnitModel, currentRate);

                    binding.edtxtQuantity.setText(quantityToReplace);
                    binding.edtxtRate.setText(rateToReplace);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private boolean validate() {

        if (TextUtils.isEmpty(binding.edtxtDescription.getText()) && !mAddChargesModel.isEdit) {
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
        } else if (TextUtils.isEmpty(binding.edtxtDays.getText()) && binding.tilDays.getVisibility()==View.VISIBLE) {
            binding.edtxtDays.setError(getString(R.string.empty_field_error));
            binding.edtxtDays.requestFocus();
            return false;
        }  else if (binding.lilBilling.getVisibility() == View.VISIBLE && binding.spinBilling.getSelectedItemPosition() == 0) {
            Snackbar.make(binding.getRoot(), R.string.please_select_billing_type, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



    public interface OnNewItemSubmittedListener {
        void onNewItemSubmitted(String description, double quantity, String unit, String unitId, double rate, String days);
    }

    public interface OnNewItemSubmittedListenerForStorageRecurring {
        void onNewItemSubmitted(String description, double quantity, String unit, String unitId, double rate, String days, String billing);
    }

    public interface OnEditedItemSubmittedListener {
        void onEditedItemSubmitted(double quantity, String unit, String unitId, double rate, String days);

        void onDelete();
    }

    public interface OnEditedItemSubmittedListenerForStorageRecurring {
        void onEditedItemSubmitted(double quantity, String unit, String unitId, double rate, String days, String billing);
        void onDelete();
    }

    *//**
     * For some cases on selection of certain units change of quantity or rate is required.
     * This interface gets the rate and quantity to replace.
     * For the cases when no change in quantity or rate is required then functions can return the
     * original value. Hence sending original values as parameters.
     *//*
    public interface OnUnitSelectedListener{
        String getQuantityToReplace(ChargesUnitModel unitModel, String currentQuantity);
        String getRateToRepalce(ChargesUnitModel unitModel, String currentRate);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.addChargesModelLive.postValue(mAddChargesModel);
    }*/
}
