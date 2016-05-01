package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.net.Connect;
import com.nairbspace.octoandroid.net.Connection;

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

    void syncConnection(GetConnectionFinishedListener listener);

    void saveConnection(Connection connection);

    void postConnect(Connect connect);
}
