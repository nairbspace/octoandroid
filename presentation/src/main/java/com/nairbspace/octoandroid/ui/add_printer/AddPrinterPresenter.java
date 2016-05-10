package com.nairbspace.octoandroid.ui.add_printer;

import com.nairbspace.octoandroid.domain.pojo.AddPrinter;
import com.nairbspace.octoandroid.domain.pojo.Printer;
import com.nairbspace.octoandroid.domain.pojo.Version;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetVersion;
import com.nairbspace.octoandroid.domain.interactor.TransformAddPrinter;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.model.mapper.ModelMapper;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

import rx.functions.Action1;

public class AddPrinterPresenter extends UseCasePresenter<AddPrinterScreen> {

    private AddPrinterScreen mScreen;
    private final ModelMapper mModelMapper;
    private final TransformAddPrinter mTransformAddPrinterUseCase;
    private final GetVersion mGetVersionUseCase;

    @Inject
    public AddPrinterPresenter(TransformAddPrinter transformAddPrinterUseCase,
                               ModelMapper modelMapper,
                               GetVersion getVersionUseCase) {
        super(transformAddPrinterUseCase);
        mTransformAddPrinterUseCase = transformAddPrinterUseCase;
        mModelMapper = modelMapper;
        mGetVersionUseCase = getVersionUseCase;
    }

    @Override
    protected void onInitialize(AddPrinterScreen addPrinterScreen) {
        mScreen = addPrinterScreen;
    }

    public void onAddPrinterClicked(final AddPrinterModel addPrinterModel) {
        mModelMapper.transformObservable(addPrinterModel).subscribe(new Action1<AddPrinter>() {
            @Override
            public void call(AddPrinter addPrinter) {
                showLoading(true);
                mTransformAddPrinterUseCase.setAddPrinter(addPrinter);
                mTransformAddPrinterUseCase.execute(new AddPrinterSubscriber());
            }
        });
    }

    @Override
    protected void onDestroy(AddPrinterScreen addPrinterScreen) {
        super.onDestroy(addPrinterScreen);
        mGetVersionUseCase.unsubscribe(); // super class only able to do unsubscribe from one subscription.
    }

    private void showLoading(boolean shouldShow) {
        if (shouldShow) {
            mScreen.hideSoftKeyboard(true);
            mScreen.showProgress(true);
        } else {
            mScreen.showProgress(false);
        }
    }

    private final class AddPrinterSubscriber extends DefaultSubscriber<Printer> {

        @Override
        public void onError(Throwable e) {
            showLoading(false);
            String errorMessage = ErrorMessageFactory
                    .createIpAddressError(mScreen.context(), (Exception) e);
            mScreen.showIpAddressError(errorMessage);
            e.printStackTrace();
        }

        @Override
        public void onNext(Printer printer) {
            mGetVersionUseCase.setPrinter(printer);
            mGetVersionUseCase.execute(new GetVersionSubscriber());
        }
    }

    private final class GetVersionSubscriber extends DefaultSubscriber<Version> {
        @Override
        public void onCompleted() {
            showLoading(false);
            mScreen.navigateToPreviousScreen();
        }

        @Override
        public void onError(Throwable e) {
            mScreen.showProgress(false);
            e.printStackTrace();
            String message = ErrorMessageFactory.createGetVersionError(mScreen.context(), e.getMessage());
            if (ErrorMessageFactory.ifSslError(mScreen.context(), e.getMessage())) {
                String alertTitle = ErrorMessageFactory.getSslTitle(mScreen.context());
                mScreen.showAlertDialog(alertTitle, message);
            } else {
                mScreen.showSnackbar(message);
            }
        }
    }
}
