package com.nairbspace.octoandroid.ui.files;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.ui.files.FilesFragment.ListFragmentListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilesRvAdapter extends RecyclerView.Adapter<FilesRvAdapter.ViewHolder> {

    private final ListFragmentListener mListener;
    private FilesModel mFilesModel;

    public FilesRvAdapter(FilesModel filesModel, ListFragmentListener listener) {
        mFilesModel = filesModel;
        mListener = listener;
    }

    public void setFilesModel(FilesModel filesModel) {
        mFilesModel = filesModel;
        notifyDataSetChanged(); // TODO-Low should be notifyitem changed if possible
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_files, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FilesModel.FileModel fileModel = mFilesModel.fileModels().get(position);
        holder.bindFileModel(fileModel);
    }

    @Override
    public int getItemCount() {
        return mFilesModel.fileModels().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.file_name_text_view) TextView mFileNameTextView;
        @BindView(R.id.file_size_text_view) TextView mFileSizeTextView;
        @BindView(R.id.files_list_on_click_view) LinearLayout mOnClickView;
        public FilesModel.FileModel mFileModel;
        private final ListFragmentListener mListener;

        public ViewHolder(View view, ListFragmentListener listener) {
            super(view);
            mListener = listener;
            view.setOnClickListener(this);
            ButterKnife.bind(this, view); // TODO probably need to null this
        }

        public void bindFileModel(FilesModel.FileModel fileModel) {
            mFileNameTextView.setText(fileModel.name());
            mFileSizeTextView.setText(fileModel.size());
        }

        @Override
        public void onClick(View v) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            if (mListener != null) {
                mListener.onListFragmentInteraction(mFileModel);
            }

            showOnClickView(mOnClickView.isShown());
        }

        private void showOnClickView(final boolean isShown) {
            int shortAnimTime = 500; // TODO Should get from context?

            mOnClickView.setVisibility(isShown ? View.GONE : View.VISIBLE);
            mOnClickView.animate().setDuration(shortAnimTime).alpha(isShown ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mOnClickView.setVisibility(isShown ? View.GONE : View.VISIBLE);
                }
            });
        }
    }
}
