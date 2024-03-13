package com.moverbol.views.activities.moveprocess.bill_of_lading;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.BolSignatureDialog;
import com.moverbol.custom.dialogs.EditBolDiscountDialog;
import com.moverbol.custom.dialogs.RatingDialog;
import com.moverbol.custom.dialogs.TermsAndPoliciesDialog;
import com.moverbol.custom.dialogs.TipDiscountDialog;
import com.moverbol.databinding.BillOfLadingBinding;
import com.moverbol.model.billOfLading.BillOfLadingPojo;
import com.moverbol.model.billOfLading.BillOfLadingRequestModel;
import com.moverbol.model.billOfLading.BolDetailsPojo;
import com.moverbol.model.billOfLading.CouponDetailsModel;
import com.moverbol.model.billOfLading.RatingDiscountPercentagePojo;
import com.moverbol.model.billOfLading.RawBodyBOLSignatureSubmitRequestModel;
import com.moverbol.model.billOfLading.TipsModel;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.CommonChargesRequestModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.BillOfLadingViewModel;
import com.moverbol.views.activities.PickupExtraStopActivity;
import com.moverbol.views.activities.confirmation_detail.ValuationActivity;
import com.moverbol.views.activities.moveprocess.PaymentActivity;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Admin on 09-10-2017.
 */

