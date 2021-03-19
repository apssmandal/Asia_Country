package com.animixer.asiacountry;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Country.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CountryDAO countryDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {

        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "COUNTRY_NAMES")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }


}
