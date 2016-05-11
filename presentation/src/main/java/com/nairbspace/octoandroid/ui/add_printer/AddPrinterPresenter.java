package com.nairbspace.octoandroid.ui.add_printer;

import com.nairbspace.octoandroid.domain.interactor.AddPrinterDetails;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.model.mapper.ModelMapper;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

import rx.functions.Action1;

public class AddPrinterPresenter extends UseCasePresenter<AddPrinterScreen> {

    private AddPrinterScreen mScreen;
    private final ModelMapper mModelMapper;
    private final AddPrinterDetails mAddPrinterDetails;

    @Inject
    public AddPrinterPresenter(AddPrinterDetails addPrinterDetails,
                               ModelMapper modelMapper) {
        super(addPrinterDetails);
        mAddPrinterDetails = addPrinterDetails;
        mModelMapper = modelMapper;
    }

    @Override
    protected void onInitialize(AddPrinterScreen addPrinterScreen) {
        mScreen = addPrinterScreen;
    }

    public void onAddPrinterClicked(final AddPrinterModel addPrinterModel) {
        mModelMapper.transformObs(addPrinterModel).subscribe(mAddPrinterAction);
    }

    private Action1<AddPrinter> mAddPrinterAction = new Action1<AddPrinter>() {
        @Override
        public void call(AddPrinter addPrinter) {
            showLoading(true);
            mAddPrinterDetails.setAddPrinter(addPrinter);
            mAddPrinterDetails.execute(new AddPrinterSubscriber());
        }
    };

    @Override
    protected void onDestroy(AddPrinterScreen addPrinterScreen) {
        super.onDestroy(addPrinterScreen);
    }

    private void showLoading(boolean shouldShow) {
        if (shouldShow) {
            mScreen.hideSoftKeyboard(true);
            mScreen.showProgress(true);
        } else {
            mScreen.showProgress(false);
        }
    }

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

            String errorMessage = ErrorMessageFactory.createGetVersionError(mScreen.context(), e.getMessage());
            if (ErrorMessageFactory.isIpAddressError(ex)) {
                errorMessage = ErrorMessageFactory.createIpAddressError(mScreen.context(), (Exception) e);
                mScreen.showIpAddressError(errorMessage);
            } else if (ErrorMessageFactory.ifSslError(mScreen.context(), e.getMessage())) {
                String alertTitle = ErrorMessageFactory.getSslTitle(mScreen.context());
                mScreen.showAlertDialog(alertTitle, errorMessage);
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
