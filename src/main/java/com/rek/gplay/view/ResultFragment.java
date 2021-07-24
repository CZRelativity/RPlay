package com.rek.gplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.presenter.ResultContract;
import com.rek.gplay.presenter.ResultPresenter;
import com.rek.gplay.view.adapter.ResultAdapter;

import java.util.List;

public class ResultFragment extends Fragment implements ResultContract.View {

    private final static String TAG = "ResultFragment";

    RecyclerView rv;
    ResultAdapter adapter;

    ResultPresenter presenter;
    String mKey;

    public ResultFragment() {
    }

    public static ResultFragment newInstance(String key) {
        ResultFragment fragment = new ResultFragment();
        fragment.mKey = key;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ResultPresenter(this);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rv = new RecyclerView(getContext());
        initRv();
        presenter.requestData(mKey);
        return rv;
    }

    private void initRv() {

        rv.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false));
        adapter = new ResultAdapter(getContext());
        adapter.setOnItemClickListener((view, pos, url) -> {
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
        });
        rv.setAdapter(adapter);

        RvOnScrollListener mRvOnScrollListener = new RvOnScrollListener(rv);
        mRvOnScrollListener.setOnScrollLoader(() -> {
            if (mKey != null) {
                presenter.requestMoreData(mKey);
            }
        });
        rv.addOnScrollListener(mRvOnScrollListener);
        //rv获取焦点，防止banner自动获取焦点从而不自然地滑动
    }

    public void query(String key) {
        mKey = key;
        presenter.requestData(key);
    }

    @Override
    public void showData(List<ArticleBean> data) {
        adapter.setData(data);
    }

    @Override
    public void showMoreData(List<ArticleBean> moreArticleList) {
        adapter.addData(moreArticleList);
    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showError(String msg) {

    }
}