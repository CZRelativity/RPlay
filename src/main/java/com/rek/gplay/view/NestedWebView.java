package com.rek.gplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

public class NestedWebView extends WebView implements NestedScrollingChild {

    static final String TAG = "NestedWebView";

    private int mLastY;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedOffsetY;
    private NestedScrollingChildHelper mChildHelper;

    public NestedWebView(Context context) {
        this(context, null);
    }

    public NestedWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public NestedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //官方提供的嵌套滑动辅助类
        mChildHelper = new NestedScrollingChildHelper(this);
        //支持嵌套滑动
        setNestedScrollingEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean returnValue = false;

        //复制一个相同的motionEvent
        MotionEvent event = MotionEvent.obtain(ev);

        //不再使用MotionEventCompat,而是直接使用event对象
        final int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedOffsetY = 0;
        }

        //统一使用原始event来采集，一开始子View完整的滑动距离
        int eventY = (int) event.getY();
//        Log.d(TAG, "raw " + eventY);
        event.offsetLocation(0, mNestedOffsetY);

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                //相较上一个Move的距离
                int deltaY = mLastY - eventY;
                /*
                 NestedPreScroll
                在子View消费滑动距离之前，分发给父布局
                这里使用的两个数组[0]代表x轴，[1]代表y轴，所以发现我们一直只是用了[1]
                */
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    //原位移-被父布局消费的距离
                    deltaY -= mScrollConsumed[1];
                    /* 更新位置记录，offset是相对于原来eventY的偏移量
                     * 跟consumed可能没有必然联系 */
                    mLastY = eventY - mScrollOffset[1];
                    /* 在y轴上滑动时，事件先被父布局消费，然后改变了原来的事件，
                    webView再接受到的事件是偏移之后的事件 */
                    event.offsetLocation(0, -mScrollOffset[1]);
                    //总的offset
                    mNestedOffsetY += mScrollOffset[1];
                }
//                Log.d(TAG, "before webView" + event.getY());
                //super指WebView？webView的滑动事件呢？给WebView的滑动事件确实Offset了，
                returnValue = super.onTouchEvent(event);
//                Log.d(TAG, "after webView " + event.getY());
                /*
                 NestedScroll
                在子View消费滑动距离之后，再分发给父布局，ScrollOffset数组复用了，在onNestedScroll中做了+=
                这里的消耗量偏移量都是对于子View来说的
                */
                if (dispatchNestedScroll(0, mScrollOffset[1], 0, deltaY, mScrollOffset)) {

                    event.offsetLocation(0, mScrollOffset[1]);
                    mNestedOffsetY += mScrollOffset[1];
                    mLastY -= mScrollOffset[1];
                }
                break;
            case MotionEvent.ACTION_DOWN:
                //直接给webView了
                returnValue = super.onTouchEvent(event);
                mLastY = eventY;
                // start NestedScroll
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //直接给webView了
                returnValue = super.onTouchEvent(event);
                // end NestedScroll
                stopNestedScroll();
                break;
        }
        return returnValue;
    }

    // Nested Scroll implements
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                                        int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
