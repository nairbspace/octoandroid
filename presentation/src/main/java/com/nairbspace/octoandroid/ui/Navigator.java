package com.nairbspace.octoandroid.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.services.WebsocketService;
import com.nairbspace.octoandroid.ui.add_printer.AddPrinterActivity;
import com.nairbspace.octoandroid.ui.printer_controls.PrinterControlsActivity;
import com.nairbspace.octoandroid.ui.printer_settings.PrinterSettingsActivity;
import com.nairbspace.octoandroid.ui.settings.SettingsActivity;
import com.nairbspace.octoandroid.ui.status.StatusActivity;
import com.nairbspace.octoandroid.ui.temp.TempActivity;
import com.nairbspace.octoandroid.ui.webcam.WebcamActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {
    private static final int ADD_PRINTER_REQUEST_CODE = 0;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;

    private static final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;

    private static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

    private static final String ROOT_MIME_TYPE = "*/*";
    private static final String FILE_MIME_TYPE = "file/*";

    @Inject
    public Navigator() {
    }

    public int getAddPrinterRequestCode() {
        return ADD_PRINTER_REQUEST_CODE;
    }

    public int getPickFileRequestCode() {
        return PICK_FILE_REQUEST_CODE;
    }

    public void navigateToAddPrinterActivityForResult(Activity activity) {
        Intent intentToLaunch = AddPrinterActivity.newIntent(activity);
        activity.startActivityForResult(intentToLaunch, ADD_PRINTER_REQUEST_CODE);
    }

    public boolean wasAddPrinterResultOk(int requestCode, int resultCode) {
        if (requestCode == getAddPrinterRequestCode()) {
            if (resultCode == Activity.RESULT_OK) {
                return true;
            }
        }
        return false;
    }

    public void navigateToStatusActivity(Activity activity) {
        Intent i = StatusActivity.newIntent(activity);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(i);
    }

    public void navigateToStatusActivityFromNotification(Context context, NotificationCompat.Builder builder, int id) {
        Intent i = StatusActivity.newIntent(context);
        // TODO need to fix flags. If screen is currently on another Activity besides StatusActivity it will create unwanted backstack.
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = builder.setContentIntent(pi).build();
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, notification);
    }

    public void setWebsocketServiceAndAlarm(Context context, boolean isOn) {
        Intent i = WebsocketService.newIntent(context);
        if (isOn) {
            context.startService(i);
        } else {
            context.stopService(i);
        }

        setWebsocketServiceAlarm(context, isOn);
    }

    public void setWebsocketServiceAlarm(Context context, boolean isOn) {
        Intent i = WebsocketService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public void navigateToTempActivity(Activity activity) {
        Intent i = TempActivity.newIntent(activity);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(i);
    }

    public void navigateToPrinterControlsActivity(Activity activity) {
        Intent i = PrinterControlsActivity.newIntent(activity);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(i);
    }

    public void navigateToSettingsActivity(Activity activity) {
        Intent i = SettingsActivity.newIntent(activity);
        activity.startActivity(i);
    }

    public void navigateToPrinterSettingsActivity(Activity activity) {
        Intent i = PrinterSettingsActivity.newIntent(activity);
        activity.startActivity(i);
    }

    public void navigateToDownloadFile(Fragment fragment, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if ((intent.resolveActivity(fragment.getActivity().getPackageManager()) != null)) {
            fragment.startActivity(intent);
        }
    }

    public void tryToNavigateToFileManagerForResult(Fragment fragment) {
        if (havePermission(fragment, READ_EXTERNAL_STORAGE_PERMISSION)) {
            navigateToFileManagerForResult(fragment);
        } else {
            requestPermission(fragment, READ_EXTERNAL_STORAGE_PERMISSION,
                    READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    public void navigateToWebcam(final Activity activity) {
        Resources res = activity.getResources();
        String warning = res.getString(R.string.warning);
        String message = res.getString(R.string.warning_webcam_message);
        new AlertDialog.Builder(activity)
                .setTitle(warning)
                .setMessage(message)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = WebcamActivity.newIntent(activity);
                        activity.startActivity(intent);
                    }
                })
                .setCancelable(true)
                .create()
                .show();
    }

    private void navigateToFileManagerForResult(Fragment fragment) {
        Intent intent = new Intent();
        intent.setType(FILE_MIME_TYPE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }

        if (intent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        }
    }

    public void checkReadGrantedAndTryAgain(int requestCode, int[] grantResults, Fragment fragment) {
        if (checkPermissionGranted(requestCode, READ_EXTERNAL_STORAGE_REQUEST_CODE, grantResults)) {
            tryToNavigateToFileManagerForResult(fragment);
        }
    }

    public boolean haveCameraPermission(Fragment fragment) {
        return havePermission(fragment, CAMERA_PERMISSION);
    }

    public void requestCameraPermission(Fragment fragment) {
        requestPermission(fragment, CAMERA_PERMISSION, CAMERA_REQUEST_CODE);
    }

    public boolean checkCameraPermissionGranted(int requestCode, int[] grantResults) {
        return checkPermissionGranted(requestCode, CAMERA_REQUEST_CODE, grantResults);
    }

    private boolean havePermission(Fragment fragment, String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ActivityCompat.checkSelfPermission(fragment.getContext(), permission) == PERMISSION_GRANTED;
    }

    private void requestPermission(Fragment fragment, String permission, int requestCode) {
        fragment.requestPermissions(new String[] {permission}, requestCode);
    }

    private boolean checkPermissionGranted(int inputRequestCode, int actualRequestCode, int[] grantResults) {
        if (inputRequestCode == actualRequestCode) {
            if (grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }
}
