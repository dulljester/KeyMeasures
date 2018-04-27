package com.example.sj.keymeasures;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.TypeConverter;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * a non-trivial select with grouping from WorkSessions table
 * returns certain rows, so we need to write a custom POJO to
 * convert the Cursor returned into usable form
 * Our POJO has also to implement Parceable in order for it
 * to be sent via Message
 * It also implements Comparable for testing purposes
 */
public class Converter01 {

    public static class Model01 implements Parcelable, Comparable<Model01> {
        @Override
        public int compareTo( Model01 other ) {
            return activityName.compareTo(other.getActivityName());
        }
        @ColumnInfo(name="activity_name")
        String activityName;
        @ColumnInfo(name="sum")
        Double timeInvested;
        public Model01() {};
        public Model01( String a, Double t ) {
            setTimeInvested(t);
            setActivityName(a);
        }
        public Double getTimeInvested() {
            return timeInvested;
        }
        public void setTimeInvested(Double timeInvested) {
            this.timeInvested = timeInvested;
        }
        public String getActivityName() {
            return activityName;
        }
        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public static final Parcelable.Creator<Model01> CREATOR
                = new Parcelable.Creator<Model01>() {
            public Model01 createFromParcel(Parcel in) {
                return new Model01(in);
            }
            public Model01[] newArray(int size) {
                return new Model01[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeDouble(timeInvested);
            parcel.writeString(activityName);
        }
        private Model01( Parcel p ) {
            timeInvested= p.readDouble();
            activityName= p.readString();
        }
    }
    @TypeConverter
    public static List<Model01> cursor2List( Cursor cursor ) {
        List<Model01> lst= new ArrayList<>();
        cursor.moveToFirst();
        while ( !cursor.isAfterLast() ) {
            double tm = (double) cursor.getInt(2);
            lst.add(new Model01(cursor.getString(1),cursor.getDouble(2)));
            cursor.moveToNext();
        }
        return lst;
    }
}

