package com.rek.gplay.http;

import com.rek.gplay.bean.ArticlePageBean;
import com.rek.gplay.bean.BannerBean;
import com.rek.gplay.bean.ResponseBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiService {

    String BASE_URL = "https://www.wanandroid.com/";

    //io.reactivex:rxJava2, rx:rxJava
    @GET("article/list/{page}/json")
    Observable<ResponseBean<ArticlePageBean>> getArticlePage(@Path("page") int page);

    @GET("banner/json")
    Observable<ResponseBean<List<BannerBean>>> getBanner();

    @POST("article/query/{page}/json")
    Observable<ResponseBean<ArticlePageBean>> getArticlePageBySearchKey(@Path("page") int page,
                                                                        @Field("k") String key);

}
