package com.rek.gplay.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rek.gplay.R;
import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.bean.BannerBean;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RoundLinesIndicator;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_ARTICLE = 1;
    private static final int TYPE_LOAD = 2;
    private List<ArticleBean> articleBeanList;
    private List<BannerBean> bannerBeanList;
    LayoutInflater mLayoutInflater;
    Context mContext;
    OnItemClickListener mOnItemClickListener;

    public HomeAdapter(Context context, List<ArticleBean> articleBeans, List<BannerBean> bannerBeans) {
        mContext = context;
        this.articleBeanList = articleBeans;
        this.bannerBeanList = bannerBeans;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos, String url);
    }

    public void addMoreArticles(List<ArticleBean> moreArticleBeanList) {
        articleBeanList.addAll(moreArticleBeanList);
        this.notifyDataSetChanged();
    }

    //创建ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* 《第一行代码》P117：LayoutInflater.inflate()方法的第三个参数指定为false，表示只让我们在父布局中声明的layout属性生效，
        但不为这个View添加父布局，因为一旦View有了父布局以后，就不能添加到ListView/RecyclerView中了 */
        switch (viewType) {
            case TYPE_BANNER:
                return new MyBannerViewHolder(mLayoutInflater.inflate(R.layout.item_banner, parent, false));
            case TYPE_ARTICLE:
                return new ArticleViewHolder(mLayoutInflater.inflate(R.layout.item_article, parent, false));
            default:
                return new LoadMoreViewHolder(mLayoutInflater.inflate(R.layout.item_load_more, parent, false));
        }
    }

    //绑定数据到ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((MyBannerViewHolder) holder).banner.setAdapter(new BannerImageAdapter<BannerBean>(bannerBeanList) {
                @Override
                public void onBindView(BannerImageHolder holder, BannerBean data, int position, int size) {
                    Glide.with(holder.itemView)
                            .load(data.getImagePath())
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                            .into(holder.imageView);
                    holder.imageView.setOnClickListener(v -> {
                        mOnItemClickListener.onItemClick(v, position, data.getUrl());
                    });
                }
            }).setIndicator(new RoundLinesIndicator(mContext));
        } else if (position != getItemCount() - 1) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            ArticleBean articleBean = articleBeanList.get(position - 1);
            articleViewHolder.tv_title.setText(articleBean.getTitle());
            List<ArticleBean.TagsBean> tags = articleBean.getTags();
            if (tags != null && tags.size() != 0) {
                String name = tags.get(0).getName();
                if (name.equals("项目")) {
                    articleViewHolder.tv_tag_project.setVisibility(View.VISIBLE);
                    articleViewHolder.tv_tag_public.setVisibility(View.GONE);
                } else if (name.equals("公众号")) {
                    articleViewHolder.tv_tag_public.setVisibility(View.VISIBLE);
                    articleViewHolder.tv_tag_project.setVisibility(View.GONE);
                }
            } else {
                articleViewHolder.tv_tag_project.setVisibility(View.GONE);
                articleViewHolder.tv_tag_public.setVisibility(View.GONE);
            }
            if (articleBean.getAuthor().equals("")) {
                articleViewHolder.tv_authorName.setText(articleBean.getShareUser());
            } else {
                articleViewHolder.tv_authorName.setText(articleBean.getAuthor());
            }
            articleViewHolder.tv_chapterName.setText(String.format("%s\\%s", articleBean.getSuperChapterName(), articleBean.getChapterName()));
            String date = articleBean.getNiceDate();
            if (date.length() > 10) {
                date = date.substring(0, 10);
                articleViewHolder.tv_tag_new.setVisibility(View.GONE);
            } else {
                articleViewHolder.tv_tag_new.setVisibility(View.VISIBLE);
            }
            articleViewHolder.tv_dateValue.setText(date);
            articleViewHolder.itemView.setOnClickListener(v -> {
                mOnItemClickListener.onItemClick(v, position, articleBean.getLink());
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == getItemCount() - 1) {
            return TYPE_LOAD;
        }
        return TYPE_ARTICLE;
    }

    @Override
    public int getItemCount() {
        return articleBeanList.size() + 2;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_tag_new, tv_tag_project, tv_tag_public, tv_authorName, tv_chapterName, tv_dateValue;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_article_title);
            tv_tag_new = itemView.findViewById(R.id.tv_tag_new);
            tv_tag_project = itemView.findViewById(R.id.tv_tag_project);
            tv_tag_public = itemView.findViewById(R.id.tv_tag_public);
            tv_authorName = itemView.findViewById(R.id.tv_article_author_shareUser_name);
            tv_chapterName = itemView.findViewById(R.id.tv_article_chapter_name);
            tv_dateValue = itemView.findViewById(R.id.tv_article_date_value);
        }
    }

    static class MyBannerViewHolder extends RecyclerView.ViewHolder {

        Banner<BannerBean, BannerImageAdapter<BannerBean>> banner;

        public MyBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner_home);
        }
    }

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
