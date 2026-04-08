package com.example.android2048.scoreBD;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ScoreEntity.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract ScoreDAO scoreDAO();
    private static AppDB instance;

    public static synchronized AppDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "score_bd")
                    .allowMainThreadQueries() // Pour faire simple au début
                    .build();
        }
        return instance;
    }
}