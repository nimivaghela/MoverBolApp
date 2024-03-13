package com.moverbol.services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.moverbol.HomeActivity;
import com.moverbol.R;
import com.moverbol.constants.Constants;
import com.moverbol.util.MoversPreferences;
import com.moverbol.views.activities.jobsummary.MoveProcessActivity;
import com.moverbol.views.activities.moveprocess.NotesActivity;
import com.moverbol.views.activities.moveprocess.bill_of_lading.BillOfLadingActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AkashM on 18/4/18.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String KEY_NOTIFICATION_SOUND = "sound";
    private static final String KEY_NOTIFICATION_TITLE = "title";
    private static final String KEY_NOTIFICATION_DESCRIPTION = "description";
    private static final String KEY_NOTIFICATION_VIBRATE = "vibrate";
    private static final String KEY_NOTIFICATION_MESSAGE = "message";
    private static final String KEY_NOTIFICATION_OPPORTUNITY_ID = "opportunity_id";
    private static final String KEY_NOTIFICATION_DATA = "data";
    private static final String KEY_NOTIFICATION_JOB_ID = "job_id";
    private static final String KEY_NOTIFICATION_TYPE = "notification_type";
    private static final String KEY_NOTIFICATION_DATA_BOL_STATUS = "bol_status";
    private static final int NOTIFICATION_TYPE_BOL_APPROVAL = 1;
    private static final int NOTIFICATION_TYPE_NEW_NOTE = 3;
    private static final int NOTIFICATION_TYPE_NEW_JOB = 2;
    private static final int NOTIFICATION_TYPE_JOB_DELETED = 4;
    private static final int NOTIFICATION_TYPE_ESTIMATE_AND_BOL_CHANGED = 5;
    private final String TAG = Constants.BASE_LOG_TAG + MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        MoversPreferences.getInstance(this).setFirebaseToken(s);
        Log.d(Constants.BASE_LOG_TAG + "FireBaseToken", "Refreshed token: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // ...
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        int notificationCount = MoversPreferences.getInstance(getApplicationContext()).getNotificationCount();
        MoversPreferences.getInstance(getApplicationContext()).setNotificationCount(notificationCount + 1);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            try {
                sendNotification(remoteMessage);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "JSON Parsing error: " + e.getLocalizedMessage());
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) throws JSONException {

        String sound = remoteMessage.getData().get(KEY_NOTIFICATION_SOUND);
        boolean shouldHaveSound = TextUtils.equals(sound, "1");
        String vibration = remoteMessage.getData().get(KEY_NOTIFICATION_VIBRATE);
        boolean shouldHaveVibration = TextUtils.equals(vibration, "1");
        String title = remoteMessage.getData().get(KEY_NOTIFICATION_TITLE);
        JSONObject jsonObject = new JSONObject(remoteMessage.getData().get(KEY_NOTIFICATION_MESSAGE));
        String description = remoteMessage.getData().get(KEY_NOTIFICATION_DESCRIPTION);


        if (!TextUtils.isEmpty(jsonObject.getString(KEY_NOTIFICATION_TYPE)) && TextUtils.isDigitsOnly(jsonObject.getString(KEY_NOTIFICATION_TYPE))) {
            int notificationType = Integer.parseInt(jsonObject.getString(KEY_NOTIFICATION_TYPE));

            switch (notificationType) {
                case NOTIFICATION_TYPE_BOL_APPROVAL:
                    createBolApprovalNotification(title, description, jsonObject, shouldHaveSound, shouldHaveVibration);
                    break;
                case NOTIFICATION_TYPE_NEW_JOB:
                    createNewJobNotification(title, description, jsonObject, shouldHaveSound, shouldHaveVibration);
                    break;
                case NOTIFICATION_TYPE_NEW_NOTE:
                    createNewNoteNotification(title, description, jsonObject, shouldHaveSound, shouldHaveVibration);
                    break;
                case NOTIFICATION_TYPE_JOB_DELETED:
                    createJobDeletedNotification(title, description, jsonObject, shouldHaveSound, shouldHaveVibration);
                    break;
                case NOTIFICATION_TYPE_ESTIMATE_AND_BOL_CHANGED:
                    createEstimateAndBolChangedNotification(title, description, jsonObject, shouldHaveSound, shouldHaveVibration);
                    break;

                default:
                    break;
            }
        }
    }


    private void createBolApprovalNotification(String title, String description, JSONObject jsonObject, boolean shouldHaveSound, boolean shouldHaveVibration) throws JSONException {

        String channelId = "channel_id_1";

        int notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_BOL_APPROVAL);

        MoversPreferences.getInstance(getApplicationContext()).
                setNotificationId(NOTIFICATION_TYPE_BOL_APPROVAL,
                        notificationId + NOTIFICATION_TYPE_BOL_APPROVAL);

        notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_BOL_APPROVAL);

        Intent intent = new Intent(this, BillOfLadingActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(Constants.EXTRA_NOTIFICATION_INTENT, true);
        intent.putExtra(BillOfLadingActivity.EXTRA_BOL_APPROVAL_STATUS, jsonObject.getString(KEY_NOTIFICATION_DATA_BOL_STATUS));
        intent.putExtra(Constants.EXTRA_JOB_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_JOB_ID));
        intent.putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_OPPORTUNITY_ID));

        String jobId = jsonObject.getString(KEY_NOTIFICATION_JOB_ID);

        MoversPreferences.getInstance(getApplicationContext())
                .setCurrentJobId(jobId);
        MoversPreferences.getInstance(getApplicationContext())
                .setOpportunityId(jsonObject.getString(KEY_NOTIFICATION_OPPORTUNITY_ID));


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);


        NotificationCompat.Builder notificationBuilder = null;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "BOL Approval Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notifies if the submitted BOL has been approved or not");
            notificationChannel.enableLights(false);
