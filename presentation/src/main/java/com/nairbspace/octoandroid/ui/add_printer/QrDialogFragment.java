package com.nairbspace.octoandroid.ui.add_printer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.ui.templates.BaseDialogFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class QrDialogFragment extends BaseDialogFragment<QrDialogFragment.Listener> {

    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    private boolean isPermissionDenied;
    private String mApiKey;

    @BindView(R.id.camera_view) SurfaceView mCameraView;
    @BindView(R.id.qr_close) ImageView mCloseButton;
    @BindView(R.id.qr_frame) ImageView mQrFrame;
    @BindString(R.string.exception_camera_error) String CAMERA_ERROR;

    private Listener mListener;

    public interface Listener {
        void onQrSuccess(String apiKey);
    }

    public static QrDialogFragment newInstance() {
        return new QrDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBarcodeDetector = new BarcodeDetector.Builder(getActivity())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        mCameraSource = new CameraSource.Builder(getActivity(), mBarcodeDetector).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_qr_dialog, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (getNavigator().haveCameraPermission(this)) {
            populateSurfaceView(true);
        } else {
            populateSurfaceView(false);
            getNavigator().requestCameraPermission(this);
        }

        return view;
    }

    /** Layout initially inflated as GONE since SurfaceView doesn't update after Camera permission */
    private void populateSurfaceView(boolean shouldPopulate) {
        mCameraView.setVisibility(shouldPopulate ? View.VISIBLE : View.GONE);
        mCloseButton.setVisibility(shouldPopulate ? View.VISIBLE : View.GONE);
        mQrFrame.setVisibility(shouldPopulate ? View.VISIBLE : View.GONE);
        if (shouldPopulate) {
            mCameraView.getHolder().addCallback(new Callback());
            mBarcodeDetector.setProcessor(new QrProcessor());
        }
    }

    @OnClick(R.id.qr_close)
    void closeQrDialogFragment() {
        dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (getNavigator().checkCameraPermissionGranted(requestCode, grantResults)) {
            populateSurfaceView(true);
        } else {
            isPermissionDenied = true; // Cannot dismiss fragment until onResume
        }
    }

    private void triggerQrFinishedListener(String apiKey) {
        if (mListener != null) {
            mListener.onQrSuccess(apiKey);
            dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPermissionDenied) {
            dismiss();
        }
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    private final class Callback extends SurfaceHolderCallback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    getNavigator().requestCameraPermission(QrDialogFragment.this);
                    return;
                }
                mCameraSource.start(mCameraView.getHolder());
            } catch (Exception e) {
                Timber.e(e, null);
                Toast.makeText(getContext(), CAMERA_ERROR, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCameraSource.stop();
        }
    }

    private final class QrProcessor extends BarcodeProcessor implements Runnable {

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
            final SparseArray<Barcode> barcodes = detections.getDetectedItems();

            if (barcodes.size() != 0) {
                mApiKey = barcodes.valueAt(0).displayValue;
                getActivity().runOnUiThread(this);
            }
        }

        @Override
        public void run() {
            triggerQrFinishedListener(mApiKey);
        }
    }
}