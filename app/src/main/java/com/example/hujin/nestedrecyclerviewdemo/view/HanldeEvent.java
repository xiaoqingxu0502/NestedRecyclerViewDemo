package com.example.hujin.nestedrecyclerviewdemo.view;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author hujin
 * @package com.example.hujin.nestedrecyclerviewdemo.view
 * @description: ${TODO}(用一句话描述该文件做什么)
 * @email xiaoqingxu0502@gamil.com
 * @since 2018/4/11 下午12:36
 */
public interface HanldeEvent {
    void setIntercept(boolean intercept);
    boolean getIntercept();
    void setChildView(View childView);
    void setCurPosY(float curPosY);
    void actionIntercept(MotionEvent event);
}