//            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setBypassDnd(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //   String contentText = "BOL has been rejected for Job id:" + jsonObject.getString(KEY_NOTIFICATION_JOB_ID);
        MoversPreferences.getInstance(getApplicationContext()).setCurrentJobBolStatus(jobId, Constants.BolStatus.REJECTED);
        if (TextUtils.equals(jsonObject.getString(KEY_NOTIFICATION_DATA_BOL_STATUS).toLowerCase(), BillOfLadingActivity.BOL_APPROVAL_STATUS_ACCEPTED.toLowerCase())) {
            //     contentText = "BOL has been approved for Job id:" + jsonObject.getString(KEY_NOTIFICATION_JOB_ID);
            MoversPreferences.getInstance(getApplicationContext()).setCurrentJobBolStatus(jobId, Constants.BolStatus.APPROVED);
        }


        notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_bus_green)
//                .setSmallIcon(R.drawable.notification_icon)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(description)
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(AppUtil.getBitmapFromURL(this, pushData.image)))*/
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        } else {
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        if (shouldHaveSound) {
            notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        if (shouldHaveVibration) {
            notificationBuilder.setVibrate(new long[]{1000, 1000});
        }


        //Send Broadcast
        Intent broadcastIntent = new Intent(Constants.MY_BROADCAST_ACTION_FOR_BOL_APPROVAL);
        broadcastIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_JOB_ID));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void createJobDeletedNotification(String title, String description, JSONObject jsonObject, boolean shouldHaveSound, boolean shouldHaveVibration) throws JSONException {
        String channelId = "channel_id_2";
        int notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_JOB_DELETED);

        MoversPreferences.getInstance(getApplicationContext()).
                setNotificationId(NOTIFICATION_TYPE_JOB_DELETED,
                        notificationId + NOTIFICATION_TYPE_JOB_DELETED);

        notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_JOB_DELETED);

        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra(Constants.EXTRA_NOTIFICATION_INTENT, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        /*intent.putExtra(BillOfLadingActivity.EXTRA_BOL_APPROVAL_STATUS, jsonObject.getJSONObject(KEY_NOTIFICATION_DATA).getString(KEY_NOTIFICATION_DATA_BOL_STATUS));
        intent.putExtra(Constants.EXTRA_JOB_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_JOB_ID));
        intent.putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_OPPORTUNITY_ID));*/

        /*TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);*/

        NotificationCompat.Builder notificationBuilder = null;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "Job deleted Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notifies when a job gets deleted from server");
            notificationChannel.enableLights(false);
