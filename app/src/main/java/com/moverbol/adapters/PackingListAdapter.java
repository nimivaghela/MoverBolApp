package com.moverbol.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.PackingListItemBinding;
import com.moverbol.model.PackingListPojo;

/**
 * Created by Admin on 28-09-2017.
 */

public class PackingListAdapter  extends RecyclerView.Adapter<PackingListAdapter.HomeItemHolder>{
    private ObservableArrayList<PackingListPojo> homeList;
    private RecyclerView rvHome;
    private Context context;
    //private String type;

    public PackingListAdapter(RecyclerView rvHome, ObservableArrayList<PackingListPojo> homeList) {
        this.homeList = homeList;
        this.rvHome = rvHome;
        this.context = rvHome.getContext();

    }

    @Override
    public PackingListAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_packinglist_item, parent, false);
        return new PackingListAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(PackingListAdapter.HomeItemHolder holder, int position) {
        holder.packingListItemBinding.setPackinglistPojo(homeList.get(position));
       // holder.packingListItemBinding.setPosition(position);
        holder.packingListItemBinding.executePendingBindings();
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
        public PackingListItemBinding packingListItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            packingListItemBinding = DataBindingUtil.bind(itemView);
        }
    }

}
