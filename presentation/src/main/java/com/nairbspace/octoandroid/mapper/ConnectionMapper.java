package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.data.disk.ResManager;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.model.SpinnerModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class ConnectionMapper extends MapperUseCase<Connection, ConnectModel> {

    private final ResManager mResManager;

    @Inject
    public ConnectionMapper(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            ResManager resManager) {
        super(threadExecutor, postExecutionThread);
        mResManager = resManager;
    }

    @Override
    protected Observable<ConnectModel> buildUseCaseObservableInput(final Connection connection) {
        return Observable.create(new Observable.OnSubscribe<ConnectModel>() {
            @Override
            public void call(Subscriber<? super ConnectModel> subscriber) {
                try {
                    ConnectModel connectModel = mapConnection(connection);
                    subscriber.onNext(connectModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        });
    }

    private ConnectModel mapConnection(Connection connection) {
        Connection.Current current = connection.current();
        String state = current.state();
        String closed = mResManager.getPrinterStateClosedString();
        boolean isNotConnected = state.equals(closed);

        Connection.Options options = connection.options();
        boolean autoconnect = connection.options().autoconnect();
        List<String> ports = options.ports();

        List<Integer> baudrates = options.baudrates();

        List<Connection.PrinterProfile> printerProfileList = options.printerProfiles();
        List<SpinnerModel> printerProfiles = new ArrayList<>();
        for (Connection.PrinterProfile printerProfile : printerProfileList) {
            SpinnerModel spinnerModel = new SpinnerModel(printerProfile.id(), printerProfile.name());
            printerProfiles.add(spinnerModel);
        }

        int defaultPortId = 0;
        for (int i = 0; i < ports.size(); i++) {
            if (ports.get(i).equals(options.portPreference())) {
                defaultPortId = i;
            }
        }

        int defaultBaudrateId = 0;
        for (int i = 0; i < baudrates.size(); i++) {
            if (baudrates.get(i).equals(options.baudratePreference())) {
                defaultBaudrateId = i;
            }
        }

        int defaultPrinterProfileId = 0;
        for (int i = 0; i < printerProfiles.size(); i++) {
            if (printerProfiles.get(i).id().equals(options.printerProfilePreference())) {
                defaultPrinterProfileId = i;
            }
        }

        return ConnectModel.builder()
                .isNotConnected(isNotConnected)
                .ports(ports)
                .baudrates(baudrates)
                .printerProfiles(printerProfiles)
                .selectedPortId(defaultPortId)
                .selectedBaudrateId(defaultBaudrateId)
                .selectedPrinterProfileId(defaultPrinterProfileId)
                .isSaveConnectionChecked(false)
                .isAutoConnectChecked(autoconnect)
                .build();
    }
}
