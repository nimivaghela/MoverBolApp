package com.moverbol.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.moverbol.R;
import com.moverbol.databinding.LayoutPhotoRoomItemBinding;
import com.moverbol.model.photoes.ImagesModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11-10-2017.
 */


public class PhotoRoomAdapter extends RecyclerView.Adapter<PhotoRoomAdapter.ViewHolder> {
    private List<ImagesModel> imagesModelList;
    private PhotoesEventListener photoesEventListener;


    public PhotoRoomAdapter(PhotoesEventListener photoesEventListener) {
        this.imagesModelList = new ArrayList<>();
        this.photoesEventListener = photoesEventListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public PhotoRoomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_photo_room_item, viewGroup, false);
        return new PhotoRoomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoRoomAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.photoItemBinding.setObj(imagesModelList.get(i));
    }

    @Override
    public int getItemCount() {
        return imagesModelList == null ? 0 : imagesModelList.size();
    }

    public void setPhotoModelList(List<ImagesModel> photoUrlList) {
        this.imagesModelList = photoUrlList;
        notifyDataSetChanged();
    }

    public interface PhotoesEventListener {
        void onPhotoClicked(ImagesModel photoModel);

        void onPhotoLongClicked(ImagesModel photoModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutPhotoRoomItemBinding photoItemBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            photoItemBinding = DataBindingUtil.bind(itemView);

            if (photoItemBinding != null) {
                photoItemBinding.imgAndroid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (photoesEventListener != null) {
                            photoesEventListener.onPhotoClicked(imagesModelList.get(getAdapterPosition()));
                        }
                    }
                });

                photoItemBinding.imgAndroid.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (photoesEventListener != null) {
                            photoesEventListener.onPhotoLongClicked(imagesModelList.get(getAdapterPosition()));
                        }
                        return true;
                    }
                });
            }


        }

    }
}