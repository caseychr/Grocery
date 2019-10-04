package com.grocery.grocery.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grocery.grocery.sqlite.GroceryDBSchema;

public class GroceryBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "groceryBase.db";

    public GroceryBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ GroceryDBSchema.GroceryTable.NAME+"("+
        "_id integer primary key autoincrement, "+
        GroceryDBSchema.GroceryTable.Cols.UUID+", "+
        GroceryDBSchema.GroceryTable.Cols.GROCERY_NAME+", "+
        GroceryDBSchema.GroceryTable.Cols.GROCERY_AMOUNT+", "+
        GroceryDBSchema.GroceryTable.Cols.GROCERY_LOCATION+", "+
        GroceryDBSchema.GroceryTable.Cols.GROCERY_COMPLETED+", "+
        GroceryDBSchema.GroceryTable.Cols.GROCERY_RECURRING+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
