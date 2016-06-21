package com.nairbspace.octoandroid.mapper;

import com.google.gson.reflect.TypeToken;
import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Slicer;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.SlicerModel;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class SlicerModelMapper extends MapperUseCase<Map<String, Slicer>, Map<String, SlicerModel>> {

    private final ModelSerializer mSerializer;

    @Inject
    public SlicerModelMapper(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ModelSerializer serializer) {
        super(threadExecutor, postExecutionThread);
        mSerializer = serializer;
    }

    @Override
    protected Observable<Map<String, SlicerModel>> buildUseCaseObservable(final Map<String, Slicer> slicerMap) {
        return Observable.create(new Observable.OnSubscribe<Map<String, SlicerModel>>() {
            @Override
            public void call(Subscriber<? super Map<String, SlicerModel>> subscriber) {
                try {
                    // TODO null check slicer models across modules!
                    if (slicerMap == null) subscriber.onError(new TransformErrorException());
                    else {
                        subscriber.onNext(mapToSlicerModelMap(slicerMap));
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private Map<String, SlicerModel> mapToSlicerModelMap(Map<String, Slicer> slicerMap) {
        Type type = new TypeToken<Map<String, SlicerModel>>() {}.getType();
        return mSerializer.transform(slicerMap, type);
    }
}
