package com.grocery.grocery.room;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataManager {
    private static final String TAG = "DataManager";

    private static DataManager instance;

    private GroceryItemDao mGroceryItemDao;

    private DataManager(Context context) {
        mGroceryItemDao = GroceryItemDatabase.getInstance(context).mGroceryItemDao();
    }

    public static DataManager getInstance(Context context) {
        if(instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public void insert(GroceryItem groceryItem) {
        new InsertAsyncTask(mGroceryItemDao).execute(groceryItem);
    }

    public void update(GroceryItem groceryItem) {
        new UpdateAsyncTask(mGroceryItemDao).execute(groceryItem);
    }

    public void delete(GroceryItem groceryItem) {
        new DeleteAsyncTask(mGroceryItemDao).execute(groceryItem);
    }

    public List<GroceryItem> getAllNonCompletedItems() throws ExecutionException, InterruptedException {
        return new ListNonCompleteAsyncTask(mGroceryItemDao).execute().get();
    }

    public List<GroceryItem> getRecurringItems() throws ExecutionException, InterruptedException {
        return new ListRecurringAsyncTask(mGroceryItemDao).execute().get();
    }

    public List<GroceryItem> getAllItems() throws ExecutionException, InterruptedException {
        return new ListAsyncTask(mGroceryItemDao).execute().get();
    }

    public class InsertAsyncTask extends AsyncTask<GroceryItem, Void, Void> {
        private GroceryItemDao groceryItemDao;

        private InsertAsyncTask(GroceryItemDao groceryItemDao) {
            this.groceryItemDao = groceryItemDao;
        }

        @Override
        protected Void doInBackground(GroceryItem... groceryItems) {
            groceryItemDao.insert(groceryItems[0]);
            return null;
        }
    }

    public class UpdateAsyncTask extends AsyncTask<GroceryItem, Void, Void> {
        private GroceryItemDao groceryItemDao;

        private UpdateAsyncTask(GroceryItemDao groceryItemDao) {
            this.groceryItemDao = groceryItemDao;
        }

        @Override
        protected Void doInBackground(GroceryItem... groceryItems) {
            if(groceryItems[0] != null) {
                Log.i(TAG, groceryItems[0].toString());
                groceryItemDao.update(groceryItems[0]);
            }
            return null;
        }
    }

    public class DeleteAsyncTask extends AsyncTask<GroceryItem, Void, Void> {
        private GroceryItemDao groceryItemDao;

        private DeleteAsyncTask(GroceryItemDao groceryItemDao) {
            this.groceryItemDao = groceryItemDao;
        }

        @Override
        protected Void doInBackground(GroceryItem... groceryItems) {
            if(groceryItems[0] != null) {
                groceryItemDao.delete(groceryItems[0]);
            }
            return null;
        }
    }

    public class ListAsyncTask extends AsyncTask<Void, Void, List<GroceryItem>> {
        private GroceryItemDao groceryItemDao;

        private ListAsyncTask(GroceryItemDao groceryItemDao) {
            this.groceryItemDao = groceryItemDao;
        }


        @Override
        protected List<GroceryItem> doInBackground(Void... voids) {
            return groceryItemDao.getAllItems();
        }
    }

    public class ListNonCompleteAsyncTask extends AsyncTask<Void, Void, List<GroceryItem>> {
        private GroceryItemDao groceryItemDao;

        private ListNonCompleteAsyncTask(GroceryItemDao groceryItemDao) {
            this.groceryItemDao = groceryItemDao;
        }


        @Override
        protected List<GroceryItem> doInBackground(Void... voids) {
            return groceryItemDao.getAllNonCompletedItems(false);
        }
    }

    public class ListRecurringAsyncTask extends AsyncTask<Void, Void, List<GroceryItem>> {
        private GroceryItemDao groceryItemDao;

        private ListRecurringAsyncTask(GroceryItemDao groceryItemDao) {
            this.groceryItemDao = groceryItemDao;
        }


        @Override
        protected List<GroceryItem> doInBackground(Void... voids) {
            return groceryItemDao.getRecurringItems(true, true);
        }
    }
}
