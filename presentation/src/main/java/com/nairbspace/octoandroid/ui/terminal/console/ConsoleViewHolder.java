package com.nairbspace.octoandroid.ui.terminal.console;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsoleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.console_text) TextView mTextView;

    public ConsoleViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bindLog(String log) {
        mTextView.setText(log);
    }
}
