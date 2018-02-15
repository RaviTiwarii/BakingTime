package com.android.example.bakingtime.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.android.example.bakingtime.fragments.RecipeListFragment;

public class RecipeListActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment createFragment() {
        return RecipeListFragment.newInstance();
    }
}
