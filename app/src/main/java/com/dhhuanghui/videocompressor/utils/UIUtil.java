package com.dhhuanghui.videocompressor.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;


import com.dhhuanghui.videocompressor.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具管理类
 *
 * @author huanghui
 */
public class UIUtil {
    private static UIUtil instance = null;
    private DisplayMetrics dm;
    private Handler handler = new Handler();

    private UIUtil(Context context) {
        dm = context.getResources().getDisplayMetrics();
    }

    public static UIUtil getInstance() {
        if (instance == null) {
            instance = new UIUtil(MyApplication.getInstance());
        }
        return instance;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        if (dm == null) {
            dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        }
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getScreenHeight() {
        if (dm == null) {
            dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        }
        return dm.heightPixels;
    }

    /**
     * 获取屏幕像素密度
     *
     * @return
     */
    public int getDensityDpi() {
        if (dm == null) {
            dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        }
        return dm.densityDpi;
    }

    /**
     * 获取屏幕密度 density = densityDpi /160;
     *
     * @return
     */
    public float getDensity() {
        if (dm == null) {
            dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        }
        return dm.density;
    }

    public DisplayMetrics getDisplayMetrics() {
        if (dm == null) {
            dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        }
        return dm;
    }


    private String matchString(String wholeString, String reg) {
        Pattern network = Pattern.compile(reg);
        Matcher networkMatcher = network.matcher(wholeString);
        if (networkMatcher.find()) {
            return networkMatcher.group(1);
        }
        return "";
    }

    /**
     * 获取状态栏的高度(有些机型获取不到),废弃
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Activity context) {
        //获取状态栏的高度；outRect.top
        Rect outRect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect.top;
    }

}
