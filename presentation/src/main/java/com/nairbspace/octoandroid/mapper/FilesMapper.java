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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

public class FilesMapper extends MapperUseCase<Files, FilesModel> {
    private static final String FOLDER = "folder";

    @Inject
    public FilesMapper(ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
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
        String free = humanReadableByteCount(files.free(), true);
        String total = humanReadableByteCount(files.total(), true);
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
            if (ifFileIsNotFolder(file)) {
                FileModel fileModel = mapToFileModel(file);
                fileModels.add(fileModel);
            }
        }
        return fileModels;
    }

    private FileModel mapToFileModel(File file) {
        String name = file.name();
        String size = "-";
        if (file.size() != null) {
            size = humanReadableByteCount(file.size(), false);
        }
        String date = "-" ;
        if (file.date() != null) {
            date = formatDate(file.date());
        }
        String origin = file.origin();
        String apiPath = urlPath(file.refs().resource());
        String downloadPath = "-";
        if (file.refs().download() != null) {
            downloadPath = file.refs().download();
        }
        String estimatedPrintTime = "-";
        if (file.gcodeAnalysis() != null && file.gcodeAnalysis().estimatedPrintTime() != null) {
            estimatedPrintTime = formatTimeInterval(file.gcodeAnalysis().estimatedPrintTime().longValue());
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

    private boolean ifFileIsNotFolder(File file) {
        return !file.type().contains(FOLDER);
    }

    private String humanReadableByteCount(long bytes, boolean inSiUnits) {
        int unit = inSiUnits ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exponent = (int) (Math.log(bytes) / Math.log(unit));
        String prefix = (inSiUnits ? "kMGTPE" : "KMGTPE").charAt(exponent-1) + (inSiUnits ? "" : "i");
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit, exponent), prefix);
    }

    public String urlPath(String url) {
        try {
            URI uri = new URI(url);
            return uri.getPath();
        } catch (URISyntaxException e) {
            throw Exceptions.propagate(new TransformErrorException(e));
        }
    }

    public String formatDate(long unixSeconds) {
        Date date = new Date(unixSeconds * (long) 1000);
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        return df.format(date);
    }

    public String formatTimeInterval(long estimatedSeconds) {
        long hours = estimatedSeconds / 3600;
        long secondsLeft = estimatedSeconds - hours * 3600;
        long minutes = secondsLeft / 60;
        long seconds = secondsLeft - minutes * 60;

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
    }
}
