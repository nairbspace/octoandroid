package com.nairbspace.octoandroid.ui.files;

import android.content.Context;

import com.nairbspace.octoandroid.model.FilesModel;

public interface FilesScreen {

    void updateUi(FilesModel filesModel);

    void showProgressBar();

    void showEmptyScreen();

    void deleteFileFromAdapter(int adapterPosition);

    void showToast(String message);

    Context context();
}
