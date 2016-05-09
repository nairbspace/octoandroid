package com.nairbspace.octoandroid.data.entity.mapper;

import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.domain.AddPrinter;

import javax.inject.Inject;

public class AddPrinterEntityDataMapper {

    @Inject public AddPrinterEntityDataMapper() {
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
}
