package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.model.Printer;
import com.nairbspace.octoandroid.model.PrinterDao;
import com.nairbspace.octoandroid.model.Version;
import com.nairbspace.octoandroid.model.VersionDao;
import com.nairbspace.octoandroid.net.OctoApiImpl;
import com.nairbspace.octoandroid.net.OctoInterceptor;

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
    @Inject PrinterDao mPrinterDao;
    @Inject VersionDao mVersionDao;

    @Inject
    public GetPrinterImpl() {

    }

    @Override
    public void getVersion(final Printer printer, final GetPrinterFinishedListener listener) {
        listener.onLoading();
        mInterceptor.setInterceptor(printer.getScheme(), printer.getHost(),
                printer.getPort(), printer.getApi_key());
        mApi.getVersionObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Version>() {
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
                    public void onNext(Version version) {
                        addPrinterToDb(printer);
                        addVersionToDb(printer, version);
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
    public boolean isUrlValid(Printer printer) {
        try {
            new HttpUrl.Builder()
                    .scheme(printer.getScheme())
                    .host(printer.getHost())
                    .port(printer.getPort())
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
    public Printer setPrinter(Printer printer, String accountName, String apiKey,
                              String scheme, String ipAddress, int portNumber) {
        printer.setName(accountName);
        printer.setApi_key(apiKey);
        printer.setScheme(scheme);
        printer.setHost(ipAddress);
        printer.setPort(portNumber);
        return printer;
    }

    @Override
    public void addPrinterToDb(Printer printer) {
        deleteOldPrintersInDb(printer);
        mPrinterDao.insertOrReplace(printer);
    }

    @Override
    public void deleteOldPrintersInDb(Printer printer) {

        Printer oldPrinter;
        try {
            oldPrinter = mPrinterDao.queryBuilder()
                    .where(PrinterDao.Properties.Name.eq(printer.getName()))
                    .unique();
        } catch (DaoException e) {
            oldPrinter = null;
        }

        if (oldPrinter != null) {
            mVersionDao.delete(oldPrinter.getVersion());
            mPrinterDao.delete(oldPrinter);
        }
    }

    @Override
    public void addVersionToDb(Printer printer, Version version) {
        mVersionDao.insertOrReplace(version);

        printer.setVersion(version);
        mPrinterDao.update(printer);
    }
}