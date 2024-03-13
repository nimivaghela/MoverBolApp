package com.moverbol.views.activities.moveprocess.photos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.ImageAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityImagesBinding;
import com.moverbol.model.photoes.ImagesModel;
import com.moverbol.model.photoes.PhotoModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.PhotosViewModel;

import java.util.ArrayList;

import retrofit2.Call;

public class ImagesActivity extends BaseAppCompactActivity {
    private static final int DELETE_PHOTO_REQUEST_CODE = 101;
    private static final int ADD_PHOTO_REQUEST_CODE = 100;

    private ActivityImagesBinding binding;
    private PhotoModel photoModel;
    private ImageAdapter imageAdapter;
    private ImagesModel openImageModel;
    private MenuItem addPhotoMenuItem;
    private MenuItem removePhotoMenuItem;
    private MenuItem selectPhotoMenuItem;
    private MenuItem cancelMenuItem;
    private boolean showSelectionOn = false;
    private PhotosViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_images);
        photoModel = getIntent().getParcelableExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_MODEL);
        setToolbar(binding.toolbar.toolbar, photoModel.getName(), R.drawable.ic_arrow_back_white_24dp);
        viewModel = new ViewModelProvider(this).get(PhotosViewModel.class);
        setRecyclerView();
    }

    private void setRecyclerView() {
        imageAdapter = new ImageAdapter(new ImageAdapter.PhotoesEventListener() {
            @Override
            public void onPhotoClicked(ImagesModel imagesModel) {
                if (imagesModel.isShowSelection()) {
                    imagesModel.setSelectedForDelete(!imagesModel.isSelectedForDelete());
                } else {
                    openImageModel = imagesModel;
                    Intent intent = new Intent(ImagesActivity.this, PhotoDetailActivity.class);
                    intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_ID, imagesModel.getcId());
                    intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_URL, imagesModel.getLink());
                    intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_TITLE, imagesModel.getPhotoTitle());
                    intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_DESCRIPTION, imagesModel.getPhotoDescription());
                    startActivityForResult(intent, DELETE_PHOTO_REQUEST_CODE);
                }
            }

            @Override
            public void onPhotoLongClicked(ImagesModel photoModel) {
                photoModel.setSelectedForDelete(true);
                startDeleteProcess();
            }
        });

        binding.rvPhotos.setAdapter(imageAdapter);
        updateList();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            if (requestCode == DELETE_PHOTO_REQUEST_CODE) {
                photoModel.getImages().remove(openImageModel);
                updateList();
            } else if (requestCode == ADD_PHOTO_REQUEST_CODE) {
                if (data != null) {
                    photoModel.getImages().add(data.getParcelableExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_MODEL));
                    updateList();
                }
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        addPhotoMenuItem = menu.findItem(R.id.add_photo);
        removePhotoMenuItem = menu.findItem(R.id.remove_photo);
        selectPhotoMenuItem = menu.findItem(R.id.tick);
        cancelMenuItem = menu.findItem(R.id.cancel);

        removePhotoMenuItem.setVisible(false);
        cancelMenuItem.setVisible(false);

        if (!photoModel.getName().equalsIgnoreCase(Constants.BOL_APP_PHOTOS)) {
            addPhotoMenuItem.setVisible(false);
            selectPhotoMenuItem.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_photo) {
            if (addPhotoMenuItem == null) {
                addPhotoMenuItem = item;
            }
            startActivityForResult(new Intent(this, AddPhotoActivity.class), ADD_PHOTO_REQUEST_CODE);
        }
        if (id == R.id.tick) {
            if (selectPhotoMenuItem == null) {
                selectPhotoMenuItem = item;
            }
            startDeleteProcess();
        }
        if (id == R.id.remove_photo) {
            if (removePhotoMenuItem == null) {
                removePhotoMenuItem = item;
            }
            if (validate()) {
                callDeleteSelectedPhotos();
            } else {
                Snackbar.make(binding.getRoot(), "Please select at least one photo", Snackbar.LENGTH_LONG).show();
            }
        }

        if (id == R.id.cancel) {
            if (cancelMenuItem == null) {
                cancelMenuItem = item;
            }
            cancelDeleteProcess();
        }

        return super.onOptionsItemSelected(item);
    }

    private void startDeleteProcess() {
        if (photoModel.getImages() != null) {
            imageAdapter.showSelectionForAllPhotoes();
            showSelectionOn = true;
        }
        if (addPhotoMenuItem != null) {
            addPhotoMenuItem.setVisible(false);
        }
        if (selectPhotoMenuItem != null) {
            selectPhotoMenuItem.setVisible(false);
        }
        if (removePhotoMenuItem != null) {
            removePhotoMenuItem.setVisible(true);
        }
        if (cancelMenuItem != null) {
            cancelMenuItem.setVisible(true);
        }
    }

    private void cancelDeleteProcess() {
        imageAdapter.hideSelectionForAllPhotoes();
        showSelectionOn = false;
        if (addPhotoMenuItem != null) {
            addPhotoMenuItem.setVisible(true);
        }
        if (selectPhotoMenuItem != null) {
            selectPhotoMenuItem.setVisible(true);
        }
        if (removePhotoMenuItem != null) {
            removePhotoMenuItem.setVisible(false);
        }
        if (cancelMenuItem != null) {
            cancelMenuItem.setVisible(false);
        }
    }

    private boolean validate() {
        if (photoModel.getImages() == null) {
            return false;
        }
        for (ImagesModel imagesModel : photoModel.getImages()) {
            if (imagesModel.isSelectedForDelete()) {
                return true;
            }
        }
        return false;
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

        StringBuilder concatenatedIds = new StringBuilder();

        ArrayList<ImagesModel> selectedDeleteModel = new ArrayList<ImagesModel>(0);

        for (ImagesModel imagesModel : photoModel.getImages()) {
            if (imagesModel.isSelectedForDelete()) {
                if (concatenatedIds.toString().equals("")) {
                    concatenatedIds = new StringBuilder(imagesModel.getcId());
                } else {
                    concatenatedIds.append(",").append(imagesModel.getcId());
                }
                selectedDeleteModel.add(imagesModel);
            }
        }

        viewModel.deletePhotoes(subDomain, userId, opportunityId, jobId, concatenatedIds.toString(), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                setResult(RESULT_OK);
                cancelDeleteProcess();
                for (ImagesModel imagesModel : selectedDeleteModel) {
                    photoModel.getImages().remove(imagesModel);
                }
                updateList();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ImagesActivity.this);
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

    public void updateList() {
        imageAdapter.setImagesModelList(photoModel.getImages());
        if (selectPhotoMenuItem != null) {
            if (photoModel.getImages().size() > 0) {
                selectPhotoMenuItem.setVisible(true);
            } else {
                selectPhotoMenuItem.setVisible(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (showSelectionOn) {
            cancelDeleteProcess();
            return;
        }
        super.onBackPressed();
    }
}
