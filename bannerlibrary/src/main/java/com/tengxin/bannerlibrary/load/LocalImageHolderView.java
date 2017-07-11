package com.tengxin.bannerlibrary.load;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.tengxin.bannerlibrary.holder.BannerHolder;

/**
 * Created by hxl on 2017/7/11.
 */

public class LocalImageHolderView implements BannerHolder<Integer>{
    private ImageView imageView;

    @Override
    public View createView(Context mContext) {
        imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context mContext, int position, Integer data) {
        imageView.setImageResource(data);
    }
}
