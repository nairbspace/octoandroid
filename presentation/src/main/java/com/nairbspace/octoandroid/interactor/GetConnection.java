package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.data.net.rest.model.Connect;
import com.nairbspace.octoandroid.data.net.rest.model.Connection;

public interface GetConnection {

    interface GetConnectionFinishedListener {
        void onLoading();

        void onComplete();

        void onPostComplete(Connect connect);

        void onSuccess(Connection connection);

        void onNoActivePrinter();

        void onDbSuccess(Connection connection);

        void onDbFailure();

        void onFailure();

        void onSslFailure();

        void onApiKeyFailure();
    }

    void getConnection(GetConnectionFinishedListener listener);

    void getConnectionFromDb(GetConnectionFinishedListener listener);

    void pollConnection(GetConnectionFinishedListener listener);

    void saveConnection(Connection connection);

    void postConnect(Connect connect, GetConnectionFinishedListener listener);

    void unsubscribePollConnection();
}
