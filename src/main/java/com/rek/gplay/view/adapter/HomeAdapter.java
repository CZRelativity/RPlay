package com.rek.gplay.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rek.gplay.R;
import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.bean.BannerBean;
import com.rek.gplay.databinding.ItemArticleBinding;
import com.rek.gplay.view.viewholder.ArticleViewHolder;
import com.rek.gplay.view.viewholder.LoadMoreViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_ARTICLE = 1;
    private static final int TYPE_LOAD = 2;
    private final List<ArticleBean> articleList;
    private final List<BannerBean> bannerList;
    LayoutInflater mLayoutInflater;
    Context mContext;
    OnItemClickListener mOnItemClickListener;

    public HomeAdapter(Context context, List<ArticleBean> articleBeans, List<BannerBean> bannerBeans) {
        mContext = context;
        this.articleList = articleBeans;
        this.bannerList = bannerBeans;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void addMoreData(List<ArticleBean> moreArticleList) {
        articleList.addAll(moreArticleList);
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
//                return new ArticleViewHolder(mLayoutInflater.inflate(R.layout.item_article, parent, false));
                return new ArticleViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            default:
                return new LoadMoreViewHolder(mLayoutInflater.inflate(R.layout.item_load, parent, false));
        }
    }

    //绑定数据到ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((MyBannerViewHolder) holder).banner.setAdapter(new BannerImageAdapter<BannerBean>(bannerList) {
                @Override
                public void onBindView(BannerImageHolder holder, BannerBean data, int position, int size) {
                    Glide.with(holder.itemView)
                            .load(data.getImagePath())
                            .override(holder.imageView.getWidth(), holder.imageView.getHeight())
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                            .into(holder.imageView);
                    holder.imageView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, data.getUrl()));
                }
            }).setIndicator(new CircleIndicator(mContext));
        } else if (position != getItemCount() - 1) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            ArticleBean articleBean = articleList.get(position - 1);
            articleViewHolder.binding.tvArticleTitle.setText(articleBean.getTitle());
            List<ArticleBean.TagsBean> tags = articleBean.getTags();
            if (tags != null && tags.size() != 0) {
                String name = tags.get(0).getName();
                if ("项目".equals(name)) {
                    articleViewHolder.binding.tvTagProject.setVisibility(View.VISIBLE);
                    articleViewHolder.binding.tvTagPublic.setVisibility(View.GONE);
                } else if ("公众号".equals(name)) {
                    articleViewHolder.binding.tvTagPublic.setVisibility(View.VISIBLE);
                    articleViewHolder.binding.tvTagProject.setVisibility(View.GONE);
                }
            } else {
                articleViewHolder.binding.tvTagProject.setVisibility(View.GONE);
                articleViewHolder.binding.tvTagPublic.setVisibility(View.GONE);
            }
            if ("".equals(articleBean.getAuthor())) {
                articleViewHolder.binding.tvArticleAuthorShareUserName.setText(articleBean.getShareUser());
            } else {
                articleViewHolder.binding.tvArticleAuthorShareUserName.setText(articleBean.getAuthor());
            }
            articleViewHolder.binding.tvArticleChapterName.setText(String.format("%s / %s", articleBean.getSuperChapterName(), articleBean.getChapterName()));
            String date = articleBean.getNiceDate();
            if (date.length() > 10) {
                date = date.substring(0, 10);
                articleViewHolder.binding.tvTagNew.setVisibility(View.GONE);
            } else {
                articleViewHolder.binding.tvTagNew.setVisibility(View.VISIBLE);
            }
            articleViewHolder.binding.tvArticleDateValue.setText(date);
            articleViewHolder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, articleBean.getLink()));
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
        return articleList.size() + 2;
    }

    static class MyBannerViewHolder extends RecyclerView.ViewHolder {

        Banner<BannerBean, BannerImageAdapter<BannerBean>> banner;

        public MyBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner_home);
        }
    }
}
