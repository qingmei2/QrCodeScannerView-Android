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
    private Button mBtnTest2;
    private Button mBtnTest3;
    private Button mBtnTest4;
    private Button mBtnTest5;
    private Button mBtnTest6;
    private Button mBtnTest7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnTest = (Button) findViewById(R.id.btn_test1);
        mBtnTest.setOnClickListener(this);
        mBtnTest2 = (Button) findViewById(R.id.btn_test2);
        mBtnTest2.setOnClickListener(this);
        mBtnTest3 = (Button) findViewById(R.id.btn_test3);
        mBtnTest3.setOnClickListener(this);
        mBtnTest4 = (Button) findViewById(R.id.btn_test4);
        mBtnTest4.setOnClickListener(this);
        mBtnTest5 = (Button) findViewById(R.id.btn_test5);
        mBtnTest5.setOnClickListener(this);
        mBtnTest6 = (Button) findViewById(R.id.btn_test6);
        mBtnTest6.setOnClickListener(this);
        mBtnTest7 = (Button) findViewById(R.id.btn_test7);
        mBtnTest7.setOnClickListener(this);

        mScannerView = (QRCodeScannerView) findViewById(R.id.scanner_view);
        mCoverView = (QRCoverView) findViewById(R.id.cover_view);

        //自动聚焦间隔2s
        mScannerView.setAutofocusInterval(2000L);
        //闪光灯
        mScannerView.setTorchEnabled(true);
        //扫描结果监听处理
        mScannerView.setOnQRCodeReadListener(new QRCodeScannerView.OnQRCodeScannerListener() {
            @Override
            public void onDecodeFinish(String text, PointF[] points) {
                Log.d("tag", "扫描结果 ： " + text); //扫描到的内容
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
            case R.id.btn_test1:
                //扫描框的宽度和高度
                mCoverView.setCoverViewScanner(220, 220);//提交修改UI
                break;
            case R.id.btn_test2:
                mScannerView.switchCameraFace();//切换摄像头
                break;
            case R.id.btn_test3:
                //修改扫描框外背景色
                mCoverView.setCoverViewOutsideColor(R.color.cover_bg2
                );//提交修改UI
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
        }
    }
}
