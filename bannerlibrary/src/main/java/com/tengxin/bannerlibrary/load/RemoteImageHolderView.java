package com.tengxin.bannerlibrary.load;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.tengxin.bannerlibrary.holder.BannerHolder;


/**
 * Created by hxl on 2017/7/5.
 */

public class RemoteImageHolderView implements BannerHolder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, final int position, String data) {
        //可以使用第三方图片加载工具
//        Glide.with(context).load(data).into(imageView);
        Uri mParse = Uri.parse(data);
        imageView.setImageURI(mParse);
    }
}
