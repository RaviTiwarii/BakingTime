package com.android.example.bakingtime.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.adapters.StepAdapter;
import com.android.example.bakingtime.data.local.RecipeStore;
import com.android.example.bakingtime.data.model.Ingredient;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.data.model.Step;

import java.util.List;

public class RecipeFragment extends Fragment {

    private static final String LOG_TAG = RecipeFragment.class.getSimpleName();
    private static final String ARG_RECIPE_ID = "com.android.example.bakingtime.extra.recipe_id";

    private Context context;
    private Recipe recipe;

    private TextView ingredientsTextView;
    private RecyclerView recipeStepsRecyclerView;

    public static RecipeFragment newInstance(long recipeId) {
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_RECIPE_ID)) {
            long recipeId = args.getLong(ARG_RECIPE_ID);
            RecipeStore store = RecipeStore.getInstance(context, "baking.json");
            recipe = store.getRecipe(recipeId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        initializeView(view);
        if (recipe != null) showRecipeView();
        else showNoRecipeView();
        return view;
    }

    private void initializeView(@NonNull final View view) {
        ingredientsTextView = view.findViewById(R.id.tv_ingredients);
        recipeStepsRecyclerView = view.findViewById(R.id.rv_recipe_steps);
    }

    private void showRecipeView() {
        setFragmentTitle(recipe.getName());

        ingredientsTextView.setText(getAllIngredients());

        recipeStepsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        recipeStepsRecyclerView.setHasFixedSize(true);
        recipeStepsRecyclerView.setAdapter(new StepAdapter(recipe.getSteps(), this::onStepSelected));
    }

    private void setFragmentTitle(@NonNull final String title) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    private void onStepSelected(@NonNull final Step step) {
        // TODO Open new activity to show step in detail.
        Log.d(LOG_TAG, step.getShortDescription() + "selected");
    }

    private void showNoRecipeView() {
        setFragmentTitle(getString(R.string.no_recipe_message));
    }

    @NonNull
    private String getAllIngredients() {
        StringBuilder builder = new StringBuilder("");
        List<Ingredient> ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) {
            if (builder.length() != 0) builder.append("\n");
            String ingredientDetail = getString(R.string.ingredient_detail,
                    ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getName());
            builder.append(ingredientDetail);
        }
        return builder.toString();
    }

    @Nullable
    private ActionBar getActionBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) return activity.getSupportActionBar();
        else return null;
    }
}
