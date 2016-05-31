package com.nairbspace.octoandroid.ui.temp_graph;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class TargetTempDataSet extends LineDataSet {

    public TargetTempDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
    }

    /**
     *  By calling setColor initializes the rest of the default paramaters
     * @param color the color of the line
     */
    @Override
    public void setColor(int color) {
        super.setColor(color);
        setDrawCircles(false);
        setDrawValues(false);
        enableDashedLine(10f, 5f, 0f);
    }
}
