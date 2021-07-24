package com.rek.gplay.model;

import com.rek.gplay.bean.ArticlePageBean;
import com.rek.gplay.bean.BannerBean;
import com.rek.gplay.bean.ResponseBean;
import com.rek.gplay.http.HttpService;
import com.rek.gplay.http.HttpManager;

import java.util.List;

import rx.Observable;

public class HomeModel {

    private final HttpService api;
    public int dataPage;

    //考虑到连续获取页面
    public HomeModel() {
        this.api = HttpManager.getService();
        dataPage = 0;
    }

    public Observable<ResponseBean<ArticlePageBean>> getArticlePage(int page) {
        return api.getArticlePage(page);
    }

    public Observable<ResponseBean<List<BannerBean>>> getBanner() {
        return api.getBanner();
    }

//    @Override
//    public void getDatas(Callback<ResponseBean<ArticlePageBean>> callback) {
//
////        Call<ResponseBean<ArticlePageBean>> call = apiService.getArticlePage(0);
////        call.enqueue(callback);
//
//    }

}
