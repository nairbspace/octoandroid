package com.nairbspace.octoandroid.ui.add_printer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.nairbspace.octoandroid.R;

public class SslErrorDialogFragment extends BaseDialogFragment<SslErrorDialogFragment.Listener>
        implements Dialog.OnClickListener{

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
                .setIcon(R.drawable.exclamation_triangle)
                .setNegativeButton(android.R.string.cancel, this)
                .setNeutralButton(R.string.info, this)
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
            case DialogInterface.BUTTON_NEGATIVE: // TODO need to implement info on SSL error
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                break;
            case DialogInterface.BUTTON_POSITIVE:
                mListener.tryUnsecureConnection();
                break;
        }
    }

    public interface Listener {
        void tryUnsecureConnection();
    }
}
