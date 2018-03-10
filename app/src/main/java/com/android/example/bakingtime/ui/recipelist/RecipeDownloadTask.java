package com.android.example.bakingtime.ui.recipelist;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.util.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class RecipeDownloadTask extends AsyncTask<String, Void, ArrayList<Recipe>> {

    private static final String TAG = RecipeDownloadTask.class.getSimpleName();

    @NonNull
    private TaskCallback<Recipe> callback;

    public RecipeDownloadTask(@NonNull TaskCallback<Recipe> callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onPreExecute();
    }

    @Override
    protected ArrayList<Recipe> doInBackground(String... urls) {
        ArrayList<Recipe> recipes = null;
        if (urls != null && urls.length > 0) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                recipes = NetworkUtils.downloadRecipes(url);
            } catch (Exception e) {
                Log.e(TAG, "Error on getting recipes", e);
            }
        }
        return recipes;
    }

    @Override
    protected void onPostExecute(ArrayList<Recipe> recipes) {
        super.onPostExecute(recipes);
        callback.onPostExecute(recipes);
    }

    public interface TaskCallback<T> {
        void onPreExecute();

        void onPostExecute(ArrayList<T> items);
    }
}
