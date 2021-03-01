package com.rek.gplay.model;

import com.rek.gplay.bean.ArticlePageBean;
import com.rek.gplay.bean.BannerBean;
import com.rek.gplay.bean.HomeBean;
import com.rek.gplay.bean.ResponseBean;
import com.rek.gplay.http.ApiService;
import com.rek.gplay.http.HttpManager;

import java.util.List;

import rx.Observable;

public class HomeModel {

    private ApiService apiService;
    public int articlePage;

    //考虑到连续获取页面
    public HomeModel() {
        HttpManager manager = new HttpManager();
        this.apiService = manager.getRetrofit(manager.getOkHttpClient()).create(ApiService.class);
        articlePage = 0;
    }

    public Observable<ResponseBean<ArticlePageBean>> getArticlePage(int page) {
        return apiService.getArticlePage(page);
    }

    public Observable<ResponseBean<List<BannerBean>>> getBanner() {
        return apiService.getBanner();
    }

//    @Override
//    public void getDatas(Callback<ResponseBean<ArticlePageBean>> callback) {
//
////        Call<ResponseBean<ArticlePageBean>> call = apiService.getArticlePage(0);
////        call.enqueue(callback);
//
//    }

}
