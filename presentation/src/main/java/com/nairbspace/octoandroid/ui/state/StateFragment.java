package com.nairbspace.octoandroid.ui.state;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StateFragment extends BasePagerFragmentListener<StateScreen,
        StateFragment.Listener> implements StateScreen {

    private static final String STATUS_MODEL_KEY = "status_model_key";

    @Inject StatePresenter mPresenter;

    @BindView(R.id.machine_state_textview) TextView mMachineStateTextView;
    @BindView(R.id.file_textview) TextView mFileTextview;
    @BindView(R.id.approx_total_print_time_textview) TextView mApproxTotalPrintTimeTextView;
    @BindView(R.id.print_time_textview) TextView mPrintTimeTextView;
    @BindView(R.id.print_time_left_textview) TextView mPrintTimeLeftTextView;
    @BindView(R.id.printed_bytes_textview) TextView mPrintedBytesTextView;
    @BindView(R.id.printed_file_size_textview) TextView mPrintedFileSizeTextView;
    @BindString(R.string.status) String STATUS;

    private Listener mListener;
    private WebsocketModel mWebsocketModel;

    public static StateFragment newInstance() {
        return new StateFragment();
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
        setActionBarTitle(STATUS);
        if (savedInstanceState != null && savedInstanceState.getParcelable(STATUS_MODEL_KEY) != null) {
            updateUI((WebsocketModel) savedInstanceState.getParcelable(STATUS_MODEL_KEY));
        }
        return view;
    }

    @Override
    public void updateUI(WebsocketModel websocketModel) {
        mWebsocketModel = websocketModel;
        mMachineStateTextView.setText(websocketModel.state());
        mFileTextview.setText(websocketModel.file());
        mApproxTotalPrintTimeTextView.setText(websocketModel.approxTotalPrintTime());
        mPrintTimeLeftTextView.setText(websocketModel.printTimeLeft());
        mPrintTimeTextView.setText(websocketModel.printTime());
        mPrintedBytesTextView.setText(websocketModel.printedBytes());
        mPrintedFileSizeTextView.setText(websocketModel.printedFileSize());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATUS_MODEL_KEY, mWebsocketModel);
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected StateScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    public interface Listener {

    }
}
