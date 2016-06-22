package com.nairbspace.octoandroid.ui.slicer.slicing;

import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.model.SlicingProgressModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SlicingScreen {

    void updateSlicer(Map<String, SlicerModel> modelMap, List<String> slicerNames);
    void updatePrinterProfile(HashMap<String, String> map, List<String> printerProfileNames);
    String getDotGco();
    void updateProgress(SlicingProgressModel progressModel);
    void showProgress(boolean show);
}
