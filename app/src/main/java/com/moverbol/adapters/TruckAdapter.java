package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.TruckItemBinding;
import com.moverbol.model.TruckPojo;

import java.util.List;

/**
 * Created by AkashM on 29/11/17.
 */

public class TruckAdapter extends RecyclerView.Adapter<TruckAdapter.HomeItemHolder> {

    private List<TruckPojo> homeList;
    private String type = "";
    private View.OnClickListener onClickListener;

    public TruckAdapter(List<TruckPojo> homeList, View.OnClickListener onClickListener) {
        this.homeList = homeList;
        this.onClickListener = onClickListener;
    }

    @Override
    public TruckAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_truck_item, parent, false);
        return new TruckAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(TruckAdapter.HomeItemHolder holder, int position) {
        holder.truckItemBinding.setTruckPojo(homeList.get(position));
        holder.truckItemBinding.setPosition(position);
        holder.truckItemBinding.setType(type);
        holder.truckItemBinding.executePendingBindings();
    }


    public void setHomeList(List<TruckPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public List<TruckPojo> getHomeList() {
        return homeList;
    }

    public void setType(String type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void unSelectAll(){

        if(homeList==null || homeList.isEmpty()){
            return;
        }

        for (int i = 0; i < homeList.size(); i++) {
            homeList.get(i).setSelectedForDelete(false);
        }

        notifyDataSetChanged();
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
        public TruckItemBinding truckItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            truckItemBinding = DataBindingUtil.bind(itemView);

            truckItemBinding.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int adapterPosition = Integer.parseInt((String) v.getTag());
//                    adapterPosition = adapterPosition - 1;
//                    v.getContext().startActivity(new Intent(v.getContext(), AddTruckActivity.class).putExtra(Constants.KEY_ADAPTER_POSITION, adapterPosition + ""));
                    TruckAdapter.this.onClickListener.onClick(v);
                }
            });

            truckItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (truckItemBinding.checkBox.getVisibility() == View.VISIBLE)
                        truckItemBinding.checkBox.toggle();
                }
            });
        }

    }

}
