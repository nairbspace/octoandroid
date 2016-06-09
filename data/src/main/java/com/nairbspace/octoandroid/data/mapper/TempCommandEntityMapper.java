package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.TempCommandEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.TempCommand;

import rx.Observable;
import rx.Subscriber;

public class TempCommandEntityMapper {

    public static Observable.OnSubscribe<Object> mapToTempCommandEntity(final TempCommand tempCommand) {
        return new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                switch (tempCommand.toolBedOffsetTemp()) {
                    case TARGET_TOOL0:
                        subscriber.onNext(TempCommandEntity.TargetTool0.create(tempCommand.temp()));
                        break;
                    case OFFSET_TOOL0:
                        subscriber.onNext(TempCommandEntity.OffsetTool0.create(tempCommand.temp()));
                        break;
                    case TARGET_TOOL1:
                        subscriber.onNext(TempCommandEntity.TargetTool1.create(tempCommand.temp()));
                        break;
                    case OFFSET_TOOL1:
                        subscriber.onNext(TempCommandEntity.OffsetTool1.create(tempCommand.temp()));
                        break;
                    case TARGET_BED:
                        subscriber.onNext(TempCommandEntity.TargetBed.create(tempCommand.temp()));
                        break;
                    case OFFSET_BED:
                        subscriber.onNext(TempCommandEntity.OffsetBed.create(tempCommand.temp()));
                        break;
                    default:
                        subscriber.onError(new EntityMapperException());
                }
                subscriber.onCompleted();
            }
        };
    }
}
