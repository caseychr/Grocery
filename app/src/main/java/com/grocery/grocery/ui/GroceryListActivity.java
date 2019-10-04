package com.grocery.grocery.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.grocery.grocery.R;
import com.grocery.grocery.ui.list.GroceryListFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class GroceryListActivity extends AppCompatActivity {

    public static Intent newInstance(Context context)
    {
        Intent intent = new Intent(context, GroceryListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.list_fragment_container);

        if(fragment == null)
        {
            fragment = new GroceryListFragment();
            fm.beginTransaction().add(R.id.list_fragment_container, fragment).commit();
        }
    }
}
