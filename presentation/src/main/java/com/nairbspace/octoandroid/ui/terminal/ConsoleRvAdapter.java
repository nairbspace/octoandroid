package com.nairbspace.octoandroid.ui.terminal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.R;

import java.util.List;

public class ConsoleRvAdapter extends RecyclerView.Adapter<ConsoleViewHolder> {

    private final List<String> mLogList;

    public ConsoleRvAdapter(List<String> logList) {
        mLogList = logList;
    }

    public void addLogItem(String log) {
        mLogList.add(log);
        notifyItemInserted(mLogList.size());
    }

    public List<String> getLogList() {
        return mLogList;
    }

    @Override
    public ConsoleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_console_text, parent, false);
        return new ConsoleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConsoleViewHolder holder, int position) {
        holder.bindLog(mLogList.get(position));
    }

    @Override
    public int getItemCount() {
        return mLogList.size();
    }
}
