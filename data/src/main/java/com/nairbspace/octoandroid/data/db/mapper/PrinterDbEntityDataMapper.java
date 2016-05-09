package com.nairbspace.octoandroid.data.db.mapper;

import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.Version;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrinterDbEntityDataMapper {

    @Inject
    public PrinterDbEntityDataMapper() {
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
}
