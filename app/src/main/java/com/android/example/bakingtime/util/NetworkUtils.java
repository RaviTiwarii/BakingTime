package com.android.example.bakingtime.util;

import android.support.annotation.NonNull;

import com.android.example.bakingtime.data.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public final class NetworkUtils {

    private NetworkUtils() {
    }

    @NonNull
    public static ArrayList<Recipe> downloadRecipes(@NonNull final URL url) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Recipe recipe = Recipe.fromJson(jsonObject.toString(2));
                recipes.add(recipe);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
