package com.moverbol.custom.dialogs;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.SelectCameraOrGalleryBinding;

/**
 * Created by AkashM on 7/2/18.
 */

public class SelectCameraOrGalleryDialog extends BaseDialogFragment {

    private SelectCameraOrGalleryBinding binding;
    private OnSelectionListener onSelectionListener;

    public interface OnSelectionListener{
        void onCameraSelected(SelectCameraOrGalleryDialog dialog);
        void onGallerySelected(SelectCameraOrGalleryDialog dialog);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialogue_select_camera_or_gallery, container, false);
        setCancelable(true);

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCameraOrGalleryDialog.this.dismiss();
            }
        });

        binding.linrlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSelectionListener!=null)
                onSelectionListener.onCameraSelected(SelectCameraOrGalleryDialog.this);
            }
        });

        binding.linrlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSelectionListener!=null)
                onSelectionListener.onGallerySelected(SelectCameraOrGalleryDialog.this);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onSelectionListener = (OnSelectionListener) context;
        } catch (ClassCastException cce){
            throw new ClassCastException(context.toString()
            + " must implement SelectCameraOrGalleryDialog.OnSelectionListener");
        }
    }

    /*public void setOnSelectionListener(OnSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }*/
}
