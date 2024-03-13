package com.moverbol.adapters;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.moverbol.R;
import com.moverbol.databinding.SelectAddressItemBinding;
import com.moverbol.model.releaseForm.AddressSelectionListItemModel;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectionListAdapter extends RecyclerView.Adapter<AddressSelectionListAdapter.ViewHolder> {

    private List<AddressSelectionListItemModel> mHomeList;
    private SelectAddressClickListener listener;

    public AddressSelectionListAdapter(){
        mHomeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.layout_select_addresses_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*boolean isLastItem = (position == (getItemCount() - 1));
        holder.binding.setIsLastItem(isLastItem);*/
        holder.binding.setObj(mHomeList.get(position));

        if(mHomeList.get(position).isShouldShowAddressEmptyError()){
            holder.binding.edtxtAdddress.setError(holder.itemView.getContext().getText(R.string.select_address_error));
        } else {
            holder.binding.edtxtAdddress.setError(null);
        }

    }

    @Override
    public int getItemCount() {

        if (mHomeList == null) {
            return 0;
        }

        return mHomeList.size();
    }

    public List<AddressSelectionListItemModel> getmHomeList() {
        return mHomeList;
    }

    public void setmHomeList(List<AddressSelectionListItemModel> mHomeList) {
        this.mHomeList = mHomeList;
        notifyDataSetChanged();
    }

    public void setListener(SelectAddressClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SelectAddressItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            /*binding.imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHomeList.add(new AddressSelectionListItemModel("", false, true, false));
                    notifyItemInserted(mHomeList.size() - 1);

                    *//*Because we want last item only to have plus symbol and till now
                    this(mHomeList.size() - 2) item was the last item.So we want the recycler
                    view to change this item. Easier way could have been to simply call the
                    notifyDataSetChanged() but I think this would be faster in execution.*//*
                    notifyItemChanged(mHomeList.size() - 2);
                }
            });

            binding.imgMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHomeList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });*/

            View.OnClickListener selectAddressClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onSelectAddressClicked(binding.edtxtAdddress, getAdapterPosition());
                    }
                }
            };

            binding.imgLocation.setOnClickListener(selectAddressClickListener);
            binding.edtxtAdddress.setOnClickListener(selectAddressClickListener);

        }
    }


    public interface SelectAddressClickListener{
        void onSelectAddressClicked(EditText editText, int adapterPosition);
    }

}
