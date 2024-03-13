package com.moverbol.custom.dialogs;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.PaymentSignBinding;

/**
 * Created by Admin on 10-10-2017.
 */

public class PaymentSignDialog extends BaseDialogFragment  {
     private PaymentSignBinding paymentSignBinding;
     private OnSubmitListener onSubmitListener;

     private boolean isCancelable;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        paymentSignBinding = DataBindingUtil.inflate(inflater, R.layout.layout_paymentsign_dialog, container, false);
        setCancelable(false);

        if(isCancelable){
            paymentSignBinding.ivClose.setVisibility(View.VISIBLE);
            paymentSignBinding.signature.setText("");
            paymentSignBinding.textView140.setText(R.string.signature);
        }

        setActionListeners();

        return paymentSignBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            onSubmitListener = (OnSubmitListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement PaymentSignDialog.OnSubmitListener");
        }
    }

    private void setActionListeners() {
        paymentSignBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSubmitListener!=null && validate()){
                    onSubmitListener.OnSignatureSubmit(PaymentSignDialog.this, paymentSignBinding.signaturePad.getSignatureBitmap());
                }
            }
        });

        paymentSignBinding.txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentSignBinding.signaturePad.clear();
            }
        });

        paymentSignBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentSignDialog.this.dismiss();
            }
        });
    }

    private boolean validate() {
        if (paymentSignBinding.signaturePad.isEmpty()) {
            Snackbar.make(paymentSignBinding.getRoot(), R.string.signature_required_error, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /*public void openRatingDialog(View view) {
        RatingDialog ratingDialog = new RatingDialog();
        ratingDialog.show(getActivity().getSupportFragmentManager(), "dialog");
        dismiss();
    }*/

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }

    public static void startWithActionListener(FragmentManager fragmentManager, OnSubmitListener listener) {
        PaymentSignDialog dialog = new PaymentSignDialog();
        dialog.setOnSubmitListener(listener);
        dialog.show(fragmentManager, "dialog");
    }

    public static void startWithActionListener(FragmentManager fragmentManager, OnSubmitListener listener, boolean isCancelable) {
        PaymentSignDialog dialog = new PaymentSignDialog();
        dialog.isCancelable = isCancelable;
        dialog.setOnSubmitListener(listener);
        dialog.show(fragmentManager, "dialog");
    }

    public static void start(FragmentManager fragmentManager) {
        PaymentSignDialog dialog = new PaymentSignDialog();
        dialog.show(fragmentManager, "dialog");
    }

    public static void start(FragmentManager fragmentManager, boolean isCancelable) {
        PaymentSignDialog dialog = new PaymentSignDialog();
        dialog.isCancelable = isCancelable;
        dialog.show(fragmentManager, "dialog");
    }

    public interface OnSubmitListener{
        void OnSignatureSubmit(PaymentSignDialog dialog, Bitmap signatureBitmap);
    }

}