package com.dhhuanghui.videocompressor.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.dhhuanghui.videocompressor.MyApplication;

import java.io.File;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2015/3/24.
 */
public class Util {

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyBroad(Context context, EditText editText) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    /**
     * 隐藏软键盘，不需要用EditText
     */
    public static void hideSoftKeyBroadNotEditText(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param editText
     */
    public static void showSoftKeyBroad(Context context, EditText editText) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // only will trigger it if no physical keyboard is open
        mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 显示软键盘，不需要EditText
     *
     * @param context
     */
    public static void showSoftKeyBroadNotET(Context context) {
        InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取缓存路径
     *
     * @param c
     * @return
     */
    public static String getCachePath(Context c) {
        String dirPath = c.getFilesDir().getAbsolutePath();
        String storageState = Environment.getExternalStorageState();
        boolean canWrite = Environment.getExternalStorageDirectory().canWrite();
        if (Environment.MEDIA_MOUNTED.equals(storageState) && canWrite) {
            dirPath = Environment.getExternalStorageDirectory().getPath() + dirPath;
        }
        dirPath += "/cache";
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 日志目录
     *
     * @param c
     * @return
     */
    public static String getLogPath(Context c) {
        return getAbslutePath(getCachePath(c) + "/" + "logs");
    }

    private static String getAbslutePath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    /**
     * 转化浮点型为小数点后n位
     *
     * @param num
     * @return
     */
    public static float formatFloat2float2Bits(float num, int n) {
        BigDecimal bd = new BigDecimal(num);
        BigDecimal bd1 = bd.setScale(n, bd.ROUND_HALF_UP);
        return bd1.floatValue();
    }

    /**
     * 转化浮点型为小数点后两位
     *
     * @param str
     * @return
     */
    public static String formatFloat2String2bits(float str) {
        return String.format("%.2f", str);
    }

    /**
     * 删除小数点
     *
     * @param str
     * @return
     */
    public static String deleteBits(float str) {
        return String.format("%.0f", str);
    }

    /**
     * 转化为小数点后两位
     *
     * @param d
     * @return
     */
    public static String formatDouble2String2bits(double d) {
        return String.valueOf(new DecimalFormat("0.00").format(d));
    }

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Log.w("Util", "couldn't get connectivity manager");
        } else {
            NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
            // 检测网络是否可用
            return (netWorkInfo != null && netWorkInfo.isAvailable());
        }
        return false;
    }
//    public static boolean isNetworkAvailable(Context context) {
//        if (context == null) {
//            return false;
//        }
//        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity == null) {
//            ELog.w("Util", "couldn't get connectivity manager");
//        } else {
//            NetworkInfo[] info = connectivity.getAllNetworkInfo();
//            if (info != null) {
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    public static String getMemoryCachePath(Context c) {
        return c.getFilesDir().getAbsolutePath() + "/cache";
    }

    /**
     * 获取当前版本
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
            return verName;
        } catch (Exception e) {
            return "";
        }

    }

    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            return versionCode;
        } catch (Exception e) {
            return versionCode;
        }

    }


    /**
     * 删除data/data/com.wtmbuy.wtmbuylocalmarker/files/cache中的图片文件
     *
     * @param file
     */
    public static void deleteCacheImageFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                if (childFiles[i].isDirectory()) {
                    continue;
                }
                delete(childFiles[i]);
            }
        }
    }

    public static void delete(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
        }
    }


    /**
     *  IMEI: 仅仅只对Android手机有效
     *
     */
