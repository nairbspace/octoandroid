package com.nairbspace.octoandroid.ui.files;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.files_list_on_click_view) LinearLayout mOnClickView;
    @BindView(R.id.file_type_icon) ImageView mFileTypeIcon;
    @BindView(R.id.file_date_text_view) TextView mDateTextView;
    @BindView(R.id.file_time_text_view) TextView mTimeTextView;
    @BindView(R.id.load_file_icon) ImageView mLoadFileIcon;
    @BindView(R.id.printer_file_icon) ImageView mPrintFileIcon;
    @BindView(R.id.slice_file_icon) ImageView mSliceFileIcon;
    @BindDrawable(R.drawable.ic_file_black_24dp) Drawable mFileDrawable;
    @BindDrawable(R.drawable.ic_sd_storage_black_24dp) Drawable mSdDrawable;
    @BindString(R.string.file_origin_sdcard) String SDCARD;
    @BindInt(android.R.integer.config_longAnimTime) int mLongAnimTime;
    @BindColor(R.color.fileSelected) int mBackgroundColor;
    @BindColor(android.R.color.white) int mDefaultColor;

    private FilesModel.FileModel mFileModel;

    public FileViewHolder(View view, Listener listener) {
        super(view);
        mView = view;
        mListener = listener;
        ButterKnife.bind(this, view);
    }

    public void bindFileModel(FilesModel.FileModel fileModel) {
        mFileModel = fileModel;
        mFileNameTextView.setText(fileModel.name());
        mFileSizeTextView.setText(fileModel.size());
        mDateTextView.setText(fileModel.date());
        mTimeTextView.setText(fileModel.time());
        setFileTypeIcon(fileModel);
        setSliceFileIcon(fileModel);
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

    private void setSliceFileIcon(FilesModel.FileModel fileModel) {
        boolean isStl = fileModel.isStl();
        mLoadFileIcon.setVisibility(isStl ? View.GONE : View.VISIBLE);
        mPrintFileIcon.setVisibility(isStl ? View.GONE : View.VISIBLE);
        mSliceFileIcon.setVisibility(isStl ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.file_visible_card_view)
    void visibleLayoutClicked() {
        mListener.fileViewClicked(getAdapterPosition());
    }

    @OnClick(R.id.printer_file_icon)
    void printerButtonClicked() {
        mListener.printButtonClicked(mFileModel.apiUrl());
    }

    @OnClick(R.id.file_delete_icon)
    void deleteButtonClicked() {
        mListener.deleteButtonClicked(mFileModel.apiUrl(), getAdapterPosition());
    }

    @OnClick(R.id.file_details_icon)
    void onDetailsClicked() {
        Toast.makeText(mView.getContext(), "No file details found", Toast.LENGTH_SHORT).show(); // TODO-low include file details
    }

    @OnClick(R.id.load_file_icon)
    void loadButtonClicked() {
        mListener.loadButtonClicked(mFileModel.apiUrl());
    }

    @OnClick(R.id.file_download_icon)
    void downloadButtonClicked() {
        mListener.downloadButtonClicked(mFileModel.downloadUrl());
    }

    @OnClick(R.id.slice_file_icon)
    void sliceButtonClicked() {mListener.sliceButtonClicked(mFileModel.apiUrl());}

    public void showOnClickView() {
        mOnClickView.setVisibility(View.VISIBLE);
        mView.setBackgroundColor(mBackgroundColor);
    }

    public interface Listener {
        void fileViewClicked(int position);
        void downloadButtonClicked(String downloadUrl);
        void deleteButtonClicked(String apiPath, int adapterPosition);
        void loadButtonClicked(String apiUrl);
        void printButtonClicked(String apiUrl);
        void sliceButtonClicked(String apiUrl);
    }
}
