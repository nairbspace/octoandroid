package com.nairbspace.octoandroid.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.nairbspace.octoandroid.app.LifecycleHandler;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetWebsocket;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.mapper.WebsocketModelMapper;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.Navigator;

import javax.inject.Inject;

import timber.log.Timber;

public class WebsocketService extends Service {

    private static final int POLL_INTERVAL = 1000 * 60; // 60 seconds

    @Inject GetWebsocket mGetWebsocket;
    @Inject WebsocketModelMapper mWebsocketModelMapper;
    @Inject Navigator mNavigator;

    private boolean mWasPreviouslyPrinting;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        SetupApplication.get(getApplicationContext()).getAppComponent().inject(this);
        Timber.d("onCreate");
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, WebsocketService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("Websocket service started");
        if (!LifecycleHandler.isApplicationVisible()) {
            Timber.d("Websocket service started in background");
            mGetWebsocket.execute(new WebsocketSubscriber());
        } else {
            turnOffAlarmAndStopService();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private final class WebsocketSubscriber extends DefaultSubscriber<Websocket> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Websocket websocket) {
            mWebsocketModelMapper.execute(new MapperSubscriber(), websocket);
        }
    }

    private final class MapperSubscriber extends DefaultSubscriber<WebsocketModel> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(WebsocketModel websocketModel) {
            Timber.d(websocketModel.toString());

            // If application is visible kill this service
            if (LifecycleHandler.isApplicationInForeground()) {
                turnOffAlarmAndStopService();
            }

            // If last update was previously printing
            if (mWasPreviouslyPrinting) {

                // And is now 100%
                if (websocketModel.completionProgress() == 100) {

                    // Send notification
                    mNavigator.navigateToStatusActivityFromNotification(getApplicationContext(),
                            websocketModel.file());
                    turnOffAlarmAndStopService();
                }
            } else { // Otherwise store current status as previous status
                mWasPreviouslyPrinting = websocketModel.printing();
            }
        }
    }

    public static void toggleServiceAlarm(Context context) {
        boolean shouldStartAlarm = !isServiceAlarmOn(context);
        setServiceAlarm(context, shouldStartAlarm);
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    private void turnOffAlarmAndStopService() {
        setServiceAlarm(getApplicationContext(), false);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy");
        mGetWebsocket.unsubscribe();
        mWebsocketModelMapper.unsubscribe();
        super.onDestroy();
    }
}
