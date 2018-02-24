package com.android.example.bakingtime.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.model.Ingredient;

import java.util.List;

public final class Utils {

    private Utils() {
    }

    public static String formatIngredients(@NonNull final Context context,
                                           @NonNull final List<Ingredient> ingredients) {
        StringBuilder builder = new StringBuilder("");
        for (Ingredient ingredient : ingredients) {
            if (builder.length() != 0) builder.append("\n");
            builder.append(formatIngredient(context, ingredient));
        }
        return builder.toString();
    }

    private static String formatIngredient(@NonNull Context context, Ingredient ingredient) {
        return context.getString(R.string.ingredient_detail,
                ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getName());
    }
}
