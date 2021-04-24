package com.rek.gplay.presenter;

import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.bean.ArticlePageBean;
import com.rek.gplay.bean.BannerBean;
import com.rek.gplay.bean.HomeBean;
import com.rek.gplay.bean.ResponseBean;
import com.rek.gplay.model.HomeModel;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    //presenter实际上是通过View的接口与View交互的，如果直接实例化View本身，解耦性就没这么好了
    private HomeContract.View homeView;
    private HomeModel homeModel;
    private HomeBean homeBean;

    //不写Contract的话自己要多写好多次泛型- -
    public HomePresenter(HomeContract.View view) {
        homeView = view;
        homeModel = new HomeModel();
    }

    @Override
    public void requestData() {

        homeBean = new HomeBean();

        homeModel.getArticlePage(0)
                //把这句前面的操作放到子线程去做
                .subscribeOn(Schedulers.io())

                //然后这句下面的操作转到主线程
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Subscriber<ResponseBean<ArticlePageBean>>() {
                    //在订阅之前要做的事情
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        homeView.showError(e.getMessage());
                    }

                    //处理数据
                    @Override
                    public void onNext(ResponseBean<ArticlePageBean> articlePageBean) {
                        if (articlePageBean != null) {
                            homeBean.setHomeArticleBeanList(articlePageBean.getData().getArticles());
                            if (homeBean.getHomeBannerBeanList() != null) {
                                homeModel.articlePage = 0;
                                homeView.showData(homeBean);
                            }
                        } else {
                            homeView.showNoData();
                        }
                    }
                });

        homeModel.getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBean<List<BannerBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBean<List<BannerBean>> bannerBeanList) {
                        if (bannerBeanList != null) {
                            homeBean.setHomeBannerBeanList(bannerBeanList.getData());
                            if (homeBean.getHomeArticleBeanList() != null) {
                                homeView.showData(homeBean);
                            }
                        } else {
                            homeView.showNoData();
                        }
                    }
                });
    }

    @Override
    public void requestMoreDatas() {

        homeModel.articlePage++;
        homeModel.getArticlePage(homeModel.articlePage)
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
                            } else {
                                homeModel.articlePage--;
                                homeView.showNoData();
                            }
                        }
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
