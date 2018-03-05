package com.android.example.bakingtime.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class SharedPreferencesWidgetRecipe {
    private static final String KEY_RECIPE_ID = "key.recipe_id";

    private final SharedPreferences preferences;

    public SharedPreferencesWidgetRecipe(@NonNull Context context) {
        preferences = context.getSharedPreferences("widget_recipe.xml", Context.MODE_PRIVATE);
    }

    public int get() {
        return preferences.getInt(KEY_RECIPE_ID, -1);
    }

    public void put(int recipeId) {
        preferences.edit()
                .putInt(KEY_RECIPE_ID, recipeId)
                .apply();
    }
}
