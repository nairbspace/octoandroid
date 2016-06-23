package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Files;
import com.nairbspace.octoandroid.domain.model.Files.File;
import com.nairbspace.octoandroid.exception.TransformErrorException;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.model.FilesModel.FileModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class FilesMapper extends MapperUseCase<Files, FilesModel> {
    private static final String FOLDER = "folder";
    private static final String STL = "stl";

    @Inject
    public FilesMapper(ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    protected Observable<FilesModel> buildUseCaseObservableInput(final Files files) {
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
        String free = ByteConverter.toReadableString(files.free());
        String total = ByteConverter.toReadableString(files.total());
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

        boolean isStl = isStl(file.name());

        String size = "";
        if (file.size() != null) {
            size = ByteConverter.toReadableString(file.size());
        }

        String date = "";
        String time = "";
        if (file.date() != null) {
            date = DateTimeConverter.unixTimeToShortDateString(file.date());
            time = DateTimeConverter.unixTimeToShortTimeString(file.date());
        }

        String origin = file.origin();

        String apiUrl = file.refs().resource();

        String downloadUrl = "";
        if (file.refs().download() != null) {
            downloadUrl = file.refs().download();
        }

        String estimatedPrintTime = "";
        if (isEstimatedPrintTimeNotNull(file)) {
            Double estPrintTime = file.gcodeAnalysis().estimatedPrintTime();
            estimatedPrintTime = DateTimeConverter.secondsToHHmmss(estPrintTime);
        }

        String type = "";
        if (!file.type().contains(FOLDER)) {
            type = file.type();
        }

        return FileModel.builder()
                .name(name)
                .isStl(isStl)
                .size(size)
                .date(date)
                .time(time)
                .origin(origin)
                .apiUrl(apiUrl)
                .downloadUrl(downloadUrl)
                .estimatedPrintTime(estimatedPrintTime)
                .type(type)
                .build();
    }

    private boolean isStl(String fileName) {
        String ext = getFileExt(fileName);
        return ext != null && ext.equals(STL);
    }

    private String getFileExt(String fileName) {
        try {
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            return ext.toLowerCase();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
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

}
