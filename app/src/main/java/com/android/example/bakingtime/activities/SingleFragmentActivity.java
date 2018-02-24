package com.android.example.bakingtime.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.android.example.bakingtime.R;

/**
 * This is an abstract class which represents an activity having single fragment.
 *
 * @author Ravi Tiwari
 * @version 1.0
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    /**
     * Returns the instance of the hosted fragment
     *
     * @return instance of the hosted fragment
     */
    @NonNull
    protected abstract Fragment createFragment();

    /**
     * Returns the layout resource id of the hosting activity
     *
     * @return layout resource id of the hosting activity
     */
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    /**
     * Called when activity was created
     *
     * @param savedInstanceState bundle which keeps cached data
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
