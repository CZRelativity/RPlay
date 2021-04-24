package com.rek.gplay.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rek.gplay.R;
import com.rek.gplay.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RvOnScrollListener.OnScrollUpperShower, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private SystemFragment systemFragment;
    private ProjectFragment projectFragment;
    private final static int FRAG_HOME = 0;
    private final static int FRAG_SYSTEM = 1;
    private final static int FRAG_PROJECT = 2;
    private int fragmentId = -1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {

        setSupportActionBar(binding.appbarMain.tb);

        binding.btnUpMain.setOnClickListener(this);
        binding.btNvMain.setOnNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        showFragmentById(FRAG_HOME);

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
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerHome, binding.appbarMain.tb, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        binding.drawerHome.addDrawerListener(actionBarDrawerToggle);

    }

    @Override
    public void onClick(View v) {
        if (fragmentId == FRAG_HOME) {
            homeFragment.scroll2Top();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                showFragmentById(FRAG_HOME);
                break;
            case R.id.menu_system:
                showFragmentById(FRAG_SYSTEM);
                break;
            case R.id.menu_project:
                showFragmentById(FRAG_PROJECT);
                break;
            default:
                break;
        }
        return true;
    }

    private void showFragmentById(int id) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (id == fragmentId) {
            return;
        } else {
            hideFragmentById(fragmentId);
            fragmentId = id;
        }
        switch (id) {
            case FRAG_HOME:
                if (homeFragment == null) {
                    homeFragment = HomeFragment.newInstance(this);
                    transaction.add(R.id.frame_main, homeFragment, "home");
                } else {
                    transaction.show(homeFragment);
                }
                getSupportActionBar().setTitle("首页");
                break;
            case FRAG_SYSTEM:
                if (systemFragment == null) {
                    systemFragment = SystemFragment.newInstance();
                    transaction.add(R.id.frame_main, systemFragment, "system");
                } else {
                    transaction.show(systemFragment);
                }
                getSupportActionBar().setTitle("体系");
                break;
            case FRAG_PROJECT:
                if (projectFragment == null) {
                    projectFragment = ProjectFragment.newInstance();
                    transaction.add(R.id.frame_main, projectFragment, "project");
                } else {
                    transaction.show(projectFragment);
                }
                getSupportActionBar().setTitle("项目");
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragmentById(int id) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (id) {
            case FRAG_HOME:
                transaction.hide(homeFragment);
                break;
            case FRAG_SYSTEM:
                transaction.hide(systemFragment);
                break;
            case FRAG_PROJECT:
                transaction.hide(projectFragment);
                break;
            default:
                break;
        }
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
