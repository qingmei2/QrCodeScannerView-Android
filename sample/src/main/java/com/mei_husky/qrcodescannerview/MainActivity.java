package com.mei_husky.qrcodescannerview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mei_husky.library.view.QRCodeScannerView;
import com.mei_husky.library.view.QRCoverView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private QRCodeScannerView mScannerView;
    private QRCoverView mCoverView;
    private Button mBtnTest;

    private final int PERMISSION_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mScannerView = (QRCodeScannerView) findViewById(R.id.scanner_view);
        mCoverView = (QRCoverView) findViewById(R.id.cover_view);
        mBtnTest = (Button) findViewById(R.id.btn_test);
        mBtnTest.setOnClickListener(this);

        //自动聚焦间隔2s
        mScannerView.setAutofocusInterval(2000L);
        //闪光灯
        mScannerView.setTorchEnabled(true);
        //扫描结果监听处理
        mScannerView.setOnQRCodeReadListener(new QRCodeScannerView.OnQRCodeScannerListener() {
            @Override
            public void onDecodeFinish(String text, PointF[] points) {
                Log.d("tag", "扫描结果 ： " + text);
                RectF finderRect = mCoverView.getViewFinderRect();
                Log.d("tag", "points.length = " + points.length);
                boolean isContain = true;
                //依次判断扫描结果的每个point是否都在扫描框内
                for (int i = 0, length = points.length; i < length; i++) {
                    if (!finderRect.contains(points[i].x, points[i].y)) {
                        isContain = false;  //只要有一个不在，说明二维码不完全在扫描框中
                        break;
                    }
                }
                if (isContain) {
                    Toast.makeText(MainActivity.this, "扫描成功！Result = " + text, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "扫描失败！请将二维码图片摆放在正确的扫描区域中...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //相机权限监听
        mScannerView.setOnCheckCameraPermissionListener(new QRCodeScannerView.OnCheckCameraPermissionListener() {
            @Override
            public boolean onCheckCameraPermission() {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                    return false;
                }
            }
        });
        //开启后置摄像头
        mScannerView.setBackCamera();
    }

    /**
     * 权限请求回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mScannerView.grantCameraPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test:
                //切换扫描界面UI的样式
                mCoverView.setCoverViewConnerFace(true)         //扫描边框在扫描框外部（默认为里部）
                        .setCoverViewCorner(40, 6, true)        //设置扫描边框的长度和厚度（单位是否为dp）
                        .setCoverViewCornerColor(R.color.colorAccent)//扫描边框的颜色
                        .setCoverViewScanner(220, 220)          //扫描框的宽度和高度
//                        .setCoverViewOutsideColor(R.color.colorPrimary)//修改扫描框外背景色
                        .commitUi();//提交修改UI
                mScannerView.switchCameraFace();//切换摄像头0
                break;
        }
    }
}
