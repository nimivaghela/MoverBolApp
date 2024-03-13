package com.moverbol.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.databinding.LayoutPhotoActivityItemBinding;
import com.moverbol.model.photoes.ImagesModel;
import com.moverbol.views.activities.moveprocess.photos.PhotoDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11-10-2017.
 */


public class ImagesItemAdapter extends RecyclerView.Adapter<ImagesItemAdapter.ViewHolder> {
    private List<ImagesModel> imagesModelList;
    private Context context;


    public ImagesItemAdapter() {
        this.imagesModelList = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ImagesItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_photo_activity_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImagesItemAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.photoItemBinding.txtName.setText(imagesModelList.get(i).getItem());
        ImageAdapter imageAdapter = new ImageAdapter(new ImageAdapter.PhotoesEventListener() {
            @Override
            public void onPhotoClicked(ImagesModel imagesModel) {
                if (imagesModel.isShowSelection()) {
                    imagesModel.setSelectedForDelete(!imagesModel.isSelectedForDelete());
                } else {
                    Intent intent = new Intent(context, PhotoDetailActivity.class);
                    intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_ID, imagesModel.getcId());
                    intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_URL, imagesModel.getLink());
                    intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_TITLE, imagesModel.getPhotoTitle());
                    intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_DESCRIPTION, imagesModel.getPhotoDescription());
                    context.startActivity(intent);
                }
            }

            @Override
            public void onPhotoLongClicked(ImagesModel photoModel) {
            }
        });
        viewHolder.photoItemBinding.rvPhotos.setAdapter(imageAdapter);
        imageAdapter.setImagesModelList(imagesModelList.get(i).getImages());
        viewHolder.photoItemBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return imagesModelList == null ? 0 : imagesModelList.size();
    }

    public void setPhotoModelList(List<ImagesModel> photoUrlList) {
        this.imagesModelList = photoUrlList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LayoutPhotoActivityItemBinding photoItemBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            photoItemBinding = DataBindingUtil.bind(itemView);
        }

    }
}