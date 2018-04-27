package com.example.sj.keymeasures;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.time.LocalDateTime;

/**
 * The heart of the application: the place that keeps track
 * of the "facts": which activity when took place with what duration
 */
@Entity
public class WorkSession {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "activity_id")
    private int aid;

    @ColumnInfo(name = "_when")
    @TypeConverters(DateConverter.class)
    private LocalDateTime _when;

    @ColumnInfo(name = "duration")
    private int duration;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public int getAid() {
        return aid;
    }

    public void setAid( int aid ) {
        this.aid = aid;
    }

    public LocalDateTime getWhen() {
        return _when;
    }

    public void setWhen( LocalDateTime _when ) {
        this._when = _when;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration( int duration ) {
        this.duration = duration;
    }
}

