package com.tengxin.bannerlibrary.listener;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by hxl on 2017/7/5.
 */

public class BannerPageChangeListener implements ViewPager.OnPageChangeListener {
    private ArrayList<ImageView> pointViews;
    private int[] page_indicatorId;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    public BannerPageChangeListener(int[] mPage_indicatorId, ArrayList<ImageView> mPointViews) {
        page_indicatorId = mPage_indicatorId;
        pointViews = mPointViews;
    }

    public  void addOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener){
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (null != mOnPageChangeListener) {
            mOnPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < pointViews.size(); i++) {
            pointViews.get(position).setImageResource(page_indicatorId[1]);
            if (i != position) {
                pointViews.get(i).setImageResource(page_indicatorId[0]);
            }
        }
        if (null != mOnPageChangeListener) {
            mOnPageChangeListener.onPageSelected(position);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (null != mOnPageChangeListener) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}
