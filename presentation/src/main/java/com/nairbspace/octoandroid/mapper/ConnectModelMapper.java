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
    protected Observable<Connect> buildUseCaseObservable(final ConnectModel connectModel) {
        return Observable.create(new Observable.OnSubscribe<Connect>() {
            @Override
            public void call(Subscriber<? super Connect> subscriber) {
                try {
                    Connect connect = Connect.builder()
                            .isNotConnected(connectModel.isNotConnected())
                            .ports(connectModel.ports())
                            .baudrates(connectModel.baudrates())
                            .printerProfileIds(connectModel.printerProfileIds())
                            .portId(connectModel.portId())
                            .baudrateId(connectModel.baudrateId())
                            .printerProfileId(connectModel.printerProfileId())
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
