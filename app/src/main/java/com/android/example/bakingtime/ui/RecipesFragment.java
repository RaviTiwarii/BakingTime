package com.android.example.bakingtime.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.local.RecipeStore;
import com.android.example.bakingtime.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {

    private static final String LOG_TAG = RecipesFragment.class.getSimpleName();

    private RecyclerView recipeRecyclerView;
    private RecipeAdapter recipeAdapter;

    @NonNull
    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        initializeViews(view);
        loadRecipes();
        return view;
    }

    private void initializeViews(@NonNull final View view) {
        recipeRecyclerView = view.findViewById(R.id.rv_recipes);

        GridLayoutManager layoutManager;
        if (isTabletMode())
            layoutManager = new GridLayoutManager(getContext(), 3);
        else
            layoutManager = new GridLayoutManager(getContext(), 1);
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setHasFixedSize(true);
        setupRecipeAdapter(new ArrayList<Recipe>());
    }

    private boolean isTabletMode() {
        Configuration configuration = getActivity().getResources().getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void setupRecipeAdapter(@NonNull final List<Recipe> recipes) {
        if (recipeAdapter != null) {
            recipeAdapter = new RecipeAdapter(recipes);
            recipeRecyclerView.setAdapter(new RecipeAdapter(recipes));
        } else {
            recipeAdapter.setRecipes(recipes);
        }
    }

    private void loadRecipes() {
        RecipeStore recipeStore = new RecipeStore(getContext(), "baking.json");
        List<Recipe> recipes = recipeStore.getRecipes();
        Log.d(LOG_TAG, "Total Recipe Count = " + recipes.size());
        if (recipes.isEmpty()) showNoRecipeView();
        else setupRecipeAdapter(recipes);
    }

    private void showNoRecipeView() {
        // TODO Implement view for empty recipe list
    }
}
