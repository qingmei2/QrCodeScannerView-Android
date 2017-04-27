package com.mei_husky.qrcodescannerview;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mei_husky.library.view.QRCodeScannerView;
import com.mei_husky.library.view.QRCoverView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private QRCodeScannerView mScannerView;
    private QRCoverView mCoverView;
    private Button mBtnTest;

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

        mScannerView.setOnQRCodeReadListener(new QRCodeScannerView.OnQRCodeScannerListener() {
            @Override
            public void onDecodeFinish(String text, PointF[] points) {

            }
        });
        mScannerView.setAutofocusInterval(2000L);
//        mScannerView.setTorchEnabled(true);
        mScannerView.setBackCamera();
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
                mCoverView.setCoverViewConnerFace(true)         //扫描边框在扫描框外部（默认为里部）
                        .setCoverViewCorner(40, 6, true)        //设置扫描边框的长度和厚度（单位是否为dp）
                        .setCoverViewCornerColor(R.color.colorAccent)//扫描边框的颜色
                        .setCoverViewScanner(220, 220)          //扫描框的宽度和高度
                        .setCoverViewOutsideColor(R.color.colorPrimary)//修改扫描框外背景色
                        .commitUi();                            //修改UI
                break;
        }
    }
}
