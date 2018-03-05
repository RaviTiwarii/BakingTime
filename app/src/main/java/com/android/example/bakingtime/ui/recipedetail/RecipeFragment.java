package com.android.example.bakingtime.ui.recipedetail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.local.RecipeStore;
import com.android.example.bakingtime.data.local.SharedPreferencesWidgetRecipe;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.data.model.Step;
import com.android.example.bakingtime.ui.widget.RecipeWidgetProvider;
import com.android.example.bakingtime.util.RecipeUtil;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class RecipeFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "com.android.example.bakingtime.extra.recipe_id";

    private Context context;
    private Recipe recipe;
    private Callback callback;

    public static RecipeFragment newInstance(int recipeId) {
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
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
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_RECIPE_ID)) {
            int recipeId = args.getInt(ARG_RECIPE_ID);
            RecipeStore store = RecipeStore.get(context);
            recipe = store.getRecipe(recipeId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        TextView ingredientsTextView = view.findViewById(R.id.tv_ingredients);
        RecyclerView stepsRecyclerView = view.findViewById(R.id.rv_recipe_steps);

        if (recipe != null) {
            setFragmentTitle(recipe.getName());
            ingredientsTextView.setText(RecipeUtil.formatIngredients(context, recipe.getIngredients()));

            stepsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            stepsRecyclerView.setHasFixedSize(true);
            stepsRecyclerView.addItemDecoration(new DividerItemDecoration(context, VERTICAL));
            stepsRecyclerView.setAdapter(new StepAdapter(recipe.getSteps(), callback::onStepSelected));
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recipe_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_to_widget:
                addToWidgetsAndUpdateWidget();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addToWidgetsAndUpdateWidget() {
        new SharedPreferencesWidgetRecipe(context).put(recipe.getId());
        Toast.makeText(context, R.string.recipe_added_to_widget, Toast.LENGTH_SHORT).show();

        ComponentName componentName = new ComponentName(context, RecipeWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        Intent intent = new Intent(context, RecipeWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(intent);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_ingredients);
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
