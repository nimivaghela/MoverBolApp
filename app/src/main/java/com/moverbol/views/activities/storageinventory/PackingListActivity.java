package com.moverbol.views.activities.storageinventory;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.moverbol.R;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.SignatureDialog;
import com.moverbol.databinding.PackingListEditBinding;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.viewmodels.inventory.PackingListEditVM;

/**
 * Created by Admin on 03-10-2017.
 */

public class PackingListActivity extends BaseAppCompactActivity {
    PackingListEditBinding packingListEditBinding;
    private PackingListEditVM packingListEditVM;
    private int dy = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        packingListEditBinding = DataBindingUtil.setContentView(this, R.layout.activity_packinglist);

        if (packingListEditVM == null) {
            packingListEditVM = new PackingListEditVM();
        }
        packingListEditVM.loadData();
        packingListEditBinding.setPackinglisteditVM(packingListEditVM);
        packingListEditBinding.rvPackingedit.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        packingListEditBinding.rvPackingedit.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {

                    showHideFAB(View.INVISIBLE);
                } else if (dy < 0) {

                    showHideFAB(View.VISIBLE);
                }
            }
        });

        setToolbar(packingListEditBinding.toolbar.toolbar, getString(R.string.packing_list), R.drawable.ic_arrow_back_white_24dp);
    }

    public void showHideFAB(int visibility) {
        if (visibility == View.VISIBLE) {
            packingListEditBinding.floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        } else if (visibility == View.INVISIBLE) {
            packingListEditBinding.floatingActionButton.animate().translationY(packingListEditBinding.floatingActionButton.getHeight() + 100).setInterpolator(new AccelerateInterpolator(2)).start();
        }
    }

    public void openSignatureDialog(View view) {
        SignatureDialog signatureDialogFragment = new SignatureDialog();
        signatureDialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void openAddItemActivity(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("AddItem", "add_item");
        startActivity(intent);
    }

    public void openEditItemActivity(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("AddItem", "edit_item");
        startActivity(intent);

    }

}
