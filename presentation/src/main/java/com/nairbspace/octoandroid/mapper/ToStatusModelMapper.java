package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.CurrentHistory;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.StatusModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

@SuppressWarnings("ConstantConditions")
public class ToStatusModelMapper extends MapperUseCase<Websocket, StatusModel> {

    private final UglyNullChecker mUglyNullChecker;
    private final ByteConverter mByteConverter;
    private final DateTimeConverter mDateTimeConverter;

    @Inject
    public ToStatusModelMapper(ThreadExecutor threadExecutor,
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
    protected Observable<StatusModel> buildUseCaseObservable(final Websocket websocket) {
        return Observable.create(new Observable.OnSubscribe<StatusModel>() {
            @Override
            public void call(Subscriber<? super StatusModel> subscriber) {
                try {
                    if (mUglyNullChecker.isCurrentNotNull(websocket)) {
                        StatusModel statusModel = mapToStatusModel(websocket, websocket.current());
                        subscriber.onNext(statusModel);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException(e));
                }
            }
        });
    }

    private StatusModel mapToStatusModel(Websocket websocket, CurrentHistory current) {
        String state = "-";
        if (mUglyNullChecker.ifStateTextNotNull(websocket)) {
            state = current.state().text();
        }

        String file = "-";
        if (mUglyNullChecker.isFileNameNotNull(websocket)) {
            file = current.job().file().name();
        }

        String approxTotalPrintTime = "-";
        if (mUglyNullChecker.isApproxTotalPrintTimeIsNotNull(websocket)) {
            Double time = current.job().estimatedPrintTime();
            approxTotalPrintTime = mDateTimeConverter.secondsToHHmmss(time);
        }

        String printTime = "-";
        if (mUglyNullChecker.isPrintTimeIsNotNull(websocket)) {
            printTime = mDateTimeConverter.secondsToHHmmss(current.progress().printTime());
        }

        String printTimeLeft = "-";
        if (mUglyNullChecker.isPrintTimeLeftIsNotNull(websocket)) {
            if (current.progress().printTimeLeft() > 0) {
                printTimeLeft = mDateTimeConverter.secondsToHHmmss(current.progress().printTimeLeft());
            }
        }

        String printedBytes = "-";
        if (mUglyNullChecker.isPrintedBytesIsNotNull(websocket)) {
            printedBytes = mByteConverter.toReadableString(current.progress().filepos());
        }

        String printedFileSize = "-";
        if (mUglyNullChecker.isPrintedFileSizeIsNotNull(websocket)) {
            printedFileSize = mByteConverter.toReadableString(current.job().file().size());
        }

        int completionProgress = 0;
        if (mUglyNullChecker.isProgressNotNull(websocket)) {
            completionProgress = formatCompletion(current.progress().completion());
        }

        return StatusModel.builder()
                .state(state)
                .file(file)
                .approxTotalPrintTime(approxTotalPrintTime)
                .printTime(printTime)
                .printTimeLeft(printTimeLeft)
                .printedBytes(printedBytes)
                .printedFileSize(printedFileSize)
                .completionProgress(completionProgress)
                .build();
    }

    public int formatCompletion(Double completion) {
        int completionProgress = completion.intValue();
        return Math.abs(completionProgress);
    }
}
