package com.nairbspace.octoandroid.ui.add_printer;

import com.nairbspace.octoandroid.domain.AddPrinter;
import com.nairbspace.octoandroid.domain.Printer;
import com.nairbspace.octoandroid.domain.Version;
import com.nairbspace.octoandroid.domain.interactor.GetVersion;
import com.nairbspace.octoandroid.domain.interactor.TransformAddPrinter;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.exception.ErrorMessageFactory;
import com.nairbspace.octoandroid.model.AddPrinterModel;
import com.nairbspace.octoandroid.model.mapper.AddPrinterModelDomainMapper;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class AddPrinterPresenter extends UseCasePresenter<AddPrinterScreen> {

    private AddPrinterScreen mScreen;
    private final AddPrinterModelDomainMapper mDomainMapper;
    private final TransformAddPrinter mTransformAddPrinterUseCase;
    private Subscription mTransformSubscription = Subscriptions.unsubscribed();
    private final GetVersion mGetVersionUseCase;

    @Inject
    public AddPrinterPresenter(TransformAddPrinter transformAddPrinterUseCase,
                               AddPrinterModelDomainMapper domainMapper,
                               GetVersion getVersionUseCase) {
        super(transformAddPrinterUseCase);
        mTransformAddPrinterUseCase = transformAddPrinterUseCase;
        mDomainMapper = domainMapper;
        mGetVersionUseCase = getVersionUseCase;
    }

    @Override
    protected void onInitialize(AddPrinterScreen addPrinterScreen) {
        mScreen = addPrinterScreen;
    }

    public void onAddPrinterClicked(final AddPrinterModel addPrinterModel) {
        mScreen.showProgress(true);
        mTransformSubscription = Observable.create(new Observable.OnSubscribe<AddPrinter>() {
            @Override
            public void call(Subscriber<? super AddPrinter> subscriber) {
                try {
                    AddPrinter addPrinter = mDomainMapper.transform(addPrinterModel);
                    subscriber.onNext(addPrinter);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<AddPrinter>() {
                    @Override
                    public void call(AddPrinter addPrinter) {
                        mTransformAddPrinterUseCase.setAddPrinter(addPrinter);
                        mTransformAddPrinterUseCase.execute(new AddPrinterSubscriber());
                    }
                });
    }

    @Override
    protected void onDestroy(AddPrinterScreen addPrinterScreen) {
        super.onDestroy(addPrinterScreen);
        if (!mTransformSubscription.isUnsubscribed()) {
            mTransformSubscription.unsubscribe();
        }
        mGetVersionUseCase.unsubscribe();
    }

    protected final class AddPrinterSubscriber extends DefaultSubscriber<Printer> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            mScreen.showProgress(false);
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

    protected final class GetVersionSubscriber extends DefaultSubscriber<Version> {
        @Override
        public void onCompleted() {
            mScreen.showProgress(false);
            mScreen.showIpAddressError("Success!!!!");
        }

        @Override
        public void onError(Throwable e) {
            mScreen.showProgress(false);
            e.printStackTrace();
        }

        @Override
        public void onNext(Version version) {

        }
    }

}
