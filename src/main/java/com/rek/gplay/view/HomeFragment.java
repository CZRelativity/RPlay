package com.rek.gplay.view;

import android.content.Intent;
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
    RecyclerView rv;
    SwipeRefreshLayout sr;

    HomePresenter presenter;
    HomeAdapter adapter;

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
        presenter = new HomePresenter(this);
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
    public void initView() {
        sr = binding.srRvHome.sr;
        rv = binding.srRvHome.rv;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        RvOnScrollListener mRvOnScrollListener = new RvOnScrollListener(rv);
        mRvOnScrollListener.setOnScrollLoader(() -> presenter.requestMoreData());
//        mRvOnScrollListener.setOnScrollUpperShower(mUpperShower);
        rv.addOnScrollListener(mRvOnScrollListener);

        //rv获取焦点，防止banner自动获取焦点从而不自然地滑动

        sr.setOnRefreshListener(this);
        presenter.requestData();
    }

    public void scroll2Top() {
        rv.smoothScrollToPosition(0);
    }

    private void loadRV(List<ArticleBean> articleBeanList, List<BannerBean> bannerBeanList) {
        adapter = new HomeAdapter(getContext(), articleBeanList, bannerBeanList);
        adapter.setOnItemClickListener((view, pos, url) -> {
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
        });
        rv.setAdapter(adapter);
    }

    private void loadMoreRV(List<ArticleBean> moreArticleList) {
        adapter.addMoreData(moreArticleList);
    }

    @Override
    public void showData(HomeBean homeBean) {
        loadRV(homeBean.getHomeArticleBeanList(), homeBean.getHomeBannerBeanList());
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
        Toast.makeText(getContext(), "No data received, please try again later! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //OnRefreshListener
    @Override
    public void onRefresh() {
        presenter.requestData();
    }

}
