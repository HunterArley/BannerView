package com.tengxin.bannerlibrary.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.tengxin.bannerlibrary.R;
import com.tengxin.bannerlibrary.holder.BannerHolder;
import com.tengxin.bannerlibrary.holder.BannerHolderCreator;
import com.tengxin.bannerlibrary.view.BannerViewPager;

import java.util.List;

/**
 * Created by hxl on 2017/7/5.
 */

public class BannerPageAdapter<T> extends PagerAdapter {
    private BannerViewPager mViewPager ;
    //翻页的最大量
    private final int MAX_COUNT = 500;
    //控制是否循环翻页
    private boolean isLoop = true;
    //page的数据
    private final List<T> mList;
    private final BannerHolderCreator mHolderCreator;

    public BannerPageAdapter(List<T> mList, BannerHolderCreator mHolderCreator) {
        this.mList = mList;
        this.mHolderCreator = mHolderCreator;
    }

    /**
     * 设置Count
     * @return
     */
    @Override
    public int getCount() {
        return isLoop?getRealCount()* MAX_COUNT :getRealCount() ;
    }

    /**
     * 获取实际的翻页数量
     * @return
     */
    public int getRealCount(){
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    /**
     * 当所有页面中的更改已完成时调用。
     * 此时必须确保所有页面都已实际添加或从容器中删除。
     * 根据当前item设置实际的item实现无限循环。
     * @param container
     */
    @Override
    public void finishUpdate(ViewGroup container) {
        int position = mViewPager.getCurrentItem();
        if (0 == position) {
            position = mViewPager.getItem();
        } else if (getCount() - 1 == position) {
            position = mViewPager.getLastItem();
        }
        mViewPager.setCurrentItem(position, false);
    }

    /**
     * 创建item
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int mRealPosition = getRealPosition(position);

        View mView = getView(mRealPosition, null, container);
        container.addView(mView);
        return mView;
    }

    /**
     * 获取实际的position
     * @param position
     * @return
     */
    public int getRealPosition(int position){
        int mRealCount = getRealCount();
        if (0 == mRealCount) {
            return 0;
        }
        int mRealPosition = position % mRealCount;
        return mRealPosition;
    }

    /**
     * 获取item视图
     * @param position
     * @param mView
     * @param mGroup
     * @return
     */
    public View getView(int position,View mView,ViewGroup mGroup){
        BannerHolder mHolder = null;
        if (null == mView) {
            mHolder = (BannerHolder) mHolderCreator.createHolder();
            mView = mHolder.createView(mGroup.getContext());
            mView.setTag(R.id.banner_item_tag, mHolder);
        } else {
            mHolder = (BannerHolder<T>) mView.getTag(R.id.banner_item_tag);
        }
        if (null != mList && !mList.isEmpty()) {
            mHolder.UpdateUI(mGroup.getContext(),position,mList.get(position));
        }
        return mView;
    }

    //设置是否循环播放
    public void setLoop(boolean mLoop) {
        isLoop = mLoop;
    }

    //设置View Pager
    public void setViewPager(BannerViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

    /**
     * 销毁item
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View mView= (View) object;
        container.removeView(mView);
    }
}
