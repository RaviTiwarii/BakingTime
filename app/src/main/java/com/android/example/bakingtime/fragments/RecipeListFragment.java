package com.android.example.bakingtime.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.activities.RecipeActivity;
import com.android.example.bakingtime.adapters.RecipeAdapter;
import com.android.example.bakingtime.data.local.RecipeStore;
import com.android.example.bakingtime.data.model.Recipe;

import java.util.List;

/**
 * This fragment shows the list of recipes.
 *
 * @author Ravi Tiwari
 * @version 1.0
 */
public class RecipeListFragment extends Fragment {

    private static final String STATE_GRID_SCROLL_POSITION = "state.grid_scroll_position";

    private Context context;
    private GridView recipeGridView;
    private RecipeAdapter recipeAdapter;
    private CountingIdlingResource idlingResource =
            new CountingIdlingResource("RECIPE_DOWNLOADER");

    private int gridScrollPosition = 0;

    /**
     * Returns the new instance of this fragment.
     *
     * @return new instance of this fragment
     */
    @NonNull
    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        recipeGridView = view.findViewById(R.id.grid_view_recipes);
        recipeGridView.smoothScrollToPosition(gridScrollPosition);
        recipeGridView.setOnItemClickListener((parent, view1, position, id) -> {
            Recipe recipe = (Recipe) parent.getItemAtPosition(position);
            Intent intent = RecipeActivity.newIntent(context, recipe.getId());
            startActivity(intent);
        });

        idlingResource.increment();
        List<Recipe> recipes = new RecipeDownloader().download();
        idlingResource.decrement();

        setupAdapter(recipes);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STATE_GRID_SCROLL_POSITION, recipeGridView.getFirstVisiblePosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null &&
                savedInstanceState.containsKey(STATE_GRID_SCROLL_POSITION))
            gridScrollPosition = savedInstanceState.getInt(STATE_GRID_SCROLL_POSITION);
    }

    private void setupAdapter(@NonNull final List<Recipe> recipes) {
        if (recipeAdapter == null) {
            recipeAdapter = new RecipeAdapter(context, R.layout.list_item_recipe, recipes);
            recipeGridView.setAdapter(recipeAdapter);
        } else {
            recipeAdapter.setRecipes(recipes);
            recipeAdapter.notifyDataSetChanged();
        }
    }

    public CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }

    private class RecipeDownloader {
        List<Recipe> download() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            RecipeStore recipeStore = RecipeStore.get(context);
            List<Recipe> recipes = recipeStore.getRecipes();

            return recipes;
        }
    }
}
