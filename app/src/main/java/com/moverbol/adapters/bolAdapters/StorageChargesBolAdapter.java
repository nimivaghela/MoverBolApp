package com.moverbol.adapters.bolAdapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.StorageItemBOLBinding;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkashM on 13/4/18.
 */
public class StorageChargesBolAdapter extends RecyclerView.Adapter<StorageChargesBolAdapter.HomeItemHolder> {
    private List<CommonChargesRequestModel> homeList;

    private OnEditClickedListener onEditClickedListener;

    public interface OnEditClickedListener{
        void onEditClicked(CommonChargesRequestModel commonChargesRequestModel, int adapterPosition);
    }

    public StorageChargesBolAdapter() {
        this.homeList = new ArrayList<>();
    }

    @Override
    public StorageChargesBolAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_storage_charges_bol_item, parent, false);
        return new StorageChargesBolAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(StorageChargesBolAdapter.HomeItemHolder holder, int position) {
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

    public void setHomeList(List<CommonChargesRequestModel> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public void addToHomeList(CommonChargesRequestModel moveChargesAutoPricingPojo, int adapterPosition) {
        if(adapterPosition > this.homeList.size() || adapterPosition < 0){
            this.homeList.add(moveChargesAutoPricingPojo);
            this.notifyItemInserted(homeList.size() - 1);
        } else {
            this.homeList.remove(adapterPosition);
            this.homeList.add( adapterPosition, moveChargesAutoPricingPojo);
            this.notifyItemChanged(adapterPosition);
        }

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
        private StorageItemBOLBinding storageItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            storageItemBinding = DataBindingUtil.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onEditClickedListener!=null && storageItemBinding.frameLayout.getVisibility()==View.VISIBLE){
                        onEditClickedListener.onEditClicked(homeList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }
    }
}
