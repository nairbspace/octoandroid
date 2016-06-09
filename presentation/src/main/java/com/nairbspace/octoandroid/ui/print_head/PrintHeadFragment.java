package com.nairbspace.octoandroid.ui.print_head;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;
import com.nairbspace.octoandroid.views.AbstractSeekBarListener;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintHeadFragment extends BasePagerFragmentListener<PrintHeadScreen, PrintHeadFragment.Listener>
        implements PrintHeadScreen {

    private static final String SELECTED_MULTIPLIER_KEY = "selected_multiplier_key";
    private static final String SEEKBAR_PROGRESS_KEY = "seekbar_progress_key";

    @BindString(R.string.feed_rate_colon_space) String FEEDRATE;
    @BindString(R.string.percent) String PERCENT;

    @BindDimen(R.dimen.jog_tenth_multiplier) float mTenth;
    @BindDimen(R.dimen.jog_one_multiplier) float mOne;
    @BindDimen(R.dimen.jog_ten_multiplier) float mTen;
    @BindDimen(R.dimen.jog_hundred_multiplier) float mHundred;

    @Inject PrintHeadPresenter mPresenter;
    private Listener mListener;

    @OnClick(R.id.jog_xy_up_imageview) void xyUpClicked() {}
    @OnClick(R.id.jog_xy_left_imageview) void xyLeftClicked(){}
    @OnClick(R.id.jog_xy_right_imageview) void xyRightClicked(){}
    @OnClick(R.id.jog_xy_down_imageview) void xyDownClicked(){}
    @OnClick(R.id.jog_xy_home_imageview) void xyHomeClicked(){}

    @OnClick(R.id.jog_z_up_imageview) void mZUpImageView(){}
    @OnClick(R.id.jog_z_down_imageview) void mZDownImageView(){}
    @OnClick(R.id.jog_z_home_imageview) void mZHomeImageView(){}

    @BindView(R.id.jog_xyz_radiogroup) RadioGroup mRadioGroup;
    @BindView(R.id.print_head_feedrate_seekbar) SeekBar mFeedRateSeekBar;
    @BindInt(R.integer.feedrate_percent_offset) int mFeedRateOffset;
    @BindView(R.id.set_print_head_feedrate_button) Button mFeedRateSetButton;
    @OnClick(R.id.set_print_head_feedrate_button) void feedRateSetButtonClicked() {
        int feedRate = mFeedRateSeekBar.getProgress() + mFeedRateOffset;
    }

    public static PrintHeadFragment newInstance() {
        return new PrintHeadFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_controls_xyz, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        if (savedInstanceState != null) {
            mRadioGroup.check(savedInstanceState.getInt(SELECTED_MULTIPLIER_KEY));
            mFeedRateSeekBar.setProgress(savedInstanceState.getInt(SEEKBAR_PROGRESS_KEY));
        }

        FeedRateListener feedRateListener = new FeedRateListener();
        feedRateListener.setFeedRateButtonText(mFeedRateSeekBar.getProgress());
        mFeedRateSeekBar.setOnSeekBarChangeListener(feedRateListener);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_MULTIPLIER_KEY, mRadioGroup.getCheckedRadioButtonId());
        outState.putInt(SEEKBAR_PROGRESS_KEY, mFeedRateSeekBar.getProgress());
    }

    @Override
    public float getFeedRateMultiplier() {
        int id = mRadioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.jog_xyz_tenth_button: return mTenth;
            case R.id.jog_xyz_one_button: return mOne;
            case R.id.jog_xyz_ten_button: return mTen;
            case R.id.jog_xyz_hundred_button: return mHundred;
            default: return mOne;
        }
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected PrintHeadScreen setScreen() {
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

    private final class FeedRateListener extends AbstractSeekBarListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setFeedRateButtonText(progress);
        }

        private int feedRateWithOffset(int progress) {
            return progress + mFeedRateOffset;
        }

        private String feedRateButtonString(int feedrate) {
            return FEEDRATE + feedrate + PERCENT;
        }

        private void setFeedRateButtonText(int progress) {
            String feedRatePercent = feedRateButtonString(feedRateWithOffset(progress));
            mFeedRateSetButton.setText(feedRatePercent);
        }
    }
}
