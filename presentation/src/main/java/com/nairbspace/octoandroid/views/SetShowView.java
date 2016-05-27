package com.nairbspace.octoandroid.views;

import android.support.annotation.NonNull;
import android.view.View;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SetShowView implements ButterKnife.Setter<View, Boolean> {

    @Inject public SetShowView() {

    }

    @Override
    public void set(@NonNull final View view, final Boolean show, int index) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
