package com.nairbspace.octoandroid.ui.terminal.console;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConsoleFragment extends BasePagerFragmentListener<ConsoleScreen, ConsoleFragment.Listener>
        implements ConsoleScreen {

    private static final String LOG_LIST_KEY = "log_list_key";
    private static final String AUTO_SCROLL_KEY = "auto_scroll_key";
    private static final String LOCK_KEY = "lock_key";

    @BindString(R.string.lock) String LOCK;
    @BindString(R.string.unlock) String UNLOCK;
    @BindString(R.string.disable_autoscroll) String DISABLE_AUTOSCROLL;
    @BindString(R.string.enable_autoscroll) String ENABLE_AUTOSCROLL;
    @BindString(R.string.command_sent) String COMMAND_SENT;
    @BindString(R.string.exception_command_error) String COMMAND_ERROR;

    @Inject ConsolePresenter mPresenter;
    private Listener mListener;

    @BindView(R.id.console_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.console_command_edit) EditText mCommandEditText;
    @BindDrawable(R.drawable.ic_close_white_24dp) Drawable mCloseDrawable;
    @BindDrawable(R.drawable.ic_expand_more_white_24dp) Drawable mExpandDrawable;
    @BindDrawable(R.drawable.ic_lock_open_white_24dp) Drawable mUnlockDrawable;
    @BindDrawable(R.drawable.ic_lock_outline_white_24dp) Drawable mLockDrawable;
    private View mMainView;
    private boolean mIsAutoScrollEnabled = true;

    public static ConsoleFragment newInstance() {
        return new ConsoleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_console, container, false);
        setUnbinder(ButterKnife.bind(this, mMainView));
        List<String> logList;
        if (savedInstanceState == null) logList = new ArrayList<>();
        else logList = restoreInstance(savedInstanceState);
        ConsoleRvAdapter adapter = new ConsoleRvAdapter(logList);
        mRecyclerView.setAdapter(adapter);
        return mMainView;
    }

    private List<String> restoreInstance(Bundle savedInstanceState) {
        mIsAutoScrollEnabled = savedInstanceState.getBoolean(AUTO_SCROLL_KEY);
        boolean lock = savedInstanceState.getBoolean(LOCK_KEY);
        ViewCompat.setNestedScrollingEnabled(mMainView, lock);
        List<String> logList = savedInstanceState.getStringArrayList(LOG_LIST_KEY);
        if (logList == null) return new ArrayList<>();
        else return logList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(LOCK_KEY, ViewCompat.isNestedScrollingEnabled(mMainView));
        outState.putBoolean(AUTO_SCROLL_KEY, mIsAutoScrollEnabled);
        if (mRecyclerView.getAdapter() == null) return;
        ConsoleRvAdapter adapter = ((ConsoleRvAdapter) mRecyclerView.getAdapter());
        ArrayList<String> logLost = (ArrayList<String>) adapter.getLogList();
        outState.putStringArrayList(LOG_LIST_KEY, logLost);
    }

    @Override
    public void updateUi(String log) {
        ConsoleRvAdapter adapter;
        if (mRecyclerView.getAdapter() == null) {
            List<String> logList = new ArrayList<>();
            logList.add(log);
            adapter = new ConsoleRvAdapter(logList);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter = ((ConsoleRvAdapter) mRecyclerView.getAdapter());
            adapter.addLogItem(log);
        }

        if (mIsAutoScrollEnabled) {
            mRecyclerView.setVerticalScrollBarEnabled(false);
            mRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
            mRecyclerView.setVerticalScrollBarEnabled(true);
        }
    }

    @OnClick(R.id.console_command_edit) void editClicked() {
        mListener.openCommandDialogFragment(getCommand());
    }

    @OnClick(R.id.console_send_button) void sendClicked() {
        mPresenter.sendClicked(getCommand());
    }

    public void updateCommand(String command, boolean send) {
        mCommandEditText.setText(command);
        if (send) sendClicked();
    }

    private String getCommand() {
        return mCommandEditText.getText().toString();
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toastSent() {
        toastMessage(COMMAND_SENT);
    }

    @Override
    public void toastError() {
        toastMessage(COMMAND_ERROR);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_console, menu);
        MenuItem lock = menu.findItem(R.id.console_lock_menu_item);
        if (lock != null) updateLockIcon(lock);
        MenuItem scroll = menu.findItem(R.id.console_stop_auto_scroll_menu_item);
        if (scroll != null) updateScrollIcon(scroll);
    }

    private void updateLockIcon(@NonNull MenuItem menuItem) {
        boolean isEnabled = ViewCompat.isNestedScrollingEnabled(mMainView);
        menuItem.setTitle(isEnabled ? UNLOCK : LOCK);
        menuItem.setIcon(isEnabled ? mUnlockDrawable : mLockDrawable);
    }

    private void toggleLock() {
        boolean isEnabled = ViewCompat.isNestedScrollingEnabled(mMainView);
        ViewCompat.setNestedScrollingEnabled(mMainView, !isEnabled);
    }

    private void updateScrollIcon(@NonNull MenuItem menuItem) {
        menuItem.setTitle(mIsAutoScrollEnabled ? DISABLE_AUTOSCROLL : ENABLE_AUTOSCROLL);
        menuItem.setIcon(mIsAutoScrollEnabled ? mCloseDrawable : mExpandDrawable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.console_lock_menu_item:
                toggleLock();
                updateLockIcon(item);
                return true;
            case R.id.console_stop_auto_scroll_menu_item:
                mIsAutoScrollEnabled = !mIsAutoScrollEnabled;
                updateScrollIcon(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected ConsoleScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    public interface Listener {
        void openCommandDialogFragment(String command);
    }
}
