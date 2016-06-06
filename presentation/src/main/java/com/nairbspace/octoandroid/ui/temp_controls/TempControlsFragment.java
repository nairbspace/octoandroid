package com.nairbspace.octoandroid.ui.temp_controls;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.domain.model.TempCommand.ToolBedOffsetTemp;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;
import com.nairbspace.octoandroid.views.SetEnableView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TempControlsFragment extends BasePagerFragmentListener<TempControlsScreen, TempControlsFragment.Listener>
        implements TempControlsScreen, TextView.OnEditorActionListener {

    private static final String TEMP_CONTROL_MODELS_KEY = "temp_control_models_key";
    @BindString(R.string.degrees_celcius) String DEG_C;

    @Inject TempControlsPresenter mPresenter;
    @Inject SetEnableView mSetEnableView;
    private Listener mListener;

    @BindView(R.id.actual_temp_tool0_textview) TextView mActualTempTool0;
    @BindView(R.id.target_temp_tool0_input) TextInputEditText mTargetTempTool0Input;
    @BindView(R.id.set_target_temp_tool0) Button mSetTargetTempTool0Button;
    @OnClick(R.id.set_target_temp_tool0) void setTargetTempTool0() {
        mPresenter.execute(ToolBedOffsetTemp.TARGET_TOOL0, mTargetTempTool0Input.getText().toString());
    }
    @BindView(R.id.offset_temp_tool0_input) TextInputEditText mOffsetTempTool0Input;
    @BindView(R.id.set_offset_temp_tool0) Button mSetOffsetTempTool0Button;
    @OnClick(R.id.set_offset_temp_tool0) void setOffsetTempTool0() {
        mPresenter.execute(ToolBedOffsetTemp.OFFSET_TOOL0, mOffsetTempTool0Input.getText().toString());
    }
    @BindView(R.id.actual_temp_tool1_textview) TextView mActualTempTool1;
    @BindView(R.id.target_temp_tool1_input) TextInputEditText mTargetTempTool1Input;
    @BindView(R.id.set_target_temp_tool1) Button mSetTargetTempTool1Button;
    @OnClick(R.id.set_target_temp_tool1) void setTargetTempTool1() {
        mPresenter.execute(ToolBedOffsetTemp.TARGET_TOOL1, mTargetTempTool1Input.getText().toString());
    }
    @BindView(R.id.offset_temp_tool1_input) TextInputEditText mOffsetTempTool1Input;
    @BindView(R.id.set_offset_temp_tool1) Button mSetOffsetTempTool1Button;
    @OnClick(R.id.set_offset_temp_tool1) void setOffsetTempTool1() {
        mPresenter.execute(ToolBedOffsetTemp.OFFSET_TOOL1, mOffsetTempTool1Input.getText().toString());
    }
    @BindView(R.id.actual_temp_bed_textview) TextView mActualTempBed;
    @BindView(R.id.target_temp_bed_input) TextInputEditText mTargetTempBedInput;
    @BindView(R.id.set_target_temp_bed) Button mSetTargetTempBedButton;
    @OnClick(R.id.set_target_temp_bed) void setTargetTempBed() {
        mPresenter.execute(ToolBedOffsetTemp.TARGET_BED, mTargetTempBedInput.getText().toString());
    }
    @BindView(R.id.offset_temp_bed_input) TextInputEditText mOffsetTempBedInput;
    @BindView(R.id.set_offset_temp_bed) Button mSetOffsetTempBedButton;
    @OnClick(R.id.set_offset_temp_bed) void setOffsetTempBed() {
        mPresenter.execute(ToolBedOffsetTemp.OFFSET_BED, mOffsetTempBedInput.getText().toString());
    }

    @BindViews({R.id.set_target_temp_tool0, R.id.set_offset_temp_tool0,
            R.id.set_target_temp_tool1, R.id.set_offset_temp_tool1,
            R.id.set_target_temp_bed, R.id.set_offset_temp_bed})
    List<View> mEnableViews;

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
        mTargetTempTool0Input.setOnEditorActionListener(this);
        mOffsetTempTool0Input.setOnEditorActionListener(this);
        mTargetTempTool1Input.setOnEditorActionListener(this);
        mOffsetTempTool1Input.setOnEditorActionListener(this);
        mTargetTempBedInput.setOnEditorActionListener(this);
        mOffsetTempBedInput.setOnEditorActionListener(this);

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

    @Override
    public void enableButtons(boolean shouldEnable) {
        ButterKnife.apply(mEnableViews, mSetEnableView, shouldEnable);
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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            switch (v.getId()) {
                case R.id.target_temp_tool0_input:
                    setTargetTempTool0();
                    break;
                case R.id.offset_temp_tool0_input:
                    setOffsetTempTool0();
                    break;
                case R.id.target_temp_tool1_input:
                    setTargetTempTool1();
                    break;
                case R.id.offset_temp_tool1_input:
                    setOffsetTempTool1();
                    break;
                case R.id.target_temp_bed_input:
                    setTargetTempBed();
                    break;
                case R.id.offset_temp_bed_input:
                    setOffsetTempBed();
                    break;
            }
        }
        return false;
    }

    @Override
    public void inputError(ToolBedOffsetTemp toolBedOffsetTemp, String error) {
        switch (toolBedOffsetTemp) {
            case TARGET_TOOL0:
                mTargetTempTool0Input.setError(error);
                break;
            case OFFSET_TOOL0:
                mOffsetTempTool0Input.setError(error);
                break;
            case TARGET_TOOL1:
                mTargetTempTool1Input.setError(error);
                break;
            case OFFSET_TOOL1:
                mOffsetTempTool1Input.setError(error);
                break;
            case TARGET_BED:
                mTargetTempBedInput.setError(error);
                break;
            case OFFSET_BED:
                mOffsetTempBedInput.setError(error);
                break;
        }
    }

    @Override
    public void hideSoftKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface Listener {

    }
}
