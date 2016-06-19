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

    /**
     * Checks to see if application is visible. If so then turns off alarm and stops service.
     */
    @Override
    public void checkApplicationStatus() {
        if (LifecycleHandler.isApplicationInForeground()) turnOffAlarmAndStopService();
    }

    /**
     * Checks to see if {@link #mStickyBuilder} is not null since {@link android.app.AlarmManager}
     * will call {@link #onStartCommand(Intent, int, int)} and start service all over again. If it's
     * the first time loading it will display the initial notification screen until it is updated
     * later again with the current progress and info.
     * @param model WebsocketModel
     */
    @Override
    public void showSticky(WebsocketModel model) {
        if (mStickyBuilder == null) createSticky();
        else updateSticky(model);
    }

    private void createSticky() {
        mStickyBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_print_white_24dp)
                .setContentTitle(getResources().getString(R.string.printing))
                .setContentText(getResources().getString(R.string.in_progress))
                .setAutoCancel(true)
                .setOngoing(true);

        mNavigator.createNotificationToDispatchActivity(this, mStickyBuilder, PRINT_NOTIFICATION_ID);
    }

    private void updateSticky(WebsocketModel model) {
        Resources res = getResources();
        String printingFile = res.getString(R.string.printing_space_colon) + model.file();
        String printTimeLeft = res.getString(R.string.print_time_left_colon_space) + model.printTimeLeft();
        mStickyBuilder
                .setContentTitle(printingFile)
                .setContentText(printTimeLeft)
                .setProgress(COMPLETE, model.completionProgress(), false);
        mNotificationManager.notify(PRINT_NOTIFICATION_ID, mStickyBuilder.build());
    }

    @Override
    public void showFinishedAndDestroy(String fileName, boolean showFinish) {
        if (showFinish) showFinishedNotification(fileName);
        turnOffAlarmAndStopService();
    }

    private void showFinishedNotification(String fileName) {
        mFinishedBuilder = new NotificationCompat.Builder(this)
                .setTicker(getResources().getString(R.string.print_complete))
                .setSmallIcon(R.drawable.ic_print_white_24dp)
                .setContentTitle(getResources().getString(R.string.print_complete))
                .setContentText(fileName)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        mNavigator.createNotificationToDispatchActivity(this, mFinishedBuilder, PRINT_NOTIFICATION_ID);
    }

    @Override
    public void showErrorAndStopService() {
        mFinishedBuilder = new NotificationCompat.Builder(this)
                .setTicker(getResources().getString(R.string.print_stopped))
                .setSmallIcon(R.drawable.ic_error_white_24dp)
                .setContentTitle(getResources().getString(R.string.print_stopped))
                .setContentText(getResources().getString(R.string.print_stopped_description))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        mNavigator.createNotificationToDispatchActivity(this, mFinishedBuilder, PRINT_NOTIFICATION_ID);
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
//        setWebsocketServiceAndAlarm(context, shouldStartAlarm);
//    }
//
//    public static boolean isServiceAlarmOn(Context context) {
//        Intent i = newIntent(context);
//        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
//        return pi != null;
//    }
}
