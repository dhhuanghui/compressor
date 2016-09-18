package com.dhhuanghui.ffmpeg_demo_2.jni;

/**
 * Created by THINK on 2016/9/12.
 */
public class FFmpegUtil {

    static {
        System.loadLibrary("avutil-54");
        System.loadLibrary("swresample-1");
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avformat-56");
        System.loadLibrary("swscale-3");
        System.loadLibrary("postproc-53");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("NdkJniDemo");
    }

    /**
     * 获取视频的角度信息
     * @param path
     * @return
     */
    public static native int getRotate(String path);

    /**
     * 对视频进行处理(旋转、压缩等等)
     * @param argc
     * @param argv
     * @return
     */
    public static native int ffmpegcore(int argc, String[] argv);
}
