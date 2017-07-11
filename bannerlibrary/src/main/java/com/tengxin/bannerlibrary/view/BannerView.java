package com.tengxin.bannerlibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tengxin.bannerlibrary.adapter.BannerPageAdapter;
import com.tengxin.bannerlibrary.listener.BannerPageChangeListener;
import com.tengxin.bannerlibrary.listener.OnItemClickListener;
import com.tengxin.bannerlibrary.R;
import com.tengxin.bannerlibrary.transformeranimo.ViewPagerScroller;
import com.tengxin.chelingwang.R;
import com.tengxin.chelingwang.widget.banner.holder.BannerHolderCreator;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxl on 2017/7/5.
 */

public class BannerView<T> extends LinearLayout {
    private ViewPagerScroller mScroller;
    private BannerViewPager mViewPager;
    private ViewGroup loPageTurningPoint;
    private int[] page_indicatorId;
    private BannerPageChangeListener mBannerPageChangeListener;
    private BannerPageAdapter mAdapter;
    //是否自动翻页
    private boolean autoTurn = false;
    //是否正在翻页
    private boolean turning;
    private AdSwitchTask mAdSwitchTask;
    //翻页间隔时间
    private long autoTurningTime;
    private boolean canLoop = true;
    //数据
    private List<T> mDatas;
    //翻页指示点的位置
    public enum PageIndicatorAlign{
        ALIGN_PARENT_LEFT,ALIGN_PARENT_RIGHT,CENTER_HORIZONTAL
    }
     //指示点图片数组
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public BannerView(Context context) {
        super(context);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context mContext) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_banner, this, true);
        mViewPager = (BannerViewPager) mView.findViewById(R.id.mBannerViewPager);
        loPageTurningPoint = (ViewGroup) mView.findViewById(R.id.loPageTurningPoint);
        initViewPagerScroll();
        mAdSwitchTask = new AdSwitchTask(this);
    }

    /**
     * 自定义翻页动画效果
     * @param mTransformer
     * @return
     */
    public BannerView<T> setPageTransformer(ViewPager.PageTransformer mTransformer) {
        mViewPager.setPageTransformer(true,mTransformer);
        return this;
    }

    /**
     * 通过java反射设置View Pager的滑动速度
     */
    private void initViewPagerScroll() {

        try {
            Field mScroller = null;
            mScroller = mViewPager.getClass().getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            this.mScroller = new ViewPagerScroller(mViewPager.getContext());
            mScroller.set(mViewPager,this.mScroller);
        } catch (NoSuchFieldException mE) {
            mE.printStackTrace();
        } catch (IllegalAccessException mE) {
            mE.printStackTrace();
        }
    }

    /**
     * 自动滚动线程
     * 使用WeakReference防止内存泄漏的问题
     */
    static class AdSwitchTask implements Runnable{

        private final WeakReference<BannerView> mReference;

        AdSwitchTask(BannerView mBannerView) {
            this.mReference = new WeakReference<BannerView>(mBannerView);
        }


        @Override
        public void run() {
            BannerView mBannerView = mReference.get();
            if (null != mBannerView) {
                if (null != mBannerView.mViewPager && mBannerView.turning) {
                    int page = mBannerView.mViewPager.getCurrentItem() + 1;
                    mBannerView.mViewPager.setCurrentItem(page);
                    mBannerView.postDelayed(mBannerView.mAdSwitchTask, mBannerView.autoTurningTime);
                }

            }
        }
    }

    /**
     * 是否开启了翻页
     * @return
     */
    public boolean isTurning(){
        return turning;
    }

    /**
     * 开始翻页
     * @param mAutoTurningTime
     * @return
     */
    public BannerView startTurning(long mAutoTurningTime) {
        //如果正在翻页的话先停止翻页
        if (turning) {
            stopTurning();
        }
        //设置可以翻页并开启翻页
        autoTurn = true;
        autoTurningTime = mAutoTurningTime;
        turning = true;
        postDelayed(mAdSwitchTask, autoTurningTime);
        return this;

    }

    /**
     * 停止自动翻页
     */
    private void stopTurning() {
        turning = false;
        removeCallbacks(mAdSwitchTask);
    }

    /**
     * 底部指示器资源图片
     * @param page_indicatorId
     * @return
     */
    public BannerView setPagerIndicator(int[] page_indicatorId){
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.page_indicatorId = page_indicatorId;
        if (null == mDatas) {
            return this;
        }
        for (int i = 0; i < mDatas.size(); i++) {
            //翻页指示的点
            ImageView mImageView = new ImageView(getContext());
            mImageView.setPadding(5,0,5,0);
            if (mPointViews.isEmpty()) {
                mImageView.setImageResource(page_indicatorId[1]);
            } else {
                mImageView.setImageResource(page_indicatorId[0]);
            }
            mPointViews.add(mImageView);
            loPageTurningPoint.addView(mImageView);
        }
        mBannerPageChangeListener = new BannerPageChangeListener(page_indicatorId, mPointViews);
        mViewPager.addOnPageChangeListener(mBannerPageChangeListener);
        mBannerPageChangeListener.onPageSelected(mViewPager.getRealItem());
        if (null != mBannerPageChangeListener) {
            mBannerPageChangeListener.addOnPageChangeListener(onPageChangeListener);
        }
        return this;
    }

    /**
     * 设置底部指示器是否可见
     * @param visible
     * @return
     */
    public BannerView setPointViewVisible(boolean visible){
        loPageTurningPoint.setVisibility(visible?VISIBLE:GONE);
        return this;
    }

    /**
     * 设置翻页监听器
     * @param mOnPageChangeListener
     * @return
     */
    public BannerView setOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener) {
        onPageChangeListener = mOnPageChangeListener;
        //如果有默认的监听器（即是使用了默认的翻页指示器）则把用户设置的依附到默认的上面，否则就直接设置
        if (null != mBannerPageChangeListener) {
            mBannerPageChangeListener.addOnPageChangeListener(onPageChangeListener);
        } else {
            mViewPager.addOnPageChangeListener(onPageChangeListener);
        }
        mViewPager.addOnPageChangeListener(onPageChangeListener);
        return this;
    }


    /**
     * 设置翻页数据
     * @param mDatas
     * @param mCreator
     * @return
     */
    public BannerView setDatas(List<T> mDatas, BannerHolderCreator mCreator) {
        this.mDatas = mDatas;
        mAdapter = new BannerPageAdapter(mDatas, mCreator);
        mViewPager.setAdapter(mAdapter,canLoop);
        if (null != page_indicatorId) {
            setPagerIndicator(page_indicatorId);
        }
        return this;
    }

    /**
     * 指示器的方向
     * @param mAlign
     * @return
     */
    public BannerView setPageIndicatorAlign(PageIndicatorAlign mAlign){
        RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) loPageTurningPoint.getLayoutParams();
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, mAlign == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, mAlign == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, mAlign == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        loPageTurningPoint.setLayoutParams(mLayoutParams);
        return this;
    }

    /**
     * 触碰控件的时候，翻页应该停止
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int mAction = ev.getAction();
        if (MotionEvent.ACTION_UP == mAction || MotionEvent.ACTION_CANCEL == mAction || MotionEvent.ACTION_OUTSIDE == mAction) {
            //开始翻页
            if (autoTurn) {
                startTurning(autoTurningTime);
            }
        } else if (MotionEvent.ACTION_DOWN==mAction) {
            //停止翻页
            if (autoTurn) {
                stopTurning();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //是否循环
    public void setAutoTurn(boolean mAutoTurn) {
        autoTurn = mAutoTurn;
        mViewPager.setLoop(mAutoTurn);
    }

    //设置View Pager的滚动速度
    public void setScrollDuration(int mScrollDuration){
        mScroller.setScrollDuration(mScrollDuration);

    }

    public BannerView setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        if (null == mOnItemClickListener) {
            mViewPager.setOnItemClickListener(null);
            return this;
        }
        mViewPager.setOnItemClickListener(mOnItemClickListener);
        return this;
    }

    public boolean isManualPageable(){
        return mViewPager.isCanScroll();
    }

    public void setManualPageable(boolean mManualPageable){
        mViewPager.setCanScroll(mManualPageable);
    }
}
