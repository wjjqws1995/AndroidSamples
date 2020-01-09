package com.cyq.customview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cyq.customview.drawText.DrawTextActivity;
import com.cyq.customview.shadowLayout.ShadowLayoutActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnShadowLayout;
    private Button mBtnDrawText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnShadowLayout = findViewById(R.id.btn_shadow_layout);
        mBtnShadowLayout.setOnClickListener(this);
        mBtnDrawText = findViewById(R.id.btn_draw_text);
        mBtnDrawText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_shadow_layout:
                intent.setClass(this, ShadowLayoutActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_draw_text:
                intent.setClass(this, DrawTextActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
}
