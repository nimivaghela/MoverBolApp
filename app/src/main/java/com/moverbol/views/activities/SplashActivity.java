package com.moverbol.views.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.moverbol.BuildConfig;
import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.model.ForceUpdateResponseModel;
import com.moverbol.network.HeaderInterceptor;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;
import com.moverbol.viewmodels.SplashViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;

public class SplashActivity extends BaseAppCompactActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 1;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Handler myHandler;
    private Runnable myRunnable;
    private TextView txtVersion;
    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);

        initVariable();

        /**
         * Need to send these unique ids in every API call. Cannot get it directly into
         * RetrofitClient class as for that RetrofitClient class will need to have a context.
         * Hence saving it as a static variable.
         */

        if (MoversPreferences.getInstance(SplashActivity.this).isLogin()) {
            HeaderInterceptor.deviceId = MoversPreferences.getInstance(this).getUniqueAppId();
            HeaderInterceptor.userId = MoversPreferences.getInstance(this).getUserId();
        }


//        openNextScreen();

        if (viewModel.shouldShowUpdateDialogLive.getValue() == null) {
            Util.showLog("###", "shouldShowUpdateLiveData");
            callCheckIfUpdateNeeded();
//            callCheckIfUpdateNeeded(true);//FIXME
        }

        viewModel.shouldShowUpdateDialogLive.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer updateRequiredCheckFlags) {
                if (updateRequiredCheckFlags != null && (updateRequiredCheckFlags == Constants.UpdateRequiredCheckFlags.UPDATE_FORCE_NEEDED || updateRequiredCheckFlags == Constants.UpdateRequiredCheckFlags.UPDATE_SOFT_NEDDED)) {
                    showUpdateRequiredDialog(updateRequiredCheckFlags);
                } else {
                    openNextScreen();
                }
            }
        });

    }

    private void openNextScreen() {
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable, Constants.SPLASH_SCREEN_TIMEOUT);

    }

    private boolean checkIfBetaTestingPeriodIsOverOrNot(String dateString) {
        try {
//            String dateString = "10/31/2018 23:59:59";
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.OUTPUT_DATE_FORMAT_TIMINGS_DETAILS, Locale.getDefault());
            Date dateToCompate = dateFormat.parse(dateString);
            if ((new Date()).after(dateToCompate)) {
                showBetaTestingPeriodOverAlert();
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    public void initVariable() {

        myHandler = new Handler();

        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        myRunnable = new Runnable() {
            @Override
            public void run() {
                startActivity();
            }
        };

        txtVersion = findViewById(R.id.txt_version);
        txtVersion.setText("Version " + BuildConfig.VERSION_NAME);


    }

    private void startActivity() {
        if (MoversPreferences.getInstance(SplashActivity.this).isLogin()) {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }


    private void showBetaTestingPeriodOverAlert() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Sorry, beta testing period is over. To keep using our services please upgrade to the release version. ")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create();

        dialog.show();
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacks(myRunnable);
        }
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
                        dialog1.dismiss();
                        openNextScreen();
                    }
            );
        }

        dialog.create().show();
    }

    private void redirectToPlayStore() {

        String url = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void callCheckIfUpdateNeeded() {
        Util.showLog("###", "CallCheckIFUpdateNeeded");
        String subDomain = MoversPreferences.getInstance(this).getSubdomain();

        if ((!shouldMakeNetworkCall(txtVersion.getRootView())) || subDomain == null || subDomain.isEmpty()) {
            openNextScreen();
            Util.showLog("###", "open");
            return;
        }

        showProgress();
        Util.showLog("###", "CallUpdateAPI");
        viewModel.checkIfUpdateNeeded(subDomain, new ResponseListener<BaseResponseModel<ForceUpdateResponseModel>>() {
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


    /*private void callCheckIfUpdateNeeded(boolean a){
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
      *//*  appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                // Checks that the platform will allow the specified type of update.
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.
                }
            }
        });*//*

        appUpdateManager.getAppUpdateInfo().addOnCompleteListener(new com.google.android.play.core.tasks.OnCompleteListener<AppUpdateInfo>() {
            @Override
            public void onComplete(com.google.android.play.core.tasks.Task<AppUpdateInfo> task) {
                task.isSuccessful();
            }
        });

    }*/




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (MoversPreferences.getInstance(SplashActivity.this).isLogin()) {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}


