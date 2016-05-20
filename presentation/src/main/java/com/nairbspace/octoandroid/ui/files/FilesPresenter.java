package com.nairbspace.octoandroid.ui.files;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.DeleteFile;
import com.nairbspace.octoandroid.domain.interactor.GetFiles;
import com.nairbspace.octoandroid.domain.interactor.SendFileCommand;
import com.nairbspace.octoandroid.domain.model.FileCommand;
import com.nairbspace.octoandroid.domain.model.Files;
import com.nairbspace.octoandroid.mapper.FileCommandModelMapper;
import com.nairbspace.octoandroid.mapper.FilesMapper;
import com.nairbspace.octoandroid.model.FileCommandModel;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

public class FilesPresenter extends UseCasePresenter<FilesScreen> {

    private final GetFiles mGetFiles;
    private final FilesMapper mFilesMapper;
    private final FileCommandModelMapper mFileCommandModelMapper;
    private final SendFileCommand mSendFileCommand;
    private final DeleteFile mDeleteFile;
    private FilesScreen mScreen;

    @Inject
    public FilesPresenter(GetFiles getFiles, FilesMapper filesMapper,
                          FileCommandModelMapper fileCommandModelMapper,
                          SendFileCommand sendFileCommand, DeleteFile deleteFile) {
        super(getFiles);
        mGetFiles = getFiles;
        mFilesMapper = filesMapper;
        mFileCommandModelMapper = fileCommandModelMapper;
        mSendFileCommand = sendFileCommand;
        mDeleteFile = deleteFile;
    }

    @Override
    protected void onInitialize(FilesScreen filesScreen) {
        mScreen = filesScreen;
        execute();
    }

    @Override
    protected void execute() {
        mScreen.showProgressBar();
        mGetFiles.execute(new GetFilesSubsubscriber());
    }

    @Override
    protected void networkNowInactive() {
        super.networkNowInactive();
        mScreen.showEmptyScreen();
    }

    @Override
    protected void onNetworkSwitched() {
        execute();
    }

    private void renderScreen(FilesModel filesModel) {
        mScreen.updateUi(filesModel);
    }

    @Override
    protected void onDestroy(FilesScreen filesScreen) {
        super.onDestroy(filesScreen);
        mFilesMapper.unsubscribe();
    }

    public void executePrint(String apiUrl) {
        FileCommandModel model = FileCommandModel.startPrint(apiUrl);
        mFileCommandModelMapper.execute(new FileCommandMapperSubscriber(), model);
    }

    public void executeLoad(String apiUrl) {
        FileCommandModel model = FileCommandModel.loadPrint(apiUrl);
        mFileCommandModelMapper.execute(new FileCommandMapperSubscriber(), model);
    }

    public void executeDelete(String apiUrl, int adapterPosition) {
        mDeleteFile.execute(new DeleteFileSubscriber(adapterPosition), apiUrl);
    }

    private final class GetFilesSubsubscriber extends DefaultSubscriber<Files> {

        @Override
        public void onError(Throwable e) {
            mScreen.showEmptyScreen();
            super.onError(e);
        }

        @Override
        public void onNext(Files files) {
            super.onNext(files);
            mFilesMapper.execute(new TransformSubscriber(), files);
        }
    }

    private final class TransformSubscriber extends DefaultSubscriber<FilesModel> {
        @Override
        public void onError(Throwable e) {
            mScreen.showEmptyScreen();
            super.onError(e);
        }

        @Override
        public void onNext(FilesModel filesModel) {
            renderScreen(filesModel);
        }
    }

    private final class FileCommandMapperSubscriber extends DefaultSubscriber<FileCommand> {
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(FileCommand fileCommand) {
            super.onNext(fileCommand);
            mSendFileCommand.execute(new SendFileCommandSubscriber(), fileCommand);
        }
    }

    private final class SendFileCommandSubscriber extends DefaultSubscriber<Object> {

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }
    }

    private final class DeleteFileSubscriber extends DefaultSubscriber {

        private final int mAdapterPosition;

        public DeleteFileSubscriber(int adapterPosition) {
            mAdapterPosition = adapterPosition;
        }

        @Override
        public void onNext(Object o) {
            mScreen.deleteFileFromAdapter(mAdapterPosition);
        }
    }
}
