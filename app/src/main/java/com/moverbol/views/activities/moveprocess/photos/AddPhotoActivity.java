package com.moverbol.views.activities.moveprocess.photos;

import static com.moverbol.util.Util.compressImage;
import static com.moverbol.util.Util.createCacheFile;
import static com.moverbol.util.Util.showToast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.SelectCameraOrGalleryDialog;
import com.moverbol.databinding.AddPhotoBinding;
import com.moverbol.model.photoes.ImagesModel;
import com.moverbol.model.photoes.PhotoUploadPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.PermissionHelper;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.PhotosViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import retrofit2.Call;

/**
 * Created by Admin on 12-10-2017.
 */

public class AddPhotoActivity extends BaseAppCompactActivity implements SelectCameraOrGalleryDialog.OnSelectionListener {
    private static final String KEY_IMAGE_PREVIEW_FILE_PATH = "file_path";
    private static final String KEY_IMAGE_TITLE_TEXT = "key_image_title";
    private static final String KEY_IMAGE_DESCRIPTION_TEXT = "key_image_description";
    private AddPhotoBinding binding;
    private PhotosViewModel viewModel;
    private PermissionHelper permissionHelper;
    private boolean mNewImageAdded;
    private MenuItem cancelMenuItem;
    private Uri imageUri;

