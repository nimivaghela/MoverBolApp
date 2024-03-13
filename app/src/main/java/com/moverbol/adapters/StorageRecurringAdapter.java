package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.StorageItemBinding;
import com.moverbol.model.confirmationPage.StorageChargesPojo;

import java.util.ArrayList;
import java.util.List;

public class StorageRecurringAdapter extends RecyclerView.Adapter<StorageRecurringAdapter.HomeItemHolder> {
    private List<StorageChargesPojo> homeList;
    private boolean editValues;

    private OnEditClickedListener onEditClickedListener;

    public interface OnEditClickedListener{
        void onEditClicked(StorageChargesPojo storageChargesPojo, int adapterPosition);
    }

    public StorageRecurringAdapter() {
        this.homeList = new ArrayList<>();
    }

    @Override
    public HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_storage_charges_item, parent, false);
        return new HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeItemHolder holder, int position) {
        holder.storageItemBinding.setStoragePojo(homeList.get(position));
        holder.storageItemBinding.setPosition(position);
        holder.storageItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (homeList == null) {
            return 0;
        } else {
            return homeList.size();
        }
    }

    public void setHomeList(List<StorageChargesPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public void addToHomeList(StorageChargesPojo storageChargesPojo) {
        this.homeList.add(storageChargesPojo);
        notifyItemInserted(this.homeList.size() - 1);
    }


    public void showEditOption(){
        if(homeList!=null){
            for (int i = 0; i < homeList.size(); i++) {
                homeList.get(i).setShowEditOption(true);
            }
        }
        notifyDataSetChanged();
    }

    public void hideEditOption(){
        if(homeList!=null){
            for (int i = 0; i < homeList.size(); i++) {
                homeList.get(i).setShowEditOption(false);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnEditClickedListener(OnEditClickedListener onEditClickedListener) {
        this.onEditClickedListener = onEditClickedListener;
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public StorageItemBinding storageItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            storageItemBinding = DataBindingUtil.bind(itemView);

        }
    }
}
