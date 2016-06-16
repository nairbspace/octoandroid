package com.nairbspace.octoandroid.ui.temp_graph;

import android.graphics.Color;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.List;

public class TempLineData extends LineData {

    public TempLineData() {
        initialize();
    }

    public TempLineData(List<String> xVals) {
        super(xVals);
        initialize();
    }

    public TempLineData(String[] xVals) {
        super(xVals);
        initialize();
    }

    public TempLineData(List<String> xVals, List<ILineDataSet> dataSets) {
        super(xVals, dataSets);
        initialize();
    }

    public TempLineData(String[] xVals, List<ILineDataSet> dataSets) {
        super(xVals, dataSets);
        initialize();
    }

    public TempLineData(List<String> xVals, ILineDataSet dataSet) {
        super(xVals, dataSet);
        initialize();
    }

    public TempLineData(String[] xVals, ILineDataSet dataSet) {
        super(xVals, dataSet);
        initialize();
    }

    private void initialize() {
        setValueTextColor(Color.BLACK);
    }
}
