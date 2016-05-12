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

    public Func1<PrinterDbEntity, Printer> maptoPrinter() {
        return new Func1<PrinterDbEntity, Printer>() {
            @Override
            public Printer call(PrinterDbEntity printerDbEntity) {
                try {
                    return Printer.builder()
                            .id(printerDbEntity.getId())
                            .name(printerDbEntity.getName())
                            .apiKey(printerDbEntity.getApiKey())
                            .scheme(printerDbEntity.getScheme())
                            .host(printerDbEntity.getHost())
                            .port(printerDbEntity.getPort())
                            .build();
                } catch (Exception e) {
                    throw Exceptions.propagate(new EntityMapperException(e));
                }
            }
        };
    }

    public Observable.OnSubscribe<AddPrinterEntity> mapAddPrinterToEntity(final AddPrinter addPrinter) {
        return new Observable.OnSubscribe<AddPrinterEntity>() {
            @Override
            public void call(Subscriber<? super AddPrinterEntity> subscriber) {
                try {
                    AddPrinterEntity addPrinterEntity = AddPrinterEntity.builder()
                            .accountName(addPrinter.accountName())
                            .ipAddress(addPrinter.ipAddress())
                            .port(addPrinter.port())
                            .apiKey(addPrinter.apiKey())
                            .isSslChecked(addPrinter.isSslChecked())
                            .build();
                    subscriber.onNext(addPrinterEntity);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new EntityMapperException());
                }
            }
        };
    }

    public Func1<ConnectionEntity, Connection> mapToConnection() {
        return new Func1<ConnectionEntity, Connection>() {
            @Override
            public Connection call(ConnectionEntity connectionEntity) {
                try {
                    return transform(connectionEntity, Connection.class);
                } catch (Exception e) {
                    throw Exceptions.propagate(new EntityMapperException(e));
                }
            }
        };
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
