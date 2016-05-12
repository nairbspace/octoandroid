package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.PrinterModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class PrinterModelMapper extends MapperUseCase<Printer, PrinterModel> {

    @Inject
    public PrinterModelMapper(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<PrinterModel> buildUseCaseObservable(final Printer printer) {
        return Observable.create(new Observable.OnSubscribe<PrinterModel>() {
            @Override
            public void call(Subscriber<? super PrinterModel> subscriber) {
                try {
                    PrinterModel model = PrinterModel.builder()
                            .id(printer.id())
                            .name(printer.name())
                            .apiKey(printer.apiKey())
                            .scheme(printer.scheme())
                            .host(printer.host())
                            .port(printer.port())
                            .build();
                    subscriber.onNext(model);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        });
    }
}
