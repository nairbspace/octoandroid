package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.FilesEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.Files;

import javax.inject.Inject;

import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class FilesEntityMapper {

    private final EntitySerializer mEntitySerializer;

    @Inject
    public FilesEntityMapper(EntitySerializer entitySerializer) {
        mEntitySerializer = entitySerializer;
    }

    public Func1<FilesEntity, Files> mapToFiles() {
        return new Func1<FilesEntity, Files>() {
            @Override
            public Files call(FilesEntity filesEntity) {
                try {
                    return mEntitySerializer.transform(filesEntity, Files.class);
                } catch (Exception e) {
                    throw Exceptions.propagate(new EntityMapperException());
                }
            }
        };
    }
}
