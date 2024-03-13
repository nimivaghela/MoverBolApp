package com.moverbol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.ItemCarryForwardDetailsBinding;
import com.moverbol.databinding.ItemCarryForwardDetailsBottomBinding;
import com.moverbol.databinding.ItemCarryForwardDetailsHeaderBinding;
import com.moverbol.model.CarryForwardModel;
import com.moverbol.util.Util;

import java.util.ArrayList;

public class CarryForwardDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEMS = 1;
    private final int TYPE_FOOTER = 2;

    ArrayList<CarryForwardModel> data = new ArrayList<>(0);
    private Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carry_forward_details_header, parent, false);
            return new ViewHolderHeader(v);
        } else if (viewType == TYPE_ITEMS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carry_forward_details, parent, false);
            return new ViewHolderItem(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carry_forward_details_bottom, parent, false);
            return new ViewHolderFooter(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItem) {
            ItemCarryForwardDetailsBinding binding = ((ViewHolderItem) holder).binding;
            binding.setModel(data.get(position - 1));
            binding.executePendingBindings();

        } else if (holder instanceof ViewHolderHeader) {
            ItemCarryForwardDetailsHeaderBinding binding = ((ViewHolderHeader) holder).binding;

        } else if (holder instanceof ViewHolderFooter) {
            ItemCarryForwardDetailsBottomBinding binding = ((ViewHolderFooter) holder).binding;
            binding.txtTotalAmount.setText(showTotal());


        }

    }

    @Override
    public int getItemCount() {
        if (data.size() > 0) {
            return data.size() + 1;
        }
        return 0;
    }

    private String showTotal() {
        double total = 0;
        for (CarryForwardModel model : data) {
            total = total + model.getAmount();
        }
        return Util.getGeneralFormattedDecimalString(total);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == data.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEMS;
    }


    public void updateList(ArrayList<CarryForwardModel> homeList) {
        homeList.add(new CarryForwardModel());
        this.data = homeList;
        notifyDataSetChanged();
    }


    private static class ViewHolderItem extends RecyclerView.ViewHolder {
        ItemCarryForwardDetailsBinding binding;

        ViewHolderItem(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    private static class ViewHolderHeader extends RecyclerView.ViewHolder {
        ItemCarryForwardDetailsHeaderBinding binding;

        ViewHolderHeader(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }
    }

    private static class ViewHolderFooter extends RecyclerView.ViewHolder {
        ItemCarryForwardDetailsBottomBinding binding;

        ViewHolderFooter(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
