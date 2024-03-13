package com.moverbol.custom.dialogs;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.AddNoteDialogBinding;

/**
 * Created by Admin on 09-10-2017.
 */

public class AddNoteDialog extends BaseDialogFragment {
    private AddNoteDialogBinding binding;
    private OnNoteSubmittedListener onNoteSubmittedListener;
    
    public interface OnNoteSubmittedListener{
        void onNewNoteSubmitted(String note);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_add_note, container, false);
        setCancelable(false);
        
        setActionListeners();
        
        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            onNoteSubmittedListener = (OnNoteSubmittedListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement AddNoteDialog.OnNoteSubmittedListener");
        }

    }


    private void setActionListeners() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNoteDialog.this.dismiss();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNoteSubmittedListener!=null){
                    if(TextUtils.isEmpty(binding.edtxtNote.getText().toString())){
                        binding.edtxtNote.setError(getString(R.string.empty_field_error));
                        return;
                    }
                    onNoteSubmittedListener.onNewNoteSubmitted(binding.edtxtNote.getText().toString());
                }
                AddNoteDialog.this.dismiss();
            }
        });
    }

}