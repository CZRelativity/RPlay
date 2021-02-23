package com.rek.gplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.rek.gplay.R;
import com.rek.gplay.bean.ArticleBean;
import com.rek.gplay.bean.BannerBean;
import com.rek.gplay.bean.HomeBean;
import com.rek.gplay.presenter.HomeContract;
import com.rek.gplay.presenter.HomePresenter;
import com.rek.gplay.view.adapter.HomeAdapter;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private static final String TAG = "HomeActivity";

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    RecyclerView mRv;
    HomeAdapter mAdapter;

    HomePresenter homePresenter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        homePresenter = new HomePresenter(this);
        initData();

        //getHeaderView需要传入一个下标，如果只有1个就从0开始算就可以了
        View headerView = mNavigationView.getHeaderView(0);
        headerView.setOnClickListener(v -> Toast.makeText(HomeActivity.this, "headerView", Toast.LENGTH_SHORT).show());

        mNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_app_update:
                    Toast.makeText(HomeActivity.this, "点击了更新", Toast.LENGTH_SHORT).show();
                case R.id.menu_message:
                    Toast.makeText(HomeActivity.this, "点击了消息", Toast.LENGTH_SHORT).show();
                case R.id.menu_settings:
                    Toast.makeText(HomeActivity.this, "点击了设置", Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        /* ActionBarDrawerToggle实际继承了DrawerLayout.DrawerListener,
        所以可以直接传入DrawerListener */
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout_home);
        mNavigationView = findViewById(R.id.navigation_view_home);
        mToolbar = findViewById(R.id.toolbar_home);
        mRv = findViewById(R.id.rv_home);
        setSupportActionBar(mToolbar);
    }

    private void initData() {
        homePresenter.requestDatas();
    }

    private void initRV(List<ArticleBean> articleBeanList, List<BannerBean> bannerBeanList) {
        mRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mAdapter = new HomeAdapter(this, articleBeanList, bannerBeanList);
        mAdapter.setOnItemClickListener((view, pos, url) -> {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra("URL", url);
            startActivity(intent);
        });
        mRv.setAdapter(mAdapter);
    }

    @Override
    public void showData(HomeBean homeBean) {
        initRV(homeBean.getHomeArticleBeanList(), homeBean.getHomeBannerBeanList());
    }

    @Override
    public void showNoData() {
        Toast.makeText(this, "No data received, please try again later! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String meg) {
        Toast.makeText(this, meg, Toast.LENGTH_SHORT).show();
    }

}
