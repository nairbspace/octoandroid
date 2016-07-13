package com.nairbspace.octoandroid.ui.connection;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.nairbspace.octoandroid.model.SpinnerModel;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;
import com.nairbspace.octoandroid.views.SetEnableView;
import com.nairbspace.octoandroid.views.SetShowView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionFragment extends BasePagerFragmentListener<ConnectionScreen,
        ConnectionFragment.Listener> implements ConnectionScreen,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String CONNECT_MODEL_KEY = "connection_model_key";

    @Inject ConnectionPresenter mPresenter;
    @Inject SetEnableView mSetEnableView;
    @Inject SetShowView mSetShowView;

    @BindView(R.id.connection_refresh_layout) SwipeRefreshLayout mRefreshLayout;
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
    @BindDimen(R.dimen.connect_progress_bar_z_translation) float mProgressZTranslation;
    @BindViews({R.id.serial_port_spinner, R.id.baudrate_spinner, R.id.printer_profile_spinner,
    R.id.save_connection_settings_checkbox, R.id.auto_connect_checkbox, R.id. connect_button})
    List<View> mInputViews;
    @BindViews({R.id.serial_port_spinner, R.id.baudrate_spinner, R.id.printer_profile_spinner,
            R.id.save_connection_settings_checkbox, R.id.auto_connect_checkbox,
            R.id.serial_port_spinner_title, R.id.baudrate_spinner_title,
            R.id.printer_profile_spinner_title})
    List<View> mNotConnectedViews;

    private Listener mListener;
    private ConnectModel mConnectModel;
    private List<String> mPorts;
    private ArrayAdapter<String> mSerialPortAdapter;
    private List<Integer> mBaudrates;
    private ArrayAdapter<Integer> mBaudrateAdapter;
    private List<SpinnerModel> mPrinterProfiles;
    private ArrayAdapter<SpinnerModel> mPrinterProfileAdapter;

    public static ConnectionFragment newInstance() {
        return new ConnectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getActivity()).getAppComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        setActionBarTitle("Status");
        mRefreshLayout.setOnRefreshListener(this);

        // ProgressBar doesn't show on top of CardView in Lollipop+ builds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mConnectProgressBar.setTranslationZ(mProgressZTranslation);
        }

        if (savedInstanceState != null && savedInstanceState.getParcelable(CONNECT_MODEL_KEY) != null) {
            mConnectModel = savedInstanceState.getParcelable(CONNECT_MODEL_KEY);
            updateUi(mConnectModel);
        }

        setEnableInputViews(false);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putParcelable(CONNECT_MODEL_KEY, getConnectModel());
        } catch (NullPointerException | IllegalStateException e) {
            if (mConnectModel != null) outState.putParcelable(CONNECT_MODEL_KEY, mConnectModel);
        }
    }

    private ConnectModel getConnectModel() {
        int selectedPortId = mSerialPortSpinner.getSelectedItemPosition();
        int selectedBaudrateId = mBaudrateSpinner.getSelectedItemPosition();
        int selectedPrinterProfileId = mPrinterProfileSpinner.getSelectedItemPosition();
        boolean isSaveConnectionChecked = mSaveConnectionSettingsCheckBox.isChecked();
        boolean isAutoConnectChecked = mAutoConnectCheckBox.isChecked();

        return ConnectModel.builder()
                .isNotConnected(mConnectModel.isNotConnected())
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

    @OnClick(R.id.connect_button)
    void connectButtonPressed() {
        mPresenter.connectButtonClicked(getConnectModel());
    }

    @Override
    public void showDisconnectAlert() {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle(R.string.warning)
                .setMessage(R.string.disconnect_warning)
                .setCancelable(true)
                .setNegativeButton(android.R.string.cancel, mDisconnectListener)
                .setPositiveButton(android.R.string.ok, mDisconnectListener)
                .show();
    }

    private DialogInterface.OnClickListener mDisconnectListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    mPresenter.executeConnect(getConnectModel());
                    break;
            }
        }
    };

    @Override
    public void setEnableInputViews(boolean shouldEnable) {
        ButterKnife.apply(mInputViews, mSetEnableView, shouldEnable);
    }

    @Override
    public void showProgressBar(boolean isLoading) {
        if (isLoading) {
            if (!mRefreshLayout.isRefreshing()) {
                mConnectProgressBar.setVisibility(View.VISIBLE);
            }
            setEnableInputViews(false);
        } else {
            mRefreshLayout.setRefreshing(false);
            mConnectProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateUi(ConnectModel connectModel) {
        mConnectModel = connectModel;
        updateSerialPortSpinner(connectModel.ports());
        updateBaudRateSpinner(connectModel.baudrates());
        updatePrinterProfileSpinner(connectModel.printerProfiles());
        mAutoConnectCheckBox.setChecked(connectModel.isAutoConnectChecked());
        showProgressBar(false);
        setEnableInputViews(true);
        setShowNotConnectedViews(connectModel.isNotConnected());
    }

    public void setShowNotConnectedViews(boolean shouldShow) {
        ButterKnife.apply(mNotConnectedViews, mSetShowView, shouldShow);
        mConnectButton.setText(shouldShow ? CONNECT : DISCONNECT);
    }

    @Override
    public void showErrorView() {
        setEnableInputViews(false);
        showProgressBar(false);
    }

    @Override
    public void updateUiWithDefaults(int defaultPortId, int defaultBaudrateId,
                                     int defaultPrinterProfileId) {
        setSpinnerSelection(mSerialPortSpinner, defaultPortId);
        setSpinnerSelection(mBaudrateSpinner, defaultBaudrateId);
        setSpinnerSelection(mPrinterProfileSpinner, defaultPrinterProfileId);
    }

    private void updateSerialPortSpinner(List<String> ports) {
        mPorts = ports;
        mSerialPortAdapter = updateAdapter(mSerialPortAdapter, ports);
        mSerialPortSpinner = updateSpinner(mSerialPortAdapter, mSerialPortSpinner);
    }

    private void updateBaudRateSpinner(List<Integer> baudrates) {
        mBaudrates = baudrates;
        mBaudrateAdapter = updateAdapter(mBaudrateAdapter, baudrates);
        mBaudrateSpinner = updateSpinner(mBaudrateAdapter, mBaudrateSpinner);
    }

    private void updatePrinterProfileSpinner(List<SpinnerModel> printerProfiles) {
        mPrinterProfiles = printerProfiles;
        mPrinterProfileAdapter = updateAdapter(mPrinterProfileAdapter, printerProfiles);
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
        int position = spinner.getSelectedItemPosition(); // Gets current position before updating
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
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_connection, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_connection_menu_item:
                mPresenter.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.execute();
    }

    public interface Listener {

    }
}