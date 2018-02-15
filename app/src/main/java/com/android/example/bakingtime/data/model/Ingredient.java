package com.android.example.bakingtime.data.model;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represent the ingredients of a recipe.
 *
 * @author Ravi Tiwari
 * @version 1.0
 */
public class Ingredient {

    @NonNull
    private final String name;
    @NonNull
    private final String measure;
    private final float quantity;

    /**
     * Returns the new instance of name.
     *
     * @param name     Name of the ingredient
     * @param measure  Measurement of ingredient
     * @param quantity Quantity of ingredient
     * @return new instance of Ingredient
     */
    private Ingredient(@NonNull final String name, @NonNull final String measure, float quantity) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    /**
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    @NonNull
    public static Ingredient fromJson(@NonNull final JSONObject jsonObject)
            throws JSONException {
        String name = jsonObject.getString("ingredient");
        String measure = jsonObject.getString("measure");
        float quantity = (float) jsonObject.getDouble("quantity");

        return new Ingredient(name, measure, quantity);
    }

    /**
     * Returns the name of the ingredient.
     *
     * @return name of the ingredient
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Returns the measurement of the ingredient.
     *
     * @return measurement of the ingredient
     */
    @NonNull
    public String getMeasure() {
        return measure;
    }

    /**
     * Returns the quantity of the ingredient.
     *
     * @return the quantity of the ingredient
     */
    public float getQuantity() {
        return quantity;
    }
}
