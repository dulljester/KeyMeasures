package com.example.sj.keymeasures;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * AppDatabase written following the official documentation and Singleton design pattern
 */
@Database(entities = {KeyDirections.class,WorkSession.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
   private static AppDatabase INSTANCE;
   public static AppDatabase getInstance( Context context ) {
        if ( INSTANCE == null )
            INSTANCE= Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "my_db"
            ).build();
        return INSTANCE;
   }
   public static void destroyInstance() {
       INSTANCE= null;
   }

    /**
     * there are two tables: the one storing Directions, the other storing work sessions
     * @return
     */
   public abstract KeyDirectionsDao keyDirectionsDao();
   public abstract WorkSessionDao workSessionDao();
}

