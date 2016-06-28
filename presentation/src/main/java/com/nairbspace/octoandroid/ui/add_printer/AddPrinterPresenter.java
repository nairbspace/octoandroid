package com.nairbspace.octoandroid.ui.add_printer;

import com.nairbspace.octoandroid.domain.interactor.AddPrinterDetails;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.mapper.AddPrinterModelMapper;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.ui.templates.UseCasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

public class AddPrinterPresenter extends UseCasePresenter<AddPrinterScreen> {

    private final AddPrinterModelMapper mAddPrinterModelMapper;
    private final AddPrinterDetails mAddPrinterDetails;
    private AddPrinterScreen mScreen;

    @Inject
    public AddPrinterPresenter(AddPrinterModelMapper addPrinterModelMapper,
                               AddPrinterDetails addPrinterDetails) {
        super(addPrinterModelMapper, addPrinterDetails);
        mAddPrinterModelMapper = addPrinterModelMapper;
        mAddPrinterDetails = addPrinterDetails;
    }

    @Override
    protected void onInitialize(AddPrinterScreen addPrinterScreen) {
        mScreen = addPrinterScreen;
    }

    public void onAddPrinterClicked(final AddPrinterModel addPrinterModel) {
        showLoading(true);
        mAddPrinterModelMapper.execute(new TransformSubscriber(), addPrinterModel);
    }

    private void showLoading(boolean shouldShow) {
        if (shouldShow) {
            mScreen.hideSoftKeyboard();
            mScreen.showProgress(true);
        } else {
            mScreen.showProgress(false);
        }
    }

    private final class TransformSubscriber extends DefaultSubscriber<AddPrinter> {

        @Override
        public void onError(Throwable e) {
            showError(e);
        }

        @Override
        public void onNext(AddPrinter addPrinter) {
            mAddPrinterDetails.execute(new AddPrinterSubscriber(), addPrinter);
        }
    }

    private final class AddPrinterSubscriber extends DefaultSubscriber {

        @Override
        public void onError(Throwable e) {
            showError(e);
        }

        @Override
        public void onCompleted() {
            showLoading(false);
            mScreen.navigateToPreviousScreen();
        }
    }

    private void showError(Throwable t) {
        Timber.e(t, null);
        showLoading(false);
        Exception e = (Exception) t;
        String errorMessage = ErrorMessageFactory.create(mScreen.context(), e);
        if (ErrorMessageFactory.ifSslError(mScreen.context(), e)) {
            mScreen.showAlertDialog();
        } else if (ErrorMessageFactory.isIpAddressError(e)) {
            mScreen.showIpAddressError(errorMessage);
        } else {
            mScreen.showSnackbar(errorMessage);
        }
    }
}
