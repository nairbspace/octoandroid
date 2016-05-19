package com.nairbspace.octoandroid.ui.files;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.FilesModel;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileViewHolder extends RecyclerView.ViewHolder {
    private final Listener mListener;
    private final View mView;

    @BindView(R.id.file_name_text_view) TextView mFileNameTextView;
    @BindView(R.id.file_size_text_view) TextView mFileSizeTextView;
    @BindView(R.id.file_visible_card_view) RelativeLayout mVisibleLayout;
    @BindView(R.id.files_list_on_click_view) LinearLayout mOnClickView;
    @BindView(R.id.file_type_icon) ImageView mFileTypeIcon;
    @BindDrawable(R.drawable.ic_file_black_24dp) Drawable mFileDrawable;
    @BindDrawable(R.drawable.ic_sd_storage_black_24dp) Drawable mSdDrawable;
    @BindString(R.string.file_origin_sdcard) String SDCARD;
    @BindInt(android.R.integer.config_longAnimTime) int mLongAnimTime;
    @BindColor(R.color.colorPrimaryLight) int mBackgroundColor;
    @BindColor(android.R.color.white) int mDefaultColor;

    private FilesModel.FileModel mFileModel;

    public FileViewHolder(View view, Listener listener) {
        super(view);
        mView = view;
        mListener = listener;
        ButterKnife.bind(this, view); // TODO probably need to null this
    }

    public void bindFileModel(FilesModel.FileModel fileModel) {
        mFileModel = fileModel;
        mFileNameTextView.setText(fileModel.name());
        mFileSizeTextView.setText(fileModel.size());
        setFileTypeIcon(fileModel);
    }

    public void hideOnClickView() {
        mOnClickView.setVisibility(View.GONE);
        mView.setBackgroundColor(mDefaultColor);
    }

    private void setFileTypeIcon(FilesModel.FileModel fileModel) {
        if (fileModel.origin().equals(SDCARD)) {
            mFileTypeIcon.setImageDrawable(mSdDrawable);
        } else {
            mFileTypeIcon.setImageDrawable(mFileDrawable);
        }
    }

    @OnClick(R.id.file_visible_card_view)
    void visibleLayoutClicked() {
        mListener.fileViewClicked(getAdapterPosition());
    }

    @OnClick(R.id.printer_file_icon)
    void printerButtonClicked() {
        mListener.printButtonClicked(mFileModel.apiUrl());
    }

    @OnClick(R.id.file_download_icon)
    void downloadButtonClicked() {
        mListener.downloadButtonClicked(mFileModel.downloadUrl());
    }

    public void showOnClickView() {
        mOnClickView.setVisibility(View.VISIBLE);
        mView.setBackgroundColor(mBackgroundColor);
    }

    public void updateOnClickView(final boolean isShown) {
        mOnClickView.setVisibility(isShown ? View.GONE : View.VISIBLE);
        mOnClickView.animate().setDuration(mLongAnimTime).alpha(isShown ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mOnClickView.setVisibility(isShown ? View.GONE : View.VISIBLE);
            }
        });
    }

    public interface Listener {
        void fileViewClicked(int position);
        void printButtonClicked(String apiUrl);
        void downloadButtonClicked(String downloadUrl);
    }
}
