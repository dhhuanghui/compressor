package com.dhhuanghui.videocompressor.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Video.Media;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;

import com.dhhuanghui.videocompressor.bean.ImageBucket;
import com.dhhuanghui.videocompressor.bean.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;


public class AlbumHelper {
    final String TAG = getClass().getSimpleName();
    //    Context context;
    private ContentResolver contentResolver;
    // 缩略图列表
    private LinkedHashMap<String, String> thumbnailList = new LinkedHashMap<String, String>();
    // 专辑列表
    private List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    private LinkedHashMap<String, ImageBucket> bucketList = new LinkedHashMap<String, ImageBucket>();
    private static AlbumHelper instance;

    private AlbumHelper() {
    }

    public static AlbumHelper getHelper() {
        if (instance == null) {
            instance = new AlbumHelper();
        }
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        contentResolver = context.getContentResolver();
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        String[] projection = {Thumbnails._ID, Thumbnails.VIDEO_ID, Thumbnails.DATA};
        Cursor cursor = contentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getThumbnailColumnData(cursor);
    }

    /**
     * 从数据库中得到缩略图
     *
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        if (cur != null && cur.moveToFirst()) {
            @SuppressWarnings("unused")
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(Thumbnails.VIDEO_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
            do {
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    /**
     * 得到原图
     */
    void getAlbum() {
        String[] projection = {Albums._ID, Albums.ALBUM, Albums.ALBUM_ART, Albums.ALBUM_KEY, Albums.ARTIST,
                Albums.NUMBER_OF_SONGS};
        Cursor cursor = contentResolver.query(Albums.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getAlbumColumnData(cursor);
    }

    /**
     * 从本地数据库中得到原图
     *
     * @param cur
     */
    private void getAlbumColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            String album;
            String albumArt;
            String albumKey;
            String artist;
            int numOfSongs;
            int _idColumn = cur.getColumnIndex(Albums._ID);
            int albumColumn = cur.getColumnIndex(Albums.ALBUM);
            int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
            int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
            int artistColumn = cur.getColumnIndex(Albums.ARTIST);
            int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);
            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                album = cur.getString(albumColumn);
                albumArt = cur.getString(albumArtColumn);
                albumKey = cur.getString(albumKeyColumn);
                artist = cur.getString(artistColumn);
                numOfSongs = cur.getInt(numOfSongsColumn);
                // Do something with the values.
                Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt + "albumKey: " + albumKey + " artist: "
                        + artist + " numOfSongs: " + numOfSongs + "---");
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("_id", _id + "");
                hash.put("album", album);
                hash.put("albumArt", albumArt);
                hash.put("albumKey", albumKey);
                hash.put("artist", artist);
                hash.put("numOfSongs", numOfSongs + "");
                albumList.add(hash);
            } while (cur.moveToNext());
        }
    }

    /**
     * 是否创建了图片集
     */
    boolean hasBuildImagesBucketList = false;

    /**
     * 得到图片集
     */
    void buildImagesBucketList() {
        long startTime = System.currentTimeMillis();
        /* 初始化全部图片集合 */
        ImageBucket allImageBucket = new ImageBucket();
        bucketList.put("-1000", allImageBucket);
        allImageBucket.imageList = new ArrayList<ImageItem>();
        allImageBucket.bucketName = "所有图片";
        // 构造缩略图索引
        getThumbnail();
        // 构造相册索引
        String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.DATA, Media.DISPLAY_NAME,
                Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME};
        // 得到一个游标
//        Cursor cursor=null;
//        try
//        {
//            cursor = MediaStore.Images.Media.query(contentResolver, Media.EXTERNAL_CONTENT_URI, columns, null, Media.DATE_MODIFIED + " desc");
//
//            if (cursor != null && cursor.moveToFirst())
//            {
//                do
//                {
//                    String path = cursor.getString(cursor.getColumnIndex(Media.DATA));
//                    Log.d("path", path);
//                    if (!new File(path).exists())
//                    {
//                        continue;
//                    }
//                    int _id = cursor.getInt(cursor.getColumnIndex(Media._ID));
//                    String dir_id = cursor.getString(cursor.getColumnIndex(Media.BUCKET_ID));
//                    String pa = cursor.getString(cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME));
//                    String size = cursor.getString(cursor.getColumnIndex(Media.SIZE));
//
//
//                } while (cursor.moveToNext());
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            if (cursor != null && !cursor.isClosed())
//            {
//                cursor.close();
//                cursor = null;
//            }
//        }

        Cursor cur = contentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, Media.DATE_ADDED + " DESC");
        if (cur != null && cur.moveToFirst()) {
            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            // 获取图片总数
//            int totalNum = cur.getCount();
            do {
                String _id = cur.getString(photoIDIndex);
//                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
//                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
//                String picasaId = cur.getString(picasaIdIndex);
                if ("0".equals(size)) {
                    continue;
                }
                ImageBucket bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new ImageBucket();
                    bucketList.put(bucketId, bucket);
                    bucket.imageList = new ArrayList<ImageItem>();
                    bucket.bucketName = bucketName;
                }
                bucket.count++;
                ImageItem imageItem = new ImageItem();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageItem.size = size;
                imageItem.thumbnailPath = thumbnailList.get(_id);
                bucket.imageList.add(imageItem);
                allImageBucket.count++;
                allImageBucket.imageList.add(imageItem);
            } while (cur.moveToNext());
        }
        Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) itr.next();
            ImageBucket bucket = entry.getValue();
            for (int i = 0; i < bucket.imageList.size(); ++i) {
                ImageItem image = bucket.imageList.get(i);
                Log.e(TAG, "----- " + bucket.bucketName + "--" + image.imagePath + "--" + image.thumbnailPath);
            }
        }
        hasBuildImagesBucketList = true;
        long endTime = System.currentTimeMillis();
    }

    /**
     * 得到图片集
     *
     * @param refresh
     * @return
     */
    public ArrayList<ImageBucket> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        ArrayList<ImageBucket> tmpList = new ArrayList<ImageBucket>();
        Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) itr.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    public void clear() {
        contentResolver = null;
    }

    /**
     * 得到原始图像路径
     *
     * @param image_id
     * @return
     */
    String getOriginalImagePath(String image_id) {
        String path = null;
        String[] projection = {Media._ID, Media.DATA};
        Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "=" + image_id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(Media.DATA));
        }
        return path;
    }
}
