package com.rek.gplay.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.databinding.ItemArticleBinding;
import com.rek.gplay.util.DataParser;
import com.rek.gplay.view.viewholder.ArticleViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<ArticleBean> articleList;
    OnItemClickListener mOnItemClickListener;

    public ResultAdapter(Context mContext) {
        this.mContext = mContext;
        articleList=new ArrayList<>();
    }

    public ResultAdapter(Context context, List<ArticleBean> articleList) {
        mContext = context;
        this.articleList = articleList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setData(List<ArticleBean> articleList){
        this.articleList =articleList;
        notifyDataSetChanged();
    }

    public void addData(List<ArticleBean> moreArticleList) {
        articleList.addAll(moreArticleList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
        ArticleBean articleBean = articleList.get(position);
        articleViewHolder.binding.tvArticleTitle.setText(DataParser.parseTitle(articleBean.getTitle()));
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
            date = DataParser.parseDate(date);
            articleViewHolder.binding.tvTagNew.setVisibility(View.GONE);
        } else {
            articleViewHolder.binding.tvTagNew.setVisibility(View.VISIBLE);
        }
        articleViewHolder.binding.tvArticleDateValue.setText(date);
        articleViewHolder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position, articleBean.getLink()));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
