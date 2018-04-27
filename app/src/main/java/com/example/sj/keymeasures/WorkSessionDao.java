package com.example.sj.keymeasures;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface WorkSessionDao {
    @Query("SELECT * FROM workSession")
    List<WorkSession> getAll() ;

    @Query("SELECT * FROM workSession WHERE activity_id = (:aids)")
    List<WorkSession> loadAllByActivityIds( int[] aids ) ;

    @Query("SELECT duration FROM workSession WHERE activity_id = :aid and _when >= :startDate")
    @TypeConverters(DateConverter.class)
    List<Integer> loadAllDurationsById( int aid, LocalDateTime startDate ) ;

    @Query("SELECT activity_name, SUM(duration) as sum FROM worksession JOIN keydirections ON worksession.activity_id = keydirections.id WHERE _when >= :startDate GROUP BY activity_name")
    //@TypeConverters(Converter01.class)
    @TypeConverters(DateConverter.class)
    List<Converter01.Model01> summarize( LocalDateTime startDate ) ;

    @Insert
    void insertAll( WorkSession ... workSessions ) ;

    @Delete
    void delete( WorkSession workSession ) ;
}

