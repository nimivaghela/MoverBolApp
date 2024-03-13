package com.moverbol.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.PackingListEditItemBinding;
import com.moverbol.model.PackingListEditPojo;

/**
 * Created by Admin on 03-10-2017.
 */

public class PackingListEditAdapter extends RecyclerView.Adapter<PackingListEditAdapter.HomeItemHolder>{
    private ObservableArrayList<PackingListEditPojo> homeList;
    private RecyclerView rvHome;
    private Context context;
    //private String type;

    public PackingListEditAdapter(RecyclerView rvHome, ObservableArrayList<PackingListEditPojo> homeList) {
        this.homeList = homeList;
        this.rvHome = rvHome;
        this.context = rvHome.getContext();

    }

    @Override
    public PackingListEditAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_packing_inventory_list_item, parent, false);
        return new PackingListEditAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(PackingListEditAdapter.HomeItemHolder holder, int position) {
        holder.packingListEditItemBinding.setPackinglisteditPojo(homeList.get(position));
        // holder.packingListItemBinding.setPosition(position);
        holder.packingListEditItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (homeList == null) {
            return 0;
        } else {
            return homeList.size();
        }
    }


    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public PackingListEditItemBinding packingListEditItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            packingListEditItemBinding = DataBindingUtil.bind(itemView);
        }
    }

}
