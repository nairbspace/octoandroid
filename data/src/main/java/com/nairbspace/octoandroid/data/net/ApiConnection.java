package com.nairbspace.octoandroid.data.net;

import android.content.Context;
import android.text.TextUtils;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.entity.AddPrinterEntity;
import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.PrinterStateEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.data.exception.IncorrectAddPrinterFormattingException;
import com.nairbspace.octoandroid.data.exception.IpAddressEmptyException;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import retrofit2.http.Body;
import rx.Observable;
import rx.Subscriber;

public class ApiConnection implements OctoApi {
    private static final String HTTP_SCHEME = "http";
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;
    private static final String HTTPS_SCHEME = "https";

    private final OctoInterceptor mOctoInterceptor;
    private final Context mContext;
    private final OctoApi mOctoApi;

    @Inject
    public ApiConnection(OctoInterceptor octoInterceptor, Context context,
                         OctoApi octoApi) {
        mOctoInterceptor = octoInterceptor;
        mContext = context;
        mOctoApi = octoApi;
    }

    public void setOctoInterceptor(PrinterDbEntity dbEntity) {
        mOctoInterceptor.setScheme(dbEntity.getScheme());
        mOctoInterceptor.setHost(dbEntity.getHost());
        mOctoInterceptor.setPort(dbEntity.getPort());
        mOctoInterceptor.setApiKey(dbEntity.getApiKey());
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

    public Observable<PrinterDbEntity> transformAddPrinterEntity(final AddPrinterEntity addPrinterEntity) {
        return Observable.create(new Observable.OnSubscribe<PrinterDbEntity>() {
            @Override
            public void call(Subscriber<? super PrinterDbEntity> subscriber) {
                if (TextUtils.isEmpty(addPrinterEntity.ipAddress())) {
                    subscriber.onError(new IpAddressEmptyException());
                }

                String ipAddress = extractHost(addPrinterEntity.ipAddress());
                String accountName = validateName(ipAddress, addPrinterEntity.accountName());
                int port = convertPortStringToInt(addPrinterEntity.port(), addPrinterEntity.isSslChecked());
                String scheme = convertIsSslCheckedToScheme(addPrinterEntity.isSslChecked());

                PrinterDbEntity printerDbEntity = new PrinterDbEntity();
                printerDbEntity.setName(accountName);
                printerDbEntity.setApiKey(addPrinterEntity.apiKey());
                printerDbEntity.setScheme(scheme);
                printerDbEntity.setHost(ipAddress);
                printerDbEntity.setPort(port);

                if (isUrlValid(printerDbEntity)) {
                    subscriber.onNext(printerDbEntity);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new IncorrectAddPrinterFormattingException());
                }
            }
        });
    }

    private String extractHost(String ipAddress) {
        // If user inputted http:// or https:// try to extract only IP Address
        HttpUrl ipAddressUrl = HttpUrl.parse(ipAddress);
        if (ipAddressUrl != null) {
            return ipAddressUrl.host();
        }
        return ipAddress;
    }

    private String validateName(String ipAddress, String name) {
        if (TextUtils.isEmpty(name)) {
            return ipAddress;
        }
        return name;
    }

    private int convertPortStringToInt(String port, boolean isSslChecked) {
        int formattedPortNum;
        try {
            formattedPortNum = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            if (isSslChecked) {
                formattedPortNum = HTTPS_PORT;
            } else {
                formattedPortNum = HTTP_PORT;
            }
        }

        return formattedPortNum;
    }

    private String convertIsSslCheckedToScheme(boolean isSslChecked) {
        return isSslChecked ? HTTPS_SCHEME : HTTP_SCHEME;
    }

    private boolean isUrlValid(PrinterDbEntity printerDbEntity) {
        try {
            new HttpUrl.Builder()
                    .scheme(printerDbEntity.getScheme())
                    .host(printerDbEntity.getHost())
                    .port(printerDbEntity.getPort())
                    .build();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
