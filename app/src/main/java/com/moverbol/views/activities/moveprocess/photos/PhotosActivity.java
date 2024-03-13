package com.moverbol.views.activities.moveprocess.photos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.PhotoAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.PhotosBinding;
import com.moverbol.model.photoes.PhotoModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.PhotosViewModel;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Admin on 11-10-2017.
 */

public class PhotosActivity extends BaseAppCompactActivity {


    private static final int UPDATE_PHOTO_REQUEST_CODE = 101;
    private PhotosBinding binding;
    private PhotosViewModel viewModel;
    private PhotoAdapter adapter;
    private List<PhotoModel> mPhotoModelList;

  /*  private MenuItem addPhotoMenuItem;
    private MenuItem removePhotoMenuItem;
    private MenuItem selectPhotoMenuItem;
    private MenuItem cancelMenuItem;

    private boolean showSelectionOn = false;
    private boolean newPhotoAdded = false;*/

    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photos);
        setToolbar(binding.toolbar.toolbar, getString(R.string.photos), R.drawable.ic_arrow_back_white_24dp);

        viewModel = new ViewModelProvider(this).get(PhotosViewModel.class);


        adapter = new PhotoAdapter(new PhotoAdapter.PhotoesEventListener() {
            @Override
            public void onPhotoClicked(PhotoModel photoModel) {

                if (photoModel.getImages().isEmpty() && !photoModel.getName().equalsIgnoreCase(Constants.BOL_APP_PHOTOS)) {
                    String title = getString(R.string.no_photos_available);
                    if (photoModel.getName().equalsIgnoreCase(Constants.INVENTORY_PHOTOS)) {
                        title = getString(R.string.no_rooms_available);
                    } else if (photoModel.getName().equalsIgnoreCase(Constants.CRATING_PHOTOS)) {
                        title = getString(R.string.no_items_available);
                    }
                    showNoItemAlert(title);
                } else {
                    if (photoModel.getName().equalsIgnoreCase(Constants.INVENTORY_PHOTOS)) {

                        Intent intent = new Intent(PhotosActivity.this, PhotosRoomActivity.class);
                        intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_MODEL, photoModel);
                        startActivity(intent);
                    } else if (photoModel.getName().equalsIgnoreCase(Constants.CRATING_PHOTOS)) {
                        Intent intent = new Intent(PhotosActivity.this, PhotoItemActivity.class);
                        intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_MODEL, photoModel);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(PhotosActivity.this, ImagesActivity.class);
                        intent.putExtra(Constants.PhotoDetailsIntentExtras.EXTRA_PHOTO_MODEL, photoModel);
                        startActivityForResult(intent, UPDATE_PHOTO_REQUEST_CODE);
                    }
                }
            }

            @Override
            public void onPhotoLongClicked(PhotoModel photoModel) {
            }
        });

        viewModel.photoPojoList.observe(this, new Observer<List<PhotoModel>>() {
            @Override
            public void onChanged(@Nullable List<PhotoModel> photoModels) {
                if (photoModels != null) {
                    mPhotoModelList = photoModels;
                    adapter.setPhotoModelList(mPhotoModelList);
                  /*  if (selectPhotoMenuItem != null) {
                        //Made this change because of Fabric crash report
                        if (photoModels.size() > 0) {
                            selectPhotoMenuItem.setVisible(true);
                        } else {
                            selectPhotoMenuItem.setVisible(false);
                        }
                    }*/
                } /*else {
                    if (selectPhotoMenuItem != null)
                        selectPhotoMenuItem.setVisible(false);
                }*/
            }
        });


        binding.setAdapter(adapter);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (viewModel.photoPojoList.getValue() == null) {
            callGetSubmittedPhotosUrls();
        }
    }


  /*  public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        addPhotoMenuItem = menu.findItem(R.id.add_photo);
        removePhotoMenuItem = menu.findItem(R.id.remove_photo);
        selectPhotoMenuItem = menu.findItem(R.id.tick);
        cancelMenuItem = menu.findItem(R.id.cancel);

        selectPhotoMenuItem.setVisible(false);
        removePhotoMenuItem.setVisible(false);
        cancelMenuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_photo) {
            if (addPhotoMenuItem == null) {
                addPhotoMenuItem = item;
            }
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
    }*/


    private void callGetSubmittedPhotosUrls() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY)) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        showProgress();

        viewModel.getSubmittedPhotosUrls(subDomain, userId, opportunityId, jobId, new ResponseListener<BaseResponseModel<List<PhotoModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<PhotoModel>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PhotosActivity.this);
                    return;
                }
                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<PhotoModel>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            callGetSubmittedPhotosUrls();
        }

      /*  if (requestCode == ADD_PHOTO_REQUEST_CODE || requestCode == DELETE_PHOTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
               newPhotoAdded = true;
            setResult(RESULT_OK);
            callGetSubmittedPhotosUrls();
            }
        }*/
    }


  /*  private void startDeleteProcess() {
        if (mPhotoModelList != null) {
            adapter.showSelectionForAllPhotoes();
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
        adapter.hideSelectionForAllPhotoes();
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
        if (mPhotoModelList == null) {
            return false;
        }
        for (int i = 0; i < mPhotoModelList.size(); i++) {
            if (mPhotoModelList.get(i).isSelectedForDelete()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        if (newPhotoAdded) {
            setResult(RESULT_OK);
        }
        super.onStop();
    }*/


    private void showNoItemAlert(String title) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(true)
                .setMessage(title)
                .setNegativeButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

