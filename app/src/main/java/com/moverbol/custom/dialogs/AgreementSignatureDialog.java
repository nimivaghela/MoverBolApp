package com.moverbol.custom.dialogs;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.AgreementSignatureBinding;

/**
 * Created by Admin on 10-10-2017.
 */

public class AgreementSignatureDialog extends BaseDialogFragment {

    private AgreementSignatureBinding binding;

    /*private OnSignatureSubmittedListener onSignatureSubmittedListener;
    
    public interface OnSignatureSubmittedListener{
        public void onSignatureSubmitted();
    }*/

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_agreement_signature_dialog, container, false);
        binding.setAgreementSignDialog(this);
        setCancelable(false);

        return binding.getRoot();
    }

    public void dismissDialog(View view) {
        dismiss();
    }

    /*public void setOnSignatureSubmittedListener(OnSignatureSubmittedListener onSignatureSubmittedListener) {
        this.onSignatureSubmittedListener = onSignatureSubmittedListener;
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgreementSignatureDialog.this.onSignatureSubmittedListener.onSignatureSubmitted();
            }
        });
    }*/
}