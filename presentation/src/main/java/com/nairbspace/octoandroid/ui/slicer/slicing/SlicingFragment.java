package com.nairbspace.octoandroid.ui.slicer.slicing;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.model.SlicingCommandModel;
import com.nairbspace.octoandroid.model.SlicingProgressModel;
import com.nairbspace.octoandroid.model.SpinnerModel;
import com.nairbspace.octoandroid.ui.templates.BaseFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlicingFragment extends BaseFragmentListener<SlicingScreen, SlicingFragment.Listener>
        implements SlicingScreen, SwipeRefreshLayout.OnRefreshListener {

    private static final int UPDATE_FILES_TIMER = 2000; // in milliseconds
    private static final int UPDATE_SLICER_PROFILE_DELAY = 100; // in milliseconds

    private static final String SLICER_SCREEN_MODEL_KEY = "slicer_screen_model_key";
    private static final String API_URL_KEY = "api_url_key";
    @BindString(R.string.dot_gco) String DOT_GCO;
    @BindString(R.string.colon_space) String COLON_SPACE;
    @BindString(R.string.slicer) String SLICER;
    @BindString(R.string.source_location) String SOURCE_LOCATION;
    @BindString(R.string.source_file) String SOURCE_FILE;
    @BindString(R.string.destination_location) String DEST_LOCATION;
    @BindString(R.string.destination_file) String DEST_FILE;
    @BindString(R.string.exception_slicing_parameters_missing) String SLICING_PARAMETERS_MISSING;
    @BindString(R.string.slice_complete) String SLICE_COMPLETE;

    @Inject SlicingPresenter mPresenter;
    private Listener mListener;

    @BindView(R.id.slicing_refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.slicer_spinner) Spinner mSlicerSpinner;
    @BindView(R.id.slicer_profile_spinner) Spinner mSlicerProfileSpinner;
    @BindView(R.id.printer_profile_spinner) Spinner mPrinterProfileSpinner;
    @BindView(R.id.after_slicing_spinner) Spinner mAfterSlicingSpinner;
    @BindView(R.id.slicer_gcode_filename) TextView mFileNameTextView;
    @BindView(R.id.slice_button) Button mSliceButton;
    @BindArray(R.array.after_slicing_list) String[] mAfterSlicingArray;

    @BindView(R.id.slicing_config_layout) View mConfigView;
    @BindView(R.id.slicing_progress_layout) View mProgressView;

    @BindView(R.id.slicing_slicer) TextView mSlicerText;
    @BindView(R.id.slicing_source_location) TextView mSrcLocText;
    @BindView(R.id.slicing_source_file) TextView mSrcFileText;
    @BindView(R.id.slicing_destination_location) TextView mDestLocText;
    @BindView(R.id.slicing_destination_file) TextView mDestFileText;
    @BindView(R.id.slicing_progress_bar) ProgressBar mProgressBar;

    @Override
    public void updateProgress(SlicingProgressModel progressModel) {
        showProgress(true);

        String slicer = SLICER + COLON_SPACE + progressModel.slicer();
        String srcLoc = SOURCE_LOCATION + COLON_SPACE + progressModel.sourceLocation();
        String srcFile = SOURCE_FILE + COLON_SPACE + progressModel.sourcePath();
        String destLoc = DEST_LOCATION + COLON_SPACE + progressModel.destLocation();
        String destFile = DEST_FILE + COLON_SPACE + progressModel.destPath();

        mSlicerText.setText(slicer);
        mSrcLocText.setText(srcLoc);
        mSrcFileText.setText(srcFile);
        mDestLocText.setText(destLoc);
        mDestFileText.setText(destFile);
        mProgressBar.setProgress(progressModel.progress());
    }

    @Override
    public void showProgress(boolean show) {
        mConfigView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show? View.VISIBLE: View.GONE);
    }

    private List<SlicerModel> mSlicerModels;
    private List<SpinnerModel> mPrinterProfiles;
    private String mApiUrl;

    private SlicingCommandModel.Builder getSlicingCommandModelBuilder() {
        return SlicingCommandModel.builder()
                .slicerPosition(mSlicerSpinner.getSelectedItemPosition())
                .slicingProfilePosition(mSlicerProfileSpinner.getSelectedItemPosition())
                .printerProfilePosition(mPrinterProfileSpinner.getSelectedItemPosition())
                .afterSlicingPosition(mAfterSlicingSpinner.getSelectedItemPosition())
                .apiUrl(mApiUrl)
                .slicerModels(mSlicerModels)
                .printerProfiles(mPrinterProfiles)
                .afterSlicingList(Arrays.asList(mAfterSlicingArray));
    }

    public static SlicingFragment newInstance(@Nullable String apiUrl) {
        Bundle args = new Bundle();
        args.putString(API_URL_KEY, apiUrl);
        SlicingFragment slicingFragment = new SlicingFragment();
        slicingFragment.setArguments(args);
        return slicingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slicing_settings, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        mRefreshLayout.setOnRefreshListener(this);
        mSlicerSpinner.setOnItemSelectedListener(mSlicerListener);
        mSliceButton.setEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().getString(API_URL_KEY) != null) {
            String apiUrl = getArguments().getString(API_URL_KEY);
            setApiUrl(apiUrl);
        }
        if (savedInstanceState == null) mPresenter.execute();
        else restoreSavedInstanceState(savedInstanceState);
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        SlicingCommandModel model = savedInstanceState.getParcelable(SLICER_SCREEN_MODEL_KEY);
        if (model == null) return;
        updateSlicer(model.slicerModels());
        updatePrinterProfile(model.printerProfiles());
        restoreSpinnerPositions(model);
        String apiUrl = model.apiUrl();
        if (!TextUtils.isEmpty(apiUrl)) setApiUrl(apiUrl);
    }

    private void restoreSpinnerPositions(final SlicingCommandModel model) {
        mSlicerSpinner.setSelection(model.slicerPosition());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSlicerProfileSpinner.setSelection(model.slicingProfilePosition());
            }
        }, UPDATE_SLICER_PROFILE_DELAY);
        mPrinterProfileSpinner.setSelection(model.printerProfilePosition());
        mAfterSlicingSpinner.setSelection(model.afterSlicingPosition());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSlicerModels == null || mPrinterProfiles == null) return;
        SlicingCommandModel.Builder builder = getSlicingCommandModelBuilder();
        if (mApiUrl == null) builder.apiUrl(""); // IllegalStateException if mApiUrl is null.
        outState.putParcelable(SLICER_SCREEN_MODEL_KEY, builder.build());
    }

    @OnClick(R.id.slice_button)
    void onSliceButtonClicked() {
        mPresenter.onSliceButtonClicked(getSlicingCommandModelBuilder());
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastSlicingParametersMissing() {
        toastMessage(SLICING_PARAMETERS_MISSING);
    }

    @Override
    public void showSliceCompleteAndUpdateFiles() {
        toastMessage(SLICE_COMPLETE);

        // After slice complete, wait couple seconds before updating FilesFragment.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {mListener.updateFiles();
            }
        }, UPDATE_FILES_TIMER);
    }

    @Override
    public void updateSlicer(List<SlicerModel> slicerModels) {
        showProgress(false);
        mRefreshLayout.setRefreshing(false);
        mSlicerModels = slicerModels;
        mSlicerSpinner.setAdapter(getNewAdapter(slicerModels));
    }

    @Override
    public void updatePrinterProfile(List<SpinnerModel> printerProfiles) {
        showProgress(false);
        mRefreshLayout.setRefreshing(false);
        mPrinterProfiles = printerProfiles;
        mPrinterProfileSpinner.setAdapter(getNewAdapter(printerProfiles));
    }

    public <T> ArrayAdapter<T> getNewAdapter(List<T> strings) {
        int spinnerId = android.R.layout.simple_spinner_dropdown_item;
        return new ArrayAdapter<>(getContext(), spinnerId, strings);
    }

    public void setApiUrl(String apiUrl) {
        mApiUrl = apiUrl;
        mFileNameTextView.setText(mPresenter.getFileName(mApiUrl));
        enableSliceButton(true);
    }

    @Override
    public void enableSliceButton(boolean enable) {
        if (!enable) mSliceButton.setEnabled(false);
        else mSliceButton.setEnabled(mApiUrl != null);
    }

    @Override
    public int getInvalidPosition() {
        return AdapterView.INVALID_POSITION;
    }

    @Override
    public String getDotGco() {
        return DOT_GCO;
    }

    private SpinnerSelectedListener mSlicerListener = new SpinnerSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (mSlicerModels == null) return;
            List<SlicerModel.Profile> profiles = mSlicerModels.get(position).profiles();
            mSlicerProfileSpinner.setAdapter(getNewAdapter(profiles));
        }
    };

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_slicing, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_slicing_menu_item:
                onRefresh();
                mRefreshLayout.setRefreshing(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected SlicingScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    @Override
    public void setRefresh(boolean enable) {
        mRefreshLayout.setRefreshing(enable);
    }

    @Override
    public boolean isSlicingInProgress() {
        return mProgressView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onRefresh() {
        mPresenter.execute();
    }

    public interface Listener {
        void updateFiles();
    }
}
