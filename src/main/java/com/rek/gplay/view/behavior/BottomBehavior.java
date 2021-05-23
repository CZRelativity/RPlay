package com.rek.gplay.view.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    private ObjectAnimator inAnimator, outAnimator;
    private final static String TAG = "BottomBehavior";

    /* Error:Could not inflate Behavior subclass com.rek.gplay.view.BottomBehaviour
     * 原因：没有实现所有构造方法 */
    public BottomBehavior() {
    }

    public BottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull BottomNavigationView child, @NonNull View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull BottomNavigationView child, @NonNull View dependency) {
//        Log.d(TAG, "onDependentViewChanged: "+dependency.getTop());
        //getTop获得的不是距离，是位置，理解没问题
        float translationY = Math.abs(dependency.getTop());//获取跟随布局的顶部位置
        child.setTranslationY(translationY);
        return true;
    }

    //    @Override
//    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
//        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
//    }
//
//    @Override
//    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//        Log.d(TAG, "onNestedPreScroll: dy=" + dy);
//        //手向上滑动，屏幕向下滑动，BottomNavigation退出屏幕
//        if (dy > 0) {
//            if (outAnimator == null) {
//                //向下平移整个height
//                outAnimator = ObjectAnimator.ofFloat(child, "translationY", 0, child.getHeight());
//                outAnimator.setDuration(300);
//            }
//            if (!outAnimator.isRunning() && child.getTranslationY() == 0) {
//                outAnimator.start();
//            }
//            //手向下滑动，屏幕向上滑动，BottomNavigation重新进入屏幕
//        } else if (dy < 0) {
//            if (inAnimator == null) {
//                inAnimator = ObjectAnimator.ofFloat(child, "translationY", child.getHeight(), 0);
//                inAnimator.setDuration(300);
//            }
//            if (!inAnimator.isRunning() && child.getTranslationY() == child.getHeight()) {
//                inAnimator.start();
//            }
//        }
//    }

}
