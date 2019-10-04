package com.grocery.grocery.room;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Parcel
@Entity(tableName = "grocery_item")
public class GroceryItem {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    int mId;
    String mGroceryName;
    String mGroceryAmount;
    String mGroceryLocation;
    boolean mCompleted;
    boolean mRecurring;

    @Ignore
    public GroceryItem(){

    }

    @Ignore
    public GroceryItem(int id)
    {
        mId = id;
    }

    public GroceryItem(String groceryName, String groceryAmount, String groceryLocation, boolean completed,
            boolean recurring) {
        mGroceryName = groceryName;
        mGroceryAmount = groceryAmount;
        mGroceryLocation = groceryLocation;
        mCompleted = completed;
        mRecurring = recurring;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getGroceryName() {
        return mGroceryName;
    }

    public void setGroceryName(String groceryName) {
        mGroceryName = groceryName;
    }

    public String getGroceryAmount() {
        return mGroceryAmount;
    }

    public void setGroceryAmount(String groceryAmount) {
        mGroceryAmount = groceryAmount;
    }

    public String getGroceryLocation() {
        return mGroceryLocation;
    }

    public void setGroceryLocation(String groceryLocation) {
        mGroceryLocation = groceryLocation;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public boolean isRecurring() {
        return mRecurring;
    }

    public void setRecurring(boolean recurring) {
        mRecurring = recurring;
    }

    public static List<GroceryItem> sGroceryItems = new ArrayList<>();

    @Override
    public String toString() {
        return "GroceryItem{" +
                "mId=" + mId +
                ", mGroceryName='" + mGroceryName + '\'' +
                ", mGroceryAmount='" + mGroceryAmount + '\'' +
                ", mGroceryLocation='" + mGroceryLocation + '\'' +
                ", mCompleted=" + mCompleted +
                ", mRecurring=" + mRecurring +
                '}';
    }
}
