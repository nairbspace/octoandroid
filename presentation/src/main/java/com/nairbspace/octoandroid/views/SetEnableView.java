package com.nairbspace.octoandroid.views;

import android.support.annotation.NonNull;
import android.view.View;

import javax.inject.Inject;

import butterknife.ButterKnife;

public final class SetEnableView implements ButterKnife.Setter<View, Boolean> {

    @Inject
    public SetEnableView() {
    }

    @Override
    public void set(@NonNull View view, Boolean value, int index) {
        view.setEnabled(value);
    }
}
