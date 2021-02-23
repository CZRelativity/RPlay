package com.rek.gplay.bean;

import java.util.List;

public class HomeBean {

    private List<BannerBean> homeBannerBeanList;
    private List<ArticleBean> homeArticleBeanList;

    public List<BannerBean> getHomeBannerBeanList() {
        return homeBannerBeanList;
    }

    public void setHomeBannerBeanList(List<BannerBean> homeBannerBeanList) {
        this.homeBannerBeanList = homeBannerBeanList;
    }

    public List<ArticleBean> getHomeArticleBeanList() {
        return homeArticleBeanList;
    }

    public void setHomeArticleBeanList(List<ArticleBean> homeArticleBeanList) {
        this.homeArticleBeanList = homeArticleBeanList;
    }
}
