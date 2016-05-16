package com.nairbspace.octoandroid.ui.files;

import com.nairbspace.octoandroid.model.FilesModel;

public interface FilesScreen {

    void updateUi(FilesModel filesModel);

    void showProgressBar();

    void showEmptyScreen();
}
