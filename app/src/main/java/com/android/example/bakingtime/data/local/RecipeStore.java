package com.android.example.bakingtime.data.local;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.example.bakingtime.data.model.Ingredient;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.data.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class RecipeStore {

    private static final String LOG_TAG = RecipeStore.class.getSimpleName();

    private final List<Recipe> recipes = new ArrayList<>();
    private final Map<Long, Recipe> recipesMap = new HashMap<>();

    public RecipeStore(@NonNull final Context context, @NonNull final String fileName) {
        if (fileName == null || fileName.isEmpty())
            return;
        InputStream stream = null;
        try {
            stream = context.getAssets().open(fileName);
            String jsonString = new Scanner(stream).useDelimiter("\\A").next();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Recipe recipe = Recipe.fromJson(jsonObject);
                recipes.add(recipe);
                recipesMap.put(recipe.getId(), recipe);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error on file handling " + e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error on parsing JSON " + e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NonNull
    public List<Recipe> getRecipes() {
        return recipes;
    }

    @Nullable
    public Recipe getRecipe(long id) {
        return recipesMap.get(id);
    }
}
