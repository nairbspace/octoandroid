package com.nairbspace.octoandroid.data.net.stream;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.disk.DbHelper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public class Mjpeg {

    private final DbHelper mDbHelper;

    @Inject
    public Mjpeg(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    public Observable<MjpegInputStream> connect() {
        return Observable.create(new Observable.OnSubscribe<MjpegInputStream>() {
            @Override
            public void call(Subscriber<? super MjpegInputStream> subscriber) {
                try {
                    PrinterDbEntity printerDbEntity = mDbHelper.getActivePrinterDbEntity();
                    URL url = new URL(getStreamUrl(printerDbEntity));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    subscriber.onNext(new MjpegInputStreamDefault(inputStream));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private String getStreamUrl(PrinterDbEntity printerDbEntity) {
        String scheme = printerDbEntity.getScheme();
        String host = printerDbEntity.getHost();
        int port = printerDbEntity.getPort();
        String path = "/webcam/";
        String query = "action=stream";

        try {
            return new URI(scheme, null, host, port, path, query, null).toString();
        } catch (URISyntaxException e) {
            throw Exceptions.propagate(new URISyntaxException(e.getInput(), e.getReason()));
        }
    }
}
