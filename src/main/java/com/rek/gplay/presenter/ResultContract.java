package com.rek.gplay.presenter;

import com.rek.gplay.base.BasePresenter;
import com.rek.gplay.base.BaseView;
import com.rek.gplay.bean.ArticleBean;

import java.util.List;

public interface ResultContract {

    interface View extends BaseView<List<ArticleBean>> {
        void showMoreData(List<ArticleBean> moreArticleList);
    }

    interface Presenter extends BasePresenter {
        void requestData(String key);
        void requestMoreData(String key);
    }

}
