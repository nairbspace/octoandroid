package com.nairbspace.octoandroid.interactor;

import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;

public interface GetConnection {

    interface GetConnectionFinishedListener {
        void onLoading();

        void onComplete();

        void onPostComplete(ConnectEntity connectEntity);

        void onSuccess(ConnectionEntity connectionEntity);

        void onNoActivePrinter();

        void onDbSuccess(ConnectionEntity connectionEntity);

        void onDbFailure();

        void onFailure();

        void onSslFailure();

        void onApiKeyFailure();
    }

    void getConnection(GetConnectionFinishedListener listener);

    void getConnectionFromDb(GetConnectionFinishedListener listener);

    void pollConnection(GetConnectionFinishedListener listener);

    void saveConnection(ConnectionEntity connectionEntity);

    void postConnect(ConnectEntity connectEntity, GetConnectionFinishedListener listener);

    void unsubscribePollConnection();
}
