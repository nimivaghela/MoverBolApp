package com.moverbol.network;

import androidx.annotation.NonNull;

import com.moverbol.BuildConfig;
import com.moverbol.constants.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    public static String deviceId;
    public static String userId;

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        // Customize the request
        Request.Builder requestBuilder = original.newBuilder()
                .method(original.method(), original.body());
        requestBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded");
        requestBuilder.addHeader("Connection", "close");
        requestBuilder.header("X-DEVICE-TYPE", Constants.DeviceTypeIds.ANDROID_ID);
        if (deviceId != null) {
            requestBuilder.header("X-DEVICE-ID", deviceId);
        }
        if (userId != null) {
            requestBuilder.header("X-USER-ID", userId);
        }

        requestBuilder.header("X-VERSION-ID", String.valueOf(BuildConfig.VERSION_CODE));


        return chain.proceed(requestBuilder.build());
    }
}
