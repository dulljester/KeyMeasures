package com.example.sj.keymeasures;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * KeyDirection entity; followed the official Android Developer guide
 */
@Entity
public class KeyDirections implements Comparable<KeyDirections> {
    @Override
    public boolean equals( Object other ) {
        if ( !(other instanceof KeyDirections) )
            return false ;
        KeyDirections x= (KeyDirections)other;
        return this.compareTo(x) == 0;
    }
    @Override
    public int hashCode() {
        return activityName.hashCode();
    }
    @Override
    public int compareTo( KeyDirections other ) {
        return activityName.compareTo(other.activityName);
    }
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="activity_name")
    private String activityName;

    public int getId() {
        return id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setId(int id) {
        this.id= id;
    }

    public void setActivityName( String activityName ) {
        this.activityName= activityName;
    }
}

