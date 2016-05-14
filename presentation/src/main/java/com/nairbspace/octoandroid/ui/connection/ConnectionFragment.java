package com.nairbspace.octoandroid.ui.connection;

import android.content.Context;
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
import com.nairbspace.octoandroid.ui.BaseViewPagerFragment;
import com.nairbspace.octoandroid.ui.Presenter;
import com.nairbspace.octoandroid.views.SetEnableView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionFragment extends BaseViewPagerFragment<ConnectionScreen> implements ConnectionScreen {

    private static final String CONNECT_MODEL_KEY = "connection_model_key";
    private ConnectModel mConnectModel;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @Inject ConnectionPresenter mPresenter;
    @Inject SetEnableView mSetEnableView;

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

    private boolean mIsConnectButtonVisible;
    private List<String> mPorts;
    private ArrayAdapter<String> mSerialPortAdapter;
    private List<Integer> mBaudrates;
    private ArrayAdapter<Integer> mBaudrateAdapter;
    private ArrayAdapter<String> mPrinterProfileAdapter;
    private HashMap<String, String> mPrinterProfiles;

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

        if (savedInstanceState != null) {
            mConnectModel = savedInstanceState.getParcelable(CONNECT_MODEL_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        setActionBarTitle("Status");

        if (mConnectModel != null) {
            updateUi(mConnectModel, false);
        } else {
            createInitialView();
        }
        return view;
    }

    @Override
    public void createInitialView() {
        ConnectModel dummyModel = ConnectModel.builder()
                .isNotConnected(false)
                .ports(new ArrayList<String>())
                .baudrates(new ArrayList<Integer>())
                .printerProfiles(new HashMap<String, String>())
                .selectedPortId(0)
                .selectedBaudrateId(0)
                .selectedPrinterProfileId(0)
                .isSaveConnectionChecked(false)
                .isAutoConnectChecked(false)
                .build();

        updateUi(dummyModel, false);
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
        showConnectScreen(connectModel.isNotConnected());
        mConnectButton.setEnabled(isUpdateFromPresenter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CONNECT_MODEL_KEY, getConnectModel());
    }

    @Override
    public void updateUiWithDefaults(int defaultPortId, int defaultBaudrateId, int defaultPrinterProfileId) {
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
        mConnectCardView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showConnectScreen(boolean isNotConnected) {
        mIsConnectButtonVisible = isNotConnected;
        mConnectButton.setText(isNotConnected ? CONNECT : DISCONNECT);
        ButterKnife.apply(mAllViews, mSetEnableView, isNotConnected);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}