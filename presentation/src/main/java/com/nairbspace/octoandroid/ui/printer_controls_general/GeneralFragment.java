package com.nairbspace.octoandroid.ui.printer_controls_general;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.domain.model.ArbitraryCommand;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;
import com.nairbspace.octoandroid.views.SetEnableView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GeneralFragment extends BasePagerFragmentListener<GeneralScreen, GeneralFragment.Listener>
        implements GeneralScreen {

    @Inject GeneralPresenter mPresenter;
    @Inject SetEnableView mSetEnableView;
    private Listener mListener;

    @OnClick(R.id.printer_controls_motors_off_button) void motorOffClicked() {mPresenter.executeCommand(ArbitraryCommand.Type.MOTORS_OFF, null, null);}
    @OnClick(R.id.printer_controls_fan_on_button) void fanOnClicked() {mPresenter.executeCommand(ArbitraryCommand.Type.FAN_ON, null, null);}
    @OnClick(R.id.printer_controls_fan_off_button) void fanOffClicked() {mPresenter.executeCommand(ArbitraryCommand.Type.FAN_OFF, null, null);}

    @BindViews({R.id.printer_controls_motors_off_button, R.id.printer_controls_fan_on_button, R.id.printer_controls_fan_off_button})
    List<View> mEnableViews;

    public static GeneralFragment newInstance() {
        return new GeneralFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_controls_general, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    public void setEnableViews(boolean enable) {
        ButterKnife.apply(mEnableViews, mSetEnableView, enable);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected GeneralScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    public interface Listener {

    }
}
