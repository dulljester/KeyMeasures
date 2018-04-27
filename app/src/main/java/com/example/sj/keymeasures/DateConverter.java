package com.example.sj.keymeasures;

import android.arch.persistence.room.TypeConverter;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

/**
 * We are using LocalDateTime on Java side; it is converted to Long
 * in order to be saved in the database
 * This class converts back and forth between the two formats
 * Note corresponding annotations
 */
public class DateConverter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime toLocalTime( Long timestamp ) {
        return timestamp==null?null:LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),ZoneId.systemDefault());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static Long toTimestamp( LocalDateTime date ) {
        return date==null?null:date.getLong(ChronoField.NANO_OF_SECOND);
    }
}

