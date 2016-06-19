package com.nairbspace.octoandroid.ui.playback;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.BaseFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaybackFragment extends BaseFragmentListener<PlaybackScreen,
        PlaybackFragment.Listener> implements PlaybackScreen, View.OnClickListener {

    private static final String WEBSOCKET_MODEL_KEY = "websocket_model_key";

    @Inject PlaybackPresenter mPresenter;

    @BindView(R.id.playback_seekbar) SeekBar mPrintingSeekbar;
    @BindView(R.id.playback_print_time_textview) TextView mTimeElapsedTextView;
    @BindView(R.id.playback_print_time_left_textview) TextView mTimeLeftTextView;
    @BindView(R.id.playback_print_restart_button) ImageView mPrintRestartButton;
    @BindView(R.id.playback_pause_play_button) ImageView mPausePlayButton;
    @BindView(R.id.playback_stop_button) ImageView mStopButton;

    @BindDrawable(R.drawable.ic_pause_circle_filled_black_24dp) Drawable mPauseDrawable;
    @BindDrawable(R.drawable.ic_play_circle_filled_black_24dp) Drawable mPlayDrawable;
    @BindDrawable(R.drawable.ic_print_black_24dp) Drawable mPrintDrawable;
    @BindDrawable(R.drawable.ic_replay_black_24dp) Drawable mRestartDrawable;
    @BindDimen(R.dimen.playback_enabled_alpha_float) float mEnabledAlpha;
    @BindDimen(R.dimen.playback_disabled_alpha_float) float mDisabledAlpha;
    @BindInt(android.R.integer.config_longAnimTime) int mLongAnimTime;
    @BindColor(android.R.color.holo_red_dark) int mRedColor;
    @BindColor(android.R.color.black) int mBlackColor;

    private Listener mListener;
    private WebsocketModel mWebsocketModel;

    public static PlaybackFragment newInstance() {
        return new PlaybackFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playback_controls, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        mPrintingSeekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mPrintRestartButton.setOnClickListener(this);
        mPausePlayButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);

        setEnableView(mPrintRestartButton, false);
        setEnableView(mPausePlayButton, false);
        setEnableView(mStopButton, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getParcelable(WEBSOCKET_MODEL_KEY) != null) {
            mPresenter.renderScreen((WebsocketModel) savedInstanceState.getParcelable(WEBSOCKET_MODEL_KEY));
        }
    }

    @Override
    public void updateSeekbar(WebsocketModel websocketModel) {
        mWebsocketModel = websocketModel;
        mPrintingSeekbar.setProgress(websocketModel.completionProgress());
        mTimeElapsedTextView.setText(websocketModel.printTime());
        mTimeLeftTextView.setText(websocketModel.printTimeLeft());
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClickPressed(v.getId(), mWebsocketModel);
    }

    @Override
    public void showPrintingScreen() {
        setEnableView(mPrintRestartButton, false);
        mPrintRestartButton.setImageDrawable(mPrintDrawable);
        mPrintRestartButton.setColorFilter(mBlackColor);

        setEnableView(mPausePlayButton, true);
        mPausePlayButton.setImageDrawable(mPauseDrawable);

        setEnableView(mStopButton, true);
    }

    @Override
    public void showPausedScreen() {
        setEnableView(mPrintRestartButton, true);
        mPrintRestartButton.setImageDrawable(mRestartDrawable);
        mPrintRestartButton.setColorFilter(mRedColor);

        setEnableView(mPausePlayButton, true);
        mPausePlayButton.setImageDrawable(mPlayDrawable);

        setEnableView(mStopButton, true);
    }

    @Override
    public void showFileLoadedScreen() {
        setEnableView(mPrintRestartButton, true);
        mPrintRestartButton.setImageDrawable(mPrintDrawable);
        mPrintRestartButton.setColorFilter(mBlackColor);

        setEnableView(mPausePlayButton, false);
        setEnableView(mStopButton, false);
    }

    @Override
    public void showNoFileLoadedScreen() {
        setEnableView(mPrintRestartButton,false);
        setEnableView(mPausePlayButton,false);
        setEnableView(mStopButton,false);
    }

    @Override
    public int getPrintRestartId() {
        return mPrintRestartButton.getId();
    }

    @Override
    public int getPausePlayId() {
        return mPausePlayButton.getId();
    }

    @Override
    public int getStopId() {
        return mStopButton.getId();
    }

    @Override
    public void setWebsocketServiceAndAlarm(boolean isOn) {
        getNavigator().setWebsocketServiceAndAlarm(getContext(), isOn);
    }

    @Override
    public boolean isPrinting() {
        return mWebsocketModel != null && mWebsocketModel.printing();
    }

    private void setEnableView(View view, boolean setEnabled) {
        view.setEnabled(setEnabled);
        view.animate().setDuration(mLongAnimTime).alpha(setEnabled ? mEnabledAlpha : mDisabledAlpha);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(WEBSOCKET_MODEL_KEY, mWebsocketModel);
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected PlaybackScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    public interface Listener {
    }
}
