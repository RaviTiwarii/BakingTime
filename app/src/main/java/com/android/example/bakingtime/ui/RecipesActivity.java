package com.android.example.bakingtime.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public class RecipesActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment createFragment() {
        return RecipesFragment.newInstance();
    }
}
