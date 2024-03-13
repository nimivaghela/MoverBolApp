package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.ValuationChargesItemBinding;
import com.moverbol.model.confirmationPage.ValuationChargesPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkashM on 25/1/18.
 */

public class ValuationChargesAdapter extends RecyclerView.Adapter<ValuationChargesAdapter.HomeItemHolder>  {

    private List<ValuationChargesPojo> chargesList;

    private OnEditClickedListener onEditClickedListener;

    private boolean editValues;


    public interface OnEditClickedListener{
        void onEditClicked(ValuationChargesPojo valuationChargesPojo, int adapterPosition);
    }

    public ValuationChargesAdapter(boolean editValues) {
        this.chargesList = new ArrayList<>();
        this.editValues = editValues;
    }

    @Override
    public HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_valuation_charges_item, parent, false);
        return new HomeItemHolder(view);
    }


    @Override
    public void onBindViewHolder(HomeItemHolder holder, int position) {
        holder.binding.setChargesPojo(chargesList.get(position));
        holder.binding.setPosition(position);
        holder.binding.setEditValues(editValues);
        holder.binding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return chargesList == null ? 0 : chargesList.size();
    }

    public void setChargesList(List<ValuationChargesPojo> chargesList) {
        this.chargesList = chargesList;
        notifyDataSetChanged();
    }

    public void addToHomeList(ValuationChargesPojo valuationChargesPojo) {
        this.chargesList.add(valuationChargesPojo);
        notifyItemInserted(this.chargesList.size() - 1);
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
        ValuationChargesItemBinding binding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onEditClickedListener!=null && binding.frameLayout.getVisibility()==View.VISIBLE){
                        onEditClickedListener.onEditClicked(chargesList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });*/
        }
    }

}
