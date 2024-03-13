package com.moverbol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.CratingChargesItemBinding;
import com.moverbol.model.confirmationPage.CratingChargesPojo;

import java.util.ArrayList;
import java.util.List;

public class CratingChargesAdapter extends RecyclerView.Adapter<CratingChargesAdapter.HomeItemHolder> {

    private List<CratingChargesPojo> chargesList;

    public CratingChargesAdapter() {
        this.chargesList = new ArrayList<>();
    }

    @Override
    public CratingChargesAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_crating_charges_iitems, parent, false);
        return new CratingChargesAdapter.HomeItemHolder(view);
    }


    @Override
    public void onBindViewHolder(CratingChargesAdapter.HomeItemHolder holder, int position) {
        holder.binding.setPosition(position);
        holder.binding.setChargesPojo(chargesList.get(position));
        holder.binding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return chargesList == null ? 0 : chargesList.size();
    }

    public void setChargesList(List<CratingChargesPojo> chargesList) {
        this.chargesList = chargesList;
        notifyDataSetChanged();
    }

    public List<CratingChargesPojo> getChargesList() {
        return chargesList;
    }

    public void addToHomeList(CratingChargesPojo cratingChargesPojo) {
        this.chargesList.add(cratingChargesPojo);
        notifyItemInserted(this.chargesList.size() - 1);
    }


    public class HomeItemHolder extends RecyclerView.ViewHolder {
        CratingChargesItemBinding binding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
