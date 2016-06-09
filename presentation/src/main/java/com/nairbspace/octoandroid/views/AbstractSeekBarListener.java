package com.nairbspace.octoandroid.views;

import android.widget.SeekBar;

/**
 * Abstract seekbar change listener to only override methods necessary
 */
public abstract class AbstractSeekBarListener implements SeekBar.OnSeekBarChangeListener {

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
