package com.nairbspace.octoandroid.ui.slicer.slicing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.model.SlicingCommandModel;
import com.nairbspace.octoandroid.ui.templates.BaseFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlicingFragment extends BaseFragmentListener<SlicingScreen, SlicingFragment.Listener>
        implements SlicingScreen, SwipeRefreshLayout.OnRefreshListener {

    private static final String SLICER_SCREEN_MODEL_KEY = "slicer_screen_model_key";
    private static final String API_URL_KEY = "api_url_key";
    @BindString(R.string.dot_gco) String DOT_GCO;

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

    private int mSpinnerId;
    private Map<String, SlicerModel> mModelMap;
    private HashMap<String, String> mPrinterProfileMap;
    private String mApiUrl;

    public SlicingCommandModel getSlicingCommandModel() {
        return SlicingCommandModel.builder()
                .slicerPosition(mSlicerSpinner.getSelectedItemPosition())
                .slicingProfilePosition(mSlicerProfileSpinner.getSelectedItemPosition())
                .printerProfilePosition(mPrinterProfileSpinner.getSelectedItemPosition())
                .afterSlicingPosition(mAfterSlicingSpinner.getSelectedItemPosition())
                .apiUrl(mApiUrl)
                .slicerMap(mModelMap)
                .printerProfileMap(mPrinterProfileMap)
                .afterSlicingList(Arrays.asList(mAfterSlicingArray))
                .build();
    }

    public static SlicingFragment newInstance() {
        return new SlicingFragment();
    }

    public static SlicingFragment newInstance(String apiUrl) {
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
        mSpinnerId = android.R.layout.simple_spinner_dropdown_item;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slicing_settings, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        mRefreshLayout.setOnRefreshListener(this);
        mSlicerSpinner.setOnItemSelectedListener(mSlicerListener);
        mSliceButton.setEnabled(false);
        if (savedInstanceState != null) restoreSavedInstanceState(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String apiUrl = getArguments().getString(API_URL_KEY);
            if (apiUrl != null) setApiUrl(apiUrl);
        }
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        SlicerScreenModel model = savedInstanceState.getParcelable(SLICER_SCREEN_MODEL_KEY);
        if (model == null) return;
        Map<String, SlicerModel> slicerMap = model.slicerModelMap();
        updateSlicer(slicerMap, mPresenter.getSlicerNames(slicerMap));
        HashMap<String, String> printerNameMap = model.printerProfileNamesMap();
        updatePrinterProfile(printerNameMap, mPresenter.getPrinterProfileNames(printerNameMap));
        String apiUrl = savedInstanceState.getString(API_URL_KEY);
        if (apiUrl != null) setApiUrl(apiUrl);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mModelMap != null && mPrinterProfileMap != null) {
            SlicerScreenModel model = SlicerScreenModel.create(mModelMap, mPrinterProfileMap);
            outState.putParcelable(SLICER_SCREEN_MODEL_KEY, model);
        }
        if (mApiUrl != null) {
            outState.putString(API_URL_KEY, mApiUrl);
        }
    }

    @OnClick(R.id.slice_button)
    void onSliceButtonClicked() {mPresenter.onSliceButtonClicked(getSlicingCommandModel());}

    @Override
    public void updateSlicer(Map<String, SlicerModel> modelMap, List<String> slicerNames) {
        mRefreshLayout.setRefreshing(false);
        mModelMap = modelMap;
        mSlicerSpinner.setAdapter(getNewAdapter(slicerNames));
    }

    @Override
    public void updatePrinterProfile(HashMap<String, String> map, List<String> printerProfileNames) {
        mRefreshLayout.setRefreshing(false);
        mPrinterProfileMap = map;
        mPrinterProfileSpinner.setAdapter(getNewAdapter(printerProfileNames));
    }

    public ArrayAdapter<String> getNewAdapter(List<String> strings) {
        return new ArrayAdapter<>(getContext(), mSpinnerId, strings);
    }

    public void setApiUrl(String apiUrl) {
        mApiUrl = apiUrl;
        mFileNameTextView.setText(mPresenter.getFileName(mApiUrl));
        mSliceButton.setEnabled(true);
    }

    @Override
    public String getDotGco() {
        return DOT_GCO;
    }

    private SpinnerSelectedListener mSlicerListener = new SpinnerSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (mModelMap == null) return;
            List<String> profileNames = mPresenter.getProfileNames(mModelMap, position);
            if (profileNames == null) return;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), mSpinnerId, profileNames);
            mSlicerProfileSpinner.setAdapter(adapter);
        }
    };

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
    public void onRefresh() {
        mPresenter.execute();
    }

    public interface Listener {

    }
}
