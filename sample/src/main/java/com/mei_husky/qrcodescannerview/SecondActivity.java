package com.mei_husky.qrcodescannerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qingmei2.library.QRCodeHelper;

/**
 * Created by QingMei on 2017/7/19.
 * desc:如何生成二维码
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivQRCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.btn_create).setOnClickListener(this);
        ivQRCode = (ImageView) findViewById(R.id.iv_qrcode_bg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                QRCodeHelper.createQRCode2ImageView(this, "hello,QRCode!", ivQRCode);
                break;
        }
    }
}
