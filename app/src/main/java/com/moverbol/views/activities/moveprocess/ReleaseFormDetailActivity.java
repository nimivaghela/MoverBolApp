package com.moverbol.views.activities.moveprocess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.adapters.AddressSelectionListAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ReleaseFormDetailsBinding;
import com.moverbol.model.releaseForm.AddressSelectionListItemModel;
import com.moverbol.model.releaseForm.ReleaseFormMetadataPojo;
import com.moverbol.model.releaseForm.ReleaseFormRequestModel;
import com.moverbol.model.releaseForm.ReleaseFormResponseModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.ReleaseFormViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ReleaseFormDetailActivity extends BaseAppCompactActivity {

    private static final String EXTRA_RELEASE_FORM_METADATA_POJO = "extra_release_form_metadata_pojo";
    private static final String EXTRA_RELEASE_FORM_RESPONSE_MODEL = "extra_release_form_response_model";
    private ReleaseFormDetailsBinding binding;
    private ReleaseFormMetadataPojo mReleaseFormMetadataPojo;
    private ReleaseFormViewModel viewModel;
    private ReleaseFormResponseModel mReleaseFormResponseModel;
    private AddressSelectionListAdapter adapter;
    private List<AddressSelectionListItemModel> mSelectedAddressList;
    private List<AddressSelectionListItemModel> mAddressList;


    public static void start(Context context, ReleaseFormMetadataPojo releaseFormMetadataPojo
            , ReleaseFormResponseModel releaseFormResponseModel, String opportunityId) {
        Intent intent = new Intent(context, ReleaseFormDetailActivity.class);
        intent.putExtra(EXTRA_RELEASE_FORM_METADATA_POJO, releaseFormMetadataPojo);
        intent.putExtra(EXTRA_RELEASE_FORM_RESPONSE_MODEL, releaseFormResponseModel);
        intent.putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, opportunityId);

        context.startActivity(intent);
    }


    public static void startForResult(Activity activity, ReleaseFormMetadataPojo releaseFormMetadataPojo
            , ReleaseFormResponseModel releaseFormResponseModel, String opportunityId, int requestCode) {
        Intent intent = new Intent(activity, ReleaseFormDetailActivity.class);
        intent.putExtra(EXTRA_RELEASE_FORM_METADATA_POJO, releaseFormMetadataPojo);
        intent.putExtra(EXTRA_RELEASE_FORM_RESPONSE_MODEL, releaseFormResponseModel);
        intent.putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, opportunityId);

        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialise();
        setIntentData();
        setActionListener();
        setVieModelObservers();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (adapter != null && mSelectedAddressList != null) {
            if (mSelectedAddressList.isEmpty()) {
                mSelectedAddressList.add(new AddressSelectionListItemModel("", false, true));
            }
            adapter.setmHomeList(mSelectedAddressList);
        }
    }

    private void initialise() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_release_form_detail);

        setToolbar(binding.toolbar.toolbar, getString(R.string.release_form), R.drawable.ic_arrow_back_white_24dp);

        viewModel = new ViewModelProvider(this).get(ReleaseFormViewModel.class);
        adapter = new AddressSelectionListAdapter();
        mSelectedAddressList = new ArrayList<>();
        mAddressList = new ArrayList<>();
        /*mSelectedAddressList.add(new AddressSelectionListItemModel("", false, true));
        adapter.setmHomeList(mSelectedAddressList);*/
        if (mReleaseFormMetadataPojo != null) {
            binding.setObj(mReleaseFormMetadataPojo);
        }

        binding.setBolStarted(MoversPreferences.getInstance(this).getBolStarted(MoversPreferences.getInstance(this).getCurrentJobId()));
        binding.setAdapter(adapter);
    }

    private void setIntentData() {
        if (getIntent().hasExtra(EXTRA_RELEASE_FORM_METADATA_POJO)) {
            mReleaseFormMetadataPojo = getIntent().getParcelableExtra(EXTRA_RELEASE_FORM_METADATA_POJO);
            if (viewModel.releaseFormMetadataPojoLive.getValue() == null) {
                viewModel.releaseFormMetadataPojoLive.setValue(mReleaseFormMetadataPojo);
            }
        }

        if (getIntent().hasExtra(EXTRA_RELEASE_FORM_RESPONSE_MODEL)) {
            mReleaseFormResponseModel = getIntent().getParcelableExtra(EXTRA_RELEASE_FORM_RESPONSE_MODEL);
            if (viewModel.releaseFormResponseModelLive.getValue() == null) {
                viewModel.releaseFormResponseModelLive.setValue(mReleaseFormResponseModel);
            }
        }
    }

    private void setActionListener() {
        binding.txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.signaturePad.clear();
            }
        });

        /*binding.edtxtAdddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressSelectionDialog(v);
            }
        });

        binding.imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressSelectionDialog(binding.edtxtAdddress);
            }
        });*/

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    callSubmitReleaseFormDetails();
                }
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReleaseFormMetadataPojo.setSelected(false);
                binding.setObj(mReleaseFormMetadataPojo);
              /*  binding.editExceptionDescription.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.editExceptionDescription.setText(mReleaseFormMetadataPojo.getDescription());
                    }
                }, 500);
*/
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // callCancelReleaseFormSelection();

                /*mReleaseFormMetadataPojo.setSelected(false);
                binding.setObj(mReleaseFormMetadataPojo);
                binding.edtxtSmallDescription.setFocusable(true);*/
            }
        });

        adapter.setListener(new AddressSelectionListAdapter.SelectAddressClickListener() {
            @Override
            public void onSelectAddressClicked(EditText editText, int adapterPosition) {
                openAddressSelectionDialog();
            }
        });

    }


    private void setVieModelObservers() {
        viewModel.releaseFormMetadataPojoLive.observe(this, new Observer<ReleaseFormMetadataPojo>() {
            @Override
            public void onChanged(@Nullable ReleaseFormMetadataPojo releaseFormMetadataPojo) {
                if (releaseFormMetadataPojo == null) {
                    return;
                }
                mReleaseFormMetadataPojo = releaseFormMetadataPojo;

/*
                if (mReleaseFormMetadataPojo.getAddressList() != null && !mReleaseFormMetadataPojo.getAddressList().isEmpty()) {
                    mSelectedAddressList.clear();
                    for (int i = 0; i < mReleaseFormMetadataPojo.getAddressList().size(); i++) {
                        AddressSelectionListItemModel addressSelectionListItemModel =
                                new AddressSelectionListItemModel(mReleaseFormMetadataPojo.getAddressList().get(i), false, true);
                        mSelectedAddressList.add(addressSelectionListItemModel);
                    }

//                    adapter.setmHomeList(mSelectedAddressList);
                }*/

                binding.setObj(mReleaseFormMetadataPojo);
            }
        });

        viewModel.releaseFormResponseModelLive.observe(this, new Observer<ReleaseFormResponseModel>() {
            @Override
            public void onChanged(@Nullable ReleaseFormResponseModel releaseFormResponseModel) {
                if (releaseFormResponseModel == null) {
                    return;
                }

                mReleaseFormResponseModel = releaseFormResponseModel;

                mAddressList.clear();

                if (mReleaseFormResponseModel.getPickupAddress() != null) {
                    String address = mReleaseFormResponseModel.getPickupAddress().getFullAddressString();
                    AddressSelectionListItemModel model = new AddressSelectionListItemModel(address, false, false);
                    mAddressList.add(model);
                }

                if (mReleaseFormResponseModel.getDeliveryAddress() != null) {
                    String address = mReleaseFormResponseModel.getDeliveryAddress().getFullAddressString();
                    AddressSelectionListItemModel model = new AddressSelectionListItemModel(address, false, false);
                    mAddressList.add(model);
                }

                if (mReleaseFormResponseModel.getPickupExtraStops() != null && !mReleaseFormResponseModel.getPickupExtraStops().isEmpty()) {
                    for (int i = 0; i < mReleaseFormResponseModel.getPickupExtraStops().size(); i++) {
                        String address = mReleaseFormResponseModel.getPickupExtraStops().get(i).getFullAddressString();
                        AddressSelectionListItemModel model = new AddressSelectionListItemModel(address, false, false);
                        mAddressList.add(model);
                    }
                }

                if (mReleaseFormResponseModel.getDeliveryExtraStops() != null && !mReleaseFormResponseModel.getDeliveryExtraStops().isEmpty()) {
                    for (int i = 0; i < mReleaseFormResponseModel.getDeliveryExtraStops().size(); i++) {
                        String address = mReleaseFormResponseModel.getDeliveryExtraStops().get(i).getFullAddressString();
                        AddressSelectionListItemModel model = new AddressSelectionListItemModel(address, false, false);
                        mAddressList.add(model);
                    }
                }

            }
        });
    }


    private void callSubmitReleaseFormDetails() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY)) {
            return;
        }


        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = getIntent().getStringExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY);

