package com.moverbol.views.dialogs;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.databinding.DialogReceiptBinding;
import com.moverbol.util.MoversPreferences;

import net.authorize.ResponseReasonCode;
import net.authorize.aim.emv.Result;
import net.authorize.util.StringUtils;

import java.util.ArrayList;
import java.util.Objects;

public class PaymentReceiptDialog extends DialogFragment {

    private DialogReceiptBinding binding;
    private Result result;
    private PaymentListener paymentListener;
    private boolean isSuccess = false;


    public static PaymentReceiptDialog newInstance(Result result, Boolean isSuccess) {
        PaymentReceiptDialog f = new PaymentReceiptDialog();
        Bundle args = new Bundle();
        args.putSerializable(Constants.PAYMENT_RESULT, result);
        args.putBoolean(Constants.SUCCESS, isSuccess);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.CustomDialog);
        setCancelable(false);
        result = (Result) getArguments().getSerializable(Constants.PAYMENT_RESULT);
        isSuccess = getArguments().getBoolean(Constants.SUCCESS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.dialog_receipt, container, false);
        setData();
        binding.btnOk.setOnClickListener(view -> {
            if (isSuccess) {
                if (checkEmailValidation()) {
                    paymentListener.onSuccess(String.valueOf(binding.editEmail.getText()).trim());
                }
            } else {
                dismiss();
            }
        });
        return binding.getRoot();
    }


    public void setData() {
        binding.editEmail.setText(MoversPreferences.getInstance(getContext()).getUserEmail());
        binding.setResult(result);
        Glide.with(requireContext()).asGif()
                .load(R.drawable.payament_failed)
                .into(binding.imgSuccess);

        if (isSuccess) {
            Glide.with(requireContext()).asGif()
                    .load(R.drawable.img_payment_sucess)
                    .into(binding.imgSuccess);
            binding.txtStatus.setText(getString(R.string.transaction_successful));

        } else {
            ArrayList<ResponseReasonCode> responseError = result
                    .getTransactionResponseErrors();
            if (responseError != null
                    && responseError.size() > 0
                    && !StringUtils.isEmpty(responseError
                    .get(0).getReasonText())) {

                binding.txtStatus.setText(getString(R.string.transaction_unsuccessful) + responseError.get(0).getReasonText());
            } else {
                isSuccess = false;
                binding.txtStatus.setText(getString(R.string.transaction_unsuccessful));

            }
        }

    }

    public void setPaymentListener(PaymentListener paymentListener) {
        this.paymentListener = paymentListener;
    }

    public boolean checkEmailValidation() {
        binding.txtInputEmail.setErrorEnabled(false);
        if (TextUtils.isEmpty(binding.editEmail.getText())) {
            binding.txtInputEmail.setError(getString(R.string.please_enter_email));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(binding.editEmail.getText())).matches()) {
            binding.txtInputEmail.setError(getString(R.string.invalid_email_error));
            return false;
        }
        return true;
    }

    public interface PaymentListener {
        void onSuccess(String email);
    }
}
