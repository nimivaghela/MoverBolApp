package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.PaymentModeListItemBinding;
import com.moverbol.model.billOfLading.payment.PaymentHistoryDetailsModel;
import com.moverbol.util.MoversPreferences;

import java.util.ArrayList;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.HomeItemHolder> {
    private ArrayList<PaymentHistoryDetailsModel> homeList;

    public PaymentHistoryAdapter() {
        this.homeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public HomeItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_payment_history_list_item, parent, false);
        return new HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemHolder holder, int position) {
        holder.binding.setIndex(position + 1);
        holder.binding.setPosition(position);
        holder.binding.setObj(homeList.get(position));
        holder.binding.setCurrencySymbol(MoversPreferences.getInstance(holder.itemView.getContext()).getCurrencySymbol());
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    public void setHomeList(ArrayList<PaymentHistoryDetailsModel> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public PaymentModeListItemBinding binding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
