package com.android.example.bakingtime.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.local.SharedPreferencesWidgetRecipe;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.ui.recipedetail.RecipeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        Recipe recipe = new SharedPreferencesWidgetRecipe(context).getRecipe();
        if (recipe == null) {
            views.setTextViewText(R.id.tv_recipe_name, context.getString(R.string.no_recipe_in_widget));
        } else {
            views.setTextViewText(R.id.tv_recipe_name, recipe.getName());
            Intent intent = new Intent(context, RecipeWidgetRemoteViewService.class);
            views.setRemoteAdapter(R.id.list_view_ingredients, intent);

            Intent recipeIntent = RecipeActivity.newIntent(context, recipe);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, recipeIntent, 0);
            views.setOnClickPendingIntent(R.id.tv_recipe_name, pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds)
            updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

