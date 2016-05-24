package com.nairbspace.octoandroid.data.net.stream;

import rx.Observable;

public interface WebcamManager {

    Observable<MjpegInputStream> connect();
}
