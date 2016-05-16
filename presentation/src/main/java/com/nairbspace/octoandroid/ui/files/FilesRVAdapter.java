package com.nairbspace.octoandroid.ui.files;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.FilesModel;
import com.nairbspace.octoandroid.ui.files.FilesFragment.ListFragmentListener;

import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FilesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_FILE = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    private final ListFragmentListener mListener;
    private FilesModel mFilesModel;
    private FileViewHolder mFileViewHolder;
    private FooterViewHolder mFooterViewHolder;
    private int mFileModelsSize;

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
            mFileViewHolder = new FileViewHolder(view, mListener);
            return mFileViewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_files_list_footer, parent, false);
            mFooterViewHolder = new FooterViewHolder(view);
            return mFooterViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        List<FilesModel.FileModel> fileModels = mFilesModel.fileModels();
        if (position < mFileModelsSize) {
            FilesModel.FileModel fileModel = fileModels.get(position);
            mFileViewHolder.bindFileModel(fileModel);
        } else {
            mFooterViewHolder.bindFooterModel(mFilesModel);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mFileModelsSize ? VIEW_TYPE_FOOTER : VIEW_TYPE_FILE);
    }

    @Override
    public int getItemCount() {
        mFileModelsSize = mFilesModel.fileModels().size();
        return mFileModelsSize + 1;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.free_text_view) TextView mFreeTextView;
        @BindView(R.id.total_text_view) TextView mTotalTextView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindFooterModel(FilesModel filesModel) {
            mFreeTextView.setText(filesModel.free());
            mTotalTextView.setText(filesModel.total());
        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.file_name_text_view) TextView mFileNameTextView;
        @BindView(R.id.file_size_text_view) TextView mFileSizeTextView;
        @BindView(R.id.files_list_on_click_view) LinearLayout mOnClickView;
        @BindView(R.id.file_type_icon) ImageView mFileTypeIcon;
        @BindDrawable(R.drawable.ic_file_black_24dp) Drawable mFileDrawable;
        @BindDrawable(R.drawable.ic_sd_storage_black_24dp) Drawable mSdDrawable;
        @BindString(R.string.file_origin_sdcard) String SDCARD;
        public FilesModel.FileModel mFileModel;
        private final ListFragmentListener mListener;

        public FileViewHolder(View view, ListFragmentListener listener) {
            super(view);
            mListener = listener;
            view.setOnClickListener(this);
            ButterKnife.bind(this, view); // TODO probably need to null this
        }

        public void bindFileModel(FilesModel.FileModel fileModel) {
            mFileNameTextView.setText(fileModel.name());
            mFileSizeTextView.setText(fileModel.size());
            setFileTypeIcon(fileModel);
        }

        private void setFileTypeIcon(FilesModel.FileModel fileModel) {
            if (fileModel.origin().equals(SDCARD)) { // TODO get from resource
                mFileTypeIcon.setImageDrawable(mSdDrawable);
            } else {
                mFileTypeIcon.setImageDrawable(mFileDrawable);
            }
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
