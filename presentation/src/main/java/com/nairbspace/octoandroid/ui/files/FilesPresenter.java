package com.nairbspace.octoandroid.ui.files;

import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetFiles;
import com.nairbspace.octoandroid.domain.model.Files;
import com.nairbspace.octoandroid.mapper.FilesMapper;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import javax.inject.Inject;

public class FilesPresenter extends UseCasePresenter<FilesScreen> {

    private FilesScreen mScreen;
    private final GetFiles mGetFiles;
    private final FilesMapper mFilesMapper;

    @Inject
    public FilesPresenter(GetFiles getFiles, FilesMapper filesMapper) {
        super(getFiles);
        mGetFiles = getFiles;
        mFilesMapper = filesMapper;
    }

    @Override
    protected void onInitialize(FilesScreen filesScreen) {
        mScreen = filesScreen;
        mGetFiles.execute(new GetFilesSubsubscriber());
    }

    private void renderScreen(FilesModel filesModel) {
        mScreen.updateUi(filesModel);
    }

    private final class GetFilesSubsubscriber extends DefaultSubscriber<Files> {

        @Override
        public void onError(Throwable e) {
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
            super.onError(e);
        }

        @Override
        public void onNext(FilesModel filesModel) {
            renderScreen(filesModel);
        }
    }
}
