package com.moverbol.views.activities.storageinventory;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.moverbol.R;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.AddRiderBinding;

/**
 * Created by Admin on 25-09-2017.
 */

public class AddRiderActivity extends BaseAppCompactActivity {
    private AddRiderBinding addRiderBinding;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addRiderBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_rider);
        setToolbar(addRiderBinding.toolbar.toolbar, getString(R.string.add_rider), R.drawable.ic_arrow_back_white_24dp);
    }
}