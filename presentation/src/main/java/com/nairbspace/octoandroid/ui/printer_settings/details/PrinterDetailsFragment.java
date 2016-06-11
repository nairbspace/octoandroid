package com.nairbspace.octoandroid.ui.printer_settings.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.templates.BaseFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class PrinterDetailsFragment extends BaseFragmentListener<PrinterDetailsScreen, PrinterDetailsFragment.Listener>
        implements PrinterDetailsScreen {
    private static final String PRINTER_MODEL_KEY = "printer_model_key";

    @Inject PrinterDetailsPresenter mPresenter;
    private Listener mListener;
    private PrinterModel mPrinterModel;

    public static PrinterDetailsFragment newInstance(@NonNull PrinterModel printerModel) {
        PrinterDetailsFragment fragment = new PrinterDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRINTER_MODEL_KEY, printerModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
        if (getArguments() != null && getArguments().getParcelable(PRINTER_MODEL_KEY) != null) {
            mPrinterModel = getArguments().getParcelable(PRINTER_MODEL_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_list_item_details, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        return view;
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected PrinterDetailsScreen setScreen() {
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
