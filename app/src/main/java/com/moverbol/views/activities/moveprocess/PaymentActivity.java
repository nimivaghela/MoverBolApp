package com.moverbol.views.activities.moveprocess;

import static com.moverbol.constants.Constants.PAYMENT_RESULT;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.adapters.CarryForwardDetailsAdapter;
import com.moverbol.adapters.CustomSpinnerBolAdapter;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.custom.dialogs.EditConvenienceFeeDialog;
import com.moverbol.custom.dialogs.EditTipDialog;
import com.moverbol.custom.dialogs.PaymentDialog;
import com.moverbol.custom.dialogs.PaymentSignDialog;
import com.moverbol.custom.dialogs.SelectCameraOrGalleryDialog;
import com.moverbol.custom.dialogs.SendBOLCustomerDialog;
import com.moverbol.custom.dialogs.WebViewDialog;
import com.moverbol.databinding.PaymentBinding;
import com.moverbol.model.CarryForwardModel;
import com.moverbol.model.billOfLading.payment.PaymentHistoryDetailsModel;
import com.moverbol.model.billOfLading.payment.PaymentHistoryResponseModel;
import com.moverbol.model.billOfLading.payment.PaymentMethodsModel;
import com.moverbol.model.billOfLading.payment.SpreedlyInfoModel;
import com.moverbol.model.billOfLading.requestCharges.TipDetailsPojo;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MediaCallback;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.PermissionHelper;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.moveProcess.PaymentViewModel;
import com.moverbol.views.activities.cardreaderpayment.BluetoothConnectionActivity;

import net.authorize.aim.emv.Result;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;


/**
 * Created by Admin on 09-10-2017.
 */

public class PaymentActivity extends BaseAppCompactActivity implements PaymentSignDialog.OnSubmitListener, EditConvenienceFeeDialog.OnSubmitListener, EditTipDialog.OnSubmitListener, PaymentDialog.OnSubmitListener, SelectCameraOrGalleryDialog.OnSelectionListener, SendBOLCustomerDialog.OnSendBOLCustomerClickListener {
    public static final String EXTRA_TOTAL_BILL_AMOUNT = "extra_total_bill_amount";
    private static final int CARD_READER_UPDATE = 100;
    private static final String EXTRA_REVIEW_DISCOUNT = "extra_review_discount";
    private static final String EXTRA_IS_TIP_PERCENTAGE = "extra_is_tip_percentage";
    private static final String EXTRA_TIP = "extra_tip";
    private static final String EXTRA_DEPOSIT_AMOUNT = "extra_deposite_amount";
    private static final String EXTRA_BOL_FINAL_CHARGE_ID = "extra_bol_final_charge_id";
    private static final String PAYMENT_TYPE_NAME_CASH = "Cash";
    private static final String PAYMENT_TYPE_NAME_CARD = "Card";
    private static final String EXTRA_CARD_CONVENIENCE_FEE = "card_convenience_fee";
    private static final String EXTRA_IS_CONVENIENCE_FEE_PERCENTAGE = "card_convenience_fee_percentage";
    private static final String EXTRA_OPPORTUNITY_NAME = "extra_opportunity_name";
    private static final String RADIO_CREDIT_CHECKED_KEY = "radio_credit_selected";
    private static final String RADIO_CASH_CHECKED_KEY = "radio_cash_selected";
    private static final String EXTRA_IS_FROM_HOME = "extra_is_from_home";
    private static final String PAYMENT_HISTORY_DETAILS_MODEL_LIST_KEY = "payment_history_details_model_list";
    private static final String CARD_CONVENIENCE_FEE_KEY = "card_convenience_fee_key";
    private static final String SHOULD_SHOW_PAYMENT_SUCCESSFULL_DIALOG = "should_show_payment_successfull_dialog";
    private static final String SELECTED_PAYMENT_METHOD_POSITION_KEY = "selected_payment_method_position_key";
    private static final String AMOUNT_TO_PAY_KEY = "amount_to_pay_key";
    private static final String IS_CARD_CONVENIENCE_FEE_PERCENTAGE_KEY = "is_card_convenience_percentage_fee_key";
    //    private PaymentNewBinding paymentBinding;
    private final String spreedlyFileUrl = "file:///android_asset/spreedly_page.html";
    String mOpportunityName;
    private PaymentBinding paymentBinding;
    private WebViewDialog mWebViewDialog;
    private double totalCharges;
    private double reviewDiscount;
    private boolean isTipPercentage;
    private double subtotalOne;
    private double tip;
    private double subtotalTwo;
    private double depositeAmount;
    private double balanceDue;
    private double amountToPay;
    private double originalBalanceDue;
    private double totalPaidAmount;
    private double lastPaidAmount;
    //    private long totalAmountToPay;
    private double totalAmountToPay;
    private double cardConvinienceFee;
    private double totalCardConvinienceFee;
    private double grandTotal;
    private int paymentMadeTimes;
    private String convenienceFeeString;
    private String currencySymbol = "";
    private boolean isCardConvinienceFeePercentage;
    private PermissionHelper permissionHelper;

    private PaymentViewModel viewModel;
    private CustomSpinnerBolAdapter<PaymentMethodsModel> spinnerAdapter;
    private ArrayList<PaymentHistoryDetailsModel> mPaymentHistoryDetailsModelList;
    //    private ArrayList<PaymentHistoryResponseModel> mPaymentHistoryDetailsModelList;
    private String mSpreedlyEnvironmentKey;
    private String mSpreedlyToken;

    private String mBolfinalChargeId;
    private File mAdditionalImage;
    private Bitmap mPaymentSignature;

    private MenuItem mSkipPaymentMenuItem;
    private AlertDialog mAlertDialog;
    private MediaCallback mMediaCallback;

