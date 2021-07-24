package com.rek.gplay.model;

import com.rek.gplay.bean.ArticlePageBean;
import com.rek.gplay.bean.ResponseBean;
import com.rek.gplay.http.HttpManager;
import com.rek.gplay.http.HttpService;

import rx.Observable;

public class ResultModel {

    private final HttpService api;
    public int dataPage;

    public ResultModel() {
        api = HttpManager.getService();
        dataPage = 0;
    }

    public Observable<ResponseBean<ArticlePageBean>> getArticlePageBySearchKey(int page, String key) {
        return api.getArticlePageBySearchKey(page, key);
    }

}
