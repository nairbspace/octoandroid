package com.nairbspace.octoandroid.ui.files;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.FilesModel;

import butterknife.BindDrawable;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final FilesFragment.ListFragmentListener mListener;

    @BindView(R.id.file_name_text_view) TextView mFileNameTextView;
    @BindView(R.id.file_size_text_view) TextView mFileSizeTextView;
    @BindView(R.id.files_list_on_click_view) LinearLayout mOnClickView;
    @BindView(R.id.file_type_icon) ImageView mFileTypeIcon;
    @BindDrawable(R.drawable.ic_file_black_24dp) Drawable mFileDrawable;
    @BindDrawable(R.drawable.ic_sd_storage_black_24dp) Drawable mSdDrawable;
    @BindString(R.string.file_origin_sdcard) String SDCARD;
    @BindInt(android.R.integer.config_longAnimTime) int mLongAnimTime;

    private FilesModel.FileModel mFileModel;

    public FileViewHolder(View view, FilesFragment.ListFragmentListener listener) {
        super(view);
        mListener = listener;
        view.setOnClickListener(this);
        ButterKnife.bind(this, view); // TODO probably need to null this
    }

    public void bindFileModel(FilesModel.FileModel fileModel) {
        mFileModel = fileModel;
        mFileNameTextView.setText(fileModel.name());
        mFileSizeTextView.setText(fileModel.size());
        setFileTypeIcon(fileModel);
    }

    private void setFileTypeIcon(FilesModel.FileModel fileModel) {
        if (fileModel.origin().equals(SDCARD)) {
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
        mOnClickView.setVisibility(isShown ? View.GONE : View.VISIBLE);
        mOnClickView.animate().setDuration(mLongAnimTime).alpha(isShown ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mOnClickView.setVisibility(isShown ? View.GONE : View.VISIBLE);
            }
        });
    }
}
