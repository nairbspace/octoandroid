package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.AddPrinterModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class AddPrinterModelMapper extends MapperUseCase<AddPrinterModel, AddPrinter>  {

    @Inject
    public AddPrinterModelMapper(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<AddPrinter> buildUseCaseObservable(final AddPrinterModel addPrinterModel) {
        return Observable.create(new Observable.OnSubscribe<AddPrinter>() {
            @Override
            public void call(Subscriber<? super AddPrinter> subscriber) {
                try {
                    AddPrinter addPrinter = AddPrinter.builder()
                            .accountName(addPrinterModel.accountName())
                            .ipAddress(addPrinterModel.ipAddress())
                            .port(addPrinterModel.port())
                            .apiKey(addPrinterModel.apiKey())
                            .isSslChecked(addPrinterModel.isSslChecked())
                            .build();
                    subscriber.onNext(addPrinter);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        });
    }
}
