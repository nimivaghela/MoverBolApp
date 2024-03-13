package com.moverbol.views.activities.moveprocess.photos;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.moverbol.R;
import com.moverbol.adapters.ImagesItemAdapter;
import com.moverbol.adapters.PhotoItemAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityPhotoItemBinding;
import com.moverbol.model.photoes.ImagesModel;
import com.moverbol.model.photoes.PhotoModel;

public class PhotoItemActivity extends BaseAppCompactActivity {

    private ActivityPhotoItemBinding binding;
    private ImagesModel imagesModel;
    private PhotoModel photoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_MODEL)) {
            photoModel = getIntent().getParcelableExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_MODEL);
        }

        if (getIntent().hasExtra(Constants.PhotoDetailsIntentExtras.EXTRA_IMAGE_MODEL)) {
            imagesModel = getIntent().getParcelableExtra(Constants.PhotoDetailsIntentExtras.EXTRA_IMAGE_MODEL);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_item);
        setToolbar(binding.toolbar.toolbar, getString(R.string.photos), R.drawable.ic_arrow_back_white_24dp);
        setRecyclerView();
    }

    private void setRecyclerView() {
        if (imagesModel != null) {
            PhotoItemAdapter photoItemAdapter = new PhotoItemAdapter();
            binding.rvPhotos.setAdapter(photoItemAdapter);
            photoItemAdapter.setPhotoModelList(imagesModel.getItems());
        } else {
            ImagesItemAdapter imagesItemAdapter = new ImagesItemAdapter();
            binding.rvPhotos.setAdapter(imagesItemAdapter);
            imagesItemAdapter.setPhotoModelList(photoModel.getImages());
        }

    }
}
