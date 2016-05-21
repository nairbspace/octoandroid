package com.nairbspace.octoandroid.ui.add_printer;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.nairbspace.octoandroid.R;

public class PortInputEditText extends TextInputEditText implements View.OnFocusChangeListener {

    private OnFocusChangeListener mOnFocusChangeListener;

    public PortInputEditText(Context context) {
        super(context);
        setOnFocusChangeListener(this);
    }

    public PortInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnFocusChangeListener(this);
    }

    public PortInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnFocusChangeListener(this);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        if (l == this) {
            super.setOnFocusChangeListener(l);
            return;
        }
        mOnFocusChangeListener = l;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (mOnFocusChangeListener == null) {
            return;
        }

        View rootView = v.getRootView();
        CheckBox sslCheckBox = null;
        if (rootView != null) {
            sslCheckBox = (CheckBox) rootView.findViewById(R.id.ssl_checkbox);
        }

        if (sslCheckBox == null) {
            return;
        }

        if (v instanceof EditText) {
            EditText portEditText = (EditText) v;

            if (hasFocus) {
                if (sslCheckBox.isChecked()) {
                    portEditText.setHint(R.string.port_443);
                } else {
                    portEditText.setHint(R.string.port_80);
                }
            } else {
                portEditText.setHint("");
            }
        }
    }
}
