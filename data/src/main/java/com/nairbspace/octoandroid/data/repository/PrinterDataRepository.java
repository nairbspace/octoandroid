package com.nairbspace.octoandroid.data.repository;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.mapper.PrinterEntityDataMapper;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStore;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStoreFactory;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import rx.Observable;
import rx.functions.Func1;

public class PrinterDataRepository implements PrinterRepository {

    private final PrinterDataStoreFactory mPrinterDataStoreFactory;
    private final PrinterEntityDataMapper mPrinterEntityDataMapper;

    public PrinterDataRepository(PrinterEntityDataMapper printerEntityDataMapper,
                                 PrinterDataStoreFactory printerDataStoreFactory) {
        mPrinterEntityDataMapper = printerEntityDataMapper;
        mPrinterDataStoreFactory = printerDataStoreFactory;
    }

    @Override
    public Observable<Printer> printerDetails() {
        final PrinterDataStore printerDataStore = mPrinterDataStoreFactory.create();
        return printerDataStore.printerEntityDetails()
                .map(new Func1<PrinterDbEntity, Printer>() {
                    @Override
                    public Printer call(PrinterDbEntity printerDbEntity) {
                        return mPrinterEntityDataMapper.transform(printerDbEntity);
                    }
                });
    }
}
