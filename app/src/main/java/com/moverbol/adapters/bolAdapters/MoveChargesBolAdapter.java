package com.moverbol.adapters.bolAdapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.MoveChargesBolItemBinding;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkashM on 12/4/18.
 */
public class MoveChargesBolAdapter extends RecyclerView.Adapter<MoveChargesBolAdapter.HomeItemHolder> {

    private List<CommonChargesRequestModel> chargesList;

    private OnEditClickedListener onEditClickedListener;

    public interface OnEditClickedListener{
        void onEditClicked(CommonChargesRequestModel commonChargesRequestModel, int adapterPosition);
    }

    public MoveChargesBolAdapter() {
        this.chargesList = new ArrayList<>();
    }

    @Override
    public MoveChargesBolAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_move_charge_bol_item, parent, false);
        return new MoveChargesBolAdapter.HomeItemHolder(view);
    }

    @Override
    public void onBindViewHolder(MoveChargesBolAdapter.HomeItemHolder holder, int position) {
        holder.binding.setPosition(position);
        holder.binding.setChargesPojo(chargesList.get(position));
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


    public void showEditOption(int positionToSkip){
        if(chargesList!=null){
            for (int i = 0; i < chargesList.size(); i++) {
                if(i!=positionToSkip)
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
        MoveChargesBolItemBinding binding;
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


