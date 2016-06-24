package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.ConnectModel;

import java.util.ArrayList;
import java.util.List;

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

                    int spinnerSelectedPrinterProfile = connectModel.selectedPrinterProfileId();
                    String selectedId = connectModel.printerProfiles().get(spinnerSelectedPrinterProfile).id();
                    List<Connection.PrinterProfile> printerProfiles = new ArrayList<>();
                    int selectedPrinterProfileId = 0;

                    for (int i = 0; i < connectModel.printerProfiles().size(); i++) {
                        String id = connectModel.printerProfiles().get(i).id();
                        if (id.equals(selectedId)) selectedPrinterProfileId = i;
                        String name = connectModel.printerProfiles().get(i).name();
                        Connection.PrinterProfile printerProfile = Connection.PrinterProfile.create(id, name);
                        printerProfiles.add(printerProfile);
                    }

                    Connect connect = Connect.builder()
                            .isNotConnected(connectModel.isNotConnected())
                            .ports(connectModel.ports())
                            .baudrates(connectModel.baudrates())
                            .printerProfiles(printerProfiles)
                            .selectedPortId(connectModel.selectedPortId())
                            .selectedBaudrateId(connectModel.selectedBaudrateId())
                            .selectedPrinterProfileId(selectedPrinterProfileId)
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