    Runnable updateImagePath = new Runnable() {
        @Override
        public void run() {
            try {
                Bitmap bitmap = ((BitmapDrawable) binding.imgAddImage.getDrawable()).getBitmap();
                imageUri = Util.bitmapToFile(getBaseContext(), bitmap, new File(Util.createImageUri(getBaseContext()).getPath()));
                File compressImage = compressImage(getBaseContext(), imageUri, createCacheFile(getBaseContext(), imageUri));
                imageUri = Uri.parse(compressImage.getPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    ActivityResultLauncher<Uri> takePhoto = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        if (result) {
            File compressImage = compressImage(this, imageUri, createCacheFile(this, imageUri));
            Picasso.get().load(compressImage).into(binding.imgAddImage);
            imageUri = Uri.parse(compressImage.getPath());
            mNewImageAdded = true;
            cancelMenuItem.setVisible(true);
        }
    });

    ActivityResultLauncher<String> pickImageFromGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    Picasso.get().load(imageUri).into(binding.imgAddImage);
                    mNewImageAdded = true;
                    cancelMenuItem.setVisible(true);
                    new Handler(Looper.getMainLooper()).postDelayed(updateImagePath, 1000);
                }

            });

    private final ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), new ActivityResultCallback<CropImageView.CropResult>() {
        @Override
        public void onActivityResult(CropImageView.CropResult result) {
            if (result.isSuccessful()) {
                imageUri = Uri.parse(result.getUriFilePath(getBaseContext(), true));
                binding.imgAddImage.setImageURI(imageUri);
                mNewImageAdded = true;
                cancelMenuItem.setVisible(true);
            } else {
                showToast(getBaseContext(), Objects.requireNonNull(result.getError()).getLocalizedMessage());
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_photo);
        setToolbar(binding.toolbar.toolbar, getString(R.string.add_photo), R.drawable.ic_arrow_back_white_24dp);

        viewModel = new ViewModelProvider(this).get(PhotosViewModel.class);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_DESCRIPTION_TEXT)) {
                String imageTopReviewFilePath = savedInstanceState.getString(KEY_IMAGE_PREVIEW_FILE_PATH);
                if (imageUri != null) {
                    imageUri = Uri.parse(imageTopReviewFilePath);
                }
            }
            if (savedInstanceState.containsKey(KEY_IMAGE_DESCRIPTION_TEXT) && !TextUtils.isEmpty(savedInstanceState.getString(KEY_IMAGE_DESCRIPTION_TEXT))) {
                binding.txtDescription.setText(savedInstanceState.getString(KEY_IMAGE_DESCRIPTION_TEXT));
            }
            if (savedInstanceState.containsKey(KEY_IMAGE_TITLE_TEXT) && !TextUtils.isEmpty(savedInstanceState.getString(KEY_IMAGE_TITLE_TEXT))) {
                binding.txtTitle.setText(savedInstanceState.getString(KEY_IMAGE_TITLE_TEXT));
            }
        }

        if (imageUri != null) {
//            Picasso.with(AddPhotoActivity.this).load(imageToPreviewFile).into(binding.imgAddImage);
            Picasso.get().load(imageUri).into(binding.imgAddImage);
        }

        binding.imgAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage(v);
            }
        });
    }

    public void addImage(View view) {
        if (!(view instanceof ImageView)) {
            return;
        }
        if (mNewImageAdded) {
            return;
        }
        cropImage.launch(new CropImageContractOptions(null, new CropImageOptions()));
    }


    private void showUploadImageDialogue() {

        final SelectCameraOrGalleryDialog dialog = new SelectCameraOrGalleryDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        MenuItem addPhotoMenuItem = menu.findItem(R.id.add_photo);
        MenuItem removePhotoMenuItem = menu.findItem(R.id.remove_photo);
        MenuItem selectPhotoMenuItem = menu.findItem(R.id.tick);
        cancelMenuItem = menu.findItem(R.id.cancel);

        addPhotoMenuItem.setVisible(false);
        selectPhotoMenuItem.setVisible(false);
        removePhotoMenuItem.setVisible(false);
        cancelMenuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                if (cancelMenuItem == null) {
                    cancelMenuItem = item;
                }
                binding.imgAddImage.setImageResource(R.drawable.add_photo);
                mNewImageAdded = false;
                cancelMenuItem.setVisible(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void callUploadPhotos() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();

        String description = binding.edtxtDescription.getText().toString();
        String title = binding.edtxtTitle.getText().toString();

        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        PhotoUploadPojo photoUploadPojo = new PhotoUploadPojo();
        // photoUploadPojo.setImageEncodedString(imageEncodedString);
        photoUploadPojo.setTitle(title);
        photoUploadPojo.setDescription(description);
        photoUploadPojo.setUserId(userId);


        //  File compressImage = compressImage(this, imageUri, createCacheFile(this, imageUri));


        try {
            viewModel.uploadPhoto(subDomain, userId, opportunityId, photoUploadPojo, jobId, Util.prepareFilePart("photo_image", new URI(imageUri.getPath())), new ResponseListener<BaseResponseModel<ImagesModel>>() {
                @Override
                public void onResponse(BaseResponseModel<ImagesModel> response, String usedUrlKey) {
                    hideProgress();
                    Intent intent = new Intent();
                    intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_MODEL, response.getData());
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                    hideProgress();
                    if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                        Util.showLoginErrorDialog(AddPhotoActivity.this);
                        return;
                    }
                    Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<BaseResponseModel<ImagesModel>> call, Throwable t, String message) {
                    hideProgress();
                    Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void uploadImageFromGallery() {
        pickImageFromGallery.launch("image/*");
      /*  mediaHelper.takePictureFromGallery(new MediaCallback() {
            @Override
            public void onResult(boolean status, File file, MediaHelper.Media mediaType) {
                try {
                    imageToPreviewFile = file;
//                    Picasso.with(AddPhotoActivity.this).load(imageToPreviewFile).into(binding.imgAddImage);
                    Picasso.get().load(imageToPreviewFile).into(binding.imgAddImage);
                    mNewImageAdded = true;
                    cancelMenuItem.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
    }

    private void takePictureFromCamera() {
        try {
            imageUri = Util.createImageUri(this);
            takePhoto.launch(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

      /*  mediaHelper.takePictureFromCamera(new MediaCallback() {
            @Override
            public void onResult(boolean status, File file, MediaHelper.Media mediaType) {
                Log.d("File", file.getName());
                try {
                    imageToPreviewFile = file;
//                    Picasso.with(AddPhotoActivity.this).load(imageToPreviewFile).into(binding.imgAddImage);
                    Picasso.get().load(imageToPreviewFile).into(binding.imgAddImage);
                    mNewImageAdded = true;
                    cancelMenuItem.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });*/
    }

    private void uploadImage() {
        try {
            callUploadPhotos();
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    public void submitImage(View view) {
        if (validate()) {
            uploadImage();
        }
    }

    private boolean validate() {
        if (binding.imgAddImage.getDrawable() == null) {
            Snackbar.make(binding.getRoot(), "Cannot upload this image", Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (binding.imgAddImage.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.add_photo).getConstantState()) {
            Snackbar.make(binding.getRoot(), "No image selected to submit", Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(binding.edtxtTitle.getText().toString())) {
            binding.edtxtTitle.setError(getString(R.string.empty_field_error));
            binding.edtxtTitle.requestFocus();
            return false;
        }

        return true;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(KEY_IMAGE_PREVIEW_FILE_PATH, imageUri.getPath());
        }
        outState.putString(KEY_IMAGE_TITLE_TEXT, binding.txtTitle.getText().toString());
        outState.putString(KEY_IMAGE_DESCRIPTION_TEXT, binding.txtTitle.getText().toString());

        //To handle a crash on getting Permission we have commented this
//        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCameraSelected(SelectCameraOrGalleryDialog dialog) {
        takePictureFromCamera();
        dialog.dismiss();
    }

    @Override
    public void onGallerySelected(SelectCameraOrGalleryDialog dialog) {
        uploadImageFromGallery();
        dialog.dismiss();
    }
}