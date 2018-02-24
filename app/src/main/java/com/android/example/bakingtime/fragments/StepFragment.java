package com.android.example.bakingtime.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.bakingtime.R;
import com.android.example.bakingtime.data.local.RecipeStore;
import com.android.example.bakingtime.data.model.Recipe;
import com.android.example.bakingtime.data.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class StepFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "com.android.example.bakingtime.arg.recipe_id";
    private static final String ARG_STEP_ID = "com.android.example.bakingtime.arg.step_id";
    private static final String EXTRA_RECIPE_ID = "com.android.example.bakingtime.extra.recipe_id";
    private static final String EXTRA_STEP_ID = "com.android.example.bakingtime.extra.step_id";
    private static final String EXTRA_PLAY_WHEN_READY = "com.android.example.bakingtime.extra.play_when_ready";
    private static final String EXTRA_CURRENT_WINDOW = "com.android.example.bakingtime.extra.current_window";
    private static final String EXTRA_PLAYBACK_POSITION = "com.android.example.bakingtime.extra.playback_position";

    private Context context;
    private Step step;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private ImageView stepThumbnailView;
    private TextView noVideoMessageView;
    private TextView noThumbnailMessageView;
    private TextView stepDescriptionView;

    private long recipeId;
    private long stepId;
    private long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean playWhenReady = true;

    public static StepFragment newInstance(final long recipeId, final long stepId) {
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        args.putLong(ARG_STEP_ID, stepId);

        StepFragment fragment = new StepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_RECIPE_ID) && args.containsKey(ARG_STEP_ID)) {
            recipeId = args.getLong(ARG_RECIPE_ID);
            stepId = args.getLong(ARG_STEP_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        playerView = view.findViewById(R.id.player_view);
        stepThumbnailView = view.findViewById(R.id.iv_step_thumbnail);
        noVideoMessageView = view.findViewById(R.id.tv_no_video);
        noThumbnailMessageView = view.findViewById(R.id.tv_no_thumbnail);
        stepDescriptionView = view.findViewById(R.id.tv_step_description);

        Recipe recipe = RecipeStore.get(context).getRecipe(recipeId);
        if (recipe != null) {
            step = recipe.getStep(stepId);
            if (stepThumbnailView == null) {
                if (step.getVideoUrl().isEmpty()) {
                    showNoVideoView();
                } else {
                    showVideoView();
                    hideSystemUi();
                    initializePlayer();
                }
            } else {
                showStepView();
            }
        }
        return view;
    }

    private void showStepView() {
        if (step.getVideoUrl().isEmpty()) {
            showNoVideoView();
        } else {
            showVideoView();
            initializePlayer();
        }

        String thumbnailUrl = step.getThumbnailUrl();
        if (thumbnailUrl.isEmpty()) {
            showNoThumbnailView();
        } else {
            showThumbnailView();
            Picasso.with(context).load(thumbnailUrl).into(stepThumbnailView);
        }

        stepDescriptionView.setText(step.getDescription());
    }

    private void showNoVideoView() {
        playerView.setVisibility(View.INVISIBLE);
        noVideoMessageView.setVisibility(View.VISIBLE);
    }

    private void showVideoView() {
        noVideoMessageView.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
    }

    private void showNoThumbnailView() {
        stepThumbnailView.setVisibility(View.INVISIBLE);
        noThumbnailMessageView.setVisibility(View.VISIBLE);
    }

    private void showThumbnailView() {
        noThumbnailMessageView.setVisibility(View.INVISIBLE);
        stepThumbnailView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            if (step.getVideoUrl().isEmpty()) {
                showNoVideoView();
            } else {
                showVideoView();
                initializePlayer();
            }
        }
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(context),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri mediaUri = Uri.parse(step.getVideoUrl());
        MediaSource mediaSource = buildMediaSource(mediaUri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri mediaUri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("BakingTime"))
                .createMediaSource(mediaUri);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    private void saveState(@NonNull Bundle outState) {
        outState.putLong(EXTRA_RECIPE_ID, recipeId);
        outState.putLong(EXTRA_STEP_ID, stepId);
        outState.putBoolean(EXTRA_PLAY_WHEN_READY, playWhenReady);
        outState.putInt(EXTRA_CURRENT_WINDOW, currentWindow);
        outState.putLong(EXTRA_PLAYBACK_POSITION, playbackPosition);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) restoreState(savedInstanceState);
    }

    private void restoreState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(EXTRA_RECIPE_ID))
            recipeId = savedInstanceState.getLong(EXTRA_RECIPE_ID);

        if (savedInstanceState.containsKey(EXTRA_STEP_ID))
            stepId = savedInstanceState.getLong(EXTRA_STEP_ID);

        if (savedInstanceState.containsKey(EXTRA_PLAY_WHEN_READY))
            playWhenReady = savedInstanceState.getBoolean(EXTRA_PLAY_WHEN_READY);

        if (savedInstanceState.containsKey(EXTRA_CURRENT_WINDOW))
            currentWindow = savedInstanceState.getInt(EXTRA_CURRENT_WINDOW);

        if (savedInstanceState.containsKey(EXTRA_PLAYBACK_POSITION))
            playbackPosition = savedInstanceState.getLong(EXTRA_PLAYBACK_POSITION);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}