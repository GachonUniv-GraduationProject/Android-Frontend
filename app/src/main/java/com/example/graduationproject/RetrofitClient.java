package com.example.graduationproject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class for generating retrofit instance
 * */
public class RetrofitClient {
    /**
     * URL of web server
     * */
    private static final String BASE_URL = "https://gpdevapp.com/";
    /**
     * Get the RetrofitService instance
     * */
    public static RetrofitService getRetrofitService() { return getInstance().create(RetrofitService.class); }

    /**
     * Create and set the Retrofit instance
     * */
    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();

        // Set the HTTP logging feature
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientBuilder.addInterceptor(loggingInterceptor);

        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(clientBuilder.build()).build();
    }
}
