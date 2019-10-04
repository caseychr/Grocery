package com.grocery.grocery.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GroceryItem.class}, version = 1, exportSchema = false)
public abstract class GroceryItemDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "grocery_db";

    private static GroceryItemDatabase instance;

    public static GroceryItemDatabase getInstance(final Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GroceryItemDatabase.class, DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract GroceryItemDao mGroceryItemDao();
}
