package com.nairbspace.octoandroid.ui.printer_settings;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.PrinterModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrinterViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

    private final Listener mListener;

    @BindView(R.id.printer_name_text_view) TextView mPrinterName;
    @BindView(R.id.printer_ip_address_text_view) TextView mIpAddress;

    private PrinterModel mPrinterModel;

    public PrinterViewHolder(View itemView, Listener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
    }

    public void bindPrinterModel(PrinterModel printerModel) {
        mPrinterModel = printerModel;
        mPrinterName.setText(printerModel.name());
        mIpAddress.setText(printerModel.host());
    }

    @OnClick(R.id.printer_settings_button) void printerSettingsClicked(View v) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.activity_printer_list_item_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        long id = mPrinterModel.id();
        int position = getAdapterPosition();
        switch (item.getItemId()) {
            case R.id.printer_settings_edit_menu_item:
                mListener.printerEditClicked(id, position);
                return true;
            case R.id.printer_settings_delete_menu_item:
                mListener.printerDeleteClicked(id, position);
                return true;
        }
        return false;
    }

    public interface Listener {
        void printerEditClicked(long id, int position);
        void printerDeleteClicked(long id, int position);
    }
}
