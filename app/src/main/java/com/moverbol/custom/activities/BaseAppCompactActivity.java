package com.moverbol.custom.activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.custom.broadcast.MyBroadcastReceiver;
import com.moverbol.util.Util;

/**
 * Created by sumeet on 17/11/15.
 */
public class BaseAppCompactActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private MyBroadcastReceiver myBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;

    private String currentActivityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        alertDialog = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
                .setView(R.layout.dialog_progress)
                .setCancelable(false)
                .create();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        myBroadcastReceiver = new MyBroadcastReceiver(this);

        registerForBroadCast();
    }

    public void setToolbar(Toolbar toolbar, String title, int drawableId) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //getSupportActionBar().setDisplayShowTitleEnabled(true);
            actionBar.setHomeAsUpIndicator(drawableId);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerForBroadCast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        localBroadcastManager.unregisterReceiver(myBroadcastReceiver);
    }

    protected boolean shouldMakeNetworkCall(View viewToShowSnackBar) {
        Util.hideSoftKeyboard(this);

        if (Util.isConnected(this.getApplicationContext())) {
            return true;
        } else {
            Snackbar.make(viewToShowSnackBar, getText(R.string.error_no_connection), Snackbar.LENGTH_LONG).show();
        }
        return false;
    }

    protected void showProgress() {
        if (alertDialog != null && !alertDialog.isShowing() && !isFinishing()) {
            alertDialog.show();
        }

    }

    protected void hideProgress() {
        try {
            if (alertDialog != null && alertDialog.isShowing() && !isFinishing()) {
                alertDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void registerForBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.MY_BROADCAST_ACTION);
        intentFilter.addAction(Constants.MY_BROADCAST_ACTION_FOR_BOL_APPROVAL);

        localBroadcastManager.registerReceiver(myBroadcastReceiver, intentFilter);
    }

    /*protected void logoutDueToUnauthentication(boolean shouldShowMessage) {
        MoversPreferences.getInstance(this).loggedOut();
        MoverDatabase.getInstance(this).clearAllTables();
        Instabug.clearAllUserAttributes();
        Instabug.logoutUser();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if(shouldShowMessage){
            Toast.makeText(this, "Hey your session has expired! Looks like you logged in from another device", Toast.LENGTH_LONG).show();
        }
    }*/

}
