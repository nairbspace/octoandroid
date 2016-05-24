package com.nairbspace.octoandroid.ui.webcam;

import com.nairbspace.octoandroid.data.net.stream.MjpegInputStream;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetWebcam;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

public class WebcamPresenter extends UseCasePresenter<WebcamScreen> {

    private final GetWebcam mGetWebcam;
    private WebcamScreen mScreen;

    @Inject
    public WebcamPresenter(GetWebcam getWebcam) {
        super(getWebcam);
        mGetWebcam = getWebcam;
    }

    @Override
    protected void onInitialize(WebcamScreen webcamScreen) {
        mScreen = webcamScreen;
    }

    @Override
    protected void execute() {
        mGetWebcam.execute(new WebcamSubscriber());
    }

    // TODO this is accessing model MjpegInput Stream from outside module!
    private final class WebcamSubscriber extends DefaultSubscriber<MjpegInputStream> {

        @Override
        public void onNext(MjpegInputStream mjpegInputStream) {
            mScreen.updateUi(mjpegInputStream);
        }
    }
}
