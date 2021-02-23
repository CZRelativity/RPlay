package com.rek.gplay.http;

import com.rek.gplay.bean.ArticlePageBean;
import com.rek.gplay.bean.BannerBean;
import com.rek.gplay.bean.ResponseBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiService {

    String BASE_URL = "https://www.wanandroid.com/";

    //io.reactivex:rxJava2, rx:rxJava
    @GET("article/list/{pageNum}/json")
    Observable<ResponseBean<ArticlePageBean>> getArticlePage(@Path("pageNum") int page);

    @GET("banner/json")
    Observable<ResponseBean<List<BannerBean>>> getBanner();

}