//            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setBypassDnd(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // String contentText = "Job id:" + jsonObject.getString(KEY_NOTIFICATION_JOB_ID) + " has been deleted";

        notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_bus_green)
//                .setSmallIcon(R.drawable.notification_icon)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(description)
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(AppUtil.getBitmapFromURL(this, pushData.image)))*/
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        } else {
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        if (shouldHaveSound) {
            notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        if (shouldHaveVibration) {
            notificationBuilder.setVibrate(new long[]{1000, 1000});
        }

        //Set job id is deleted in preference
        MoversPreferences.getInstance(this).setIsJobDeleted(jsonObject.getString(KEY_NOTIFICATION_JOB_ID), true);

        //Send Broadcast
        Intent broadcastIntent = new Intent(Constants.MY_BROADCAST_ACTION);
        broadcastIntent.putExtra(Constants.EXTRA_JOB_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_JOB_ID));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);


        notificationManager.notify(notificationId, notificationBuilder.build());

        /*Intent broadcastIntent = new Intent(this, MyCustomBroadcastReceiver.class);
        broadcastIntent.setAction("com.moverbol.MOVER_BOL_JOB_DELETED_BROAD_CAST");
        broadcastIntent.putExtra(Constants.EXTRA_JOB_ID_KEY,jsonObject.getString(KEY_NOTIFICATION_JOB_ID));
        getApplicationContext().sendBroadcast(broadcastIntent);*/
    }


    private void createNewJobNotification(String title, String description, JSONObject jsonObject, boolean shouldHaveSound, boolean shouldHaveVibration) throws JSONException {
        String channelId = "channel_id_3";
        int notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_NEW_JOB);

        MoversPreferences.getInstance(getApplicationContext()).
                setNotificationId(NOTIFICATION_TYPE_NEW_JOB,
                        notificationId + NOTIFICATION_TYPE_NEW_JOB);

        notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_NEW_JOB);

        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra(Constants.EXTRA_NOTIFICATION_INTENT, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        /*intent.putExtra(BillOfLadingActivity.EXTRA_BOL_APPROVAL_STATUS, jsonObject.getJSONObject(KEY_NOTIFICATION_DATA).getString(KEY_NOTIFICATION_DATA_BOL_STATUS));
        intent.putExtra(Constants.EXTRA_JOB_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_JOB_ID));
        intent.putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_OPPORTUNITY_ID));*/

        /*TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);*/


        NotificationCompat.Builder notificationBuilder = null;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "New Job Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notifies when a new job is assigned");
            notificationChannel.enableLights(false);
//            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setBypassDnd(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // String contentText = "A new Job has been assigned to you. Job id:" + jsonObject.getString(KEY_NOTIFICATION_JOB_ID);

        notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_bus_green)
