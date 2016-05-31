package com.nairbspace.octoandroid.ui.temp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class TempChart extends LineChart implements OnChartValueSelectedListener{

    public TempChart(Context context) {
        super(context);
    }

    public TempChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TempChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * By calling setData initializes the rest of the view
     * @param data the chart data
     */
    @Override
    public void setData(LineData data) {
        super.setData(data);
        setOnChartValueSelectedListener(this);
        setDescription("");
        setNoDataTextDescription("No chart data");
        setTouchEnabled(true);
        setScaleEnabled(true);
        setDragEnabled(true);
        setDrawGridBackground(true);
        setPinchZoom(true);

        Legend legend = getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setWordWrapEnabled(true);

        XAxis xAxis = getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        YAxis yAxisLeft = getAxisLeft();
        yAxisLeft.setAxisMinValue(0);

        YAxis yAxisRight = getAxisRight();
        yAxisRight.setEnabled(false);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        // TODO-low would be nice to display where clicked
        Toast.makeText(getContext(), e.getVal() + "°C", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }
}
