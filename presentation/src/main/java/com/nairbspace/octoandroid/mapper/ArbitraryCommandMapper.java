package com.nairbspace.octoandroid.mapper;

import android.text.TextUtils;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.ArbitraryCommand;
import com.nairbspace.octoandroid.exception.TransformErrorException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class ArbitraryCommandMapper extends MapperUseCase<String, ArbitraryCommand>{

    @Inject
    public ArbitraryCommandMapper(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<ArbitraryCommand> buildUseCaseObservableInput(final String s) {
        return Observable.create(new Observable.OnSubscribe<ArbitraryCommand>() {
            @Override
            public void call(Subscriber<? super ArbitraryCommand> subscriber) {
                try {
                    List<String> commandList = parse(s);
                    if (commandList.isEmpty()) subscriber.onError(new TransformErrorException());
                    ArbitraryCommand command = ArbitraryCommand.createMultiple(commandList);
                    subscriber.onNext(command);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private static List<String> parse(String command) {
        List<String> commandList;
        if (command.contains(",")) commandList = multiParse(command);
        else commandList = parseSingle(command);
        return commandList;
    }

    private static List<String> parseSingle(String command) {
        List<String> commandList = new ArrayList<>();
        String trim = command.trim();
        if (!TextUtils.isEmpty(trim)) commandList.add(trim);
        return commandList;
    }

    private static List<String> multiParse(String command) {
        List<String> commandList = new ArrayList<>();
        String[] commands = command.split(",");
        for (String s : commands) {
            String trim = s.trim();
            if (!TextUtils.isEmpty(trim)) commandList.add(trim);
        }
        return commandList;
    }
}
