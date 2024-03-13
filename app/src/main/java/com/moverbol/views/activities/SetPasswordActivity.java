package com.moverbol.views.activities;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.activities.BaseAppCompactActivity;
import com.moverbol.databinding.SetPasswordBindingNew;
import com.moverbol.network.ResponseListener;
import com.moverbol.network.model.BaseResponseModel;
import com.moverbol.network.viewmodels.LoginViewModel;
import com.moverbol.util.MoversPreferences;
import com.moverbol.util.Util;

import retrofit2.Call;

import static com.moverbol.constants.Constants.UNAUTHENTICATED;

/**
 * Created by Mitesh on 20-07-2017.
 * <p>
 * Calls callSetPasswordApi(demo, email_id(From Preferences), password);
 */

public class SetPasswordActivity extends BaseAppCompactActivity {
    private SetPasswordBindingNew setPasswordBinding;
    private LoginViewModel loginViewModel;
    private boolean mCalledFromHome = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_password_new);
        loginViewModel = new LoginViewModel();

        if(getIntent()!=null && getIntent().hasExtra(Constants.EXTRA_CALLED_FROM_HOME)){
            mCalledFromHome = getIntent().getBooleanExtra(Constants.EXTRA_CALLED_FROM_HOME, false);
        }

        if(mCalledFromHome){
            setPasswordBinding.txtSkip.setVisibility(View.GONE);
        }
    }

    public void setPassword(View view) {
        String newPassword = setPasswordBinding.edtNewPass.getText().toString().trim();
        String confirmPassword = setPasswordBinding.edtConfirmPass.getText().toString().trim();
        if(validate(newPassword, confirmPassword)){
            if(!shouldMakeNetworkCall(setPasswordBinding.getRoot())){
                return;
            }
            showProgress();
            loginViewModel.callSetPasswordApi(MoversPreferences.getInstance(SetPasswordActivity.this).getSubdomain(), MoversPreferences.getInstance(this).getUserId(), confirmPassword,
                    new ResponseListener<BaseResponseModel>() {
                        @Override
                        public void onResponse(BaseResponseModel response, String usedUrlKey) {
                            hideProgress();
                            if(response.getResponseCode() == 200){
                                Intent intent = new Intent(SetPasswordActivity.this,HomeActivity.class);
                                if(!mCalledFromHome) {
                                    startActivity(intent);
                                }
                                finish();
                            }else if(response.getStatus().equals(UNAUTHENTICATED)){
//                                logoutDueToUnauthentication(true);

                                Util.logoutDueToUnauthentication(SetPasswordActivity.this, false);

                            } else {
                                String errorMessage = "";
                                if(response.getMessage()!=null){
                                    errorMessage = response.getMessage();
                                }
                                Snackbar.make(setPasswordBinding.getRoot(), SetPasswordActivity.this.getText(R.string.set_password_failed) + errorMessage, Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(BaseResponseModel serverResponseError, String usedUrlKey) {
                            hideProgress();

                            if(serverResponseError.getMessage().contains(getString(R.string.login_error_response_message))){
                                Util.showLoginErrorDialog(SetPasswordActivity.this);
                                return;
                            }

                            Snackbar.make(setPasswordBinding.getRoot(), serverResponseError.getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<BaseResponseModel> call, Throwable t, String message) {
                            hideProgress();
                            Snackbar.make(setPasswordBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
                        }
                    });
        }

    }

    public void onSkipClicked(View view) {
        Intent intent = new Intent(SetPasswordActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validate(String newPassword, String confirmPassword) {
        if(TextUtils.isEmpty(newPassword)){
            setPasswordBinding.edtNewPass.requestFocus();
            setPasswordBinding.edtNewPass.setError(getString(R.string.empty_field_error));
            return false;
        }else if (TextUtils.isEmpty(confirmPassword)){
            setPasswordBinding.edtConfirmPass.requestFocus();
            setPasswordBinding.edtConfirmPass.setError(getString(R.string.empty_field_error));
            return false;
        }else if (!TextUtils.equals(newPassword, confirmPassword)){
            setPasswordBinding.edtConfirmPass.requestFocus();
            setPasswordBinding.edtConfirmPass.setError(getString(R.string.error_passwords_not_matched));
            return false;
        }

        return true;
    }

    public void back(View view) {
        onBackPressed();
    }
}
