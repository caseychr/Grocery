package com.grocery.grocery.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface GroceryItemDao {

    @Insert
    void insert(GroceryItem groceryItem);

    @Update
    void update(GroceryItem groceryItem);

    @Delete
    void delete(GroceryItem groceryItem);

    @Query("SELECT * FROM grocery_item ORDER BY mGroceryLocation")
    List<GroceryItem> getAllItems();

    @Query("SELECT * FROM grocery_item WHERE mCompleted = :isCompleted ORDER BY mGroceryLocation")
    public List<GroceryItem> getAllNonCompletedItems(boolean isCompleted);

    @Query("SELECT * FROM grocery_item WHERE mRecurring = :isRecurring AND mCompleted = :isComplete")
    public List<GroceryItem> getRecurringItems(boolean isRecurring, boolean isComplete);
}
