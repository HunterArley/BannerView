package com.tengxin.bannerlibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tengxin.bannerlibrary.adapter.BannerPageAdapter;
import com.tengxin.bannerlibrary.listener.OnItemClickListener;

/**
 * Created by hxl on 2017/7/5.
 */

public class BannerViewPager extends ViewPager {
    private OnPageChangeListener mOuterPageChangeListener;
    private BannerPageAdapter mAdapter;
    //item点击事件
    private OnItemClickListener mOnItemClickListener;
    //是否循环翻页
    private boolean isLoop = true;
    //是否可以手动翻页
    private boolean isCanScroll = true;
    private float oldX=0;
    private float newX=0;
    //手指点击的便宜距离判断值
    private static final float sens = 5;

    public BannerViewPager(Context context) {
        super(context);
        init();
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.addOnPageChangeListener(onPageChangeListener);
    }

    //页面改变的监听器
    private OnPageChangeListener onPageChangeListener=new OnPageChangeListener() {
        private float mPreviousPosition = -1;
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int mRealPosition = position;
            if (null != mOuterPageChangeListener) {
                //如果position不是最后一个直接回调
                if (mAdapter.getRealCount() - 1 != mRealPosition) {
                    mOuterPageChangeListener.onPageScrolled(mRealPosition, positionOffset, positionOffsetPixels);
                } else {
                    if (positionOffset > 0.5f) {
                        mOuterPageChangeListener.onPageScrolled(0, 0, 0);
                    } else {
                        mOuterPageChangeListener.onPageScrolled(mRealPosition,0,0);
                    }
                }
            }

        }

        @Override
        public void onPageSelected(int position) {
            //转换为实际的position
            int mRealPosition = mAdapter.getRealPosition(position);
            if (mPreviousPosition != mRealPosition) {
                mPreviousPosition = mRealPosition;
                //如果设置了PageChangeListener就调用onPageSelected方法
                if (null != mOuterPageChangeListener) {
                    mOuterPageChangeListener.onPageSelected(mRealPosition);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (null != mOuterPageChangeListener) {
                mOuterPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    };

    /**
     * 事件拦截
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * 触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            if (null != mOnItemClickListener) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = ev.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        newX = ev.getX();
                        if (Math.abs(oldX - newX) < sens) {
                            mOnItemClickListener.onItemClick(getRealItem());
                        }
                        oldX = 0;
                        newX = 0;
                        break;
                }
            }
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * 获取最后一个item的position
     * @return
     */
    public int getLastItem(){
        return mAdapter.getRealCount()-1;
    }

    /**
     * 获取item的数量
     * @return
     */
    public int getItem(){
        return isLoop?mAdapter.getRealCount():0 ;
    }

    /**
     * 获取真实的count
     * @return
     */
    public int getRealItem(){
        return null!=mAdapter?mAdapter.getRealPosition(super.getCurrentItem()):0;
    }

    /**
     * 添加页面改变的监听
     * @param listener
     */
    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        mOuterPageChangeListener = listener;
    }

    /**
     * 设置item点击事件
     * @param mOnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public boolean isCanScroll(){
        return isCanScroll;
    }

    //设置是否手动滑动
    public void setCanScroll(boolean mCanScroll) {
        isCanScroll = mCanScroll;
    }

    /**
     * 设置适配器
     * @param mAdapter
     */
    public void setAdapter(BannerPageAdapter mAdapter,boolean mIsLoop) {
        this.mAdapter = mAdapter;
        mAdapter.setLoop(mIsLoop);
        mAdapter.setViewPager(this);
        super.setAdapter(mAdapter);
        //设置当前的Item为页面的数量
        setCurrentItem(getItem(),false);
    }

    /**
     * 设置是否循环翻页
     * @param mLoop
     */
    public void setLoop(boolean mLoop) {
        isLoop = mLoop;
        if (!isLoop) {
            setCurrentItem(getRealItem(),false);
        }
        if (null == mAdapter) {
            return;
        }
        mAdapter.setLoop(isLoop);
        mAdapter.notifyDataSetChanged();
    }

    public boolean isLoop(){
        return isLoop;
    }
}
