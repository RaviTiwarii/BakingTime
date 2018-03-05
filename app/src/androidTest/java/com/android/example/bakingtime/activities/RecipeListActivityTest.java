package com.android.example.bakingtime.activities;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.ui.recipelist.RecipeListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public final ActivityTestRule<RecipeListActivity> activityTestRule =
            new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void openApp_ShowsListOfRecipes() throws Exception {
        IdlingRegistry idlingRegistry = IdlingRegistry.getInstance();
        CountingIdlingResource idlingResource =
                activityTestRule.getActivity().getCountingIdlingResource();
        idlingRegistry.register(idlingResource);

        clickGridViewItemAtPosition(1);

        idlingRegistry.unregister(idlingResource);
    }

    @Test
    public void clickGridViewItem_OpensRecipeActivity() throws Exception {
        clickGridViewItemAtPosition(1);

        onView(withId(R.id.tv_ingredients))
                .check(matches(isDisplayed()));
    }

    private void clickGridViewItemAtPosition(int position) {
        onData(anything()).inAdapterView(withId(R.id.grid_view_recipes))
                .atPosition(position)
                .perform(click());
    }
}