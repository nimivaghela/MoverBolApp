package com.moverbol.adapters.bolAdapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.AdditionalChargesBOLItemBinding;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkashM on 13/4/18.
 */
public class AdditionalChargeBolAdapter extends RecyclerView.Adapter<AdditionalChargeBolAdapter.HomeItemHolder> {

    private List<CommonChargesRequestModel> chargesList;
    private OnEditClickedListener onEditClickedListener;


    public interface OnEditClickedListener{
        void onEditClicked(CommonChargesRequestModel commonChargesRequestModel, int adapterPosition);
    }

    public AdditionalChargeBolAdapter(){
        this.chargesList = new ArrayList<>();
    }

    @Override
    public AdditionalChargeBolAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_additional_charges_bol_item, parent, false);
        return new AdditionalChargeBolAdapter.HomeItemHolder(view);
    }


    @Override
    public void onBindViewHolder(AdditionalChargeBolAdapter.HomeItemHolder holder, int position) {
        holder.binding.setChargesPojo(chargesList.get(position));
        holder.binding.setPosition(position);
        holder.binding.executePendingBindings();
    }


    @Override
    public int getItemCount() {
        return chargesList == null ? 0 : chargesList.size();
    }

    public void setChargesList(List<CommonChargesRequestModel> chargesList) {
        this.chargesList = chargesList;
        notifyDataSetChanged();
    }

    public void addToHomeList(CommonChargesRequestModel moveChargesAutoPricingPojo, int adapterPosition) {
        if(adapterPosition > this.chargesList.size() || adapterPosition < 0){
            this.chargesList.add(moveChargesAutoPricingPojo);
            this.notifyItemInserted(chargesList.size() - 1);
        } else {
            this.chargesList.remove(adapterPosition);
            this.chargesList.add( adapterPosition, moveChargesAutoPricingPojo);
            this.notifyItemChanged(adapterPosition);
        }

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
        AdditionalChargesBOLItemBinding binding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onEditClickedListener!=null && binding.frameLayout.getVisibility()==View.VISIBLE){
                        onEditClickedListener.onEditClicked(chargesList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });

        }
    }
}
