package com.nairbspace.octoandroid.mapper;

import android.content.Context;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.ConnectionModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class ConnectionModelMapper extends MapperUseCase<Connection, ConnectionModel> {

    private final Context mContext;

    @Inject
    public ConnectionModelMapper(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 Context context) {
        super(threadExecutor, postExecutionThread);
        mContext = context; // TODO not sure where to get context from...
    }

    @Override
    protected Observable<ConnectionModel> buildUseCaseObservable(final Connection connection) {
        return Observable.create(new Observable.OnSubscribe<ConnectionModel>() {
            @Override
            public void call(Subscriber<? super ConnectionModel> subscriber) {
                try {
                    ConnectionModel connectionModel2 = mapConnection(connection);
                    subscriber.onNext(connectionModel2);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        });
    }

    private ConnectionModel mapConnection(Connection connection) {
        Connection.Current current = connection.current();
        String state = current.state();
        String closed = mContext.getString(R.string.printer_state_closed);
        boolean isNotConnected = state.equals(closed);

        Connection.Options options = connection.options();
        boolean autoconnect = connection.options().autoconnect();
        List<String> ports = options.ports();

        List<Integer> baudrates = options.baudrates();

        List<Connection.PrinterProfile> printerProfiles = options.printerProfiles();
        List<String> printerProfileIds = new ArrayList<>();
        List<String> printerProfileNames = new ArrayList<>();
        for (Connection.PrinterProfile printerProfile : printerProfiles) {
            printerProfileIds.add(printerProfile.id());
            printerProfileNames.add(printerProfile.name());
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
        for (int i = 0; i < printerProfileNames.size(); i++) {
            if (printerProfileNames.get(i).equals(options.printerProfilePreference())) {
                defaultPrinterProfileId = i;
            }
        }

        return ConnectionModel.builder()
                .isNotConnected(isNotConnected)
                .ports(ports)
                .baudrates(baudrates)
                .printerProfileIds(printerProfileIds)
                .printerProfileNames(printerProfileNames)
                .defaultPortId(defaultPortId)
                .defaultBaudrateId(defaultBaudrateId)
                .defaultPrinterProfileId(defaultPrinterProfileId)
                .autoconnect(autoconnect)
                .build();
    }
}
