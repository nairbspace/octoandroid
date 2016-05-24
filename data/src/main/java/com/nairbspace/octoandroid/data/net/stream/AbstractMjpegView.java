package com.nairbspace.octoandroid.data.net.stream;

import android.view.SurfaceHolder;

public abstract class AbstractMjpegView implements MjpegView {

    public abstract void onSurfaceCreated(SurfaceHolder holder);

    public abstract void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height);

    public abstract void onSurfaceDestroyed(SurfaceHolder holder);
}
