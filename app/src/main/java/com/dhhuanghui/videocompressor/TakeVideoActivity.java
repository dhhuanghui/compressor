package com.dhhuanghui.videocompressor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dhhuanghui.videocompressor.bean.ImageBucket;
import com.dhhuanghui.videocompressor.bean.ImageItem;
import com.dhhuanghui.videocompressor.utils.AlbumHelper;
import com.dhhuanghui.videocompressor.utils.ImageUtil;
import com.dhhuanghui.videocompressor.utils.ToastUtil;
import com.dhhuanghui.videocompressor.widget.HeaderView;

import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

import java.util.ArrayList;

public class TakeVideoActivity extends AppCompatActivity implements View.OnClickListener, PicturePopupWindow.OnItemCheckListener {
    private static final String TAG = "TakeVideoActivity";
    private ArrayList<ImageBucket> mDataList;// 相册文件夹集合
    private ArrayList<ImageItem> mCurrentThumbInfoList = new ArrayList<ImageItem>();// 当前图片集合
    private ArrayList<ImageItem> mSelectList = new ArrayList<ImageItem>();// 当前选中的图片集合
    private GridView mGridView;
    private PictureAdapter mPictureAdapter;
    private String currentBucket;
    private int mSelectedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        mSelectedCount = getIntent().getIntExtra("count", 0);
        AlbumHelper.getHelper().init(this);
        mDataList = AlbumHelper.getHelper().getImagesBucketList(false);
        onItemCheck(mDataList.get(0));
        initView();
        setupSpotAd();
    }

    /**
     * 设置插屏广告
     */
    private void setupSpotAd() {
        // 设置插屏图片类型，默认竖图
        //		// 横图
        //		SpotManager.getInstance(mContext).setImageType(SpotManager
        // .IMAGE_TYPE_HORIZONTAL);
        // 竖图
        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        // 设置动画类型，默认高级动画
        //		// 无动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_NONE);
        //		// 简单动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_SIMPLE);
        // 高级动画
        SpotManager.getInstance(this)
                .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

        // 展示插屏广告
        SpotManager.getInstance(TakeVideoActivity.this).showSpot(TakeVideoActivity.this, new SpotListener() {

            @Override
            public void onShowSuccess() {
                Log.d(TAG, "插屏展示成功");
            }

            @Override
            public void onShowFailed(int errorCode) {
                Log.d(TAG, "插屏展示失败");
                switch (errorCode) {
                    case ErrorCode.NON_NETWORK:
//                        Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        break;
                    case ErrorCode.NON_AD:
//                        Toast.makeText(mContext, "暂无广告", Toast.LENGTH_SHORT).show();
                        break;
                    case ErrorCode.RESOURCE_NOT_READY:
                        Log.e(TAG, "资源还没准备好");
//                        Toast.makeText(mContext, "请稍后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case ErrorCode.SHOW_INTERVAL_LIMITED:
                        Log.e(TAG, "展示间隔限制");
//                        Toast.makeText(mContext, "请勿频繁展示", Toast.LENGTH_SHORT).show();
                        break;
                    case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                        Log.e(TAG, "控件处在不可见状态");
//                        Toast.makeText(mContext, "请设置插屏为可见状态", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onSpotClosed() {
                Log.d(TAG, "插屏被关闭");
            }

            @Override
            public void onSpotClicked(boolean isWebPage) {
                Log.d(TAG, "插屏被点击");
                Log.i(TAG, String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
            }
        });
    }

    private void initView() {
        HeaderView headerView = (HeaderView) findViewById(R.id.headerView_take_pic);
        headerView.setTvMidText("选择视频");
//        headerView.setAllVisible();
//        TextView rightTv = (TextView) headerView.findViewById(R.id.tv_right);
//        rightTv.setText("完成");
//        rightTv.setBackgroundResource(R.drawable.headerview_rightbutton_bg);
//        headerView.setRightOnClickListener(this);
        findViewById(R.id.layout_take_picture).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.gridView_take_pic);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        ImageUtil.getInstance().resume();
                        break;
                    case SCROLL_STATE_FLING:
                    case SCROLL_STATE_TOUCH_SCROLL:
                        ImageUtil.getInstance().pause();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ImageItem imageItem = (ImageItem) parent.getAdapter().getItem(position);
                Intent data = new Intent();
                data.putExtra("ImageItem", imageItem);
                setResult(RESULT_OK, data);
                finish();
//                if (imageItem.isSelected) {
//                    mPictureAdapter.downSelectCount();
//                    imageItem.isSelected = !imageItem.isSelected;
//                    mPictureAdapter.updateItem(parent, position, imageItem);
//                } else {
//                    if (mPictureAdapter.getSelectCount() >= MultiChoosePicActivity.MAX_COUNT) {
//                        ToastUtil.showShort(String.format("最多选择%1$d张图片", MultiChoosePicActivity.MAX_COUNT));
//                    } else {
//                        mPictureAdapter.upSelectCount();
//                        imageItem.isSelected = !imageItem.isSelected;
//                        mPictureAdapter.updateItem(parent, position, imageItem);
//                    }
//                }
            }
        });
        mPictureAdapter = new PictureAdapter(this, mCurrentThumbInfoList, mSelectedCount);
        mGridView.setAdapter(mPictureAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_take_picture:
                PicturePopupWindow popupWindow = new PicturePopupWindow(this, mDataList, currentBucket);
                popupWindow.setOnItemCheckListener(this);
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.right:
                if (mSelectList.size() == 0) {
                    ToastUtil.showShort("请选择图片");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("mSelectList", mSelectList);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemCheck(ImageBucket bucket) {
        if (bucket != null) {
            bucket.isSelected = true;
            currentBucket = bucket.bucketName;
            if (mCurrentThumbInfoList != null) {
                mCurrentThumbInfoList.clear();
                mCurrentThumbInfoList.addAll(bucket.imageList);
            }
            if (mPictureAdapter != null) {
                mGridView.setSelection(0);
                mPictureAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        ImageUtil.getInstance().clearMemoryCache();
        for (ImageItem imageItem : mSelectList) {
            imageItem.isSelected = false;
        }
        super.onDestroy();
        // 插播广告
        SpotManager.getInstance(this).onDestroy();
    }

    /**
     * 更新保存了选中的图片的List
     *
     * @param imageItem
     */
    public void updateSelectList(ImageItem imageItem) {
        if (imageItem.isSelected) {
            mSelectList.add(imageItem);
        } else {
            mSelectList.remove(imageItem);
        }
    }

    @Override
    public void onBackPressed() {
        // 点击后退关闭插播广告
        // 当只嵌入插屏或轮播插屏其中一种广告时，不需要外层if判断
        if (SpotManager.getInstance(this).isSpotShowing()) {
            SpotManager.getInstance(this).hideSpot();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 插播广告
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 插播广告
        SpotManager.getInstance(this).onStop();
    }

}
