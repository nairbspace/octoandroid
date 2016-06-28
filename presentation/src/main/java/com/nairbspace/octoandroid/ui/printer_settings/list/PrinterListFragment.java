package com.nairbspace.octoandroid.ui.printer_settings.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.PrinterModel;
import com.nairbspace.octoandroid.ui.templates.BaseFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrinterListFragment extends BaseFragmentListener<PrinterListScreen, PrinterListFragment.Listener>
        implements PrinterListScreen, PrinterListRvAdapter.Listener {

    @Inject PrinterListPresenter mPresenter;
    private Listener mListener;
    private List<PrinterModel> mPrinterModels;

    @BindView(R.id.printer_list_recyclerview) RecyclerView mRecyclerView;

    public static PrinterListFragment newInstance() {
        return new PrinterListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_list, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        mRecyclerView.setAdapter(new PrinterListRvAdapter(this, null));
        return view;
    }

    @Override
    public void updateUi(List<PrinterModel> printerModels) {
        mPrinterModels = printerModels;
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            mRecyclerView.setAdapter(new PrinterListRvAdapter(this, printerModels));
        } else if (adapter instanceof PrinterListRvAdapter) {
            ((PrinterListRvAdapter) adapter).setPrinterModels(printerModels);
        }
    }

    @Override
    public void printerSettingsClicked(int position) {
        mListener.printerSettingsClicked(mPrinterModels.get(position));
    }

    public void updateList() {
        mPresenter.execute();
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected PrinterListScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    public interface Listener {
        void printerSettingsClicked(PrinterModel printerModel);
    }
}
