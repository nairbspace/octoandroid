package com.nairbspace.octoandroid.ui.add_printer;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

public abstract class BarcodeProcessor implements Detector.Processor<Barcode> {

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {

    }
}
