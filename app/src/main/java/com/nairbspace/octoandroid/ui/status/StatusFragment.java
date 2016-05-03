package com.nairbspace.octoandroid.ui.status;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.Presenter;
import com.nairbspace.octoandroid.ui.BaseViewPagerFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StatusFragment extends BaseViewPagerFragment<StatusScreen> implements StatusScreen {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @Inject StatusPresenter mPresenter;

    @Bind(R.id.machine_state_input) TextView mMachineStateTextView;
    @Bind(R.id.octoprint_version_input) TextView mOctoPrintVersionTextView;
    @Bind(R.id.api_version_input) TextView mApiVersionTextView;

    private OnFragmentInteractionListener mListener;

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
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Status");
        }
        ButterKnife.bind(this, view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updateUI(String machineState, String octoPrintVersion, String apiVersion) {
        updateMachineState(machineState);
        updateOctoPrintVersion(octoPrintVersion);
        updateApiVersion(apiVersion);
    }

    @Override
    public void updateMachineState(String machineState) {
        mMachineStateTextView.setText(machineState);
    }

    @Override
    public void updateOctoPrintVersion(String octoPrintVersion) {
        mOctoPrintVersionTextView.setText(octoPrintVersion);
    }

    @Override
    public void updateApiVersion(String apiVersion) {
        mApiVersionTextView.setText(apiVersion);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
