package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.FileCommand;
import com.nairbspace.octoandroid.model.FileCommandModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class FileCommandModelMapper extends MapperUseCase<FileCommandModel, FileCommand> {

    @Inject
    public FileCommandModelMapper(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<FileCommand> buildUseCaseObservable(final FileCommandModel fileCommandModel) {
        return Observable.create(new Observable.OnSubscribe<FileCommand>() {
            @Override
            public void call(Subscriber<? super FileCommand> subscriber) {
                try {
                    FileCommand fileCommand = FileCommand.builder()
                            .apiUrl(fileCommandModel.apiUrl())
                            .command(fileCommandModel.command())
                            .print(fileCommandModel.print())
                            .build();
                    subscriber.onNext(fileCommand);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new EntityMapperException(e));
                }
            }
        });
    }
}
