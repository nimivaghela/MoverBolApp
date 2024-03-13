package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.ReleaseFormItemBinding;
import com.moverbol.model.releaseForm.ReleaseFormMetadataPojo;

import java.util.ArrayList;
import java.util.List;

public class ReleaseFormListAdapter extends RecyclerView.Adapter<ReleaseFormListAdapter.ViewHolder>{

    private List<ReleaseFormMetadataPojo> homeList;
    private ReleaseFormTitleClickListener listener;

    public ReleaseFormListAdapter() {
        this.homeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_release_form_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setObj(homeList.get(position));
    }

    @Override
    public int getItemCount() {
        if(homeList==null){
            return 0;
        }
        return homeList.size();
    }

    public List<ReleaseFormMetadataPojo> getHomeList() {
        return homeList;
    }

    public void setHomeList(List<ReleaseFormMetadataPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ReleaseFormItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onItemClicked(homeList.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }
    }


    public void setListner(ReleaseFormTitleClickListener listener) {
        this.listener = listener;
    }

    public interface ReleaseFormTitleClickListener{
        void onItemClicked(ReleaseFormMetadataPojo releaseFormMetadataPojo, int position);
    }
}
