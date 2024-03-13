package com.moverbol.views.activities.storageinventory;

import android.content.Intent;
import android.content.res.Configuration;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.moverbol.R;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.AddItemBinding;
import com.moverbol.databinding.ExceptionSymbolBinding;
import com.moverbol.databinding.LocationBinding;
import com.moverbol.util.Util;

import java.util.LinkedHashSet;

/**
 * Created by Admin on 04-10-2017.
 */

public class AddItemActivity extends BaseAppCompactActivity implements View.OnClickListener {
    private AddItemBinding addItemBinding;
    private LinkedHashSet<String> locationSymbols = new LinkedHashSet<>();
    private LinkedHashSet<String> exceptionSymbols = new LinkedHashSet<>();
    private BottomSheetDialog mBottomSheetDialog;
    private BottomSheetDialog bottomSheetDialog;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_item);
        setToolbar(addItemBinding.toolbar.toolbar, "Add Item", R.drawable.ic_arrow_back_white_24dp);
        addItemBinding.ivAddLocationbottomsheet.setOnClickListener(this);
        addItemBinding.ivAddExceptionbottomsheet.setOnClickListener(this);

        Intent intent = getIntent();
        String layout = intent.getStringExtra("AddItem");

        if (layout.contentEquals("edit_item")) {
            setToolbar(addItemBinding.toolbar.toolbar, "Edit Item", R.drawable.ic_arrow_back_white_24dp);
            editItem();
        }
    }

    public void openLocationSymbol(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            locationSymbols.add(checkBox.getText().toString());
        } else {
            locationSymbols.remove(checkBox.getText().toString());
        }
    }

    public void openExceptionSymbol(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            exceptionSymbols.add(checkBox.getText().toString());
        } else {
            exceptionSymbols.remove(checkBox.getText().toString());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_add_locationbottomsheet) {
            locationSymbols.clear();
            mBottomSheetDialog = new BottomSheetDialog(this);
            LocationBinding locationBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_bottom_sheet, null, false);
            locationBinding.setAddItemActivity(AddItemActivity.this);
            mBottomSheetDialog.setContentView(locationBinding.getRoot());
            mBottomSheetDialog.show();
//            FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
            FrameLayout bottomSheet = mBottomSheetDialog.findViewById(R.id.design_bottom_sheet);//Akash changed after migrating to androidx.
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        if (id == R.id.iv_add_exceptionbottomsheet) {
            exceptionSymbols.clear();
            bottomSheetDialog = new BottomSheetDialog(this);
            ExceptionSymbolBinding exceptionSymbolBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_exception_bottomsheet, null, false);
            exceptionSymbolBinding.setAddItemActivityexception(AddItemActivity.this);
            bottomSheetDialog.setContentView(exceptionSymbolBinding.getRoot());
            bottomSheetDialog.show();
//            FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);//Akash changed after migrating to androidx.
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void cancelBottomSheet(View view) {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
    }

    public void cancelExceptionBottomSheet(View view) {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }
    }

    public void onSelectingExceptionSymbol(View view) {
        String origin = null;

        for (String str : exceptionSymbols) {

            if (origin == null) {
                origin = str;
            } else {
                origin = origin + "," + str;
            }
        }
        addItemBinding.etExcepDestination.setText(origin);
        bottomSheetDialog.dismiss();
    }

    public void onSelectingLocationSymbol(View view) {

        String origin = null;
        for (String str : locationSymbols) {
            if (origin == null) {
                origin = str;
            } else {
                origin = origin + "," + str;
            }
        }
        addItemBinding.etConditionOrigin.setText(origin);
        mBottomSheetDialog.dismiss();
    }

    public void editItem() {
      addItemBinding.txtItemNoValue.setVisibility(View.GONE);
        addItemBinding.txtItemNo.setVisibility(View.GONE);
        addItemBinding.txtRef.setVisibility(View.GONE);
        addItemBinding.txtModel.setVisibility(View.GONE);
        addItemBinding.highValueConstraint.setVisibility(View.GONE);

        Util.slide_up(this, addItemBinding.txtItemNoValue);
        Util.slide_up(this, addItemBinding.txtItemNo);
        Util.slide_up(this, addItemBinding.txtRef);
        Util.slide_up(this, addItemBinding.txtModel);
        Util.slide_up(this, addItemBinding.highValueConstraint);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) addItemBinding.txtArticles.getLayoutParams();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            params.setMargins(10, 40, 10, 0);
            params.width = 1850;
        } else {
            params.width = 1150;
            params.setMargins(10, 40, 10, 0);
        }
        addItemBinding.txtArticles.setLayoutParams(params);
    }
}