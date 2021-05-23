package com.rek.gplay.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.databinding.ItemArticleBinding;
import com.rek.gplay.view.viewholder.ArticleViewHolder;

import java.util.Collections;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<ArticleBean> articleBeanList;
    OnItemClickListener mOnItemClickListener;

    public ResultAdapter(Context context, List<ArticleBean> articleBeans) {
        mContext = context;
        articleBeanList = articleBeans;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void addMoreData(List<ArticleBean> moreArticleList) {
        articleBeanList.addAll(moreArticleList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
        ArticleBean articleBean = articleBeanList.get(position - 1);
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

    @Override
    public int getItemCount() {
        return articleBeanList.size();
    }
}
