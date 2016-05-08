package com.nairbspace.octoandroid.data.entity.mapper;

import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.domain.Printer;

public class PrinterEntityDataMapper {
    public PrinterEntityDataMapper() {
    }

    public Printer transform(@NonNull PrinterDbEntity printerDbEntity) {
        return Printer.builder()
                .id(printerDbEntity.getId())
                .build();
    }
}
