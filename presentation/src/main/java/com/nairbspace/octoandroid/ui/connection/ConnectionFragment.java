package com.nairbspace.octoandroid.ui.connection;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.ui.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.Presenter;
import com.nairbspace.octoandroid.views.SetEnableView;
import com.nairbspace.octoandroid.views.SetShowView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionFragment extends BasePagerFragmentListener<ConnectionScreen,
        ConnectionFragment.ConnectFragmentListener> implements ConnectionScreen {

    private static final String CONNECT_MODEL_KEY = "connection_model_key";
    private static final String SHOW_VIEW_KEY = "show_view_key";
    private ConnectModel mConnectModel;

    private ConnectFragmentListener mListener;
    @Inject ConnectionPresenter mPresenter;
    @Inject SetEnableView mSetEnableView;
    @Inject SetShowView mSetShowView;

    @BindView(R.id.connect_progressbar) ProgressBar mConnectProgressBar;
    @BindView(R.id.connect_cardview) CardView mConnectCardView;
    @BindView(R.id.serial_port_spinner) Spinner mSerialPortSpinner;
    @BindView(R.id.baudrate_spinner) Spinner mBaudrateSpinner;
    @BindView(R.id.printer_profile_spinner) Spinner mPrinterProfileSpinner;
    @BindView(R.id.connect_button) Button mConnectButton;
    @BindView(R.id.save_connection_settings_checkbox) CheckBox mSaveConnectionSettingsCheckBox;
    @BindView(R.id.auto_connect_checkbox) CheckBox mAutoConnectCheckBox;
    @BindString(R.string.connect) String CONNECT;
    @BindString(R.string.disconnect) String DISCONNECT;
    @BindViews({R.id.serial_port_spinner, R.id.baudrate_spinner, R.id.printer_profile_spinner,
    R.id.save_connection_settings_checkbox, R.id.auto_connect_checkbox, R.id. connect_button})
    List<View> mAllViews;

    @BindViews({R.id.serial_port_spinner, R.id.baudrate_spinner, R.id.printer_profile_spinner,
            R.id.save_connection_settings_checkbox, R.id.auto_connect_checkbox,
            R.id.serial_port_spinner_title, R.id.baudrate_spinner_title,
            R.id.printer_profile_spinner_title})
    List<View> mConnectShowView;

    private boolean mIsConnectButtonVisible;
    private List<String> mPorts;
    private ArrayAdapter<String> mSerialPortAdapter;
    private List<Integer> mBaudrates;
    private ArrayAdapter<Integer> mBaudrateAdapter;
    private ArrayAdapter<String> mPrinterProfileAdapter;
    private HashMap<String, String> mPrinterProfiles;

    public static ConnectionFragment newInstance() {
        return new ConnectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getActivity()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        setActionBarTitle("Status");

        if (savedInstanceState != null && savedInstanceState.getParcelable(CONNECT_MODEL_KEY) != null) {
            mConnectModel = savedInstanceState.getParcelable(CONNECT_MODEL_KEY);
            updateUi(mConnectModel, false);
        } else {
            updateUi(ConnectModel.dummyModel(), false); //TODO fix up start up logic!!!
        }

        return view;
    }

    @OnClick(R.id.connect_button)
    void connectButtonPressed() {
        mPresenter.connectButtonClicked(getConnectModel());
    }

    private ConnectModel getConnectModel() {
        int selectedPortId = mSerialPortSpinner.getSelectedItemPosition();
        int selectedBaudrateId = mBaudrateSpinner.getSelectedItemPosition();
        int selectedPrinterProfileId = mPrinterProfileSpinner.getSelectedItemPosition();
        boolean isSaveConnectionChecked = mSaveConnectionSettingsCheckBox.isChecked();
        boolean isAutoConnectChecked = mAutoConnectCheckBox.isChecked();

        return ConnectModel.builder()
                .isNotConnected(mIsConnectButtonVisible)
                .ports(mPorts)
                .baudrates(mBaudrates)
                .printerProfiles(mPrinterProfiles)
                .selectedPortId(selectedPortId)
                .selectedBaudrateId(selectedBaudrateId)
                .selectedPrinterProfileId(selectedPrinterProfileId)
                .isSaveConnectionChecked(isSaveConnectionChecked)
                .isAutoConnectChecked(isAutoConnectChecked)
                .build();
    }

    @Override
    public void updateUi(ConnectModel connectModel, boolean isUpdateFromPresenter) {
        mConnectModel = connectModel;
        updateSerialPortSpinner(connectModel.ports());
        updateBaudRateSpinner(connectModel.baudrates());
        updatePrinterProfileSpinner(connectModel.printerProfiles());
        mAutoConnectCheckBox.setChecked(connectModel.isAutoConnectChecked());
        enableScreen(connectModel.isNotConnected());
        mConnectButton.setEnabled(isUpdateFromPresenter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putParcelable(CONNECT_MODEL_KEY, getConnectModel());
        } catch (NullPointerException e) {
            outState.putParcelable(CONNECT_MODEL_KEY, mConnectModel);
        }
    }

    @Override
    public void updateUiWithDefaults(int defaultPortId, int defaultBaudrateId,
                                     int defaultPrinterProfileId) {
        setSpinnerSelection(mSerialPortSpinner, defaultPortId);
        setSpinnerSelection(mBaudrateSpinner, defaultBaudrateId);
        setSpinnerSelection(mPrinterProfileSpinner, defaultPrinterProfileId);
    }

    @Override
    public void updateSerialPortSpinner(List<String> ports) {
        mPorts = ports;
        mSerialPortAdapter = updateAdapter(mSerialPortAdapter, mPorts);
        mSerialPortSpinner = updateSpinner(mSerialPortAdapter, mSerialPortSpinner);
    }

    @Override
    public void updateBaudRateSpinner(List<Integer> baudrates) {
        mBaudrates = baudrates;
        mBaudrateAdapter = updateAdapter(mBaudrateAdapter, baudrates);
        mBaudrateSpinner = updateSpinner(mBaudrateAdapter, mBaudrateSpinner);
    }

    @Override
    public void updatePrinterProfileSpinner(HashMap<String, String> printerProfiles) {
        mPrinterProfiles = printerProfiles;
        List<String> printerProfileNames;
        if (printerProfiles.values() instanceof List) {
            printerProfileNames = (List<String>) printerProfiles.values();
        } else {
            printerProfileNames = new ArrayList<>(printerProfiles.values());
        }

        mPrinterProfileAdapter = updateAdapter(mPrinterProfileAdapter, printerProfileNames);
        mPrinterProfileSpinner = updateSpinner(mPrinterProfileAdapter, mPrinterProfileSpinner);
    }

    private <T> ArrayAdapter<T> updateAdapter(ArrayAdapter<T> adapter, List<T> list) {
        if (adapter == null) {
            int spinnerLayoutId = android.R.layout.simple_spinner_dropdown_item;
            adapter = new ArrayAdapter<>(getContext(), spinnerLayoutId, list);
        } else {
            adapter.clear();
            adapter.addAll(list);
        }
        return adapter;
    }

    private Spinner updateSpinner(ArrayAdapter adapter, Spinner spinner) {
        int position = spinner.getSelectedItemPosition();
        spinner.setAdapter(adapter);
        setSpinnerSelection(spinner, position);
        return spinner;
    }

    private void setSpinnerSelection(Spinner spinner, int position) {
        SpinnerAdapter adapter = spinner.getAdapter();
        if (adapter != null) {
            final int count = adapter.getCount();
            if (count > 0 && position < count) {
                spinner.setSelection(position, true);
            }
        }
    }

    @Override
    public void showProgressBar(boolean isLoading) {
        mConnectProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        enableScreen(!isLoading);
    }

    @Override
    public void enableScreen(boolean isNotConnected) {
        mIsConnectButtonVisible = isNotConnected;
        mConnectButton.setText(isNotConnected ? CONNECT : DISCONNECT);
        ButterKnife.apply(mAllViews, mSetEnableView, isNotConnected);
        ButterKnife.apply(mConnectShowView, mSetShowView, isNotConnected);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    @NonNull
    @Override
    protected ConnectFragmentListener setListener() {
        return (ConnectFragmentListener) getContext();
    }

    public interface ConnectFragmentListener {
        void onFragmentInteraction(Uri uri);
    }
}