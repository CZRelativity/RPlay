package com.rek.gplay.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    public OkHttpClient getOkHttpClient() {

        //log使用拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        //开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等等
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //如果使用HTTPS，我们需要创建SSLSocketFactory，并设置到client

        return new OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                //.sslSocketFactory()
                .readTimeout(10, TimeUnit.SECONDS).build();
    }

    //通过传入可以自定义okHttpClient，否则会使用默认配置
    public Retrofit getRetrofit(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient);
        return builder.build();
    }

}
