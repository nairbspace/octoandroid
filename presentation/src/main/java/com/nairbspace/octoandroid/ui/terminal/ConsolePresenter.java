package com.nairbspace.octoandroid.ui.terminal;

import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

public class ConsolePresenter extends Presenter<ConsoleScreen> {

    private ConsoleScreen mScreen;

    @Inject
    public ConsolePresenter() {
    }

    @Override
    protected void onInitialize(ConsoleScreen consoleScreen) {
        mScreen = consoleScreen;
        for (int i = 1; i < 100; i++) {
            String text = "Recv: ok T:0.93 /0.00 B:0.96 /0.00 @:64 " + i;
            mScreen.updateUi(text);
        }
//        View consoleView;
//        for (int i = 1; i < 100; i++) {
//            consoleView = inflater.inflate(R.layout.fragment_console_text, mLinearLayout, false);
//            String text = "Recv: ok T:0.93 /0.00 B:0.96 /0.00 @:64 " + i;
//            TextView textView = ButterKnife.findById(consoleView, R.id.console_text);
//            textView.setText(text);
//            mLinearLayout.addView(textView);
//        }
    }
}
