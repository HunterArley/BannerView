package com.tengxin.bannerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tengxin.bannerlibrary.holder.BannerHolder;
import com.tengxin.bannerlibrary.holder.BannerHolderCreator;
import com.tengxin.bannerlibrary.listener.OnItemClickListener;
import com.tengxin.bannerlibrary.load.LocalImageHolderView;
import com.tengxin.bannerlibrary.transformeranimo.OutPageTransFormer;
import com.tengxin.bannerlibrary.view.BannerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BannerView mBannerView;
    private List<Integer> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatas.add(R.drawable.banner1);
        mDatas.add(R.drawable.banner2);
        mDatas.add(R.drawable.banner3);
        mBannerView = (BannerView) findViewById(R.id.banner);
        mBannerView.setDatas(mDatas, new BannerHolderCreator() {
            @Override
            public BannerHolder createHolder() {
                return new LocalImageHolderView();
            }
        }).startTurning(3000)//设置翻页时间
                .setPageTransformer(new OutPageTransFormer())//设置翻页动画
                .setPageIndicatorAlign(BannerView.PageIndicatorAlign.CENTER_HORIZONTAL)//设置指示器方向
                .setPagerIndicator(new int[]{R.drawable.dot_blur,R.drawable.dot_focus});//设置指示器图片

        //设置点击事件
        mBannerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "你点击的是第"+position+"个Item", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
