package com.rek.gplay.view.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.rek.gplay.databinding.ItemArticleBinding;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    public ItemArticleBinding binding;

    public ArticleViewHolder(ItemArticleBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

}
