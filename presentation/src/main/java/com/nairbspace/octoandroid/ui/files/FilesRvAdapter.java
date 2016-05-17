package com.nairbspace.octoandroid.ui.files;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.FilesModel;

public class FilesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements FileViewHolder.Listener {

    private static final int VIEW_TYPE_FILE = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    private final Listener mListener;
    private FilesModel mFilesModel;
    private int mClickedPosition = -1;
    private int mOldClickedPosition = -1;

    public FilesRvAdapter(FilesModel filesModel, Listener listener) {
        mFilesModel = filesModel;
        mListener = listener;
    }

    public void setFilesModel(FilesModel filesModel) {
        mFilesModel = filesModel;
        notifyDataSetChanged();
    }

    public int getClickedPosition() {
        return mClickedPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FILE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_files, parent, false);
            return new FileViewHolder(view, this);
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
            fileViewHolder.hideOnClickView();

            if (position == mOldClickedPosition) {
                fileViewHolder.hideOnClickView();
                mOldClickedPosition = -1;
            }

            if (position == mClickedPosition) {
                fileViewHolder.showOnClickView();
                mOldClickedPosition = mClickedPosition;
            }

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

    @Override
    public void fileViewClicked(int position) {
        if (position == mOldClickedPosition) { // If file is clicked again, then close it
            mClickedPosition = -1;
            mOldClickedPosition = position;
        } else {
            mClickedPosition = position; // If not then open it
            notifyItemChanged(position);
        }

        if (mOldClickedPosition != -1) { // Close previous clicked file
            notifyItemChanged(mOldClickedPosition);
        }
    }

    @Override
    public void printButtonClicked(String apiPath) {
        mListener.printerButtonClicked(apiPath);
    }

    @Override
    public void downloadButtonClicked(String downloadUrl) {
        mListener.downloadButtonClicked(downloadUrl);
    }

    public interface Listener {
        void printerButtonClicked(String apiPath);
        void downloadButtonClicked(String downloadUrl);
    }
}
