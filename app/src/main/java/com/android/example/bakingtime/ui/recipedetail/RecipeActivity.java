package com.android.example.bakingtime.ui.recipedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.data.model.Step;
import com.android.example.bakingtime.ui.SingleFragmentActivity;
import com.android.example.bakingtime.ui.step.StepFragment;
import com.android.example.bakingtime.ui.step.StepPagerActivity;

/**
 * This activity shows the details of a recipe.
 *
 * @author Ravi Tiwari
 * @version 1.0
 */
public class RecipeActivity extends SingleFragmentActivity implements RecipeFragment.Callback {

    private static final String EXTRA_RECIPE = "extra.recipe";
    private static final String STATE_RECIPE = "state.recipe";

    private Recipe recipe;

    /**
     * Returns the intent to start this activity.
     *
     * @param context  calling context
     * @param recipeId recipe id
     * @return intent to start this activity
     */
    @NonNull
    public static Intent newIntent(@NonNull final Context context, @NonNull final Recipe recipe) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
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
     *
     * @return fragment instance.
     */
    @NonNull
    @Override
    protected Fragment createFragment() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (recipe != null) {
            return RecipeFragment.newInstance(recipe);
        } else if (extras != null && extras.containsKey(EXTRA_RECIPE)) {
            recipe = extras.getParcelable(EXTRA_RECIPE);
            return RecipeFragment.newInstance(recipe);
        } else {
            throw new IllegalArgumentException("No recipe found");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_RECIPE, recipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        recipe = savedInstanceState.getParcelable(STATE_RECIPE);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onStepSelected(@NonNull Step step) {
        boolean deviceIsPhone = findViewById(R.id.detail_fragment_container) == null;
        if (deviceIsPhone) {
            Intent intent = StepPagerActivity.newIntent(this, recipe, step);
            startActivity(intent);
        } else {
            Fragment fragment = StepFragment.newInstance(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .commit();
        }
    }
}
