package com.dhhuanghui.videocompressor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dhhuanghui.ffmpeg_demo_2.jni.FFmpegUtil;
import com.dhhuanghui.videocompressor.bean.ImageItem;
import com.dhhuanghui.videocompressor.utils.Util;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUESTCDOE_CHOOSE_VIDEO = 1;

    @InjectView(R.id.btn_choose)
    Button btnChoose;
    @InjectView(R.id.btn_compress)
    Button btnCompress;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.tv_choose_path)
    TextView tvVideoPath;
    @InjectView(R.id.tv_save_path)
    TextView tvSavePath;
    @InjectView(R.id.tv_choose_video_size)
    TextView tvPrimarySize;
    @InjectView(R.id.tv_compress_size)
    TextView tvCompressSize;

    private ImageItem imageItem;
    private ProgressDialog mProgressDialog;
    private File outputFile;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    showDialog();
                    break;
                case 2:
                    mProgressDialog.dismiss();
                    int result = msg.arg1;
                    if (result == 0) {
                        tvStatus.setText("压缩状态：压缩成功");
                        if (outputFile.exists()) {
                            tvCompressSize.setText("压缩后大小：" + Util.convertVideoSize(outputFile.length()));
                        }
                    } else {
                        tvStatus.setText("压缩状态：压缩失败");
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        initView();
        checkUpdate();


    }

    private void checkUpdate() {
        // 版本检测方式2：带更新回调监听
        PgyUpdateManager.register(MainActivity.this,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        final AppBean appBean = getAppBeanFromString(result);
                        String message = "新版本："+appBean.getVersionName()
                                +"\n更新内容：\n" + appBean.getReleaseNote();
                        Log.d("result", result);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("更新")
                                .setMessage(message)
                                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startDownloadTask(
                                                        MainActivity.this,
                                                        appBean.getDownloadURL());
                                            }
                                        }).show();

                    }

                    @Override
                    public void onNoUpdateAvailable() {
//                        Toast.makeText(MainActivity.this, "已经是最新版本",
//                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initView() {
        outputFile = new File(Environment.getExternalStorageDirectory(), "/out.mp4");
        tvSavePath.setText("视频输出路径：" + outputFile.getAbsolutePath());
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, TakeVideoActivity.class), REQUESTCDOE_CHOOSE_VIDEO);
            }
        });
        btnCompress.setEnabled(false);
        btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(imageItem);
            }
        });
    }

    private void showDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("正在压缩...");
        }
        mProgressDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void compressVideo(ImageItem imageItem) {
        Log.d("compressVideo", "path---" + imageItem.imagePath);
        Log.d("compressVideo", "size---" + imageItem.size);
        Message msg = Message.obtain();
        msg.what = 1;
        handler.sendMessage(msg);
        String cmd = String.format("ffmpeg -y -i %s -vcodec libx264 -s 640x352 -crf 25 -acodec copy -preset veryfast %s", imageItem.imagePath, outputFile.getAbsolutePath());
        String[] argv = cmd.split(" ");
        int result = FFmpegUtil.ffmpegcore(argv.length, argv);
        msg = Message.obtain();
        msg.what = 2;
        msg.arg1 = result;
        handler.sendMessage(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCDOE_CHOOSE_VIDEO && resultCode == RESULT_OK) {
            imageItem = (ImageItem) data.getSerializableExtra("ImageItem");
            if (imageItem == null) {
                return;
            }
            tvVideoPath.setText("视频路径：" + imageItem.imagePath);
            tvPrimarySize.setText("原始大小：" + Util.convertVideoSize(Long.valueOf(imageItem.size)));
            btnCompress.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
