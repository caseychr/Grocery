package com.grocery.grocery.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grocery.grocery.R;
import com.grocery.grocery.room.GroceryItem;
import com.grocery.grocery.ui.item.GroceryItemFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LocationFragment extends Fragment
{
    public static final String LOCATION = "LOCATION";

    private RecyclerView mRecyclerView;
    private LocationAdapter mLocationAdapter;
    private List<String> locations = new ArrayList<>();
    private Bundle mBundle;

    private boolean updating;

    private GroceryItem mGroceryItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updating = getArguments().getBoolean(GroceryItemFragment.ITEM_UPDATING);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGroceryItem = Parcels.unwrap(getArguments().getParcelable(GroceryItemFragment.ITEM_LOCATION_BUNDLE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.location_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        locations.add("ALDI");locations.add("Kroger");locations.add("Publix");locations.add("Trader Joe's");locations.add("Walmart");
        if(mLocationAdapter == null)
        {
            mLocationAdapter = new LocationAdapter(locations);
            mRecyclerView.setAdapter(mLocationAdapter);
        }
        mLocationAdapter.notifyDataSetChanged();
        return view;
    }

    private class LocationHolder extends RecyclerView.ViewHolder
    {
        private TextView mLocationTextView;
        private String mLocation;

        public LocationHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.list_item_location, parent, false));
            mLocationTextView = itemView.findViewById(R.id.location);

            mLocationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGroceryItem.setGroceryLocation(mLocationTextView.getText().toString());
                    mBundle = new Bundle();
                    mBundle.putParcelable(GroceryItemFragment.ITEM_LOCATION_BUNDLE, Parcels.wrap(mGroceryItem));
                    mBundle.putBoolean(GroceryItemFragment.ITEM_UPDATING, updating);
                    Fragment frag = new GroceryItemFragment();
                    frag.setArguments(mBundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.list_fragment_container, frag).commit();
                }
            });
        }

        public void bind(String location)
        {
            mLocation = location;
            mLocationTextView.setText(mLocation);
        }
    }

    private class LocationAdapter extends RecyclerView.Adapter<LocationHolder>
    {
        private List<String> mLocations;

        public LocationAdapter(List<String> locations){mLocations = locations;}

        @NonNull
        @Override
        public LocationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new LocationHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationHolder locationHolder, int i) {
            String location = mLocations.get(i);
            locationHolder.bind(location);
        }

        @Override
        public int getItemCount() {
            return mLocations.size();
        }
    }
}
