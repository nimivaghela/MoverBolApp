package com.moverbol.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.InventoryRiderItemBinding;
import com.moverbol.model.InventoryRiderPojo;

/**
 * Created by Admin on 28-09-2017.
 */

public class InventoryRiderAdapter  extends RecyclerView.Adapter<InventoryRiderAdapter.HomeItemHolder>{
    private ObservableArrayList<InventoryRiderPojo> homeList;
    private RecyclerView rvHome;
    private Context context;
    //private String type;

    public InventoryRiderAdapter(RecyclerView rvHome, ObservableArrayList<InventoryRiderPojo> homeList) {
        this.homeList = homeList;
        this.rvHome = rvHome;
        this.context = rvHome.getContext();

    }

    @Override
    public InventoryRiderAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rider_item, parent, false);
        return new InventoryRiderAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(InventoryRiderAdapter.HomeItemHolder holder, int position) {
        holder.inventoryRiderItemBinding.setRiderPojo(homeList.get(position));
        // holder.packingListItemBinding.setPosition(position);
        holder.inventoryRiderItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
       return homeList.size();
    }
    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public InventoryRiderItemBinding inventoryRiderItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            inventoryRiderItemBinding = DataBindingUtil.bind(itemView);
        }
    }

}