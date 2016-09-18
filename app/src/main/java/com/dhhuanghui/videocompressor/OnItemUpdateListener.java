package com.dhhuanghui.videocompressor;

import android.view.View;

/**
 * Created by Administrator on 2015/12/4.
 */
public interface OnItemUpdateListener {
    /**
     * 更新当前点击的可见的Item
     *
     * @param view
     * @param currentPosition
     */
    public void onUpdateCurrent(View view, int currentPosition);

    /**
     * 更新上一次点击的的可见的Item
     *
     * @param view
     * @param oldPosition
     */
    public void onUpdateOld(View view, int oldPosition);


}
