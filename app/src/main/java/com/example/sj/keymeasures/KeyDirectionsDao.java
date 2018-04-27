package com.example.sj.keymeasures;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface KeyDirectionsDao {
    @Query("SELECT * FROM keyDirections")
    List<KeyDirections> getAll();

    @Query("SELECT id FROM keyDirections")
    List<Integer> getAllIds();

    @Query("SELECT activity_name FROM keyDirections WHERE id = :aid")
    String getNameById( Integer aid ) ;

    @Query("SELECT id FROM keyDirections WHERE activity_name = :name")
    Integer getIdByName( String name ) ;

    @Query("SELECT activity_name from KeyDirections where id = :id ")
    String findById(Integer id) ;

    @Query("SELECT * FROM keyDirections WHERE id IN (:aIDs)")
    List<KeyDirections> loadAllByIds( int [] aIDs ) ;

    @Query("SELECT * FROM keydirections WHERE activity_name LIKE :nm")
    KeyDirections findByName( String nm ) ;

    @Insert
    void insertAll(KeyDirections ...keyDirections);

    @Delete
    void delete(KeyDirections keyDirections);

}

