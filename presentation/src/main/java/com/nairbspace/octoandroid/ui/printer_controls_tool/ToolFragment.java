package com.nairbspace.octoandroid.ui.printer_controls_tool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.domain.model.ToolCommand;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;
import com.nairbspace.octoandroid.views.AbstractSeekBarListener;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToolFragment extends BasePagerFragmentListener<ToolScreen, ToolFragment.Listener>
        implements ToolScreen {

    @BindString(R.string.flow_rate_colon_space) String FLOWRATE;
    @BindString(R.string.percent) String PERCENT;
    @BindInt(R.integer.flowrate_percent_offset) int mFlowrateOffset;

    @Inject ToolPresenter mPresenter;
    private Listener mListener;

    @BindView(R.id.tool_radiogroup) RadioGroup mRadioGroup;
    @BindView(R.id.tool_amount_input) TextInputEditText mAmountInputText;
    @OnClick(R.id.tool_retract_button) void retractButtonClicked() {mPresenter.executeSelectTool(ToolCommand.Type.RETRACT);}
    @OnClick(R.id.tool_extract_button) void extractButtonClicked() {mPresenter.executeSelectTool(ToolCommand.Type.EXTRACT);}
    @BindView(R.id.tool_flowrate_seekbar) SeekBar mSeekBar;
    @BindView(R.id.set_tool_flowrate_button) Button mFlowRateButton;
    @OnClick(R.id.set_tool_flowrate_button) void flowrateButtonClicked() {mPresenter.executeSelectTool(ToolCommand.Type.FLOWRATE);}

    public static ToolFragment newInstance() {
        return new ToolFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_controls_tool, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        updateFlowrateButtonText();
        mSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        return view;
    }

    @Override
    public int getFlowrateWithOffset() {
        return mSeekBar.getProgress() + mFlowrateOffset;
    }

    @Override
    public String getAmount() {
        return mAmountInputText.getText().toString();
    }

    @Override
    public int getTool() {
        switch (mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.tool0_radio: return 0;
            case R.id.tool1_radio: return 1;
            default: return 0;
        }
    }

    private String createFlowrateString(int feedrate) {
        return FLOWRATE + feedrate + PERCENT;
    }

    private void updateFlowrateButtonText() {
        String s = createFlowrateString(getFlowrateWithOffset());
        mFlowRateButton.setText(s);
    }

    @Override
    public void showInputAmountError(String message) {
        mAmountInputText.setError(message);
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
    protected ToolScreen setScreen() {
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

    private final class SeekBarListener extends AbstractSeekBarListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateFlowrateButtonText();
        }
    }
}
