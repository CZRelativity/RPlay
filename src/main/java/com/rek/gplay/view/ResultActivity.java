package com.rek.gplay.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;

import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.databinding.ActivityResultBinding;
import com.rek.gplay.presenter.ResultContract;
import com.rek.gplay.presenter.ResultPresenter;
import com.rek.gplay.view.adapter.ResultAdapter;

import java.util.List;

public class ResultActivity extends AppCompatActivity implements ResultContract.View, SwipeRefreshLayout.OnRefreshListener {

    ActivityResultBinding binding;
    SwipeRefreshLayout sr;
    RecyclerView rv;

    ResultAdapter adapter;

    ResultPresenter presenter;
    String mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        String key = getIntent().getStringExtra("key");
        if (key != null) {
            mKey = key;
            presenter.requestData(key);
        }
    }

    void initView() {
        rv = binding.srRvResult.rv;
        sr = binding.srRvResult.sr;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        RvOnScrollListener mRvOnScrollListener = new RvOnScrollListener(rv);
        mRvOnScrollListener.setOnScrollLoader(() -> {
            if (mKey != null) {
                presenter.requestMoreData(mKey);
            }
        });
//        mRvOnScrollListener.setOnScrollUpperShower(mUpperShower);
        rv.addOnScrollListener(mRvOnScrollListener);

        //rv获取焦点，防止banner自动获取焦点从而不自然地滑动

        sr.setOnRefreshListener(this);
    }

    private void loadRV(List<ArticleBean> articleBeanList) {
        adapter = new ResultAdapter(this, articleBeanList);
        adapter.setOnItemClickListener((view, pos, url) -> {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
        });
        rv.setAdapter(adapter);
    }

    private void loadMoreRV(List<ArticleBean> moreArticleList) {
        adapter.addMoreData(moreArticleList);
    }

    @Override
    public void showData(List<ArticleBean> data) {
        loadRV(data);
        if (sr.isRefreshing()) {
            sr.setRefreshing(false);
        }
    }

    @Override
    public void showMoreData(List<ArticleBean> moreArticleList) {
        loadMoreRV(moreArticleList);
    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onRefresh() {
        if (mKey != null) {
            presenter.requestData(mKey);
        }
    }
}