package com.moverbol.views.activities.cardreaderpayment;

import static com.moverbol.constants.Constants.PAYMENT_RESULT;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.moverbol.R;
import com.moverbol.custom.App;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ActivityBluetoothConnectionBinding;
import com.moverbol.model.CardReaderModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.PermissionHelper;
import com.moverbol.util.Util;
import com.moverbol.views.activities.moveprocess.PaymentActivity;
import com.moverbol.views.dialogs.PaymentReceiptDialog;
import com.moverbol.views.dialogs.ScanDeviceDialog;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.ResponseReasonCode;
import net.authorize.aim.emv.EMVDeviceConnectionType;
import net.authorize.aim.emv.EMVErrorCode;
import net.authorize.aim.emv.EMVTransaction;
import net.authorize.aim.emv.EMVTransactionManager;
import net.authorize.aim.emv.EMVTransactionType;
import net.authorize.aim.emv.Result;
import net.authorize.auth.PasswordAuthentication;
import net.authorize.auth.SessionTokenAuthentication;
import net.authorize.data.Order;
import net.authorize.data.OrderItem;
import net.authorize.data.mobile.MobileDevice;
import net.authorize.mobile.Transaction;
import net.authorize.mobile.TransactionType;
import net.authorize.util.StringUtils;
import net.authorize.xml.MessageType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;

public class BluetoothConnectionActivity extends BaseAppCompactActivity {

    private ActivityBluetoothConnectionBinding mBinding;
    private AlertDialog alertDialog = null;
    private PermissionHelper permissionHelper;
    private CardReaderVM cardReaderVM;


    private void requestLocationPermission() {
        checkLocationPermission(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requestBlePermission();
                } else {
                    blueToothScan();
                }

            }

            @Override
            public void onPermissionDenied() {

            }

