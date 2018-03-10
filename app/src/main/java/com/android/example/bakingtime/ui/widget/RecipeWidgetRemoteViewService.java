package com.android.example.bakingtime.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.local.SharedPreferencesWidgetRecipe;
import com.android.example.bakingtime.data.model.Ingredient;
import com.android.example.bakingtime.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ravi Tiwari
 * @version 1.0
 */
public class RecipeWidgetRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetRemoteViewsFactory(getApplicationContext());
    }

    private static class RecipeWidgetRemoteViewsFactory implements RemoteViewsFactory {

        private final Context context;
        private List<Ingredient> ingredients;

        public RecipeWidgetRemoteViewsFactory(@NonNull Context context) {
            this.context = context;
            this.ingredients = new ArrayList<>();
        }

        @Override
        public void onCreate() {
            fetchIngredients();
        }

        @Override
        public void onDataSetChanged() {
            fetchIngredients();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Ingredient ingredient = ingredients.get(position);
            String s = context.getString(R.string.ingredient_detail,
                    ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getName());
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.cell_wigdet_recipe_ingredient);
            remoteViews.setTextViewText(R.id.tv_ingredient, s);
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        private void fetchIngredients() {
            Recipe recipe = new SharedPreferencesWidgetRecipe(context).getRecipe();
            if (recipe != null)
                ingredients = recipe.getIngredients();
        }
    }
}
