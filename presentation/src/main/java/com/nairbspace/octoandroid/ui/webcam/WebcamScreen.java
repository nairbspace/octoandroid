package com.nairbspace.octoandroid.ui.webcam;

import com.nairbspace.octoandroid.data.net.stream.MjpegInputStream;

public interface WebcamScreen {

    void updateUi(MjpegInputStream inputStream);
}
