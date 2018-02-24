package com.android.example.bakingtime.activities;

import android.support.annotation.NonNull;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.fragments.RecipeListFragment;

/**
 * This activity shows the list of recipes.
 *
 * @author Ravi Tiwari
 * @version 1.0
 */
public class RecipeListActivity extends SingleFragmentActivity {

    /**
     * Returns the instance of RecipeListFragment
     *
     * @return instance of RecipeListFragment
     */
    @NonNull
    @Override
    protected Fragment createFragment() {
        return RecipeListFragment.newInstance();
    }

    public CountingIdlingResource getCountingIdlingResource() {
        RecipeListFragment fragment = (RecipeListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return fragment.getIdlingResource();
    }
}
