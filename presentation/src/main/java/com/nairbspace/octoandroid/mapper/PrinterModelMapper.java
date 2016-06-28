package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.PrinterModel;

import java.util.ArrayList;
import java.util.List;

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
    protected Observable<PrinterModel> buildUseCaseObservableInput(final Printer printer) {
        return Observable.create(new Observable.OnSubscribe<PrinterModel>() {
            @Override
            public void call(Subscriber<? super PrinterModel> subscriber) {
                try {
                    subscriber.onNext(printerToPrinterModel(printer));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        });
    }

    public static PrinterModel printerToPrinterModel(Printer printer) {
        return PrinterModel.builder()
                .id(printer.id())
                .name(printer.name())
                .apiKey(printer.apiKey())
                .scheme(printer.scheme())
                .host(printer.host())
                .port(printer.port())
                .websocketPath(printer.websocketPath())
                .webcamPathQuery(printer.webcamPathQuery())
                .uploadLocation(printer.uploadLocation())
                .build();
    }

    public static Printer printerModelToPrinter(PrinterModel printerModel) {
         return Printer.builder()
                .id(printerModel.id())
                .name(printerModel.name())
                .apiKey(printerModel.apiKey())
                .scheme(printerModel.scheme())
                .host(printerModel.host())
                .port(printerModel.port())
                .websocketPath(printerModel.websocketPath())
                .webcamPathQuery(printerModel.webcamPathQuery())
                .uploadLocation(printerModel.uploadLocation())
                .build();
    }

    public static class ListMapper extends MapperUseCase<List<Printer>, List<PrinterModel>> {

        @Inject
        public ListMapper(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
            super(threadExecutor, postExecutionThread);
        }

        @Override
        protected Observable<List<PrinterModel>> buildUseCaseObservableInput(final List<Printer> printers) {
            return Observable.create(new Observable.OnSubscribe<List<PrinterModel>>() {
                @Override
                public void call(Subscriber<? super List<PrinterModel>> subscriber) {
                    try {
                        List<PrinterModel> printerModels = new ArrayList<>();
                        for (Printer printer : printers) {
                            printerModels.add(PrinterModelMapper.printerToPrinterModel(printer));
                        }
                        subscriber.onNext(printerModels);
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(new TransformErrorException());
                    }
                }
            });
        }
    }

    public static class DomainMapper extends MapperUseCase<PrinterModel, Printer> {

        @Inject
        public DomainMapper(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
            super(threadExecutor, postExecutionThread);
        }

        @Override
        protected Observable<Printer> buildUseCaseObservableInput(final PrinterModel printerModel) {
            return Observable.create(new Observable.OnSubscribe<Printer>() {
                @Override
                public void call(Subscriber<? super Printer> subscriber) {
                    try {
                        subscriber.onNext(printerModelToPrinter(printerModel));
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(new TransformErrorException());
                    }
                }
            });
        }
    }
}
