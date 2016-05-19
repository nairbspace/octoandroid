package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.FileCommandEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.FileCommand;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class FileCommandEntityMapper {

    @Inject
    public FileCommandEntityMapper() {
    }

    public Observable.OnSubscribe<FileCommandEntity> mapToFileCommandEntity(final FileCommand fileCommand) {
        return new Observable.OnSubscribe<FileCommandEntity>() {
            @Override
            public void call(Subscriber<? super FileCommandEntity> subscriber) {
                try {
                    FileCommandEntity fileCommandEntity = FileCommandEntity.builder()
                            .command(fileCommand.command())
                            .print(fileCommand.print())
                            .build();
                    subscriber.onNext(fileCommandEntity);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new EntityMapperException());
                }
            }
        };
    }
}
