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
 * Model class for step
 * This class represents the step to cook recipe
 *
 * @author Ravi Tiwari
 * @version 1.0
 * @since 1.0
 */
@PaperParcel
public class Step implements Parcelable {

    public static final Creator<Step> CREATOR = PaperParcelStep.CREATOR;

    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_SHORT_DESCRIPTION = "shortDescription";
    private static final String KEY_VIDEO_URL = "videoURL";
    private static final String KEY_THUMBNAIL_URL = "thumbnailURL";

    private final int id;
    @NonNull
    private final String description;
    @NonNull
    private final String shortDescription;
    @NonNull
    private final String videoUrl;
    @NonNull
    private final String thumbnailUrl;

    /**
     * Constructor for Step
     *
     * @param id               The id of step
     * @param description      The description of step.
     * @param shortDescription The short description of step
     * @param videoUrl         The video url of step
     * @param thumbnailUrl     The thumbnail url of the step
     */
    public Step(int id, @NonNull String description, @NonNull String shortDescription,
                @NonNull String videoUrl, @NonNull String thumbnailUrl) {
        this.id = id;
        this.description = description;
        this.shortDescription = shortDescription;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * This method reads step from json object or throws JsonException if data is invalid.
     *
     * @param jsonObject step json object
     * @return the step object
     * @throws JSONException When data is invalid
     */
    @NonNull
    public static Step fromJson(@NonNull final JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt(KEY_ID);
        String description = jsonObject.getString(KEY_DESCRIPTION);
        String shortDescription = jsonObject.getString(KEY_SHORT_DESCRIPTION);
        String videoUrl = jsonObject.getString(KEY_VIDEO_URL);
        String thumbnailUrl = jsonObject.getString(KEY_THUMBNAIL_URL);
        return new Step(id, description, shortDescription, videoUrl, thumbnailUrl);
    }

    /**
     * This method returns the id of the step
     *
     * @return id of the step
     */
    public int getId() {
        return id;
    }

    /**
     * This method returns the description of the step
     *
     * @return description of the step
     */
    @NonNull
    public String getDescription() {
        return description;
    }

    /**
     * This method returns the short description of the step
     *
     * @return short description of the step
     */
    @NonNull
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * This method returns the video url of the step
     *
     * @return video url
     */
    @NonNull
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * This method returns the thumbnail url of the step
     *
     * @return thumbnail url
     */
    @NonNull
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Nullable
    public String toJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(KEY_ID, id);
            jsonObject.put(KEY_SHORT_DESCRIPTION, shortDescription);
            jsonObject.put(KEY_DESCRIPTION, description);
            jsonObject.put(KEY_VIDEO_URL, videoUrl);
            jsonObject.put(KEY_THUMBNAIL_URL, thumbnailUrl);
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
        PaperParcelStep.writeToParcel(this, dest, flags);
    }

    /**
     * This method returns the string representation of the step object.
     *
     * @return string representation of the step object.
     */
    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "Step{id=%d, shortDescription='%s'}", id, shortDescription);
    }
}
