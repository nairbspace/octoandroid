package com.nairbspace.octoandroid.ui.temp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class TempChart extends LineChart implements OnChartValueSelectedListener{

    public TempChart(Context context) {
        super(context);
        initializeChart();
    }

    public TempChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeChart();
    }

    public TempChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeChart();
    }

    private void initializeChart() {
        setOnChartValueSelectedListener(this);
        setDescription("");
        setNoDataTextDescription("No chart data");
        setTouchEnabled(true);
        setScaleEnabled(true);
        setDragEnabled(true);
        setDrawGridBackground(true);
        setPinchZoom(true);

        // Have to do this at runtime
//        Legend legend = getLegend();
//        legend.setWordWrapEnabled(true);
//
//        XAxis xAxis = getXAxis();
//        xAxis.setAvoidFirstLastClipping(true);
//        xAxis.setPosition(XAxis.XAxisPosition.TOP);
//
//        YAxis yAxisRight = getAxisRight();
//        yAxisRight.setEnabled(false);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        // TODO-low would be nice to display where clicked
        Toast.makeText(getContext(), e.getVal() + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }
}
