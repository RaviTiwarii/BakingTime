package com.android.example.bakingtime.ui.recipelist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private TextView noRecipeTextView;
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

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_RECIPE_SCROLL))
                recipeListState = savedInstanceState.getParcelable(STATE_RECIPE_SCROLL);
            if (savedInstanceState.containsKey(STATE_RECIPE_LIST))
                recipes = savedInstanceState.getParcelableArrayList(STATE_RECIPE_LIST);
        }

        int spanCount = getResources().getInteger(R.integer.recipes_column_count);
        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
        if (recipeListState != null) layoutManager.onRestoreInstanceState(recipeListState);

        noRecipeTextView = view.findViewById(R.id.tv_no_recipe);

        recipeRecyclerView = view.findViewById(R.id.rv_recipes);
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setHasFixedSize(true);

        loadingIndicator = view.findViewById(R.id.loading_indicator);

        if (recipes == null || recipes.isEmpty()) downloadRecipes();
        else setupRecipeAdapter();

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

    private void downloadRecipes() {
        if (isInternetAvailable()) {
            showRecipeView();
            new RecipeDownloadTask(this).execute(RECIPE_URL);
        } else {
            showNoRecipeView();
            Snackbar snackbar = Snackbar.make(recipeRecyclerView, R.string.no_internet_message,
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Retry", v -> downloadRecipes());
            snackbar.show();
        }
    }

    private void showNoRecipeView() {
        recipeRecyclerView.setVisibility(View.GONE);
        noRecipeTextView.setVisibility(View.VISIBLE);
    }

    private void showRecipeView() {
        noRecipeTextView.setVisibility(View.GONE);
        recipeRecyclerView.setVisibility(View.VISIBLE);
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

    private boolean isInternetAvailable() {
        boolean isAvailable = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            isAvailable = networkInfo != null && networkInfo.isConnected();
        }
        return isAvailable;
    }
}
