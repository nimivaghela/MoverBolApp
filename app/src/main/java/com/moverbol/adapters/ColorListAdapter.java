package com.moverbol.adapters;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moverbol.R;
import com.moverbol.databinding.ColorItemBinding;
import com.moverbol.model.ColorPojo;
import com.moverbol.util.Util;

/**
 * Created by Admin on 27-09-2017.
 */

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ColorHolder> {

    private String[][] colorArray;
    private RecyclerView recyclerViewColor;
    private int layoutId;
    private Context context;
    private ObservableArrayList<ColorPojo> colorList;
    private ColorPojo selectedColr;
    private String selectedDefaultColor;

    public ColorListAdapter(RecyclerView recyclerViewColor, ObservableArrayList<ColorPojo> colorList, int layoutId, String selectedColor) {
        //this.colorArray = colorArray;
        //colorList = OrganisemeeUtils.twoDArrayToList(colorArray);
        this.recyclerViewColor = recyclerViewColor;
        this.colorList = colorList;
        this.layoutId = layoutId;
        this.selectedDefaultColor = selectedColor;
    }

    @Override
    public ColorListAdapter.ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        context = parent.getContext();
        return new ColorHolder(v);
    }

    @Override
    public void onBindViewHolder(ColorListAdapter.ColorHolder holder, int position) {
        final ColorPojo colorPojo = colorList.get(position);
        holder.colorItemBinding.setColor(colorPojo);
        holder.colorItemBinding.setColorListAdapter(this);
        if (colorPojo.color != null) {
            String hex = Util.rgbToHex(colorPojo.color);
            if (hex.equals(selectedDefaultColor)) {
                holder.colorItemBinding.linViewColor.setBackgroundColor(Util.getColor(context, R.color.imgBlue));
            } else {
                holder.colorItemBinding.linViewColor.setBackgroundColor(0);
            }
            holder.colorItemBinding.viewColor.setBackgroundColor(Color.parseColor(hex));
        }
        holder.colorItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }


    public void selectedColors(View view) {
        //int position = recyclerViewColor.getChildLayoutPosition(view);
        int position = recyclerViewColor.getChildLayoutPosition((View) view.getParent());
        selectedColr = colorList.get(position);
        selectedDefaultColor = Util.rgbToHex(selectedColr.color);
        notifyDataSetChanged();

    }

    public ColorPojo getSelectedColor() {
        selectedColr.color = Util.rgbToHex(selectedColr.color);
        return selectedColr;
    }

  /*  public String rgbToHex(String rgbColor) {
        String[] ARGB = rgbColor.split(",");
        int r = Integer.parseInt(ARGB[0]);
        int g = Integer.parseInt(ARGB[1]);
        int b = Integer.parseInt(ARGB[2]);
        return String.format("#%02X%02X%02X", r, g, b);
    }*/

    public class ColorHolder extends RecyclerView.ViewHolder {
        public ColorItemBinding colorItemBinding;

        public ColorHolder(View itemView) {
            super(itemView);

            colorItemBinding = DataBindingUtil.bind(itemView);
        }
    }
}