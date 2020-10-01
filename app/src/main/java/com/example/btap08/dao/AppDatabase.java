package com.example.btap08.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.btap08.model.News;

@Database(entities = {News.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(
                    context,
                    AppDatabase.class,
                    "news_database"
            ).allowMainThreadQueries().build();
        }
        return appDatabase;
    }

    public abstract NewsDao getNewsDao();


}