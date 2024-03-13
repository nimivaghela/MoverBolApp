package com.moverbol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.LayoutImagesItemBinding;
import com.moverbol.model.photoes.ImagesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11-10-2017.
 */


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private static final int DELETE_PHOTO_REQUEST_CODE = 101;
    private List<ImagesModel> photoModelList;
    private PhotoesEventListener photoesEventListener;

    public ImageAdapter(PhotoesEventListener photoesEventListener) {
        this.photoModelList = new ArrayList<>();
        this.photoesEventListener = photoesEventListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_images_item, viewGroup, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.imageItemBinding.setObj(photoModelList.get(i));
    }

    @Override
    public int getItemCount() {
        return photoModelList == null ? 0 : photoModelList.size();
    }

    public void setImagesModelList(List<ImagesModel> photoUrlList) {
        this.photoModelList = photoUrlList;
        notifyDataSetChanged();
    }

    public void addUrl(ImagesModel photoModel) {
        this.photoModelList.add(photoModel);
        notifyDataSetChanged();
    }

    public void showSelectionForAllPhotoes() {
        for (ImagesModel imagesModel : photoModelList) {
            imagesModel.setShowSelection(true);
        }
        notifyDataSetChanged();
    }

    public void hideSelectionForAllPhotoes() {
        for (ImagesModel imagesModel : photoModelList) {
            imagesModel.setShowSelection(false);
            imagesModel.setSelectedForDelete(false);
        }
        notifyDataSetChanged();
    }

    public interface PhotoesEventListener {
        void onPhotoClicked(ImagesModel photoModel);

        void onPhotoLongClicked(ImagesModel photoModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutImagesItemBinding imageItemBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            imageItemBinding = DataBindingUtil.bind(itemView);

            if (imageItemBinding != null) {
                imageItemBinding.imgAndroid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (photoesEventListener != null) {
                            photoesEventListener.onPhotoClicked(photoModelList.get(getAdapterPosition()));
                        }
                    }
                });

                imageItemBinding.imgAndroid.setOnLongClickListener(new View.OnLongClickListener() {
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