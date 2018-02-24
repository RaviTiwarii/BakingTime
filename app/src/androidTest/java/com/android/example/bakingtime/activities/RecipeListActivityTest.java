package com.android.example.bakingtime.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    public ActivityTestRule<RecipeListActivity> activityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void load_recipes() throws Exception {

    }
}