package com.app.android.bakingapp.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.android.bakingapp.R;
import com.app.android.bakingapp.model.Recipe;
import com.app.android.bakingapp.utilities.JsonUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepFragment extends Fragment {

    private static final String TAG = com.app.android.bakingapp.activity.StepActivity.class.getSimpleName();
    private static final String RECIPE_NAME = "recipe_name";
    private static final String PLAYER_POSITION = "player_position";
    private static final String CURRENT_STEP = "current_step";
    private static final String STEPS_STRING = "steps_string";
    private static final String PLAY_WHEN_READY = "play_when_ready";
    private boolean playWhenReady;
    private long seekPosition = 0;
    private List<Recipe.Step> mSteps;
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private String mVideoURL;
    private int mStepID;
    private String mName;
    private String mStepsString;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.short_description_tv)
    TextView mDescriptionTV;
    @BindView(R.id.instruction_tv)
    TextView mInstructionTV;
    @BindView(R.id.previous_iv)
    ImageView mPreviousButton;
    @BindView(R.id.previous_tv)
    TextView mPreviousText;
    @BindView(R.id.total_steps)
    TextView mTotalSteps;
    @BindView(R.id.next_tv)
    TextView mNextText;
    @BindView(R.id.next_iv)
    ImageView mNextButton;
    @BindView(R.id.exo_prev)
    ImageView mExoPrevious;
    @BindView(R.id.exo_next_btn)
    ImageView mExoNext;
    @BindView(R.id.thumbnail_image_view)
    ImageView mThumbnail;


    // Empty Constructor
    public StepFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this, rootView);

        // Getting data from Intent / saved instance state
        if (savedInstanceState != null) {
            seekPosition = savedInstanceState.getLong(PLAYER_POSITION);
            mStepID = savedInstanceState.getInt(CURRENT_STEP);
            mStepsString = savedInstanceState.getString(STEPS_STRING);
            mName = savedInstanceState.getString(RECIPE_NAME);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        } else {
            Intent intent = getActivity().getIntent();
            mStepID = intent.getIntExtra(Recipe.Step.STEP_ID, 0);
            mStepsString = intent.getStringExtra(Recipe.RECIPE_STEPS);
            mName = intent.getStringExtra(Recipe.RECIPE_NAME);
            playWhenReady = true; // to start playing if not in saved instance state
        }
        // parsing steps string
        mSteps = JsonUtils.parseSteps(mStepsString);

        populateUI();
        return rootView;
    }

    // Method to create UI
    private void populateUI() {

        if (mSteps.get(mStepID).getStepThumbnailURL().isEmpty()) {
            // Setting video artwork to loading if no thumbnail available
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.loading_vid));
            mThumbnail.setVisibility(View.GONE);
        } else {
            Picasso.with(getContext()).load(mSteps.get(mStepID).getStepThumbnailURL()).into(mThumbnail);
            mPlayerView.setVisibility(View.INVISIBLE);
        }

        // getting value to populate UI
        String shortDescription = mSteps.get(mStepID).getStepShortDescription();
        String instruction = mSteps.get(mStepID).getStepsDescription();
        mVideoURL = mSteps.get(mStepID).getStepVideoURL();
        String totalSteps = (mStepID + 1) + "/" + mSteps.size();

        // Setting string values
        getActivity().setTitle(mName);
        mDescriptionTV.setText(shortDescription);
        mInstructionTV.setText(instruction);
        mTotalSteps.setText(totalSteps);

        // initializing Media Session and player
        initializeMediaSession();
        initializePlayer();

    }

    // Saving current state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        seekPosition = mExoPlayer.getCurrentPosition();
        outState.putLong(PLAYER_POSITION, seekPosition);
        outState.putInt(CURRENT_STEP, mStepID);
        outState.putString(STEPS_STRING, mStepsString);
        outState.putString(RECIPE_NAME, mName);
        playWhenReady = mExoPlayer.getPlayWhenReady();
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        super.onSaveInstanceState(outState);
    }

    private void initializeMediaSession() {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);
        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    private void initializePlayer() {

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);

            // if landscape && not tablet go full screen mode (screenHeightDp in landscape = screen width in portrait)
            // phones width is less than 500
            if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) &&
                    getResources().getConfiguration().screenHeightDp < 500) {
                mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            }
            mPlayerView.setPlayer(mExoPlayer);
        }

        String userAgent = Util.getUserAgent(getActivity(), "RecipeStepVideo");
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mVideoURL), new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);

        mThumbnail.setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
        // if no video show "video unavailable screen
        if (mVideoURL.isEmpty()) {
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.no_vid));
        }
        mExoPlayer.prepare(mediaSource);
        // play when media is available at start if no saveInstanceState value existed
        mExoPlayer.setPlayWhenReady(playWhenReady);

        // If exo player was playing it will seek to te position, if not it wil seek to initial value of 0
        mExoPlayer.seekTo(seekPosition);
    }

    // Method to release media resources
    private void releaseMediaPlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayerView == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            seekPosition = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            releaseMediaPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaSession.setActive(false);
        if (Util.SDK_INT > 23) {
            releaseMediaPlayer();
        }
    }

    // Buttons Callbacks
    @OnClick(R.id.previous_iv)
    public void previousButton() {
        navigateToStep(mStepID - 1);
    }

    @OnClick(R.id.previous_tv)
    public void previousText() {
        navigateToStep(mStepID - 1);
    }

    @OnClick(R.id.exo_prev)
    public void previousVid() {
        // if clicked previous video button (inside player) then seek to 0
        if (mExoPlayer.getCurrentPosition() > 1000) {
            mExoPlayer.seekTo(0);
        } else {
            navigateToStep(mStepID - 1);
        }
    }

    @OnClick(R.id.next_tv)
    public void nextButton() {
        navigateToStep(mStepID + 1);
    }

    @OnClick(R.id.next_iv)
    public void nextText() {
        navigateToStep(mStepID + 1);
    }

    @OnClick(R.id.exo_next_btn)
    public void nextVid() {
        navigateToStep(mStepID + 1);
    }

    // General method to move to next / previous video step if available
    public void navigateToStep(int stepID) {
        // resetting seek position
        seekPosition = 0;
        // loading new step if with in bounds
        if (stepID != mSteps.size() && stepID >= 0) {
            mStepID = stepID;
            populateUI();
        }
    }
}