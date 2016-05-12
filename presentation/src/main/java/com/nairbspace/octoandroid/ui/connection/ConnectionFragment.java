package com.nairbspace.octoandroid.ui.connection;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.ui.BaseViewPagerFragment;
import com.nairbspace.octoandroid.ui.Presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionFragment extends BaseViewPagerFragment<ConnectionScreen> implements ConnectionScreen {

    private String CONNECT;
    private String DISCONNECT;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @Inject ConnectionPresenter mPresenter;

    @BindView(R.id.connect_progressbar) ProgressBar mConnectProgressBar;
    @BindView(R.id.connect_cardview) CardView mConnectCardView;
    @BindView(R.id.serial_port_spinner) Spinner mSerialPortSpinner;
    @BindView(R.id.baudrate_spinner) Spinner mBaudrateSpinner;
    @BindView(R.id.printer_profile_spinner) Spinner mPrinterProfileSpinner;
    @BindView(R.id.connect_button) Button mConnectButton;
    @BindView(R.id.save_connection_settings_checkbox) CheckBox mSaveConnectionSettingsCheckBox;
    @BindView(R.id.auto_connect_checkbox) CheckBox mAutoConnectCheckBox;

    private int mDefaultSpinnerId;
    private boolean mIsNotConnected;

    private List<String> mPorts = new ArrayList<>();
    private ArrayAdapter<String> mSerialPortAdapter;

    private List<Integer> mBaudrates = new ArrayList<>();
    private ArrayAdapter<Integer> mBaudrateAdapter;

    private List<String> mPrinterProfileIds = new ArrayList<>();
    private List<String> mPrinterProfileNames = new ArrayList<>();
    private ArrayAdapter<String> mPrinterProfileAdapter;

    public static ConnectionFragment newInstance(String param1, String param2) {
        ConnectionFragment fragment = new ConnectionFragment();
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

        Resources res = getResources();
        CONNECT = res.getString(R.string.connect);
        DISCONNECT = res.getString(R.string.disconnect);
        mDefaultSpinnerId = android.R.layout.simple_spinner_dropdown_item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Status");
        }
        ButterKnife.bind(this, view);
        updateUI(mPorts, mBaudrates, mPrinterProfileIds, mPrinterProfileNames, true, false);
        return view;
    }

    @OnClick(R.id.connect_button)
    void connectButtonPressed() {
        int portId = mSerialPortSpinner.getSelectedItemPosition();
        int baudrateId = mBaudrateSpinner.getSelectedItemPosition();
        int printerProfileId = mPrinterProfileSpinner.getSelectedItemPosition();
        boolean isSaveConnectionChecked = mSaveConnectionSettingsCheckBox.isChecked();
        boolean isAutoConnectChecked = mAutoConnectCheckBox.isChecked();

        ConnectModel connectModel = ConnectModel.builder()
                .isNotConnected(mIsNotConnected)
                .ports(mPorts)
                .baudrates(mBaudrates)
                .printerProfileIds(mPrinterProfileIds)
                .portId(portId)
                .baudrateId(baudrateId)
                .printerProfileId(printerProfileId)
                .isSaveConnectionChecked(isSaveConnectionChecked)
                .isAutoConnectChecked(isAutoConnectChecked)
                .build();

        mPresenter.connectButtonClicked(connectModel);
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

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected ConnectionScreen setScreen() {
        return this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updateUI(List<String> ports, List<Integer> baudrates,
                         List<String> printerProfileIds, List<String> printerProfileNames,
                         boolean isNotConnected, boolean autoconnect) {
        updateSerialPortSpinner(ports);
        updateBaudRateSpinner(baudrates);
        updatePrinterProfileSpinner(printerProfileNames);
        mAutoConnectCheckBox.setChecked(autoconnect);

        mPrinterProfileIds = printerProfileIds;
        mIsNotConnected = isNotConnected;
        showConnectScreen(isNotConnected);
    }

    @Override
    public void updateUiWithDefaults(int defaultPortId, int defaultBaudrateId, int defaultProfileNameId) {
        mSerialPortSpinner.setSelection(defaultPortId);
        mBaudrateSpinner.setSelection(defaultBaudrateId);
        mSerialPortSpinner.setSelection(defaultProfileNameId);
        mConnectButton.setEnabled(true);
    }

    @Override
    public void updateSerialPortSpinner(List<String> ports) {
        mPorts = ports;
        if (mSerialPortAdapter == null) {
            mSerialPortAdapter = new ArrayAdapter<>(getContext(), mDefaultSpinnerId, ports);
            mSerialPortSpinner.setAdapter(mSerialPortAdapter);
        } else {
            int position = mSerialPortSpinner.getSelectedItemPosition();
            mSerialPortAdapter.clear();
            mSerialPortAdapter.addAll(ports);
            mSerialPortSpinner.setAdapter(mSerialPortAdapter);
            mSerialPortSpinner.setSelection(position);
        }
    }

    @Override
    public void updateBaudRateSpinner(List<Integer> baudrates) {
        mBaudrates = baudrates;
        if (mBaudrateAdapter == null) {
            mBaudrateAdapter = new ArrayAdapter<>(getContext(), mDefaultSpinnerId, baudrates);
            mBaudrateSpinner.setAdapter(mBaudrateAdapter);
        } else {
            int position = mBaudrateSpinner.getSelectedItemPosition();
            mBaudrateAdapter.clear();
            mBaudrateAdapter.addAll(baudrates);
            mBaudrateSpinner.setAdapter(mBaudrateAdapter);
            mBaudrateSpinner.setSelection(position);
        }
    }

    @Override
    public void updatePrinterProfileSpinner(List<String> printerProfileNames) {
        mPrinterProfileNames = printerProfileNames;
        if (mPrinterProfileAdapter == null) {
            mPrinterProfileAdapter = new ArrayAdapter<>(getContext(), mDefaultSpinnerId, printerProfileNames);
            mPrinterProfileSpinner.setAdapter(mPrinterProfileAdapter);
        } else {
            int position = mPrinterProfileSpinner.getSelectedItemPosition(); // TODO should save oninstancestate?
            mPrinterProfileAdapter.clear();
            mPrinterProfileAdapter.addAll(printerProfileNames);
            mPrinterProfileSpinner.setAdapter(mPrinterProfileAdapter);
            mPrinterProfileSpinner.setSelection(position);
        }
    }

    @Override
    public void showProgressBar(boolean isLoading) {
        mConnectProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        mConnectCardView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showConnectScreen(boolean isNotConnected) {
        mSerialPortSpinner.setEnabled(isNotConnected);
        mBaudrateSpinner.setEnabled(isNotConnected);
        mPrinterProfileSpinner.setEnabled(isNotConnected);
        mSaveConnectionSettingsCheckBox.setEnabled(isNotConnected);
        mAutoConnectCheckBox.setEnabled(isNotConnected);
        mConnectButton.setText(isNotConnected ? CONNECT : DISCONNECT);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}