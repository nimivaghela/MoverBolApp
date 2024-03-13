package com.moverbol.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.AutoPricingItemBinding;
import com.moverbol.model.confirmationPage.MoveChargesAutoPricingPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkashM on 27/1/18.
 */

public class MoveChargesAutoPricingAdapter extends RecyclerView.Adapter<MoveChargesAutoPricingAdapter.HomeItemHolder> {

    private List<MoveChargesAutoPricingPojo> chargesList;
    private boolean editValues;

    private OnEditClickedListener onEditClickedListener;

    public interface OnEditClickedListener{
        void onEditClicked(MoveChargesAutoPricingPojo moveChargesAutoPricingPojo, int adapterPosition);
    }


    public MoveChargesAutoPricingAdapter(boolean editValues) {
        this.chargesList = new ArrayList<>();
        this.editValues = editValues;
    }

    @Override
    public HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_move_charges_auto_pricing_item, parent, false);
        return new HomeItemHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeItemHolder holder, int position) {
        holder.binding.setPosition(position);
        holder.binding.setChargesPojo(chargesList.get(position));
        holder.binding.setEditValues(editValues);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return chargesList == null ? 0 : chargesList.size();
    }

    public void setChargesList(List<MoveChargesAutoPricingPojo> chargesList) {
        this.chargesList = chargesList;
        notifyDataSetChanged();
    }

    public void addToHomeList(MoveChargesAutoPricingPojo moveChargesAutoPricingPojo) {
        this.chargesList.add(moveChargesAutoPricingPojo);
        this.notifyItemInserted(chargesList.size() - 1);
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
        AutoPricingItemBinding binding;
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
