package com.nairbspace.octoandroid.interactor;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.db.PrinterDbEntityDao;
import com.nairbspace.octoandroid.data.pref.PrefManager;
import com.nairbspace.octoandroid.data.net.rest.OctoApiImpl;
import com.nairbspace.octoandroid.data.net.rest.OctoInterceptor;
import com.nairbspace.octoandroid.data.entity.VersionEntity;

import javax.inject.Inject;

import de.greenrobot.dao.DaoException;
import okhttp3.HttpUrl;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetPrinterImpl implements GetPrinter {

    private static final String HTTP_SCHEME = "http";
    private static final String HTTPS_SCHEME = "https";

    @Inject OctoApiImpl mApi;
    @Inject OctoInterceptor mInterceptor;
    @Inject
    PrinterDbEntityDao mPrinterDbEntityDao;
    @Inject Gson mGson;
    @Inject PrefManager mPrefManager;

    @Inject
    public GetPrinterImpl() {

    }

    @Override
    public void getVersion(final PrinterDbEntity printerDbEntity, final GetPrinterFinishedListener listener) {
        listener.onLoading();
        mInterceptor.setInterceptor(printerDbEntity.getScheme(), printerDbEntity.getHost(),
                printerDbEntity.getPort(), printerDbEntity.getApiKey());
        mApi.getVersionObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VersionEntity>() {
                    @Override
                    public void onCompleted() {
                        listener.onComplete();
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onComplete();
                        if (e.getMessage().contains("Trust anchor for certification path not found.")) {
                            listener.onSslFailure();
                        } else if (e.getMessage().contains("Invalid API key")) {
                            listener.onApiKeyFailure();
                        } else {
                            e.printStackTrace();
                            listener.onFailure();
                        }
                    }

                    @Override
                    public void onNext(VersionEntity versionEntity) {
                        addPrinterToDb(printerDbEntity);
                        addVersionToDb(printerDbEntity, versionEntity);
                    }
                });
    }

    @Override
    public String extractHost(String ipAddress) {
        // If user inputted http:// or https:// try to extract only IP Address
        HttpUrl ipAddressUrl = HttpUrl.parse(ipAddress);
        if (ipAddressUrl != null) {
            return ipAddressUrl.host();
        }
        return ipAddress;
    }

    @Override
    public boolean isUrlValid(PrinterDbEntity printerDbEntity) {
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

    @Override
    public int convertPortStringToInt(String port, boolean isSslChecked) {
        int formattedPortNum;
        try {
            formattedPortNum = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            if (isSslChecked) {
                formattedPortNum = 443;
            } else {
                formattedPortNum = 80;
            }
        }

        return formattedPortNum;
    }

    @Override
    public String convertIsSslCheckedToScheme(boolean isSslChecked) {
        return isSslChecked ? HTTPS_SCHEME : HTTP_SCHEME;
    }

    @Override
    public PrinterDbEntity setPrinter(PrinterDbEntity printerDbEntity, String accountName, String apiKey,
                                      String scheme, String ipAddress, int portNumber) {
        printerDbEntity.setName(accountName);
        printerDbEntity.setApiKey(apiKey);
        printerDbEntity.setScheme(scheme);
        printerDbEntity.setHost(ipAddress);
        printerDbEntity.setPort(portNumber);
        return printerDbEntity;
    }

    @Override
    public void addPrinterToDb(PrinterDbEntity printerDbEntity) {
        deleteOldPrintersInDb(printerDbEntity);
        mPrinterDbEntityDao.insertOrReplace(printerDbEntity);
    }

    @Override
    public void deleteOldPrintersInDb(PrinterDbEntity printerDbEntity) {

        PrinterDbEntity oldPrinterDbEntity;
        try {
            oldPrinterDbEntity = mPrinterDbEntityDao.queryBuilder()
                    .where(PrinterDbEntityDao.Properties.Name.eq(printerDbEntity.getName()))
                    .unique();
        } catch (DaoException e) {
            oldPrinterDbEntity = null;
        }

        if (oldPrinterDbEntity != null) {
            mPrinterDbEntityDao.delete(oldPrinterDbEntity);
        }
    }

    @Override
    public void addVersionToDb(PrinterDbEntity printerDbEntity, VersionEntity versionEntity) {
        String json = mGson.toJson(versionEntity);
        printerDbEntity.setVersionJson(json);
        mPrinterDbEntityDao.update(printerDbEntity);
        setActivePrinter(printerDbEntity.getId());
    }

    @Override
    public void setActivePrinter(long printerId) {
        mPrefManager.setActivePrinter(printerId);
    }
}