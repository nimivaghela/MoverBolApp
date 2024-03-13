package com.moverbol.views.activities.moveprocess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.moverbol.R;
import com.moverbol.custom.activities.BaseAppCompactActivity;

public class SpreedlyExpressInWebviewActivity extends BaseAppCompactActivity {

    public static final String EXTRA_CUSTOMER_NAME = "customer_name";
    private static final String EXTRA_AMOUNT_TO_PAY = "amount_to_pay";
    private static final String EXTRA_EXPIRY_MONTH = "expiry_month";
    private static final String EXTRA_EXPIRY_YEAR = "expiry_year";
    private static final String EXTRA_SPREEDLY_TOKEN = "spreedly_token";
    private WebView mWebView;
    private WebSettings mWebSettings;

    private static OnPaymentReceivedListener onPaymentReceivedListener;
    private static WebAppJavaScriptInterface webAppJavaScriptInterface;

    public interface OnPaymentReceivedListener {
        void onPaymentReceived(String fullName, String month, String year, String token);
    }

    public interface WebAppJavaScriptInterface{
        @JavascriptInterface
        String getEnvironmentKey();

        @JavascriptInterface
        String getPayableAmmount();

        @JavascriptInterface
        void onSpreedlyExpressClosed();

        @JavascriptInterface
        void onSpreedlyPamentMethod(String fullName, String month, String year, String token);
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, SpreedlyExpressInWebviewActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }

    public static void startForResult(Activity activity, int requestCode, String amountToPay, WebAppJavaScriptInterface webAppJavaScriptInterface) {
        Intent starter = new Intent(activity, SpreedlyExpressInWebviewActivity.class);
        starter.putExtra(EXTRA_AMOUNT_TO_PAY, amountToPay);

        SpreedlyExpressInWebviewActivity.webAppJavaScriptInterface = webAppJavaScriptInterface;

        activity.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spreedly_express_in_webview);

        initialise();
        setIntentData();
        setActionListeners();
    }

    private void initialise() {
        mWebView = findViewById(R.id.webview);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(webAppJavaScriptInterface, "Android");
        mWebView.loadUrl("file:///android_asset/spreedly_page.html");
//        mWebView.loadUrl("file:///android_asset/Spreedly_Frame_Page/spreedly_frame_page.html");

//        mWebView.loadUrl("https://www.google.co.in/");
    }

    private void setIntentData() {

    }

    private void setActionListeners() {

    }

    public class WebAppInterFace {

        Context context;

        public WebAppInterFace(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void showToast(String toastMessage) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void showToast() {
            Toast.makeText(context, "Toast Called", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void showToast(String fullName, String month, String year, String token) {
            Toast.makeText(context, fullName + " " + month + " " + year + " " + token, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public String getEnvironmentKey() {
            return "Akash";
        }

        @JavascriptInterface
        public String getPayableAmmount() {
            return "$50000";
        }

        @JavascriptInterface
        public void onSpreedlyExoressClosed() {
            finish();
//            Toast.makeText(context, "Close", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void onSpreedlyPamentMethod(String fullName, String month, String year, String token) {
//            Toast.makeText(context, fullName + " " + month + " " + year + " " + token, Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent();
            intent.putExtra(EXTRA_CUSTOMER_NAME, fullName);
            intent.putExtra(EXTRA_EXPIRY_MONTH, month);
            intent.putExtra(EXTRA_EXPIRY_YEAR, year);
            intent.putExtra(EXTRA_SPREEDLY_TOKEN, token);
            setResult(RESULT_OK, intent);
            finish();*/

            onPaymentReceivedListener.onPaymentReceived(fullName, month, year, token);
        }
    }


}
