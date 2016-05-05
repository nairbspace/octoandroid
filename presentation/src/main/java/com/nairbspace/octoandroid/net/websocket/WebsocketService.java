package com.nairbspace.octoandroid.net.websocket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.data.db.Printer;
import com.nairbspace.octoandroid.data.db.PrinterDao;
import com.nairbspace.octoandroid.data.pref.PrefManager;
import com.nairbspace.octoandroid.net.websocket.model.WebsocketObj;

import org.greenrobot.eventbus.EventBus;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import timber.log.Timber;

public class WebsocketService extends Service
        implements AsyncHttpClient.WebSocketConnectCallback, WebSocket.StringCallback  {

    @Inject Gson mGson;
    @Inject EventBus mEventBus;
    @Inject PrinterDao mPrinterDao;
    @Inject PrefManager mPrefManager;
    private Printer mPrinter;

    private WebSocket mWebsocket;
    private WebsocketObj mWebsocketObj;

    public static Intent newService(Context context) {
        return new Intent(context, WebsocketService.class);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("onBind not implemented with this Service.");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SetupApplication.get(this).getAppComponent().inject(this);

        long printerId = mPrefManager.getActivePrinter();
        mPrinter = mPrinterDao.queryBuilder()
                .where(PrinterDao.Properties.Id.eq(printerId))
                .unique();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mPrinter == null) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        String scheme = mPrinter.getScheme(); // Async client converts to ws or wss respectively.
        String host = mPrinter.getHost();
        String path = "/sockjs/websocket";
        try {
            String uri = new URI(scheme, host, path, null).toString();
            AsyncHttpClient.getDefaultInstance().websocket(uri, null, this);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCompleted(Exception ex, WebSocket webSocket) {
        if (ex != null) {
            ex.printStackTrace();
        }
        mWebsocket = webSocket;
        mWebsocket.setStringCallback(this);
    }

    @Override
    public void onStringAvailable(String s) {
        try {
            mWebsocketObj = mGson.fromJson(s, WebsocketObj.class);
            // TODO emit data via Eventbus

            if (mWebsocketObj.getCurrent().getState().getText() != null) {
                final String machineStatus = mWebsocketObj.getCurrent().getState().getText();
                Timber.d(machineStatus);

                Observable.just(machineStatus)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                mEventBus.post(s);
                            }
                        });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (mWebsocket != null && mWebsocket.getSocket() != null) {
            mWebsocket.getSocket().close();
        }
        super.onDestroy();
    }
}
