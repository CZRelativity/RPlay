package com.rek.gplay.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.bean.BannerBean;
import com.rek.gplay.bean.HomeBean;
import com.rek.gplay.databinding.FragmentHomeBinding;
import com.rek.gplay.presenter.HomeContract;
import com.rek.gplay.presenter.HomePresenter;
import com.rek.gplay.view.adapter.HomeAdapter;

import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    FragmentHomeBinding binding;
    HomePresenter homePresenter;
    HomeAdapter homeAdapter;

    //    RvOnScrollListener.OnScrollToolBarShower toolBarShower;
    RvOnScrollListener.OnScrollUpperShower mUpperShower;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(RvOnScrollListener.OnScrollUpperShower upperShower) {
        HomeFragment fragment = new HomeFragment();
        fragment.mUpperShower = upperShower;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePresenter = new HomePresenter(this);
    }

    //onCreateView后于onCreate，两个回调都可以拿到Bundle，onCreateView额外拿到父容器和LayoutInflater，正好配合ViewBinding
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //fragment使用getContext方法获取宿主的Context
    @Override
    public void initView() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvHome.setLayoutManager(mLinearLayoutManager);
        RvOnScrollListener mRvOnScrollListener = new RvOnScrollListener(binding.rvHome);
        mRvOnScrollListener.setOnScrollLoader(() -> homePresenter.requestMoreDatas());
//        mRvOnScrollListener.setOnScrollUpperShower(mUpperShower);
        binding.rvHome.addOnScrollListener(mRvOnScrollListener);

//        //防止banner自动获取焦点从而不自然地滑动
//        binding.rvHome.setFocusable(true);
//        binding.rvHome.setFocusableInTouchMode(true);

        binding.srLayoutHome.setOnRefreshListener(this);
        requestData();
    }

    public void scroll2Top() {
        binding.rvHome.smoothScrollToPosition(0);
    }

    private void loadRV(List<ArticleBean> articleBeanList, List<BannerBean> bannerBeanList) {
        homeAdapter = new HomeAdapter(getContext(), articleBeanList, bannerBeanList);
        homeAdapter.setOnItemClickListener((view, pos, url) -> {
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
        });
        binding.rvHome.setAdapter(homeAdapter);
    }

    private void loadMoreRV(List<ArticleBean> moreArticleList) {
        homeAdapter.addMoreArticles(moreArticleList);
    }

    @Override
    public void requestData() {
        homePresenter.requestData();
    }

    @Override
    public void showData(HomeBean homeBean) {
        loadRV(homeBean.getHomeArticleBeanList(), homeBean.getHomeBannerBeanList());
        if (binding.srLayoutHome.isRefreshing()) {
            binding.srLayoutHome.setRefreshing(false);
        }
    }

    @Override
    public void showMoreData(List<ArticleBean> moreArticleList) {
        loadMoreRV(moreArticleList);
    }

    @Override
    public void showNoData() {
        Toast.makeText(getContext(), "No data received, please try again later! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //OnRefreshListener
    @Override
    public void onRefresh() {
        requestData();
    }

}
