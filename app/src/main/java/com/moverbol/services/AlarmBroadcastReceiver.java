package com.moverbol.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

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

import java.util.Date;

import retrofit2.Call;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private String[] mPermissionsArray = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final long F_INTERVAL = (10 * 1000);
    private static final long FASTEST_INTERVAL = F_INTERVAL / 2;

    private static final int NOTIFICATION_TYPE_SENT_LOCATION_UPDATES = 6;

    @Override
    public void onReceive(Context context, Intent intent) {

        final Context finalContext = context;

//        sendNotification("Do work started", finalContext);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(finalContext);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(F_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(!MoversPreferences.getInstance(context.getApplicationContext()).isLogin()){
            return;
        }

        if(!checkSelfPermission(mPermissionsArray, finalContext)){
            return;
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
                locationUpdateRequestModel.setSubdomain(MoversPreferences.getInstance(finalContext.getApplicationContext()).getSubdomain());
                locationUpdateRequestModel.setUserId(MoversPreferences.getInstance(finalContext.getApplicationContext()).getUserId());

                /**
                 * Need to send these unique ids in every API call. Cannot get it directly into
                 * RetrofitClient class as for that RetrofitClient class will need to have a context.
                 * Hence saving it as a static variable.
                 */
                HeaderInterceptor.deviceId = MoversPreferences.getInstance(finalContext.getApplicationContext()).getUniqueAppId();
                HeaderInterceptor.userId = MoversPreferences.getInstance(finalContext.getApplicationContext()).getUserId();

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

                        if(BuildConfig.shouldShowLocationUpdateNotification)
                            sendNotification("Location sent Successfully: " + latitude + "," + longitude, finalContext);

                        setAlarm(finalContext);

                    }

                    @Override
                    public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                        //TODO:Remove this after testing
                        if(BuildConfig.shouldShowLocationUpdateNotification)
                            sendNotification("Location sent error: " + serverResponseError.getMessage(), finalContext);

                        setAlarm(finalContext);
                    }

                    @Override
                    public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                        //TODO:Remove this after testing
                        if(BuildConfig.shouldShowLocationUpdateNotification)
                            sendNotification("Location sent failure: " + message, finalContext);


                        setAlarm(finalContext);
                    }
                });
            }
        });
    }


    private void sendNotification(String message, Context context) {

        String channelId = "channel_id_6";
//        int notificationId = 1000;
        int notificationId = MoversPreferences.getInstance(context.getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_SENT_LOCATION_UPDATES);

        MoversPreferences.getInstance(context.getApplicationContext()).
                setNotificationId(NOTIFICATION_TYPE_SENT_LOCATION_UPDATES,
                        notificationId + NOTIFICATION_TYPE_SENT_LOCATION_UPDATES);

        notificationId = MoversPreferences.getInstance(context.getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_SENT_LOCATION_UPDATES);


        NotificationCompat.Builder notificationBuilder = null;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "BLocation updates sent changed notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notifies if the location updates are sent to server or not.");
            notificationChannel.enableLights(false);
//            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setBypassDnd(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(context, SplashActivity.class);

        notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_bus_green)
//                .setSmallIcon(R.drawable.notification_icon)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
//                .setContentText("From Alarm Manager" + message)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_ONE_SHOT));

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


    public void setAlarm(Context context) {

        if(!MoversPreferences.getInstance(context.getApplicationContext()).isLogin()){
            return;
        }

        long time = new Date().getTime() + 15 * 60 * 1000;

        //getting the alarm manager
        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(context.getApplicationContext(), AlarmBroadcastReceiver.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(context.getApplicationContext(), 0, i, 0);

        //setting the repeating alarm that will be fired every day

//        am.setInexactRepeating(AlarmManager.RTC, time, 5 * 60 * 1000, pi);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            am.setAndAllowWhileIdle(AlarmManager.RTC, time, pi);
        } else {
            am.set(AlarmManager.RTC, time, pi);
        }
    }

}
