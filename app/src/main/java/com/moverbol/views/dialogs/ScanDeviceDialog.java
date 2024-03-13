package com.moverbol.views.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.moverbol.R;
import com.moverbol.databinding.DialogScanDeviceBinding;

public class ScanDeviceDialog extends DialogFragment {

    private DialogScanDeviceBinding binding;


    public static ScanDeviceDialog newInstance() {
        ScanDeviceDialog f = new ScanDeviceDialog();
       /* Bundle args = new Bundle();
        args.putInt(Constants.DISCOUNT_TYPE, discountType);
        args.putDouble(Constants.DISCOUNT, discountValue);
        f.setArguments(args);*/
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.CustomDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.dialog_scan_device, container, false);
        Glide.with(requireContext()).asGif()
                .load(R.drawable.scan_device)
                .into(binding.imgSearch);
        return binding.getRoot();
    }
}
