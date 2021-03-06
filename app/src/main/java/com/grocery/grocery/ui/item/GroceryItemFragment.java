package com.grocery.grocery.ui.item;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.grocery.grocery.room.DataManager;
import com.grocery.grocery.room.GroceryItem;
import com.grocery.grocery.R;
import com.grocery.grocery.ui.list.GroceryListFragment;
import com.grocery.grocery.ui.location.LocationFragment;

import org.parceler.Parcels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GroceryItemFragment extends Fragment {
    private static final String TAG = "GroceryItemFragment";

    public static final String ITEM_LOCATION_BUNDLE = "ITEM_LOCATION_BUNDLE";
    public static final String ITEM_UPDATING = "ITEM_UPDATING";

    private EditText mGroceryName;
    private EditText mGroceryAmount;
    private Switch mGroceryRecurring;
    private TextView mGroceryLocation;
    private Button mAddButton;
    private GroceryItem mGroceryItem;
    private Bundle mBundle;

    boolean updating = false;

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        if(getArguments() != null)
        {
            Log.i(TAG, "back "+mGroceryLocation.getText().toString());
            if(getArguments().containsKey(GroceryListFragment.BUNDLE_ITEM)) {
                mGroceryItem = Parcels.unwrap(getArguments().getParcelable(GroceryListFragment.BUNDLE_ITEM));
                Log.i(TAG, "GROCERY "+mGroceryItem.toString());
                if(mGroceryItem.getGroceryName() != null) {
                    mGroceryName.setText(mGroceryItem.getGroceryName());
                }
                if(mGroceryItem.getGroceryAmount() != null) {
                    mGroceryAmount.setText(mGroceryItem.getGroceryAmount());
                }
                if(mGroceryItem.getGroceryLocation() != null) {
                    mGroceryLocation.setText(mGroceryItem.getGroceryLocation());
                }
                mGroceryRecurring.setChecked(mGroceryItem.isRecurring());
                updating = true;
                mAddButton.setText("UPDATE");
            } else if(getArguments().containsKey(ITEM_LOCATION_BUNDLE)) {
                mGroceryItem = Parcels.unwrap(getArguments().getParcelable(ITEM_LOCATION_BUNDLE));
                mGroceryLocation.setText(mGroceryItem.getGroceryLocation());
                Log.i(TAG, "GROCERY "+mGroceryItem.toString());
                if(mGroceryItem.getGroceryName() != null) {
                    mGroceryName.setText(mGroceryItem.getGroceryName());
                }
                if(mGroceryItem.getGroceryAmount() != null) {
                    mGroceryAmount.setText(mGroceryItem.getGroceryAmount());
                }
                if(mGroceryItem.getGroceryLocation() != null) {
                    mGroceryLocation.setText(mGroceryItem.getGroceryLocation());
                }
                mGroceryRecurring.setChecked(mGroceryItem.isRecurring());
            }
            if(getArguments().containsKey(ITEM_UPDATING)) {
                updating = getArguments().getBoolean(ITEM_UPDATING);
                mAddButton.setText("UPDATE");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        DataManager.getInstance(getContext()).update(mGroceryItem);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroceryItem = new GroceryItem();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grocery_item_fragment, container, false);
        Log.i(TAG, "onCreateView");
        mGroceryName = view.findViewById(R.id.grocery_name);
        mGroceryName.setText(mGroceryItem.getGroceryName());
        mGroceryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mGroceryItem.setGroceryName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mGroceryAmount = view.findViewById(R.id.grocery_amount);
        mGroceryAmount.setText(mGroceryItem.getGroceryAmount());
        mGroceryAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mGroceryItem.setGroceryAmount(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mGroceryRecurring = view.findViewById(R.id.grocery_recurring);
        mGroceryRecurring.setChecked(mGroceryItem.isRecurring());
        mGroceryRecurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mGroceryItem.setRecurring(b);
            }
        });

        mGroceryLocation = view.findViewById(R.id.grocery_location);
        if(getArguments() != null)
            mGroceryLocation.setText(getArguments().getString(LocationFragment.LOCATION));
        mGroceryItem.setGroceryLocation(mGroceryLocation.getText().toString());
        mGroceryLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBundle = new Bundle();
                mBundle.putParcelable(ITEM_LOCATION_BUNDLE, Parcels.wrap(mGroceryItem));
                mBundle.putBoolean(ITEM_UPDATING, updating);
                Fragment frag = new LocationFragment();
                frag.setArguments(mBundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.list_fragment_container, frag).commit();
            }
        });

        mAddButton = view.findViewById(R.id.add_new);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updating) {
                    Log.i(TAG, "update");
                    Log.i(TAG, mGroceryItem.toString());
                    DataManager.getInstance(getContext()).update(mGroceryItem);
                } else {
                    Log.i(TAG, "add");
                    DataManager.getInstance(getContext()).insert(mGroceryItem);
                }
                Fragment frag = new GroceryListFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.list_fragment_container, frag).commit();
            }
        });

        return view;
    }
}
