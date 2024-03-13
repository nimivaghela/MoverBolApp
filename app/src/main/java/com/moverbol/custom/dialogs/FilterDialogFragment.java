package com.moverbol.custom.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.moverbol.R;
import com.moverbol.constants.Constants;

/**
 * Created by sumeet on 31/8/17.
 */

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    /*public static FilterDialogFragment newInstance() {
        return new FilterDialogFragment();
    }*/

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FilterDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_filter, container, false);
        return binding.getRoot();
    }*/

    private View.OnClickListener onClickListener;
    private OnSubmitListener onSubmitListener;
    private RadioGroup radioGroup;
    private Dialog dialog;
    private String jobStatusCode;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.layout_dialog_filter);
        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        Button btnSubmit = dialog.findViewById(R.id.btn_apply);
        btnSubmit.setOnClickListener(this);

        radioGroup = dialog.findViewById(R.id.radioGroup);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();

        if(this.getArguments()!=null)
        jobStatusCode = (String) this.getArguments().get("jobString");
        setSelectedRadio(jobStatusCode);

        //Getting Listener from parent fragment. All other ways were working onl for activity or
        // were not handling screen rotation.
        if(getParentFragment()!=null) {
            try {
                onSubmitListener = (OnSubmitListener) getParentFragment();
            } catch (ClassCastException cce) {
                throw new ClassCastException(getParentFragment().toString()
                        + " must implement FilterDialogFragment.OnSubmitListener");
            }
        }


        super.onCreate(savedInstanceState);
        return dialog;
    }


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void setSelectedRadio(String jobStatusCode){
        if(dialog==null || jobStatusCode==null) return;
        if(jobStatusCode.equals(Constants.JOB_STATUS_ACCEPTED)){
            ((RadioButton)dialog.findViewById(R.id.rb_accept)).setChecked(true);
        }else if(jobStatusCode.equals(Constants.JOB_STATUS_INPROGRESS)){
            ((RadioButton)dialog.findViewById(R.id.rb_progress)).setChecked(true);
        } else if(jobStatusCode.equals(Constants.JOB_STATUS_NEW)){
            ((RadioButton)dialog.findViewById(R.id.rb_new)).setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {

        String jobStatusCode;

        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.rb_accept:
                jobStatusCode = Constants.JOB_STATUS_ACCEPTED;
                break;
            case R.id.rb_progress:
                jobStatusCode = Constants.JOB_STATUS_INPROGRESS;
                break;
            case R.id.rb_new:
                jobStatusCode = Constants.JOB_STATUS_NEW;
                break;

            default:
                jobStatusCode = "";
                break;
        }

        /*view.setTag(jobStatusCode);
        if(onClickListener!=null){
            onClickListener.onClick(view);
        }*/

        if(onSubmitListener!=null){
            onSubmitListener.setOnFilterTagSubmitListener(jobStatusCode);
        }

        dismiss();
    }

    public interface OnSubmitListener {
        void setOnFilterTagSubmitListener(String jobStatusCode);
    }

}
