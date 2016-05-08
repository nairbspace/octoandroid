package com.nairbspace.octoandroid.data.db.mapper;

import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.domain.Printer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrinterDbEntityDataMapper {

    @Inject
    public PrinterDbEntityDataMapper() {
    }

    public Printer transform(@NonNull PrinterDbEntity printerDbEntity) {
        return Printer.builder()
                .id(printerDbEntity.getId())
                .build();
    }
}
