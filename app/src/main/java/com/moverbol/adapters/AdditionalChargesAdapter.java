package com.moverbol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.AdditionalChargesItemBinding;
import com.moverbol.model.confirmationPage.AdditionalChargesPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 27-09-2017.
 */

public class AdditionalChargesAdapter extends RecyclerView.Adapter<AdditionalChargesAdapter.HomeItemHolder> {

    private List<AdditionalChargesPojo> chargesList;

    public AdditionalChargesAdapter() {
        this.chargesList = new ArrayList<>();
    }

    @Override
    public HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_additional_charges_item, parent, false);
        return new HomeItemHolder(view);
    }


    @Override
    public void onBindViewHolder(HomeItemHolder holder, int position) {
        holder.additionalChargesItemBinding.setChargesPojo(chargesList.get(position));
        holder.additionalChargesItemBinding.setPosition(position);
        holder.additionalChargesItemBinding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return chargesList == null ? 0 : chargesList.size();
    }

    public void setChargesList(List<AdditionalChargesPojo> chargesList) {
        this.chargesList = chargesList;
        notifyDataSetChanged();
    }

    public List<AdditionalChargesPojo> getChargeList() {
        return chargesList;
    }

    public void addToHomeList(AdditionalChargesPojo additionalChargesPojo) {
        this.chargesList.add(additionalChargesPojo);
        notifyItemInserted(this.chargesList.size() - 1);
    }


    public class HomeItemHolder extends RecyclerView.ViewHolder {
        AdditionalChargesItemBinding additionalChargesItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            additionalChargesItemBinding = DataBindingUtil.bind(itemView);

        }
    }
}