package com.android.example.bakingtime.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private final long id;
    @NonNull
    private final String name;
    @NonNull
    private final List<Ingredient> ingredients;
    @NonNull
    private final List<Step> steps;
    private final int servings;
    @Nullable
    private final String imageUrl;

    private Recipe(final long id,
                   @NonNull final String name,
                   @NonNull final List<Ingredient> ingredients,
                   @NonNull final List<Step> steps,
                   final int servings,
                   @Nullable final String imageUrl) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.imageUrl = imageUrl;
    }

    @NonNull
    public static Recipe fromJson(@NonNull final JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("ingredients");
        List<Ingredient> ingredients = parseIngredients(jsonArray);

        JSONArray stepsJsonArray = jsonObject.getJSONArray("steps");
        List<Step> steps = parseSteps(stepsJsonArray);

        long id = jsonObject.getLong("id");
        String name = jsonObject.getString("name");
        int servings = jsonObject.getInt("servings");
        String imageUrl = jsonObject.getString("image");

        return new Recipe(id, name, ingredients, steps, servings, imageUrl);
    }

    @NonNull
    private static List<Ingredient> parseIngredients(@NonNull final JSONArray jsonArray)
            throws JSONException {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Ingredient ingredient = Ingredient.fromJson(jsonObject);
            ingredients.add(ingredient);
        }

        return ingredients;
    }

    private static List<Step> parseSteps(@NonNull final JSONArray jsonArray) throws JSONException {
        List<Step> steps = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Step step = Step.fromJson(jsonObject);
            steps.add(step);
        }

        return steps;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @NonNull
    public List<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }
}
