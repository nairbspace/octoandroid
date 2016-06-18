package com.nairbspace.octoandroid.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.LifecycleHandler;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.Navigator;

import javax.inject.Inject;

public class WebsocketService extends Service implements WebsocketServiceHelper.Listener {
    private static final int PRINT_NOTIFICATION_ID = 1;
    private static final int COMPLETE = 100;

    @Inject Navigator mNavigator;
    @Inject WebsocketServiceHelper mServiceHelper;

    private NotificationCompat.Builder mStickyBuilder;
    private NotificationCompat.Builder mFinishedBuilder;
    private NotificationManagerCompat mNotificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        SetupApplication.get(getApplicationContext()).getAppComponent().inject(this);
        mServiceHelper.onCreate(this);
        mNotificationManager = NotificationManagerCompat.from(this);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, WebsocketService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mServiceHelper.onStartCommand();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean isApplicationVisible() {
        return LifecycleHandler.isApplicationVisible();
    }

    /**
     * Checks to see if application is visible. If so then turns off alarm and stops service.
     */
    @Override
    public void checkApplicationStatus() {
        if (LifecycleHandler.isApplicationInForeground()) {
            turnOffAlarmAndStopService();
        }
    }

    @Override
    public void showSticky() {
        if (mStickyBuilder != null) return;
        mStickyBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_print_black_24dp)
                .setContentTitle(getResources().getString(R.string.printer_space_colon))
                .setContentText(getResources().getString(R.string.in_progress))
                .setAutoCancel(true)
                .setOngoing(true);

        mNavigator.navigateToStatusActivityFromNotification(this, mStickyBuilder, PRINT_NOTIFICATION_ID);
    }

    /**
     * If {@link #mStickyBuilder} is null that means it was never created
     * (ie. Sticky Notification is turned off). Otherwise update it with latest data from model.
     * @param model {@link WebsocketModel}
     */
    @Override
    public void updateSticky(WebsocketModel model) {
        if (mStickyBuilder == null) return;
        Resources res = getResources();
        String printingFile = res.getString(R.string.printer_space_colon) + model.file();
        String printTimeLeft = res.getString(R.string.print_time_left_colon_space) + model.printTimeLeft();
        mStickyBuilder
                .setContentTitle(printingFile)
                .setContentText(printTimeLeft)
                .setProgress(COMPLETE, model.completionProgress(), false);
        mNotificationManager.notify(PRINT_NOTIFICATION_ID, mStickyBuilder.build());
    }

    @Override
    public void showFinishedAndDestroy(String fileName, boolean showFinish) {
        if (showFinish) {
            mFinishedBuilder = new NotificationCompat.Builder(this)
                    .setTicker(getResources().getString(R.string.print_complete))
                    .setSmallIcon(R.drawable.ic_print_black_24dp)
                    .setContentTitle(getResources().getString(R.string.print_complete))
                    .setContentText(fileName)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);

            mNavigator.navigateToStatusActivityFromNotification(this, mFinishedBuilder, PRINT_NOTIFICATION_ID);
        }
        turnOffAlarmAndStopService();
    }

    @Override
    public void turnOffAlarmAndStopService() {
        mNavigator.setWebsocketServiceAlarm(this, false);
        stopSelf();
    }

    /**
     * Checks to see if {@link #mFinishedBuilder} is null to not accidentally cancel
     * the notification, but will cancel the sticky notification if present. Also
     * Calls {@link WebsocketServiceHelper#onDestroy()} method.
     */
    @Override
    public void onDestroy() {
        if (mFinishedBuilder == null) mNotificationManager.cancel(PRINT_NOTIFICATION_ID);
        mServiceHelper.onDestroy();
        super.onDestroy();
    }

//    public static void toggleServiceAlarm(Context context) {
//        boolean shouldStartAlarm = !isServiceAlarmOn(context);
//        setWebsocketServiceAlarm(context, shouldStartAlarm);
//    }
//
//    public static boolean isServiceAlarmOn(Context context) {
//        Intent i = newIntent(context);
//        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
//        return pi != null;
//    }
}
