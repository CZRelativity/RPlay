package com.rek.gplay.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rek.gplay.R;
import com.rek.gplay.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RvOnScrollListener.OnScrollUpperShower,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final String HOME_KEY = "home";
    private static final String SYSTEM_KEY = "system";
    private static final String PROJECT_KEY = "project";

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private SystemFragment systemFragment;
    private ProjectFragment projectFragment;
    private String fragmentKey = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search_icon) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (fragmentKey != null) {
            outState.putString("fragmentKey", fragmentKey);
        }
        super.onSaveInstanceState(outState);
    }

    private void initView(Bundle savedInstanceState) {

        setSupportActionBar(binding.mainTb.tb);

        binding.btnUpMain.setOnClickListener(this);
        binding.btNvMain.setOnNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HOME_KEY);
            systemFragment = (SystemFragment) fragmentManager.findFragmentByTag(SYSTEM_KEY);
            projectFragment = (ProjectFragment) fragmentManager.findFragmentByTag(PROJECT_KEY);
            showFragmentByKey(savedInstanceState.getString("fragmentKey", HOME_KEY));
        } else {
            showFragmentByKey(HOME_KEY);
        }

        //getHeaderView需要传入一个下标，如果只有1个就从0开始算就可以了
        View headerView = binding.nvMain.getHeaderView(0);
        headerView.setOnClickListener(v -> Toast.makeText(MainActivity.this, "headerView", Toast.LENGTH_SHORT).show());

        binding.nvMain.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_app_update:
                    Toast.makeText(MainActivity.this, "点击了更新", Toast.LENGTH_SHORT).show();
                case R.id.menu_message:
                    Toast.makeText(MainActivity.this, "点击了消息", Toast.LENGTH_SHORT).show();
                case R.id.menu_settings:
                    Toast.makeText(MainActivity.this, "点击了设置", Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        /* ActionBarDrawerToggle实际继承了DrawerLayout.DrawerListener,
        所以可以直接传入DrawerListener */
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerHome, binding.mainTb.tb, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        binding.drawerHome.addDrawerListener(actionBarDrawerToggle);

    }

    @Override
    public void onClick(View v) {
        if (fragmentKey.equals(HOME_KEY)) {
            homeFragment.scroll2Top();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                showFragmentByKey(HOME_KEY);
                break;
            case R.id.menu_system:
                showFragmentByKey(SYSTEM_KEY);
                break;
            case R.id.menu_project:
                showFragmentByKey(PROJECT_KEY);
                break;
            default:
                break;
        }
        return true;
    }

    private void showFragmentByKey(String key) {

        if (key.equals(fragmentKey)) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (systemFragment != null) {
            transaction.hide(systemFragment);
        }
        if (projectFragment != null) {
            transaction.hide(projectFragment);
        }
        ActionBar actionBar = getSupportActionBar();
        switch (key) {
            case HOME_KEY:
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance(this);
                    transaction.add(R.id.frame_main, homeFragment, HOME_KEY);
                } else {
                    transaction.show(homeFragment);
                }
                if (actionBar != null) {
                    actionBar.setTitle("首页");
                }
                break;
            case SYSTEM_KEY:
                if (systemFragment == null) {
                    systemFragment = SystemFragment.newInstance();
                    transaction.add(R.id.frame_main, systemFragment, SYSTEM_KEY);
                } else {
                    transaction.show(systemFragment);
                }
                if (actionBar != null) {
                    actionBar.setTitle("体系");
                }
                break;
            case PROJECT_KEY:
                if (projectFragment == null) {
                    projectFragment = ProjectFragment.newInstance();
                    transaction.add(R.id.frame_main, projectFragment, PROJECT_KEY);
                } else {
                    transaction.show(projectFragment);
                }
                if (actionBar != null) {
                    actionBar.setTitle("项目");
                }
                break;
        }

        fragmentKey = key;
        transaction.commit();
    }

    @Override
    public void showUpper() {
        if (binding.btnUpMain.getVisibility() == View.GONE) {
            binding.btnUpMain.setVisibility(View.VISIBLE);
//            ViewPropertyAnimator animator = binding.btnUpMain.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                    binding.btnUpMain.setVisibility(View.VISIBLE);
//                }
//            });
//            animator.start();
        }
    }

    @Override
    public void hideUpper() {
        if (binding.btnUpMain.getVisibility() == View.VISIBLE) {
            binding.btnUpMain.setVisibility(View.GONE);
//            ViewPropertyAnimator animator = binding.btnUpMain.animate().alpha(1f).setDuration(200).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    binding.btnUpMain.setVisibility(View.GONE);
//                }
//            });
//            animator.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
