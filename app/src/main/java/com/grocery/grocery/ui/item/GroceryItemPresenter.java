package com.grocery.grocery.ui.item;

import android.content.Context;

import com.grocery.grocery.room.DataManager;
import com.grocery.grocery.room.GroceryItem;

public class GroceryItemPresenter {

    public void update(Context context, GroceryItem groceryItem) {
        DataManager.getInstance(context).update(groceryItem);
    }

    public void insert(Context context, GroceryItem groceryItem) {
        DataManager.getInstance(context).insert(groceryItem);
    }
}
