package com.moverbol.custom.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.moverbol.R;

/**
 * Created by Admin on 27-09-2017.
 */

public class CustomDialogDataBind extends DialogFragment {
    private String title;
    private String message;
    private String okLbl;
    private String cancelLbl;
    private DialogInterface.OnClickListener okClickListener, cancelClickListener;
    private int layoutId;
    private ViewDataBinding viewDataBinding;

    public CustomDialogDataBind() {
        super();
    }

    /*@SuppressLint("ValidFragment")
    public CustomDialogDataBind(String title, String message, String okLbl, String cancelLbl, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener, ViewDataBinding viewDataBinding) {
        this.title = title;
        this.message = message;
        this.okLbl = okLbl;
        this.cancelLbl = cancelLbl;
        this.okClickListener = okListener;
        this.cancelClickListener = cancelListener;
        //this.layoutId = layoutId;
        this.viewDataBinding = viewDataBinding;
    }*/


    public void initializeVariable(String title, String message, String okLbl, String cancelLbl, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener, ViewDataBinding viewDataBinding) {
        this.title = title;
        this.message = message;
        this.okLbl = okLbl;
        this.cancelLbl = cancelLbl;
        this.okClickListener = okListener;
        this.cancelClickListener = cancelListener;
        //this.layoutId = layoutId;
        this.viewDataBinding = viewDataBinding;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setView(viewDataBinding.getRoot());
        //builder.setView(layoutId);
        builder.setTitle(title);
        builder.setMessage(message)
                .setPositiveButton(okLbl, okClickListener)
                .setNegativeButton(cancelLbl, cancelClickListener);
        return builder.create();
    }

}
