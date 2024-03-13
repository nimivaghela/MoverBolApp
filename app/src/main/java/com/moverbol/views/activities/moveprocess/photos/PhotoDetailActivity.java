package com.moverbol.views.activities.moveprocess.photos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.PhotoDetailBinding;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.PhotosViewModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;

/**
 * Created by Admin on 12-10-2017.
 */

public class PhotoDetailActivity extends BaseAppCompactActivity {
    private PhotoDetailBinding binding;
    private PhotosViewModel viewModel;
    private String photoId;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_detail);
        binding.ivBack.setOnClickListener(view -> finish());

        binding.ivDelete.setOnClickListener(v -> callDeleteSelectedPhotos());

        viewModel = new ViewModelProvider(this).get(PhotosViewModel.class);

        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_URL)) {
                String url = intent.getStringExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_URL);
                Picasso.get().load(url).placeholder(R.drawable.placeholder_image).into(binding.selectedImage);
            }

            if (intent.hasExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_TITLE)) {
                String title = intent.getStringExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_TITLE);
                binding.txtTitle.setText(title);
            }
            if (intent.hasExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_DESCRIPTION)) {
                String description = intent.getStringExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_DESCRIPTION);
                binding.txtDescription.setText(description);
            }

            if (intent.hasExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_ID)) {
                photoId = intent.getStringExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_ID);
            }

            binding.setShowDeleteOption(photoId != null);
        }

    }

    private void callDeleteSelectedPhotos() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        viewModel.deletePhotoes(subDomain, userId, opportunityId, jobId, photoId, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PhotoDetailActivity.this);
                    return;
                }
                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

    }
}