package com.moverbol.network;

import com.moverbol.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 17-10-2017.
 */

public class RetrofitClient {
    private static RetrofitClient clientInstance;
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit retrofit;
    private ApiEndPoints apiEndPoints;
    private HeaderInterceptor headerInterceptor = new HeaderInterceptor();
    private HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    // private OkHttpClient httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    private RetrofitClient() {
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        httpClient.readTimeout(3, TimeUnit.MINUTES);
        httpClient.retryOnConnectionFailure(true);
        addInterceptors();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        apiEndPoints = retrofit.create(ApiEndPoints.class);
    }

    public static RetrofitClient getInstance() {
        if (clientInstance == null) {
            try {
                clientInstance = new RetrofitClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clientInstance;
    }

    public static void clearInstance() {
        clientInstance = null;
    }

    public static RetrofitClient getInstanceForHeaderLessCalls() {
        if (clientInstance == null) {
            try {
                clientInstance = new RetrofitClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clientInstance;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }


    public ApiEndPoints getApiInterface() {
        return apiEndPoints;
    }

    private void addInterceptors() {
        if (!httpClient.interceptors().contains(headerInterceptor)) {
            httpClient.addInterceptor(headerInterceptor);
        }

        if (BuildConfig.DEBUG && !httpClient.networkInterceptors().contains(loggingInterceptor)) {
            httpClient.addNetworkInterceptor(loggingInterceptor);
        }
    }
}
