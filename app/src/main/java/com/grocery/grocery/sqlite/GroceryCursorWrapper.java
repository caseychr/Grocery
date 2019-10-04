package com.grocery.grocery.sqlite;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.grocery.grocery.room.GroceryItem;
import com.grocery.grocery.sqlite.GroceryDBSchema;

import java.util.UUID;

public class GroceryCursorWrapper extends CursorWrapper {

    public GroceryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public GroceryItem getGrocery(){
        String uuidString = getString(getColumnIndex(GroceryDBSchema.GroceryTable.Cols.UUID));
        String itemNameString = getString(getColumnIndex(GroceryDBSchema.GroceryTable.Cols.GROCERY_NAME));
        int itemAmount = getInt(getColumnIndex(GroceryDBSchema.GroceryTable.Cols.GROCERY_AMOUNT));
        String itemLocation = getString(getColumnIndex(GroceryDBSchema.GroceryTable.Cols.GROCERY_LOCATION));
        int itemCompleted = getInt(getColumnIndex(GroceryDBSchema.GroceryTable.Cols.GROCERY_COMPLETED));
        int itemRecurring = getInt(getColumnIndex(GroceryDBSchema.GroceryTable.Cols.GROCERY_RECURRING));

        GroceryItem groceryItem = new GroceryItem();//(UUID.fromString(uuidString));
        groceryItem.setGroceryName(itemNameString);
        groceryItem.setGroceryAmount(String.valueOf(itemAmount));
        groceryItem.setGroceryLocation(itemLocation);
        groceryItem.setCompleted(itemCompleted != 0);
        groceryItem.setRecurring(itemRecurring != 0);

        return groceryItem;
    }
}
