package com.android.example.bakingtime.ui.step;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
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

    private static final String ARG_STEP = "arg.step";
    private static final String STATE_STEP = "state.step";
    private static final String STATE_PLAY_WHEN_READY = "state.play_when_ready";
    private static final String STATE_CURRENT_WINDOW = "state.current_window";
    private static final String STATE_PLAYBACK_POSITION = "state.playback_position";

    private Context context;
    private Step step;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private ImageView stepThumbnailView;
    private TextView noVideoMessageView;
    private TextView stepDescriptionView;

    private long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean playWhenReady = true;

    @NonNull
    public static StepFragment newInstance(@NonNull final Step step) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, step);

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

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_STEP))
            step = args.getParcelable(ARG_STEP);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        playerView = view.findViewById(R.id.player_view);
        stepThumbnailView = view.findViewById(R.id.iv_step_thumbnail);
        noVideoMessageView = view.findViewById(R.id.tv_no_video);
        stepDescriptionView = view.findViewById(R.id.tv_step_description);

        if (step != null) {
            if (isLandscapeMode()) showLandscapeView();
            else showPortraitView();
        }
        return view;
    }

    private void showLandscapeView() {
        hideSystemUi();
        showStepVideo();
    }

    private void showPortraitView() {
        showStepVideo();
        showStepThumbnail();
        stepDescriptionView.setText(step.getDescription());
    }

    private void showStepVideo() {
        if (step.getVideoUrl().isEmpty()) {
            showNoVideoView();
        } else {
            showVideoView();
            initializePlayer();
        }
    }

    private void showStepThumbnail() {
        String thumbnailUrl = step.getThumbnailUrl();
        if (thumbnailUrl.isEmpty())
            stepThumbnailView.setImageResource(R.drawable.placeholder);
        else
            Picasso.with(context).load(thumbnailUrl).into(stepThumbnailView);
    }

    private void showNoVideoView() {
        playerView.setVisibility(View.GONE);
        noVideoMessageView.setVisibility(View.VISIBLE);
    }

    private void showVideoView() {
        noVideoMessageView.setVisibility(View.GONE);
        playerView.setVisibility(View.VISIBLE);
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
        outState.putParcelable(STATE_STEP, step);
        outState.putBoolean(STATE_PLAY_WHEN_READY, playWhenReady);
        outState.putInt(STATE_CURRENT_WINDOW, currentWindow);
        outState.putLong(STATE_PLAYBACK_POSITION, playbackPosition);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) restoreState(savedInstanceState);
    }

    private void restoreState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_STEP))
            step = savedInstanceState.getParcelable(STATE_STEP);

        if (savedInstanceState.containsKey(STATE_PLAY_WHEN_READY))
            playWhenReady = savedInstanceState.getBoolean(STATE_PLAY_WHEN_READY);

        if (savedInstanceState.containsKey(STATE_CURRENT_WINDOW))
            currentWindow = savedInstanceState.getInt(STATE_CURRENT_WINDOW);

        if (savedInstanceState.containsKey(STATE_PLAYBACK_POSITION))
            playbackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION);
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

    private boolean isLandscapeMode() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}