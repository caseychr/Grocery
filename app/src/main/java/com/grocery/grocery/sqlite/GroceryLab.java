package com.grocery.grocery.sqlite;

import static com.grocery.grocery.sqlite.GroceryDBSchema.GroceryTable.Cols.GROCERY_AMOUNT;
import static com.grocery.grocery.sqlite.GroceryDBSchema.GroceryTable.Cols.GROCERY_COMPLETED;
import static com.grocery.grocery.sqlite.GroceryDBSchema.GroceryTable.Cols.GROCERY_LOCATION;
import static com.grocery.grocery.sqlite.GroceryDBSchema.GroceryTable.Cols.GROCERY_NAME;
import static com.grocery.grocery.sqlite.GroceryDBSchema.GroceryTable.Cols.GROCERY_RECURRING;
import static com.grocery.grocery.sqlite.GroceryDBSchema.GroceryTable.NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.grocery.grocery.room.GroceryItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class GroceryLab {
    private static final String TAG = "GroceryLab";

    private static GroceryLab sGroceryLab;

    private List<GroceryItem> mGroceryItems;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static GroceryLab get(Context context)
    {
        if(sGroceryLab == null)
            sGroceryLab = new GroceryLab(context);
        return sGroceryLab;
    }

    private static ContentValues getContentValues(GroceryItem groceryItem)
    {
        ContentValues values = new ContentValues();
        values.put(GroceryDBSchema.GroceryTable.Cols.UUID, groceryItem.getId());
        values.put(GROCERY_NAME, groceryItem.getGroceryName());
        values.put(GROCERY_AMOUNT, groceryItem.getGroceryAmount());
        values.put(GROCERY_LOCATION, groceryItem.getGroceryLocation());
        values.put(GROCERY_COMPLETED, groceryItem.isCompleted() ? 1 : 0);
        values.put(GROCERY_RECURRING, groceryItem.isRecurring() ? 1 : 0);
        return values;
    }

    private GroceryLab(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new GroceryBaseHelper(mContext).getWritableDatabase();
        //mGroceryItems = new ArrayList<>();
        /*for(int i=0;i<100;i++)
        {
            GroceryItem crime = new GroceryItem();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i%2==0);
            crime.setRequiresPolice(i%4==0);
            mCrimes.add(crime);
        }*/
    }

    public List<GroceryItem> getGroceries() {
        List<GroceryItem> groceryItems = new ArrayList<>();
        GroceryCursorWrapper cursor = queryGroceries(null, null);
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                groceryItems.add(cursor.getGrocery());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        Collections.sort(groceryItems, BY_LOCATION);
        Log.i(TAG, groceryItems.toString());
        return groceryItems;
    }

    public List<GroceryItem> getRecurringGroceries() {
        List<GroceryItem> groceryItems = new ArrayList<>();
        GroceryCursorWrapper cursor = queryGroceries(
                GROCERY_RECURRING+"=?",
                new String[]{"true"});
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                groceryItems.add(cursor.getGrocery());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        Collections.sort(groceryItems, BY_LOCATION);
        Log.i(TAG, groceryItems.toString());
        return groceryItems;
    }

    public GroceryItem getGrocery(UUID id)
    {
        GroceryCursorWrapper cursor = queryGroceries(GroceryDBSchema.GroceryTable.Cols.UUID+" = ?",
                new String[] {id.toString()});

        try
        {
            if(cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getGrocery();
        }finally {
            cursor.close();
        }
    }

    public void addGrocery(GroceryItem groceryItem)
    {
        ContentValues values = getContentValues(groceryItem);
        mDatabase.insert(NAME, null, values);
    }

    public void deleteGrocery(GroceryItem crime) {
        mDatabase.delete(NAME,
                GroceryDBSchema.GroceryTable.Cols.UUID + " = ?", new String[]{String.valueOf(crime.getId())});
    }

    public void updateGrocery(GroceryItem groceryItem)
    {
        String uuidString = String.valueOf(groceryItem.getId());
        ContentValues values = getContentValues(groceryItem);
        mDatabase.update(NAME, values,
                GroceryDBSchema.GroceryTable.Cols.UUID+" = ?", new String[] { uuidString});
    }

    private GroceryCursorWrapper queryGroceries(String whereClause, String[] whereArgs)
    {
        Cursor cursor =  mDatabase
                .query(
                NAME,
                null,
                whereClause,
                whereArgs, null, null, null
        );
        return new GroceryCursorWrapper(cursor);
    }

    private Comparator<GroceryItem> BY_LOCATION = new Comparator<GroceryItem>() {
        @Override
        public int compare(GroceryItem t0, GroceryItem t1) {
            return t0.getGroceryLocation().compareTo(t1.getGroceryLocation());
        }
    };
}
