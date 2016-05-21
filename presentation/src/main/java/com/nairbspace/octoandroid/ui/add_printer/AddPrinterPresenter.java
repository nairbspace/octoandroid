package com.nairbspace.octoandroid.ui.add_printer;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.domain.interactor.VerifyPrinterDetails;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.AddPrinterDetails;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.mapper.AddPrinterModelMapper;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

public class AddPrinterPresenter extends UseCasePresenter<AddPrinterScreen> {

    private final VerifyPrinterDetails mVerifyPrinterDetails;
    private final AddPrinterModelMapper mAddPrinterModelMapper;
    private final AddPrinterDetails mAddPrinterDetails;
    private AddPrinterScreen mScreen;

    @Inject
    public AddPrinterPresenter(VerifyPrinterDetails verifyPrinterDetails,
                               AddPrinterModelMapper addPrinterModelMapper,
                               AddPrinterDetails addPrinterDetails) {
        super(verifyPrinterDetails);
        mVerifyPrinterDetails = verifyPrinterDetails;
        mAddPrinterModelMapper = addPrinterModelMapper;
        mAddPrinterDetails = addPrinterDetails;
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

    private final class AddPrinterSubscriber extends DefaultSubscriber {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            showLoading(false);
            Exception ex = (Exception) e;

            String errorMessage = ErrorMessageFactory.create(mScreen.context(), ex);
            if (ErrorMessageFactory.isIpAddressError(ex)) {
                mScreen.showIpAddressError(errorMessage);
            } else {
                mScreen.showSnackbar(errorMessage);
            }
        }

        @Override
        public void onCompleted() {
            mVerifyPrinterDetails.execute(new VerifyPrinterSubscriber());
        }
    }


    @RxLogSubscriber
    private final class VerifyPrinterSubscriber extends DefaultSubscriber {

        @Override
        public void onCompleted() {
            showLoading(false);
            mScreen.navigateToPreviousScreen();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            showLoading(false);
            Exception ex = (Exception) e;

            String errorMessage = ErrorMessageFactory.create(mScreen.context(), ex);
            if (ErrorMessageFactory.ifSslError(mScreen.context(), e.getMessage())) {
                mScreen.showAlertDialog();
            } else {
                mScreen.showSnackbar(errorMessage);
            }
        }
    }
}