    private boolean isFromHome = false;
    private boolean mShouldShowPaymentSuccessFullDialog = false;
    private boolean mIsPayingThroughtSreedly = false;
    private PaymentMethodsModel selectedPaymentMethod;
    // private MenuItem carryForwardMenu;
    private double amountDue;
    private CarryForwardDetailsAdapter adapter;
    private Uri imageUri;
    private PaymentDialog paymentDialog;
    ActivityResultLauncher<Uri> takePhoto = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        if (result) {
            if (paymentDialog != null) {
                paymentDialog.showImagePreview(imageUri, false);
            }

        }
    });
    ActivityResultLauncher<String> pickImageFromGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            imageUri = uri;
            if (paymentDialog != null) {
                paymentDialog.showImagePreview(imageUri, true);
            }
        }

    });
    private SpreedlyInfoModel mSpreedlyInfoModel;

    public static void start(Context context, double totalCharges, double reviewDiscount, boolean isTipPercentage, double tip, double depositAmount, String bolFinalChargeId, double cardConvenienceFee, boolean isCardConvinienceFeePercentage, String opportunityName) {
        Intent starter = new Intent(context, PaymentActivity.class);
        starter.putExtra(EXTRA_TOTAL_BILL_AMOUNT, totalCharges);
        starter.putExtra(EXTRA_REVIEW_DISCOUNT, reviewDiscount);
        starter.putExtra(EXTRA_IS_TIP_PERCENTAGE, isTipPercentage);
        starter.putExtra(EXTRA_TIP, tip);
        starter.putExtra(EXTRA_DEPOSIT_AMOUNT, depositAmount);
        starter.putExtra(EXTRA_BOL_FINAL_CHARGE_ID, bolFinalChargeId);
        starter.putExtra(EXTRA_CARD_CONVENIENCE_FEE, cardConvenienceFee);
        starter.putExtra(EXTRA_IS_CONVENIENCE_FEE_PERCENTAGE, isCardConvinienceFeePercentage);
        starter.putExtra(EXTRA_OPPORTUNITY_NAME, opportunityName);
        context.startActivity(starter);
    }


    public static void startFromHome(Context context, double totalCharges, double depositAmount, String bolFinalChargeId, double cardConvenienceFee, boolean isCardConvinienceFeePercentage, String opportunityName) {
        Intent starter = new Intent(context, PaymentActivity.class);
        starter.putExtra(EXTRA_TOTAL_BILL_AMOUNT, totalCharges);
        starter.putExtra(EXTRA_DEPOSIT_AMOUNT, depositAmount);
        starter.putExtra(EXTRA_BOL_FINAL_CHARGE_ID, bolFinalChargeId);
        starter.putExtra(EXTRA_CARD_CONVENIENCE_FEE, cardConvenienceFee);
        starter.putExtra(EXTRA_IS_CONVENIENCE_FEE_PERCENTAGE, isCardConvinienceFeePercentage);
        starter.putExtra(EXTRA_OPPORTUNITY_NAME, opportunityName);
        starter.putExtra(EXTRA_IS_FROM_HOME, true);
        context.startActivity(starter);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialisation();
        setIntentData();
        setActionListeners();
        setViewModelObservers();



        //setValuesOnView();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PAYMENT_HISTORY_DETAILS_MODEL_LIST_KEY)) {
                mPaymentHistoryDetailsModelList = savedInstanceState.getParcelableArrayList(PAYMENT_HISTORY_DETAILS_MODEL_LIST_KEY);
            }
            if (savedInstanceState.containsKey(CARD_CONVENIENCE_FEE_KEY)) {
                cardConvinienceFee = savedInstanceState.getDouble(CARD_CONVENIENCE_FEE_KEY);
            }
            if (savedInstanceState.containsKey(IS_CARD_CONVENIENCE_FEE_PERCENTAGE_KEY)) {
                isCardConvinienceFeePercentage = savedInstanceState.getBoolean(IS_CARD_CONVENIENCE_FEE_PERCENTAGE_KEY);
            }

            /*if (savedInstanceState.containsKey(SELECTED_PAYMENT_METHOD_POSITION_KEY)) {
                int selectedPaymentMethodPosition = savedInstanceState.getInt(SELECTED_PAYMENT_METHOD_POSITION_KEY);
                paymentBinding.spinPaymentMethod.setSelection(selectedPaymentMethodPosition);
            }
            if (savedInstanceState.containsKey(AMOUNT_TO_PAY_KEY)) {
                String amountToPayString = savedInstanceState.getString(AMOUNT_TO_PAY_KEY);
                paymentBinding.edtxtAmountToPayCard.setText(amountToPayString);
            }*/

            if (savedInstanceState.containsKey(SHOULD_SHOW_PAYMENT_SUCCESSFULL_DIALOG)) {
                mShouldShowPaymentSuccessFullDialog = savedInstanceState.getBoolean(SHOULD_SHOW_PAYMENT_SUCCESSFULL_DIALOG);
                if (mShouldShowPaymentSuccessFullDialog) {
                    showPaymentSuccessFullDialog();
                }
            }
        }

    }

    private void setValuesOnView() {
        paymentBinding.setTotalCharges(totalCharges);
        paymentBinding.setAdditionalDiscount(reviewDiscount);
        paymentBinding.setSubtotalOne(totalCharges - reviewDiscount);
        paymentBinding.setTip(tip);

        double tipValue = tip;
        paymentBinding.setTipText(currencySymbol + Util.getGeneralFormattedDecimalString(tipValue));
        if (isTipPercentage) {
            tipValue = (tip * totalCharges) / 100;
            tipValue = Util.roundUpDoubleTo2DecimalPlaces(tipValue);
            paymentBinding.setTipText(tip + "% " + currencySymbol + tipValue);
        }
        subtotalTwo = totalCharges - reviewDiscount + tipValue;
        paymentBinding.setSubTotalTwo(subtotalTwo);
        paymentBinding.setDeposit(depositeAmount);


        grandTotal = totalCharges - reviewDiscount + tipValue;
        if (paymentBinding.constraintDeposit.getVisibility() == View.VISIBLE) {
            grandTotal = grandTotal - depositeAmount;
        }

        if (grandTotal < 0) {
            paymentBinding.txtBalanceAmountExcess.setText(String.format(getString(R.string.balance_amount_as_excess_payment), Util.getGeneralFormattedDecimalString(String.valueOf(Math.abs(grandTotal)))));
            paymentBinding.setIsLabelBalanceExcess(true);
            grandTotal = 0;
        }

        paymentBinding.setGrandTotal(grandTotal);

        balanceDue = Util.getRoundUpDouble(grandTotal - totalPaidAmount);
        amountToPay = Util.getRoundUpDouble(balanceDue) + Util.getRoundUpDouble(amountDue);
        originalBalanceDue = amountToPay;

        if (balanceDue <= 0) {
            balanceDue = 0;
        }

        paymentBinding.setBalanceDue(balanceDue);
        paymentBinding.setAmountToPay(amountToPay);

        if (amountToPay < 1 || mSpreedlyInfoModel.getOpportunityDetails().getPaymentsMoveTypeFlag() == 0) {
            hidePaymentViewsAndShowCompleteMove();
        }

        if (isCardConvinienceFeePercentage) {
            double convenienceFeeNum = Util.getRoundUpDouble((amountToPay * cardConvinienceFee) / 100);
            totalAmountToPay = (amountToPay + convenienceFeeNum);
            convenienceFeeString = cardConvinienceFee + "% (" + currencySymbol + convenienceFeeNum + ")";
        } else {
            totalAmountToPay = (amountToPay + cardConvinienceFee);
            convenienceFeeString = currencySymbol + cardConvinienceFee;
        }

        paymentBinding.setConvenienceFee(cardConvinienceFee);
        paymentBinding.setConvenienceFeeString(convenienceFeeString);

        paymentBinding.setTotalCardAmountToPay(totalAmountToPay);


    }

    private void setActionListeners() {
        paymentBinding.constraintPaid.setOnClickListener(v -> PaymentHistoryActivity.start(PaymentActivity.this, mPaymentHistoryDetailsModelList));

        paymentBinding.txtEdit.setOnClickListener(v -> showEditConvenienceFeeDialog());

        paymentBinding.spinPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mIsPayingThroughtSreedly = false;
                selectedPaymentMethod = (PaymentMethodsModel) parent.getAdapter().getItem(position);

                if (position == 0) {
                    if (paymentBinding.cashCardview.getVisibility() == View.VISIBLE) {
                        Util.showLog("###", "Payment position 0");
                        paymentBinding.btnPayment.setVisibility(View.GONE);
                        paymentBinding.btnComplete.setVisibility(View.GONE);
                        paymentBinding.setConvenienceFeeNeeded(false);
                    }

                } else {
                    if (parent.getAdapter().getItem(position) instanceof PaymentMethodsModel) {
                        Util.showLog("###", "Payment position 0");


                        showPaymentViewsAndHideCompleteMove();

                        Util.showLog("###", "Carry Forward logic");
                        // if payment method is carry forward show carry Forward button.
                        if (((PaymentMethodsModel) parent.getAdapter().getItem(position)).getCardFlag().equalsIgnoreCase(Constants.PaymentMethodIds.PAYMENT_CARRY_FORWARD_METHOD_ID)) {
                            paymentBinding.btnComplete.setVisibility(View.GONE);
                            paymentBinding.btnPayment.setVisibility(View.GONE);
                            paymentBinding.btnCarryForward.setVisibility(View.VISIBLE);
                            paymentBinding.edtxtAmountToPayCard.setEnabled(false);
                        } else {
                            paymentBinding.btnComplete.setVisibility(View.GONE);
                            paymentBinding.btnPayment.setVisibility(View.VISIBLE);
                            paymentBinding.btnCarryForward.setVisibility(View.GONE);
                            paymentBinding.edtxtAmountToPayCard.setEnabled(true);
                        }


                        paymentBinding.setConvenienceFeeNeeded(selectedPaymentMethod.getCardFlag().equals(Constants.PaymentMethodIds.CREDIT_CARD_METHOD_ID) || selectedPaymentMethod.getCardFlag().equals(Constants.PaymentMethodIds.DEBIT_CARD_METHOD_ID) || selectedPaymentMethod.getCardFlag().equals(Constants.PaymentMethodIds.OTHERS_METHOD_ID));
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        paymentBinding.edtxtAmountToPayCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double lastAmountToPay = Util.getDoubleFromString(s.toString());
                if (isCardConvinienceFeePercentage) {
                    double convenienceFeeNum = Util.getRoundUpDouble((lastAmountToPay * cardConvinienceFee) / 100);
                    totalAmountToPay = (lastAmountToPay + convenienceFeeNum);

                    convenienceFeeString = "%" + cardConvinienceFee + "(" + paymentBinding.getCurrencySymbol() + convenienceFeeNum + ")";

                    paymentBinding.setConvenienceFee(cardConvinienceFee);
                    paymentBinding.setConvenienceFeeString(convenienceFeeString);
                } else {
                    totalAmountToPay = (lastAmountToPay + cardConvinienceFee);
                }
                paymentBinding.setTotalCardAmountToPay(totalAmountToPay);
            }
        });

        paymentBinding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCompleteMoveDialog();

            }
        });

        paymentBinding.btnCarryForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCarryForwardPaymentDialog();
            }
        });

        paymentBinding.txtTipEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTipDialog();
            }
        });

        paymentBinding.imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentBinding.rcvCarryForward.getVisibility() == View.GONE) {
                    paymentBinding.imgExpand.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_collapse));
                    paymentBinding.rcvCarryForward.setVisibility(View.VISIBLE);
                } else {
                    paymentBinding.imgExpand.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_expand));
                    paymentBinding.rcvCarryForward.setVisibility(View.GONE);
                }
            }
        });

    }

    private void showCompleteMoveDialog() {
        String message = getString(R.string.complete_move_alert_message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme).setCancelable(true).setMessage(message).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                        logoutDueToUnauthentication(false);
                callCompleteMove();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

//        AlertDialog dialog = builder.create();
//        dialog.show();
        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void showCompletePaymentAlertDialog(double amount) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme).setCancelable(true).setMessage(getString(R.string.complete_payment_msg)).setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            callSubmitPayment(mSpreedlyToken, lastPaidAmount, isCardConvinienceFeePercentage, cardConvinienceFee, amount, mPaymentSignature, mAdditionalImage);
        }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_payment, menu);

        mSkipPaymentMenuItem = menu.findItem(R.id.skip_payment);


        /*if (balanceDue < 1) {
            mSkipPaymentMenuItem.setVisible(false);
        }*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.skip_payment) {
            showCompleteMoveDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (viewModel.paymentMethodModelListLive.getValue() == null) {
            callGetPaymentMethods();
        }

        if (viewModel.spreedlyInfoModelLive.getValue() == null) {
            callGetSpreedlySetup();
        }

        /*if (balanceDue < 1) {
            hidePaymentViewsAndShowCompleteMove();
        } else {
            showPaymentViewsAndHideCompleteMove();
        }*/
    }


    private void initialisation() {
        currencySymbol = MoversPreferences.getInstance(PaymentActivity.this).getCurrencySymbol();
        paymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        paymentBinding.setCurrencySymbol(currencySymbol);
//        paymentBinding.setPaymentActivity(this);
        setToolbar(paymentBinding.toolbar.toolbar, getString(R.string.payment), R.drawable.ic_arrow_back_white_24dp);

        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        spinnerAdapter = new CustomSpinnerBolAdapter<>(this, R.layout.layout_spinner_item, new ArrayList<PaymentMethodsModel>());
        paymentBinding.spinPaymentMethod.setAdapter(spinnerAdapter);

        mPaymentHistoryDetailsModelList = new ArrayList<>();
        adapter = new CarryForwardDetailsAdapter();
        paymentBinding.rcvCarryForward.setAdapter(adapter);
    }

    private void setIntentData() {
        if (getIntent().hasExtra(EXTRA_TOTAL_BILL_AMOUNT)) {
            totalCharges = getIntent().getDoubleExtra(EXTRA_TOTAL_BILL_AMOUNT, 0);
        }
        if (getIntent().hasExtra(EXTRA_REVIEW_DISCOUNT)) {
            reviewDiscount = getIntent().getDoubleExtra(EXTRA_REVIEW_DISCOUNT, 0);
        }
        /*if (getIntent().hasExtra(EXTRA_TIP)) {
            tip = getIntent().getDoubleExtra(EXTRA_TIP, 0);
        }*/
        /*if (getIntent().hasExtra(EXTRA_DEPOSIT_AMOUNT)) {
            //  depositeAmount = getIntent().getDoubleExtra(EXTRA_DEPOSIT_AMOUNT, 0);
        }*/
        /*if (getIntent().hasExtra(EXTRA_IS_TIP_PERCENTAGE)) {
            isTipPercentage = getIntent().getBooleanExtra(EXTRA_IS_TIP_PERCENTAGE, false);
        }*/
        if (getIntent().hasExtra(EXTRA_BOL_FINAL_CHARGE_ID)) {
            mBolfinalChargeId = getIntent().getStringExtra(EXTRA_BOL_FINAL_CHARGE_ID);
        }
        if (getIntent().hasExtra(EXTRA_CARD_CONVENIENCE_FEE)) {
            cardConvinienceFee = getIntent().getDoubleExtra(EXTRA_CARD_CONVENIENCE_FEE, 0.0);
        }
        if (getIntent().hasExtra(EXTRA_IS_CONVENIENCE_FEE_PERCENTAGE)) {
            isCardConvinienceFeePercentage = getIntent().getBooleanExtra(EXTRA_IS_CONVENIENCE_FEE_PERCENTAGE, false);
            paymentBinding.setIsConvenienceFeePercentage(isCardConvinienceFeePercentage);
        }
        if (getIntent().hasExtra(EXTRA_OPPORTUNITY_NAME)) {
            mOpportunityName = getIntent().getStringExtra(EXTRA_OPPORTUNITY_NAME);
        }

        if (getIntent().hasExtra(EXTRA_IS_FROM_HOME)) {
            isFromHome = getIntent().getBooleanExtra(EXTRA_IS_FROM_HOME, false);
        }

    }

    private void setViewModelObservers() {

        viewModel.paymentMethodModelListLive.observe(this, new Observer<List<PaymentMethodsModel>>() {
            @Override
            public void onChanged(@Nullable List<PaymentMethodsModel> paymentMethodsModels) {
                if (paymentMethodsModels != null) {
                    PaymentMethodsModel model = new PaymentMethodsModel();
                    model.setName("--Select Payment Method--");

                    //Making sure "--Select Payment Method--" is not already added here
                    if (!paymentMethodsModels.isEmpty() && paymentMethodsModels.get(0) != null && TextUtils.equals(paymentMethodsModels.get(0).getName(), "--Select Payment Method--")) {
                        paymentMethodsModels.remove(0);
                    }
                    paymentMethodsModels.add(0, model);
                    spinnerAdapter.setDataList(paymentMethodsModels);
                }
            }
        });

        viewModel.spreedlyInfoModelLive.observe(this, new Observer<SpreedlyInfoModel>() {
            @Override
            public void onChanged(@Nullable SpreedlyInfoModel spreedlyInfoModel) {

                // Clear All Data after Refresh
                totalPaidAmount = 0;
                totalCardConvinienceFee = 0;
                mPaymentHistoryDetailsModelList.clear();
                paymentBinding.spinPaymentMethod.setSelection(0);
                mSpreedlyInfoModel = spreedlyInfoModel;


                mSpreedlyEnvironmentKey = spreedlyInfoModel != null ? spreedlyInfoModel.getEnvironmentKey() : null;
                if (spreedlyInfoModel != null) {
                    paymentBinding.setAmountDue(spreedlyInfoModel.getOpportunityDetails().getAmountDue());
                    paymentBinding.setPaymentCarryForward(spreedlyInfoModel.getOpportunityDetails().getPaymentCarryForward());

                    /*   if (mPaymentHistoryDetailsModelList.isEmpty()) {*/
                    if (spreedlyInfoModel.getPaymentHistoryList() != null && !spreedlyInfoModel.getPaymentHistoryList().isEmpty()) {
                        paymentMadeTimes = spreedlyInfoModel.getPaymentHistoryList().size();
                        for (int i = 0; i < spreedlyInfoModel.getPaymentHistoryList().size(); i++) {
                            PaymentHistoryResponseModel paymentHistory = spreedlyInfoModel.getPaymentHistoryList().get(i);

                            double paidAmount = Util.getDoubleFromString(paymentHistory.getAmount());
                            double convenienceFeePaid = paidAmount - Util.getDoubleFromString(paymentHistory.getAmountWithCardConvenienceFeeNotAdded());
                            String paymentMethodName = "";

                            if (paymentHistory.getSpreedlyStatus()) {
                                paymentMethodName = "Card";
                            } else {
                                paymentMethodName = paymentHistory.getPaymentModeName();
                            }

                            long paymentTimeMillis = 0;
                            try {
                                paymentTimeMillis = Util.getDateObjectFromStringDate(paymentHistory.getPaymentTimestamp(), Constants.INPUT_DATE_FORMAT_COMMENTS).getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            addPaymentToHistory(paidAmount, convenienceFeePaid, paymentMethodName, paymentTimeMillis);
                            totalPaidAmount += (paidAmount - convenienceFeePaid);
                            totalCardConvinienceFee += convenienceFeePaid;
                        }

                    }
                   /* } else {
                        for (int i = 0; i < mPaymentHistoryDetailsModelList.size(); i++) {
                            PaymentHistoryDetailsModel paymentHistoryDetailsModel = mPaymentHistoryDetailsModelList.get(i);

                            double paidAmount = paymentHistoryDetailsModel.getPaidAmount();
                            double convinienceFeePaid = paymentHistoryDetailsModel.getCardConvinienceFee();

                            totalPaidAmount += paidAmount;
                            totalCardConvinienceFee += convinienceFeePaid;
                        }
                    }*/


                    //balanceDue -= totalPaidAmount;
                    if (spreedlyInfoModel.getOpportunityDetails() != null) {
                        totalCharges = Double.parseDouble(spreedlyInfoModel.getOpportunityDetails().getTotalcharges());
                        if (spreedlyInfoModel.getOpportunityDetails().getDeposit() != null) {
                            depositeAmount = Double.parseDouble(spreedlyInfoModel.getOpportunityDetails().getDeposit());
                            paymentBinding.setDeposit(Double.parseDouble(spreedlyInfoModel.getOpportunityDetails().getDeposit()));
                        }
                        paymentBinding.setTipsFlag(spreedlyInfoModel.getOpportunityDetails().getTipsFlag());
                    }


                    amountDue = Double.parseDouble(spreedlyInfoModel.getOpportunityDetails().getAmountDue());
                    amountToPay = Util.getRoundUpDouble(balanceDue) + Util.getRoundUpDouble(amountDue);
                    paymentBinding.setPaidAmount(totalPaidAmount);
                    paymentBinding.setAmountToPay(amountToPay);


                    String totalConvenienceFeeString = currencySymbol + Util.getGeneralFormattedDecimalString(totalCardConvinienceFee);
                    if (paymentMadeTimes == 1 && isCardConvinienceFeePercentage) {
                        totalConvenienceFeeString = cardConvinienceFee + "%(" + totalConvenienceFeeString + ")";
                    }

                    paymentBinding.setTotalConvenienceFee(totalCardConvinienceFee);
                    paymentBinding.setTotalConvenienceFeeString(totalConvenienceFeeString);


                    if (spreedlyInfoModel.getTipDetailsPojo() != null) {
                        if (spreedlyInfoModel.getOpportunityDetails().getTipsFlag() == 1) {
                            tip = spreedlyInfoModel.getTipDetailsPojo().getTipDiscountAmount();
                        } else {
                            tip = 0;
                        }

                        isTipPercentage = spreedlyInfoModel.getTipDetailsPojo().isTipPercentage();

                        //setValuesOnView();
                    }
                    setValuesOnView();

                }

                if (paymentBinding.getPaymentCarryForward() == 1) {
                    callCarryForwardList();
                }

            }
        });
    }


  /*  private void showHideCarryForwardMenu(OpportunityDetails opportunityDetails) {
        carryForwardMenu.setVisible(opportunityDetails.getMultidateFlag() == 1 && opportunityDetails.getLastMultiDateJob() == 0);
    }*/

    private void callGetPaymentMethods() {
        if (!shouldMakeNetworkCall(paymentBinding.getRoot())) {
            return;
        }

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        showProgress();
        viewModel.getPaymentMethods(subdomain, userId, opportunityId, new ResponseListener<BaseResponseModel<List<PaymentMethodsModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<List<PaymentMethodsModel>> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PaymentActivity.this);
                    return;
                }
                Snackbar.make(paymentBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<List<PaymentMethodsModel>>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(paymentBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    public void onPayNowClicked(final View view) {
        lastPaidAmount = Util.getRoundUpDouble(Util.getDoubleFromString(paymentBinding.edtxtAmountToPayCard.getText().toString()));

     /*   if (lastPaidAmount > amountToPay) {
            paymentBinding.edtxtAmountToPayCard.setError("Cannot be greater than balance due");
            return;
        } else */
        if (lastPaidAmount < Util.getRoundUpDouble(amountToPay)) {

            if (lastPaidAmount == 0) {
                paymentBinding.edtxtAmountToPayCard.requestFocus();
                paymentBinding.edtxtAmountToPayCard.setError("Should be greater than 0");
                return;
            }

            showPaidAmountIsLessAlert((dialog, which) -> {
                dialog.dismiss();
                paymentFlowStart();

            }, (dialog, which) -> dialog.dismiss());
        } else {
            paymentFlowStart();
        }
    }


    private void paymentFlowStart() {
        if (selectedPaymentMethod.getCardFlag().equals(Constants.PaymentMethodIds.CARD_READER_METHOD_ID)) {
            Intent intent = new Intent(this, BluetoothConnectionActivity.class);
            intent.putExtra(EXTRA_TOTAL_BILL_AMOUNT, String.valueOf(lastPaidAmount));
            startActivityForResult(intent, CARD_READER_UPDATE);
        } else if (selectedPaymentMethod.getCardFlag().equals(Constants.PaymentMethodIds.CREDIT_CARD_METHOD_ID) || selectedPaymentMethod.getCardFlag().equals(Constants.PaymentMethodIds.DEBIT_CARD_METHOD_ID)) {
            mIsPayingThroughtSreedly = true;
            showSpreedlyDialog();
        } else {
            showPaymentDialog(false);
        }
    }


    private void showSpreedlyDialog() {
        mWebViewDialog = new WebViewDialog();
        if (shouldMakeNetworkCall(paymentBinding.getRoot())) {
            mWebViewDialog.showWithActionListener(getSupportFragmentManager(), spreedlyFileUrl, true, new WebViewDialog.WebAppInterface() {
                @Override
                public String getEnvironmentKey() {
                    return mSpreedlyEnvironmentKey;
                }

                @Override
                public String getCompanyName() {
                    return mOpportunityName;
                }

                @Override
                public String getPayableAmount() {
                    String currencySymbol = MoversPreferences.getInstance(PaymentActivity.this).getCurrencySymbol();
                    return currencySymbol + Util.getGeneralFormattedDecimalString(totalAmountToPay);
//                    return currencySymbol + Util.getGeneralFormattedDecimalString(5.55 + convenienceFeeNum);
                }

                @Override
                public void onDialogClosed() {
//                        Toast.makeText(PaymentActivity.this, "Dialog Closed called", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPaymentTokenGenerated(String fullName, String month, String year, String token) {
                    Log.d("Spreedly Token", token);

                    mSpreedlyToken = token;

                    showPaymentDialog(true);

                  /*  double convenienceFeeNum;

                    if (isCardConvinienceFeePercentage) {
                        convenienceFeeNum = ((lastPaidAmount * cardConvinienceFee) / 100);
                    } else {
                        convenienceFeeNum = cardConvinienceFee;
                    }

                    //totalPaidAmount += lastPaidAmount + convenienceFeeNum;*/
                }
            });
        }
    }

    private void showPaymentDialog(final boolean shouldShowCustomerId) {
        paymentDialog = PaymentDialog.start(getSupportFragmentManager(), shouldShowCustomerId);
    }

    private void addPaymentToHistory(double lastPaidAmount, double cardConvinienceFee, String paymentTypeName, long timeInMillis) {
        PaymentHistoryDetailsModel paymentHistoryDetailsModel = new PaymentHistoryDetailsModel();
        paymentHistoryDetailsModel.setPaidAmount(lastPaidAmount);
        paymentHistoryDetailsModel.setPaymentMethod(paymentTypeName);
        paymentHistoryDetailsModel.setCardConvinienceFee(cardConvinienceFee);
        paymentHistoryDetailsModel.setTimeStamp(Util.getFormattedTimeFromMillis(timeInMillis, Constants.OUTPUT_DATE_FORMAT_TIMINGS_DETAILS));
        mPaymentHistoryDetailsModelList.add(paymentHistoryDetailsModel);
    }

    private void showPaymentSignDialog() {
        /*PaymentSignDialog.startWithActionListener(getSupportFragmentManager(), new PaymentSignDialog.OnSubmitListener() {
            @Override
            public void OnSignatureSubmit(PaymentSignDialog dialog, Bitmap signatureBitmap) {
                dialog.dismiss();
                mPaymentSignature = Util.getBase64EncodeStringFromBitmap(signatureBitmap);
                callSubmitPayment(mSpreedlyToken, lastPaidAmount, isCardConvinienceFeePercentage, cardConvinienceFee, totalAmountToPay, true, mPaymentSignature, mAdditionalImage);
            }
        });*/

        PaymentSignDialog.start(getSupportFragmentManager());
    }


    private void showUploadImageDialogue() {
        SelectCameraOrGalleryDialog dialog = new SelectCameraOrGalleryDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CARD_READER_UPDATE) {
            Result result = (Result) data.getSerializableExtra(PAYMENT_RESULT);
            if (result != null) {
                if (result.getSignatureBase64() != null) {
                    byte[] decodedString = Base64.decode(result.getSignatureBase64().getBytes(), Base64.DEFAULT);
                    mPaymentSignature = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }

                callSubmitPayment(result.getTransId(), lastPaidAmount, isCardConvinienceFeePercentage, cardConvinienceFee, Double.parseDouble(Objects.requireNonNull(result).getAuthorizedAmount()), mPaymentSignature, mAdditionalImage);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void showPaidAmountIsLessAlert(DialogInterface.OnClickListener positiveButtonClickListener, DialogInterface.OnClickListener negativeButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme).setCancelable(false).setMessage(R.string.amount_less_than_balance_due_alert).setPositiveButton(R.string.yes, positiveButtonClickListener).setNegativeButton(getString(R.string.no_), negativeButtonClickListener);

        /*AlertDialog dialog = builder.create();
        dialog.show();*/

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    public void callGetSpreedlySetup() {
        if (!shouldMakeNetworkCall(paymentBinding.getRoot())) {
            return;
        }

        showProgress();

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();

        /*boolean isCallingForMappdevUrl = false; FIXME

        if (BuildConfig.DEBUG) {
            isCallingForMappdevUrl = RetrofitClient.changeableBaseUrl.contains("mappdev");
        } else {
            isCallingForMappdevUrl = BuildConfig.BASE_URL.contains("mappdev");
        }

        if(!isCallingForMappdevUrl){
            jobId = null;
        }*/

        viewModel.getSpreedlySetup(subdomain, userId, jobId, opportunityId, new ResponseListener<BaseResponseModel<SpreedlyInfoModel>>() {
            @Override
            public void onResponse(BaseResponseModel<SpreedlyInfoModel> response, String usedUrlKey) {
                hideProgress();
             /*   try {
                    totalCharges = Double.parseDouble(response.getData().getOpportunityDetails().getTotalcharges());
                    paymentBinding.setTotalCharges(Double.parseDouble(response.getData().getOpportunityDetails().getTotalcharges()));
                }catch (Exception e){
                    Util.showLog(getClass().getSimpleName(),"convert TotalCharge Double");
                }*/

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PaymentActivity.this);
                    return;
                }
                Snackbar.make(paymentBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<SpreedlyInfoModel>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(paymentBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void callSubmitPayment(String paymentToken, final double amountWithCardConvenienceFeeNotAdded, final boolean isCardConvenienceFeePercentage, final double cardConvenienceFee, final double amount, Bitmap paymentSignature, File additionalImage) {
        if (!shouldMakeNetworkCall(paymentBinding.getRoot())) {
            return;
        }

        if (paymentToken == null) {
            paymentToken = "";
        }

        /*if (paymentSignature == null) {
            paymentSignature = "";
        }

        if (additionalImage == null) {
            additionalImage = "";
        }*/

        String paymentMethod = ((PaymentMethodsModel) paymentBinding.spinPaymentMethod.getSelectedItem()).getId();
        String paymentType = Constants.PaymentTypeIndexs.PAYMENT_TYPE_SPREEDLY;

        if (!mIsPayingThroughtSreedly) {
            paymentType = Constants.PaymentTypeIndexs.PAYMENT_TYPE_CASH_CHECK_OTHERS;
        }

        showProgress();

        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        final String paymentTypeFinal = paymentType;

        final String toPay = Util.convertTwoDigit(amount);
        String totalAmountToPay = Util.convertTwoDigit(originalBalanceDue);


        // Set Card ConvenienceFee Type
        String cardConvenienceFeeType;
        if (isCardConvenienceFeePercentage) {
            cardConvenienceFeeType = Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE + "";
        } else {
            cardConvenienceFeeType = Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT + "";
        }

        String finalCardConvenienceFee;
        if (paymentBinding.getConvenienceFeeNeeded()) {
            finalCardConvenienceFee = String.valueOf(cardConvenienceFee);
        } else {
            finalCardConvenienceFee = "0";
            cardConvenienceFeeType = "0";
        }


        viewModel.submitPayment(subdomain, userId, opportunityId, jobId, mBolfinalChargeId, paymentToken, amountWithCardConvenienceFeeNotAdded + "", cardConvenienceFeeType, finalCardConvenienceFee, toPay, totalAmountToPay, paymentTypeFinal, paymentMethod, "", "", String.valueOf(depositeAmount), Util.BitmapToFile(this, paymentSignature), additionalImage, new ResponseListener<BaseResponseModel<String>>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                if (response.getData().equals("1")) {
                    showPaymentSuccessAlertDialog(getString(R.string.payment_success_and_job_completed_msg), true);
                } else {
                    showPaymentSuccessAlertDialog(getString(R.string.payment_success_msg), false);
                }
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PaymentActivity.this);
                    return;
                }
                showPaymentAlertDialog(serverResponseError.getMessage());
            }

            @Override
            public void onFailure(Call<BaseResponseModel<String>> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(paymentBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void hidePaymentViewsAndShowCompleteMove() {
        Util.showLog("###", "HidePaymentAndShowCompleteMove");
        paymentBinding.cashCardview.setVisibility(View.GONE);
        paymentBinding.btnPayment.setVisibility(View.GONE);
        paymentBinding.btnComplete.setVisibility(View.VISIBLE);
        paymentBinding.txtChoosePaymentMethod.setVisibility(View.GONE);
        if (mSkipPaymentMenuItem != null) mSkipPaymentMenuItem.setVisible(false);
    }

    private void showPaymentViewsAndHideCompleteMove() {
        Util.showLog("###", "showPaymentViewsAndHideCompleteMove");
        paymentBinding.cashCardview.setVisibility(View.VISIBLE);
//        paymentBinding.creditcardCardview.setVisibility(View.VISIBLE);
        paymentBinding.btnPayment.setVisibility(View.VISIBLE);
        paymentBinding.btnComplete.setVisibility(View.GONE);
        paymentBinding.txtChoosePaymentMethod.setVisibility(View.VISIBLE);
        if (mSkipPaymentMenuItem != null) mSkipPaymentMenuItem.setVisible(true);

    }


    public void callCompleteMove() {
        if (!shouldMakeNetworkCall(paymentBinding.getRoot())) {
            return;
        }

        showProgress();
        String subdomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();

        viewModel.completeMove(subdomain, userId, opportunityId, jobId, Util.removeFormatAmount(String.valueOf(paymentBinding.getSubTotalTwo())), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                showPaymentSuccessFullDialog();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PaymentActivity.this);
                    return;
                }
                Snackbar.make(paymentBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(paymentBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });

    }


    private void showPaymentSuccessFullDialog() {

        mShouldShowPaymentSuccessFullDialog = true;

        String message = getString(R.string.payment_done_message);
        /*String message = "Done. This Job will be deleted.";
        if(isCardPayment){
            message = "Payment Successful. This Job will be removed from the deleted.";
        }*/

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme).setCancelable(false).setMessage(message).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                        logoutDueToUnauthentication(false);
                Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        /*AlertDialog dialog = builder.create();
        dialog.show();*/

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void showEditConvenienceFeeDialog() {
        EditConvenienceFeeDialog.show(getSupportFragmentManager(), isCardConvinienceFeePercentage, cardConvinienceFee/*, new EditConvenienceFeeDialog.OnSubmitListener() {
                    @Override
                    public void onConvenienceFeeSubmit(double feeValue, boolean isPercentage, EditConvenienceFeeDialog dialog) {

                        dialog.dismiss();

                        callSetCardConvenienceFee(feeValue, isPercentage, dialog);
                    }
                }*/);
    }


    private void showEditTipDialog() {
        EditTipDialog.show(getSupportFragmentManager(), isTipPercentage, tip/*, new EditTipDialog.OnSubmitListener() {
                    @Override
                    public void onTipSubmit(double feeValue, boolean isPercentage, EditTipDialog dialog) {

                        dialog.dismiss();

                        *//*tip = feeValue;
                        isTipPercentage = isPercentage;*//*

                        callSubmitTip(feeValue, isPercentage, dialog);

                    }
                }*/);
    }


    private void callSubmitTip(final double amount, final boolean isPercentage, final DialogFragment dialogFragment) {
        if (!shouldMakeNetworkCall(paymentBinding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String discountType = "";

        double calculatedAmount = amount;
        if (isPercentage) {
            calculatedAmount = (calculatedAmount * totalCharges) / 100;
            calculatedAmount = Util.roundUpDoubleTo2DecimalPlaces(calculatedAmount);
        }

        if (isPercentage) {
            discountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE + "";
        } else {
            discountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT + "";
        }

        showProgress();

        viewModel.saveBolTipsOrDiscount(subDomain, userId, opportunityId, mBolfinalChargeId, discountType, amount + "", jobId, String.valueOf(calculatedAmount), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                dialogFragment.dismiss();

                isTipPercentage = isPercentage;
                tip = amount;

                setValuesOnView();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PaymentActivity.this);
                    return;
                }
                Snackbar.make(paymentBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(paymentBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void callSetCardConvenienceFee(final double amount, final boolean isPercentage, final DialogFragment dialogFragment) {
        if (!shouldMakeNetworkCall(paymentBinding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        String userId = MoversPreferences.getInstance(this).getUserId();
        String jobId = MoversPreferences.getInstance(this).getCurrentJobId();
        String opportunityId = MoversPreferences.getInstance(this).getOpportunityId();
        String discountType = "";

        if (isPercentage) {
            discountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_PERCENTAGE + "";
        } else {
            discountType = Constants.BolDiscountFlags.DISCOUNT_FLAG_CURENCY_AMOUNT + "";
        }

        showProgress();

        viewModel.setCardConvenienceFee(subDomain, userId, opportunityId, mBolfinalChargeId, discountType, amount + "", jobId, new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                dialogFragment.dismiss();

                cardConvinienceFee = amount;
                isCardConvinienceFeePercentage = isPercentage;

                double lastAmountToPay = Util.getDoubleFromString(paymentBinding.edtxtAmountToPayCard.getText().toString());
                if (isCardConvinienceFeePercentage) {
                    double convenienceFeeNum = Util.getRoundUpDouble(((lastAmountToPay * cardConvinienceFee) / 100));
                    totalAmountToPay = (lastAmountToPay + convenienceFeeNum);
                    convenienceFeeString = cardConvinienceFee + "% (" + paymentBinding.getCurrencySymbol() + convenienceFeeNum + ")";
                } else {
                    totalAmountToPay = (lastAmountToPay + cardConvinienceFee);
                    convenienceFeeString = paymentBinding.getCurrencySymbol() + cardConvinienceFee;
                }
                paymentBinding.setConvenienceFee(cardConvinienceFee);
                paymentBinding.setConvenienceFeeString(convenienceFeeString);
                paymentBinding.setTotalCardAmountToPay(totalAmountToPay);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PaymentActivity.this);
                    return;
                }
                Snackbar.make(paymentBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(paymentBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (paymentBinding.getPaidAmount() > 0 && !isFromHome) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (viewModel.spreedlyInfoModelLive.getValue() != null) {
            if (viewModel.spreedlyInfoModelLive.getValue().getTipDetailsPojo() == null) {
                viewModel.spreedlyInfoModelLive.getValue().setTipDetailsPojo(new TipDetailsPojo());
            }

            viewModel.spreedlyInfoModelLive.getValue().getTipDetailsPojo().setTipDiscountAmount(tip + "");
            viewModel.spreedlyInfoModelLive.getValue().getTipDetailsPojo().setIsTipPercentage(isTipPercentage);
        }


        outState.putParcelableArrayList(PAYMENT_HISTORY_DETAILS_MODEL_LIST_KEY, mPaymentHistoryDetailsModelList);
        outState.putDouble(CARD_CONVENIENCE_FEE_KEY, cardConvinienceFee);
        outState.putBoolean(IS_CARD_CONVENIENCE_FEE_PERCENTAGE_KEY, isCardConvinienceFeePercentage);
        outState.putBoolean(SHOULD_SHOW_PAYMENT_SUCCESSFULL_DIALOG, mShouldShowPaymentSuccessFullDialog);

        /*outState.putInt(SELECTED_PAYMENT_METHOD_POSITION_KEY, paymentBinding.spinPaymentMethod.getSelectedItemPosition());
        outState.putString(AMOUNT_TO_PAY_KEY, paymentBinding.edtxtAmountToPayCard.getText().toString());*/

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.cancel();
        }

        //To handle a crash on Permission we have commented this
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void OnSignatureSubmit(PaymentSignDialog dialog, Bitmap signatureBitmap) {
        dialog.dismiss();
        mPaymentSignature = signatureBitmap;
        showCompletePaymentAlertDialog(totalAmountToPay);

    }


    @Override
    public void onConvenienceFeeSubmit(double feeValue, boolean isPercentage, EditConvenienceFeeDialog dialog) {

        dialog.dismiss();

        callSetCardConvenienceFee(feeValue, isPercentage, dialog);
    }


    @Override
    public void onTipSubmit(double feeValue, boolean isPercentage, EditTipDialog dialog) {

        dialog.dismiss();

                        /*tip = feeValue;
                        isTipPercentage = isPercentage;*/

        callSubmitTip(feeValue, isPercentage, dialog);

    }


    @Override
    public void onPaymentDialogRadioButtonClicked(PaymentDialog dialog) {
        showUploadImageDialogue();
    }


    @Override
    public void onPaymentDialogSubmitted(PaymentDialog dialog, File imageBase64String) {
        mAdditionalImage = imageBase64String;
        dialog.dismiss();
        if (mIsPayingThroughtSreedly) {
            showPaymentSignDialog();
        } else {
            double amount = lastPaidAmount;
            if (paymentBinding.getConvenienceFeeNeeded()) {
                amount = totalAmountToPay;
//                callSubmitPayment(mSpreedlyToken, lastPaidAmount, isCardConvinienceFeePercentage, cardConvinienceFee, totalAmountToPay, mIsPayingThroughtSreedly, mPaymentSignature, mAdditionalImage);
            } /*else {
                callSubmitPayment(mSpreedlyToken, lastPaidAmount, isCardConvinienceFeePercentage, cardConvinienceFee, lastPaidAmount, mIsPayingThroughtSreedly, mPaymentSignature, mAdditionalImage);
            }*/

            showCompletePaymentAlertDialog(amount);
        }
    }

    @Override
    public void onPaymentDialogSkip(PaymentDialog dialog) {
        dialog.dismiss();
        if (mIsPayingThroughtSreedly) {
            showPaymentSignDialog();
        } else {
            double amount = lastPaidAmount;
            if (paymentBinding.getConvenienceFeeNeeded()) {
                amount = totalAmountToPay;
//                callSubmitPayment(mSpreedlyToken, lastPaidAmount, isCardConvinienceFeePercentage, cardConvinienceFee, totalAmountToPay, mIsPayingThroughtSreedly, mPaymentSignature, mAdditionalImage);
            } /*else {
                callSubmitPayment(mSpreedlyToken, lastPaidAmount, isCardConvinienceFeePercentage, cardConvinienceFee, lastPaidAmount, mIsPayingThroughtSreedly, mPaymentSignature, mAdditionalImage);
            }*/

            showCompletePaymentAlertDialog(amount);


        }
    }


    @Override
    public void onCameraSelected(SelectCameraOrGalleryDialog dialog) {
        takePictureFromCamera();
        dialog.dismiss();
    }

    private void takePictureFromCamera() {
        try {
            imageUri = Util.createImageUri(this);
            takePhoto.launch(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGallerySelected(SelectCameraOrGalleryDialog dialog) {
        uploadImageFromGallery();
        dialog.dismiss();
    }

    private void uploadImageFromGallery() {
        pickImageFromGallery.launch("image/*");
    }


    private void showPaymentAlertDialog(String message) {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(this).setMessage(message).setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss()).create();
        alertDialog.show();
    }

    private void showPaymentSuccessAlertDialog(String message, boolean jobCompleted) {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(this).setMessage(message).setPositiveButton(R.string.complete_job, (dialog, which) -> {
            dialog.dismiss();
            if (jobCompleted) {
                callCompleteMove();
            } else {
                callGetSpreedlySetup();
            }


        });

        if (jobCompleted) {
            alertDialog.setNegativeButton(R.string.send_bol_to_customer, ((dialog, which) -> {
                //downloadPDF("https://test1.movegistics.com/api/texting/download-boldoc");
                showEmailToBOLDialog();
            }));
        }


        alertDialog.create().show();
    }

    private void showEmailToBOLDialog() {
        SendBOLCustomerDialog.start(getSupportFragmentManager());
    }


    private void showCarryForwardPaymentDialog() {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(this).setTitle(getString(R.string.carry_forward)).setMessage(getString(R.string.carry_forward_payment_alert)).setPositiveButton(R.string.yes, (dialog, which) -> {
            dialog.dismiss();
            callCarryForwardPayment();
        }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss()).create();
        alertDialog.show();
    }


    private void callCarryForwardPayment() {
        if (!shouldMakeNetworkCall(paymentBinding.getRoot())) {
            return;
        }

        String subDomain = MoversPreferences.getInstance(this).getSubdomain();
        showProgress();

        viewModel.carryForwardPayment(subDomain, mBolfinalChargeId, 1, Util.convertTwoDigit(amountToPay), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getCurrentJobId(), new ResponseListener<BaseResponseModel>() {
            @Override
            public void onResponse(BaseResponseModel response, String usedUrlKey) {
                hideProgress();
                callCompleteMove();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                    Util.showLoginErrorDialog(PaymentActivity.this);
                    return;
                }
                Snackbar.make(paymentBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                hideProgress();
                Snackbar.make(paymentBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    private void callCarryForwardList() {
        showProgress();
        viewModel.callCarryForwardList(MoversPreferences.getInstance(this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<ArrayList<CarryForwardModel>>>() {
            @Override
            public void onResponse(BaseResponseModel<ArrayList<CarryForwardModel>> response, String usedUrlKey) {
                hideProgress();
                adapter.updateList(response.getData());
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
                Util.showSnackBar(paymentBinding.getRoot(), serverResponseError.getMessage());
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ArrayList<CarryForwardModel>>> call, Throwable t, String message) {
                hideProgress();
                Util.showSnackBar(paymentBinding.getRoot(), message);
            }
        });
    }


    private void downloadPDF(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading PDF");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName + ".pdf");

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        long downloadId = downloadManager.enqueue(request);
    }

    @Override
    public void onSendBOLCustomerClicked(String emailAddress) {
        showProgress();
        viewModel.sendBOLToCustomer(MoversPreferences.getInstance(this).getSubdomain(),
                MoversPreferences.getInstance(this).getCurrentJobId(),
                MoversPreferences.getInstance(this).getOpportunityId(),
                emailAddress,
                new ResponseListener<BaseResponseModel>() {
                    @Override
                    public void onResponse(BaseResponseModel response, String usedUrlKey) {
                        hideProgress();
                        callCompleteMove();
                    }
                    @Override
                    public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                        hideProgress();
                        if (serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))) {
                            Util.showLoginErrorDialog(PaymentActivity.this);
                            return;
                        }
                        Snackbar.make(paymentBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                        hideProgress();
                        Snackbar.make(paymentBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                    }
                });
    }
}