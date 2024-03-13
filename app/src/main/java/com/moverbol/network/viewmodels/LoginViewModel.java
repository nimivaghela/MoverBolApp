package com.moverbol.network.viewmodels;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.moverbol.DataRepository;
import com.moverbol.constants.Constants;
import com.moverbol.model.ForceUpdateResponseModel;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.RetrofitClient;
import com.moverbol.network.WebServiceManager;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.model.SignInData;
import com.moverbol.services.AlarmBroadcastReceiver;
import com.moverbol.services.LocationUpdateWork;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;

/**
 * Created by Admin on 06-11-2017.
 */

public class LoginViewModel extends ViewModel {

    public MutableLiveData<Integer> shouldShowUpdateDialogLive = new MutableLiveData<>();

    /**
     * Checks if internet is  avilable. Creates a Call<SignInPojo> object and then calls
     * WebServiceManager's dologin(Call<SignInPojo> object).
     * WebServiceManager object needs in responceListener and context. We are not doing
     * anything here in responceListener.
     *
     * @param demo  Company id
     * @param email Registered email
     * @param pwd   Password
     */

    public void callLogInAPI(String demo, String email, String pwd, String firebaseId, String deviceId, ResponseListener<BaseResponseModel<SignInData>> responseListener) {
        Call<BaseResponseModel<SignInData>> signInPojoCall = RetrofitClient.getInstance().getApiInterface().signIn(demo, email, pwd, firebaseId, deviceId);
        DataRepository.getInstance().doLogin(signInPojoCall, responseListener);
    }

    public void callForgotPasswordApi(String demo, String email, ResponseListener<BaseResponseModel> responseListener) {
        Call<BaseResponseModel> forgotPasswordPojoCall = RetrofitClient.getInstance().getApiInterface().forgotpassword(demo, email);
//            new WebServiceManager(this, context).forgotPwd(forgotPasswordPojoCall);
        WebServiceManager.getInstance().forgotPwd(forgotPasswordPojoCall, responseListener);
    }


    /**
     * Checks if internet is  avilable. Creates a Call<ForgotPasswordPojo> object and then calls
     * WebServiceManager's setPwd(Call<ForgotPasswordPojo> object).
     * WebServiceManager object needs in responceListener and context. We are not doing
     * anything here in responceListener.
     *
     * @param demo    Company id
     * @param user_id Registered email
     * @param pwd     Password
     */
    public void callSetPasswordApi(String demo, String user_id, String pwd, ResponseListener<BaseResponseModel> responseListener) {

        Call<BaseResponseModel> forgotPasswordPojoCall = RetrofitClient.getInstance().getApiInterface().setPassword(demo, user_id, pwd);
//            new WebServiceManager(this, context).setPwd(forgotPasswordPojoCall);
        WebServiceManager.getInstance().setPwd(forgotPasswordPojoCall, responseListener);
    }


    public void checkIfUpdateNeeded(String subdomain, final ResponseListener<BaseResponseModel<ForceUpdateResponseModel>> responseListener) {
        DataRepository.getInstance().checkIfUpdateNeeded(subdomain, new ResponseListener<BaseResponseModel<ForceUpdateResponseModel>>() {
            @Override
            public void onResponse(BaseResponseModel<ForceUpdateResponseModel> response, String usedUrlKey) {

                if (response.getData() != null && response.getData().getForceUpdateStatus() != null &&
                        response.getData().getForceUpdateStatus().equalsIgnoreCase("Update New Version") &&
                        response.getData().shouldForceUpdate()) {
                    if (response.getData().shouldClosePopup()) {
                        shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_SOFT_NEDDED);
                    } else {
                        shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_FORCE_NEEDED);
                    }
                } else {
                    shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_NOT_NEEDED);
                }
                responseListener.onResponse(response, usedUrlKey);
            }

            @Override
            public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_CHECK_ERROR);
                responseListener.onResponseError(serverResponseError, usedUrlKey);
            }

            @Override
            public void onFailure(Call<BaseResponseModel<ForceUpdateResponseModel>> call, Throwable t, String message) {
                shouldShowUpdateDialogLive.setValue(Constants.UpdateRequiredCheckFlags.UPDATE_CHECK_ERROR);
                responseListener.onFailure(call, t, message);
            }
        });
    }


    public void startLocationUpdate() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(LocationUpdateWork.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .addTag("location_update_work")
                .build();

        WorkManager.getInstance().cancelAllWork();
//        WorkManager.getInstance().enqueue(request);

        WorkManager.getInstance().enqueueUniquePeriodicWork(
                "location_update_work",
                ExistingPeriodicWorkPolicy.KEEP,
                request
        );

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

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            am.setAndAllowWhileIdle(AlarmManager.RTC, time, pi);
        } else {
            am.set(AlarmManager.RTC, time, pi);
        }

//        Toast.makeText(context, "Alarm is set", Toast.LENGTH_SHORT).show();
    }


}
