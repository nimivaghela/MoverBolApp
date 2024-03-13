package com.moverbol.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.RiderListEditItemBinding;
import com.moverbol.model.PackingListEditPojo;

/**
 * Created by Admin on 03-10-2017.
 */

public class RiderListEditAdapter extends RecyclerView.Adapter<RiderListEditAdapter.HomeItemHolder>{
    private ObservableArrayList<PackingListEditPojo> homeList;
    private RecyclerView rvHome;
    private Context context;
    //private String type;

    public RiderListEditAdapter(RecyclerView rvHome, ObservableArrayList<PackingListEditPojo> homeList) {
        this.homeList = homeList;
        this.rvHome = rvHome;
        this.context = rvHome.getContext();

    }

    @Override
    public RiderListEditAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rider_edit_item, parent, false);
        return new RiderListEditAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RiderListEditAdapter.HomeItemHolder holder, int position) {
        holder.riderListEditItemBinding.setPackinglisteditPojo(homeList.get(position));
        // holder.packingListItemBinding.setPosition(position);
        holder.riderListEditItemBinding.executePendingBindings();
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
        public RiderListEditItemBinding riderListEditItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            riderListEditItemBinding = DataBindingUtil.bind(itemView);
        }
    }

}
