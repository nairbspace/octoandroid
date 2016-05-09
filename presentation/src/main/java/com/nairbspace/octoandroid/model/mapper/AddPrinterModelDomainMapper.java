package com.nairbspace.octoandroid.model.mapper;

import android.support.annotation.NonNull;

import com.nairbspace.octoandroid.domain.AddPrinter;
import com.nairbspace.octoandroid.model.AddPrinterModel;

import javax.inject.Inject;

public class AddPrinterModelDomainMapper {

    @Inject
    public AddPrinterModelDomainMapper() {

    }

    public AddPrinter transform(@NonNull AddPrinterModel addPrinterModel) {
        return AddPrinter.builder()
                .accountName(addPrinterModel.accountName())
                .ipAddress(addPrinterModel.ipAddress())
                .port(addPrinterModel.port())
                .apiKey(addPrinterModel.apiKey())
                .isSslChecked(addPrinterModel.isSslChecked())
                .build();
    }

}
