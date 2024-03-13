package com.moverbol.custom.dialogs;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moverbol.R;

/**
 * Created by AkashM on 28/3/18.
 */

public class TermsAndPoliciesDialog extends DialogFragment {

    private String title;
    private String message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.CustomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_terms_policies, container, true);

        TextView txtTitle = rootView.findViewById(R.id.txt_title);
        TextView txtMessage = rootView.findViewById(R.id.txt_message);
        ImageView imgClose = rootView.findViewById(R.id.img_close);
        txtMessage.setMovementMethod(new ScrollingMovementMethod());
        txtTitle.setText(title);
        txtMessage.setText(message);
        imgClose.setOnClickListener(v -> TermsAndPoliciesDialog.this.dismiss());

        return rootView;
    }

    public void setTitleAndMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }

}
