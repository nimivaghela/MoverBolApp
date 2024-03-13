package com.moverbol.views.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.moverbol.BuildConfig;
import com.moverbol.R;
import com.moverbol.databinding.ActivityBackgroundLocationBinding;
import com.moverbol.services.AlarmBroadcastReceiver;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.PermissionHelper;

public class BackgroundLocationActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private ActivityBackgroundLocationBinding mBinding;
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_background_location);

        loadData();
    }

    private void loadData() {
        mBinding.btnTurnOn.setOnClickListener(v -> {
            checkLocationPermission(new PermissionHelper.PermissionCallback() {
                @Override
                public void onPermissionGranted() {

                    checkLocationSettings(new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            setAlarm(0, BackgroundLocationActivity.this);
                            onBackPressed();
                        }
                    });
                }

                @Override
                public void onPermissionDenied() {
                    /*myHandler.removeCallbacks(myRunnable);
                    myHandler.postDelayed(myRunnable, Constants.SPLASH_SCREEN_TIMEOUT);*/
                    onBackPressed();
                }

                @Override
                public void onPermissionDeniedBySystem() {
                    permissionHelper.showGoToSettingsDialog(getString(R.string.goto_setting_dialoge_message), getString(R.string.setting_toast_message_phone));
                }
            });
        });

        mBinding.btnNoThanks.setOnClickListener(v -> {
            MoversPreferences.getInstance(this).setDenyLocationPermission(true);
            onBackPressed();
        });
    }

    private void checkLocationPermission(PermissionHelper.PermissionCallback permissionCallback) {
        String[] permission;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            permission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        } else {
            permission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        }
        permissionHelper = new PermissionHelper(this, permission, 100, getString(R.string.permanently_deny_all_telephony));
        permissionHelper.request(permissionCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkLocationSettings(OnSuccessListener<LocationSettingsResponse> onSuccessListener) {

        /*
        Clients needs to run demo on ARC welder(Chrome Extension). ARC welder does not support google play services.
        Hence in that case app sends location as 00.
        */
        if (!BuildConfig.usePlayServicesForLoction) {
            onSuccessListener.onSuccess(null);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationRequest.create());
        SettingsClient client = LocationServices.getSettingsClient(this);

        //Check if location settings are as per requirement or not.
        client.checkLocationSettings(builder.build())
                .addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                    }
                })
                .addOnSuccessListener(this, onSuccessListener)
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ResolvableApiException) {
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(BackgroundLocationActivity.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                        }
                    }
                });
    }


    public void setAlarm(long time, Context context) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(context, AlarmBroadcastReceiver.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(context.getApplicationContext(), 0, i, 0);

        //setting the repeating alarm that will be fired every day

//        am.setInexactRepeating(AlarmManager.RTC, time, 5 * 60 * 1000, pi);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setAndAllowWhileIdle(AlarmManager.RTC, time, pi);
        } else {
            am.set(AlarmManager.RTC, time, pi);
        }

//        Toast.makeText(context, "Alarm is set", Toast.LENGTH_SHORT).show();
    }


}