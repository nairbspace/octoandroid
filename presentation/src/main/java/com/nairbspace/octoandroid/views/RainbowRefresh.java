package com.nairbspace.octoandroid.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class RainbowRefresh extends SwipeRefreshLayout {

    public RainbowRefresh(Context context) {
        super(context);
        setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
    }

    public RainbowRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
    }
}
