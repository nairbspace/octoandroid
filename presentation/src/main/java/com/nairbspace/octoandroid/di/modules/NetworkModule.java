package com.nairbspace.octoandroid.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nairbspace.octoandroid.data.entity.AutoValueTypeAdapterFactory;
import com.nairbspace.octoandroid.data.net.rest.OctoApi;
import com.nairbspace.octoandroid.data.net.rest.OctoApiImpl;
import com.nairbspace.octoandroid.data.net.rest.OctoInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    private static final String DUMMY_URL = "http://localhost"; // Will be changed at runtime

    @Singleton
    @Provides
    OctoInterceptor provideInterceptor() {
        return new OctoInterceptor();
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(OctoInterceptor interceptor, HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor);
//                .cache(cache) // TODO: implement cache
        return builder.build();
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
//        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES); // TODO: Change field naming policies
        return gsonBuilder.create();
    }

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
    Retrofit.Builder provideRetrofitBuilder(GsonConverterFactory converterFactory,
                                            OkHttpClient okHttpClient,
                                            RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(converterFactory)
                .client(okHttpClient)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .baseUrl(DUMMY_URL);
    }

    @Provides
    @Singleton
    OctoApi provideOctoprintApi(Retrofit.Builder builder) {
        return builder.build().create(OctoApi.class);
    }

    @Provides
    @Singleton
    OctoApiImpl provideApiImpl(OctoApi api) {
        return new OctoApiImpl(api);
    }
}
