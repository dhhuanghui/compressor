package com.dhhuanghui.videocompressor.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.dhhuanghui.videocompressor.MyApplication;


/**
 * Created by wtmbuy on 2015/3/14.
 */
public class ToastUtil {
    @Deprecated
    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 400);
        toast.show();
    }

    @Deprecated
    public static void showToast(Context context, int resId) {
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showShort(int resid) {
        Toast.makeText(MyApplication.getInstance(), resid, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int resid) {
        Toast.makeText(MyApplication.getInstance(), resid, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
    }


}
