package com.nairbspace.octoandroid.ui.terminal.console;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.ui.templates.BaseDialogFragment;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CommandDialogFragment extends BaseDialogFragment<CommandDialogFragment.Listener>
        implements Dialog.OnClickListener {

    private static final String COMMAND_TEXT_KEY = "command_text_key";
    private static final String DIALOG_ID_KEY = "dialog_id_key";
    @BindString(R.string.edit_command) String EDIT_COMMAND;
    @BindString(R.string.send) String SEND;

    private Listener mListener;
    @BindView(R.id.console_command_input) EditText mEditText;
    @BindDrawable(R.drawable.ic_mode_edit_black_24dp) Drawable mEditDrawable;

    public static CommandDialogFragment newInstance(String command) {
        return newInstance(command, 0);
    }

    /**
     * Id is used to identify listener action if executed from different fragments
     */
    public static CommandDialogFragment newInstance(String command, int dialogId) {
        CommandDialogFragment fragment = new CommandDialogFragment();
        Bundle args = new Bundle();
        args.putString(COMMAND_TEXT_KEY, command);
        args.putInt(DIALOG_ID_KEY, dialogId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_console_edit, null);
        setUnbinder(ButterKnife.bind(this, view));
        String command = "";
        if (getArguments() != null && getArguments().getString(COMMAND_TEXT_KEY) != null) {
            command = getArguments().getString(COMMAND_TEXT_KEY);
        }
        mEditText.setText(command);
        return new AlertDialog.Builder(getContext())
                .setTitle(EDIT_COMMAND)
                .setIcon(mEditDrawable)
                .setView(view)
                .setCancelable(true)
                .setNeutralButton(SEND, this)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String command = mEditText.getText().toString();
        int id = 0;
        if (getArguments() != null) id = getArguments().getInt(DIALOG_ID_KEY);
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mListener.onDialogFinish(command, false, id);
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                mListener.onDialogFinish(command, true, id);
                break;
        }
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    public interface Listener {
        void onDialogFinish(String command, boolean send, int dialogId);
    }
}
