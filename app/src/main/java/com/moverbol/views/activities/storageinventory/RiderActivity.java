package com.moverbol.views.activities.storageinventory;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.moverbol.R;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.SignatureDialogFragment;
import com.moverbol.databinding.RiderListEditBinding;
import com.moverbol.util.DividerItemDecoration;
import com.moverbol.viewmodels.inventory.PackingListEditVM;

/**
 * Created by Admin on 03-10-2017.
 */

public class RiderActivity extends BaseAppCompactActivity {
    RiderListEditBinding riderListEditBinding;
    private PackingListEditVM packingListEditVM;
    private int dy = -1;
    private Boolean isFabOpen = false;

    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        riderListEditBinding = DataBindingUtil.setContentView(this, R.layout.activity_riderlist);
        setToolbar(riderListEditBinding.toolbar.toolbar, getString(R.string.rider), R.drawable.ic_arrow_back_white_24dp);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        riderListEditBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });

        if (packingListEditVM == null) {
            packingListEditVM = new PackingListEditVM();
        }
        packingListEditVM.loadData();
        riderListEditBinding.setPackinglisteditVM(packingListEditVM);
        riderListEditBinding.rvRiderlist.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        riderListEditBinding.rvRiderlist.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
    }

    public void showHideFAB(int visibility) {
        if (visibility == View.VISIBLE) {
            riderListEditBinding.floatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            riderListEditBinding.cnAddNewItem.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            riderListEditBinding.cnAddFromPackingList.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        } else if (visibility == View.INVISIBLE) {
            riderListEditBinding.floatingActionButton.animate().translationY(riderListEditBinding.floatingActionButton.getHeight() + 100).setInterpolator(new AccelerateInterpolator(2)).start();
            riderListEditBinding.cnAddNewItem.animate().translationY(riderListEditBinding.cnAddNewItem.getHeight() + 400).setInterpolator(new AccelerateInterpolator(2)).start();
            riderListEditBinding.cnAddFromPackingList.animate().translationY(riderListEditBinding.cnAddFromPackingList.getHeight() + 300).setInterpolator(new AccelerateInterpolator(2)).start();
        }
    }

    public void animateFAB() {
        if (isFabOpen) {
            //riderListEditBinding.floatingActionButton.startAnimation(rotate_backward);
            riderListEditBinding.cnAddFromPackingList.startAnimation(fab_close);
            riderListEditBinding.cnAddNewItem.startAnimation(fab_close);
            riderListEditBinding.cnAddFromPackingList.setClickable(false);
            riderListEditBinding.cnAddNewItem.setClickable(false);
            isFabOpen = false;
        } else {
            //riderListEditBinding.floatingActionButton.startAnimation(rotate_forward);
            riderListEditBinding.cnAddFromPackingList.startAnimation(fab_open);
            riderListEditBinding.cnAddNewItem.startAnimation(fab_open);
            riderListEditBinding.cnAddFromPackingList.setClickable(true);
            riderListEditBinding.cnAddNewItem.setClickable(true);
            isFabOpen = true;
        }
    }

    public void openAddItemActivity(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("AddItem","add_item");
        startActivity(intent);
    }

   /* public void openAddRiderActivity(View view) {
        startActivity(new Intent(this, AddRiderActivity.class));
    }*/

    public void openSignatureDialog(View view) {
        SignatureDialogFragment signatureDialogFragment = new SignatureDialogFragment();
        signatureDialogFragment.show(getSupportFragmentManager(), "dialog");
    }
    public void openEditItemActivity(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
       intent.putExtra("AddItem","edit_item");
        startActivity(intent);

    }
}
