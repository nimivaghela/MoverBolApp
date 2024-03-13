package com.moverbol.custom.dialogs;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.adapters.CustomSpinnerWithoughtHintAdapter;
import com.moverbol.databinding.EditConvenienceFeeBinding;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;

public class EditConvenienceFeeDialog extends BaseDialogFragment {

    private EditConvenienceFeeBinding binding;
    private int selectedType;
    private double feeValue;
    private OnSubmitListener onSubmitListener;

    public interface OnSubmitListener{
        void onConvenienceFeeSubmit(double feeValue, boolean isPercentage, EditConvenienceFeeDialog dialog);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_convinience_fee, container, false);

        initialisation();
        setActionListeners();


        return binding.getRoot();
    }

    private void initialisation() {

        setCancelable(true);

        String currencySymbol = MoversPreferences.getInstance(this.getContext()).getCurrencySymbol();
        String[] typeList = {"%", currencySymbol};

        CustomSpinnerWithoughtHintAdapter<String> spinnerBolAdapter = new CustomSpinnerWithoughtHintAdapter<>(getContext(), R.layout.layout_spinner_item, typeList);
        binding.spinFeeType.setAdapter(spinnerBolAdapter);
        binding.spinFeeType.setSelection(selectedType);

        if(feeValue>0.0) {
            binding.edtxtFeeValue.setText(feeValue + "");
        } else {
            binding.edtxtFeeValue.setText("");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onSubmitListener = (OnSubmitListener) context;
        } catch (ClassCastException cce){
            throw new ClassCastException(context.toString()
            + " must implement EditConvenienceFeeDialog.OnSubmitListener");
        }

    }

    private void setActionListeners() {
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditConvenienceFeeDialog.this.dismiss();
            }
        });

        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    return;
                }

                feeValue = Util.getDoubleFromString(binding.edtxtFeeValue.getText().toString());

                if(onSubmitListener!=null){
                    onSubmitListener.onConvenienceFeeSubmit(feeValue,
                            binding.spinFeeType.getSelectedItemPosition()==0,
                            EditConvenienceFeeDialog.this);
                }
            }
        });

        /*binding.spinFeeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    binding.txtSubmit.setEnabled(true);
                    binding.txtSubmit.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                } else {
                    binding.txtSubmit.setEnabled(false);
                    binding.txtSubmit.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_login_gray));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    private boolean validate() {

        if(TextUtils.isEmpty(binding.edtxtFeeValue.getText().toString())){
            binding.edtxtFeeValue.setError(getString(R.string.empty_field_error));
            return false;
        }

        return true;
    }

    /*public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }*/


    public static void show(FragmentManager fragmentManager, boolean isPercentage, double feeValue/*, OnSubmitListener onSubmitListener*/) {
        EditConvenienceFeeDialog dialog = new EditConvenienceFeeDialog();


        if(!isPercentage){
            dialog.selectedType = 1;
        }

        dialog.feeValue = feeValue;
//        dialog.setOnSubmitListener(onSubmitListener);
        dialog.show(fragmentManager, "");
    }


}
