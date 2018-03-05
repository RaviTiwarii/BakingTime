package com.android.example.bakingtime.ui.downloader;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.widget.Toast;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.local.RecipeStore;
import com.android.example.bakingtime.data.model.Recipe;

public final class RecipeDownloader {

    private static final int DELAY_MILLIS = 3000;

    private RecipeDownloader() {
    }

    public static void downloadRecipes(@NonNull Context context,
                                       @NonNull DelayerCallback<Recipe> callback,
                                       @NonNull CountingIdlingResource idlingResource) {
        idlingResource.increment();
        showLoadingMessage(context);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            callback.onDone(RecipeStore.get(context).getRecipes());
            idlingResource.decrement();
        }, DELAY_MILLIS);
    }

    private static void showLoadingMessage(@NonNull Context context) {
        String text = context.getString(R.string.loading_msg);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
