package com.nairbspace.octoandroid.ui.temp_controls;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TempControlsFragment extends BasePagerFragmentListener<TempControlsScreen, TempControlsFragment.Listener>
        implements TempControlsScreen {

    private static final String TEMP_CONTROL_MODELS_KEY = "temp_control_models_key";

    @Inject TempControlsPresenter mPresenter;
    private Listener mListener;

    @BindView(R.id.actual_temp_tool0_textview) TextView mActualTempTool0;
    @BindView(R.id.target_temp_tool0_input) TextInputEditText mTargetTempTool0Input;
    @OnClick(R.id.set_target_temp_tool0) void setTargetTempTool0() {
        int temp = parseInt(mTargetTempTool0Input.getText().toString());
        if (temp > 0) {
            // execute
        }
    }
    @BindView(R.id.offset_temp_tool0_input) TextInputEditText mOffsetTempTool0Input;
    @OnClick(R.id.set_offset_temp_tool0) void setOffsetTempTool0() {}

    @BindView(R.id.actual_temp_tool1_textview) TextView mActualTempTool1;
    @BindView(R.id.target_temp_tool1_input) TextInputEditText mTargetTempTool1Input;
    @OnClick(R.id.set_target_temp_tool1) void setTargetTempTool1() {}
    @BindView(R.id.offset_temp_tool1_input) TextInputEditText mOffsetTempTool1Input;
    @OnClick(R.id.set_offset_temp_tool1) void setOffsetTempTool1() {}

    @BindView(R.id.actual_temp_bed_textview) TextView mActualTempBed;
    @BindView(R.id.target_temp_bed_input) TextInputEditText mTargetTempBedInput;
    @OnClick(R.id.set_target_temp_bed) void setTargetTempBed() {}
    @BindView(R.id.offset_temp_bed_input) TextInputEditText mOffsetTempBedInput;
    @OnClick(R.id.set_offset_temp_bed) void setOffsetTempBed() {}

    @BindString(R.string.degrees_celcius) String DEG_C;

    public static TempControlsFragment newInstance() {
        return new TempControlsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temp_controls, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        if (savedInstanceState != null) {
            TempControlsModel model = savedInstanceState.getParcelable(TEMP_CONTROL_MODELS_KEY);
            if (model != null) {
                restoreSavedInstanceState(model);
            }
        }
        return view;
    }

    private void restoreSavedInstanceState(@NonNull TempControlsModel model) {
            mActualTempTool0.setText(model.actualTempTool0());
            mTargetTempTool0Input.setText(model.targetTempTool0());
            mOffsetTempTool0Input.setText(model.offsetTempTool0());

            mActualTempTool1.setText(model.actualTempTool1());
            mTargetTempTool1Input.setText(model.targetTempTool1());
            mOffsetTempTool1Input.setText(model.offsetTempTool1());

            mActualTempBed.setText(model.actualTempBed());
            mTargetTempBedInput.setText(model.targetTempBed());
            mOffsetTempBedInput.setText(model.offsetTempBed());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TempControlsModel model = TempControlsModel.builder()
                .actualTempTool0(mActualTempTool0.getText().toString())
                .targetTempTool0(mTargetTempTool0Input.getText().toString())
                .offsetTempTool0(mOffsetTempTool0Input.getText().toString())
                .actualTempTool1(mActualTempTool1.getText().toString())
                .targetTempTool1(mTargetTempTool1Input.getText().toString())
                .offsetTempTool1(mOffsetTempTool1Input.getText().toString())
                .actualTempBed(mActualTempBed.getText().toString())
                .targetTempBed(mTargetTempBedInput.getText().toString())
                .offsetTempBed(mOffsetTempBedInput.getText().toString())
                .build();

        outState.putParcelable(TEMP_CONTROL_MODELS_KEY, model);
    }

    @Override
    public void updateUi(WebsocketModel websocketModel) {
        String text = websocketModel.actualTempTool0() + DEG_C;
        mActualTempTool0.setText(text);
        text = websocketModel.actualTempTool1() + DEG_C;
        mActualTempTool1.setText(text);
        text = websocketModel.actualTempBed() + DEG_C;
        mActualTempBed.setText(text);
    }

    private int parseInt(String integer) {
        if (!TextUtils.isEmpty(integer)) {
            try {
                return Integer.parseInt(integer);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected TempControlsScreen setScreen() {
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
