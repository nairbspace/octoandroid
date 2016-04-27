package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.model.Connection;
import com.nairbspace.octoandroid.model.Printer;
import com.nairbspace.octoandroid.model.PrinterDao;
import com.nairbspace.octoandroid.net.OctoApiImpl;
import com.nairbspace.octoandroid.net.OctoInterceptor;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GetConnectionImpl implements GetConnection {

    @Inject OctoApiImpl mApi;
    @Inject PrinterDao mPrinterDao;
    @Inject OctoInterceptor mInterceptor;

    @Inject
    public GetConnectionImpl() {
    }


    @Override
    public void getConnection(final GetConnectionFinishedListener listener) {
        Printer printer = mPrinterDao.queryBuilder()
                .where(PrinterDao.Properties.Name.eq("192.168.1.204"))
                .unique();

        mInterceptor.setInterceptor(printer.getScheme(), printer.getHost(), printer.getPort(), printer.getApi_key());

        listener.onLoading();
        mApi.getConnectionObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Connection>() {
                    @Override
                    public void onCompleted() {
                        listener.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Connection connection) {
                        checkData(connection);
                        listener.onSuccess(connection);
                    }
                });
    }

    void checkData(Connection connection) {
        Connection.Current current = connection.getCurrent();
        Connection.Options option = connection.getOptions();

        Timber.d(current.getPort());

        List<Integer> baudrates = option.getBaudrates();
        for (int baudrate : baudrates) {
            Timber.d(baudrate + "");
        }
    }
}
