package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Files;
import com.nairbspace.octoandroid.domain.model.Files.File;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.model.FilesModel.FileModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public class FilesMapper extends MapperUseCase<Files, FilesModel> {
    private static final String FOLDER = "folder";
    private final ByteConverter mByteConverter;
    private final DateTimeConverter mDateTimeConverter;

    @Inject
    public FilesMapper(ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread,
                       ByteConverter byteConverter,
                       DateTimeConverter dateTimeConverter) {
        super(threadExecutor, postExecutionThread);
        mByteConverter = byteConverter;
        mDateTimeConverter = dateTimeConverter;
    }

    @Override
    protected Observable<FilesModel> buildUseCaseObservable(final Files files) {
        return Observable.create(new Observable.OnSubscribe<FilesModel>() {
            @Override
            public void call(Subscriber<? super FilesModel> subscriber) {
                try {
                    FilesModel filesModel = mapToFilesModel(files);
                    subscriber.onNext(filesModel);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new TransformErrorException());
                }
            }
        });
    }

    private FilesModel mapToFilesModel(Files files) {
        String free = mByteConverter.toReadableString(files.free());
        String total = mByteConverter.toReadableString(files.total());
        List<FileModel> fileModels = mapToFileModels(files.files());
        return FilesModel.builder()
                .free(free)
                .total(total)
                .fileModels(fileModels)
                .build();
    }

    private List<FileModel> mapToFileModels(List<File> files) {
        List<FileModel> fileModels = new ArrayList<>();
        for (File file : files) {
            if (isFileNotFolder(file)) {
                FileModel fileModel = mapToFileModel(file);
                fileModels.add(fileModel);
            }
        }
        return fileModels;
    }

    @SuppressWarnings("ConstantConditions")
    private FileModel mapToFileModel(File file) {
        String name = file.name();

        String size = "-";
        if (file.size() != null) {
            size = mByteConverter.toReadableString(file.size());
        }

        String date = "-" ;
        if (file.date() != null) {
            date = mDateTimeConverter.secondsToDateTimeString(file.date());
        }

        String origin = file.origin();

        String apiPath = urlPath(file.refs().resource());

        String downloadPath = "-";
        if (file.refs().download() != null) {
            downloadPath = file.refs().download();
        }

        String estimatedPrintTime = "-";
        if (isEstimatedPrintTimeNotNull(file)) {
            Double estPrintTime = file.gcodeAnalysis().estimatedPrintTime();
            estimatedPrintTime = mDateTimeConverter.secondsToHHmmss(estPrintTime);
        }

        String type = "-";
        if (!file.type().contains(FOLDER)) {
            type = file.type();
        }

        return FileModel.builder()
                .name(name)
                .size(size)
                .date(date)
                .origin(origin)
                .apiPath(apiPath)
                .downloadPath(downloadPath)
                .estimatedPrintTime(estimatedPrintTime)
                .type(type)
                .build();
    }

    private boolean isGcodeAnalysisNotNull(File file) {
        return file.gcodeAnalysis() != null;
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isEstimatedPrintTimeNotNull(File file) {
        return isGcodeAnalysisNotNull(file) && file.gcodeAnalysis().estimatedPrintTime() != null;
    }

    private boolean isFileNotFolder(File file) {
        return !file.type().contains(FOLDER);
    }

    public String urlPath(String url) {
        try {
            URI uri = new URI(url);
            return uri.getPath();
        } catch (URISyntaxException e) {
            throw Exceptions.propagate(new TransformErrorException(e));
        }
    }
}
