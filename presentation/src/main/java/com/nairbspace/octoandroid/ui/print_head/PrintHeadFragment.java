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
import android.widget.Toast;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.domain.model.PrintHeadCommand;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;
import com.nairbspace.octoandroid.views.AbstractSeekBarListener;
import com.nairbspace.octoandroid.views.SetEnableView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
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
    @Inject SetEnableView mSetEnableView;
    private Listener mListener;

    @OnClick(R.id.jog_xy_left_imageview) void xLeftClicked(){mPresenter.executeCommand(PrintHeadCommand.Type.JOG_X_LEFT);}
    @OnClick(R.id.jog_xy_right_imageview) void xRightClicked(){mPresenter.executeCommand(PrintHeadCommand.Type.JOG_X_RIGHT);}
    @OnClick(R.id.jog_xy_up_imageview) void yUpClicked() {mPresenter.executeCommand(PrintHeadCommand.Type.JOG_Y_UP);}
    @OnClick(R.id.jog_xy_down_imageview) void yDownClicked(){mPresenter.executeCommand(PrintHeadCommand.Type.JOG_Y_DOWN);}
    @OnClick(R.id.jog_xy_home_imageview) void xyHomeClicked(){mPresenter.executeCommand(PrintHeadCommand.Type.HOME_XY);}

    @OnClick(R.id.jog_z_up_imageview) void zUpClicked(){mPresenter.executeCommand(PrintHeadCommand.Type.JOG_Z_UP);}
    @OnClick(R.id.jog_z_down_imageview) void zDownClicked(){mPresenter.executeCommand(PrintHeadCommand.Type.JOG_Z_DOWN);}
    @OnClick(R.id.jog_z_home_imageview) void zHomeClicked(){mPresenter.executeCommand(PrintHeadCommand.Type.HOME_Z);}

    @BindView(R.id.jog_xyz_radiogroup) RadioGroup mRadioGroup;
    @BindView(R.id.print_head_feedrate_seekbar) SeekBar mFeedRateSeekBar;
    @BindInt(R.integer.feedrate_percent_offset) int mFeedRateOffset;
    @BindView(R.id.set_print_head_feedrate_button) Button mFeedRateSetButton;
    @OnClick(R.id.set_print_head_feedrate_button) void feedRateSetButtonClicked() {mPresenter.executeCommand(PrintHeadCommand.Type.FEEDRATE);}

    @BindViews({R.id.jog_xy_left_imageview, R.id.jog_xy_right_imageview,
            R.id.jog_xy_up_imageview, R.id.jog_xy_down_imageview,
            R.id.jog_xy_home_imageview,
            R.id.jog_z_up_imageview, R.id.jog_z_down_imageview, R.id.jog_z_home_imageview,
            R.id.jog_xyz_radiogroup, R.id.set_print_head_feedrate_button}) List<View> mEnableViews;

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

        updateFeedrateButtonText();
        mFeedRateSeekBar.setOnSeekBarChangeListener(new FeedRateListener());
        return view;
    }

    @Override
    public int getFeedRateWithOffset() {
        return mFeedRateSeekBar.getProgress() + mFeedRateOffset;
    }

    private String createFeedRateString(int feedrate) {
        return FEEDRATE + feedrate + PERCENT;
    }

    private void updateFeedrateButtonText() {
        String s = createFeedRateString(getFeedRateWithOffset());
        mFeedRateSetButton.setText(s);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_MULTIPLIER_KEY, mRadioGroup.getCheckedRadioButtonId());
        outState.putInt(SEEKBAR_PROGRESS_KEY, mFeedRateSeekBar.getProgress());
    }

    @Override
    public float getJogMultiplier() {
        int id = mRadioGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.jog_xyz_tenth_button: return mTenth;
            case R.id.jog_xyz_one_button: return mOne;
            case R.id.jog_xyz_ten_button: return mTen;
            case R.id.jog_xyz_hundred_button: return mHundred;
            default: return mOne;
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEnableViews(boolean enable) {
        ButterKnife.apply(mEnableViews, mSetEnableView, enable);
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
            updateFeedrateButtonText();
        }
    }
}
