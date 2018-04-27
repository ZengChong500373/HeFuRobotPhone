package com.hefu.robotphone.http;

import java.io.File;
import java.util.concurrent.TimeUnit;

import hefu.robotphone.sdk.RobotSdk;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class Network {
    private static OkHttpClient mOkHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static HttpMethods methods;
    private static YSMethods  ysMethods;

    private static String BASEURL="http://api.fir.im/apps/latest/";
    private static String YS_BASEURL="https://open.ys7.com/";
    public static void initOkhttp() {
        if (mOkHttpClient == null) {
            synchronized (Network.class) {
                if (mOkHttpClient == null) {
                    File mFile = new File(RobotSdk.getContext().getCacheDir(), "cache");
                    Cache mCache = new Cache(mFile, 1024 * 1024 * 200);

                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(mCache)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    public static HttpMethods getMethods() {
        initOkhttp();
        if (methods == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mOkHttpClient)
                    .baseUrl(BASEURL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)

                    .build();
            methods = retrofit.create(HttpMethods.class);
        }
        return methods;
    }

    public static YSMethods getYSMethods() {
        initOkhttp();
        if (ysMethods == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mOkHttpClient)
                    .baseUrl(YS_BASEURL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)

                    .build();
            ysMethods = retrofit.create(YSMethods.class);
        }
        return ysMethods;
    }
}
