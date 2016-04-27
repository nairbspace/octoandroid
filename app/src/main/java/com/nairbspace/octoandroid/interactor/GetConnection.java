package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.model.Connection;

public interface GetConnection {

    interface GetConnectionFinishedListener {
        void onLoading();

        void onComplete();

        void onSuccess(Connection connection);

        void onFailure();

        void onSslFailure();

        void onApiKeyFailure();
    }

    void getConnection(GetConnectionFinishedListener listener);
}