//        List<ReleaseFormMetadataPojo> metadataPojoList = viewModel.getSelectedMetadataObject(viewModel.releaseFormResponseModelLive.getValue());

        if (mReleaseFormMetadataPojo == null) {
            return;
        }

        showProgress();

        List<ReleaseFormRequestModel> requestModelList = new ArrayList<>();

        String encoded = Util.getBase64EncodeStringFromBitmap(binding.signaturePad.getSignatureBitmap());

        ReleaseFormRequestModel requestModel = new ReleaseFormRequestModel();


//            requestModel.setAddress(mReleaseFormMetadataPojo.getAddress());

        requestModel.setAddressList(mReleaseFormMetadataPojo.getAddressList());
        requestModel.setAddress(null);
        requestModel.setReleaseFormId(mReleaseFormMetadataPojo.getReleaseFormId());
        requestModel.setDescription(Objects.requireNonNull(binding.editExceptionDescription.getText()).toString().trim());
        if (mReleaseFormMetadataPojo.getSelectionId() != null) {
            requestModel.setSelectionId(mReleaseFormMetadataPojo.getSelectionId());
        } else {
            requestModel.setSelectionId("");
        }
//        requestModel.setId("");
        requestModelList.add(requestModel);

        File file = Util.BitmapToFile(this, binding.signaturePad.getSignatureBitmap());

        viewModel.submitReleaseFormDetails(subDomain, userId, opportunityId, requestModel, encoded, jobId, file, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                if (mReleaseFormMetadataPojo != null && !mReleaseFormMetadataPojo.isSelected()) {
                    setResult(RESULT_OK);
                }
                finish();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ReleaseFormDetailActivity.this);
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


    private void callCancelReleaseFormSelection() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        if (getIntent() == null || !getIntent().hasExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY)) {
            return;
        }

        if (mReleaseFormMetadataPojo == null) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = getIntent().getStringExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY);

        showProgress();

        viewModel.cancelReleaseFormSelection(subDomain, userId, jobId, opportunityId, mReleaseFormMetadataPojo.getSelectionId(), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                setResult(RESULT_OK);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(ReleaseFormDetailActivity.this);
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


    private boolean validate() {

        /*if (TextUtils.isEmpty(mReleaseFormMetadataPojo.getAddress())) {
            binding.edtxtAdddress.setError(getText(R.string.empty_field_error));
            binding.edtxtAdddress.requestFocus();

            Snackbar.make(binding.getRoot(), R.string.select_address_error, Snackbar.LENGTH_LONG).show();

            return false;
        }*/

        if (mSelectedAddressList == null || mSelectedAddressList.isEmpty()) {
            return false;
        } else {
            if (mSelectedAddressList.get(0).getAddress().isEmpty()) {
                mSelectedAddressList.get(0).setShouldShowAddressEmptyError(true);
                adapter.setmHomeList(mSelectedAddressList);
                return false;
            }
            /*for (int i = 0; i < mSelectedAddressList.size(); i++) {
                if (mSelectedAddressList.get(i).getAddress().isEmpty()) {
                    mSelectedAddressList.get(i).setShouldShowAddressEmptyError(true);
                    adapter.setmHomeList(mSelectedAddressList);
                    return false;
                }
            }*/
        }

        if (binding.signaturePad.isEmpty()) {
            Snackbar.make(binding.getRoot(), R.string.signature_required_error, Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    /*public void openAddressSelectionDialog(final View view) {

        if (!(view instanceof EditText) || mReleaseFormMetadataPojo == null ||
                mReleaseFormMetadataPojo.isSelected() || binding.getBolStarted()) {
            return;
        }

        final EditText editText = (EditText) view;
        editText.setError(null);
        String pickupAddress = "";
        String deliveryAddress = "";


        if (mReleaseFormResponseModel != null) {
            if (mReleaseFormResponseModel.getPickupAddress() != null) {
                pickupAddress = mReleaseFormResponseModel.getPickupAddress().getFullAddressStraightString();
            }
            if (mReleaseFormResponseModel.getDeliveryAddress() != null) {
                deliveryAddress = mReleaseFormResponseModel.getDeliveryAddress().getFullAddressStraightString();
            }
        } else {
            return;
        }

        final String[] addressArray = {pickupAddress, deliveryAddress};

        int checkedItem = -1;

        if (!TextUtils.isEmpty(editText.getText().toString())) {
            if (TextUtils.equals(editText.getText().toString(), pickupAddress)) {
                checkedItem = 0;
            } else if (TextUtils.equals(editText.getText().toString(), deliveryAddress)) {
                checkedItem = 1;
            }
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Select an address")
                .setCancelable(true)
                .setSingleChoiceItems(addressArray, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText.setText(addressArray[which]);
                        dialog.dismiss();
                    }
                })
                .create();

        alertDialog.show();
    }*/


    public void openAddressSelectionDialog() {

        if (mReleaseFormMetadataPojo == null || mAddressList == null || mAddressList.isEmpty() || binding.getBolStarted()) {
            return;
        }


        final String[] addressArray = new String[mAddressList.size()];
        final boolean[] checkedItems = new boolean[mAddressList.size()];


        if (mSelectedAddressList != null && !mSelectedAddressList.isEmpty()) {
            for (int i = 0; i < mSelectedAddressList.size(); i++) {
                AddressSelectionListItemModel model = mSelectedAddressList.get(i);
                for (int j = 0; j < mAddressList.size(); j++) {
                    if (model.getAddress().equals(mAddressList.get(j).getAddress())) {
                        mAddressList.get(j).setSelected(true);
                    }
                }
            }
        }

        for (int i = 0; i < mAddressList.size(); i++) {
            addressArray[i] = mAddressList.get(i).getAddress();
            checkedItems[i] = mAddressList.get(i).isSelected();
        }


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Select an address")
                .setCancelable(true)
                .setMultiChoiceItems(addressArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                })
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mSelectedAddressList.clear();

                        for (int i = 0; i < mAddressList.size(); i++) {
                            mAddressList.get(i).setSelected(checkedItems[i]);
                            if (checkedItems[i]) {
                                mSelectedAddressList.add(mAddressList.get(i));
                            }
                        }

                        if (mSelectedAddressList.isEmpty()) {
                            mSelectedAddressList.add(new AddressSelectionListItemModel("", false, false));
                        }
                        adapter.setmHomeList(mSelectedAddressList);
                        List<String> adressList = new ArrayList<>();
                        for (int i = 0; i < mSelectedAddressList.size(); i++) {
                            adressList.add(mSelectedAddressList.get(i).getAddress());
                        }

                        mReleaseFormMetadataPojo.setAddressList(adressList);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        alertDialog.show();
    }


}
