package com.example.hujin.nestedrecyclerviewdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.hujin.nestedrecyclerviewdemo.Util;

/**
 * @author hujin
 * @package com.example.hujin.nestedrecyclerviewdemo.view
 * @description: ${TODO}(用一句话描述该文件做什么)
 * @email xiaoqingxu0502@gamil.com
 * @since 2018/4/10 下午1:13
 */
public class NestedRecyclerView extends RecyclerView implements HanldeEvent {
    /**
     * 当手指的落点在RvSon区域内时赋值，当手指离开屏幕时置空，用来判断事件由谁处理。
     * */
    private View mChildView;
    /**
     * RvParent用户判断是否拦截
     */
    private boolean isIntercept;

    private float mPosY;
    private float mCurPosY;

    private int mTouchStop;

    public NestedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        //根据自己需求可调整大小
        mTouchStop = Util.px2dp(context, vc.getScaledTouchSlop());
    }


    @Override
    public void setIntercept(boolean isIntercept) {
        this.isIntercept = isIntercept;
    }

    @Override
    public boolean getIntercept() {
        return isIntercept;
    }

    @Override
    public void setChildView(View childView) {
        mChildView = childView;
    }

    @Override
    public void setCurPosY(float curPosY) {
        mCurPosY = curPosY;
    }

    @Override
    public void actionIntercept(MotionEvent event) {
        onInterceptTouchEvent(event);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // 去掉默认行为，使得每个事件都会经过这个Layout
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mChildView == null) {
            return super.onInterceptTouchEvent(e);
        }
        if (isIntercept) {
            return super.onInterceptTouchEvent(e);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mChildView == null) {
            return super.onTouchEvent(e);
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurPosY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mPosY = mCurPosY;
                mCurPosY = e.getRawY();
                if (mCurPosY - mPosY > mTouchStop) {
                    //当RvParent向上滑动时，如果手指的落点在RvSon区域内，且RvSon可以向上滑动，重新dispatch一次down事件，使得列表可以继续滚动
                    if (mChildView != null && mChildView.canScrollVertically(-1)) {
                        aginDispatch(e);
                    }
                } else if (mCurPosY - mPosY < -mTouchStop) {
                    //原理同上
                    if (mChildView != null && mChildView.canScrollVertically(1)) {
                        aginDispatch(e);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercept = true;
                setChildView(null);
                break;
            default:
                break;

        }
        return super.onTouchEvent(e);
    }

    /**
     * @description:重新派发
     */
    private void aginDispatch(MotionEvent e) {
        setIntercept(false);
        e.setAction(MotionEvent.ACTION_DOWN);
        dispatchTouchEvent(e);
    }
}
