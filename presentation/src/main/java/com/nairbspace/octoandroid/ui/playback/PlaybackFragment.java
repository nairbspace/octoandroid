package com.nairbspace.octoandroid.ui.playback;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.StatusModel;
import com.nairbspace.octoandroid.ui.BaseFragmentListener;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaybackFragment extends BaseFragmentListener<PlaybackScreen,
        PlaybackFragment.Listener> implements PlaybackScreen {

    private static final String WEBSOCKET_MODEL_KEY = "websocket_model_key";
    private StatusModel mStatusModel;

    @Inject PlaybackPresenter mPresenter;

    @BindView(R.id.playback_seekbar) SeekBar mPrintingSeekbar;
    @BindView(R.id.playback_print_time_textview) TextView mTimeElapsedTextView;
    @BindView(R.id.playback_print_time_left_textview) TextView mTimeLeftTextView;
    @BindView(R.id.playback_print_button) ImageView mPrintButton;
    @BindView(R.id.playback_pause_play_button) ImageView mPausePlayButton;
    @BindView(R.id.playback_stop_button) ImageView mStopButton;

    @Override
    public void updateUi(StatusModel statusModel) {
        mStatusModel = statusModel;
        mPrintingSeekbar.setProgress(statusModel.completionProgress());
        mTimeElapsedTextView.setText(statusModel.printTime());
        mTimeLeftTextView.setText(statusModel.printTimeLeft());
    }

    private Listener mListener;

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

        if (savedInstanceState != null && savedInstanceState.getParcelable(WEBSOCKET_MODEL_KEY) != null) {
            updateUi((StatusModel) savedInstanceState.getParcelable(WEBSOCKET_MODEL_KEY));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(WEBSOCKET_MODEL_KEY, mStatusModel);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        void onFragmentInteraction(Uri uri);
    }
}
