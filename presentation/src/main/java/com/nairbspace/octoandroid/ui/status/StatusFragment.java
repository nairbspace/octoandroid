package com.nairbspace.octoandroid.ui.status;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusFragment extends BasePagerFragmentListener<StatusScreen,
        StatusFragment.Listener> implements StatusScreen {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @Inject StatusPresenter mPresenter;

    @BindView(R.id.machine_state_textview) TextView mMachineStateTextView;
    @BindView(R.id.file_textview) TextView mFileTextview;
    @BindView(R.id.print_time_textview) TextView mPrintTimeTextView;

    private Listener mListener;

    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getActivity()).getAppComponent().inject(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void updateUI(String machineState, String octoPrintVersion, String apiVersion) {
        updateMachineState(machineState);
        updateFileName(octoPrintVersion);
        updateTime(apiVersion);
    }

    @Override
    public void updateMachineState(String machineState) {
        mMachineStateTextView.setText(machineState);
    }

    @Override
    public void updateFileName(String fileName) {
        mFileTextview.setText(fileName);
    }

    @Override
    public void updateTime(String time) {
        mPrintTimeTextView.setText(time);
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
