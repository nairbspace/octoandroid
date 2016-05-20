package com.nairbspace.octoandroid.ui.files;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.ui.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FilesFragment extends BasePagerFragmentListener<FilesScreen,
        FilesFragment.Listener> implements FilesScreen, FilesRvAdapter.Listener {

    private static final String FILESMODEL_KEY = "filesmodel_key";
    private static final String CLICKED_POSITION_KEY = "clicked_position_key";

    @Inject FilesPresenter mPresenter;
    private Listener mListener;
    private FilesModel mFilesModel;
    private FilesRvAdapter mAdapter;

    @BindView(R.id.file_list_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.empty_files_textview) TextView mEmptyTextView;
    @BindView(R.id.files_progress_bar) ProgressBar mProgressBar;
    @BindString(R.string.status) String STATUS;

    public static FilesFragment newInstance() {
        return new FilesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files_list, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        setActionBarTitle(STATUS);
        showEmptyScreen();
        if (savedInstanceState != null && savedInstanceState.getParcelable(FILESMODEL_KEY) != null) {
            mFilesModel = savedInstanceState.getParcelable(FILESMODEL_KEY);
            updateUi(mFilesModel);
            int clickedPosition = savedInstanceState.getInt(CLICKED_POSITION_KEY, -1);
            mAdapter.fileViewClicked(clickedPosition);
        }
        return view;
    }

    @Override
    public void showEmptyScreen() {
        mEmptyTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void deleteFileFromAdapter(int adapterPosition) {
        if (mAdapter != null) {
            mAdapter.deleteFile(adapterPosition);
        }
    }

    private void showRecyclerView() {
        mEmptyTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        mEmptyTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateUi(FilesModel filesModel) {
        mFilesModel = filesModel;
        if (mAdapter == null) {
            mAdapter = new FilesRvAdapter(filesModel, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setFilesModel(filesModel);
        }
        showRecyclerView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FILESMODEL_KEY, mFilesModel);
        if (mAdapter != null) {
            outState.putInt(CLICKED_POSITION_KEY, mAdapter.getClickedPosition());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter = null;
    }

    @NonNull
    @Override
    protected Presenter setPresenter() {
        return mPresenter;
    }

    @NonNull
    @Override
    protected FilesScreen setScreen() {
        return this;
    }

    @NonNull
    @Override
    protected Listener setListener() {
        mListener = (Listener) getContext();
        return mListener;
    }

    @Override
    public void printerButtonClicked(String apiUrl) {
        mPresenter.executePrint(apiUrl);
        Toast.makeText(getContext(), apiUrl, Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteButtonClicked(String apiUrl, int adapterPosition) {
        mPresenter.executeDelete(apiUrl, adapterPosition);
    }

    @Override
    public void downloadButtonClicked(String downloadUrl) {
        Toast.makeText(getContext(), downloadUrl, Toast.LENGTH_LONG).show();
        mListener.downloadFile(downloadUrl);
    }

    @Override
    public void loadButtonClicked(String apiUrl) {
        mPresenter.executeLoad(apiUrl);
    }

    public interface Listener {
        void downloadFile(String url);
    }
}
