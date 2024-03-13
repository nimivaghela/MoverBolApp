package com.moverbol.custom.dialogs;

import android.app.Dialog;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.moverbol.R;

public class RejectDialogFragment extends DialogFragment implements View.OnClickListener {
    //public FilterFragment.onSubmitListener mListener;

    private View.OnClickListener onClickListener;
    private int homeAdapterPosition;
    private EditText txt_comment;
    private String mComments;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.layout_dialog_reject);
        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        txt_comment = dialog.findViewById(R.id.txt_comment);
        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setHomeAdapterPosition(int homeAdapterPosition) {
        this.homeAdapterPosition = homeAdapterPosition;
    }

    public String getmComments() {
        return mComments;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_submit:

                if(TextUtils.isEmpty(txt_comment.getText().toString().trim())){
                    txt_comment.setError(getString(R.string.empty_field_error));
                    return;
                }
                mComments = txt_comment.getText().toString().trim();
                view.setTag(homeAdapterPosition);
                if(onClickListener!=null){
                    onClickListener.onClick(view);
                }
                dismiss();
                break;

            case R.id.iv_close:
                dismiss();
                break;
        }

    }
}
