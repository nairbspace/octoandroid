package com.nairbspace.octoandroid.ui.add_printer;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.View;

import com.nairbspace.octoandroid.R;

public class IpAddressInputEditText extends TextInputEditText implements View.OnFocusChangeListener {

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

    /**
     * Overrides the OnFocusChangeListener to always be the one implemented here.
     * @param l Gets ignored if set outside of this class.
     */
    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        if (l != this) {
            l = this;
        }

        super.setOnFocusChangeListener(l);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof IpAddressInputEditText) {
            IpAddressInputEditText ipAddressEditText = (IpAddressInputEditText) v;

            if (hasFocus) {
                ipAddressEditText.setHint(R.string.ip_address_edit_text_hint);
            } else {
                ipAddressEditText.setHint("");
            }
        }
    }
}
