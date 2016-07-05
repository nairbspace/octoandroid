package com.nairbspace.octoandroid.ui.temp_graph;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TempGraphFragment extends BasePagerFragmentListener<TempGraphScreen, TempGraphFragment.Listener>
        implements TempGraphScreen {

    private static final String TEMP_CHART_MODEL_KEY = "temp_chart_model_key";

    @BindString(R.string.actual_temp_bed_chart_label) String ACTUAL_TEMP_BED_LABEL;
    @BindString(R.string.target_temp_bed_chart_label) String TARGET_TEMP_BED_LABEL;
    @BindString(R.string.actual_temp_tool0_chart_label) String ACTUAL_TEMP_TOOL0_LABEL;
    @BindString(R.string.target_temp_tool0_chart_label) String TARGET_TEMP_TOOL0_LABEL;
    @BindString(R.string.actual_temp_tool1_chart_label) String ACTUAL_TEMP_TOOL1_LABEL;
    @BindString(R.string.target_temp_tool1_chart_label) String TARGET_TEMP_TOOL1_LABEL;

    @BindView(R.id.temp_line_chart) TempChart mLineChart;
    @BindColor(R.color.chart_actual_temp_bed_color) int mActualTempBedColor;
    @BindColor(R.color.chart_target_temp_bed_color) int mTargetTempBedColor;
    @BindColor(R.color.chart_actual_temp_tool_zero_color) int mActualTempTool0Color;
    @BindColor(R.color.chart_target_temp_tool_zero_color) int mTargetTempTool0Color;
    @BindColor(R.color.chart_actual_temp_tool_one_color) int mActualTempTool1Color;
    @BindColor(R.color.chart_target_temp_tool_one_color) int mTargetTempTool1Color;

    // Menu stuff
    @BindString(R.string.unlock_screen) String UNLOCK;
    @BindString(R.string.lock_screen) String LOCK;
    @BindDrawable(R.drawable.ic_lock_open_white_24dp) Drawable mUnlockDrawable;
    @BindDrawable(R.drawable.ic_lock_outline_white_24dp) Drawable mLockDrawable;
    @BindString(R.string.disable_autoscroll) String DISABLE_AUTOSCROLL;
    @BindString(R.string.enable_autoscroll) String ENABLE_AUTOSCROLL;
    @BindDrawable(R.drawable.ic_close_white_24dp) Drawable mStopDrawable;
    @BindDrawable(R.drawable.ic_chevron_right_white_24dp) Drawable mAutoScrollDrawable;
    private boolean mIsAutoScrollEnabled = true;

    @Inject TempGraphPresenter mPresenter;
    private Listener mListener;

    private final Map<String, Integer> mLineDataSetMap = new HashMap<>();
    private final static float VISIBLE_X_RANGE_MAX = 10f;

    public static TempGraphFragment newInstance() {
        return new TempGraphFragment();
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
        View view = inflater.inflate(R.layout.fragment_temp_graph, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        addLineDataSet(createModel(ACTUAL_TEMP_BED_LABEL), dataSets);
        addLineDataSet(createModel(TARGET_TEMP_BED_LABEL), dataSets);
        addLineDataSet(createModel(ACTUAL_TEMP_TOOL0_LABEL), dataSets);
        addLineDataSet(createModel(TARGET_TEMP_TOOL0_LABEL), dataSets);
        addLineDataSet(createModel(ACTUAL_TEMP_TOOL1_LABEL), dataSets);
        addLineDataSet(createModel(TARGET_TEMP_TOOL1_LABEL), dataSets);

        TempLineData lineData = new TempLineData(new ArrayList<String>(), dataSets);
        mLineChart.setData(lineData);
        mLineChart.invalidate();
        if (savedInstanceState != null) restoreSavedInstanceState(savedInstanceState);
        return view;
    }

    public LineDataSetModel createModel(String label) {
        LineDataSetModel lineDataSetModel = null;
        if (label.equals(ACTUAL_TEMP_BED_LABEL)) {
            lineDataSetModel = LineDataSetModel.create(label, mActualTempBedColor, true);
        } else if (label.equals(TARGET_TEMP_BED_LABEL)) {
            lineDataSetModel = LineDataSetModel.create(label, mTargetTempBedColor, false);
        } else if (label.equals(ACTUAL_TEMP_TOOL0_LABEL)) {
            lineDataSetModel = LineDataSetModel.create(label, mActualTempTool0Color, true);
        } else if (label.equals(TARGET_TEMP_TOOL0_LABEL)) {
            lineDataSetModel = LineDataSetModel.create(label, mTargetTempTool0Color, false);
        } else if (label.equals(ACTUAL_TEMP_TOOL1_LABEL)) {
            lineDataSetModel = LineDataSetModel.create(label, mActualTempTool1Color, true);
        } else if (label.equals(TARGET_TEMP_TOOL1_LABEL)) {
            lineDataSetModel = LineDataSetModel.create(label, mTargetTempTool1Color, false);
        }
        return lineDataSetModel;
    }

    private int addLineDataSet(LineDataSetModel model, ArrayList<ILineDataSet> dataSets) {
        TempLineDataSet lineDataSet = new TempLineDataSet(new ArrayList<Entry>(), model.label(), model);
        dataSets.add(lineDataSet);
        int index = dataSets.size() - 1;
        mLineDataSetMap.put(model.label(), index);
        return index;
    }

    private void restoreSavedInstanceState(@NonNull Bundle savedInstanceState) {
        TempChartModel model = savedInstanceState.getParcelable(TEMP_CHART_MODEL_KEY);
        if (model == null) return;

        mLineChart.getData().setXVals(model.xValsTime());
        setYVals(ACTUAL_TEMP_BED_LABEL, model.yActualTempBed());
        setYVals(TARGET_TEMP_BED_LABEL, model.yTargetTempBed());
        setYVals(ACTUAL_TEMP_TOOL0_LABEL, model.yActualTempTool0());
        setYVals(TARGET_TEMP_TOOL0_LABEL, model.yTargetTempTool0());
        setYVals(ACTUAL_TEMP_TOOL1_LABEL, model.yActualTempTool1());
        setYVals(TARGET_TEMP_TOOL1_LABEL, model.yTargetTempTool1());

        mLineChart.getData().notifyDataChanged();
        updateChart();
    }

    private void setYVals(String key, List<Entry> entries) {
        getLineDataSetFromChart(getIndex(key)).setYVals(entries);
    }

    private List<Entry> getYValsFromChart(String key) {
        int index = getIndex(key);
        return getLineDataSetFromChart(index).getYVals();
    }

    private LineDataSet getLineDataSetFromChart(int index) {
        return ((LineDataSet) mLineChart.getData().getDataSetByIndex(index));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(TEMP_CHART_MODEL_KEY, getTempChartModel());
    }

    private TempChartModel getTempChartModel() {
        return TempChartModel.builder()
                .xValsTime((ArrayList<String>) mLineChart.getData().getXVals())
                .yActualTempBed(getYValsFromChart(ACTUAL_TEMP_BED_LABEL))
                .yTargetTempBed(getYValsFromChart(TARGET_TEMP_BED_LABEL))
                .yActualTempTool0(getYValsFromChart(ACTUAL_TEMP_TOOL0_LABEL))
                .yTargetTempTool0(getYValsFromChart(TARGET_TEMP_TOOL0_LABEL))
                .yActualTempTool1(getYValsFromChart(ACTUAL_TEMP_TOOL1_LABEL))
                .yTargetTempTool1(getYValsFromChart(TARGET_TEMP_TOOL1_LABEL))
                .build();
    }

    private int getIndex(String key) {
        return mLineDataSetMap.get(key);
    }

    @Override
    public void updateUi(WebsocketModel websocketModel) {
        mLineChart.getData().addXValue(websocketModel.tempTime());
        addEntry(getIndex(ACTUAL_TEMP_BED_LABEL), websocketModel.actualTempBed());
        addEntry(getIndex(TARGET_TEMP_BED_LABEL), websocketModel.targetTempBed());
        addEntry(getIndex(ACTUAL_TEMP_TOOL0_LABEL), websocketModel.actualTempTool0());
        addEntry(getIndex(TARGET_TEMP_TOOL0_LABEL), websocketModel.targetTempTool0());
        addEntry(getIndex(ACTUAL_TEMP_TOOL1_LABEL), websocketModel.actualTempTool1());
        addEntry(getIndex(TARGET_TEMP_TOOL1_LABEL), websocketModel.targetTempTool1());
        updateChart();
    }

    private Entry addEntry(int dataSetIndex, float val) {
        int xIndex = mLineChart.getData().getDataSetByIndex(dataSetIndex).getEntryCount();
        Entry entry = new Entry(val, xIndex);
        mLineChart.getData().addEntry(entry, dataSetIndex);
        return entry;
    }

    public void updateChart() {
        mLineChart.notifyDataSetChanged();
        if (mIsAutoScrollEnabled) {
            mLineChart.setVisibleXRangeMaximum(VISIBLE_X_RANGE_MAX);
            mLineChart.moveViewToX(mLineChart.getData().getXValCount() - VISIBLE_X_RANGE_MAX - 1); // Calls invalidate
        } else {
            mLineChart.setVisibleXRangeMaximum(mLineChart.getXChartMax());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_temp_graph, menu);
        MenuItem lock = menu.findItem(R.id.temp_graph_lock_swipe_menu_item);
        if (lock != null) updateLockIcon(lock);
        MenuItem scroll = menu.findItem(R.id.temp_graph_stop_auto_scroll_menu_item);
        if (scroll != null) updateScrollIcon(scroll);
    }

    private void toggleLock() {
        mListener.setSwipeEnabled(!mListener.isSwipeEnabled());
        if (getView() == null) return;
        boolean isNestedScrollingEnabled = ViewCompat.isNestedScrollingEnabled(getView());
        ViewCompat.setNestedScrollingEnabled(getView(), !isNestedScrollingEnabled);
    }

    private void updateLockIcon(@NonNull MenuItem menuItem) {
        menuItem.setTitle(mListener.isSwipeEnabled() ? UNLOCK : LOCK);
        menuItem.setIcon(mListener.isSwipeEnabled() ? mUnlockDrawable : mLockDrawable);
    }

    private void toggleAutoScroll() {
        mIsAutoScrollEnabled = !mIsAutoScrollEnabled;
    }

    private void updateScrollIcon(@NonNull MenuItem menuItem) {
        menuItem.setTitle(mIsAutoScrollEnabled ? DISABLE_AUTOSCROLL : ENABLE_AUTOSCROLL);
        menuItem.setIcon(mIsAutoScrollEnabled ? mStopDrawable : mAutoScrollDrawable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.temp_graph_lock_swipe_menu_item:
                toggleLock();
                updateLockIcon(item);
                return true;
            case R.id.temp_graph_stop_auto_scroll_menu_item:
                toggleAutoScroll();
                updateScrollIcon(item);
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
    protected TempGraphScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    public interface Listener {
        void setSwipeEnabled(boolean swipeEnabled);
        boolean isSwipeEnabled();
    }
}
