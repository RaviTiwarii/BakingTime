package com.android.example.bakingtime.ui.recipelist;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.android.example.bakingtime.ui.SingleFragmentActivity;

/**
 * This activity shows the list of recipes.
 *
 * @author Ravi Tiwari
 * @version 1.0
 * @since 1.0
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
}