//                .setSmallIcon(R.drawable.notification_icon)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(description)
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(AppUtil.getBitmapFromURL(this, pushData.image)))*/
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        } else {
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        if (shouldHaveSound) {
            notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        if (shouldHaveVibration) {
            notificationBuilder.setVibrate(new long[]{1000, 1000});
        }

        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    private void createNewNoteNotification(String title, String description, JSONObject jsonObject, boolean shouldHaveSound, boolean shouldHaveVibration) throws JSONException {
        String channelId = "channel_id_4";
        int notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_NEW_NOTE);

        MoversPreferences.getInstance(getApplicationContext()).
                setNotificationId(NOTIFICATION_TYPE_NEW_NOTE,
                        notificationId + NOTIFICATION_TYPE_NEW_NOTE);

        notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_NEW_NOTE);


        String opportunityId = jsonObject.getString(KEY_NOTIFICATION_OPPORTUNITY_ID);
        String jobId = jsonObject.getString(KEY_NOTIFICATION_JOB_ID);

        Intent intent = new Intent(this, NotesActivity.class);

        intent.putExtra(Constants.EXTRA_NOTIFICATION_INTENT, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MoversPreferences.getInstance(getApplicationContext()).setOpportunityId(opportunityId);
        MoversPreferences.getInstance(getApplicationContext()).setCurrentJobId(jobId);
        /*intent.putExtra(BillOfLadingActivity.EXTRA_BOL_APPROVAL_STATUS, jsonObject.getJSONObject(KEY_NOTIFICATION_DATA).getString(KEY_NOTIFICATION_DATA_BOL_STATUS));
        intent.putExtra(Constants.EXTRA_JOB_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_JOB_ID));
        intent.putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_OPPORTUNITY_ID));*/

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        NotificationCompat.Builder notificationBuilder = null;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "New Note Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notifies when a new note for a job is added");
            notificationChannel.enableLights(false);
//            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setBypassDnd(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //  String contentText = "A new note has been added for Job id: " + jsonObject.getString(KEY_NOTIFICATION_JOB_ID);

        notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_bus_green)
//                .setSmallIcon(R.drawable.notification_icon)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(description)
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(AppUtil.getBitmapFromURL(this, pushData.image)))*/
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        } else {
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        if (shouldHaveSound) {
            notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        if (shouldHaveVibration) {
            notificationBuilder.setVibrate(new long[]{1000, 1000});
        }

        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    private void createEstimateAndBolChangedNotification(String title, String description, JSONObject jsonObject, boolean shouldHaveSound, boolean shouldHaveVibration) throws JSONException {
        String channelId = "channel_id_4";

        int notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_ESTIMATE_AND_BOL_CHANGED);

        MoversPreferences.getInstance(getApplicationContext()).
                setNotificationId(NOTIFICATION_TYPE_ESTIMATE_AND_BOL_CHANGED,
                        notificationId + NOTIFICATION_TYPE_ESTIMATE_AND_BOL_CHANGED);

        notificationId = MoversPreferences.getInstance(getApplicationContext()).getNotificationId(NOTIFICATION_TYPE_ESTIMATE_AND_BOL_CHANGED);

        Intent intent = new Intent(this, MoveProcessActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.EXTRA_NOTIFICATION_INTENT, true);
        intent.putExtra(Constants.EXTRA_JOB_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_JOB_ID));
        intent.putExtra(Constants.EXTRA_OPPORTUNITY_ID_KEY, jsonObject.getString(KEY_NOTIFICATION_OPPORTUNITY_ID));

        String jobId = jsonObject.getString(KEY_NOTIFICATION_JOB_ID);

        MoversPreferences.getInstance(getApplicationContext())
                .setCurrentJobId(jobId);
        MoversPreferences.getInstance(getApplicationContext())
                .setOpportunityId(jsonObject.getString(KEY_NOTIFICATION_OPPORTUNITY_ID));


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);


        NotificationCompat.Builder notificationBuilder = null;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "BOL Estimate and BOL changed notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notifies if the estimate and BOL get's changed from the server.");
            notificationChannel.enableLights(false);
//            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setBypassDnd(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //   String contentText = " Estimate and BOL has been changed for Job id:" + jsonObject.getString(KEY_NOTIFICATION_JOB_ID);


        notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_bus_green)
//                .setSmallIcon(R.drawable.notification_icon)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(description)
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(AppUtil.getBitmapFromURL(this, pushData.image)))*/
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        } else {
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }


        if (shouldHaveSound) {
            notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        if (shouldHaveVibration) {
            notificationBuilder.setVibrate(new long[]{1000, 1000});
        }

        notificationManager.notify(notificationId, notificationBuilder.build());
    }


}

