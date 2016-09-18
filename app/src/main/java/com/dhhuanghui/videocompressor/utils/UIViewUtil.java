package com.dhhuanghui.videocompressor.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.dhhuanghui.videocompressor.OnItemUpdateListener;

/**
 * Created by Administrator on 2015/7/16.
 */
public class UIViewUtil {
    /**
     * 设置View的显示和隐藏
     *
     * @param view
     * @param v
     */
    public static void setViewVisible(View view, int v) {
        if (view.getVisibility() != v) {
            view.setVisibility(v);
        }
    }

    public static void setViewSelect(View view, boolean b) {
        if (view.isSelected() != b) {
            view.setSelected(b);
        }
    }

    public static void setViewChecked(CheckBox view, boolean b) {
        if (view.isChecked() != b) {
            view.setChecked(b);
        }
    }

    public static void setViewEnable(View view, boolean b) {
        if (view.isEnabled() != b) {
            view.setEnabled(b);
        }
    }

    public static boolean imageViewReused(ImageView imageView, String imageUri) {
        String path = (String) imageView.getTag();
        if (path != null && path.equals(imageUri)) {
            return true;
        }
        return false;
    }

    /**
     * 更新listView或者gridView的Item
     *
     * @param viewGroup
     * @param currentPosition
     * @param oldPosition     上一次选中的位置，假如等于-1，则不使用
     * @param listener
     */
    public static void updateGridView(ViewGroup viewGroup, int currentPosition, int oldPosition, OnItemUpdateListener listener) {
        if (viewGroup instanceof GridView || viewGroup instanceof ListView) {
            AbsListView absListView = ((AbsListView) viewGroup);
            int firstVisiblePos = absListView.getFirstVisiblePosition();
            int lastVisiblePos = absListView.getLastVisiblePosition();
            if (oldPosition != -1 && oldPosition >= firstVisiblePos && oldPosition <= lastVisiblePos) {
                View viewOld = absListView.getChildAt(oldPosition - firstVisiblePos);
                if (viewOld != null && viewOld.getTag() != null) {
                    if (listener != null) {
                        listener.onUpdateOld(viewOld, oldPosition);
                    }
                }
            }
            if (currentPosition < firstVisiblePos || currentPosition > lastVisiblePos) return;
            View view = absListView.getChildAt(currentPosition - firstVisiblePos);
            if (view != null && view.getTag() != null) {
                if (listener != null) {
                    listener.onUpdateCurrent(view, currentPosition);
                }
            }
        }
    }
}
