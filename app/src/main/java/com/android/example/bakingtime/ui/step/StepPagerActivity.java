package com.android.example.bakingtime.ui.step;

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

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.data.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepPagerActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE = "com.android.example.bakingtime.extra_recipe_id";
    private static final String EXTRA_STEP = "com.android.example.bakingtime.extra_step_id";

    private ViewPager viewPager;
    private Recipe recipe;
    private Step currentStep;
    private List<Step> steps;

    public static Intent newIntent(@NonNull final Context context,
                                   @NonNull final Recipe recipe,
                                   @NonNull final Step step) {
        Intent intent = new Intent(context, StepPagerActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        intent.putExtra(EXTRA_STEP, step);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_pager);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_RECIPE)
                && extras.containsKey(EXTRA_STEP)) {
            recipe = extras.getParcelable(EXTRA_RECIPE);
            currentStep = extras.getParcelable(EXTRA_STEP);
            steps = recipe != null ? recipe.getSteps() : new ArrayList<>();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(recipe.getName());
        }

        initializeViews();
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.step_view_pager);
        setUpViewPager();
        showSelectedStepOnViewPager();
    }

    private void setUpViewPager() {
        FragmentManager manager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                Step step = steps.get(position);
                return StepFragment.newInstance(step);
            }

            @Override
            public int getCount() {
                return steps.size();
            }
        });
    }

    private void showSelectedStepOnViewPager() {
        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).getId() == currentStep.getId()) {
                viewPager.setCurrentItem(i);
                break;
            }
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
}