            @Override
            public void onPermissionDeniedBySystem() {
                permissionHelper.showGoToSettingsDialog(getString(R.string.goto_setting_dialoge_message), getString(R.string.setting_toast_message_phone));
            }
        });
    }

    EMVTransactionManager.QuickChipTransactionSessionListener iemvTransaction = new EMVTransactionManager.QuickChipTransactionSessionListener() {
        @Override
        public void onReturnBluetoothDevices(final List<BluetoothDevice> bluetoothDeviceList) {
            if (alertDialog != null) {
                alertDialog.dismiss();
                alertDialog = null;
            }
            hideScanDevice();

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(BluetoothConnectionActivity.this);
            builder.setTitle(R.string.bluetooth_devices);

            String[] titles = new String[bluetoothDeviceList.size()];
            for (int i = 0; i < bluetoothDeviceList.size(); i++) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        requestBlePermission();
                    }
                    return;
                }
                titles[i] = bluetoothDeviceList.get(i).getName();
            }
            builder.setItems(titles, (dialog, which) -> EMVTransactionManager.connectBTDevice(BluetoothConnectionActivity.this, bluetoothDeviceList.get(which), iemvTransaction));
            // create and show the alert dialog
            alertDialog = builder.create();

            if (!alertDialog.isShowing() && !isFinishing()) {
                alertDialog.show();
            }

        }

        @Override
        public void onBluetoothDeviceConnected(BluetoothDevice bluetoothDevice) {
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requestBlePermission();
                }
                return;
            }
            Util.showLog("Bluetooth device", "bluetooth device connected : " + bluetoothDevice.getName());
            mBinding.setIsBlueToothConnected(true);
        }

        @Override
        public void onBluetoothDeviceDisConnected() {
            Util.showLog("Bluetooth device", "bluetooth device disconnected");
            mBinding.btnBtScan.setVisibility(View.VISIBLE);
            mBinding.setIsBlueToothConnected(false);
        }

        @Override
        public void onTransactionStatusUpdate(String transactionStatus) {
            Util.showLog("onTransactionStatusUpdate", transactionStatus);
            mBinding.txtStatus.setTextColor(Color.GREEN);
            mBinding.txtStatus.setText(transactionStatus);
        }

        @Override
        public void onPrepareQuickChipDataSuccessful() {
            Util.showLog("onPrepareQuickChipDataSuccessful", "success");
            mBinding.txtStatus.setTextColor(Color.BLACK);
            mBinding.txtStatus.setText(R.string.chip_data_save_message);
        }

        @Override
        public void onPrepareQuickChipDataError(EMVErrorCode error, String cause) {
            Util.showLog("onPrepareQuickChipDataError", cause);
            mBinding.txtStatus.setTextColor(Color.RED);
            mBinding.txtStatus.setText(cause);
            hideScanDevice();
        }

        @Override
        public void onEMVTransactionSuccessful(net.authorize.aim.emv.Result result) {
            processEmvTransactionResult(result);
        }

        @Override
        public void onEMVReadError(EMVErrorCode emvError) {

            hideScanDevice();
            mBinding.setIsBlueToothConnected(false);
            AlertDialog.Builder adb = new MaterialAlertDialogBuilder(BluetoothConnectionActivity.this);
            adb.setTitle(R.string.emv_status);
            adb.setNeutralButton(getString(R.string.ok), null);
            adb.setCancelable(true);
            if (emvError != null) {
                adb.setMessage(emvError.getErrorString());

            } else
                adb.setMessage(R.string.emv_error);
            if (emvError == EMVErrorCode.VOLUME_WARNING_NOT_ACCEPTED) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            } else {
                adb.create().show();
            }
        }

        @Override
        public void onEMVTransactionError(net.authorize.aim.emv.Result result, EMVErrorCode emvError) {
            processEmvTransactionError(result, emvError);
        }
    };
    private final ScanDeviceDialog scanDeviceDialog = new ScanDeviceDialog();
    Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message inputMessage) {
            hideProgress();
            if (inputMessage.what == 1) {
                Util.showToast(BluetoothConnectionActivity.this, getString(R.string.some_login_error_please_try_again_later));
            }
        }

    };
    private String totalAmount = "0";

    public void onClick(View view) {
        if (view == mBinding.btnBtScan) {
            requestLocationPermission();
        } else if (view == mBinding.btnPayNow) {
            payNow();
        }

    }

    private void blueToothScan() {
        showScanDevice();
        EMVTransactionManager.setDeviceConnectionType(EMVDeviceConnectionType.BLUETOOTH);
        EMVTransactionManager.startBTScan(this, iemvTransaction);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void requestBlePermission() {
        checkBlePermission(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                blueToothScan();
            }

            @Override
            public void onPermissionDenied() {

            }

            @Override
            public void onPermissionDeniedBySystem() {
                permissionHelper.showGoToSettingsDialog(getString(R.string.goto_setting_dialoge_message), getString(R.string.setting_toast_message_phone));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void checkBlePermission(PermissionHelper.PermissionCallback permissionCallback) {
        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT}, 100, getString(R.string.permanently_deny_all_payment));
        permissionHelper.request(permissionCallback);
    }

    private void checkLocationPermission(PermissionHelper.PermissionCallback permissionCallback) {
        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100, getString(R.string.permanently_deny_all_payment));
        permissionHelper.request(permissionCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth_connection);
        cardReaderVM = new ViewModelProvider(this).get(CardReaderVM.class);
        setToolbar(mBinding.toolbar.toolbar, getString(R.string.payment), R.drawable.ic_arrow_back_white_24dp);
        totalAmount = getIntent().getStringExtra(PaymentActivity.EXTRA_TOTAL_BILL_AMOUNT);
        callAuthorizeNetDetails();
        mBinding.txtAmount.setText(String.format(getString(R.string.dollar_value), MoversPreferences.getInstance(this).getCurrencySymbol(), Util.convertTwoDigit(totalAmount)));

    }


    protected void showScanDevice() {
        if (scanDeviceDialog != null && !scanDeviceDialog.isVisible() && !isFinishing()) {
            scanDeviceDialog.show(getSupportFragmentManager(), scanDeviceDialog.getTag());
        }

    }

    protected void hideScanDevice() {
        try {
            if (scanDeviceDialog != null && scanDeviceDialog.isVisible() && !isFinishing()) {
                scanDeviceDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void processEmvTransactionError(net.authorize.aim.emv.Result result, EMVErrorCode emvError) {
        Util.showLog("###", emvError.getError() + "  " + emvError.getErrorString());
        Util.showLog("###", new Gson().toJson(result));
        if (result != null) {
            Util.showLog("onEMVTransactionError tId: ", result.getTransId());
            ArrayList<MessageType> message1 = null;
            message1 = result.getMessages();
            if (message1 != null
                    && message1.size() > 0
                    && message1.get(0) != null
                    && message1.get(0).compareTo(MessageType.E00007) == 0) {
                Toast.makeText(BluetoothConnectionActivity.this, "Session " +
                        "time out: Please log in again", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return;
            }
        }

        if (result != null && result.getEmvTlvMap() != null) {
            showPaymentSuccessDialog(result, false);
        } else {
            MaterialAlertDialogBuilder adb = new MaterialAlertDialogBuilder(this);
            ArrayList<MessageType> message = null;
            if (result != null) {
                message = result.getMessages();
            }

            if (result == null) {
                if (emvError == EMVErrorCode.TRANSACTION_DECLINED)
                    adb.setMessage(emvError.getError() + "Transaction Decline");
            } else if (message != null
                    && message.size() > 0
                    && message.get(0) != null
                    && message.get(0).compareTo(MessageType.E00007) == 0) {
                Toast.makeText(getApplicationContext(), "Session time out: Please log in again", Toast.LENGTH_SHORT).show();
                onBackPressed();


            } else {
                ArrayList<ResponseReasonCode> responseError = result
                        .getTransactionResponseErrors();
                if (responseError != null
                        && responseError.size() > 0
                        && !StringUtils.isEmpty(responseError
                        .get(0).getReasonText()))
                    adb.setMessage(responseError.get(0)
                            .getReasonText());
                else
                    adb.setMessage("Transaction Unsuccessful");
            }
            adb.setTitle("EMV Status");
            adb.setNeutralButton("OK", null);
            adb.setCancelable(true);
            adb.create().show();
        }
    }

    private void payNow() {
        if (Double.parseDouble(totalAmount) > 0) {
            if (App.merchant == null) {
                onBackPressed();
                return;
            }

            Order order = Order.createOrder();
            OrderItem oi = OrderItem.createOrderItem();
            oi.setItemId(MoversPreferences.getInstance(this).getCurrentJobId());
            oi.setItemName(MoversPreferences.getInstance(this).getCurrentJobId());
            oi.setItemQuantity("1");
            oi.setItemTaxable(false);
            oi.setItemDescription("Goods");
            order.addOrderItem(oi);
            order.setDescription("Movers packers");
            order.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
            BigDecimal transAmount = new BigDecimal(totalAmount);
            oi.setItemPrice(transAmount);
            order.setTotalAmount(transAmount);
            EMVTransaction emvTransaction = EMVTransactionManager.createEMVTransaction(App.merchant, transAmount);
            emvTransaction.setEmvTransactionType(EMVTransactionType.GOODS);
            emvTransaction.setOrder(order);
            emvTransaction.setEmployeeId(MoversPreferences.getInstance(this).getUserId());
            EMVTransactionManager.setTerminalMode(EMVTransactionManager.TerminalMode.SWIPE_OR_INSERT);
            EMVTransactionManager.setDeviceConnectionType(EMVDeviceConnectionType.BLUETOOTH);

            EMVTransactionManager.startQuickChipTransaction(emvTransaction, iemvTransaction, BluetoothConnectionActivity.this);
        } else {
            Util.showSnackBar(mBinding.getRoot(), getString(R.string.please_enter_valid_amount));
        }
    }

    void processEmvTransactionResult(final net.authorize.aim.emv.Result result) {
        if (result != null) {
            Util.showLog("Result", new Gson().toJson(result));
            showPaymentSuccessDialog(result, true);
        } else {
            Util.showToast(this, "EMV Transaction Result is null");
        }

    }

    public void showPaymentSuccessDialog(Result result, Boolean isSuccess) {
        showProgress();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            hideProgress();
            PaymentReceiptDialog paymentReceiptDialog = PaymentReceiptDialog.newInstance(result, isSuccess);
            paymentReceiptDialog.setPaymentListener((email) -> {
                sendEmailReceipt(result.getTransId(), email);
                paymentReceiptDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra(PAYMENT_RESULT, result);
                setResult(RESULT_OK, intent);
                onBackPressed();
            });
            Util.showDialog(getSupportFragmentManager(), paymentReceiptDialog);
        }, 2000);

    }


    private void loginUser(String userName, String password) {
        showProgress();
        //Admin Password username : netensity password : #4435Osgood

        Thread t = new Thread() {

            @Override
            public void run() {
                String deviceID = UUID.randomUUID().toString();
                PasswordAuthentication passAuth = PasswordAuthentication
                        .createMerchantAuthentication(userName, password, deviceID);
                // TransactionKeyAuthentication auth = TransactionKeyAuthentication.createMerchantAuthentication("3d6KUS763twE", "6W9N5sU27J5Z4hzn");
                App.merchant = Merchant.createMerchant(Environment.PRODUCTION, passAuth);

                Transaction transaction = App.merchant
                        .createMobileTransaction(TransactionType.MOBILE_DEVICE_LOGIN);
                MobileDevice mobileDevice = MobileDevice.createMobileDevice(deviceID,
                        MoversPreferences.getInstance(BluetoothConnectionActivity.this).getUserName(), MoversPreferences.getInstance(BluetoothConnectionActivity.this).getUserId(), "Android");
                transaction.setMobileDevice(mobileDevice);
                net.authorize.mobile.Result result = (net.authorize.mobile.Result) App.merchant
                        .postTransaction(transaction);

                if (result.isOk()) {

                    try {
                        SessionTokenAuthentication sessionTokenAuthentication = SessionTokenAuthentication
                                .createMerchantAuthentication(App.merchant
                                        .getMerchantAuthentication().getName(), result
                                        .getSessionToken(), deviceID);
                        if (result.getSessionToken() != null) {
                            App.merchant
                                    .setMerchantAuthentication(sessionTokenAuthentication);
                            handler.sendEmptyMessage(0);

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    Log.e("### Login Error", result.getMessages().get(0).getText() + result.getMessages().get(0).getText());
                    handler.sendEmptyMessage(1);
                }
            }
        };
        t.start();

    }


    private void sendEmailReceipt(String transactionId, String emailId) {
        Thread thread = new Thread(() -> {
            try {
                net.authorize.notification.Transaction t = App.merchant.createNotificationTransaction(net.authorize.notification.TransactionType.CUSTOMER_TRANSACTION_RECEIPT_EMAIL);
                t.setTransId(transactionId);
                t.setCustomerEmailAddress(emailId);
                net.authorize.notification.Result result = (net.authorize.notification.Result) App.merchant.postTransaction(t);
                Util.showLog("Email Receipt", new Gson().toJson(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();


    }


    private void callAuthorizeNetDetails() {
        showProgress();
        cardReaderVM.callAuthorizeNetDetails(
                MoversPreferences.getInstance(this).getSubdomain(),
                MoversPreferences.getInstance(this).getUserId(),
                MoversPreferences.getInstance(this).getOpportunityId(), new ResponseListener<BaseResponseModel<CardReaderModel>>() {
                    @Override
                    public void onResponse(BaseResponseModel<CardReaderModel> response, String usedUrlKey) {
                        hideProgress();
                        loginUser(response.getData().getUserName(), response.getData().getPassword());
                    }

                    @Override
                    public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                        hideProgress();
                        Util.showSnackBar(mBinding.getRoot(), serverResponseError.getMessage());
                    }

                    @Override
                    public void onFailure(Call<BaseResponseModel<CardReaderModel>> call, Throwable t, String message) {
                        hideProgress();
                        Util.showSnackBar(mBinding.getRoot(), message);
                    }
                });
    }


}