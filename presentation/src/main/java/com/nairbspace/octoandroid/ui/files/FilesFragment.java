package com.nairbspace.octoandroid.ui.files;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.app.SetupApplication;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.ui.BasePagerFragmentListener;
import com.nairbspace.octoandroid.ui.Presenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilesFragment extends BasePagerFragmentListener<FilesScreen,
        FilesFragment.ListFragmentListener> implements FilesScreen {

    private static final String FILESMODEL_KEY = "filesmodel_key";

    @Inject FilesPresenter mPresenter;
    private ListFragmentListener mListener;
    private FilesModel mFilesModel;
    private FilesRvAdapter mAdapter;

    @BindView(R.id.file_list_recyclerview) RecyclerView mRecyclerView;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        if (savedInstanceState != null && savedInstanceState.getParcelable(FILESMODEL_KEY) != null) {
            mFilesModel = savedInstanceState.getParcelable(FILESMODEL_KEY);
            updateUi(mFilesModel);
        }
        return view;
    }

    @Override
    public void updateUi(FilesModel filesModel) {
        mFilesModel = filesModel;
        if (mAdapter == null) {
            mAdapter = new FilesRvAdapter(filesModel, mListener);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setFilesModel(filesModel);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FILESMODEL_KEY, mFilesModel); // TODO would be nice to save last clicked item
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
    protected ListFragmentListener setListener() {
        mListener = (ListFragmentListener) getContext();
        return mListener;
    }

    public interface ListFragmentListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(FilesModel.FileModel fileModel);
    }
}
