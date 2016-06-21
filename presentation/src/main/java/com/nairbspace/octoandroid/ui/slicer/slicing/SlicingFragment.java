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
import android.widget.Spinner;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.ui.templates.BaseFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlicingFragment extends BaseFragmentListener<SlicingScreen, SlicingFragment.Listener>
        implements SlicingScreen, SwipeRefreshLayout.OnRefreshListener {

    private static final String SLICER_SCREEN_MODEL_KEY = "slicer_screen_model_key";

    @Inject SlicingPresenter mPresenter;
    private Listener mListener;

    @BindView(R.id.slicing_refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.slicer_spinner) Spinner mSlicerSpinner;
    @BindView(R.id.slicer_profile_spinner) Spinner mSlicerProfileSpinner;
    @BindView(R.id.printer_profile_spinner) Spinner mPrinterProfileSpinner;
    @BindView(R.id.after_slicing_spinner) Spinner mAfterSlicingSpinner;

    private int mSpinnerId;
    private Map<String, SlicerModel> mModelMap;
    private HashMap<String, String> mPrinterProfileMap;

    public static SlicingFragment newInstance() {
        return new SlicingFragment();
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
        if (savedInstanceState != null) restoreSavedInstanceState(savedInstanceState);
        return view;
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        SlicerScreenModel model = savedInstanceState.getParcelable(SLICER_SCREEN_MODEL_KEY);
        if (model == null) return;
        mModelMap = model.slicerModelMap();
        updateSlicer(mModelMap, mPresenter.getSlicerNames(mModelMap));
        mPrinterProfileMap = model.printerProfileNamesMap();
        updatePrinterProfile(mPrinterProfileMap, mPresenter.getPrinterProfileNames(mPrinterProfileMap));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mModelMap != null && mPrinterProfileMap != null) {
            SlicerScreenModel model = SlicerScreenModel.create(mModelMap, mPrinterProfileMap);
            outState.putParcelable(SLICER_SCREEN_MODEL_KEY, model);
        }
    }

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
