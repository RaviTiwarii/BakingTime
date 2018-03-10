package com.android.example.bakingtime.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.example.bakingtime.data.model.Recipe;

import org.json.JSONException;

public class SharedPreferencesWidgetRecipe {

    private static final String KEY_RECIPE = "key.recipe";

    private final SharedPreferences preferences;

    public SharedPreferencesWidgetRecipe(@NonNull Context context) {
        preferences = context.getSharedPreferences("widget_recipe.xml", Context.MODE_PRIVATE);
    }

    @Nullable
    public Recipe getRecipe() {
        Recipe recipe = null;
        String recipeJsonString = preferences.getString(KEY_RECIPE, null);
        if (!TextUtils.isEmpty(recipeJsonString)) {
            try {
                recipe = Recipe.fromJson(recipeJsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return recipe;
    }

    public boolean putRecipe(@NonNull final Recipe recipe) {
        String recipeJson = recipe.toJson();
        if (!TextUtils.isEmpty(recipeJson)) {
            preferences.edit().putString(KEY_RECIPE, recipe.toJson()).apply();
            return true;
        } else {
            return false;
        }
    }
}
