package com.nairbspace.octoandroid.ui.files;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nairbspace.octoandroid.R;
import com.nairbspace.octoandroid.model.FilesModel;

import butterknife.BindView;
import butterknife.ButterKnife;

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
