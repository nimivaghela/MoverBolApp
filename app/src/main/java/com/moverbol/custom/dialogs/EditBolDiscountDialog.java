package com.moverbol.custom.dialogs;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.moverbol.R;
import com.moverbol.databinding.EditDiscountDialogBinding;
import com.moverbol.util.MoversPreferences;

public class EditBolDiscountDialog extends BaseDialogFragment {

    private EditDiscountDialogBinding binding;
    private OnDiscountSubmittedListener onDiscountSubmittedListener;
    private Context context;
    private double mDiscountAmount;
    private boolean isPercentage;

    public interface OnDiscountSubmittedListener{
        void onEditedDiscountSubmitted(EditBolDiscountDialog  editBolDiscountDialog, double discount, boolean isPercentage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_edit_bol_discount_dialog, container, false);
        setCancelable(false);

        if(context!=null) {
            String currencySymbol = MoversPreferences.getInstance(context).getCurrencySymbol();
            binding.radioOther.setText("Other Amount (" + currencySymbol + ")");
        }

        setActionListeners();

//        radioGroup.getCheckedRadioButtonId() == -1

        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onDiscountSubmittedListener = (OnDiscountSubmittedListener) context;
        } catch (ClassCastException cce){
            throw new ClassCastException(context.toString()
             + " must implement EditBolDiscountDialog.OnDiscountSubmittedListener");
        }

    }

    private void setActionListeners() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                binding.btnDone.setEnabled(true);
                binding.btnDone.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));


                switch (i){
                    case R.id.radio_zero:
                        isPercentage = true;
                        mDiscountAmount = 0;
                        break;

                    case R.id.radio_one:
                        isPercentage = true;
                        mDiscountAmount = 5;
                        break;

                    case R.id.radio_two:
                        isPercentage = true;
                        mDiscountAmount = 10;
                        break;

                    case R.id.radio_three:
                        isPercentage = true;
                        mDiscountAmount = 15;
                        break;

                    case R.id.radio_four:
                        isPercentage = true;
                        mDiscountAmount = 20;
                        break;

                    case R.id.radio_other:
                        isPercentage = false;
                        break;
                }
            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(onDiscountSubmittedListener!=null){
                    if(validate()) {
                        if(!isPercentage){
                            mDiscountAmount = Double.parseDouble(binding.editText3.getText().toString());
                        }
                        onDiscountSubmittedListener.onEditedDiscountSubmitted(EditBolDiscountDialog.this, mDiscountAmount, isPercentage);
                    }
                }
            }
        });

        binding.radioOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    binding.editText3.setVisibility(View.VISIBLE);
                } else {
                    binding.editText3.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setOnDiscountSubmittedListener(OnDiscountSubmittedListener onDiscountSubmittedListener) {
        this.onDiscountSubmittedListener = onDiscountSubmittedListener;
    }

    private boolean validate() {
        if(binding.radioGroup.getCheckedRadioButtonId() == -1){
            return false;
        }

        if(binding.radioGroup.getCheckedRadioButtonId() == R.id.radio_other && TextUtils.isEmpty(binding.editText3.getText())){
            binding.editText3.requestFocus();
            binding.editText3.setError(getString(R.string.empty_field_error));
            return false;
        }
        return true;
    }

    public static void start(Context context, FragmentManager supportFragmentManager) {
        EditBolDiscountDialog editBolDiscountDialog = new EditBolDiscountDialog();
        editBolDiscountDialog.context = context;
        editBolDiscountDialog.show(supportFragmentManager, "dialog");
    }


    /*public static void startWithActionListeners(Context context, FragmentManager supportFragmentManager, OnDiscountSubmittedListener onDiscountSubmittedListener) {
        EditBolDiscountDialog dialog = new EditBolDiscountDialog();
        dialog.context = context;
        dialog.setOnDiscountSubmittedListener(onDiscountSubmittedListener);
        dialog.show(supportFragmentManager, "dialog");
    }*/

}
