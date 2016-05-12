package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.PrinterStateEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.http.Body;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class ApiManagerImpl implements ApiManager {
    private final OctoApi mOctoApi;

    @Inject
    public ApiManagerImpl(OctoApi octoApi) {
        mOctoApi = octoApi;
    }

    @Override
    public Observable<VersionEntity> getVersion() {
        return mOctoApi.getVersion();
    }

    @Override
    public Observable<ConnectionEntity> getConnection() {
        return mOctoApi.getConnection();
    }

    @Override
    public Observable<ConnectEntity> postConnect(@Body ConnectEntity connectEntity) {
        return mOctoApi.postConnect(connectEntity);
    }

    @Override
    public Observable<PrinterStateEntity> getPrinter() {
        return mOctoApi.getPrinter();
    }

    @Override
    public Func1<PrinterDbEntity, Observable<VersionEntity>> funcGetVersion() {
        return new Func1<PrinterDbEntity, Observable<VersionEntity>>() {
            @Override
            public Observable<VersionEntity> call(PrinterDbEntity printerDbEntity) {
                return mOctoApi.getVersion();
            }
        };
    }

    @Override
    public Func1<ConnectionEntity, Observable<ConnectionEntity>> funcGetConnection() {
        return new Func1<ConnectionEntity, Observable<ConnectionEntity>>() {
            @Override
            public Observable<ConnectionEntity> call(ConnectionEntity connectionEntity) {
                return mOctoApi.getConnection();
            }
        };
    }

    @Override
    public Func1<ConnectEntity, Observable<ConnectEntity>> connectToPrinter() {
        return new Func1<ConnectEntity, Observable<ConnectEntity>>() {
            @Override
            public Observable<ConnectEntity> call(ConnectEntity connectEntity) {
                return mOctoApi.postConnect(connectEntity);
            }
        };
    }

    @Override
    public Func1<ConnectEntity, Boolean> connectPrinterResult() { // TODO have to do this because result object is null?
        return new Func1<ConnectEntity, Boolean>() {
            @Override
            public Boolean call(ConnectEntity connectEntity) {
                return true;
            }
        };
    }
}
