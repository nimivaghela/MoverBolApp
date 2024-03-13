package com.moverbol.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.ValuationItemBinding;
import com.moverbol.model.valuationPage.ValuationItemPojo;
import com.moverbol.util.MoversPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkashM on 31/1/18.
 */

public class ValuationAdapter extends RecyclerView.Adapter<ValuationAdapter.ViewHolder> {

    private List<ValuationItemPojo> homeList;

    public ValuationAdapter() {
        this.homeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_valuation_item, parent, false);
        return new ValuationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setObj(homeList.get(position));
        holder.binding.setPosition(position);
        holder.binding.setCurrencySymbol(MoversPreferences.getInstance(holder.binding.getRoot().getContext()).getCurrencySymbol());
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return homeList == null ? 0 : homeList.size();
    }

    public void setHomeList(List<ValuationItemPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ValuationItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            itemView.setOnClickListener(v -> {
                for (int i = 0; i < homeList.size(); i++) {
                    homeList.get(i).setSelected(false);
                }

                homeList.get(getAdapterPosition()).setSelected(true);
            });

            binding.radioSelected.setOnClickListener(v -> {
                for (int i = 0; i < homeList.size(); i++) {
                    homeList.get(i).setSelected(false);
                }

                homeList.get(getAdapterPosition()).setSelected(true);
            });

            binding.edtxtDeclaredValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    binding.setObj(homeList.get(getAdapterPosition()));
                }
            });
        }
    }
}
