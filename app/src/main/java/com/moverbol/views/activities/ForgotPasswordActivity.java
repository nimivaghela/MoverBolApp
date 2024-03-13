package com.moverbol.views.activities;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.moverbol.R;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.ForgotPasswordBinding;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.viewmodels.LoginViewModel;
import com.moverbol.util.Util;

import retrofit2.Call;

import static com.moverbol.constants.Constants.UNAUTHENTICATED;

/**
 * Created by Mitesh on 20-07-2017.
 */

public class ForgotPasswordActivity extends BaseAppCompactActivity implements View.OnClickListener {

    private ForgotPasswordBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password_new);
        binding.ivBack.setOnClickListener(this);
        loginViewModel = new LoginViewModel();
    }

    @Override
    public void onClick(View view) {
        if (view == binding.ivBack) {
            finish();
        }
    }
    public void sendMail(View view)
    {
        String companyId = binding.editText.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();

        if(validate(companyId, email)) {
            if(!shouldMakeNetworkCall(binding.getRoot())){
                return;
            }
            showProgress();

            if(!shouldMakeNetworkCall(binding.getRoot())){
                return;
            }
            Util.hideSoftKeyboard(this);
            loginViewModel.callForgotPasswordApi(companyId, email, new ResponseListener<BaseResponseModel>() {
                @Override
                public void onResponse(BaseResponseModel response, String usedUrlKey) {
                    hideProgress();
                    if(response.getResponseCode() == 200){
                        showDialog();
                    }else if(response.getStatus().equals(UNAUTHENTICATED)){
//                        logoutDueToUnauthentication(false);

                        Util.logoutDueToUnauthentication(ForgotPasswordActivity.this, false);
                    } else {
                        String errorMessage = "";
                        if(response.getMessage()!=null){
                            errorMessage = response.getMessage();
                        }
                        Snackbar.make(binding.getRoot(), ForgotPasswordActivity.this.getText(R.string.failed) + errorMessage, Snackbar.LENGTH_LONG).show();
//                        Snackbar.make(binding.getRoot(), ForgotPasswordActivity.this.getText(R.string.failed) + response.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                    hideProgress();
                    if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                        Util.showLoginErrorDialog(ForgotPasswordActivity.this);
                        return;
                    }
                    Snackbar.make(binding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                    hideProgress();
                    Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setCancelable(true)
                .setMessage(R.string.forgot_password_dialog_message)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);*/
                        finish();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validate(String companyId, String email) {
        if(TextUtils.isEmpty(companyId)){
            binding.editText.setError(getString(R.string.empty_field_error));
            return false;
        }else if (TextUtils.isEmpty(email)){
            binding.edtEmail.setError(getString(R.string.empty_field_error));
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.setError(getString(R.string.invalid_email_error));
            return false;
        }
        return true;
    }

    /*private void logoutDueToUnauthentication() {
        MoversPreferences.getInstance(this).loggedOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "Hey your session has expired! Looks like you logged in from another device", Toast.LENGTH_LONG).show();
    }*/

}
