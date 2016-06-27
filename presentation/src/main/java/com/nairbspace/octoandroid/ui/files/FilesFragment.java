package com.nairbspace.octoandroid.ui.files;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.ui.templates.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.templates.Presenter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FilesFragment extends BasePagerFragmentListener<FilesScreen,
        FilesFragment.Listener> implements FilesScreen, FilesRvAdapter.Listener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String FILESMODEL_KEY = "filesmodel_key";
    private static final String CLICKED_POSITION_KEY = "clicked_position_key";
    @BindString(R.string.exception_download_file_error) String DOWNLOAD_ERROR;

    @Inject FilesPresenter mPresenter;

    @BindView(R.id.files_swipe_refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.file_list_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.empty_files_textview) TextView mEmptyTextView;
    @BindView(R.id.files_progress_bar) ProgressBar mProgressBar;

    private Listener mListener;
    private FilesModel mFilesModel;
    private FilesRvAdapter mAdapter;

    public static FilesFragment newInstance() {
        return new FilesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupApplication.get(getContext()).getAppComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files_list, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        showEmptyScreen();
        mRefreshLayout.setOnRefreshListener(this);
        if (savedInstanceState != null && savedInstanceState.getParcelable(FILESMODEL_KEY) != null) {
            mFilesModel = savedInstanceState.getParcelable(FILESMODEL_KEY);
            updateUi(mFilesModel);
            int clickedPosition = savedInstanceState.getInt(CLICKED_POSITION_KEY, -1);
            mAdapter.fileViewClicked(clickedPosition);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_file, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == getNavigator().getPickFileRequestCode() && resultCode == Activity.RESULT_OK) {
            mPresenter.executeUpload(data.getDataString()); // TODO-low should probably have upload preference default alert
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        getNavigator().checkReadGrantedAndTryAgain(requestCode, grantResults, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_file_menu_item:
                getNavigator().tryToNavigateToFileManagerForResult(this);
                return true;
            case R.id.refresh_file_menu_item:
                mPresenter.execute();
            default:
                return super.onOptionsItemSelected(item);
        }
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
        mRefreshLayout.setRefreshing(false);
        mEmptyTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        mEmptyTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        if (!mRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
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
    }

    @Override
    public void deleteButtonClicked(String apiUrl, int adapterPosition) {
        mPresenter.executeDelete(apiUrl, adapterPosition);
    }

    @Override
    public void downloadButtonClicked(String downloadUrl) {
        boolean result = getNavigator().navigateToDownloadFile(this, downloadUrl);
        if (!result) showToast(DOWNLOAD_ERROR);
    }

    @Override
    public void loadButtonClicked(String apiUrl) {
        mPresenter.executeLoad(apiUrl);
    }

    @Override
    public void sliceButtonClicked(String apiUrl) {
        mListener.sliceButtonClicked(apiUrl);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void onRefresh() {
        mPresenter.execute();
    }

    public interface Listener {
        void sliceButtonClicked(String apiUrl);
    }
}
