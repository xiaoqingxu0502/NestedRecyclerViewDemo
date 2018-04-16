package com.example.hujin.nestedrecyclerviewdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.example.hujin.nestedrecyclerviewdemo.Util;

/**
 * @author hujin
 * @package com.example.hujin.nestedrecyclerviewdemo.view
 * @description: ${TODO}(用一句话描述该文件做什么)
 * @email xiaoqingxu0502@gamil.com
 * @since 2018/4/11 下午12:34
 */
public class NestedChildRecyclerView extends RecyclerView {
    private HanldeEvent mHanldeEvent;
    private float mCurPosY;
    private float mPosY;
    private int mTouchStop;

    public NestedChildRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        //根据自己需求可调整大小
        mTouchStop = Util.px2dp(context, vc.getScaledTouchSlop());
        setLayoutManager(new LinearLayoutManager(getContext()));
        //用于监听RecyclerView能否上下滑动
        initListener();
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        //当子RvSon停止滚动时，将RvParent的mScrollState也置成SCROLL_STATE_IDLE状态，否则。。。你可以取消试试
        if (state == SCROLL_STATE_IDLE && mHanldeEvent instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) mHanldeEvent;
            recyclerView.stopScroll();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mHanldeEvent == null) {
            return false;
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurPosY = e.getRawY();
                mHanldeEvent.setChildView(NestedChildRecyclerView.this);
                mHanldeEvent.setCurPosY(mCurPosY);
                break;
            case MotionEvent.ACTION_MOVE:
                mPosY = mCurPosY;
                mCurPosY = e.getRawY();
                //向上滑动
                if (mCurPosY - mPosY > mTouchStop) {
                    if (canScrollVertically(-1)) {
                        mHanldeEvent.setIntercept(false);
                    }
                }
                //向下滑动
                else if (mCurPosY - mPosY < -mTouchStop) {
                    if (canScrollVertically(1)) {
                        mHanldeEvent.setIntercept(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mHanldeEvent.setChildView(null);
                break;
            case MotionEvent.ACTION_CANCEL:
                //如果去掉，滑动事件从RvSon过度到RvParent会出现不平滑。　
                if (mHanldeEvent.getIntercept()) {
                    e.setAction(MotionEvent.ACTION_DOWN);
                    mHanldeEvent.actionIntercept(e);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(e);
    }

    private void initListener() {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mHanldeEvent == null) {
                    return;
                }
                //滑动到底部
                if (!canScrollVertically(1)) {
                    mHanldeEvent.setIntercept(true);
                }
                //滑动到顶部
                else if (!canScrollVertically(-1)) {
                    mHanldeEvent.setIntercept(true);
                }
            }
        });
    }


    public void setHanldeEvent(HanldeEvent hanldeEvent) {
        mHanldeEvent = hanldeEvent;
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return true;
    }
}
