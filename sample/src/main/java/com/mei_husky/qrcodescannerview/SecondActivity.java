package com.mei_husky.qrcodescannerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qingmei2.library.util.QRCodeEncoder;

/**
 * Created by QingMei on 2017/7/19.
 * desc:create QrCode
 */
public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivQRCode;
    private QRCodeEncoder qrCodeEncoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.btn_create).setOnClickListener(this);
        findViewById(R.id.btn_create2).setOnClickListener(this);
        qrCodeEncoder = new QRCodeEncoder(this);
        ivQRCode = (ImageView) findViewById(R.id.iv_qrcode_bg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                qrCodeEncoder.createQrCode2ImageView("https://github.com/qingmei2", ivQRCode);
                break;
            case R.id.btn_create2:
                qrCodeEncoder.createQrCode2ImageView("https://github.com/qingmei2", ivQRCode, R.mipmap.ic_launcher);
                break;
        }
    }
}
