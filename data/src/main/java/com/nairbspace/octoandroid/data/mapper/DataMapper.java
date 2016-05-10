package com.nairbspace.octoandroid.data.mapper;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.domain.AddPrinter;
import com.nairbspace.octoandroid.domain.Connection;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.Version;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataMapper {

    private final Gson mGson;

    @Inject
    public DataMapper(Gson gson) {
        mGson = gson;
        // TODO clean up mapper
    }

    public Printer transformWithNoId(@NonNull PrinterDbEntity printerDbEntity) {
        return Printer.builder()
                .name(printerDbEntity.getName())
                .apiKey(printerDbEntity.getApiKey())
                .scheme(printerDbEntity.getScheme())
                .host(printerDbEntity.getHost())
                .port(printerDbEntity.getPort())
                .build();
    }

    /**
     *
     * @param printer Printer object from domain module
     * @return PrinterDbEntity with no ID
     */
    public PrinterDbEntity transformToEntity(@NonNull Printer printer) {
        PrinterDbEntity printerDbEntity = new PrinterDbEntity();
        printerDbEntity.setName(printer.name());
        printerDbEntity.setApiKey(printer.apiKey());
        printerDbEntity.setScheme(printer.scheme());
        printerDbEntity.setHost(printer.host());
        printerDbEntity.setPort(printer.port());
        return printerDbEntity;
    }

    public Version transform(@NonNull VersionEntity versionEntity) {
        return Version.builder()
                .api(versionEntity.api())
                .server(versionEntity.server())
                .build();
    }

    public AddPrinterEntity transform(AddPrinter addPrinter) {
        return AddPrinterEntity.builder()
                .accountName(addPrinter.accountName())
                .ipAddress(addPrinter.ipAddress())
                .port(addPrinter.port())
                .apiKey(addPrinter.apiKey())
                .isSslChecked(addPrinter.isSslChecked())
                .build();
    }

    public Connection transform(ConnectionEntity connectionEntity) {
        String json = mGson.toJson(connectionEntity);
        return mGson.fromJson(json, Connection.class);
    }

    public String serialize(VersionEntity versionEntity) {
        return mGson.toJson(versionEntity, VersionEntity.class);
    }
}
