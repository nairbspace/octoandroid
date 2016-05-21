package com.nairbspace.octoandroid.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.nairbspace.octoandroid.ui.add_printer.AddPrinterActivity;
import com.nairbspace.octoandroid.ui.settings.SettingsActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {
    private static final int ADD_PRINTER_REQUEST_CODE = 0;
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 2;

    private static final String READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;

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

    public void navigateToSettingsActivity(Activity activity) {
        Intent i = SettingsActivity.newIntent(activity);
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

    private boolean havePermission(Fragment fragment, String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ActivityCompat.checkSelfPermission(fragment.getContext(), permission) ==PERMISSION_GRANTED;
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
