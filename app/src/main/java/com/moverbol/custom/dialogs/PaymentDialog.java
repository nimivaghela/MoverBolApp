package com.moverbol.custom.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.moverbol.R;
import com.moverbol.databinding.PaymentDialogBinding;
import com.moverbol.util.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 10-10-2017.
 */

public class PaymentDialog extends BaseDialogFragment {
    private List<RadioButton> radioButtons;
    private PaymentDialogBinding paymentDialogBinding;
    private boolean shouldShowCustomerId;
    private OnSubmitListener onSubmitListener;

    private File mImageFile;

    public static PaymentDialog start(FragmentManager fragmentManager, boolean shouldShowCustomerId/*, OnSubmitListener listener*/) {
        PaymentDialog paymentDialog = new PaymentDialog();
        paymentDialog.shouldShowCustomerId = shouldShowCustomerId;
//        paymentDialog.setOnSubmitListener(listener);
        paymentDialog.show(fragmentManager, "dialog");
        return paymentDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        paymentDialogBinding = DataBindingUtil.inflate(inflater, R.layout.layout_payment_dialog, container, false);
        setCancelable(false);
        /*radioButtons = new ArrayList<RadioButton>();
        radioButtons.add(paymentDialogBinding.rbCreditslip);
        radioButtons.add(paymentDialogBinding.rbCustomerId);
        for (RadioButton button : radioButtons) {
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) processRadioButtonClick(buttonView);
                }
            });
        }*/
        if (shouldShowCustomerId) {
            paymentDialogBinding.rbCreditslip.setVisibility(View.INVISIBLE);
            paymentDialogBinding.rbCustomerId.setVisibility(View.VISIBLE);
        } else {
            paymentDialogBinding.rbCreditslip.setVisibility(View.VISIBLE);
            paymentDialogBinding.rbCustomerId.setVisibility(View.INVISIBLE);
        }

        setActionListeners();

        return paymentDialogBinding.getRoot();
    }

    private void setActionListeners() {
        paymentDialogBinding.ivClose.setOnClickListener(v -> {
            if (onSubmitListener != null) {
                onSubmitListener.onPaymentDialogSkip(PaymentDialog.this);
            }
        });

        paymentDialogBinding.btnSave.setOnClickListener(v -> {
            if (onSubmitListener != null) {
                if (mImageFile != null) {
                    // String imgeBase64String = Util.getBase64EncodeStringFromBitmap(mImageFile);
                    onSubmitListener.onPaymentDialogSubmitted(PaymentDialog.this, mImageFile);
                }
            }
        });

        View.OnClickListener radioButtonClickListener = v -> {
            ((RadioButton) v).setChecked(false);
            if (onSubmitListener != null) {
                onSubmitListener.onPaymentDialogRadioButtonClicked(PaymentDialog.this);
            }
        };

        paymentDialogBinding.rbCustomerId.setOnClickListener(radioButtonClickListener);
        paymentDialogBinding.rbCreditslip.setOnClickListener(radioButtonClickListener);

        paymentDialogBinding.imgClose.setOnClickListener(v -> {
            paymentDialogBinding.imgClose.setVisibility(View.INVISIBLE);
            paymentDialogBinding.imgPreview.setVisibility(View.INVISIBLE);
            if (shouldShowCustomerId) {
                paymentDialogBinding.rbCreditslip.setVisibility(View.INVISIBLE);
                paymentDialogBinding.rbCustomerId.setVisibility(View.VISIBLE);
            } else {
                paymentDialogBinding.rbCreditslip.setVisibility(View.VISIBLE);
                paymentDialogBinding.rbCustomerId.setVisibility(View.INVISIBLE);
            }

            mImageFile = null;

            paymentDialogBinding.btnSave.setEnabled(false);
            paymentDialogBinding.btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_login_gray));
        });
    }

    /*public void setOnSubmitListener(PaymentDialog.OnSubmitListener onSubmitListener) {
        this.onSubmitListener = onSubmitListener;
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onSubmitListener = (OnSubmitListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString()
                    + " must implement PaymentDialog.OnSubmitListener");
        }
    }

    private void processRadioButtonClick(CompoundButton buttonView) {
        for (RadioButton button : radioButtons) {
            if (button != buttonView) button.setChecked(false);
        }
    }

    /*public static void startWithActionListener(FragmentManager fragmentManager, boolean shouldShowCustomerId, OnSubmitListener listener) {
        PaymentDialog paymentDialog = new PaymentDialog();
        paymentDialog.shouldShowCustomerId = shouldShowCustomerId;
        paymentDialog.setOnSubmitListener(listener);
        paymentDialog.show(fragmentManager, "dialog");
    }*/

    public void showImagePreview(Uri imageUri, boolean isGalleryImage) {


//        Picasso.with(getContext()).load(imageFile).placeholder(R.drawable.placeholder_image).into(paymentDialogBinding.imgPreview);
        Picasso.get().load(imageUri).placeholder(R.drawable.placeholder_image).into(paymentDialogBinding.imgPreview);
        paymentDialogBinding.imgPreview.setVisibility(View.VISIBLE);
        paymentDialogBinding.imgClose.setVisibility(View.VISIBLE);
        paymentDialogBinding.rbCreditslip.setVisibility(View.INVISIBLE);
        paymentDialogBinding.rbCustomerId.setVisibility(View.INVISIBLE);

        paymentDialogBinding.btnSave.setEnabled(true);
        paymentDialogBinding.btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        if (isGalleryImage) {
            try {
                Bitmap bitmap = ((BitmapDrawable) paymentDialogBinding.imgPreview.getDrawable()).getBitmap();
                Uri mImageUri = Util.bitmapToFile(getContext(), bitmap, new File(Util.createImageUri(getContext()).getPath()));
                mImageFile = (new File(mImageUri.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            mImageFile = (new File(imageUri.getPath()));
        }


    }


    public interface OnSubmitListener {
        void onPaymentDialogRadioButtonClicked(PaymentDialog dialog);

        void onPaymentDialogSubmitted(PaymentDialog dialog, File imageBase64String);

        void onPaymentDialogSkip(PaymentDialog dialog);
    }
}