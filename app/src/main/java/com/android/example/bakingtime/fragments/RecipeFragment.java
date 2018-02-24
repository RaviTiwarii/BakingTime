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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.adapters.StepAdapter;
import com.android.example.bakingtime.data.local.RecipeStore;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.data.model.Step;
import com.android.example.bakingtime.util.Utils;

public class RecipeFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "com.android.example.bakingtime.extra.recipe_id";

    private Context context;
    private Recipe recipe;
    private Callback callback;

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
        if (context instanceof Callback)
            this.callback = (Callback) context;
        else
            throw new RuntimeException("activity should implement " + Callback.class.getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_RECIPE_ID)) {
            long recipeId = args.getLong(ARG_RECIPE_ID);
            RecipeStore store = RecipeStore.get(context);
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
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    private void initializeView(@NonNull final View view) {
        ingredientsTextView = view.findViewById(R.id.tv_ingredients);
        recipeStepsRecyclerView = view.findViewById(R.id.rv_recipe_steps);
    }

    private void showRecipeView() {
        setFragmentTitle(recipe.getName());

        ingredientsTextView.setText(Utils.formatIngredients(context, recipe.getIngredients()));

        recipeStepsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        recipeStepsRecyclerView.setHasFixedSize(true);
        recipeStepsRecyclerView.setAdapter(new StepAdapter(recipe.getSteps(), callback::onStepSelected));
    }

    private void setFragmentTitle(@NonNull final String title) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    @Nullable
    private ActionBar getActionBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) return activity.getSupportActionBar();
        else return null;
    }

    @FunctionalInterface
    public interface Callback {
        void onStepSelected(@NonNull Step step);
    }
}
