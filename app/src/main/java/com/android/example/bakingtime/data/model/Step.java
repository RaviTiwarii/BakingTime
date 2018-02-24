package com.android.example.bakingtime.data.model;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Step {

    private final long id;
    private final String description;
    private final String shortDescription;
    private final String videoUrl;
    private final String thumbnailUrl;

    private Step(long id, @NonNull String description, @NonNull String shortDescription,
                 @NonNull String videoUrl, @NonNull String thumbnailUrl) {
        this.id = id;
        this.description = description;
        this.shortDescription = shortDescription;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    @NonNull
    public static Step fromJson(@NonNull final JSONObject jsonObject) throws JSONException {
        long id = jsonObject.getLong("id");
        String description = jsonObject.getString("description");
        String shortDescription = jsonObject.getString("shortDescription");
        String videoUrl = jsonObject.getString("videoURL");
        String thumbnailUrl = jsonObject.getString("thumbnailURL");

        return new Step(id, description, shortDescription, videoUrl, thumbnailUrl);
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getShortDescription() {
        return shortDescription;
    }

    @NonNull
    public String getVideoUrl() {
        return videoUrl;
    }

    @NonNull
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
