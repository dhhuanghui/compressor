package com.dhhuanghui.videocompressor.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.dhhuanghui.videocompressor.MyApplication;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by huanghui on 2015/3/28.
 * 图片下载封装类
 */
public class ImageUtil {
    public static final String PREFIX_LOCAL = "file:///";
    private static ImageUtil instance = null;

    private ImageUtil() {

    }

    public static ImageUtil getInstance() {
        if (instance == null) {
            instance = new ImageUtil();
        }
        return instance;
    }

    public static void init() {
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.mipmap.bvendor).showImageOnLoading(R.mipmap.bvendor).showImageOnFail(R.mipmap.bvendor)
//                .cacheInMemory(true).cacheOnDisk(true).build();
//        File cacheDir = new File(Util.getCachePath(MyApplication.getInstance()));
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MyApplication.getInstance()).denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LRULimitedMemoryCache((int) Runtime.getRuntime().maxMemory() / 8))
//                .memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 8)
//                .diskCache(new UnlimitedDiscCache(cacheDir))
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).threadPoolSize(5).threadPriority(Thread.NORM_PRIORITY - 1)
//                .defaultDisplayImageOptions(defaultOptions)
//                .build();
//        ImageLoader.getInstance().init(config);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().considerExifParams(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MyApplication.getInstance())
                .threadPriority(Thread.NORM_PRIORITY - 1).threadPoolSize(3)
                .tasksProcessingOrder(QueueProcessingType.FIFO).memoryCache(new WeakMemoryCache())
                .defaultDisplayImageOptions(defaultOptions).memoryCache(new LruMemoryCache((int) (Runtime.getRuntime().maxMemory() / 8))).build();
        ImageLoader.getInstance().init(config);
    }

    public static void destroy() {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().destroy();
        }
    }

    private void initImageLoader() {
        if (!ImageLoader.getInstance().isInited()) {
            init();
        }
    }

    /**
     * 暂停下载
     */
    public void pause() {
        initImageLoader();
        ImageLoader.getInstance().pause();
    }

    /**
     * 恢复下载
     */
    public void resume() {
        initImageLoader();
        ImageLoader.getInstance().resume();
    }

    /**
     * 下载图片，无默认加载图片
     *
     * @param imageView
     * @param url
     */
    public void displayImage(ImageView imageView, String url) {
        initImageLoader();
        ImageLoader.getInstance().displayImage(url, imageView);
    }

    /**
     * 下载图片，无默认图
     *
     * @param imageView
     * @param url
     */
    public void displayImageNoImage(ImageView imageView, String url) {
        initImageLoader();
        DisplayImageOptions mNoImageOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).displayer(new FadeInBitmapDisplayer(500)).build();
        ImageLoader.getInstance().displayImage(url, imageView, mNoImageOptions);
    }

    /**
     * 指定ImageView大小来加载图片,使用此方法就要防止图片错位
     *
     * @param url
     * @param width
     * @param height
     * @param loadingListener
     */
    public void loadImage(String url, int width, int height, SimpleImageLoadingListener loadingListener) {
        initImageLoader();
        ImageSize mImageSize = new ImageSize(width, height);
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().loadImage(url, mImageSize, options, loadingListener);
    }

//    public void displayImageShopHome(ImageView imageView, String url) {
//        initImageLoader();
//        if (headOption == null) {
//            headOption = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.phone_img)
//                    .showImageForEmptyUri(R.mipmap.phone_img).showImageOnFail(R.mipmap.phone_img).cacheOnDisk(true)
//                    .build();
//        }
//        ImageLoader.getInstance().displayImage(url, imageView, headOption);
//    }


    /**
     * 下载图片，自定义默认加载图片
     *
     * @param imageView
     * @param url
     * @param resId     图片资源id
     */
    public void displayImage(ImageView imageView, String url, int resId) {
        initImageLoader();
        DisplayImageOptions customOptions = new DisplayImageOptions.Builder().showImageOnLoading(resId)
                .showImageForEmptyUri(resId).showImageOnFail(resId).cacheOnDisk(true)
                .build();
        ImageLoader.getInstance().displayImage(url, imageView, customOptions);
    }

    /**
     * 下载图片监听
     *
     * @param imageView
     * @param url
     */
    public void displayImageWithListener(ImageView imageView, String url, ImageLoadingListener listener) {
        initImageLoader();
        ImageLoader.getInstance().displayImage(url, imageView, listener);
    }

    /**
     * 清理图片缓存
     */
    public void clearImageCache() {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().clearDiskCache();
        }
    }

    /**
     * 清理Memory中的图片缓存
     */
    public void clearMemoryCache() {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().clearMemoryCache();
        }
    }

    /**
     * 清理disk中的图片缓存
     */
    public void clearDiskCache() {
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().clearDiskCache();
        }
    }
    /**
     * 下载圆形图片的方法
     */
//    public void downLoadRoundImage(ImageView ivProductImage, String url) {
//        initImageLoader();
//        ImageLoader.getInstance().displayImage(url, ivProductImage, optionsRound);
////        ImageLoader.getInstance().displayImage(url, ivProductImage, optionsRound, animateFirstListener);
//    }

    /**
     * 下载图片的方法
     */
//    public void downLoadImage(ImageView ivProductImage, String url) {
//        initImageLoader();
//        ImageLoader.getInstance().displayImage(url, ivProductImage, options);
//    }

    /**
     * 没有默认加载图片
     *
     * @param ivProductImage
     * @param url
     */
//    public void downLoadImageNoLoadingImage(ImageView ivProductImage, String url) {
//        initImageLoader();
//        ImageLoader.getInstance().displayImage(url, ivProductImage, optionsNoLoadImage);
//    }

    /**
     * 下载图片监听 ，变圆角图片
     *
     * @param context
     * @param iv
     * @param url
     */
//    public void downLoadImageRoundCouner(final Context context, final ImageView iv, String url) {
//        initImageLoader();
//        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
//
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loadpicture_1);
//                Bitmap roundBitmap = BitmapTools.toRoundBitmap(bitmap);
//                iv.setImageBitmap(roundBitmap);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loadpicture_1);
//                bitmap = BitmapTools.toRoundBitmap(bitmap);
//                iv.setImageBitmap(bitmap);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                if (loadedImage == null) {
//                    return;
//                }
//                Bitmap roundBitmap = BitmapTools.toRoundBitmap(loadedImage);
//                iv.setImageBitmap(roundBitmap);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
//    }

    /**
     * 下载图片显示在background
     *
     * @param imageView
     * @param url
     */
//    public void downLoadImageDisplayBackground(final ImageView imageView, String url) {
//        loadImage(url, new ImageLoadingListener() {
//
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                imageView.setBackgroundResource(R.drawable.loadpicture_1);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                imageView.setBackgroundResource(R.drawable.loadpicture_1);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                imageView.setBackgroundDrawable(new BitmapDrawable(loadedImage));
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
//    }

    /**
     * 下载图片，触发监听事件
     *
     * @param url
     * @param listener
     */
//    public void loadImage(String url, ImageLoadingListener listener) {
//        initImageLoader();
//        ImageLoader.getInstance().loadImage(url, listener);
//    }

//    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//        @Override
//        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            if (loadedImage != null) {
//                ImageView imageView = (ImageView) view;
//                boolean firstDisplay = !displayedImages.contains(imageUri);
//                if (firstDisplay) {
//                    FadeInBitmapDisplayer.animate(imageView, 500);
//                    displayedImages.add(imageUri);
//                }
//            }
//        }
//    }

}
