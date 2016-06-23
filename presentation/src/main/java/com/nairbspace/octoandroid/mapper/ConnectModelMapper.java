package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.ConnectModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class ConnectModelMapper extends MapperUseCase<ConnectModel, Connect> {

    @Inject
    public ConnectModelMapper(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<Connect> buildUseCaseObservableInput(final ConnectModel connectModel) {
        return Observable.create(new Observable.OnSubscribe<Connect>() {
            @Override
            public void call(Subscriber<? super Connect> subscriber) {
                try {
                    Connect connect = Connect.builder()
                            .isNotConnected(connectModel.isNotConnected())
                            .ports(connectModel.ports())
                            .baudrates(connectModel.baudrates())
                            .printerProfiles(connectModel.printerProfiles())
                            .selectedPortId(connectModel.selectedPortId())
                            .selectedBaudrateId(connectModel.selectedBaudrateId())
                            .selectedPrinterProfileId(connectModel.selectedPrinterProfileId())
                            .isSaveConnectionChecked(connectModel.isSaveConnectionChecked())
                            .isAutoConnectChecked(connectModel.isAutoConnectChecked())
                            .build();
                    subscriber.onNext(connect);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        });
    }
}
