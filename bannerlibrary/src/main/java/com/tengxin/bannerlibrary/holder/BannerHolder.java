package com.tengxin.bannerlibrary.holder;

import android.content.Context;
import android.view.View;

/**
 * Created by hxl on 2017/7/5.
 */

public interface BannerHolder<T> {
    View createView(Context mContext);

    void UpdateUI(Context mContext, int position, T data);
}
