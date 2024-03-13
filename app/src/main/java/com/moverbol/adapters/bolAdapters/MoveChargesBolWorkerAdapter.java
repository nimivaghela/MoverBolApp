package com.moverbol.adapters.bolAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.ItemMoveChargeBolWorkerBinding;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkashM on 12/4/18.
 */
public class MoveChargesBolWorkerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEMS = 1;

    private List<CommonChargesRequestModel> chargesList;

    private OnEditClickedListener onEditClickedListener;

    public MoveChargesBolWorkerAdapter() {
        this.chargesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_move_charge_bol_worker_header, parent, false);
            return new MoveChargesBolWorkerAdapter.HeaderItemHolder(view);
        } else if (viewType == TYPE_ITEMS) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_move_charge_bol_worker, parent, false);
            return new MoveChargesBolWorkerAdapter.HomeItemHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeItemHolder) {
            ItemMoveChargeBolWorkerBinding binding = ((HomeItemHolder) holder).binding;
            binding.setChargesPojo(chargesList.get(position - 1));
            binding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        if (chargesList.size() > 0) {
            return chargesList.size() + 1;
        }
        return 0;
    }

    public void setChargesList(List<CommonChargesRequestModel> chargesList) {
        this.chargesList = chargesList;
        notifyDataSetChanged();
    }

    public void addToHomeList(CommonChargesRequestModel moveChargesAutoPricingPojo, int adapterPosition) {
        if (adapterPosition > this.chargesList.size() || adapterPosition < 0) {
            this.chargesList.add(moveChargesAutoPricingPojo);
            this.notifyItemInserted(chargesList.size() - 1);
        } else {
            this.chargesList.remove(adapterPosition);
            this.chargesList.add(adapterPosition, moveChargesAutoPricingPojo);
            this.notifyItemChanged(adapterPosition);
        }

    }

    public void showEditOption() {
        if (chargesList != null) {
            for (int i = 0; i < chargesList.size(); i++) {
                chargesList.get(i).setShowEditOption(true);
            }
        }
        notifyDataSetChanged();
    }

    public void showEditOption(int positionToSkip) {
        if (chargesList != null) {
            for (int i = 0; i < chargesList.size(); i++) {
                if (i != positionToSkip)
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

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEMS;
        }
    }

    public interface OnEditClickedListener {
        void onEditClicked(CommonChargesRequestModel commonChargesRequestModel, int adapterPosition);
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        ItemMoveChargeBolWorkerBinding binding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public class HeaderItemHolder extends RecyclerView.ViewHolder {

        HeaderItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}


