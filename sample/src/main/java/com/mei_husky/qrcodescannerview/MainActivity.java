package com.mei_husky.qrcodescannerview;

import android.Manifest;
import android.content.Intent;
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

import com.qingmei2.library.view.QRCodeScannerView;
import com.qingmei2.library.view.QRCoverView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private QRCodeScannerView mScannerView;
    private QRCoverView mCoverView;

    private static final String TAG = "MainActivity";

    private final int PERMISSION_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_test1).setOnClickListener(this);
        findViewById(R.id.btn_test2).setOnClickListener(this);
        findViewById(R.id.btn_test3).setOnClickListener(this);
        findViewById(R.id.btn_test4).setOnClickListener(this);
        findViewById(R.id.btn_test5).setOnClickListener(this);
        findViewById(R.id.btn_test6).setOnClickListener(this);
        findViewById(R.id.btn_test7).setOnClickListener(this);
        findViewById(R.id.btn_test8).setOnClickListener(this);

        mScannerView = (QRCodeScannerView) findViewById(R.id.scanner_view);
        mCoverView = (QRCoverView) findViewById(R.id.cover_view);

        //自动聚焦间隔2s
        mScannerView.setAutofocusInterval(2000L);
        //扫描结果监听处理
        mScannerView.setOnQRCodeReadListener(new QRCodeScannerView.OnQRCodeScannerListener() {
            @Override
            public void onDecodeFinish(String text, PointF[] points) {
                Log.d(TAG, "扫描结果 result -> " + text); //扫描到的内容
                //【可选】判断二维码是否在扫描框中
                judgeResult(text, points);
            }
        });
        //相机权限监听(如果你有相关的权限类，可以不实现该接口)
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

    private void judgeResult(String result, PointF[] points) {
        //接下来是处理二维码是否在扫描框中的逻辑
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
            Intent intent = new Intent(this, ResultActivity.class).putExtra("result", result);
            startActivity(intent);
        } else {
            Log.d(TAG, "扫描失败！请将二维码图片摆放在正确的扫描区域中...");
        }
    }

    /**
     * 权限请求回调
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
            case R.id.btn_test1:
                //扫描框的宽度和高度
                mCoverView.setCoverViewScanner(280, 280);//提交修改UI
                break;
            case R.id.btn_test2:
                mScannerView.switchCameraFace();//切换摄像头
                break;
            case R.id.btn_test3:
                //修改扫描框外背景色
                mCoverView.setCoverViewOutsideColor(R.color.cover_bg2);//提交修改UI
                break;
            case R.id.btn_test4:
                //不显示扫描线
                mCoverView.setShowLaser(false);
                break;
            case R.id.btn_test5:
                //显示扫描线
                mCoverView.setShowLaser(true);
                break;
            case R.id.btn_test6:
                //扫描框边角在外
                mCoverView.setCoverViewConnerFace(true);
                break;
            case R.id.btn_test7:
                //停止扫描
                mScannerView.setQRDecodingEnabled(false);
                mCoverView.setShowLaser(false);//隐藏扫描线
                break;
            case R.id.btn_test8:
                startActivity(new Intent(this, SecondActivity.class));
                break;
        }
    }
}
