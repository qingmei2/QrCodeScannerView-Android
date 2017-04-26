package com.mei_husky.qrcodescannerview;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mei_husky.library.view.QRCodeScannerView;
import com.mei_husky.library.view.QRCoverView;

public class MainActivity extends AppCompatActivity {

    private QRCodeScannerView mScannerView;
    private QRCoverView mCoverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mScannerView = (QRCodeScannerView) findViewById(R.id.scanner_view);
        mCoverView = (QRCoverView) findViewById(R.id.cover_view);

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
}
