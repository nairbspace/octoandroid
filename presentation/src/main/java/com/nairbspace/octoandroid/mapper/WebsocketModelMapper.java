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

        String tempTime = "";
        float actualTempBed = 0f;
        float targetTempBed = 0f;
        float actualTempTool0 = 0f;
        float targetTempTool0 = 0f;
        float actualTempTool1 = 0f;
        float targetTempTool1 = 0f;
        if (mUglyNullChecker.isTempNotNull(websocket)) {
            CurrentHistory.Temps temp = websocket.current().temps().get(0);
            if (mUglyNullChecker.isTempTimeNotNull(websocket)) {
                long time = temp.time();
                tempTime = mDateTimeConverter.secondsToShortTimeString(time); // TODO format to MM:ss
            }

            if (mUglyNullChecker.isActualTempBedNotNull(websocket)) {
                actualTempBed = temp.bed().actual().floatValue();
            }

            if (mUglyNullChecker.isTargetTempBedNotNull(websocket)) {
                targetTempBed = temp.bed().target().floatValue();
            }

            if (mUglyNullChecker.isActualTempTool0NotNull(websocket)) {
                actualTempTool0 = temp.tool0().actual().floatValue();
            }

            if (mUglyNullChecker.isTargetTempTool0NotNull(websocket)) {
                targetTempTool0 = temp.tool0().target().floatValue();
            }

            if (mUglyNullChecker.isActualTempTool1NotNull(websocket)) {
                actualTempTool1 = temp.tool1().actual().floatValue();
            }

            if (mUglyNullChecker.isTargetTempTool1NotNull(websocket)) {
                targetTempTool1 = temp.tool1().target().floatValue();
            }
        }

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

                .tempTime(tempTime)
                .actualTempBed(actualTempBed)
                .targetTempBed(targetTempBed)
                .actualTempTool0(actualTempTool0)
                .targetTempTool0(targetTempTool0)
                .actualTempTool1(actualTempTool1)
                .targetTempTool1(targetTempTool1)
                .build();
    }

    public int formatCompletion(Double completion) {
        int completionProgress = completion.intValue();
        return Math.abs(completionProgress);
    }
}
