package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.MaterialItemBinding;
import com.moverbol.model.MaterialPojo;
import com.moverbol.util.MoversPreferences;

import java.util.List;

/**
 * Created by Admin on 21-09-2017.
 */

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.HomeItemHolder> {

    private List<MaterialPojo> homeList;
    private String type;
    private View.OnClickListener onClickListener;

    public MaterialAdapter(List<MaterialPojo> homeList, View.OnClickListener onClickListener) {
        this.homeList = homeList;
        this.onClickListener = onClickListener;
    }

    @Override
    public MaterialAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_material_item, parent, false);
        return new MaterialAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(MaterialAdapter.HomeItemHolder holder, int position) {
        holder.materialItemBinding.setMaterialPojo(homeList.get(position));
        holder.materialItemBinding.setAdapterPosition(position);
        holder.materialItemBinding.setType(type);
        holder.materialItemBinding.setCurrencySymbol(MoversPreferences.getInstance(holder.materialItemBinding.getRoot().getContext()).getCurrencySymbol());
        holder.materialItemBinding.executePendingBindings();
    }

    public void setHomeList(List<MaterialPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public List<MaterialPojo> getHomeList() {
        return homeList;
    }

    public void setType(String type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void unSelectAll(){

        if(homeList==null || homeList.isEmpty()){
            return;
        }

        for (int i = 0; i < homeList.size(); i++) {
            homeList.get(i).setSelectedForDelete(false);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (homeList == null) {
            return 0;
        } else {
            return homeList.size();
        }
    }


    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public MaterialItemBinding materialItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            materialItemBinding = DataBindingUtil.bind(itemView);

            materialItemBinding.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*int adapterPosition = (int) v.getTag();
                    adapterPosition = adapterPosition - 1;
                    v.getContext().startActivity(new Intent(v.getContext(), AddMaterialActivity.class).putExtra(Constants.KEY_ADAPTER_POSITION, adapterPosition + ""));*/

                    MaterialAdapter.this.onClickListener.onClick(v);
                }
            });


            materialItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (materialItemBinding.checkBox.getVisibility() == View.VISIBLE)
                        materialItemBinding.checkBox.toggle();
                }
            });

        }
    }

}
