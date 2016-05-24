package com.nairbspace.octoandroid.data.net.stream;

public interface MjpegView {

    void setSource(MjpegInputStream stream);

    void setDisplayMode(DisplayMode mode);

    void showFps(boolean show);

    void stopPlayback();

    void resumePlayback();

    boolean isStreaming();
}
