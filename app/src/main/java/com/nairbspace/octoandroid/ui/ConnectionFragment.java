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
import android.widget.Spinner;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.Connection;
import com.nairbspace.octoandroid.presenter.ConnectionPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

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

    public ConnectionFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Status");
        }

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.connect_button)
    void connectButtonPressed() {
        Timber.d("Button pressed");
        mPresenter.getConnection();
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
    public void updateUI(Connection connection) {
        Connection.Options options = connection.getOptions();
        List<String> ports = options.getPorts();
        ArrayAdapter<String> portAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, ports);
        mSerialPortSpinner.setAdapter(portAdapter);

        List<Integer> baudrates = options.getBaudrates();
        ArrayAdapter<Integer> baudratesAdapter = new ArrayAdapter<Integer>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, baudrates);
        mBaudrateSpinner.setAdapter(baudratesAdapter);

        List<Connection.PrinterProfile> printerProfiles = options.getPrinterProfiles();
        List<String> printerProfileNames = new ArrayList<>();
        for (Connection.PrinterProfile printerProfile : printerProfiles) {
            printerProfileNames.add(printerProfile.getName());
        }

        ArrayAdapter<String> printerProfileAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, printerProfileNames);
        mPrinterProfileSpinner.setAdapter(printerProfileAdapter);


    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
