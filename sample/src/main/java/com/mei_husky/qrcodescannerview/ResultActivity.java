package com.mei_husky.qrcodescannerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by QingMei on 2017/7/28.
 * desc:
 */

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        String result = getIntent().getStringExtra("result");
        TextView tvResult = (TextView) findViewById(R.id.tv_result);
        tvResult.setText(result);

        findViewById(R.id.btn_go_scan).setOnClickListener(this);
        findViewById(R.id.btn_go_encode).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_scan:
                finish();
                break;
            case R.id.btn_go_encode:
                startActivity(new Intent(this, SecondActivity.class));
                finish();
                break;
        }
    }
}
