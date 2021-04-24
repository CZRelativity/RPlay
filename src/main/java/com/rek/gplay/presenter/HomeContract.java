package com.rek.gplay.presenter;

import com.rek.gplay.base.BasePresenter;
import com.rek.gplay.base.BaseView;
import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.bean.HomeBean;

import java.util.List;

public interface HomeContract {

    interface View extends BaseView<HomeBean> {
        void showMoreData(List<ArticleBean> moreArticleList);
    }

    interface Presenter extends BasePresenter {
        void requestMoreDatas();
    }
}
