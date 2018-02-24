package com.android.example.bakingtime.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.local.RecipeStore;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.data.model.Step;
import com.android.example.bakingtime.fragments.StepFragment;

import java.util.ArrayList;
import java.util.List;

public class StepPagerActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE_ID = "com.android.example.bakingtime.extra_recipe_id";
    private static final String EXTRA_STEP_ID = "com.android.example.bakingtime.extra_step_id";

    private Recipe recipe;
    private List<Step> steps = new ArrayList<>();
    private long stepId;
    private int currentStepIndex;

    private ViewPager viewPager;
    private ImageButton nextStepButton;
    private ImageButton previousStepButton;

    public static Intent newIntent(@NonNull final Context context, final long recipeId,
                                   final long stepId) {
        Intent intent = new Intent(context, StepPagerActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_STEP_ID, stepId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_pager);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_RECIPE_ID)
                && extras.containsKey(EXTRA_STEP_ID)) {
            long recipeId = extras.getLong(EXTRA_RECIPE_ID);
            stepId = extras.getLong(EXTRA_STEP_ID);
            recipe = RecipeStore.get(this).getRecipe(recipeId);
            steps = recipe.getSteps();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(recipe.getName());
        }

        initializeViews();
    }

    private void initializeViews() {
        nextStepButton = findViewById(R.id.btn_next);
        nextStepButton.setOnClickListener(view -> viewPager.setCurrentItem(++currentStepIndex));

        previousStepButton = findViewById(R.id.btn_previous);
        previousStepButton.setOnClickListener(view -> viewPager.setCurrentItem(--currentStepIndex));

        viewPager = findViewById(R.id.step_view_pager);
        setUpViewPager();

        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).getId() == stepId) {
                currentStepIndex = i;
                break;
            }
        }
        viewPager.setCurrentItem(currentStepIndex);
    }

    private void setUpViewPager() {
        FragmentManager manager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                Step step = steps.get(position);
                return StepFragment.newInstance(recipe.getId(), step.getId());
            }

            @Override
            public int getCount() {
                return steps.size();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                currentStepIndex = position;

                int nextButtonVisibility = (position < steps.size() - 1) ? View.VISIBLE : View.INVISIBLE;
                nextStepButton.setVisibility(nextButtonVisibility);

                int previousButtonVisibility = (position > 0) ? View.VISIBLE : View.INVISIBLE;
                previousStepButton.setVisibility(previousButtonVisibility);
            }
        });
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