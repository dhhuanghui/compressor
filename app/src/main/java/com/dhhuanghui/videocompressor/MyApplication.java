package com.dhhuanghui.videocompressor;

import android.app.Application;

/**
 * Created by Administrator on 2015/3/20.
 */
public class MyApplication extends Application {
    private static Application instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        LeakCanary.install(this);
//        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
    }

    public static Application getInstance() {
        return instance;
    }

    /**
     * 捕获异常
     */
//    public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
//        private Thread.UncaughtExceptionHandler defaultUEH;
//
//        /*
//         * if any of the parameters is null, the respective functionality will
//         * not be used
//         */
//        public CustomExceptionHandler() {
//            this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
//        }
//
//        @Override
//        public void uncaughtException(Thread thread, Throwable ex) {
//                Date dt = new Date();
//                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//                String timestamp = df.format(dt);
//                Writer result = new StringWriter();
//                PrintWriter printWriter = new PrintWriter(result);
//                printWriter.append("TIME:" + System.currentTimeMillis() + "," + "\r\n");
//                printWriter.append("MODEL:" + Build.MODEL + "," + "\r\n");
//                printWriter.append("APP_VERSION:" + Util.getVerName(instance) + "," + "\r\n");
//                printWriter.append("ANDROID_VERSION:" + Build.VERSION.RELEASE + "," + "\r\n");
//                printWriter.append("ERROR_MSG:");
//                ex.printStackTrace(printWriter);
//                String stacktrace = result.toString();
//                printWriter.close();
//                ELog.f(stacktrace, "Exception-" + timestamp + ".txt");
//                defaultUEH.uncaughtException(thread, ex);
//            }
//        }
//    }

}
