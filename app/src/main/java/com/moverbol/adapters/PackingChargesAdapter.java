package com.moverbol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.PackingtItemBinding;
import com.moverbol.model.confirmationPage.PackingChargesPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 27-09-2017.
 */

public class PackingChargesAdapter extends RecyclerView.Adapter<PackingChargesAdapter.HomeItemHolder> {
    private List<PackingChargesPojo> chargesList;
    private OnEditClickedListener onEditClickedListener;

    private boolean editValues;

    public interface OnEditClickedListener{
        void onEditClicked(PackingChargesPojo packingChargesPojo, int adapterPosition);
    }

    public PackingChargesAdapter(boolean editValues) {
        this.chargesList = new ArrayList<>();
        this.editValues = editValues;
    }

    @Override
    public HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_packing_item, parent, false);
        return new HomeItemHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeItemHolder holder, int position) {
        holder.packingItemBinding.setPosition(position);
        holder.packingItemBinding.setChargesPojo(chargesList.get(position));
        holder.packingItemBinding.setEditValues(editValues);
        holder.packingItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return chargesList == null ? 0 : chargesList.size();
    }


    public void setChargesList(List<PackingChargesPojo> chargesList) {
        this.chargesList = chargesList;
        notifyDataSetChanged();
    }

    public void addToHomeList(PackingChargesPojo packingChargesPojo) {
        this.chargesList.add(packingChargesPojo);
        notifyItemInserted(chargesList.size()-1);
    }

    public List<PackingChargesPojo> getChargeList() {
        return chargesList;
    }


    public void showEditOption(){
        if(chargesList!=null){
            for (int i = 0; i < chargesList.size(); i++) {
                chargesList.get(i).setShowEditOption(true);
            }
        }
        notifyDataSetChanged();
    }

    public void hideEditOption(){
        if(chargesList!=null){
            for (int i = 0; i < chargesList.size(); i++) {
                chargesList.get(i).setShowEditOption(false);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnEditClickedListener(OnEditClickedListener onEditClickedListener) {
        this.onEditClickedListener = onEditClickedListener;
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        PackingtItemBinding packingItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            packingItemBinding = DataBindingUtil.bind(itemView);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onEditClickedListener!=null && packingItemBinding.frameLayout.getVisibility()==View.VISIBLE){
                        onEditClickedListener.onEditClicked(chargesList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });*/

        }
    }
}