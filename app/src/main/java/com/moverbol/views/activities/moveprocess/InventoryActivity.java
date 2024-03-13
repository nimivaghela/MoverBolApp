package com.moverbol.views.activities.moveprocess;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.moverbol.R;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.InventoryActivityBinding;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.viewmodels.inventory.InventoryRiderVM;
import com.moverbol.viewmodels.inventory.PackingListVM;
import com.moverbol.views.activities.storageinventory.AddRiderActivity;
import com.moverbol.views.activities.storageinventory.AddStorageInventoryActivity;
import com.moverbol.views.activities.storageinventory.PackingListActivity;
import com.moverbol.views.activities.storageinventory.RiderActivity;

/**
 * Created by Admin on 27-09-2017.
 */

public class InventoryActivity extends BaseAppCompactActivity {
    private PackingListVM packingListVM;
    private InventoryRiderVM inventoryRiderVM;
    private InventoryActivityBinding inventoryActivityBinding;
    private int dy = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inventoryActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_inventory);
        setToolbar(inventoryActivityBinding.toolbar.toolbar, getString(R.string.inventory), R.drawable.ic_arrow_back_white_24dp);


        if (packingListVM == null) {
            packingListVM = new PackingListVM();
        }
        packingListVM.loadData();
        inventoryActivityBinding.setPackinglistVM(packingListVM);
        inventoryActivityBinding.rvPackinglist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        inventoryActivityBinding.svInventory.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int currentY = inventoryActivityBinding.svInventory.getScrollY();
                if (dy == -1) {
                    dy = currentY;
                }
                if (currentY > dy) {
                    showHideFAB(View.INVISIBLE);
                    dy = currentY;
                } else if (currentY < dy) {
                    showHideFAB(View.VISIBLE);
                    dy = currentY;
                }
            }
        });

        if (inventoryRiderVM == null) {
            inventoryRiderVM = new InventoryRiderVM();
        }
        inventoryRiderVM.loadData();
        inventoryActivityBinding.setRiderlistVM(inventoryRiderVM);
        inventoryActivityBinding.rvRider.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    public void openAddStorageInventoryActivity(View view) {
        startActivity(new Intent(this, AddStorageInventoryActivity.class));
    }


    public void showHideFAB(int visibility) {
        if (visibility == View.VISIBLE) {
            inventoryActivityBinding.floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        } else if (visibility == View.INVISIBLE) {
            inventoryActivityBinding.floatingActionButton.animate().translationY(inventoryActivityBinding.floatingActionButton.getHeight() + 100).setInterpolator(new AccelerateInterpolator(2)).start();
        }
    }


    public void openPackingListActivity(View view) {
        startActivity(new Intent(this,PackingListActivity.class));
    }
    public void openRiderActivity(View view) {
        startActivity(new Intent(this,RiderActivity.class));

    }
    public void openAddRiderActivity(View view) {
        startActivity(new Intent(this,AddRiderActivity.class));

    }
}



