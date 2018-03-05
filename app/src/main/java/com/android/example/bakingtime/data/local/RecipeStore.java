package com.android.example.bakingtime.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.example.bakingtime.data.model.Recipe;

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

/**
 * This class represents the local in memory data store for the recipes.
 *
 * @author Ravi Tiwari
 * @version 1.0
 * @since 1.0
 */
public class RecipeStore {

    private static final String LOG_TAG = RecipeStore.class.getSimpleName();
    private static final String FILE_NAME = "baking.json";
    private static RecipeStore instance;
    private final List<Recipe> recipes = new ArrayList<>();
    private final Map<Integer, Recipe> recipesMap = new HashMap<>();

    /**
     * Constructor of RecipeStore
     *
     * @param context the app context.
     */
    private RecipeStore(@NonNull Context context) {
        InputStream stream = null;
        try {
            stream = context.getAssets().open(FILE_NAME);
            parse(stream);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error on file handling " + e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error on parsing JSON " + e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error on closing stream " + e);
                }
            }
        }
    }

    /**
     * This method returns the singleton instance of RecipeStore
     *
     * @param context The app context
     * @return instance of recipe store
     */
    public static RecipeStore get(@NonNull final Context context) {
        if (instance == null) instance = new RecipeStore(context);
        return instance;
    }

    /**
     * This method reads the json data from input stream and parse recipes.
     *
     * @param stream The given input stream
     * @throws JSONException when data is invalid
     */
    private void parse(@NonNull final InputStream stream) throws JSONException {
        String jsonString = new Scanner(stream).useDelimiter("\\A").next();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Recipe recipe = Recipe.fromJson(jsonObject);
            recipes.add(recipe);
            recipesMap.put(recipe.getId(), recipe);
        }
    }

    /**
     * This method returns the list of recipes.
     *
     * @return the list of recipes
     */
    @NonNull
    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * This method finds and returns recipe by its id, returns null if recipe not found.
     *
     * @param id The recipe id
     * @return recipe object
     */
    @Nullable
    public Recipe getRecipe(int id) {
        return recipesMap.get(id);
    }
}
