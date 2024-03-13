package com.moverbol.database;

import androidx.lifecycle.LiveData;
/*import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;*/


/**
 * Created by AkashM on 27/11/17.
 */


//@Dao
public interface JobDao {

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<JobPojo> jobPojoList);


    *//*@Insert()
    void bulkInsertOne(JobPojo jobPojo);

    *//**//*WHERE jobStatus IN (0,1)*//*
    @Query("SELECT * FROM job WHERE jobStatus IN (0,1)")
    List<JobPojo> getNewJobList();*/


}