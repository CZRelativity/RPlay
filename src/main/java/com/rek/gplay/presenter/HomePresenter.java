package com.rek.gplay.presenter;

import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.bean.ArticlePageBean;
import com.rek.gplay.bean.BannerBean;
import com.rek.gplay.bean.HomeBean;
import com.rek.gplay.bean.ResponseBean;
import com.rek.gplay.model.HomeModel;

import java.util.List;
import java.util.function.BiFunction;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    //presenter实际上是通过View的接口与View交互的，如果直接实例化View本身，解耦性就没这么好了
    private final HomeContract.View homeView;
    private final HomeModel homeModel;

    //不写Contract的话自己要多写好多次泛型- -
    public HomePresenter(HomeContract.View view) {
        homeView = view;
        homeModel = new HomeModel();
    }

    public void requestData() {

        Observable.zip(
                homeModel.getArticlePage(0),
                homeModel.getBanner(),
                (articlePageResponse, bannerResponse) ->
                        new HomeBean(articlePageResponse.getData().getArticles(),bannerResponse.getData()))
                //把这句前面的操作放到子线程去做
                .subscribeOn(Schedulers.io())
                //然后这句下面的操作转到主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        homeView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(HomeBean homeBean) {
                        if(homeBean!=null) {
                            homeView.showData(homeBean);
                            homeModel.dataPage = 0;
                        }else {
                            homeView.showNoData();
                        }
                    }
                });

    }

    @Override
    public void requestMoreData() {

        homeModel.dataPage++;
        homeModel.getArticlePage(homeModel.dataPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBean<ArticlePageBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBean<ArticlePageBean> articlePageBean) {
                        if (articlePageBean != null) {
                            List<ArticleBean> articleBeanList = articlePageBean.getData().getArticles();
                            if (articleBeanList != null) {
                                homeView.showMoreData(articleBeanList);
                                return;
                            }
                        }
                        homeModel.dataPage--;
                        homeView.showNoData();
                    }
                });

    }

//    @Override
//    public void requestDatas() {
//        homeModel.getDatas(new Callback<ResponseBean<ArticlePageBean>>() {
//            @Override
//            public void onResponse(Call<ResponseBean<ArticlePageBean>> call, Response<ResponseBean<ArticlePageBean>> response) {
//                ResponseBean<ArticlePageBean> body = response.body();
//                if (body != null) {
//                    List<ArticleBean> articles = body.getData().getArticles();
//                    homeView.showDatas(articles);
//                } else {
//                    homeView.showNoData();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBean<ArticlePageBean>> call, Throwable t) {
//                homeView.showError(t.getMessage());
//            }
//        });
//    }
}
