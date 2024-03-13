package com.moverbol.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.moverbol.BuildConfig;
import com.moverbol.R;
import com.moverbol.model.RawBodyLocationUpdateRequestModel;
import com.moverbol.network.HeaderInterceptor;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.views.activities.SplashActivity;

import retrofit2.Call;

import static android.content.Context.NOTIFICATION_SERVICE;

public class LocationUpdateWork extends Worker {

    private Context mContext;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private String[] mPermissionsArray = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final long F_INTERVAL = (10 * 1000);
    private static final long FASTEST_INTERVAL = F_INTERVAL / 2;

    private static final int NOTIFICATION_TYPE_SENT_LOCATION_UPDATES = 6;


    public LocationUpdateWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context.getApplicationContext();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(F_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @NonNull
    @Override
    public Result doWork() {

//        sendNotification("Do work started");

        if (!checkSelfPermission(mPermissionsArray, mContext)) {
            return Result.failure();
        }

        getUserLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {

                double latitude = 0;
                double longitude = 0;

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                RawBodyLocationUpdateRequestModel locationUpdateRequestModel = new RawBodyLocationUpdateRequestModel();
                locationUpdateRequestModel.setLatitude(latitude + "");
                locationUpdateRequestModel.setLongitude(longitude + "");
                locationUpdateRequestModel.setSubdomain(MoversPreferences.getInstance(getApplicationContext()).getSubdomain());
                locationUpdateRequestModel.setUserId(MoversPreferences.getInstance(getApplicationContext()).getUserId());

                /**
                 * Need to send these unique ids in every API call. Cannot get it directly into
                 * RetrofitClient class as for that RetrofitClient class will need to have a context.
                 * Hence saving it as a static variable.
                 */
                HeaderInterceptor.deviceId = MoversPreferences.getInstance(getApplicationContext()).getUniqueAppId();
                HeaderInterceptor.userId = MoversPreferences.getInstance(getApplicationContext()).getUserId();

                WebServiceManager.getInstance().sendCurrentLocation(locationUpdateRequestModel, new ResponseListener<BaseResponseModel>() {
                    @Override
                    public void onResponse(BaseResponseModel response, String usedUrlKey) {
                        //TODO:Remove this after testing

                        double latitude = 0;
                        double longitude = 0;

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                        if (BuildConfig.shouldShowLocationUpdateNotification)
                            sendNotification("Location sent Successfully: " + latitude + "," + longitude);
                    }

                    @Override
                    public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                        //TODO:Remove this after testing
                        if (BuildConfig.shouldShowLocationUpdateNotification)
                            sendNotification("Location sent error: " + serverResponseError.getMessage());
                    }

                    @Override
                    public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                        //TODO:Remove this after testing
                        if (BuildConfig.shouldShowLocationUpdateNotification)
                            sendNotification("Location sent failure: " + message);
                    }
                });
            }
        });


        return Result.success();
    }

    private void sendNotification(String message) {

        String channelId = "channel_id_6";
//        int notificationId = 1000;
        int notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_SENT_LOCATION_UPDATES);

        MoversPreferences.getInstance(getApplicationContext()).
                setNotificationId(NOTIFICATION_TYPE_SENT_LOCATION_UPDATES,
                        notificationId + NOTIFICATION_TYPE_SENT_LOCATION_UPDATES);

        notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_SENT_LOCATION_UPDATES);


        NotificationCompat.Builder notificationBuilder = null;
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "BLocation updates sent changed notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notifies if the location updates are sent to server or not.");
            notificationChannel.enableLights(false);
//            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setBypassDnd(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(mContext, SplashActivity.class);

        notificationBuilder = new NotificationCompat.Builder(mContext, channelId)
                .setSmallIcon(R.drawable.ic_bus_green)
//                .setSmallIcon(R.drawable.notification_icon)
                .setColor(mContext.getResources().getColor(R.color.colorPrimary))
//                .setContentText("From Work Manager" + message)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(mContext, notificationId, intent, PendingIntent.FLAG_ONE_SHOT));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        } else {
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    @SuppressLint("MissingPermission")
    private void getUserLocation(final OnSuccessListener<Location> onLocationSucessListener) {

        Log.d("LocationUpdate", "In get User Location");

        /*
        Clients needs to run demo on ARC welder(Chrome Extension). ARC welder does not support google play services.
        Hence in that case app sends location as 00.
        */
        if (!BuildConfig.usePlayServicesForLoction) {
            onLocationSucessListener.onSuccess(null);
            Log.d("LocationUpdate", "Returning without getting location");
            return;
        }

        Log.d("LocationUpdate", "starting location updates");

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("LocationUpdate", "got location update");
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Log.d("LocationUpdate", "removeing location updates and returning");
                    onLocationSucessListener.onSuccess(locationResult.getLastLocation());
                    mFusedLocationClient.removeLocationUpdates(this);
                }
            }
        }, Looper.getMainLooper());
    }


    private boolean checkSelfPermission(String[] permissions, Context context) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
