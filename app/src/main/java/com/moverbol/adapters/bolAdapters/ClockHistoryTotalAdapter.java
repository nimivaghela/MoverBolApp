package com.moverbol.adapters.bolAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.ItemClockHistoryTotalBinding;
import com.moverbol.interfaces.OnClickChildAdpter;
import com.moverbol.model.ClockActivityModel;

import java.util.ArrayList;
import java.util.List;

public class ClockHistoryTotalAdapter extends RecyclerView.Adapter<ClockHistoryTotalAdapter.ViewHolder> {

    private List<ClockActivityModel> homeList;
    private OnClickChildAdpter onClickChildAdpter;
    private Context context;

    public ClockHistoryTotalAdapter() {
        this.homeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clock_history_total, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setObj(homeList.get(position));
        //holder.binding.imgEdit.setOnClickListener(v -> onClickChildAdpter.onclickAdapter(v, holder.getAdapterPosition()));
        // holder.binding.setIsLast(holder.getAdapterPosition() == homeList.size() - 1);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    public void setOnClickChildAdapter(OnClickChildAdpter onClickChildAdpter) {
        this.onClickChildAdpter = onClickChildAdpter;
    }

    public List<ClockActivityModel> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<ClockActivityModel> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public ClockActivityModel getItem(int position) {
        return homeList.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemClockHistoryTotalBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
