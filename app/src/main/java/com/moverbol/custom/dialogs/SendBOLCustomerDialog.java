package com.moverbol.custom.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.moverbol.R;
import com.moverbol.databinding.DialogSendBolCustomerBinding;
import com.moverbol.util.Util;

public class SendBOLCustomerDialog extends BaseDialogFragment {

    private DialogSendBolCustomerBinding binding;
    private FragmentManager mFragmentManager;
    private OnSendBOLCustomerClickListener onSendBOLCustomerClickListener;

    public static void start(FragmentManager supportFragmentManager) {
        SendBOLCustomerDialog dialog = new SendBOLCustomerDialog();
        dialog.mFragmentManager = supportFragmentManager;
        dialog.show(dialog.mFragmentManager, "SendBOLCustomerDialog");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            onSendBOLCustomerClickListener = (OnSendBOLCustomerClickListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement BolSignatureDialog.OnSignatureSubmittedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_send_bol_customer, container, false);
        setCancelable(true);
        binding.btnSubmit.setEnabled(false);
        setActionListeners();
        return binding.getRoot();
    }

    private void setActionListeners() {
        binding.txtEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.btnSubmit.setEnabled(!s.toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches());
            }
        });
        binding.btnSubmit.setOnClickListener(v -> {
            Util.hideSoftKeyboard(binding.txtEditEmail);
            onSendBOLCustomerClickListener.onSendBOLCustomerClicked(binding.txtEditEmail.getText().toString());
            dismiss();
        });

    }

    public interface OnSendBOLCustomerClickListener {
        public void onSendBOLCustomerClicked(String emailAddress);
    }
}
