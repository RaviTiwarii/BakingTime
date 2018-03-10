package com.android.example.bakingtime.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import paperparcel.PaperParcel;

/**
 * Model class for ingredient.
 * This class represents the ingredient of a recipe.
 *
 * @author Ravi Tiwari
 * @version 1.0
 * @since 1.0
 */
@PaperParcel
public class Ingredient implements Parcelable {

    public static final Creator<Ingredient> CREATOR = PaperParcelIngredient.CREATOR;

    private static final String KEY_NAME = "ingredient";
    private static final String KEY_MEASURE = "measure";
    private static final String KEY_QUANTITY = "quantity";

    @NonNull
    private final String name;
    @NonNull
    private final String measure;
    @NonNull
    private final String quantity;

    /**
     * Constructor for class Ingredient.
     *
     * @param name     The name of the ingredient
     * @param measure  The measurement of ingredient
     * @param quantity The quantity of ingredient
     */
    public Ingredient(@NonNull String name, @NonNull String measure, @NonNull String quantity) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    /**
     * This method reads ingredient from json object or throws JsonException if data is invalid.
     *
     * @param jsonObject The step json object
     * @return ingredient object
     * @throws JSONException When data is invalid
     */
    @NonNull
    public static Ingredient fromJson(@NonNull final JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString(KEY_NAME);
        String measure = jsonObject.getString(KEY_MEASURE);
        String quantity = jsonObject.getString(KEY_QUANTITY);
        return new Ingredient(name, measure, quantity);
    }

    /**
     * This method returns the name of the ingredient.
     *
     * @return name of the ingredient
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * This method returns the measurement of the ingredient.
     *
     * @return measurement of the ingredient
     */
    @NonNull
    public String getMeasure() {
        return measure;
    }

    /**
     * This method returns the quantity of the ingredient.
     *
     * @return the quantity of the ingredient
     */
    @NonNull
    public String getQuantity() {
        return quantity;
    }

    @Nullable
    public String toJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(KEY_QUANTITY, quantity);
            jsonObject.put(KEY_MEASURE, measure);
            jsonObject.put(KEY_NAME, name);
            return jsonObject.toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        PaperParcelIngredient.writeToParcel(this, dest, flags);
    }

    /**
     * This method returns the string representation of the ingredient object.
     *
     * @return string representation of the ingredient object
     */
    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "Ingredient{name='%s', measure='%s', quantity=%s}", name, measure, quantity);
    }
}
