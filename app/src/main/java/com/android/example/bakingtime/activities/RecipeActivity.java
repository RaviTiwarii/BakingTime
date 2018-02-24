package com.android.example.bakingtime.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.model.Step;
import com.android.example.bakingtime.fragments.RecipeFragment;
import com.android.example.bakingtime.fragments.StepFragment;

/**
 * This activity shows the details of a recipe.
 *
 * @author Ravi Tiwari
 * @version 1.0
 */
public class RecipeActivity extends SingleFragmentActivity implements RecipeFragment.Callback {

    private static final String EXTRA_RECIPE_ID = "com.android.example.bakingtime.extra.recipe_id";
    private static final String STATE_RECIPE_ID = "com.android.example.bakingtime.state.recipe_id";

    private long recipeId = -1;

    /**
     * Returns the intent to start this activity.
     *
     * @param context  calling context
     * @param recipeId recipe id
     * @return intent to start this activity
     */
    public static Intent newIntent(@NonNull final Context context, final long recipeId) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

    /**
     * Returns the layout resource id for this activity.
     *
     * @return layout resource id.
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    /**
     * Returns the fragment instance hosted by this activity.
     * @return fragment instance.
     */
    @NonNull
    @Override
    protected Fragment createFragment() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (recipeId != -1) {
            return RecipeFragment.newInstance(recipeId);
        } else if (extras != null && extras.containsKey(EXTRA_RECIPE_ID)) {
            recipeId = extras.getLong(EXTRA_RECIPE_ID);
            return RecipeFragment.newInstance(recipeId);
        } else {
            throw new IllegalArgumentException("No recipe id found");
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_RECIPE_ID, recipeId);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        recipeId = savedInstanceState.getLong(STATE_RECIPE_ID);

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onStepSelected(@NonNull Step step) {
        boolean deviceIsPhone = findViewById(R.id.detail_fragment_container) == null;
        if (deviceIsPhone) {
            Intent intent = StepPagerActivity.newIntent(this, recipeId, step.getId());
            startActivity(intent);
        } else {
            Fragment fragment = StepFragment.newInstance(recipeId, step.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .commit();
        }
    }
}
