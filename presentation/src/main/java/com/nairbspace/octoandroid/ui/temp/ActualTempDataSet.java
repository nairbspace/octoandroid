package com.nairbspace.octoandroid.ui.temp;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class ActualTempDataSet extends LineDataSet{

    public ActualTempDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
        setDrawCircles(false);
        setDrawValues(false);
        setLineWidth(2f);
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
    public void setLineWidth(float width) {
        super.setLineWidth(2f);
    }
}
