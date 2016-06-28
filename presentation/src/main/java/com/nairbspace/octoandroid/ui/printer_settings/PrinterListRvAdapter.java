package com.nairbspace.octoandroid.ui.printer_settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.PrinterModel;

import java.util.ArrayList;
import java.util.List;

public class PrinterListRvAdapter extends RecyclerView.Adapter<PrinterViewHolder>
        implements PrinterViewHolder.Listener {

    private final Listener mListener;
    private List<PrinterModel> mPrinterModels;

    public PrinterListRvAdapter(Listener listener, List<PrinterModel> printerModels) {
        mListener = listener;
        if (printerModels == null) mPrinterModels = new ArrayList<>();
        else mPrinterModels = printerModels;
    }

    public void setPrinterModels(List<PrinterModel> printerModels) {
        mPrinterModels = printerModels;
        notifyDataSetChanged();
    }

    public void deletePrinterModel(int position) {
        mPrinterModels.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public PrinterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_printer_list_item, parent, false);
        return new PrinterViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(PrinterViewHolder holder, int position) {
        holder.bindPrinterModel(mPrinterModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mPrinterModels.size();
    }

    @Override
    public void printerEditClicked(long id, int position) {
        mListener.printerEditClicked(id, position);
    }

    @Override
    public void printerDeleteClicked(long id, int position) {
        mListener.printerDeleteClicked(id, position);
    }

    @Override
    public void printerSetActiveClicked(long id) {
        mListener.printerSetActiveClicked(id);
    }

    public interface Listener {
        void printerEditClicked(long id, int position);
        void printerDeleteClicked(long id, int position);
        void printerSetActiveClicked(long id);
    }
}
