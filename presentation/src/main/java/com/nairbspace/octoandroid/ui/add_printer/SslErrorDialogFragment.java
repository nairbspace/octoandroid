package com.nairbspace.octoandroid.ui.add_printer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.ui.templates.BaseDialogFragment;

public class SslErrorDialogFragment extends BaseDialogFragment<SslErrorDialogFragment.Listener>
        implements Dialog.OnClickListener {

    private Listener mListener;

    public static SslErrorDialogFragment newInstance() {
        return new SslErrorDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.ssl_error_title)
                .setMessage(R.string.ssl_error_display_message)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setNegativeButton(android.R.string.cancel, this)
                .setNeutralButton(R.string.more, null) // Neutral click listener is implemented onStart
                .setPositiveButton(android.R.string.ok, this)
                .create();
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                mListener.tryUnsecureConnection(false);
                break;
            case DialogInterface.BUTTON_POSITIVE:
                mListener.tryUnsecureConnection(true);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(mNeutralClickListener);
        }
    }

    private View.OnClickListener mNeutralClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog alertDialog = (AlertDialog) getDialog();
            showExtendedMessage(alertDialog);
        }
    };

    private void showExtendedMessage(@NonNull AlertDialog alertDialog) {
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        Button neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        if (textView != null) {
            textView.setText(R.string.ssl_error_display_message_extended);
            neutralButton.setEnabled(false);
        }
    }

    public interface Listener {
        void tryUnsecureConnection(boolean ok);
    }
}
