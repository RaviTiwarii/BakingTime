package com.android.example.bakingtime.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

public class RecipeActivity extends SingleFragmentActivity {

    private static final String EXTRA_RECIPE_ID = "com.android.example.bakingtime.extra.recipe_id";

    public static Intent newIntent(@NonNull final Context context, long recipeId) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

    @NonNull
    @Override
    protected Fragment createFragment() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_RECIPE_ID))
            return RecipeFragment.newInstance(extras.getLong(EXTRA_RECIPE_ID));
        else
            return RecipeFragment.newInstance(-1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
