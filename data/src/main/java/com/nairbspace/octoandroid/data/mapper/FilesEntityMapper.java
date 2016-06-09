package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.FilesEntity;
import com.nairbspace.octoandroid.data.exception.EntityMapperException;
import com.nairbspace.octoandroid.domain.model.Files;

import rx.exceptions.Exceptions;
import rx.functions.Func1;

public class FilesEntityMapper {

    public static Func1<FilesEntity, Files> mapToFiles(final EntitySerializer entitySerializer) {
        return new Func1<FilesEntity, Files>() {
            @Override
            public Files call(FilesEntity filesEntity) {
                try {
                    return entitySerializer.transform(filesEntity, Files.class);
                } catch (Exception e) {
                    throw Exceptions.propagate(new EntityMapperException());
                }
            }
        };
    }
}
