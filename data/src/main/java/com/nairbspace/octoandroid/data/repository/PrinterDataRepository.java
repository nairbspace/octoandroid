package com.nairbspace.octoandroid.data.repository;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.db.mapper.PrinterDbEntityDataMapper;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStore;
import com.nairbspace.octoandroid.data.repository.datasource.PrinterDataStoreFactory;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public class PrinterDataRepository implements PrinterRepository {

    private final PrinterDataStoreFactory mPrinterDataStoreFactory;
    private final PrinterDbEntityDataMapper mPrinterDbEntityDataMapper;

    @Inject
    public PrinterDataRepository(PrinterDbEntityDataMapper printerDbEntityDataMapper,
                                 PrinterDataStoreFactory printerDataStoreFactory) {
        mPrinterDbEntityDataMapper = printerDbEntityDataMapper;
        mPrinterDataStoreFactory = printerDataStoreFactory;
    }

    @Override
    public Observable<Printer> printerDetails() {
        final PrinterDataStore printerDataStore = mPrinterDataStoreFactory.create();
        return printerDataStore.printerEntityDetails()
                .map(new Func1<PrinterDbEntity, Printer>() {
                    @Override
                    public Printer call(PrinterDbEntity printerDbEntity) {
                        return mPrinterDbEntityDataMapper.transform(printerDbEntity);
                    }
                });
    }
}
