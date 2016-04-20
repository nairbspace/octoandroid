package com.nairbspace.octoandroid.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.nairbspace.octoandroid.R;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QrDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QrDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrDialogFragment extends DialogFragment implements SurfaceHolder.Callback, Detector.Processor<Barcode>, Runnable {
    private static final int CAMERA_REQUEST_CODE = 0;

    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    private boolean isPermissionDenied;
    private String mApiKey;

    @Bind(R.id.camera_view) SurfaceView mCameraView;
    @Bind(R.id.qr_close) ImageView mCloseButton;
    @Bind(R.id.qr_frame) ImageView mQrFrame;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onQrFinished(String apiKey);

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
        ButterKnife.bind(this, view);

        if (haveCameraAccess()) {
            populateSurfaceView(true);
        } else {
            populateSurfaceView(false);
            requestionCameraPermission();
        }
        return view;
    }

    /** Layout initially inflated as GONE since SurfaceView doesn't update after Camera permission */
    private void populateSurfaceView(boolean shouldPopulate) {
        mCameraView.setVisibility(shouldPopulate ? View.VISIBLE : View.GONE);
        mCloseButton.setVisibility(shouldPopulate ? View.VISIBLE : View.GONE);
        mQrFrame.setVisibility(shouldPopulate ? View.VISIBLE : View.GONE);
        mCameraView.getHolder().addCallback(this);
        mBarcodeDetector.setProcessor(this);
    }

    /** SurfaceHolder Callback **/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestionCameraPermission();
                return;
            }
            mCameraSource.start(mCameraView.getHolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraSource.stop();
    }
    /** End SurfaceHolder Callback **/

    /** Barcode Detector Processor **/
    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        final SparseArray<Barcode> barcodes = detections.getDetectedItems();

        if (barcodes.size() != 0) {
            mApiKey = barcodes.valueAt(0).displayValue;
            getActivity().runOnUiThread(this);
        }
    }
    /** End Barcode Detector Processor **/

    /** Runnable since Barcode Detector is on separate thread */
    @Override
    public void run() {
        triggerQrFinishedListener(mApiKey);
    }

    @OnClick(R.id.qr_close)
    void closeQrDialogFragment() {
        triggerQrFinishedListener(null);
    }

    private boolean haveCameraAccess() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestionCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateSurfaceView(true);
            } else {
                isPermissionDenied = true; // Cannot dismiss fragment until onResume
            }
        }
    }

    private void triggerQrFinishedListener(String apiKey) {
        if (mListener != null) {
            mListener.onQrFinished(apiKey);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }
}