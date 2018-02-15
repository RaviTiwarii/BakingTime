package com.android.example.bakingtime.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.local.RecipeStore;
import com.android.example.bakingtime.data.model.Recipe;

import java.util.List;

public class RecipeListFragment extends Fragment {

    private Context context;

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
        initializeViews(view);
        return view;
    }

    private void initializeViews(@NonNull final View view) {
        RecipeStore recipeStore = RecipeStore.getInstance(context, "baking.json");
        List<Recipe> recipes = recipeStore.getRecipes();

        RecipeAdapter recipeAdapter = new RecipeAdapter(context, R.layout.list_item_recipe, recipes);

        GridView recipeGridView = view.findViewById(R.id.grid_view_recipes);
        recipeGridView.setAdapter(recipeAdapter);
        recipeGridView.setOnItemClickListener((parent, view1, position, id) -> {
            Recipe recipe = (Recipe) parent.getItemAtPosition(position);
            Intent intent = RecipeActivity.newIntent(context, recipe.getId());
            startActivity(intent);
        });
    }
}
