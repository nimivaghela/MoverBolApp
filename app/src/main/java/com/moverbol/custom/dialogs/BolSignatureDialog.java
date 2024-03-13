package com.moverbol.custom.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.databinding.BolSignatureDialogBinding;
import com.moverbol.model.confirmationPage.TermsAndConditionPojo;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;

public class BolSignatureDialog extends BaseDialogFragment {

    private BolSignatureDialogBinding binding;
    private String totalAmount;
    private FragmentManager mFragmentManager;
    private OnSignatureSubmittedListener onSignatureSubmittedListener;
    private TermsAndConditionPojo termsAndConditionPojo;
    private String companyName;



    /*public static void startWithActionListeners(FragmentManager supportFragmentManager, String totalAmount, String rescheduleTitleText, String companyPolicyTitleText, OnSignatureSubmittedListener onSignatureSubmittedListener) {
        BolSignatureDialog dialog = new BolSignatureDialog();
        dialog.totalAmount = totalAmount;
        dialog.onSignatureSubmittedListener = onSignatureSubmittedListener;
        dialog.mFragmentManager = supportFragmentManager;
        dialog.mRescheduleTitleText = rescheduleTitleText;
        dialog.mCompanyPolicyTitleText = companyPolicyTitleText;
        dialog.show(dialog.mFragmentManager, "dialog");
    }*/


    public static void start(FragmentManager supportFragmentManager, String totalAmount, TermsAndConditionPojo termsAndConditionPojo, String companyName) {
        BolSignatureDialog dialog = new BolSignatureDialog();
        dialog.totalAmount = totalAmount;
        dialog.mFragmentManager = supportFragmentManager;
        dialog.termsAndConditionPojo = termsAndConditionPojo;
        dialog.companyName = companyName;
        dialog.show(dialog.mFragmentManager, "dialog");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            onSignatureSubmittedListener = (OnSignatureSubmittedListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement BolSignatureDialog.OnSignatureSubmittedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_bol_signature_dialogue, container, false);
        setCancelable(true);

        initialisation();

        setActionListeners();

        return binding.getRoot();
    }

    private void initialisation() {

        String currencySymbol = MoversPreferences.getInstance(getContext()).getCurrencySymbol();

        if (!totalAmount.isEmpty()) {
            totalAmount = currencySymbol + totalAmount;
        }
        binding.txtTotalValue.setText(Util.getGeneralFormattedDecimalString(totalAmount));
        binding.setShowReschedule(termsAndConditionPojo.getTcShowFlag3());
        binding.setShowPolicy(termsAndConditionPojo.getTcShowFlag4());

        setUpTextLinks(termsAndConditionPojo.getRescheduleTitleText(companyName), termsAndConditionPojo.getCompanyPolicyTitleText(companyName));
    }

    private void setActionListeners() {

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (onSignatureSubmittedListener != null) {
                        // String signature = Util.getBase64EncodeStringFromBitmap(binding.signaturePad.getSignatureBitmap());
                        onSignatureSubmittedListener.onSignatureSubmitted(binding.signaturePad.getSignatureBitmap(), BolSignatureDialog.this);
                    }
                }
            }
        });

        binding.txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.signaturePad.clear();
            }
        });

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private boolean validate() {
        if (termsAndConditionPojo.getTcShowFlag3() == 1 && !binding.chkReschedule.isChecked() || (termsAndConditionPojo.getTcShowFlag4() == 1 && !binding.chkPolicies.isChecked())) {
            Snackbar.make(binding.getRoot(), getText(R.string.policy_agreement_request_message), Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.signaturePad.isEmpty()) {
            Snackbar.make(binding.getRoot(), R.string.signature_required_error, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void setUpTextLinks(String rescheduleTitleText, String companyPolicyTitleText) {

        if (rescheduleTitleText == null) {
            rescheduleTitleText = "";
        }

        if (companyPolicyTitleText == null) {
            companyPolicyTitleText = "";
        }

        SpannableString rescheduleSpan = new SpannableString(rescheduleTitleText);
        SpannableString companyPolicySpan = new SpannableString(companyPolicyTitleText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (onSignatureSubmittedListener != null) {
                    onSignatureSubmittedListener.showReschedulePolicies();
                }
            }
        };

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (onSignatureSubmittedListener != null) {
                    onSignatureSubmittedListener.showCompanyPolicies();
                }
            }
        };

        /*private void openTermsDialog(final String title, final String message) {
            final TermsAndPoliciesDialog termsAndPoliciesDialog = new TermsAndPoliciesDialog();

            termsAndPoliciesDialog.setTitleAndMessage(title, message);

            termsAndPoliciesDialog.show(getSupportFragmentManager(), "dialog");
        }*/

        rescheduleSpan.setSpan(clickableSpan, 0, rescheduleSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rescheduleSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#51b2a9")), 0, rescheduleSpan.length(), 0);

        companyPolicySpan.setSpan(clickableSpan1, 0, companyPolicySpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        companyPolicySpan.setSpan(new ForegroundColorSpan(Color.parseColor("#51b2a9")), 0, companyPolicySpan.length(), 0);
        binding.txtReschedule.setText(rescheduleSpan, TextView.BufferType.SPANNABLE);
        binding.txtReschedule.setMovementMethod(LinkMovementMethod.getInstance());

        binding.txtPolicies.setText(companyPolicySpan, TextView.BufferType.SPANNABLE);
        binding.txtPolicies.setMovementMethod(LinkMovementMethod.getInstance());

    }


    public interface OnSignatureSubmittedListener {
        void onSignatureSubmitted(Bitmap signatureFile, BolSignatureDialog bolSignatureDialog);

        void showReschedulePolicies();

        void showCompanyPolicies();
    }
}
