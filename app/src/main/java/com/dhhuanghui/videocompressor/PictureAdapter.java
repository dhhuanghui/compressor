package com.dhhuanghui.videocompressor;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dhhuanghui.videocompressor.bean.ImageItem;
import com.dhhuanghui.videocompressor.utils.ImageUtil;
import com.dhhuanghui.videocompressor.utils.UIUtil;
import com.dhhuanghui.videocompressor.utils.UIViewUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by huanghui on 2015/11/19.
 */
public class PictureAdapter extends BaseAdapter {
    private ArrayList<ImageItem> mCurrentThumbInfoList;
    private Context mContext;
    private int mSelectCount = 0;
    private int width;

    public PictureAdapter(Context context, ArrayList<ImageItem> currentThumbInfoList, int selectCount) {
        mCurrentThumbInfoList = currentThumbInfoList;
        this.mContext = context;
        this.mSelectCount = selectCount;
        width = (int) ((UIUtil.getInstance().getScreenWidth() - UIUtil.getInstance().getDensity() * 20) / 3);

    }

    public void downSelectCount() {
        mSelectCount--;
    }

    public void upSelectCount() {
        mSelectCount++;
    }

    public int getSelectCount() {
        return mSelectCount;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumb_show_item, null);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.thumb_show_item_icon);
            holder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_icon_data_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageItem imageItem = mCurrentThumbInfoList.get(position);
//        if (imageItem.isSelected) {
//            holder.ivSelected.setImageResource(R.mipmap.btn_choose_enter);
//            holder.ivImage.setColorFilter(R.color.c_black_translucent);
//        } else {
//            holder.ivSelected.setImageResource(R.mipmap.btn_choose_default);
//            holder.ivImage.clearColorFilter();
//        }
        String path = ImageUtil.PREFIX_LOCAL + imageItem.imagePath;
        final ImageView imageView = holder.ivImage;
        imageView.setTag(path);
        ImageUtil.getInstance().loadImage(path, width, width, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (UIViewUtil.imageViewReused(imageView, imageUri)) {
                    imageView.setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (UIViewUtil.imageViewReused(imageView, imageUri)) {
                    imageView.setImageResource(R.mipmap.bvendor);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (UIViewUtil.imageViewReused(imageView, imageUri)) {
                    imageView.setImageResource(R.mipmap.bvendor);
                }
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return mCurrentThumbInfoList == null ? 0 : mCurrentThumbInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCurrentThumbInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 更新单条Item
     *
     * @param parent
     * @param pos
     * @param imageItem
     */
    public void updateItem(ViewGroup parent, int pos, final ImageItem imageItem) {
        UIViewUtil.updateGridView(parent, pos, -1, new OnItemUpdateListener() {
            @Override
            public void onUpdateCurrent(View view, int currentPos) {
                if (view.getTag() instanceof ViewHolder) {
                    ViewHolder holder = (ViewHolder) view.getTag();
                    if (imageItem.isSelected) {
                        if (mContext != null && mContext instanceof TakeVideoActivity) {
                            ((TakeVideoActivity) mContext).updateSelectList(imageItem);
                        }
                        holder.ivSelected.setImageResource(R.mipmap.btn_choose_enter);
                        holder.ivImage.setColorFilter(R.color.c_black_translucent);
                    } else {
                        if (mContext != null && mContext instanceof TakeVideoActivity) {
                            ((TakeVideoActivity) mContext).updateSelectList(imageItem);
                        }
                        holder.ivSelected.setImageResource(R.mipmap.btn_choose_default);
                        holder.ivImage.clearColorFilter();
                    }
                }
            }

            @Override
            public void onUpdateOld(View view, int oldPosition) {

            }
        });
    }

    public class ViewHolder {
        public ImageView ivSelected;
        public ImageView ivImage;
    }

}
