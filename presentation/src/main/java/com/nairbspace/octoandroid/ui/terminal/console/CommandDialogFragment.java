package com.nairbspace.octoandroid.ui.terminal.console;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.ui.templates.BaseDialogFragment;
import com.nairbspace.octoandroid.views.AbstractTextWatcher;

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
    @BindView(R.id.command_uppercase_checkbox) CheckBox mUppercaseCheckbox;
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
        if (getArguments() != null) {
            String prevCommand = getArguments().getString(COMMAND_TEXT_KEY);
            if (prevCommand != null) command = prevCommand;
        }
        mEditText.setText(command);
        mEditText.setSelection(command.length());
        CommandListener listener = new CommandListener();
        mEditText.setOnEditorActionListener(listener);
        mEditText.addTextChangedListener(listener);
        mUppercaseCheckbox.setOnCheckedChangeListener(new CheckedListener());
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

    private final class CheckedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                String command = mEditText.getText().toString();
                setToUppercase(command);
            }
        }
    }

    private void setToUppercase(String command) {
        int position = mEditText.getSelectionStart();
        mEditText.setText(command.toUpperCase());
        mEditText.setSelection(position);
    }

    private final class CommandListener extends AbstractTextWatcher implements TextView.OnEditorActionListener {

        @Override
        public void afterTextChanged(Editable s) {
            String command = s.toString();
            if (mUppercaseCheckbox.isChecked() && !(command.equals(command.toUpperCase()))) {
                setToUppercase(command);
            }
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE && getDialog() != null) {
                Button ok = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
                if (ok != null) ok.performClick();
            }
            return false;
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
