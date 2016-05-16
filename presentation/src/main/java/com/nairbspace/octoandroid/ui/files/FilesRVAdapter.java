package com.nairbspace.octoandroid.ui.files;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.ui.files.FilesFragment.ListFragmentListener;

public class FilesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_FILE = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    private final ListFragmentListener mListener;
    private FilesModel mFilesModel;

    public FilesRvAdapter(FilesModel filesModel, ListFragmentListener listener) {
        mFilesModel = filesModel;
        mListener = listener;
    }

    public void setFilesModel(FilesModel filesModel) {
        mFilesModel = filesModel;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FILE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_files, parent, false);
            return new FileViewHolder(view, mListener);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_files_list_footer, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FileViewHolder) {
            FilesModel.FileModel fileModel = mFilesModel.fileModels().get(position);
            FileViewHolder fileViewHolder = (FileViewHolder) holder;
            fileViewHolder.bindFileModel(fileModel);
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.bindFooterModel(mFilesModel);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mFilesModel.fileModels().size() ? VIEW_TYPE_FOOTER : VIEW_TYPE_FILE);
    }

    @Override
    public int getItemCount() {
        return mFilesModel.fileModels().size() + 1;
    }
}