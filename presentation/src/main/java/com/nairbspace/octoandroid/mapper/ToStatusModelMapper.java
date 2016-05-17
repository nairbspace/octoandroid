package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Websocket;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.StatusModel;

import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class ToStatusModelMapper extends MapperUseCase<Websocket, StatusModel> {

    @Inject
    public ToStatusModelMapper(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<StatusModel> buildUseCaseObservable(final Websocket websocket) {
        return Observable.create(new Observable.OnSubscribe<StatusModel>() {
            @Override
            public void call(Subscriber<? super StatusModel> subscriber) {
                try {
                    StatusModel statusModel = mapToStatusModel(websocket);
                    subscriber.onNext(statusModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException(e));
                }
            }
        });
    }

    private StatusModel mapToStatusModel(Websocket websocket) {
        String state = "-";
        if (ifStateTextNotNull(websocket)) {
            state = websocket.current().state().text();
        }

        String file = "-";
        if (isFileNameNotNull(websocket)) {
            file = websocket.current().job().file().name();
        }

        String approxTotalPrintTime = "-";
        if (isApproxTotalPrintTimeIsNotNull(websocket)) {
            approxTotalPrintTime = formatTimeInterval(websocket.current().job().estimatedPrintTime());
        }

        String printTime = "-";
        if (isPrintTimeIsNotNull(websocket)) {
            printTime = formatTimeInterval(websocket.current().progress().printTime());
        }

        String printTimeLeft = "-";
        if (isPrintTimeLeftIsNotNull(websocket)) {
            if (websocket.current().progress().printTimeLeft() > 0) {
                printTimeLeft = formatTimeInterval(websocket.current().progress().printTimeLeft());
            }
        }

        String printedBytes = "-";
        if (isPrintedBytesIsNotNull(websocket)) {
            printedBytes = humanReadableByteCount(websocket.current().progress().filepos(), true);
        }

        String printedFileSize = "-";
        if (isPrintedFileSizeIsNotNull(websocket)) {
            printedFileSize = humanReadableByteCount(websocket.current().job().file().size(), true);
        }

        int completionProgress = 0;
        if (isProgressNotNull(websocket)) {
            completionProgress = formatCompletion(websocket.current().progress().completion());
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

    // TODO fix ugly null checker!!!!!!!!!!!
    private boolean isWebSocketNotNull(Websocket websocket) {
        return websocket != null;
    }

    private boolean isCurrentNotNull(Websocket websocket) {
        return isWebSocketNotNull(websocket) && websocket.current() != null;
    }

    private boolean isStateNotNull(Websocket websocket) {
        return isCurrentNotNull(websocket) && websocket.current().state() != null;
    }

    private boolean isJobNotNull(Websocket websocket) {
        return isCurrentNotNull(websocket) && websocket.current().job() != null;
    }

    private boolean ifStateTextNotNull(Websocket websocket) {
        return isStateNotNull(websocket) && websocket.current().state().text() != null;
    }

    private boolean isFileNotNull(Websocket websocket) {
        return isJobNotNull(websocket) && websocket.current().job().file() != null;
    }

    private boolean isFileNameNotNull(Websocket websocket) {
        return isFileNotNull(websocket) && websocket.current().job().file().name() != null;
    }

    private boolean isApproxTotalPrintTimeIsNotNull(Websocket websocket) {
        return isJobNotNull(websocket) && websocket.current().job().estimatedPrintTime() != null;
    }

    private boolean isProgressNotNull(Websocket websocket) {
        return isCurrentNotNull(websocket) && websocket.current().progress() != null;
    }

    private boolean isPrintTimeIsNotNull(Websocket websocket) {
        return isProgressNotNull(websocket) && websocket.current().progress().printTime() != null;
    }

    private boolean isPrintTimeLeftIsNotNull(Websocket websocket) {
        return isProgressNotNull(websocket) && websocket.current().progress().printTimeLeft() != null;
    }

    private boolean isPrintedBytesIsNotNull(Websocket websocket) {
        return isProgressNotNull(websocket) && websocket.current().progress().filepos() != null;
    }

    private boolean isPrintedFileSizeIsNotNull(Websocket websocket) {
        return isFileNotNull(websocket) && websocket.current().job().file().size() != null;
    }

    public String formatTimeInterval(long estimatedSeconds) {
        long hours = estimatedSeconds / 3600;
        long secondsLeft = estimatedSeconds - hours * 3600;
        long minutes = secondsLeft / 60;
        long seconds = secondsLeft - minutes * 60;

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
    }

    private String humanReadableByteCount(long bytes, boolean inSiUnits) {
        int unit = inSiUnits ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exponent = (int) (Math.log(bytes) / Math.log(unit));
        String prefix = (inSiUnits ? "kMGTPE" : "KMGTPE").charAt(exponent-1) + (inSiUnits ? "" : "i");
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit, exponent), prefix);
    }

    public int formatCompletion(Float completion) {
        int completionProgress = completion.intValue();
        return Math.abs(completionProgress);
    }
}
