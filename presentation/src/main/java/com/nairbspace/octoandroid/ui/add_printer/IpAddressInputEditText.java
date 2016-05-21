package com.nairbspace.octoandroid.ui.add_printer;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.View;

import com.nairbspace.octoandroid.R;

public class IpAddressInputEditText extends TextInputEditText implements View.OnFocusChangeListener {

    private OnFocusChangeListener mOnFocusChangeListener;

    public IpAddressInputEditText(Context context) {
        super(context);
        setOnFocusChangeListener(this);
    }

    public IpAddressInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnFocusChangeListener(this);
    }

    public IpAddressInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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

        if (v instanceof TextInputEditText) {
            TextInputEditText ipAddressEditText = (TextInputEditText) v;

            if (hasFocus) {
                ipAddressEditText.setHint(R.string.ip_address_edit_text_hint);
            } else {
                ipAddressEditText.setHint("");
            }
        }
    }
}
