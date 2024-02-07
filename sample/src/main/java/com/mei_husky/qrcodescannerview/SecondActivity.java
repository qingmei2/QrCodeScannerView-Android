package com.mei_husky.qrcodescannerview;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qingmei2.library.encode.QRCodeEncoder;

/**
 * Created by QingMei on 2017/7/19.
 * desc:the guide creating Qrcode with any text
 * {@link QRCodeEncoder}
 */
public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivQRCode;

    private QRCodeEncoder qrCodeEncoder;

    private String textContent = "https://github.com/qingmei2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.btn_create).setOnClickListener(this);
        findViewById(R.id.btn_create_icon).setOnClickListener(this);
        qrCodeEncoder = new QRCodeEncoder(this);
        ivQRCode = (ImageView) findViewById(R.id.iv_qrcode_bg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                qrCodeEncoder.createQrCode2ImageView(textContent, ivQRCode);
                break;
            case R.id.btn_create_icon:
                qrCodeEncoder.createQrCode2ImageView(textContent, ivQRCode, R.mipmap.ic_launcher);
                break;
        }
    }
}
