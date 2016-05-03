package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.net.model.Connect;
import com.nairbspace.octoandroid.net.model.Connection;

public interface GetConnection {

    interface GetConnectionFinishedListener {
        void onLoading();

        void onComplete();

        void onSuccess(Connection connection);

        void onDbSuccess(Connection connection);

        void onFailure();

        void onSslFailure();

        void onApiKeyFailure();
    }

    void getConnectionFromDb(GetConnectionFinishedListener listener);

    void pollConnection(GetConnectionFinishedListener listener);

    void saveConnection(Connection connection);

    void postConnect(Connect connect, GetConnectionFinishedListener listener);

    void unsubscribePollConnection();
}
