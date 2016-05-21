package com.nairbspace.octoandroid.ui.add_printer;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.domain.interactor.AddPrinterDetails;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.mapper.AddPrinterModelMapper;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

public class AddPrinterPresenter extends UseCasePresenter<AddPrinterScreen> {

    private AddPrinterScreen mScreen;
    private final AddPrinterDetails mAddPrinterDetails;
    private final AddPrinterModelMapper mAddPrinterModelMapper;

    @Inject
    public AddPrinterPresenter(AddPrinterDetails addPrinterDetails,
                               AddPrinterModelMapper addPrinterModelMapper) {
        super(addPrinterDetails);
        mAddPrinterDetails = addPrinterDetails;
        mAddPrinterModelMapper = addPrinterModelMapper;
    }

    @Override
    protected void onInitialize(AddPrinterScreen addPrinterScreen) {
        mScreen = addPrinterScreen;
    }

    public void onAddPrinterClicked(final AddPrinterModel addPrinterModel) {
        mAddPrinterModelMapper.execute(new TransformSubscriber(), addPrinterModel);
    }

    private void showLoading(boolean shouldShow) {
        if (shouldShow) {
            mScreen.hideSoftKeyboard(true);
            mScreen.showProgress(true);
        } else {
            mScreen.showProgress(false);
        }
    }

    @RxLogSubscriber
    private final class TransformSubscriber extends DefaultSubscriber<AddPrinter> {
        @Override
        public void onNext(AddPrinter addPrinter) {
            showLoading(true);
            mAddPrinterDetails.execute(new AddPrinterSubscriber(), addPrinter);
        }
    }

    @RxLogSubscriber
    private final class AddPrinterSubscriber extends DefaultSubscriber<Boolean> {

        @Override
        public void onCompleted() {
            showLoading(false);
        }

        @Override
        public void onError(Throwable e) {
            showLoading(false);
            e.printStackTrace();
            Exception ex = (Exception) e;

            String errorMessage = ErrorMessageFactory.create(mScreen.context(), ex);
            if (ErrorMessageFactory.isIpAddressError(ex)) {
                mScreen.showIpAddressError(errorMessage);
            } else if (ErrorMessageFactory.ifSslError(mScreen.context(), e.getMessage())) {
                mScreen.showAlertDialog();
            } else {
                mScreen.showSnackbar(errorMessage);
            }
        }

        @Override
        public void onNext(Boolean aBoolean) {
            if (aBoolean) {
                mScreen.navigateToPreviousScreen();
            }
        }
    }
}
