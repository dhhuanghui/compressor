package com.dhhuanghui.videocompressor;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhhuanghui.videocompressor.bean.ImageBucket;
import com.dhhuanghui.videocompressor.utils.ImageUtil;
import com.dhhuanghui.videocompressor.utils.UIUtil;

import java.util.ArrayList;


public class PicturePopupWindow {
    static final String TAG = "PicturePopupWindow";
    private TextView mTVPictureName = null;
    private Context mContext = null;
    private RelativeLayout mRLBottom = null;
    private ArrayList<ImageBucket> mImageBuckets = null;
    private PopupWindow mPopupWindow = null;
    private ListView mDisplayListView = null;
    private FolderBaseAdapter mFolderBaseAdapter = null;
    private String selected_key;

    public PicturePopupWindow(Context context, ArrayList<ImageBucket> imageBuckets, String key) {
        mContext = context;
        mTVPictureName = (TextView) ((Activity) context).findViewById(R.id.activity_take_a_picture_name);
        mRLBottom = (RelativeLayout) ((Activity) context).findViewById(R.id.activity_take_a_picture_bottom);
        selected_key = key;
        this.mImageBuckets = imageBuckets;
        onResumePopupWindow();
    }

    private void onResumePopupWindow() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mContext);
            View view = View.inflate(mContext, R.layout.z_show_folder_picture, null);
            mPopupWindow.setContentView(view);
            mDisplayListView = (ListView) view.findViewById(R.id.z_show_folder_picture_list);
            mFolderBaseAdapter = new FolderBaseAdapter(mContext);
            mDisplayListView.setAdapter(mFolderBaseAdapter);
            mDisplayListView.setOnItemClickListener(FolderOnItemClickListerner);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setWidth(UIUtil.getInstance().getScreenWidth());
            mPopupWindow.setHeight(UIUtil.getInstance().getScreenHeight() / 2);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            mPopupWindow.showAtLocation(mTVPictureName, Gravity.BOTTOM, 0, (mRLBottom.getHeight() - 5));
            mPopupWindow.update();
        }
    }

    private OnItemClickListener FolderOnItemClickListerner = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ImageBucket folderItem = (ImageBucket) parent.getAdapter().getItem(position);
            mTVPictureName.setText(folderItem.bucketName);
            if (mOnItemCheckListener != null) {
                mOnItemCheckListener.onItemCheck(folderItem);
            }
            onDestoryPopupWindow();
        }
    };

    public interface OnItemCheckListener {
        void onItemCheck(ImageBucket bucket);
    }

    private OnItemCheckListener mOnItemCheckListener = null;

    public void setOnItemCheckListener(OnItemCheckListener listener) {
        mOnItemCheckListener = listener;
    }

    private class FolderBaseAdapter extends BaseAdapter {

        public FolderBaseAdapter(Context context) {
        }

        @Override
        public int getCount() {
            return mImageBuckets == null ? 0 : mImageBuckets.size();
        }

        @Override
        public Object getItem(int position) {
            return mImageBuckets == null ? null : mImageBuckets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.z_folder_picture_item, null);
                holder = new Holder();
                holder.icon = (ImageView) convertView.findViewById(R.id.z_folder_picture_item_icon);
                holder.name = (TextView) convertView.findViewById(R.id.z_folder_picture_item_text);
                holder.nums = (TextView) convertView.findViewById(R.id.z_folder_picture_item_num);
                holder.check = (ImageView) convertView.findViewById(R.id.z_folder_picture_item_check);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            ImageBucket item = (ImageBucket) getItem(position);
            if (item != null) {
                holder.name.setText(item.bucketName == null ? "" : item.bucketName);
                holder.nums.setText(item.count + "张");
                if (item.bucketName.equals(selected_key)) {
                    item.isSelected = true;
                } else {
                    item.isSelected = false;
                }
                holder.check.setVisibility(item.isSelected ? View.VISIBLE : View.INVISIBLE);
                if (item.imageList.size() > 0) {
                    ImageUtil.getInstance().displayImage(holder.icon, ImageUtil.PREFIX_LOCAL + item.imageList.get(0).imagePath);
                }
            }
            return convertView;
        }

        private class Holder {
            ImageView icon;
            TextView name;
            TextView nums;
            ImageView check;
        }
    }

    private void onDestoryPopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }
}
