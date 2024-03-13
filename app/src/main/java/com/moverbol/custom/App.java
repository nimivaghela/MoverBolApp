package com.moverbol.custom;

import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDexApplication;

import com.instabug.bug.BugReporting;
import com.instabug.bug.invocation.Option;
import com.instabug.library.Feature;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.instabug.library.ui.onboarding.WelcomeMessage;
import com.moverbol.R;

import net.authorize.Merchant;
import net.authorize.aim.emv.Result;

public class App extends MultiDexApplication {

    private static final String TAG = App.class.getSimpleName();
    public static Merchant merchant;
    public static Result lastTransactionResult;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpInstaBug();

        /*final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(Constants.RemoteConfigKeys.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(Constants.RemoteConfigKeys.KEY_CURRENT_VERSION_NAME_IN_STORE, BuildConfig.VERSION_NAME);
//        remoteConfigDefaults.put(Constants.RemoteConfigKeys.KEY_CURRENT_VERSION_NAME_IN_STORE, "2.0.1");
        remoteConfigDefaults.put(Constants.RemoteConfigKeys.KEY_UPDATE_URL,
                "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);


        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();

        firebaseRemoteConfig.setConfigSettings(configSettings);


        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(0) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();

                            MoversPreferences.getInstance(App.this)
                                    .setIsUpdateRequired(firebaseRemoteConfig.getBoolean(Constants.RemoteConfigKeys.KEY_UPDATE_REQUIRED));

                            MoversPreferences.getInstance(App.this)
                                    .setCurrentVersionNameInStore(firebaseRemoteConfig.getString(Constants.RemoteConfigKeys.KEY_CURRENT_VERSION_NAME_IN_STORE));

                            MoversPreferences.getInstance(App.this)
                                    .setUpdateURL(firebaseRemoteConfig.getString(Constants.RemoteConfigKeys.KEY_UPDATE_URL));
                        }

                        Log.d(TAG, "test");
                    }
                });*/
    }


    private void setUpInstaBug() {
        new Instabug.Builder(this, "9c9d62415b63219e27d31299ccd93fdd")
                .setInvocationEvents(InstabugInvocationEvent.FLOATING_BUTTON)
                .setConsoleLogState(Feature.State.DISABLED)
                .build();
        BugReporting.setReportTypes(BugReporting.ReportType.BUG, BugReporting.ReportType.FEEDBACK);
        BugReporting.setOptions(Option.EMAIL_FIELD_OPTIONAL);

        Instabug.setPrimaryColor(ContextCompat.getColor(this, R.color.colorPrimary));

        Instabug.setWelcomeMessageState(WelcomeMessage.State.LIVE);
    }

}
