package com.rek.gplay.bean;

import java.util.List;

public class HomeBean {

    private final List<BannerBean> homeBannerBeanList;
    private final List<ArticleBean> homeArticleBeanList;

    public HomeBean(List<ArticleBean> articleBeans,List<BannerBean> bannerBeans){
        this.homeArticleBeanList=articleBeans;
        this.homeBannerBeanList=bannerBeans;
    }

    public List<BannerBean> getHomeBannerBeanList() {
        return homeBannerBeanList;
    }

    public List<ArticleBean> getHomeArticleBeanList() {
        return homeArticleBeanList;
    }
}
