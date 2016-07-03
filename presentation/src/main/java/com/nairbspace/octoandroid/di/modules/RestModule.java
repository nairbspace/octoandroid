package com.nairbspace.octoandroid.di.modules;

import com.google.gson.Gson;
import com.nairbspace.octoandroid.data.net.ApiManager;
import com.nairbspace.octoandroid.data.net.ApiManagerImpl;
import com.nairbspace.octoandroid.data.net.OctoApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RestModule {
    private static final String DUMMY_URL = "http://localhost"; // Will be changed at runtime

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    RxJavaCallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofitBuilder(GsonConverterFactory converterFactory,
                                            @Named("rest") OkHttpClient okHttpClient,
                                            RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(converterFactory)
                .client(okHttpClient)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .baseUrl(DUMMY_URL)
                .build();
    }

    @Provides
    @Singleton
    OctoApi provideOctoprintApi(Retrofit retrofit) {
        return retrofit.create(OctoApi.class);
    }

    @Provides
    @Singleton
    ApiManager provideApiManager(ApiManagerImpl apiManager) {
        return apiManager;
    }
}