public class BillOfLadingActivity extends BaseAppCompactActivity implements BolSignatureDialog.OnSignatureSubmittedListener,
        EditBolDiscountDialog.OnDiscountSubmittedListener, RatingDialog.RatingActionListener,
        TipDiscountDialog.OnTipSubmittedListener {

    public static final String EXTRA_FAILED_TO_GET_DATA_MESSAGE = "extra_failed_to_get_data";
    public static final int RESULT_FAILED_TO_GET_DATA = 100;
    public static final String EXTRA_BOL_APPROVAL_STATUS = "bol_status";
    public static final String BOL_APPROVAL_STATUS_ACCEPTED = "ACCEPTED";
    public static final String BOL_APPROVAL_STATUS_REJECTED = "REJECTED";
    private static final int MOVE_CHARGES_REQUEST_CODE = 1;
    private static final int PACKING_CHARGES_REQUEST_CODE = 2;
    private static final int STORAGE_CHARGES_REQUEST_CODE = 3;
    private static final int ADDITIONAL_CHARGES_REQUEST_CODE = 4;
    private static final int VALUATION_CHARGES_REQUEST_CODE = 5;
    private static final int CRATING_CHARGES_REQUEST_CODE = 6;
    private static final int STORAGE_RECURRING_CHARGES_REQUEST_CODE = 7;
    private static final String EXTRA_CHARGES_LIST = "extra_charges_list";
    private static final String EXTRA_CALCULATED_CHARGE_ITEM = "extra_calculated_charge_item";
    private static final String EXTRA_NEW_CHARGES_TOTAL = "extra_new_charges_total";
    private static final String EXTRA_CHARGES_CHANGED = "charges_changed";
    private static final int BOL_REQUEST_STATUS_SEND_FOR_APPROVAL = 1;
    private static final int BOL_REQUEST_STATUS_COMPLETE_BOL = 2;
    private static final String MODEL_TIP = "tips_discount";
    private static final String MODEL_REVIEW_DISCOUNT = "review_discount";
    private static final String MODEL_RATING = "ratings";
    private static final String EXTRA_CLOCK_REQUIRED = "extra_clock_required";
    private static final int VALUATION_UPDATE_REQUEST_CODE = 201;
    double mReviewDiscount;
    double mTip;
    boolean mIsTipDiscountPercentage;
    private BillOfLadingBinding binding;
    private BillOfLadingViewModel viewModel;
    private String mJobId;
    private String mMoveChargesPriceTypeIndex;
    private String mBolFinalChargeId;
    private BillOfLadingPojo mBillOfLadingPojo;
    private AlertDialog mSentForApprovalDialog;
    private boolean mActivityCalledFromNotification = false;

    private boolean mIsClockRequired;


    public static void setChargesResult(@NonNull Activity activity, @Nullable CommonChargesRequestModel calculatedChargeItem, @NonNull double newTotalCharges, boolean chargesChanged, int discountType, double discountValue) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CALCULATED_CHARGE_ITEM, calculatedChargeItem);
        intent.putExtra(EXTRA_NEW_CHARGES_TOTAL, newTotalCharges);
        intent.putExtra(EXTRA_CHARGES_CHANGED, chargesChanged);
        intent.putExtra(Constants.DISCOUNT_TYPE, discountType);
        intent.putExtra(Constants.DISCOUNT, discountValue);

        Log.d(Constants.BASE_LOG_TAG + " " + BillOfLadingActivity.class.getSimpleName(), activity.getClass().getSimpleName() + " total charges" + newTotalCharges);

        activity.setResult(RESULT_OK, intent);
    }


    public static void setChargesResult(@NonNull Activity activity, @NonNull double newTotalCharges, boolean chargesChanged, int discountType, double discountValue) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NEW_CHARGES_TOTAL, newTotalCharges);
        intent.putExtra(EXTRA_CHARGES_CHANGED, chargesChanged);
        intent.putExtra(Constants.DISCOUNT_TYPE, discountType);
        intent.putExtra(Constants.DISCOUNT, discountValue);
        Log.d(Constants.BASE_LOG_TAG + " " + BillOfLadingActivity.class.getSimpleName(), activity.getClass().getSimpleName() + " total charges" + newTotalCharges);
        activity.setResult(RESULT_OK, intent);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, BillOfLadingActivity.class);
        context.startActivity(starter);
    }

    public static void startForResult(Activity activity, int requestCode, boolean clockRequired) {
        Intent starter = new Intent(activity, BillOfLadingActivity.class);
        starter.putExtra(EXTRA_CLOCK_REQUIRED, clockRequired);
        activity.startActivityForResult(starter, requestCode);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util.hideSoftKeyboard(this);

        initialisation();
        setIntentData();
        setActionListeners();
        setViewModelObservers();

        binding.setIsJobHasSingleActivity(MoversPreferences.getInstance(BillOfLadingActivity.this).isJobHasSingleActivity(mJobId));

        /*if (MoversPreferences.getInstance(this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.APPROVED) {
            binding.btnSendForApproval.setVisibility(View.GONE);
            binding.btnCompleteBol.setVisibility(View.VISIBLE);
        } else*/

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        /*if (MoversPreferences.getInstance(BillOfLadingActivity.this).getCurrentJobBolStatus(mJobId) == Constants.BolStatus.PENDING_APPROVAL) {
            showSentForApprovalDialog();
            return;
        }*/


        callSetBillOfLading();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(Constants.EXTRA_NOTIFICATION_INTENT) && intent.getBooleanExtra(Constants.EXTRA_NOTIFICATION_INTENT, false)) {
            handleNotificationIntent(intent);
        }
    }


    private void initialisation() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_of_ladding);
        setToolbar(binding.toolbar.toolbar, getString(R.string.bill_of_lading), R.drawable.ic_arrow_back_white_24dp);
        viewModel = new ViewModelProvider(this).get(BillOfLadingViewModel.class);

        mBillOfLadingPojo = new BillOfLadingPojo();

        binding.setCurrencySymbol(MoversPreferences.getInstance(BillOfLadingActivity.this).getCurrencySymbol());
        Util.showLog(getClass().getSimpleName(), "initialisation");
    }

    private void setIntentData() {
        if (getIntent().hasExtra(Constants.EXTRA_NOTIFICATION_INTENT) && getIntent().getBooleanExtra(Constants.EXTRA_NOTIFICATION_INTENT, false)) {
            handleNotificationIntent(getIntent());
            return;
        }
        if (getIntent().hasExtra(Constants.EXTRA_JOB_ID_KEY)) {
            mJobId = getIntent().getStringExtra(Constants.EXTRA_JOB_ID_KEY);
        } else {
            mJobId = MoversPreferences.getInstance(this).getCurrentJobId();
        }

        if (getIntent().hasExtra(EXTRA_CLOCK_REQUIRED)) {
            mIsClockRequired = getIntent().getBooleanExtra(EXTRA_CLOCK_REQUIRED, false);
        }
    }

    private void setActionListeners() {

        binding.btnApply.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.etCouponCode.getText().toString())) {
                binding.etCouponCode.setError(getString(R.string.empty_field_error));
            } else {
                callSubmitCouponCode(binding.etCouponCode.getText().toString());
            }
        });


      /*  binding.imgActualHoursInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                JobTimingDetailsActivity.start(BillOfLadingActivity.this, mJobId);
            }
        });

        binding.imgActualTravelTimeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                JobTimingDetailsActivity.startForOnlyDriveJob(BillOfLadingActivity.this, mJobId);
            }
        });*/

        binding.btnDiscountEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditBolDiscountDialog();
            }
        });

        binding.txtPickupAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = binding.txtPickupAddress.getText().toString();
                Util.openMapForGivenAddress(address, v.getContext());
            }
        });

        binding.txtDeliveryLocationAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = binding.txtDeliveryLocationAddress.getText().toString();
                Util.openMapForGivenAddress(address, v.getContext());
            }
        });

        binding.btnDetails.setOnClickListener(v -> BillingSummaryActivity.start(BillOfLadingActivity.this, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getActivityFlag()));

        binding.swipeRefresh.setOnRefreshListener(this::callSetBillOfLading);

        /*binding.btnStartBol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBolStartAlert();
            }
        });*/

        binding.btnViewPriorWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarryForwardDetailsActivity.start(BillOfLadingActivity.this);
            }
        });

    }

    private void showEditBolDiscountDialog() {
        EditBolDiscountDialog.start(this, getSupportFragmentManager());
    }

    private void setViewModelObservers() {

        viewModel.billOfLadingPojoLive.observe(this, billOfLadingPojo -> {
            binding.mainConstraint.setVisibility(View.VISIBLE);
            if (billOfLadingPojo == null) {
                return;
            }

            mMoveChargesPriceTypeIndex = billOfLadingPojo.getJobConfirmationDetailsPojo().getMoveChargePriceType();

            // double actualHours = Util.setMinuteInterval(MoversPreferences.getInstance(BillOfLadingActivity.this).getActualHours(mJobId), Double.parseDouble(MoversPreferences.getInstance(BillOfLadingActivity.this).getDefaultMinHours(mJobId)), MoversPreferences.getInstance(BillOfLadingActivity.this).getIncrementMinValue(mJobId));
            // double actualTravelTime = MoversPreferences.getInstance(BillOfLadingActivity.this).getActualTravelTime(mJobId);

            //double actualHoursToShow = (actualHours / (1000 * 3600));
            //double actualTravelTimeToShow = (actualTravelTime / (1000 * 3600));

            //billOfLadingPojo.getJobConfirmationDetailsPojo().setActualHours(Util.getFormattedDoubleString(actualHoursToShow, Constants.DoubleFormats.FORMAT_FOR_MILLI_TO_HOURS, Constants.DoubleFormats.STRING_FORMAT_FOR_MILLI_TO_HOURS) + "");
            //billOfLadingPojo.getJobConfirmationDetailsPojo().setActualTravelTime(Util.getFormattedDoubleString(actualTravelTimeToShow, Constants.DoubleFormats.FORMAT_FOR_MILLI_TO_HOURS, Constants.DoubleFormats.STRING_FORMAT_FOR_MILLI_TO_HOURS) + "");

            billOfLadingPojo.setTotalCalculatedMoveCharges(billOfLadingPojo.getJobConfirmationDetailsPojo().getMoveCharges());
            billOfLadingPojo.setTotalCalculatedValuationCharges(billOfLadingPojo.getJobConfirmationDetailsPojo().getValuation());

            mBillOfLadingPojo = billOfLadingPojo;
            mBolFinalChargeId = billOfLadingPojo.getJobConfirmationDetailsPojo().getBolFinalChargesPojo().getId();

            binding.setCurrencySymbol(MoversPreferences.getInstance(BillOfLadingActivity.this).getCurrencySymbol());

            String moveTypeId = MoversPreferences.getInstance(BillOfLadingActivity.this).getCurrentJobMoveTypeId();
            binding.setIsLocal(TextUtils.equals(moveTypeId, Constants.MoveTypeIds.MOVE_TYPE_LOCAL));

            binding.setIsLocal(true);

            /**
             * Kept just in case client changes the requirements again. Remove after Production Release
             */
            /*if (billOfLadingPojo.isNewValuationCalculated()) {

            } else {
                callGetSubmittedValuation();
            }*/

            binding.setIsBolStarted(mBillOfLadingPojo.getJobConfirmationDetailsPojo().getBolFinalChargesPojo().getBolStarted());
            binding.setIsBolApprovalPending(mBillOfLadingPojo.getJobConfirmationDetailsPojo().getBolSentForApproval().equalsIgnoreCase("1"));

            binding.setObj(mBillOfLadingPojo);

            MoversPreferences.getInstance(BillOfLadingActivity.this).setBolStarted(billOfLadingPojo.getJobConfirmationDetailsPojo().getBolFinalChargesPojo().getBolStarted(), mJobId);


          /*  if(mMoveChargesPriceTypeIndex.equals(Constants.MoveChargesPriceTypes.MOVE_CHARGES_AUTO_PRICING_INDEX + "")) {
                Toast.makeText(BillOfLadingActivity.this, "Min Hours: " + mBillOfLadingPojo.getJobConfirmationDetailsPojo().getDefaultMinHours() + " Move charges auto pricing", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BillOfLadingActivity.this, "Min Hours: " + mBillOfLadingPojo.getJobConfirmationDetailsPojo().getDefaultMinHours() + " Move charges manual pricing", Toast.LENGTH_SHORT).show();
            }*/

            boolean isTipAdded = billOfLadingPojo.getJobConfirmationDetailsPojo().getBolFinalChargesPojo().getTipAdded();
            MoversPreferences.getInstance(BillOfLadingActivity.this).setTipHasBeenSubmittedOrSkipped(mJobId, isTipAdded);

        });

    }

    private void handleNotificationIntent(Intent intent) {
        mActivityCalledFromNotification = true;
        String bolStatus = intent.getStringExtra(EXTRA_BOL_APPROVAL_STATUS);
        mJobId = intent.getStringExtra(Constants.EXTRA_JOB_ID_KEY);
        String opportunityId = intent.getStringExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY);

        if (opportunityId != null) {
            MoversPreferences.getInstance(this).setOpportunityId(opportunityId);
        }

       /* if (TextUtils.equals(bolStatus.toLowerCase(), BOL_APPROVAL_STATUS_ACCEPTED.toLowerCase())) {
            MoversPreferences.getInstance(this).setCurrentJobBolStatus(mJobId, Constants.BolStatus.APPROVED);
            binding.setIsBolApprovalPending(false);
            binding.btnSentForApproval.setVisibility(View.GONE);
        } else if (TextUtils.equals(bolStatus, BOL_APPROVAL_STATUS_REJECTED)) {
            MoversPreferences.getInstance(this).setCurrentJobBolStatus(mJobId, Constants.BolStatus.REJECTED);
            binding.setIsBolApprovalPending(true);
            binding.btnSentForApproval.setVisibility(View.VISIBLE);
        }*/

        if (mSentForApprovalDialog != null && mSentForApprovalDialog.isShowing()) {
            mSentForApprovalDialog.dismiss();
        }

        int newNotificationCount = MoversPreferences.getInstance(this).getNotificationCount() - 1;

        MoversPreferences.getInstance(this).setNotificationCount(newNotificationCount);
        callSetBillOfLading();
        Util.showLog(getClass().getSimpleName(), "handleNotificationIntent");
    }

    private void callSubmitCouponCode(String couponCode) {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        showProgress();
        viewModel.submitCouponCode(subdomain, userId, opportunityId, couponCode, new ResponseListener<BaseResponseModel<CouponDetailsModel>>() {
            @Override
            public void onResponse(BaseResponseModel<CouponDetailsModel> response, String usedUrlKey) {
                hideProgress();
                if (mBillOfLadingPojo != null) {
                    mBillOfLadingPojo.setCouponDetailsModel(response.getData());
                    mBillOfLadingPojo.setShouldShowDiscount(true);
                    binding.setObj(mBillOfLadingPojo);
                }
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(BillOfLadingActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<CouponDetailsModel>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }

        });

    }

    private void callSetBillOfLading() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        binding.swipeRefresh.setRefreshing(true);

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        double actualHours = Util.setMinuteInterval(MoversPreferences.getInstance(BillOfLadingActivity.this).getActualHours(mJobId), Double.parseDouble(MoversPreferences.getInstance(BillOfLadingActivity.this).getDefaultMinHours(mJobId)), MoversPreferences.getInstance(BillOfLadingActivity.this).getIncrementMinValue(mJobId));
        long actualTravelTime = MoversPreferences.getInstance(BillOfLadingActivity.this).getActualTravelTime(mJobId);

        // binding.txtActualHoursValue.setText(Util.getGeneralFormattedDecimalString(actualHours / (1000.00 * 60.00 * 60.00)));
        // binding.txtActualTraveltimeValue.setText(Util.getGeneralFormattedDecimalString(actualTravelTime / (1000.00 * 60.00 * 60.00)));

        //String totalCalculationTime = (actualTravelTime + actualHours) + "";

        viewModel.setJobConfirmation(subdomain, userId, opportunityId, mJobId, actualTravelTime, actualHours, new ResponseListener<BaseResponseModel<BolDetailsPojo>>() {
            @Override
            public void onResponse(BaseResponseModel<BolDetailsPojo> response, String usedUrlKey) {
                binding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                binding.swipeRefresh.setRefreshing(false);

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(BillOfLadingActivity.this);
                    return;
                }

                setActivityResult(serverResponseError.getMessage());
                finish();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<BolDetailsPojo>> call, Throwable t, String message) {
                binding.swipeRefresh.setRefreshing(false);
                setActivityResult(message);
                finish();
            }
        });
    }

    public void onNextClicked(View view) {


        switch (view.getId()) {
            case R.id.btn_send_for_approval:
                callSubmitBOL(BOL_REQUEST_STATUS_SEND_FOR_APPROVAL);
                break;

            case R.id.btn_complete_bol:
                callSubmitBOL(BOL_REQUEST_STATUS_COMPLETE_BOL);
                    /*PaymentActivity.start(BillOfLadingActivity.this,
                            mBillOfLadingPojo.getTotal() + mBillOfLadingPojo.getJobConfirmationDetailsPojo().getDepositeAmount(),
                            mReviewDiscount, mIsTipDiscountPercentage, mTip,
                            mBillOfLadingPojo.getJobConfirmationDetailsPojo().getDepositeAmount(), mBolFinalChargeId);*/
                break;

            case R.id.btn_start_bol:
                showBolStartAlert();
                break;
        }
    }

    private boolean validate() {
        if (MoversPreferences.getInstance(this).isClockIsOn(mJobId)) {
            showClockIsOnDialog();
            return false;
        } else if (!mBillOfLadingPojo.getJobConfirmationDetailsPojo().getValuationFlag()) {
            showValuationNotSelectedDialog();
            return false;
        }

        return true;
    }

    /*private void showReviewDiscountDialog() {
        ReviewDiscountDialog.startWithActionListeners(getSupportFragmentManager(), new ReviewDiscountDialog.OnReviewDiscountSubmittedListener() {
            @Override
            public void onDiscountSubmitted(double discountAmount, ReviewDiscountDialog reviewDiscountDialog) {
                callSubmitReviewDiscount(discountAmount, reviewDiscountDialog);
            }

            @Override
            public void onSkip(ReviewDiscountDialog reviewDiscountDialog) {
                reviewDiscountDialog.dismiss();
                showTipDialog();
            }
        });
    }*/

    private void showTipDialog(ArrayList<TipsModel> tipsModelList) {
        TipDiscountDialog.start(this, getSupportFragmentManager(), tipsModelList, mBillOfLadingPojo.getTotalCharges(this));
        MoversPreferences.getInstance(this).setTipHasBeenSubmittedOrSkipped(mJobId, true);
    }

    private void showRatingDialog() {
        RatingDialog.start(getSupportFragmentManager());
    }

    private void callSubmitRating(float rating, String message, final DialogFragment dialogFragment) {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        showProgress();

        viewModel.submitRating(subDomain, userId, opportunityId, mBolFinalChargeId, rating + "", message, mJobId, new ResponseListener<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>> response, String usedUrlKey) {
                hideProgress();
                dialogFragment.dismiss();
                callGetTipDiscountList();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(BillOfLadingActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<RatingDiscountPercentagePojo>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void callSubmitReviewDiscount(final double discount, final DialogFragment dialogFragment) {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        showProgress();

        viewModel.submitReviewDiscount(subDomain, userId, opportunityId, mBolFinalChargeId, discount + "", mJobId, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                dialogFragment.dismiss();
                mReviewDiscount = discount;
                callGetTipDiscountList();
//                showTipDialog();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(BillOfLadingActivity.this);
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

    private void callSubmitTip(final double amount, final boolean isPercentage, final DialogFragment dialogFragment, String calculatedAmount) {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String discountType = "";

        if (isPercentage) {
            discountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE + "";
        } else {
            discountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT + "";
        }

        showProgress();

        viewModel.saveBolTipsOrDiscount(subDomain, userId, opportunityId, mBolFinalChargeId, discountType, amount + "", mJobId, Util.removeFormatAmount(calculatedAmount), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                dialogFragment.dismiss();

                //Adding tip to total bill
                mIsTipDiscountPercentage = isPercentage;
                mTip = amount;

                startPaymentActivity();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(BillOfLadingActivity.this);
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

    private void callSubmitBOL(final int bolRequestStatus) {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String userId = MoversPreferences.getInstance(this).getUserId();

        BillOfLadingRequestModel billOfLadingRequestModel = new BillOfLadingRequestModel();

//        String signature = Util.getBase64EncodeStringFromBitmap(binding.signaturePad.getSignatureBitmap());

        billOfLadingRequestModel.setStatus(bolRequestStatus);
        billOfLadingRequestModel.setActualHours(mBillOfLadingPojo.getActualHours());
        billOfLadingRequestModel.setActualTravelTime(mBillOfLadingPojo.getActualTravelTime());
        billOfLadingRequestModel.setTotalChargesAmount(mBillOfLadingPojo.getTotalCharges(this));
        billOfLadingRequestModel.setMoveChargesCalculated(mBillOfLadingPojo.getMoveChargesCalculated());
        billOfLadingRequestModel.setValuationChargesCalculated(mBillOfLadingPojo.getValuationChargesCalculated());
        /*if (mBillOfLadingPojo.getValuationChargesCalculated() == null) {
            billOfLadingRequestModel.setValuationChargesCalculated(new CommonChargesRequestModel());
        }*/
        if (mBillOfLadingPojo.getJobConfirmationDetailsPojo().getBottomLineChargeDiscountType()) {
            billOfLadingRequestModel.setBottomLineDiscountType(Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT + "");
        } else {
            billOfLadingRequestModel.setBottomLineDiscountType(Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE + "");
        }
        billOfLadingRequestModel.setBottomLineDiscountValue(mBillOfLadingPojo.getJobConfirmationDetailsPojo().getBottomLineChargeDiscountValue() + "");
        billOfLadingRequestModel.setBolFinalChargeId(mBolFinalChargeId);
        billOfLadingRequestModel.setHourlyRate(mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveChargesRate() + "");
        billOfLadingRequestModel.setBottomLineCalculated(String.valueOf(mBillOfLadingPojo.calculateBottomLineDiscount(this)));
        billOfLadingRequestModel.setServiceChargeCalculated(String.valueOf(mBillOfLadingPojo.calculateServiceTax(this)));

        viewModel.submitBOLDetails(subDomain, opportunityId, userId, billOfLadingRequestModel, mJobId, new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                hideProgress();
                if (bolRequestStatus == BOL_REQUEST_STATUS_SEND_FOR_APPROVAL) {
                    MoversPreferences.getInstance(BillOfLadingActivity.this).setCurrentJobBolStatus(mJobId, Constants.BolStatus.PENDING_APPROVAL);
                    binding.setIsBolApprovalPending(true);
                    binding.btnSentForApproval.setVisibility(View.VISIBLE);
                    showSentForApprovalDialog();
                } else if (bolRequestStatus == BOL_REQUEST_STATUS_COMPLETE_BOL) {
//                    showRatingDialog();
                    showBolSignatureDialog();
                    MoversPreferences.getInstance(BillOfLadingActivity.this).setCurrentJobBolStatus(mJobId, Constants.BolStatus.COMPLETED);
                }
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(BillOfLadingActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void showBolSignatureDialog() {
        String totalAmount = Util.getGeneralFormattedDecimalString(mBillOfLadingPojo.getTotalCharges(this));
        BolSignatureDialog.start(getSupportFragmentManager(), totalAmount, binding.getObj().getJobConfirmationDetailsPojo().getTermsAndConditionPojo(), binding.getObj().getJobConfirmationDetailsPojo().getCompanyName());
    }

    private void callSubmitSignature(Bitmap signatureFile, final BolSignatureDialog bolSignatureDialog) {
        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String userId = MoversPreferences.getInstance(this).getUserId();

        showProgress();

        RawBodyBOLSignatureSubmitRequestModel submitRequestModel = new RawBodyBOLSignatureSubmitRequestModel(subdomain, opportunityId, userId, mJobId, mBolFinalChargeId, "");
        viewModel.submitBOLSignature(submitRequestModel, Util.BitmapToFile(this, signatureFile), new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel<String> response, String usedUrlKey) {
                hideProgress();
                bolSignatureDialog.dismiss();
                if (MoversPreferences.getInstance(BillOfLadingActivity.this).getTipHasBeenSubmittedOrSkipped(mJobId)) {
                    startPaymentActivity();
                } else if (mBillOfLadingPojo.getJobConfirmationDetailsPojo().getReviewMoveTypeFlag() == 1 && mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTermsAndConditionPojo().getRatingsFlag() == 1) {
                    showRatingDialog();
                } else if (mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTermsAndConditionPojo().getTipsFlag() == 1) {
                    callGetTipDiscountList();
                } else {
                    startPaymentActivity();
                }
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(BillOfLadingActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void callGetTipDiscountList() {

        if (mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTipsMoveTypeFlag() == 0) {
            startPaymentActivity();
            return;
        }

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }


        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String userId = MoversPreferences.getInstance(this).getUserId();

        showProgress();

        viewModel.getTipDiscountList(subdomain, userId, opportunityId, new ResponseListener<BaseResponseModel<ArrayList<TipsModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<TipsModel>> response, String usedUrlKey) {
                hideProgress();
                showTipDialog(response.getData());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(BillOfLadingActivity.this);
                    return;
                }

                Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<TipsModel>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void callSetBolStarted() {

        if (!shouldMakeNetworkCall(binding.getRoot())) {
            return;
        }

        showProgress();

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String bolFinalChargeId = mBillOfLadingPojo.getJobConfirmationDetailsPojo().getBolFinalChargesPojo().getId();

        viewModel.setBolStarted(subdomain, userId, opportunityId, bolFinalChargeId, mJobId, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                MoversPreferences.getInstance(BillOfLadingActivity.this).setBolStarted(true, mJobId);
                binding.setIsBolStarted(true);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();

                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(BillOfLadingActivity.this);
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


    private void showSentForApprovalDialog() {
        mSentForApprovalDialog = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(true)
//                .setView(R.layout.dialog_progress)
                .setMessage("Bill of lading has been successfully sent for approval.")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();

        mSentForApprovalDialog.show();
    }


    private void openTermsDialog(final String title, final String message) {
        final TermsAndPoliciesDialog termsAndPoliciesDialog = new TermsAndPoliciesDialog();
        termsAndPoliciesDialog.setTitleAndMessage(title, message);
        termsAndPoliciesDialog.show(getSupportFragmentManager(), "dialog");
    }

    public void openExtraStops(View view) {
        if (view.getId() == R.id.pickup_dropdown) {
            String title = getString(R.string.pickup_extra_stops);
            Intent openExtraStopsIntent = new Intent(BillOfLadingActivity.this, PickupExtraStopActivity.class);
            openExtraStopsIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mJobId);
            openExtraStopsIntent.putExtra(Constants.EXTRA_TITLE, title);
            startActivity(openExtraStopsIntent);
        } else if (view.getId() == R.id.drop_dropdown) {
            String title = getString(R.string.delivery_extra_stops);
            Intent openExtraStopsIntent = new Intent(BillOfLadingActivity.this, PickupExtraStopActivity.class);
            openExtraStopsIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, mJobId);
            openExtraStopsIntent.putExtra(Constants.EXTRA_TITLE, title);
            startActivity(openExtraStopsIntent);
        }
    }

    public void openValuationActivity(View view) {
        Intent intent = new Intent(this, ValuationChargesBolActivity.class);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID, mBolFinalChargeId);
        intent.putExtra(Constants.BOLStringConstants.VALUATION_CHARGES_ITEM, mBillOfLadingPojo.getValuationChargesCalculated());
        intent.putExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, mBillOfLadingPojo.isValuationChargeChanged());
        intent.putExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveTypeId());
        startActivityForResult(intent, VALUATION_CHARGES_REQUEST_CODE);
    }

    public void openAdditionalChargesActivity(View view) {
        /*Intent intent = new Intent(this, AdditionalChargesBolActivity.class);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID, mBolFinalChargeId);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, mBillOfLadingPojo.isAdditionalChargeChanged());
        startActivityForResult(intent, ADDITIONAL_CHARGES_REQUEST_CODE);*/

        AdditionalChargesBolActivity.startForResult(this, ADDITIONAL_CHARGES_REQUEST_CODE, mBillOfLadingPojo.isStorageRecurringChargeChanged(),
                mBolFinalChargeId, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Volume(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Weight(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Miles(),
                mBillOfLadingPojo.getTotalCalculatedMoveCharges(),
                mBillOfLadingPojo.getTotalCalculatedMoveDiscounted(this),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveTypeId(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getAdditionalChargeDiscountValue(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getAdditionalChargeDiscountType()
        );

    }

    public void openMoveChargesActivity(View view) {
        MoveChargesBolActivity.startForResult(this, MOVE_CHARGES_REQUEST_CODE, /*mBillOfLadingPojo.getMoveChargesCalculated(),*/
                mBillOfLadingPojo.isMoveChargeChanged(), mBolFinalChargeId,
                mMoveChargesPriceTypeIndex,
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Volume(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Weight(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Miles(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveTypeId(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveChargeDiscountValue(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveChargeDiscountType());
    }

    public void openPackingChargesActivity(View view) {
        Intent intent = new Intent(this, PackingChargesBolActivity.class);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID, mBolFinalChargeId);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, mBillOfLadingPojo.isPackingChargeChanged());
        intent.putExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveTypeId());
        intent.putExtra(Constants.DISCOUNT, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getPackingChargeDiscountValue());
        intent.putExtra(Constants.DISCOUNT_TYPE, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getPackingChargeDiscountType());
        startActivityForResult(intent, PACKING_CHARGES_REQUEST_CODE);
    }

    public void openStorageChargesList(View view) {
        /*Intent intent = new Intent(this, StorageChargesBolActivity.class);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID, mBolFinalChargeId);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, mBillOfLadingPojo.isStorageChargeChanged());
        startActivityForResult(intent, STORAGE_CHARGES_REQUEST_CODE);*/

        StorageChargesBolActivity.startForResult(this, STORAGE_CHARGES_REQUEST_CODE, mBillOfLadingPojo.isStorageChargeChanged(),
                mBolFinalChargeId, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Volume(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Weight(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Miles(),
                mBillOfLadingPojo.getTotalCalculatedMoveCharges(),
                mBillOfLadingPojo.getTotalCalculatedMoveDiscounted(this),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveTypeId(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageChargeDiscountValue(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageChargeDiscountType());
    }

    public void openStorageRecurringChargesList(View view) {
        /*Intent intent = new Intent(this, StorageRecurringChargesBolActivity.class);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID, mBolFinalChargeId);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, mBillOfLadingPojo.isStorageRecurringChargeChanged());
        startActivityForResult(intent, STORAGE_CHARGES_REQUEST_CODE);*/

        StorageRecurringChargesBolActivity.startForResult(this, STORAGE_RECURRING_CHARGES_REQUEST_CODE, mBillOfLadingPojo.isStorageRecurringChargeChanged(),
                mBolFinalChargeId, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Volume(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Weight(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getTemp_Miles(),
                mBillOfLadingPojo.getTotalCalculatedMoveCharges(),
                mBillOfLadingPojo.getTotalCalculatedMoveDiscounted(this),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveTypeId(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageRecurringChargeDiscountValue(),
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageRecurringChargeDiscountType()
        );

    }

    public void openCratingChargesActivity(View view) {
        Intent intent = new Intent(this, CratingChargesBolActivity.class);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_BOL_FINAL_CHARGE_ID, mBolFinalChargeId);
        intent.putExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, mBillOfLadingPojo.isCratingChargeChanged());
        intent.putExtra(Constants.BOLStringConstants.EXTRA_MOVE_TYPE_ID, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveTypeId());
        intent.putExtra(Constants.DISCOUNT, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getCratingChargeDiscountValue());
        intent.putExtra(Constants.DISCOUNT_TYPE, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getCratingChargeDiscountType());
        startActivityForResult(intent, CRATING_CHARGES_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mBillOfLadingPojo == null) {
            if (viewModel.billOfLadingPojoLive.getValue() == null) {
                return;
            } else {
                mBillOfLadingPojo = viewModel.billOfLadingPojoLive.getValue();
            }
        }

        if (requestCode == MOVE_CHARGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.e(Constants.BASE_LOG_TAG + " " + BillOfLadingActivity.class.getSimpleName(), "Intent is null");
                    return;
                }

                if (data.hasExtra(EXTRA_CALCULATED_CHARGE_ITEM)) {
                    mBillOfLadingPojo.setMoveChargesCalculated(data.getParcelableExtra(EXTRA_CALCULATED_CHARGE_ITEM));
                }

                double newMoveChargesTotal = data.getDoubleExtra(EXTRA_NEW_CHARGES_TOTAL, 0);

                /*newMoveChargesTotal = viewModel.calculateTotalWithDiscount(newMoveChargesTotal,
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveChargeDiscountType(),
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveChargeDiscountValue());*/

                mBillOfLadingPojo.setTotalCalculatedMoveCharges(newMoveChargesTotal);
                boolean moveChargesChanged = data.getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
                mBillOfLadingPojo.setMoveChargeChanged(moveChargesChanged);

                if (data.hasExtra(Constants.DISCOUNT_TYPE)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setMoveChargeDiscountType(data.getIntExtra(Constants.DISCOUNT_TYPE, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveChargeDiscountType()));
                }

                if (data.hasExtra(Constants.DISCOUNT)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setMoveChargeDiscountValue(String.valueOf(data.getDoubleExtra(Constants.DISCOUNT, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getMoveChargeDiscountValue())));
                }


                binding.setObj(mBillOfLadingPojo);
            }
        } else if (requestCode == PACKING_CHARGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.e(Constants.BASE_LOG_TAG + " " + BillOfLadingActivity.class.getSimpleName(), "Intent is null");
                    return;
                }

                double newPackingChargesTotal = data.getDoubleExtra(EXTRA_NEW_CHARGES_TOTAL, 0);
                /*newPackingChargesTotal = viewModel.calculateTotalWithDiscount(newPackingChargesTotal,
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getPackingChargeDiscountType(),
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getPackingChargeDiscountValue());*/

                mBillOfLadingPojo.getJobConfirmationDetailsPojo().setPackingMaterialCharges(newPackingChargesTotal);

                boolean packingChargesChanged = data.getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);


                if (packingChargesChanged) {
                    callSetBillOfLading();
                }

                mBillOfLadingPojo.setPackingChargeChanged(packingChargesChanged);

                if (data.hasExtra(Constants.DISCOUNT_TYPE)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setPackingChargeDiscountType(data.getIntExtra(Constants.DISCOUNT_TYPE, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getPackingChargeDiscountType()));
                }

                if (data.hasExtra(Constants.DISCOUNT)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setPackingChargeDiscountValue(String.valueOf(data.getDoubleExtra(Constants.DISCOUNT, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getPackingChargeDiscountValue())));
                }


                binding.setObj(mBillOfLadingPojo);
            }
        } else if (requestCode == STORAGE_CHARGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.e(Constants.BASE_LOG_TAG + " " + BillOfLadingActivity.class.getSimpleName(), "Intent is null");
                    return;
                }

                double newStorageChargesTotal = data.getDoubleExtra(EXTRA_NEW_CHARGES_TOTAL, 0);

                /*newStorageChargesTotal = viewModel.calculateTotalWithDiscount(newStorageChargesTotal,
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageChargeDiscountType(),
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageChargeDiscountValue());*/

                mBillOfLadingPojo.getJobConfirmationDetailsPojo().setStorageCharges(newStorageChargesTotal);

                boolean storageChargesChanged = data.getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
                mBillOfLadingPojo.setStorageChargeChanged(storageChargesChanged);

                if (data.hasExtra(Constants.DISCOUNT_TYPE)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setStorageChargeDiscountType(data.getIntExtra(Constants.DISCOUNT_TYPE, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageChargeDiscountType()));
                }

                if (data.hasExtra(Constants.DISCOUNT)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setStorageChargeDiscountValue(String.valueOf(data.getDoubleExtra(Constants.DISCOUNT, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageChargeDiscountValue())));
                }

                binding.setObj(mBillOfLadingPojo);
            }
        } else if (requestCode == ADDITIONAL_CHARGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.e(Constants.BASE_LOG_TAG + " " + BillOfLadingActivity.class.getSimpleName(), "Intent is null");
                    return;
                }
                double newAdditionalChargesTotal = data.getDoubleExtra(EXTRA_NEW_CHARGES_TOTAL, 0);

                /*newAdditionalChargesTotal = viewModel.calculateTotalWithDiscount(newAdditionalChargesTotal,
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getAdditionalChargeDiscountType(),
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getAdditionalChargeDiscountValue());*/

                mBillOfLadingPojo.getJobConfirmationDetailsPojo().setAdditionalCharges(newAdditionalChargesTotal);

                boolean additionalChargesChanged = data.getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
                mBillOfLadingPojo.setAdditionalChargeChanged(additionalChargesChanged);

                if (data.hasExtra(Constants.DISCOUNT_TYPE)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setAdditionalChargeDiscountType(data.getIntExtra(Constants.DISCOUNT_TYPE, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getAdditionalChargeDiscountType()));
                }

                if (data.hasExtra(Constants.DISCOUNT)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setAdditionalChargeDiscountValue(String.valueOf(data.getDoubleExtra(Constants.DISCOUNT, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getAdditionalChargeDiscountValue())));
                }

                binding.setObj(mBillOfLadingPojo);
            }
        } else if (requestCode == CRATING_CHARGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.e(Constants.BASE_LOG_TAG + " " + BillOfLadingActivity.class.getSimpleName(), "Intent is null");
                    return;
                }
                double newCratingChargesTotal = data.getDoubleExtra(EXTRA_NEW_CHARGES_TOTAL, 0);

                /*newCratingChargesTotal = viewModel.calculateTotalWithDiscount(newCratingChargesTotal,
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getCratingChargeDiscountType(),
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getCratingChargeDiscountValue());*/

                mBillOfLadingPojo.getJobConfirmationDetailsPojo().setCratingCharges(newCratingChargesTotal + "");

                boolean cratingChargesChanged = data.getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
                mBillOfLadingPojo.setCratingChargeChanged(cratingChargesChanged);

                if (data.hasExtra(Constants.DISCOUNT_TYPE)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setCratingChargeDiscountType(data.getIntExtra(Constants.DISCOUNT_TYPE, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getCratingChargeDiscountType()));
                }

                if (data.hasExtra(Constants.DISCOUNT)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setCratingChargeDiscountValue(String.valueOf(data.getDoubleExtra(Constants.DISCOUNT, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getCratingChargeDiscountValue())));
                }

                binding.setObj(mBillOfLadingPojo);
            }
        } else if (requestCode == STORAGE_RECURRING_CHARGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.e(Constants.BASE_LOG_TAG + " " + BillOfLadingActivity.class.getSimpleName(), "Intent is null");
                    return;
                }
                double newAdditionalChargesTotal = data.getDoubleExtra(EXTRA_NEW_CHARGES_TOTAL, 0);

                /*newAdditionalChargesTotal = viewModel.calculateTotalWithDiscount(newAdditionalChargesTotal,
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageRecurringChargeDiscountType(),
                        mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageRecurringChargeDiscountValue());*/

                mBillOfLadingPojo.getJobConfirmationDetailsPojo().setStorageChargesRecurring(newAdditionalChargesTotal + "");

                boolean additionalChargesChanged = data.getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
                mBillOfLadingPojo.setStorageRecurringChargeChanged(additionalChargesChanged);

                if (data.hasExtra(Constants.DISCOUNT_TYPE)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setStorageRecurringChargeDiscountType(data.getIntExtra(Constants.DISCOUNT_TYPE, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageRecurringChargeDiscountType()));
                }

                if (data.hasExtra(Constants.DISCOUNT)) {
                    mBillOfLadingPojo.getJobConfirmationDetailsPojo().setStorageRecurringChargeDiscountValue(String.valueOf(data.getDoubleExtra(Constants.DISCOUNT, mBillOfLadingPojo.getJobConfirmationDetailsPojo().getStorageRecurringChargeDiscountValue())));
                }

                binding.setObj(mBillOfLadingPojo);
            }
        } else if (requestCode == VALUATION_CHARGES_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.e(Constants.BASE_LOG_TAG + " " + BillOfLadingActivity.class.getSimpleName(), "Intent is null");
                    return;
                }
//                ArrayList<ValuationChargesPojo> list = data.getParcelableArrayListExtra(EXTRA_CHARGES_LIST);
                double newValuationChargesTotal = data.getDoubleExtra(EXTRA_NEW_CHARGES_TOTAL, 0);

//                mBillOfLadingPojo.setValuationChargesPojoList(list);
                mBillOfLadingPojo.setTotalCalculatedValuationCharges(newValuationChargesTotal);

                boolean valuationChargesChanged = data.getBooleanExtra(Constants.BOLStringConstants.EXTRA_CHARGES_CHANGED, false);
                mBillOfLadingPojo.setValuationChargeChanged(valuationChargesChanged);

                if (data.hasExtra(EXTRA_CALCULATED_CHARGE_ITEM)) {
                    mBillOfLadingPojo.setValuationChargesCalculated(data.getParcelableExtra(EXTRA_CALCULATED_CHARGE_ITEM));
                }

                binding.setObj(mBillOfLadingPojo);


            }
        } else if (requestCode == VALUATION_UPDATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().setValuationFlag("1");
//                callGetSubmittedValuation();
                callSetBillOfLading();
            }
        }
    }

    private void showBolStartAlert() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(false)
//                .setView(R.layout.dialog_progress)
                .setMessage("Are you sure you want to start the Bill of Lading? This will disable the clock in/clock out function for this job. You must start the Bill of Lading only when you are ready to go over the final move charges with the customer. Please select 'Yes' below to start the Bill of Lading process. ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        WalkThroughActivity.start(BillOfLadingActivity.this, mJobId);

                        if (validate()) {
                            callSetBolStarted();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.show();
    }


    private void showClockIsOnDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(false)
//                .setView(R.layout.dialog_progress)
                .setMessage(R.string.clock_is_on_message)
                .setPositiveButton("Go to clock screen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        WalkThroughActivity.start(BillOfLadingActivity.this, mJobId);
                        finish();
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.show();
    }

    private void showValuationNotSelectedDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(false)
//                .setView(R.layout.dialog_progress)
                .setMessage(R.string.valuation_not_selected_message)
                .setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        callSubmitBOL(bolRequestStatus);
                        callSetBolStarted();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.go_to_valuation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ValuationActivity.startForResult(BillOfLadingActivity.this, VALUATION_UPDATE_REQUEST_CODE);
                    }
                })
                .create();

        dialog.show();
    }

    private void setActivityResult(String message) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FAILED_TO_GET_DATA_MESSAGE, message);
        setResult(RESULT_FAILED_TO_GET_DATA, intent);
    }

    @Override
    public void onSignatureSubmitted(Bitmap signatureFile, BolSignatureDialog bolSignatureDialog) {
        callSubmitSignature(signatureFile, bolSignatureDialog);
    }

    @Override
    public void showReschedulePolicies() {
        openTermsDialog(binding.getObj().getJobConfirmationDetailsPojo().getTermsAndConditionPojo().getBolCancelTitle(), binding.getObj().getJobConfirmationDetailsPojo().getTermsAndConditionPojo().getcancellationAndReschedulePolicyBOL());
    }

    @Override
    public void showCompanyPolicies() {
        openTermsDialog(binding.getObj().getJobConfirmationDetailsPojo().getTermsAndConditionPojo().getBolCompanyTitle(), binding.getObj().getJobConfirmationDetailsPojo().getTermsAndConditionPojo().getCompanyPolicyBOL());
    }

    @Override
    public void onEditedDiscountSubmitted(EditBolDiscountDialog editBolDiscountDialog, double discount, boolean isPercentage) {
        editBolDiscountDialog.dismiss();
        mBillOfLadingPojo.getJobConfirmationDetailsPojo().setBottomLineChargeDiscountValue(discount);
        mBillOfLadingPojo.getJobConfirmationDetailsPojo().setBottomLineChargeDiscountType(isPercentage);
        binding.setObj(mBillOfLadingPojo);
    }

    @Override
    public void onRatingDialogSkipped(RatingDialog ratingDialog) {
        ratingDialog.dismiss();
        callGetTipDiscountList();
//                showTipDialog();
    }

    @Override
    public void onRatingSubmitted(float rating, String message, RatingDialog ratingDialog) {
        callSubmitRating(rating, message, ratingDialog);
    }

    @Override
    public void onTipSubmitted(TipDiscountDialog tipDiscountDialog, double tip, boolean isPercentage, String calculatedAmount) {
        callSubmitTip(tip, isPercentage, tipDiscountDialog, calculatedAmount);
    }

    @Override
    public void onTipSkip(TipDiscountDialog tipDiscountDialog) {
        tipDiscountDialog.dismiss();
        startPaymentActivity();
    }


    @Override
    public void onBackPressed() {
        if (mActivityCalledFromNotification) {
            Intent intent = new Intent(BillOfLadingActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if (mSentForApprovalDialog != null) {
            mSentForApprovalDialog.dismiss();
        }
        super.onStop();
    }

    private void startPaymentActivity() {
        String cardConvenienceFee = mBillOfLadingPojo.getJobConfirmationDetailsPojo().getCreditCardConvinienceFeeValue() + "";
        boolean isCardConvenienceFeePercentage = !mBillOfLadingPojo.getJobConfirmationDetailsPojo().getCreditCardConvinienceFeeType();
        PaymentActivity.start(BillOfLadingActivity.this,
                mBillOfLadingPojo.getTotalCharges(this),
                mReviewDiscount, mIsTipDiscountPercentage, mTip,
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getDepositeAmount(), mBolFinalChargeId,
                Util.getDoubleFromString(cardConvenienceFee), isCardConvenienceFeePercentage,
                mBillOfLadingPojo.getJobConfirmationDetailsPojo().getOpportunityName());
    }

}

