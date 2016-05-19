package com.nairbspace.octoandroid.di.modules;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nairbspace.octoandroid.data.disk.DbHelper;
import com.nairbspace.octoandroid.data.mapper.AutoValueTypeAdapterFactory;
import com.nairbspace.octoandroid.data.net.OctoInterceptor;
import com.nairbspace.octoandroid.data.net.WebsocketInterceptor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    @Named("rest")
    Interceptor provideInterceptor(DbHelper dbHelper) {
        return new OctoInterceptor(dbHelper);
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    @Named("rest")
    OkHttpClient provideRestOkHttpClient(@Named("rest") Interceptor interceptor,
                                     HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
//                .cache(cache) // TODO: implement cache
                .build();
    }

    @Provides
    @Singleton
    @Named("websocket")
    Interceptor provideWebsocketInterceptor(DbHelper dbHelper) {
        return new WebsocketInterceptor(dbHelper);
    }

    @Provides
    @Singleton
    @Named("websocket")
    OkHttpClient provideRegularOkHttpClient(@Named("websocket") Interceptor websocketInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(websocketInterceptor)
                .build();
    }

    @Provides
    @Singleton
    AutoValueTypeAdapterFactory provideAutoValueTypeAdapterFactory() {
        return new AutoValueTypeAdapterFactory();
    }

    @Provides
    @Singleton
    Gson provideGson(AutoValueTypeAdapterFactory typeAdapterFactory) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(typeAdapterFactory);
//        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES); // TODO-LOW: Change field naming policies
        return gsonBuilder.create();
    }



    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
