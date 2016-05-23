package com.nairbspace.octoandroid.ui.stream;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.data.net.stream.DisplayMode;
import com.nairbspace.octoandroid.data.net.stream.Mjpeg;
import com.nairbspace.octoandroid.data.net.stream.MjpegInputStream;
import com.nairbspace.octoandroid.data.net.stream.MjpegSurfaceView;
import com.nairbspace.octoandroid.ui.BaseActivity;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WebcamActivity extends BaseActivity<WebcamScreen> implements WebcamScreen {

    @Inject WebcamPresenter mPresenter;
    @Inject Mjpeg mMjpeg;
    @BindView(R.id.stream_surface_view) MjpegSurfaceView mSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_webcam);
        ButterKnife.bind(this);

        mMjpeg.connect().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MjpegInputStream>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(MjpegInputStream mjpegInputStream) {
                        mSurfaceView.setSource(mjpegInputStream);
                        mSurfaceView.setDisplayMode(DisplayMode.BEST_FIT);
                        mSurfaceView.showFps(true);
                    }
                });
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected WebcamScreen setScreen() {
        return this;
    }
}
