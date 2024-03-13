package com.moverbol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.NotificationItemBinding;
import com.moverbol.model.notification.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.HomeItemHolder> {

    private List<NotificationModel> homeList;

    private NotificationItemClickedListener clickedListener;

    public NotificationListAdapter(@Nullable NotificationItemClickedListener clickedListener) {
        this.homeList = new ArrayList<>();
        this.clickedListener = clickedListener;
    }


    @NonNull
    @Override
    public HomeItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item, parent, false);
        return new HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemHolder holder, int position) {
        holder.binding.setObj(homeList.get(position));
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    public void setHomeList(List<NotificationModel> homeList) {
        if(homeList!=null)
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {

        private NotificationItemBinding binding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickedListener!=null){
                        clickedListener.onNotificationItemClicked(binding.getObj(), getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface NotificationItemClickedListener{
        void onNotificationItemClicked(NotificationModel notificationModel, int adapterPosition);
    }

    public void updateNotificationRead(int position) {
        homeList.get(position).setReadStatus("1");
        notifyItemChanged(position);
    }

}
