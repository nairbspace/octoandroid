package com.nairbspace.octoandroid.ui.status;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.StatusModel;
import com.nairbspace.octoandroid.ui.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusFragment extends BasePagerFragmentListener<StatusScreen,
        StatusFragment.Listener> implements StatusScreen {

    private static final String STATUS_MODEL_KEY = "status_model_key";

    @Inject StatusPresenter mPresenter;
    @Inject Gson mGson;

    @BindView(R.id.machine_state_textview) TextView mMachineStateTextView;
    @BindView(R.id.file_textview) TextView mFileTextview;
    @BindView(R.id.approx_total_print_time_textview) TextView mApproxTotalPrintTimeTextView;
    @BindView(R.id.print_time_textview) TextView mPrintTimeTextView;
    @BindView(R.id.print_time_left_textview) TextView mPrintTimeLeftTextView;
    @BindView(R.id.printed_bytes_textview) TextView mPrintedBytesTextView;
    @BindView(R.id.printed_file_size_textview) TextView mPrintedFileSizeTextView;

    private Listener mListener;
    private StatusModel mStatusModel;

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getActivity()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        if (savedInstanceState != null && savedInstanceState.getParcelable(STATUS_MODEL_KEY) != null) {
            updateUI((StatusModel) savedInstanceState.getParcelable(STATUS_MODEL_KEY));
        }
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void updateUI(StatusModel statusModel) {
        mStatusModel = statusModel;
        mMachineStateTextView.setText(statusModel.state());
        mFileTextview.setText(statusModel.file());
        mApproxTotalPrintTimeTextView.setText(statusModel.approxTotalPrintTime());
        mPrintTimeLeftTextView.setText(statusModel.printTimeLeft());
        mPrintTimeTextView.setText(statusModel.printTime());
        mPrintedBytesTextView.setText(statusModel.printedBytes());
        mPrintedFileSizeTextView.setText(statusModel.printedFileSize());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATUS_MODEL_KEY, mStatusModel);
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected StatusScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    public interface Listener {
        void onFragmentInteraction(Uri uri);
    }
}
