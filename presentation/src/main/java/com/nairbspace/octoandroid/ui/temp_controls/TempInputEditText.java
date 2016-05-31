package com.nairbspace.octoandroid.ui.temp_controls;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.View;

import com.nairbspace.octoandroid.R;

public class TempInputEditText extends TextInputEditText implements View.OnFocusChangeListener {

    public TempInputEditText(Context context) {
        super(context);
        setOnFocusChangeListener(this);
    }

    public TempInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnFocusChangeListener(this);
    }

    public TempInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
        if (v instanceof TempInputEditText) {
            TempInputEditText tempInputEditText = (TempInputEditText) v;

            if (hasFocus) {
                tempInputEditText.setHint(R.string.degrees_celcius);
            } else {
                tempInputEditText.setHint("");
            }
        }
    }
}
