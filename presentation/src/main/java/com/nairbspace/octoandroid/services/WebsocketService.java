package com.nairbspace.octoandroid.services;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.LifecycleHandler;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.BaseService;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

public class WebsocketService extends BaseService<WebsocketServiceListener> implements WebsocketServiceListener {
    private static final int PRINT_NOTIFICATION_ID = 1;
    private static final int COMPLETE = 100;

    @Inject WebsocketServicePresenter mPresenter;

    private NotificationCompat.Builder mStickyBuilder;
    private NotificationCompat.Builder mFinishedBuilder;
    private NotificationManagerCompat mNotificationManager;
    private Subscription mDelayedSub = Subscriptions.unsubscribed();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        SetupApplication.get(getApplicationContext()).getAppComponent().inject(this);
        mNotificationManager = NotificationManagerCompat.from(this);
        super.onCreate();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, WebsocketService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPresenter.onStartCommand();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Checks to see if application is visible. If so then turns off alarm and stops service.
     */
    @Override
    public void checkApplicationStatus() {
        if (LifecycleHandler.isApplicationInForeground()) executeDelayedStop();
    }

    // TODO figure out why notification isn't getting canceled unless delay.
    private void executeDelayedStop() {
        unsubDelayedSub();
        mDelayedSub = Observable.just(null)
                .delay(25, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                turnOffAlarmAndStopService();
            }
        });
    }

    private void unsubDelayedSub() {
        if (!mDelayedSub.isUnsubscribed()) mDelayedSub.unsubscribe();
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

        getNavigator().createNotificationToDispatchActivity(this, mStickyBuilder, PRINT_NOTIFICATION_ID);
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

    protected enum FinishType {
        REGULAR, ERROR, NONE
    }

    @Override
    public void showFinishedAndDestroy(FinishType type, @Nullable String fileName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Resources res = getResources();
        switch (type) {
            case REGULAR:
                if (fileName == null) fileName = "";
                builder.setTicker(res.getString(R.string.print_complete))
                        .setSmallIcon(R.drawable.ic_print_white_24dp)
                        .setContentTitle(res.getString(R.string.print_complete))
                        .setContentText(fileName);
                break;
            case ERROR:
                builder.setTicker(res.getString(R.string.print_stopped))
                        .setSmallIcon(R.drawable.ic_error_white_24dp)
                        .setContentTitle(res.getString(R.string.print_stopped))
                        .setContentText(res.getString(R.string.print_stopped_description));
                break;
            case NONE:
                builder = null;
                break;
        }
        if (builder != null) showFinishedNotification(builder);
        turnOffAlarmAndStopService();
    }

    private void showFinishedNotification(@NonNull NotificationCompat.Builder builder) {
        mFinishedBuilder = builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        getNavigator().createNotificationToDispatchActivity(this, mFinishedBuilder, PRINT_NOTIFICATION_ID);
    }

    @Override
    public void turnOffAlarmAndStopService() {
        getNavigator().setWebsocketServiceAlarm(this, false);
        stopSelf();
    }

    /**
     * Checks to see if {@link #mFinishedBuilder} is null to not accidentally cancel
     * the notification, but will cancel the sticky notification if present. Also
     * Calls {@link WebsocketServicePresenter#onDestroy(Object)} method.
     */
    @Override
    public void onDestroy() {
        if (mFinishedBuilder == null) mNotificationManager.cancel(PRINT_NOTIFICATION_ID);
        unsubDelayedSub();
        super.onDestroy();
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected WebsocketServiceListener setListener() {
        return this;
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
