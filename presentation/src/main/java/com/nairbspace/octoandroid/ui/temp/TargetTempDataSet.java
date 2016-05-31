package com.nairbspace.octoandroid.ui.temp;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class TargetTempDataSet extends LineDataSet {

    public TargetTempDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
        setDrawCircles(false);
        setDrawValues(false);
        enableDashedLine(10f, 5f, 0f);
    }

    @Override
    public void setDrawCircles(boolean enabled) {
        super.setDrawCircles(false);
    }

    @Override
    public void setDrawValues(boolean enabled) {
        super.setDrawValues(false);
    }

    @Override
    public void enableDashedLine(float lineLength, float spaceLength, float phase) {
        super.enableDashedLine(10f, 5f, 0f);
    }
}
