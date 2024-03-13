package com.moverbol.views.activities.moveprocess.photos;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.moverbol.R;
import com.moverbol.adapters.PhotoRoomAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityPhotosRoomBinding;
import com.moverbol.model.photoes.ImagesModel;
import com.moverbol.model.photoes.PhotoModel;

public class PhotosRoomActivity extends BaseAppCompactActivity {

    private ActivityPhotosRoomBinding binding;
    private PhotoRoomAdapter adapter;
    private PhotoModel photoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photos_room);
        setToolbar(binding.toolbar.toolbar, getString(R.string.rooms), R.drawable.ic_arrow_back_white_24dp);
        photoModel = getIntent().getParcelableExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_MODEL);
        setRecyclerView();
    }

    private void setRecyclerView() {
        adapter = new PhotoRoomAdapter(new PhotoRoomAdapter.PhotoesEventListener() {
            @Override
            public void onPhotoClicked(ImagesModel imagesModel) {
                Intent intent = new Intent(PhotosRoomActivity.this, PhotoItemActivity.class);
                intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_IMAGE_MODEL, imagesModel);
                startActivity(intent);
            }

            @Override
            public void onPhotoLongClicked(ImagesModel photoModel) {

            }

        });

        binding.rvPhotos.setAdapter(adapter);
        if (photoModel != null) {
            adapter.setPhotoModelList(photoModel.getImages());
        }

    }
}
