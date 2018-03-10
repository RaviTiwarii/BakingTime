package com.android.example.bakingtime.ui.recipedetail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.android.example.bakingtime.data.local.SharedPreferencesWidgetRecipe;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.data.model.Step;
import com.android.example.bakingtime.ui.widget.RecipeWidgetProvider;
import com.android.example.bakingtime.util.RecipeUtil;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class RecipeFragment extends Fragment {

    private static final String ARG_RECIPE = "arg.recipe";
    private static final String STATE_STEP_SCROLL = "state.step_scroll";

    private Context context;
    private Recipe recipe;
    private Callback callback;
    private RecyclerView stepsRecyclerView;
    private Parcelable stepListState;

    @NonNull
    public static RecipeFragment newInstance(@NonNull final Recipe recipe) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);

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
        if (args != null && args.containsKey(ARG_RECIPE))
            recipe = args.getParcelable(ARG_RECIPE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        TextView ingredientsTextView = view.findViewById(R.id.tv_ingredients);
        stepsRecyclerView = view.findViewById(R.id.rv_recipe_steps);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_STEP_SCROLL))
            stepListState = savedInstanceState.getParcelable(STATE_STEP_SCROLL);

        if (recipe != null) {
            getActivity().setTitle(recipe.getName());

            ingredientsTextView.setText(RecipeUtil.formatIngredients(context, recipe.getIngredients()));

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            if (stepListState != null)
                layoutManager.onRestoreInstanceState(stepListState);

            stepsRecyclerView.setLayoutManager(layoutManager);
            stepsRecyclerView.setHasFixedSize(true);
            stepsRecyclerView.addItemDecoration(new DividerItemDecoration(context, VERTICAL));
            stepsRecyclerView.setAdapter(new StepAdapter(recipe.getSteps(), callback::onStepSelected));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        stepListState = stepsRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(STATE_STEP_SCROLL, stepListState);
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
        boolean isAdded = new SharedPreferencesWidgetRecipe(context).putRecipe(recipe);
        if (isAdded) {
            Toast.makeText(context, R.string.recipe_added_to_widget, Toast.LENGTH_SHORT).show();
            ComponentName componentName = new ComponentName(context, RecipeWidgetProvider.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

            Intent intent = new Intent(context, RecipeWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            context.sendBroadcast(intent);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_ingredients);
        } else {
            Toast.makeText(context, R.string.recipe_not_added_to_widget, Toast.LENGTH_SHORT).show();
        }
    }

    @FunctionalInterface
    public interface Callback {
        void onStepSelected(@NonNull Step step);
    }
}
