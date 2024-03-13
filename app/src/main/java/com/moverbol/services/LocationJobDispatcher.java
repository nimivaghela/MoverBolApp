package com.moverbol.services;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by 28/12/17.
 */

public class LocationJobDispatcher {
    private static FirebaseJobDispatcher dispatcher;


    public static void start(Context mContext) {
        if (dispatcher == null) {
            dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(mContext));
            init();
        }

    }


    private static void init() {
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
//                .setService(LocationService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag("Location Update")
                .setTrigger(Trigger.executionWindow(1, 10 * 60))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setReplaceCurrent(true)
                .build();

        dispatcher.mustSchedule(myJob);

    }

    public static void cancelAllJob() {
        if (dispatcher != null) {
            dispatcher.cancelAll();
        }
    }


}
