package com.moverbol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.LayoutPhotosItemBinding;
import com.moverbol.model.photoes.PhotoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11-10-2017.
 */


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<PhotoModel> photoModelList;
    private static final int DELETE_PHOTO_REQUEST_CODE = 101;

    private PhotoesEventListener photoesEventListener;

    public PhotoAdapter(PhotoesEventListener photoesEventListener) {
        this.photoModelList = new ArrayList<>();
        this.photoesEventListener = photoesEventListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_photos_item, viewGroup, false);
        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.photoItemBinding.setObj(photoModelList.get(i));
    }

    @Override
    public int getItemCount() {
        return photoModelList == null ? 0 : photoModelList.size();
    }

    public void setPhotoModelList(List<PhotoModel> photoUrlList) {
        this.photoModelList = photoUrlList;
        notifyDataSetChanged();
    }

    public void addUrl(PhotoModel photoModel) {
        this.photoModelList.add(photoModel);
        notifyDataSetChanged();
    }

    public void showSelectionForAllPhotoes() {
        for (int i = 0; i < photoModelList.size(); i++) {
            photoModelList.get(i).setShowSelection(true);
        }
//        notifyDataSetChanged();
    }

    public void hideSelectionForAllPhotoes() {
        for (int i = 0; i < photoModelList.size(); i++) {
            photoModelList.get(i).setShowSelection(false);
            photoModelList.get(i).setSelectedForDelete(false);
        }
//        notifyDataSetChanged();
    }

    public interface PhotoesEventListener {
        void onPhotoClicked(PhotoModel photoModel);

        void onPhotoLongClicked(PhotoModel photoModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutPhotosItemBinding photoItemBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            photoItemBinding = DataBindingUtil.bind(itemView);

            if (photoItemBinding != null) {
                photoItemBinding.imgAndroid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (photoesEventListener != null) {
                            photoesEventListener.onPhotoClicked(photoModelList.get(getAdapterPosition()));
                        }
                    }
                });

                photoItemBinding.imgAndroid.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (photoesEventListener != null) {
                            photoesEventListener.onPhotoLongClicked(photoModelList.get(getAdapterPosition()));
                        }
                        return true;
                    }
                });
            }


        }

    }
}