//    public static String getAndroidImei(Context aContext){
//
//        TelephonyManager TelephonyMgr = (TelephonyManager)aContext.getSystemService(aContext.TELEPHONY_SERVICE);
//        String aImei = TelephonyMgr.getDeviceId();
//        return aImei;
//    }


    /**
     * string : 数字转化  如 以千、万为单位，保留 2位小数点
     */
    public static String getUnit(Long aGold, int aUnit) {
        String aGoldStr = null;
        if (aGold > aUnit) {
            float aGoldfloat = formatFloat2float2Bits((float) aGold / aUnit, 2);
            if (aUnit == 1000) {
                aGoldStr = aGoldfloat + "K";
            } else if (aUnit == 10000) {
                aGoldStr = aGoldfloat + "W";
            }
        } else {
            aGoldStr = String.valueOf(aGold);
        }
        return aGoldStr;
    }

    /**
     * 获得设备ID，如果设备ID为空，则获得UUID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        if (null == context) {
            return saveDeviceId(context);
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephonyManager) {
            return saveDeviceId(context);
        }
        String deviceId = telephonyManager.getDeviceId();
        if (deviceId != null && !TextUtils.isEmpty(deviceId.trim())) {
            return deviceId;
        } else {
            return saveDeviceId(context);
        }
    }

    private static String saveDeviceId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String deviceId = sp.getString("deviceId", "");
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = UUID.randomUUID().toString();
            sp.edit().putString("deviceId", deviceId).commit();
            return deviceId;
        }
        return deviceId;
    }

    public static boolean isHighVersion() {
        return Build.VERSION.SDK_INT >= 14;
    }

    /**
     * 转化字符串时间为long
     *
     * @param time
     * @return
     */
    public static long formatStringToLong(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 转化当前时间为年月日
     *
     * @return
     */
    public static String formatCurrentTime2String() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    /**
     * 转化当前时间为年月日 时 分
     *
     * @return
     */
    public static String formatCurrentTimeSecString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }

    /**
     * 获取当天0时的long型时间
     *
     * @return
     */
    public static long getCurrentDayBeginTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取当天的结束时间
     *
     * @return
     */
    public static long getCurrentDayEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime().getTime();
    }

    public static long formatTimeToLong(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @return 判断sdcard是否ok
     */
    public static boolean isSdcardAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);

    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    //转化手机号为中间带*号的
    public static String formatCellPhone(String cellphone) {
        if (cellphone == null || cellphone.length() < 11) {
            return cellphone;
        }
        //取前四位
        String str1 = cellphone.substring(0, 3);
        String lastStr = cellphone.substring(7);
        cellphone = str1 + "****" + lastStr;
        return cellphone;
    }

    //    //获取IP地址
//    public int getLocalIpAddress(Context context) {
//        context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
//        if (connectionInfo == null) {
//            return 0;
//        }
//        IpAddress = connectionInfo.getIpAddress();
//        return IpAddress;
//    }

    //获取本地的ip地址
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "";
    }

    public static void setBind(Context context, boolean flag) {
        String flagStr = "not";
        if (flag) {
            flagStr = "ok";
        }
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("bind_flag", flagStr);
        editor.commit();
    }

    /**
     * 判断是否是android4.4.2系统
     *
     * @return
     */
    public static boolean isAndroid4_4_2() {
        return Build.VERSION.RELEASE.equals("4.4.2");
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static String formatRandom() {
        String ranStr = null;
        Random random = new Random();
        int ranInt = random.nextInt(60);
        if (ranInt < 10) {
            ranStr = "0" + ranInt;
        } else {
            ranStr = String.valueOf(ranInt);
        }
        return ranStr;
    }

    /**
     * dp转px
     *
     * @param dip
     * @return
     */
    public static int dip2Px(float dip) {
        return (int) (dip * UIUtil.getInstance().getDensity() + 0.5f);
    }

    /**
     * 调起拨号盘
     *
     * @param context
     * @param phone
     */
    public static void dialPhone(Context context, String phone) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
    }

//    /**
//     * 直接拨打电话
//     *
//     * @param context
//     * @param phone
//     */
//    public static void callPhone(Context context, String phone) {
//        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone)));
//    }

    public static SpannableStringBuilder setForegroundSpan(String content, int color, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder setBackgroundSpan(String content, int color, int start, int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(new BackgroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    /**
     * 找到viewPager当前的fragment
     *
     * @param fragmentActivity
     * @param viewPagerCurrentItem
     */
    public static Fragment findFragmentByTag(FragmentActivity fragmentActivity, int pagerId, int viewPagerCurrentItem) {
        return fragmentActivity.getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + pagerId + ":" + viewPagerCurrentItem);
    }

    /**
     * 检测是否安装了指定应用
     *
     * @param context
     * @param packName
     * @return
     */
    public static boolean isAvilibe(Context context, String packName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++) {
            if (packageInfoList.get(i).packageName.equalsIgnoreCase(packName))
                return true;
        }
        return false;
    }


    public static String convertVideoSize(long size) {
        if (size >= 1024 * 1024) {
            float s1 = size / (1024 * 1024f);
            return formatFloat2String2bits(s1) + "M";
        } else if (size >= 1024) {
            float s1 = size / 1024f;
            return formatFloat2String2bits(s1) + "K";
        } else {
            return formatFloat2String2bits(Float.valueOf(size)) + "B";
        }
    }
}
