package com.rek.gplay.presenter;

import com.rek.gplay.bean.ArticlePageBean;
import com.rek.gplay.bean.ResponseBean;
import com.rek.gplay.model.ResultModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ResultPresenter implements ResultContract.Presenter {

    private final ResultContract.View resultView;
    private final ResultModel resultModel;

    public ResultPresenter(ResultContract.View view) {
        resultView = view;
        resultModel = new ResultModel();
    }


    @Override
    public void requestData(String key) {
        resultModel.getArticlePageBySearchKey(0, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBean<ArticlePageBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        resultView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBean<ArticlePageBean> articlePageBean) {
                        if (articlePageBean != null) {
                            resultView.showData(articlePageBean.getData().getArticles());
                            resultModel.dataPage = 0;
                        } else {
                            resultView.showNoData();
                        }
                    }
                });
    }

    @Override
    public void requestMoreData(String key) {
        resultModel.dataPage++;
        resultModel.getArticlePageBySearchKey(resultModel.dataPage, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBean<ArticlePageBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        resultView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBean<ArticlePageBean> articlePageBean) {
                        if (articlePageBean != null) {
                            resultView.showMoreData(articlePageBean.getData().getArticles());
                        } else {
                            resultModel.dataPage--;
                            resultView.showNoData();
                        }
                    }
                });
    }
}
