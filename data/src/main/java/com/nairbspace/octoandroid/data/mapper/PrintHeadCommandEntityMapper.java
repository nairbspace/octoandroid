package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.PrintHeadCommandEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.PrintHeadCommand;

import rx.Observable;
import rx.Subscriber;

public class PrintHeadCommandEntityMapper {

    public static Observable.OnSubscribe<Object> mapToPrintHeadCommandEntity(final PrintHeadCommand printHeadCommand) {
        return new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                switch (printHeadCommand.type()) {
                    case JOG_X_LEFT:
                    case JOG_X_RIGHT:
                        subscriber.onNext(PrintHeadCommandEntity.Jog.createX(printHeadCommand.jog()));
                        break;
                    case JOG_Y_UP:
                    case JOG_Y_DOWN:
                        subscriber.onNext(PrintHeadCommandEntity.Jog.createY(printHeadCommand.jog()));
                        break;
                    case JOG_Z_UP:
                    case JOG_Z_DOWN:
                        subscriber.onNext(PrintHeadCommandEntity.Jog.createZ(printHeadCommand.jog()));
                        break;
                    case HOME_XY:
                        subscriber.onNext(PrintHeadCommandEntity.Home.createXy());
                        break;
                    case HOME_Z:
                        subscriber.onNext(PrintHeadCommandEntity.Home.createZ());
                        break;
                    case FEEDRATE:
                        subscriber.onNext(PrintHeadCommandEntity.FeedRate.create(printHeadCommand.factor()));
                        break;
                    default:
                        subscriber.onError(new EntityMapperException());
                }
                subscriber.onCompleted();
            }
        };
    }
}
