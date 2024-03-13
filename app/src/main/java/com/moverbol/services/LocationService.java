package com.moverbol.services;

/*
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.instabug.library.network.NetworkManager;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.RetrofitClient;
import com.moverbol.network.model.BaseResponseModel;

import androidx.core.app.ActivityCompat;
import retrofit2.Call;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

*/
/**
 * Created by 28/12/17.
 *//*


public class LocationService extends JobService implements ResponseListener {

    public static final String TAG = LocationService.class.getSimpleName();

    public static final String LAT = "lat";
    public static final String LOG = "lng";
    public static boolean isServiceStart = false;

    private final int INTERVAL_LOCATION_CHANGE = 10 * 60 * 1000;  // 10 Min

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private LocationRequest mLocationRequest;
    private JobParameters mJob;
    private ResponseListener responseListener;



    @Override
    public void onCreate() {
        super.onCreate();
        this.responseListener = new ResponseListener() {
            @Override
            public void onResponse(Object response, String usedUrlKey) {

            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {

            }

            @Override
            public void onFailure(Call call, Throwable t, String message) {

            }
        };
        Utility.Log("On Create");
        isServiceStart = true;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startLocationUpdates();
    }

    private void getLocationUpdate() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location currentLocation = locationResult.getLastLocation();
                Utility.Log("get Location Update");
                if (currentLocation == null) {
                    return;
                } else {
                    Utility.Log("" + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                    ShareMealPreferences.setValue(getApplicationContext(), GlobalKeys.KEY_LATITUDE, String.valueOf(currentLocation.getLatitude()));
                    ShareMealPreferences.setValue(getApplicationContext(), GlobalKeys.KEY_LONGITUDE, String.valueOf(currentLocation.getLongitude()));

                    if (Utility.isNetworkAvailable(getApplicationContext())) {
                        Call<ResponseData> call = RetrofitClient.getInstance(getApplicationContext()).getApiInterface().changeLocationService(
                                String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude())
                        );
                        new NetworkManager(getApplicationContext()).RequestData(responseListener, call, ApiService.CHANGE_LOCATION);
                    }

                }

                jobFinished(mJob, false);

            }
        };
    }


    @Override
    public boolean onStartJob(JobParameters job) {
        this.mJob = job;
        Utility.Log("Start Job");
        isGPSEnable();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        disConnectLocationListener();
        isServiceStart = false;
        return true;
    }

    @Override
    public void onDestroy() {
        Utility.Log("OnDestroy");
        disConnectLocationListener();
        isServiceStart = false;
        super.onDestroy();
    }

    private void disConnectLocationListener() {
        if (mFusedLocationClient != null && mLocationCallback != null) {
            Utility.Log("stop Location Update");
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void startLocationUpdates() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();

            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            mLocationRequest.setInterval(INTERVAL_LOCATION_CHANGE);
        }

        if (mLocationCallback == null) {
            getLocationUpdate();
        }

    }

    public void isGPSEnable() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getApplicationContext());
        com.google.android.gms.tasks.Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<LocationSettingsResponse> task) {
                if (task.isSuccessful()) {


                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        jobFinished(mJob, false);
                        return;
                    }
                    Utility.Log("start Location Update");
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());

                } else {
                    int statusCode = ((ApiException) task.getException()).getStatusCode();
                    switch (statusCode) {
                        case CommonStatusCodes.RESOLUTION_REQUIRED:
                            jobFinished(mJob, false);
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way
                            // to fix the settings so we won't show the dialog.
                            jobFinished(mJob, false);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onResponse(ResponseData response, String key) {

    }
}
*/
