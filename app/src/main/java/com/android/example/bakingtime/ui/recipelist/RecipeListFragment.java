package com.android.example.bakingtime.ui.recipelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.ui.recipedetail.RecipeActivity;

import java.util.ArrayList;

/**
 * This fragment shows the list of recipes.
 *
 * @author Ravi Tiwari
 * @version 1.0
 * @since 1.0
 */
public class RecipeListFragment extends Fragment implements RecipeDownloadTask.TaskCallback<Recipe> {

    private static final String RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String STATE_RECIPE_LIST = "state.recipe_list";
    private static final String STATE_RECIPE_SCROLL = "state.scroll";

    private Context context;
    private ArrayList<Recipe> recipes;
    private Parcelable recipeListState;
    private ProgressBar loadingIndicator;
    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

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

        int spanCount = getResources().getInteger(R.integer.recipes_column_count);
        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);

        recipeRecyclerView = view.findViewById(R.id.rv_recipes);
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setHasFixedSize(true);

        loadingIndicator = view.findViewById(R.id.loading_indicator);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_RECIPE_SCROLL)) {
                recipeListState = savedInstanceState.getParcelable(STATE_RECIPE_SCROLL);
                recipeRecyclerView.getLayoutManager().onRestoreInstanceState(recipeListState);
            }
            if (savedInstanceState.containsKey(STATE_RECIPE_LIST)) {
                recipes = savedInstanceState.getParcelableArrayList(STATE_RECIPE_LIST);
                setupRecipeAdapter();
            }
        } else {
            new RecipeDownloadTask(this).execute(RECIPE_URL);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        recipeListState = recipeRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(STATE_RECIPE_SCROLL, recipeListState);
        outState.putParcelableArrayList(STATE_RECIPE_LIST, recipes);
    }

    @Override
    public void onPreExecute() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(@Nullable final ArrayList<Recipe> recipes) {
        loadingIndicator.setVisibility(View.GONE);
        if (recipes == null || recipes.isEmpty()) {
            showNoRecipeView();
        } else {
            this.recipes = recipes;
            setupRecipeAdapter();
        }
    }

    private void showNoRecipeView() {
        Toast.makeText(context, "No recipe found :(", Toast.LENGTH_SHORT).show();
    }

    private void setupRecipeAdapter() {
        if (recipeAdapter == null) {
            recipeAdapter = new RecipeAdapter(recipes, this::onRecipeSelected);
            recipeRecyclerView.setAdapter(recipeAdapter);
        } else {
            recipeAdapter.setRecipes(recipes);
        }
    }

    private void onRecipeSelected(@NonNull final Recipe recipe) {
        Intent intent = RecipeActivity.newIntent(context, recipe);
        startActivity(intent);
    }
}
