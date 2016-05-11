package com.nairbspace.octoandroid.data.mapper;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.domain.model.Printer;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

@Singleton
public class EntityMapper {

    private final Gson mGson;

    @Inject
    public EntityMapper(Gson gson) {
        mGson = gson;
        // TODO clean up mapper
    }

    public Printer mapPrinterDbEntity(PrinterDbEntity printerDbEntity)  {
        return Printer.builder()
                .id(printerDbEntity.getId())
                .name(printerDbEntity.getName())
                .apiKey(printerDbEntity.getApiKey())
                .scheme(printerDbEntity.getScheme())
                .host(printerDbEntity.getHost())
                .port(printerDbEntity.getPort())
                .build();
    }

    public Observable<Printer> mapPrinterDbEntityObs(Observable<PrinterDbEntity> inputObservable) {
        return inputObservable.map(new Func1<PrinterDbEntity, Printer>() {
            @Override
            public Printer call(PrinterDbEntity printerDbEntity) {
                try {
                    return mapPrinterDbEntity(printerDbEntity);
                } catch (Exception e) {
                    throw Exceptions.propagate(new EntityMapperException(e));
                }
            }
        });
    }

    /**
     *
     * @param printer Printer object from domain module
     * @return PrinterDbEntity with no ID
     */
    public PrinterDbEntity mapPrinter(Printer printer) {
        PrinterDbEntity printerDbEntity = new PrinterDbEntity();
        printerDbEntity.setName(printer.name());
        printerDbEntity.setApiKey(printer.apiKey());
        printerDbEntity.setScheme(printer.scheme());
        printerDbEntity.setHost(printer.host());
        printerDbEntity.setPort(printer.port());
        return printerDbEntity;
    }

    public Observable<PrinterDbEntity> mapPrinterToEntityObs(final Printer printer) {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                try {
                    PrinterDbEntity printerDbEntity = mapPrinter(printer);
                    subscriber.onNext(printerDbEntity);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new EntityMapperException(e));
                }
            }
        });
    }

    public AddPrinterEntity mapAddPrinter(AddPrinter addPrinter) {
        return AddPrinterEntity.builder()
                .accountName(addPrinter.accountName())
                .ipAddress(addPrinter.ipAddress())
                .port(addPrinter.port())
                .apiKey(addPrinter.apiKey())
                .isSslChecked(addPrinter.isSslChecked())
                .build();
    }

    public Observable<AddPrinterEntity> mapAddPrinterToEntityObs(final AddPrinter addPrinter) {
        return Observable.create(new Observable.OnSubscribe<AddPrinterEntity>() {
            @Override
            public void call(Subscriber<? super AddPrinterEntity> subscriber) {
                try {
                    AddPrinterEntity addPrinterEntity = mapAddPrinter(addPrinter);
                    subscriber.onNext(addPrinterEntity);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new EntityMapperException());
                }
            }
        });
    }

    public Observable<Connection> mapConnectionEntityObs(Observable<ConnectionEntity> connectionEntityObs) {
        return connectionEntityObs.map(new Func1<ConnectionEntity, Connection>() {
            @Override
            public Connection call(ConnectionEntity connectionEntity) {
                try {
                    return transform(connectionEntity, Connection.class);
                } catch (Exception e) {
                    throw Exceptions.propagate(new EntityMapperException(e));
                }
            }
        });
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

    /**
     *
     * @param json String json input
     * @param inputClass Entity class
     * @param <T> Entity class
     * @return Entity object
     */
    public <T> T deserialize(String json, Class<T> inputClass) {
        return mGson.fromJson(json, inputClass);
    }

    /**
     *
     * @param inputObject input Object
     * @return Json string
     */
    public String serialize(Object inputObject) {
        return mGson.toJson(inputObject);
    }
}
