package com.nairbspace.octoandroid.ui.printer_settings.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.PrinterModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrinterViewHolder extends RecyclerView.ViewHolder {

    private final Listener mListener;

    @BindView(R.id.printer_name_text_view) TextView mPrinterName;
    @BindView(R.id.printer_ip_address_text_view) TextView mIpAddress;

    public PrinterViewHolder(View itemView, Listener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
    }

    public void bindPrinterModel(PrinterModel printerModel) {
        mPrinterName.setText(printerModel.name());
        mIpAddress.setText(printerModel.host());
    }

    @OnClick(R.id.printer_settings_button) void printerSettingsClicked() {
        mListener.printerSettingsClicked(getAdapterPosition());
    }

    public interface Listener {
        void printerSettingsClicked(int position);
    }
}
