package com.nairbspace.octoandroid.ui.temp_graph;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

public class TempLineDataSet extends LineDataSet {

    public TempLineDataSet(List<Entry> yVals, String label, LineDataSetModel model) {
        super(yVals, label);
        setColor(model.color());
        setDrawCircles(false);
        setDrawValues(false);
        if (model.actual()) {
            setLineWidth(2f);
        } else {
            enableDashedLine(10f, 5f, 0f);
        }
    }
}
