package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.SlicingProgress;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.SlicingProgressModel;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class SlicingProgressModelMapper extends MapperUseCase<SlicingProgress, SlicingProgressModel> {

    @Inject
    public SlicingProgressModelMapper(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<SlicingProgressModel> buildUseCaseObservableInput(final SlicingProgress slicingProgress) {
        return Observable.create(new Observable.OnSubscribe<SlicingProgressModel>() {
            @Override
            public void call(Subscriber<? super SlicingProgressModel> subscriber) {
                try {
                    if (slicingProgress != null) {
                        subscriber.onNext(mapToModel(slicingProgress));
                        subscriber.onCompleted();
                    } else {
                        subscriber.unsubscribe();
                    }
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException(e));
                }
            }
        });
    }

    private SlicingProgressModel mapToModel(SlicingProgress slicingProgress) {

        String slicer = slicingProgress.slicer();
        if (slicer == null) slicer = "";

        String sourceLocation = slicingProgress.sourceLocation();
        if (sourceLocation == null) sourceLocation = "";

        String sourcePath = slicingProgress.sourcePath();
        if (sourcePath == null) sourcePath = "";

        String destLocation = slicingProgress.destLocation();
        if (destLocation == null) destLocation = "";

        String destPath = slicingProgress.destPath();
        if (destPath == null) destPath = "";

        int progress = 0;
        Double progressInput = slicingProgress.progress();
        if (progressInput != null) progress = progressInput.intValue();

        return SlicingProgressModel.builder()
                .slicer(slicer)
                .sourceLocation(sourceLocation)
                .sourcePath(sourcePath)
                .destLocation(destLocation)
                .destPath(destPath)
                .progress(progress)
                .build();
    }
}
