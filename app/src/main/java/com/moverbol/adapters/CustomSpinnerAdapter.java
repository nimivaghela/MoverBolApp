package com.moverbol.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.moverbol.R;
import com.moverbol.databinding.Spinnerbinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AkashM on 6/12/17.
 */


public class CustomSpinnerAdapter<T> extends BaseAdapter {

    private List<T> dataList = new ArrayList<>();
    private Context context;
    private int resourceLayoutId;

    public CustomSpinnerAdapter(Context context, int resourceLayoutId, @NonNull List<T> dataList) {
        this.dataList = dataList;
        this.context = context;
        this.resourceLayoutId = resourceLayoutId;
    }

    public CustomSpinnerAdapter(Context context, int resourceLayoutId, @NonNull T[] dataArray) {
        for (int i = 0; i < dataArray.length; i++) {
            dataList.add(dataArray[i]);
        }
        this.context = context;
        this.resourceLayoutId = resourceLayoutId;
    }



    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resourceLayoutId, parent, false);

        ViewHolder holder = new ViewHolder(view);
        try {
            holder.bind(dataList.get(position).toString());
        }catch (NullPointerException e){
            holder.bind("null value in response");
        }

        if(position == 0 && !dataList.get(position).toString().equals(context.getString(R.string.temporary))){
            holder.binding.txtSpinnerText.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
        }

        return view;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setDataList(T[] dataArray) {
        dataList.clear();
        for (int i = 0; i < dataArray.length; i++) {
            dataList.add(dataArray[i]);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder{

        private Spinnerbinding binding;

        public ViewHolder(View itemView) {
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(String s){
            binding.setString(s);
            binding.executePendingBindings();
        }
    }
}
