package com.nairbspace.octoandroid.ui.slicer.slicing;

import android.content.Context;

import com.nairbspace.octoandroid.model.SlicerModel;
import com.nairbspace.octoandroid.model.SlicingProgressModel;
import com.nairbspace.octoandroid.model.SpinnerModel;

import java.util.List;
import java.util.Map;

public interface SlicingScreen {

    void updateSlicer(Map<String, SlicerModel> modelMap, List<String> slicerNames);
    void updatePrinterProfile(List<SpinnerModel> printerProfiles);
    String getDotGco();
    void updateProgress(SlicingProgressModel progressModel);
    void showProgress(boolean show);
    void toastMessage(String message);
    void toastSlicingParamtersMissing();
    void showSliceCompleteAndUpdateFiles();
    Context context();
    int getInvalidPosition();
}
