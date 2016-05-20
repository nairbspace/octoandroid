package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.CurrentHistory;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.WebsocketModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

@SuppressWarnings("ConstantConditions")
public class WebsocketModelMapper extends MapperUseCase<Websocket, WebsocketModel> {

    private final UglyNullChecker mUglyNullChecker;
    private final ByteConverter mByteConverter;
    private final DateTimeConverter mDateTimeConverter;

    @Inject
    public WebsocketModelMapper(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                UglyNullChecker uglyNullChecker,
                                ByteConverter byteConverter,
                                DateTimeConverter dateTimeConverter) {
        super(threadExecutor, postExecutionThread);
        mUglyNullChecker = uglyNullChecker;
        mByteConverter = byteConverter;
        mDateTimeConverter = dateTimeConverter;
    }

    @Override
    protected Observable<WebsocketModel> buildUseCaseObservable(final Websocket websocket) {
        return Observable.create(new Observable.OnSubscribe<WebsocketModel>() {
            @Override
            public void call(Subscriber<? super WebsocketModel> subscriber) {
                try {
                    if (mUglyNullChecker.isCurrentNotNull(websocket)) {
                        WebsocketModel websocketModel = mapToStatusModel(websocket, websocket.current());
                        subscriber.onNext(websocketModel);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException(e));
                }
            }
        });
    }

    private WebsocketModel mapToStatusModel(Websocket websocket, CurrentHistory current) {
        String state = "";
        if (mUglyNullChecker.ifStateTextNotNull(websocket)) {
            state = current.state().text();
        }

        String file = "";
        if (mUglyNullChecker.isFileNameNotNull(websocket)) {
            file = current.job().file().name();
        }

        String approxTotalPrintTime = "";
        if (mUglyNullChecker.isApproxTotalPrintTimeIsNotNull(websocket)) {
            Double time = current.job().estimatedPrintTime();
            approxTotalPrintTime = mDateTimeConverter.secondsToHHmmss(time);
        }

        String printTime = "";
        if (mUglyNullChecker.isPrintTimeIsNotNull(websocket)) {
            printTime = mDateTimeConverter.secondsToHHmmss(current.progress().printTime());
        }

        String printTimeLeft = "";
        if (mUglyNullChecker.isPrintTimeLeftIsNotNull(websocket)) {
            if (current.progress().printTimeLeft() > 0) {
                printTimeLeft = mDateTimeConverter.secondsToHHmmss(current.progress().printTimeLeft());
            }
        }

        String printedBytes = "";
        if (mUglyNullChecker.isPrintedBytesIsNotNull(websocket)) {
            printedBytes = mByteConverter.toReadableString(current.progress().filepos());
        }

        String printedFileSize = "";
        if (mUglyNullChecker.isPrintedFileSizeIsNotNull(websocket)) {
            printedFileSize = mByteConverter.toReadableString(current.job().file().size());
        }

        int completionProgress = 0;
        if (mUglyNullChecker.isCompletionNotNull(websocket)) {
            completionProgress = formatCompletion(current.progress().completion());
        }

        boolean operational = false;
        boolean paused = false;
        boolean printing = false;
        boolean pausedOrPrinting = false;
        boolean sdReady = false;
        boolean error = false;
        boolean ready = false;
        boolean closedorError = false;
        if (mUglyNullChecker.isStateFlagsNotNull(websocket)) {
            CurrentHistory.State.Flags flags = current.state().flags();
            operational = flags.operational();
            paused = flags.paused();
            printing = flags.printing();
            pausedOrPrinting = flags.paused() || flags.printing();
            sdReady = flags.sdReady();
            error = flags.error();
            ready = flags.ready();
            closedorError = flags.closedOrError();
        }

        boolean fileLoaded = mUglyNullChecker.isFileNameNotNull(websocket);

        return WebsocketModel.builder()
                .state(state)
                .file(file)
                .approxTotalPrintTime(approxTotalPrintTime)
                .printTime(printTime)
                .printTimeLeft(printTimeLeft)
                .printedBytes(printedBytes)
                .printedFileSize(printedFileSize)
                .completionProgress(completionProgress)
                .operational(operational)
                .paused(paused)
                .printing(printing)
                .pausedOrPrinting(pausedOrPrinting)
                .sdReady(sdReady)
                .error(error)
                .ready(ready)
                .closedOrError(closedorError)
                .fileLoaded(fileLoaded)
                .build();
    }

    public int formatCompletion(Double completion) {
        int completionProgress = completion.intValue();
        return Math.abs(completionProgress);
    }
}
