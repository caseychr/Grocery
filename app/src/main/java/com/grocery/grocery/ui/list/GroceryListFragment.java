package com.grocery.grocery.ui.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.grocery.grocery.room.DataManager;
import com.grocery.grocery.room.GroceryItem;
import com.grocery.grocery.R;
import com.grocery.grocery.ui.item.GroceryItemFragment;

import org.parceler.Parcels;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroceryListFragment extends Fragment {
    private static final String TAG = "GroceryListFragment";
    public static final String BUNDLE_ITEM = "BUNDLE_ITEM";

    private RecyclerView mRecyclerView;
    private GroceryAdapter mGroceryAdapter;
    private Button mAddButton;
    private Button mRecurringButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grocery_list_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.grocery_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAddButton = view.findViewById(R.id.create_new);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.list_fragment_container, new GroceryItemFragment()).commit();
            }
        });
        mRecurringButton = view.findViewById(R.id.create_recurring);
        mRecurringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<GroceryItem> groceryItems = null;
                try {
                    groceryItems = DataManager.getInstance(getContext()).getRecurringItems();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(GroceryItem groceryItem:groceryItems) {
                    groceryItem.setCompleted(false);
                    DataManager.getInstance(getContext()).update(groceryItem);
                }

                try {
                    updateUI(DataManager.getInstance(getContext()).getAllNonCompletedItems());
                    Log.i(TAG, "ALL0 "+DataManager.getInstance(getContext()).getAllItems());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            updateUI(DataManager.getInstance(getContext()).getAllNonCompletedItems());
            Log.i(TAG, "ALL1 "+DataManager.getInstance(getContext()).getAllItems());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void updateUI(List<GroceryItem> groceryItems) {
        if(mGroceryAdapter == null)
        {
            mGroceryAdapter = new GroceryAdapter(groceryItems);
            mRecyclerView.setAdapter(mGroceryAdapter);
        }
        else
        {
            mGroceryAdapter.setGroceries(groceryItems);
            mGroceryAdapter.notifyDataSetChanged();
        }
        Log.i(TAG, "list "+groceryItems.toString());
    }

    private class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryHolder>
    {
        private List<GroceryItem> mGroceries;

        public GroceryAdapter(List<GroceryItem> groceries){mGroceries = groceries;}

        @NonNull
        @Override
        public GroceryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new GroceryHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull GroceryHolder groceryHolder, int i) {
            GroceryItem groceryItem = mGroceries.get(i);
            groceryHolder.bind(groceryItem);
        }

        @Override
        public int getItemCount() {
            return mGroceries.size();
        }

        public void setGroceries(List<GroceryItem> groceries){mGroceries = groceries;}

        public class GroceryHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            private TextView mItemName;
            private TextView mItemAmount;
            private TextView mItemLocation;
            private CheckBox mItemCompleted;
            private GroceryItem mGroceryItem;

            public GroceryHolder(LayoutInflater layoutInflater, ViewGroup parent)
            {
                super(layoutInflater.inflate(R.layout.list_item_grocery, parent, false));
                mItemName = itemView.findViewById(R.id.item_name);
                mItemAmount = itemView.findViewById(R.id.item_amount);
                mItemLocation = itemView.findViewById(R.id.item_location);
                mItemCompleted = itemView.findViewById(R.id.item_completed);

                itemView.setOnClickListener(this);

                mItemCompleted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mGroceryItem.setCompleted(true);
                        if(!mGroceryItem.isRecurring()) {
                            DataManager.getInstance(getContext()).delete(mGroceryItem);
                        } else {
                            DataManager.getInstance(getContext()).update(mGroceryItem);
                        }

                        try {
                            updateUI(DataManager.getInstance(getContext()).getAllNonCompletedItems());
                            Log.i(TAG, "ALL2 "+DataManager.getInstance(getContext()).getAllItems());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            public void bind(GroceryItem groceryItem)
            {
                mGroceryItem = groceryItem;
                mItemName.setText(mGroceryItem.getGroceryName());
                mItemAmount.setText("("+mGroceryItem.getGroceryAmount()+")");
                mItemLocation.setText(mGroceryItem.getGroceryLocation());
                mItemCompleted.setChecked(groceryItem.isCompleted());
            }

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_ITEM, Parcels.wrap(mGroceryItem));
                Fragment fragment = new GroceryItemFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.list_fragment_container, fragment).commit();
            }
        }
    }
}
