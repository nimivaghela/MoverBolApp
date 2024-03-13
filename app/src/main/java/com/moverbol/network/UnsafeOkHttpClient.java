package com.moverbol.network;

import com.moverbol.BuildConfig;
import com.moverbol.constants.Constants;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Admin on 06-11-2017.
 */

public class UnsafeOkHttpClient {

    public static String deviceId;
    public static String userId;

    public static OkHttpClient getUnsafeOkHttpClient(/*final String deviceId, final String userId*/) {


        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectionSpecs(Collections.singletonList(ConnectionSpec.COMPATIBLE_TLS));
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request original = chain.request();
                    // Customize the request
                    Request.Builder requestBuilder = original.newBuilder()
                            .method(original.method(), original.body());
                    requestBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    requestBuilder.header("X-DEVICE-TYPE", Constants.DeviceTypeIds.ANDROID_ID);
                    if (deviceId != null) {
//                        requestBuilder.addHeader("X-DEVICE-ID", deviceId);
                        requestBuilder.header("X-DEVICE-ID", deviceId);
                    }
                    if (userId != null) {
//                        requestBuilder.addHeader("X-USER-ID", userId);
                        requestBuilder.header("X-USER-ID", userId);
                    }

                    requestBuilder.header("X-VERSION-ID", String.valueOf(BuildConfig.VERSION_CODE));



               /* if (apiToken != null) {
                    requestBuilder.addHeader(HEADER_API_TOKEN, apiToken);
                }*/
                /*if (XAPIKEY.equals("")) {
                    requestBuilder.addHeader("X-API-KEY", APPKEY);
                } else {
                    requestBuilder.addHeader("X-API-KEY", XAPIKEY);
                }*/


                    return chain.proceed(requestBuilder.build());
                }
            });


            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(logging);
            }

            builder.readTimeout(2, TimeUnit.MINUTES);
            builder.connectTimeout(2, TimeUnit.MINUTES);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

