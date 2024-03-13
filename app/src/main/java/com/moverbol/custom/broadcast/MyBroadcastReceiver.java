package com.moverbol.custom.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;

import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.util.MoversPreferences;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private AppCompatActivity mActivityToShowAlert;

    public MyBroadcastReceiver(AppCompatActivity mActivityToShowAlert) {
        this.mActivityToShowAlert = mActivityToShowAlert;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (Constants.MY_BROADCAST_ACTION.equals(action)) {

            String deletedJobId = intent.getStringExtra(Constants.EXTRA_JOB_ID_KEY);
            if (deletedJobId != null) {
                if (TextUtils.equals(MoversPreferences.getInstance(mActivityToShowAlert).getCurrentJobId(), deletedJobId)) {

                    AlertDialog alertDialog = new AlertDialog.Builder(mActivityToShowAlert, R.style.AppTheme_AlertDialogTheme)
                            .setCancelable(false)
                            .setTitle("Job Closed")
                            .setMessage("This job has been closed by server. You can no longer access this. Please go back to job listing.")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(mActivityToShowAlert, HomeActivity.class);
//                            intent.putExtra(Constants.EXTRA_NOTIFICATION_INTENT, true);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mActivityToShowAlert.startActivity(intent);
                                }
                            })
                            .create();

                    alertDialog.show();
                }
            }

        } else if (Constants.MY_BROADCAST_ACTION_FOR_BOL_APPROVAL.equals(action)) {
            String jobId = intent.getStringExtra(Constants.EXTRA_JOB_ID_KEY);
            if (MoversPreferences.getInstance(mActivityToShowAlert).getCurrentJobId().equals(jobId)) {
                if (mActivityToShowAlert.getSupportActionBar() == null ||
                        mActivityToShowAlert.getSupportActionBar().getTitle() == null) {
                    return;
                }

                String activityTitle = mActivityToShowAlert.getSupportActionBar().getTitle().toString();

                if (activityTitle.equals(mActivityToShowAlert.getString(R.string.bill_of_lading)))
                    mActivityToShowAlert.recreate();
            }
        }
    }
}
