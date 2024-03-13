package com.moverbol.views.activities.storageinventory;

import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.moverbol.R;
import com.moverbol.adapters.ColorListAdapter;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.CustomDialogDataBind;
import com.moverbol.databinding.AddStorageInventoryBinding;
import com.moverbol.databinding.ColorBinding;
import com.moverbol.model.ColorPojo;
import com.moverbol.util.ItemOffsetDecoration;
import com.moverbol.viewmodels.color.ColorListModel;

/**
 * Created by Admin on 25-09-2017.
 */

public class AddStorageInventoryActivity extends BaseAppCompactActivity {

    private AddStorageInventoryBinding addStorageInventoryBinding;
    private String selectedColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addStorageInventoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_storage_inventory);
        setToolbar(addStorageInventoryBinding.toolbar.toolbar, getString(R.string.add_storage_inventory), R.drawable.ic_arrow_back_white_24dp);
    }



    public void openColorDialog(View view) {
        final ColorBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_color_dialog, null, false);
        binding.setColorList(new ColorListModel());
        if (selectedColor != null) {
            binding.setColorName(selectedColor);
        }
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        binding.recyclerColors.addItemDecoration(itemDecoration);
        final CustomDialogDataBind customDialogDataBind = new CustomDialogDataBind();
        android.content.DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ColorListAdapter colorListAdapter = (ColorListAdapter) binding.recyclerColors.getAdapter();
                ColorPojo colorPojo = colorListAdapter.getSelectedColor();
                if (colorPojo != null) {
                    /*addStorageInventoryBinding.setColor(colorPojo);
                    addStorageInventoryBinding.executePendingBindings();*/
                    selectedColor = colorPojo.color;
                    addStorageInventoryBinding.viewColor.setBackgroundColor(Color.parseColor(colorPojo.color));
                }
                customDialogDataBind.dismiss();
            }
        };
        customDialogDataBind.initializeVariable(getString(R.string.change_color_list), null, getString(R.string.change), getString(R.string.cancel), okListener, null, binding);
        customDialogDataBind.show(getSupportFragmentManager(), "Dialog");
    }

}