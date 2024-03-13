package com.moverbol.custom.dialogs;


import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.moverbol.R;


/**
 * Created by AkashM on 10/4/18.
 */
public class WebViewDialog extends BaseDialogFragment {


    private WebView mWebView;
    private ProgressBar mProgressBar;
    private WebSettings mWebSettings;
    private String urlToLoad;

    private WebAppInterface webAppInterFace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_webview, container, true);

        mWebView = rootView.findViewById(R.id.webview);
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setAllowFileAccess(true);


        mWebView.addJavascriptInterface(new WebAppInterFaceClass(), "Android");

        if (urlToLoad != null) {
            mWebView.loadUrl(urlToLoad);
        }

//        mWebView.loadUrl("https://www.google.co.in/");
//        mWebView.loadUrl("file:///android_asset/spreedly_page.html");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                mProgressBar.setVisibility(View.INVISIBLE);
                super.onPageCommitVisible(view, url);
            }

           /* @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }*/

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                String message = getString(R.string.ssl_certificate_error);
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = getString(R.string.the_certificate_authority_is_not_trusted);
                        break;
                    case SslError.SSL_EXPIRED:
                        message = getString(R.string.the_certificate_has_expired);
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = getString(R.string.the_certificate_host_name_mismatch);
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = getString(R.string.the_certificate_is_not_yet_valid);
                        break;
                }
                message += getString(R.string.do_you_want_to_continue_anyway);

                builder.setTitle(R.string.ssl_certificate_error);
                builder.setMessage(message);
                builder.setPositiveButton(R.string.Continue, (dialog, which) -> handler.proceed());
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> handler.cancel());
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        return rootView;
    }


    public void showWithActionListener(@NonNull FragmentManager fragmentManager, @NonNull String url, boolean dialogCancelable, WebAppInterface webAppInterFace) {
        this.webAppInterFace = webAppInterFace;
        this.urlToLoad = url;
        this.setCancelable(dialogCancelable);
        show(fragmentManager, "dialog");
    }


    public interface WebAppInterface {
        String getEnvironmentKey();

        String getCompanyName();

        String getPayableAmount();

        void onDialogClosed();

        void onPaymentTokenGenerated(String fullName, String month, String year, String token);
    }

    private class WebAppInterFaceClass {

        @JavascriptInterface
        public String getEnvironmentKey() {
            if (webAppInterFace != null) {
                return webAppInterFace.getEnvironmentKey();
            }

            return "";
        }

        @JavascriptInterface
        public String getCompanyName() {
            if (webAppInterFace != null) {
                return webAppInterFace.getCompanyName();
            }
            return "";
        }

        @JavascriptInterface
        public String getPayableAmmount() {
            if (webAppInterFace != null) {
                return webAppInterFace.getPayableAmount();
            }
            return "$50000";
        }

        @JavascriptInterface
        public void onSpreedlyExoressClosed() {
            WebViewDialog.this.dismiss();
            if (webAppInterFace != null) {
                webAppInterFace.onDialogClosed();
            }
        }

        @JavascriptInterface
        public void onSpreedlyPamentMethod(String fullName, String month, String year, String token) {
            if (webAppInterFace != null) {
                webAppInterFace.onPaymentTokenGenerated(fullName, month, year, token);
            }
        }

        @JavascriptInterface
        public void onSpreedlyLoad(){

            if(WebViewDialog.this.getActivity()!=null) {
                WebViewDialog.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebViewDialog.this.mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }
}
