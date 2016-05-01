package com.nairbspace.octoandroid.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.presenter.ConnectionPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConnectionFragment extends Fragment implements ConnectionScreen {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @Inject ConnectionPresenterImpl mPresenter;

    @Bind(R.id.serial_port_spinner) Spinner mSerialPortSpinner;
    @Bind(R.id.baudrate_spinner) Spinner mBaudrateSpinner;
    @Bind(R.id.printer_profile_spinner) Spinner mPrinterProfileSpinner;
    @Bind(R.id.connect_button) Button mConnectButton;
    @Bind(R.id.save_connection_settings_checkbox) CheckBox mSaveConnectionSettingsCheckBox;
    @Bind(R.id.auto_connect_checkbox) CheckBox mAutoConnectCheckBox;

    private int mDefaultSpinnerId = android.R.layout.simple_spinner_dropdown_item;

    private List<String> mPorts = new ArrayList<>();
    private ArrayAdapter<String> mSerialPortAdapter;

    private List<Integer> mBaudrates = new ArrayList<>();
    private ArrayAdapter<Integer> mBaudrateAdapter;

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

        mPresenter.setView(this);
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
        updateUI(mPorts, mBaudrates, mPrinterProfileNames);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.getConnection();
    }

    @OnClick(R.id.connect_button)
    void connectButtonPressed() {
        String connectButtonText = mConnectButton.getText().toString();
        int portPosition = mSerialPortSpinner.getSelectedItemPosition();
        int baudRatePosition = mBaudrateSpinner.getSelectedItemPosition();
        int printerProfileNamePosition = mPrinterProfileSpinner.getSelectedItemPosition();
        String port = mSerialPortSpinner.getSelectedItem().toString();
        String baudrate = mBaudrateSpinner.getSelectedItem().toString();
        String printerProfileName = mPrinterProfileSpinner.getSelectedItem().toString();
        boolean isSaveConnectionChecked = mSaveConnectionSettingsCheckBox.isChecked();
        boolean isAutoConnectChecked = mAutoConnectCheckBox.isChecked();

        mPresenter.connectButtonClicked(connectButtonText, portPosition, baudRatePosition,
                printerProfileNamePosition, isSaveConnectionChecked, isAutoConnectChecked);
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
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void updateUI(List<String> ports, List<Integer> baudrates, List<String> printerProfileNames) {
        updateSerialPortSpinner(ports);
        updateBaudRateSpinner(baudrates);
        updatePrinterProfileSpinner(printerProfileNames);
    }

    @Override
    public void updateSerialPortSpinner(List<String> ports) {
        mPorts = ports;
        if (mSerialPortAdapter == null) {
            mSerialPortAdapter = new ArrayAdapter<>(getContext(), mDefaultSpinnerId, mPorts);
            mSerialPortSpinner.setAdapter(mSerialPortAdapter);
        } else {
            mSerialPortAdapter.clear();
            mSerialPortAdapter.addAll(mPorts);
            mSerialPortAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateBaudRateSpinner(List<Integer> baudrates) {
        mBaudrates = baudrates;
        if (mBaudrateAdapter == null) {
            mBaudrateAdapter = new ArrayAdapter<>(getContext(), mDefaultSpinnerId, mBaudrates);
            mBaudrateSpinner.setAdapter(mBaudrateAdapter);
        } else {
            mBaudrateAdapter.clear();
            mBaudrateAdapter.addAll(mBaudrates);
            mBaudrateAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updatePrinterProfileSpinner(List<String> printerProfileNames) {
        mPrinterProfileNames = printerProfileNames;
        if (mPrinterProfileAdapter == null) {
            mPrinterProfileAdapter = new ArrayAdapter<>(getContext(), mDefaultSpinnerId, mPrinterProfileNames);
            mPrinterProfileSpinner.setAdapter(mPrinterProfileAdapter);
        } else {
            mPrinterProfileAdapter.clear();
            mPrinterProfileAdapter.addAll(mPrinterProfileNames);
            mPrinterProfileAdapter.notifyDataSetChanged();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
