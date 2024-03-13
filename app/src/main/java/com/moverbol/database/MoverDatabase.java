package com.moverbol.database;


import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.moverbol.model.ClockEntityModel;

import static com.moverbol.constants.Constants.BASE_LOG_TAG;

/**
 * Created by AkashM on 27/11/17.
 */



/*, exportSchema = false*/

@Database(entities = {ClockEntityModel.class}, version = 1)
public abstract class MoverDatabase extends RoomDatabase {

    private static final String LOG_TAG = BASE_LOG_TAG + "Data";
    private static final String DATABASE_NAME = "mover_bol.db";

    //For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile MoverDatabase sInstance;
//    private static MoverDatabase sInstance;

    public static MoverDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(), MoverDatabase.class,
                        MoverDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }


    public abstract ClockHistoryDao clockHistoryDao();


/*public static MoverDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), MoverDatabase.class,
                    MoverDatabase.DATABASE_NAME).build();
        }
        return sInstance;
    }


    // The associated DAOs for the database
    public abstract JobDao jobDao();*/

}
