package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.CrewItemBinding;
import com.moverbol.model.ManPowerPojo;

import java.util.List;

/**
 * Created by sumeet on 11/9/17.
 */

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.HomeItemHolder> {

    private List<ManPowerPojo> homeList;
    private String type = "";
    private View.OnClickListener onClickListener;

    public CrewAdapter(List<ManPowerPojo> homeList, View.OnClickListener onClickListener) {
        this.homeList = homeList;
        this.onClickListener = onClickListener;
    }

    @Override
    public CrewAdapter.HomeItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_crew_item, parent, false);
        return new CrewAdapter.HomeItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final CrewAdapter.HomeItemHolder holder, final int position) {
        holder.bind(homeList.get(position), position, type);
    }

    @Override
    public int getItemCount() {
        if (homeList == null) {
            return 0;
        } else {
            return homeList.size();
        }
    }

    public void setHomeList(List<ManPowerPojo> homeList) {
        this.homeList = homeList;
        notifyDataSetChanged();
    }

    public List<ManPowerPojo> getHomeList() {
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


    public String getType() {
        return this.type;
    }

    public class HomeItemHolder extends RecyclerView.ViewHolder {
        public CrewItemBinding crewItemBinding;

        public HomeItemHolder(View itemView) {
            super(itemView);
            crewItemBinding = DataBindingUtil.bind(itemView);


            crewItemBinding.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    int adapterPosition = (int) v.getTag();
//                    adapterPosition = adapterPosition - 1;
                    CrewAdapter.this.onClickListener.onClick(v);
//                    v.getContext().startActivity(new Intent(v.getContext(), AddCrewActivity.class).putExtra(Constants.KEY_ADAPTER_POSITION, adapterPosition + ""));
                }
            });

            crewItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if(crewItemBinding.getCrewPojo().isSelectedForDelete()){
                        crewItemBinding.getCrewPojo().setSelectedForDelete(false);
                    }else {
                        crewItemBinding.getCrewPojo().setSelectedForDelete(true);
                    }*/
                    crewItemBinding.checkBox.toggle();
                }
            });
        }

        public void bind(ManPowerPojo crewPojo, int position, String type){
            crewItemBinding.setCrewPojo(crewPojo);
            crewItemBinding.setPosition(position);
            crewItemBinding.setType(type);
            crewItemBinding.executePendingBindings();
        }

    }

}
