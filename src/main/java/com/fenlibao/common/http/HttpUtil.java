package com.fenlibao.common.http;

import com.fenlibao.common.base.BaseApplication;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {
    private static final int DEFAULT_TIMEOUT = 10;
    private static ApiService apiService, cacheApiService;
    private static String API_HOST = "";

    /**
     * 初始化获取代理对象
     */
    public static ApiService api() {
        if (apiService == null) {
            synchronized (HttpUtil.class) {
                if (apiService == null) {
                    retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                            .baseUrl(API_HOST)
                            .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                            .client(getOkHttpClient(false))//构建对应的OkHttpClient
                            .build();
                    apiService = retrofit.create(ApiService.class);
                }
            }
        }
        return apiService;
    }

    /**
     * 配置缓存功能的api
     */
    public synchronized static ApiService cacheApi() {
        if (cacheApiService == null) {
//            synchronized (HttpUtil.class) {
//                if (cacheApiService == null) {
            retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(API_HOST)
                    .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                    .client(getOkHttpClient(true))//构建对应的OkHttpClient
                    .build();
            cacheApiService = retrofit.create(ApiService.class);
//                }
//            }
        }
        return cacheApiService;
    }


    /**
     * 初始化okHttp
     */
    private static OkHttpClient getOkHttpClient(boolean isCache) {
        //设置缓存路径 内置存储
        File httpCacheDirectory = new File(BaseApplication.getInstance().getExternalCacheDir(), "responses");
        //设置缓存 10M
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        //日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(isCache ? new CacheInterceptor() : new BaseInterceptor())//添加请求拦截器
                .addInterceptor(loggingInterceptor)//日志拦截
                .build();
    }

    public static void cancelAll() {
        for (Call call : getOkHttpClient(true).dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient(true).dispatcher().runningCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient(false).dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient(false).dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient(false).dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient(false).dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
