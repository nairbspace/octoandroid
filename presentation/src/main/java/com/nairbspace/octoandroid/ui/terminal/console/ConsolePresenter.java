package com.nairbspace.octoandroid.ui.terminal.console;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.SendArbitraryCommand;
import com.nairbspace.octoandroid.domain.model.ArbitraryCommand;
import com.nairbspace.octoandroid.mapper.ArbitraryCommandMapper;
import com.nairbspace.octoandroid.model.WebsocketModel;
import com.nairbspace.octoandroid.ui.templates.UseCaseEventPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import timber.log.Timber;

public class ConsolePresenter extends UseCaseEventPresenter<ConsoleScreen, WebsocketModel> {

    private final ArbitraryCommandMapper mListMapper;
    private final SendArbitraryCommand mSendArbitraryCommand;
    private ConsoleScreen mScreen;

    @Inject
    public ConsolePresenter(EventBus eventBus, ArbitraryCommandMapper listMapper,
                            SendArbitraryCommand sendArbitraryCommand) {
        super(eventBus, listMapper, sendArbitraryCommand);
        mListMapper = listMapper;
        mSendArbitraryCommand = sendArbitraryCommand;
    }

    @Override
    protected void onInitialize(ConsoleScreen consoleScreen) {
        mScreen = consoleScreen;
    }

    @Override
    protected void onEvent(WebsocketModel model) {
        if (model.logs().isEmpty()) return;
        for (String log : model.logs()) {
            mScreen.updateUi(log);
        }
    }

    public void sendClicked(String command) {
        if (command.isEmpty()) mScreen.toastMessage("Brb sending nothing");
        else mListMapper.execute(new ListMapperSubscriber(), command);
    }

    private final class ListMapperSubscriber extends DefaultSubscriber<ArbitraryCommand> {

        @Override
        public void onError(Throwable e) {
            Timber.e(e, null); // Shouldn't happen
        }

        @Override
        public void onNext(ArbitraryCommand command) {
            mSendArbitraryCommand.execute(new SendCommandSubscriber(), command);
        }
    }

    private final class SendCommandSubscriber extends DefaultSubscriber {
        @Override
        public void onCompleted() {
            mScreen.toastSent();
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, null);
            mScreen.toastError();
        }
    }
}
