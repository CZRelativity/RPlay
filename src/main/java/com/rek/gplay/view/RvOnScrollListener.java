package com.rek.gplay.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//自定义OnScrollListener实现下拉加载更多
class RvOnScrollListener extends RecyclerView.OnScrollListener {

    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
    OnScrollLoader onScrollLoader;
    OnScrollUpperShower onScrollUpperShower;

    int lastVisibleItemPos;

    public RvOnScrollListener(RecyclerView rv) {
        this.rv = rv;
        linearLayoutManager = (LinearLayoutManager) rv.getLayoutManager();
    }

    public void setOnScrollLoader(OnScrollLoader onScrollLoader) {
        this.onScrollLoader = onScrollLoader;
    }

    public void setOnScrollUpperShower(OnScrollUpperShower onScrollUpperShower) {
        this.onScrollUpperShower = onScrollUpperShower;
    }

    /* 滚动状态变化
     * STATE_IDLE为无滑动，STATE_SETTLING滑动中 */
    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (onScrollLoader != null && newState == RecyclerView.SCROLL_STATE_IDLE
                && lastVisibleItemPos == linearLayoutManager.getItemCount() - 1) {
            onScrollLoader.loadMore();
        }
    }

    /* 在滚动中
     * 当dy>0时表示手正在向上滑动，然后屏幕向下走
     * 当dy<=0时表示停止或手向下滑动，然后屏幕向上走 */
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
//        if (dy < 0) {
//            if (onScrollUpperShower != null) {
//                onScrollUpperShower.showUpper();
//            }
//        }
        lastVisibleItemPos = linearLayoutManager.findLastVisibleItemPosition();
    }

    //因为需要很多主类属性而不方便在子类里面实现的方法，可以写个接口到主类里面去实现！跟ItemOnClickListener一个道理
    public interface OnScrollLoader {
        void loadMore();
    }

    public interface OnScrollUpperShower {
        void showUpper();

        void hideUpper();
    }

}
