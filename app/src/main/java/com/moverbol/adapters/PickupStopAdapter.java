package com.moverbol.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.PickupStopItemBinding;
import com.moverbol.model.ExtraStopsPojo;
import com.moverbol.util.Util;

import java.util.List;

/**
 * Created by sumeet on 11/9/17.
 */

public class PickupStopAdapter extends RecyclerView.Adapter<PickupStopAdapter.HomeItemHolder> {

//    private ObservableArrayList<ExtraStopsPojo> pickupStopList;
    private List<ExtraStopsPojo> pickupStopList;
    private RecyclerView rvHome;
    private Context context;

    public PickupStopAdapter(RecyclerView rvHome, List<ExtraStopsPojo> pickupList) {
        this.pickupStopList = pickupList;
        this.rvHome = rvHome;
        this.context = rvHome.getContext();
    }

    @Override
    public PickupStopAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pickup_stop_item, parent, false);
        return new PickupStopAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(PickupStopAdapter.HomeItemHolder holder, int position) {
        holder.pickupStopBinding.setPickupStopPojo(pickupStopList.get(position));
        holder.pickupStopBinding.setPosition(position);
        //holder.pickupStopBinding.set
        /*holder.homeRvRowItemBinding.setHomeListAdapter(this);*/
        holder.pickupStopBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (pickupStopList == null) {
            return 0;
        } else {
            return pickupStopList.size();
        }
    }


    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public PickupStopItemBinding pickupStopBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            pickupStopBinding = DataBindingUtil.bind(itemView);

            pickupStopBinding.txtPickupContactPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(pickupStopList.get(getAdapterPosition()).getOrganisationPhone())) {
                        Util.startDialer(v.getContext(), pickupStopList.get(getAdapterPosition()).getOrganisationPhone());
                    }
                }
            });


            pickupStopBinding.tvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String address = pickupStopBinding.tvAddress.getText().toString();
                    Util.openMapForGivenAddress(address, v.getContext());
                }
            });
        }
    }

}
