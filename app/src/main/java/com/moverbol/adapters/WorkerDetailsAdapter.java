package com.moverbol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.ItemWorkerDetailsBillableFooterBinding;
import com.moverbol.databinding.ItemWorkerDetailsDataBinding;
import com.moverbol.databinding.ItemWorkerDetailsFooterBinding;
import com.moverbol.databinding.ItemWorkerDetailsHeaderBinding;
import com.moverbol.model.moveProcess.ActivityItem;
import com.moverbol.model.moveProcess.WorkerItemType;

import java.util.ArrayList;
import java.util.List;


public class WorkerDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEMS = 1;
    private final int TYPE_FOOTER = 2;
    private final int TYPE_BILLABLE_FOOTER = 3;

    private List<ActivityItem> homeList = new ArrayList<>(0);
    private Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worker_details_header, parent, false);
            return new ViewHolderHeader(v);
        } else if (viewType == TYPE_ITEMS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worker_details_data, parent, false);
            return new ViewHolderItem(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worker_details_footer, parent, false);
            return new ViewHolderFooter(v);
        } else if (viewType == TYPE_BILLABLE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_worker_details_billable_footer, parent, false);
            return new ViewHolderBillableFooter(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItem) {
            ViewHolderItem viewHolderItem = ((ViewHolderItem) holder);
            viewHolderItem.binding.setActivityItem(homeList.get(position - 1));
            viewHolderItem.binding.setIsWorkerName(homeList.get(position - 1).getItemType() == WorkerItemType.WorkerName);
            viewHolderItem.binding.executePendingBindings();
        } else if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader viewHolderHeader = ((ViewHolderHeader) holder);
            viewHolderHeader.bindingHeader.executePendingBindings();
        } else if (holder instanceof ViewHolderFooter) {
            ViewHolderFooter viewHolderFooter = (ViewHolderFooter) holder;
            viewHolderFooter.bindingFooter.setActivityItem(homeList.get(position - 1));
            viewHolderFooter.bindingFooter.executePendingBindings();
        } else if (holder instanceof ViewHolderBillableFooter) {
            ViewHolderBillableFooter viewHolderBillableFooter = (ViewHolderBillableFooter) holder;
            viewHolderBillableFooter.bindingFooter.setActivityItem(homeList.get(position - 1));
            viewHolderBillableFooter.bindingFooter.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        if (homeList.size() > 0) {
            return homeList.size() + 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (homeList.get(position - 1).getItemType() == WorkerItemType.TotalHours) {
            return TYPE_FOOTER;
        } else if (homeList.get(position - 1).getItemType() == WorkerItemType.TotalBillableHours) {
            return TYPE_BILLABLE_FOOTER;
        } else {
            return TYPE_ITEMS;
        }

    }

    public List<ActivityItem> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<ActivityItem> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public ActivityItem getItem(int position) {
        return homeList.get(position);
    }


    private class ViewHolderItem extends RecyclerView.ViewHolder {

        private final ItemWorkerDetailsDataBinding binding;

        ViewHolderItem(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        private final ItemWorkerDetailsHeaderBinding bindingHeader;

        ViewHolderHeader(View itemView) {
            super(itemView);
            bindingHeader = DataBindingUtil.bind(itemView);
        }
    }

    class ViewHolderFooter extends RecyclerView.ViewHolder {
        private final ItemWorkerDetailsFooterBinding bindingFooter;

        ViewHolderFooter(@NonNull View itemView) {
            super(itemView);
            bindingFooter = DataBindingUtil.bind(itemView);
        }
    }

    class ViewHolderBillableFooter extends RecyclerView.ViewHolder {
        private final ItemWorkerDetailsBillableFooterBinding bindingFooter;

        ViewHolderBillableFooter(@NonNull View itemView) {
            super(itemView);
            bindingFooter = DataBindingUtil.bind(itemView);
        }
    }

}
