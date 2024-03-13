package com.moverbol.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.moverbol.model.ClockEntityModel;

import java.util.List;

@Dao
public interface ClockHistoryDao {

    @Query("SELECT * FROM clockHistory order by occurrenceNum")
    List<ClockEntityModel> getAll();

    @Query("SELECT * FROM clockHistory WHERE jobId = (:jobId)  order by occurrenceNum")
    List<ClockEntityModel> getAllForGivenJob(String jobId);

    @Insert
    void insertAll(ClockEntityModel... clockEntityModel);

    @Delete
    void delete(ClockEntityModel clockEntityModel);

    @Query("DELETE FROM clockHistory")
    void deleteAll();

    @Query("DELETE FROM clockHistory WHERE jobId = (:jobId)")
    void deleteAllForGivenJob(String jobId);

    @Query("SELECT max(occurrenceNum) FROM clockHistory WHERE jobId = (:jobId)")
    int getLastOccurrenceNumForJob(String jobId);

    @Query("SELECT max(breakOccurrenceNum) FROM clockHistory WHERE occurrenceNum = (:clockOccurrenceNum) AND jobId = (:jobId) ;")
    int getLastBreakOccurrenceNum(int clockOccurrenceNum, String jobId);

    @Query("SELECT * FROM clockHistory WHERE jobActivity = (:jobActivityName) AND jobId = (:jobId) order by occurrenceNum")
    List<ClockEntityModel> getAllForGivenJobActivityName(String jobActivityName, String jobId);
}
