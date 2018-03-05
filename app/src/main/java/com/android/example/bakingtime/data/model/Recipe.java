package com.android.example.bakingtime.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Model class for recipe.
 * This class represents a recipe.
 *
 * @author Ravi Tiwari
 * @version 1.0
 * @since 1.0
 */
public class Recipe {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SERVINGS = "servings";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_IMAGE = "image";

    private final int id;
    private final int servings;
    @NonNull
    private final String name;
    @NonNull
    private final List<Ingredient> ingredients;
    @NonNull
    private final List<Step> steps;
    @NonNull
    private final String imageUrl;

    /**
     * Constructor for class Recipe
     *
     * @param id          The id of the recipe
     * @param name        The name of the recipe
     * @param ingredients The list of ingredients for the recipe
     * @param steps       The list of steps to cook recipe
     * @param servings    The total number of servings
     * @param imageUrl    The url for recipe picture
     */
    private Recipe(int id, @NonNull String name, @NonNull List<Ingredient> ingredients,
                   @NonNull List<Step> steps, int servings, @NonNull String imageUrl) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.imageUrl = imageUrl;
    }

    /**
     * This method reads recipe from json object or throws JsonException if data is invalid.
     *
     * @param jsonObject The recipe json object
     * @return The recipe object
     * @throws JSONException When data is invalid
     */
    @NonNull
    public static Recipe fromJson(@NonNull final JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt(KEY_ID);
        int servings = jsonObject.getInt(KEY_SERVINGS);
        String name = jsonObject.getString(KEY_NAME);
        String imageUrl = jsonObject.getString(KEY_IMAGE);

        JSONArray ingredientsJsonArray = jsonObject.getJSONArray(KEY_INGREDIENTS);
        List<Ingredient> ingredients = parseIngredients(ingredientsJsonArray);

        JSONArray stepsJsonArray = jsonObject.getJSONArray(KEY_STEPS);
        List<Step> steps = parseSteps(stepsJsonArray);

        return new Recipe(id, name, ingredients, steps, servings, imageUrl);
    }

    /**
     * This method reads recipe ingredients from jsonArray or throws JsonException id data is invalid
     *
     * @param jsonArray The recipe ingredients json array
     * @return List of ingredients
     * @throws JSONException When data is invalid
     */
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

    /**
     * This method reads recipe steps from jsonArray or throws JsonException id data is invalid
     *
     * @param jsonArray The recipe steps json array
     * @return list of steps
     * @throws JSONException When data is invalid
     */
    private static List<Step> parseSteps(@NonNull final JSONArray jsonArray) throws JSONException {
        List<Step> steps = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Step step = Step.fromJson(jsonObject);
            steps.add(step);
        }
        return steps;
    }

    /**
     * This method returns the id of the recipe
     *
     * @return id of the recipe
     */
    public int getId() {
        return id;
    }

    /**
     * This method returns name of the recipe
     *
     * @return name of the recipe
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * This method returns the list of recipe ingredients
     *
     * @return list of ingredients
     */
    @NonNull
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * This method finds and returns ingredient by its name, returns null if step not found.
     *
     * @param name The ingredient name
     * @return ingredient object
     */
    @Nullable
    public Ingredient getIngredient(String name) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equalsIgnoreCase(name))
                return ingredient;
        }
        return null;
    }

    /**
     * This method returns the list of recipe steps.
     *
     * @return list of steps.
     */
    @NonNull
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * This method finds and returns step by its name, returns null if step not found.
     *
     * @param id The step id
     * @return step object
     */
    @Nullable
    public Step getStep(long id) {
        for (Step step : steps) {
            if (step.getId() == id)
                return step;
        }
        return null;
    }

    /**
     * This method returns the total number of servings.
     *
     * @return number of servings
     */
    public int getServings() {
        return servings;
    }

    /**
     * This method returns the url of recipe picture.
     *
     * @return the picture url
     */
    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * This method returns the string representation of the recipe object.
     *
     * @return string representation of the recipe object
     */
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "Recipe{id=%d, name='%s'}", id, name);
    }
}