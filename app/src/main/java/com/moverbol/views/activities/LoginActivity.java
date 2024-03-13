package com.moverbol.views.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.instabug.library.Instabug;
import com.moverbol.BuildConfig;
import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.LoginBinding;
import com.moverbol.model.ForceUpdateResponseModel;
import com.moverbol.network.HeaderInterceptor;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.SignInData;
import com.moverbol.network.viewmodels.LoginViewModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.PermissionHelper;

import java.util.UUID;

import retrofit2.Call;


/**
 * On ForgetPassword selection open ForgetPassword activity.
 * On Login selection Calls LoginViewModel's callLoginAPI(demo(optional company id), email, password).
 */

public class LoginActivity extends BaseAppCompactActivity implements View.OnClickListener {

    private LoginBinding binding;
    private LoginViewModel loginViewModel;
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_new);

        loginViewModel = new LoginViewModel();
        binding.tvForgotPassword.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {

            MoversPreferences.getInstance(LoginActivity.this).setFirebaseToken(token);

            Log.i(Constants.BASE_LOG_TAG + "firebaseToken", token + "");
        });


        setViewModelObservers();

    }

    private void setViewModelObservers() {
        loginViewModel.shouldShowUpdateDialogLive.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer updateRequiredCheckFlags) {
                //TODO; Uncomment to enable force update
                if (updateRequiredCheckFlags != null && (updateRequiredCheckFlags == Constants.UpdateRequiredCheckFlags.UPDATE_FORCE_NEEDED || updateRequiredCheckFlags == Constants.UpdateRequiredCheckFlags.UPDATE_SOFT_NEDDED)) {
                    showUpdateRequiredDialog(updateRequiredCheckFlags);
                } else {
                    clickLoginCall();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_forgot_password:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

            case R.id.btn_login:
                final String companyId = binding.edtCompanyId.getText().toString().trim();
                String email = binding.edtEmail.getText().toString().trim();
                String password = binding.edtPass.getText().toString().trim();
                boolean shouldSavePassword = binding.chkRemember.isChecked();
                if (validate(companyId, email, password)) {
                    callCheckIfUpdateNeeded(companyId);
//                    callLogin(companyId, email, password, shouldSavePassword);
                }
                break;
        }
    }

    private void performLogin(final String companyId, final String email, final String password, final boolean shouldRememberLogin) {
        checkLocationPermission(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                callLogin(companyId, email, password, shouldRememberLogin);
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

    private void callLogin(final String companyId, final String email, final String password, final boolean shouldRememberLogin) {
        if (!shouldMakeNetworkCall(binding.cnstrRoot)) {
            return;
        }

        final String firebaseToken = MoversPreferences.getInstance(this).getFirebaseToken();

        final String uniqueID = UUID.randomUUID().toString();
/*        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        @SuppressLint({"MissingPermission", "HardwareIds"}) String uniqueID = telephonyManager.getDeviceId();*/
//        String uniqueID = Settings.Secure.ANDROID_ID;
        MoversPreferences.getInstance(this).setUniqueAppId(uniqueID);


        Log.i(Constants.BASE_LOG_TAG + "firebaseToken", firebaseToken + "");
        Log.i(Constants.BASE_LOG_TAG + "uniqueID", uniqueID + "");

        showProgress();

        loginViewModel.callLogInAPI(companyId, email, password, firebaseToken, uniqueID,
                new ResponseListener<BaseResponseModel<SignInData>>() {
                    @Override
                    public void onResponse(BaseResponseModel<SignInData> response, String usedUrlKey) {

                        hideProgress();

                        if (response.getResponseCode() == 200) {
                            MoversPreferences.getInstance(LoginActivity.this).setValue(MoversPreferences.USER_ID, response.getData().getUserId());
                            MoversPreferences.getInstance(LoginActivity.this).setUserDetails(response.getData());
                            MoversPreferences.getInstance(LoginActivity.this).setUserName(response.getData().getUserName());
                            MoversPreferences.getInstance(LoginActivity.this).setUserEmail(response.getData().getUserEmailId());
                            MoversPreferences.getInstance(LoginActivity.this).setSubdomain(companyId);
                            MoversPreferences.getInstance(LoginActivity.this).setCurrencySymbol(response.getData().getCurrencySymbol());
                            MoversPreferences.getInstance(LoginActivity.this).setUserDesignationType(response.getData().getUserDesignationType());
                            MoversPreferences.getInstance(LoginActivity.this).setProfileImageUrl(response.getData().getProfileImageUrl());

                            //To log user in fabric
                            logUser(response.getData());


                            /**
                             * Sending this unique id in every API call. Cannot directly get it directly into
                             * RetrofitClient class as for that RetrofitClient class will need to have a context.
                             * Hence saving it as a static variable.
                             */
                            HeaderInterceptor.deviceId = uniqueID;
                            HeaderInterceptor.userId = MoversPreferences.getInstance(LoginActivity.this).getUserId();


                            setUserInInstaBug(response.getData().getUserId(),
                                    response.getData().getUserName(), response.getData().getUserEmailId());

                            if (shouldRememberLogin) {
                                MoversPreferences.getInstance(LoginActivity.this).setLogin(true);
                            }

                            //Starting Location Periodic Updates
                            /*if (binding.rbWorkManager.isChecked()) {
                                loginViewModel.startLocationUpdate();
                            } else {
                                loginViewModel.setAlarm(0l, LoginActivity.this);
                            }*/

                            //   loginViewModel.setAlarm(0l, LoginActivity.this);

                            Intent intent = new Intent();
                            if (response.getData().isShowSetPassword()) {
                                intent.setClass(LoginActivity.this, SetPasswordActivity.class);
                            } else {
                                intent.setClass(LoginActivity.this, HomeActivity.class);
                            }

                            startActivity(intent);

                            finish();
                        } else {
                            String errorMessage = "";
                            if (response.getMessage() != null && !response.getMessage().equals("null")) {
                                errorMessage = response.getMessage();
                            }
                            Snackbar.make(binding.cnstrRoot, LoginActivity.this.getText(R.string.login_failed) + errorMessage, Snackbar.LENGTH_LONG).show();
                                       /* String errorMessage = "";
                                        if(response.getMessage()!=null){
                                            errorMessage = response.getMessage();
                                        }
                                        Util.showSnackBarWithNullPosibility(binding.cnstrRoot, LoginActivity.this.getText(R.string.login_failed) + errorMessage, "");*/
                        }
                    }

                    @Override
                    public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                        hideProgress();
                        Snackbar.make(binding.cnstrRoot, serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<BaseResponseModel<SignInData>> call, Throwable t, String message) {
                        hideProgress();
                        Snackbar.make(binding.cnstrRoot, message, Snackbar.LENGTH_LONG).show();
                    }
                });
    }


    private void callCheckIfUpdateNeeded(String subDomain) {

        if ((!shouldMakeNetworkCall(binding.getRoot())) || subDomain == null || subDomain.isEmpty()) {
            return;
        }

        showProgress();

        loginViewModel.checkIfUpdateNeeded(subDomain, new ResponseListener<BaseResponseModel<ForceUpdateResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ForceUpdateResponseModel> response, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                hideProgress();
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ForceUpdateResponseModel>> call, Throwable t, String message) {
                hideProgress();
            }
        });
    }


    private boolean validate(String companyId, String email, String password) {
        if (TextUtils.isEmpty(companyId)) {
            binding.edtCompanyId.setError(getString(R.string.empty_field_error));
            return false;
        } else if (TextUtils.isEmpty(email)) {
            binding.edtEmail.setError(getString(R.string.empty_field_error));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError(getString(R.string.invalid_email_error));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            binding.edtEmail.requestFocus();
            binding.edtEmail.setError(getString(R.string.empty_field_error));
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkLocationPermission(PermissionHelper.PermissionCallback permissionCallback) {
        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100, getString(R.string.permanently_deny_all_telephony));
        permissionHelper.request(permissionCallback);
    }


    private void showUpdateRequiredDialog(Integer updateRequiredCheckFlags) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.new_version_available)
                .setMessage(R.string.update_request_message)
                .setPositiveButton(R.string.update,
                        (dialog12, which) -> {
                            redirectToPlayStore();
                            finish();
                        });
        if (updateRequiredCheckFlags == Constants.UpdateRequiredCheckFlags.UPDATE_SOFT_NEDDED) {
            dialog.setNegativeButton(R.string.no_thanks,
                    (dialog1, which) -> {
                        clickLoginCall();
                    }
            );
        }

        dialog.create().show();
    }

    private void clickLoginCall() {
        final String companyId = binding.edtCompanyId.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPass.getText().toString().trim();
        boolean shouldSavePassword = binding.chkRemember.isChecked();
        if (validate(companyId, email, password))
            callLogin(companyId, email, password, shouldSavePassword);
    }

    private void redirectToPlayStore() {

        String url = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * Created for testing purpose only. It sets required sharedprefs withought having to login.
     * I can test a user's account by just his userId and subdomain using this function by calling
     * it on click of skip login.
     *
     * @param subdomain
     * @param userId
     */
    private void setTestLoginDetails(String subdomain, String userId) {
        MoversPreferences.getInstance(LoginActivity.this).setValue(MoversPreferences.USER_ID, userId);
        MoversPreferences.getInstance(LoginActivity.this).setUserName("Test Nme");
        MoversPreferences.getInstance(LoginActivity.this).setUserEmail("test@t.com");
        MoversPreferences.getInstance(LoginActivity.this).setSubdomain(subdomain);
        MoversPreferences.getInstance(LoginActivity.this).setCurrencySymbol("$");

        SignInData testData = new SignInData();
        testData.setUserId(userId);
        testData.setUserEmailId("test@t.com");
        testData.setUserName("Test Name");

        //To log user in fabric
        logUser(new SignInData());

        setUserInInstaBug(userId,
                "test", "test@t.com");

        MoversPreferences.getInstance(LoginActivity.this).setLogin(true);

        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void logUser(SignInData data) {
        // You can call any combination of these three methods
        FirebaseCrashlytics.getInstance().setUserId(data.getUserId());
        FirebaseCrashlytics.getInstance().setCustomKey("Email", data.getUserEmailId());
        FirebaseCrashlytics.getInstance().setCustomKey("Name", data.getUserName());
    }

    private void setUserInInstaBug(String userId, String userName, String userEmailId) {
        Instabug.identifyUser(userName, userEmailId);
        Instabug.setUserAttribute("UserId", userId);
    }

}
