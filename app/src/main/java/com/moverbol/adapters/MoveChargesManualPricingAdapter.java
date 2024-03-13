package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.MoveChargesItemBinding;
import com.moverbol.model.confirmationPage.MoveChargesManualPricingPojo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 27-09-2017.
 */

public class MoveChargesManualPricingAdapter extends RecyclerView.Adapter<MoveChargesManualPricingAdapter.HomeItemHolder> {

    private List<MoveChargesManualPricingPojo> chargesList;
    private boolean editValues;

    private OnEditClickedListener onEditClickedListener;

    public MoveChargesManualPricingAdapter(boolean editValues) {
        this.chargesList = new ArrayList<>();
        this.editValues = editValues;
    }

    @Override
    public HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_move_charges_manual_pricing_item, parent, false);
        return new MoveChargesManualPricingAdapter.HomeItemHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeItemHolder holder, int position) {
        holder.moveChargesItemBinding.setPosition(position);
        holder.moveChargesItemBinding.setEditValues(editValues);
        holder.moveChargesItemBinding.setChargesPojo(chargesList.get(position));
        holder.moveChargesItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return chargesList == null ? 0 : chargesList.size();
    }

    public void setChargesList(List<MoveChargesManualPricingPojo> chargesList) {
        this.chargesList = chargesList;
        notifyDataSetChanged();
    }

    public void addToHomeList(MoveChargesManualPricingPojo manualPricingPojo) {
        chargesList.add(manualPricingPojo);
        this.notifyItemInserted(chargesList.size() - 1);
    }

    public void showEditOption() {
        if (chargesList != null) {
            for (int i = 0; i < chargesList.size(); i++) {
                chargesList.get(i).setShowEditOption(true);
            }
        }
        notifyDataSetChanged();
    }

    public void hideEditOption() {
        if (chargesList != null) {
            for (int i = 0; i < chargesList.size(); i++) {
                chargesList.get(i).setShowEditOption(false);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnEditClickedListener(OnEditClickedListener onEditClickedListener) {
        this.onEditClickedListener = onEditClickedListener;
    }

    public interface OnEditClickedListener {
        void onEditClicked(MoveChargesManualPricingPojo moveChargesManualPricingPojo, int adapterPosition);
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        MoveChargesItemBinding moveChargesItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            moveChargesItemBinding = DataBindingUtil.bind(itemView);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEditClickedListener != null && moveChargesItemBinding.frameLayout.getVisibility() == View.VISIBLE) {
                        onEditClickedListener.onEditClicked(chargesList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });*/
        }

    }
}
