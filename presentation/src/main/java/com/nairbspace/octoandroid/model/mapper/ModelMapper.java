package com.nairbspace.octoandroid.model.mapper;

import android.content.Context;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.data.executor.JobExecutor;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.Printer;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.model.ConnectModel;
import com.nairbspace.octoandroid.model.ConnectionModel;
import com.nairbspace.octoandroid.model.PrinterModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ModelMapper {

    private final Gson mGson;
    private final JobExecutor mJobExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private final Context mContext; // TODO not sure if should pass activity context for this

    @Inject
    public ModelMapper(Gson gson, JobExecutor jobExecutor,
                       PostExecutionThread postExecutionThread, Context context) {
        mGson = gson;
        mJobExecutor = jobExecutor;
        mPostExecutionThread = postExecutionThread;
        mContext = context;
        // TODO Figure out better way to map
    }

    public Observable<AddPrinter> transformObs(final AddPrinterModel addPrinterModel) {
        return createObs(new Observable.OnSubscribe<AddPrinter>() {
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

    public Observable<Connect> transformObs(final ConnectModel connectModel) {
        return createObs(new Observable.OnSubscribe<Connect>() {
            @Override
            public void call(Subscriber<? super Connect> subscriber) {
                try {
                    Connect connect = Connect.builder()
                            .isNotConnected(connectModel.isNotConnected())
                            .ports(connectModel.ports())
                            .baudrates(connectModel.baudrates())
                            .printerProfileNames(connectModel.printerProfileNames())
                            .portId(connectModel.portId())
                            .baudrateId(connectModel.baudrateId())
                            .printerNameId(connectModel.printerNameId())
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

    public Observable<PrinterModel> transformObs(final Printer printer) {
        return createObs(new Observable.OnSubscribe<PrinterModel>() {
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

    public Observable<ConnectionModel> transformObs(final Connection connection) {
        return createObs(new Observable.OnSubscribe<ConnectionModel>() {
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
        List<String> ports = options.ports();

        List<Integer> baudrates = options.baudrates();

        List<Connection.PrinterProfile> printerProfiles = options.printerProfiles();
        List<String> printerProfileNames = new ArrayList<>();
        for (Connection.PrinterProfile printerProfile : printerProfiles) {
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

        int defaultPrinterNameId = 0;
        for (int i = 0; i < printerProfileNames.size(); i++) {
            if (printerProfileNames.get(i).equals(options.printerProfilePreference())) {
                defaultPrinterNameId = i;
            }
        }

        return ConnectionModel.builder()
                .isNotConnected(isNotConnected)
                .ports(ports)
                .baudrates(baudrates)
                .printerProfileNames(printerProfileNames)
                .defaultPortId(defaultPortId)
                .defaultBaudrateId(defaultBaudrateId)
                .defaultPrinterNameId(defaultPrinterNameId)
                .build();
    }

    /**
     *
     * @param inputObject Input object
     * @param outputType Output class
     * @param <T> Generic output
     * @return Output object
     */
    public <T> T transform(Object inputObject, Class<T> outputType) {
        String json = mGson.toJson(inputObject);
        return mGson.fromJson(json, outputType);
    }

    // TODO need to decide on how to thread
    public <T> Observable<T> createObs(Observable.OnSubscribe<T> f) {
        return Observable.create(f)
                .subscribeOn(Schedulers.from(mJobExecutor))
                .observeOn(mPostExecutionThread.getScheduler());
    }
}
