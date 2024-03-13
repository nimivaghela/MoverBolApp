package com.moverbol.custom.dialogs;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.SignatureDialogBinding;

/**
 * Created by Admin on 25-09-2017.
 */

public class SignatureDialogFragment extends BaseDialogFragment  {
    private Context context;
    private SignatureDialogBinding signatureDialogBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        signatureDialogBinding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_signature, container, false);
        signatureDialogBinding.setSignaturedialog(this);
        setCancelable(false);
        return signatureDialogBinding.getRoot();
    }
    public void dismissDialog(View view) {
        dismiss();
    }
}