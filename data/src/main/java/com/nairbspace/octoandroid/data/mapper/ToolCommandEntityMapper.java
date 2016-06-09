package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.ToolCommandEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.ToolCommand;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class ToolCommandEntityMapper {

    @Inject
    public ToolCommandEntityMapper() {
    }

    public Observable.OnSubscribe<Object> mapToToolCommandEntity(final ToolCommand toolCommand) {
        return new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                switch (toolCommand.command()) {
                    case EXTRACT:
                    case RETRACT:
                        subscriber.onNext(ToolCommandEntity.Extract.create(toolCommand.amount()));
                        break;
                    case FLOWRATE:
                        subscriber.onNext(ToolCommandEntity.Flowrate.create(toolCommand.factor()));
                        break;
                    default:
                        subscriber.onError(new EntityMapperException());
                }
                subscriber.onCompleted();
            }
        };
    }
}
