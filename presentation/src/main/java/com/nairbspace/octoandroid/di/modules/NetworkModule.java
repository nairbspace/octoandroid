package com.nairbspace.octoandroid.di.modules;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.nairbspace.octoandroid.BuildConfig;
import com.nairbspace.octoandroid.data.mapper.AutoValueTypeAdapterFactory;
import com.nairbspace.octoandroid.data.net.OctoInterceptor;
import com.nairbspace.octoandroid.data.net.WebsocketInterceptor;
import com.nairbspace.octoandroid.data.net.stream.WebcamManager;
import com.nairbspace.octoandroid.data.net.stream.WebcamManagerImpl;

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
    Interceptor provideInterceptor(OctoInterceptor octoInterceptor) {
        return octoInterceptor;
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
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
//        .cache(cache) // // OctoPrint API does not implement caching.
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    @Named("websocket")
    Interceptor provideWebsocketInterceptor(WebsocketInterceptor websocketInterceptor) {
        return websocketInterceptor;
    }

    @Provides
    @Singleton
    @Named("websocket")
    OkHttpClient provideWebsocketOkHttpClient(@Named("websocket") Interceptor websocketInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(websocketInterceptor)
                .build();
    }

    @Provides
    @Singleton
    TypeAdapterFactory provideAutoValueTypeAdapterFactory() {
        return new AutoValueTypeAdapterFactory();
    }

    @Provides
    @Singleton
    Gson provideGson(TypeAdapterFactory typeAdapterFactory) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(typeAdapterFactory);
//        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    WebcamManager provideWebcamManager(WebcamManagerImpl webcamManager) {
        return webcamManager;
    }
}
