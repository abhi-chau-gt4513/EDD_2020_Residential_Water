package com.example.edd_2020_residential_water;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ForkJoinPool;

@Database(entities = {Water.class}, version = 2, exportSchema = false)
public abstract class WaterDatabase extends RoomDatabase {
    public static ForkJoinPool databaseWriteExecutor;

    public abstract WaterDao waterDao();
    public static final String DB_NAME = "water";

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE splashes ADD COLUMN climbSecs INTEGER");
        }
    };

    public static WaterDatabase getDatabase(@NonNull Context context) {
        return Room.databaseBuilder(context, WaterDatabase.class, DB_NAME)
                .addMigrations(WaterDatabase.MIGRATION_1_2)
                .allowMainThreadQueries()
                .build(); //build database
    }
}