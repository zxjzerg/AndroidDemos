package com.zxjdev.textshadowdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;


public class MyActivity extends ActionBarActivity {

    TextView mTvFromCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mTvFromCode = (TextView) findViewById(R.id.tv_from_code);
        mTvFromCode.setShadowLayer(10f, 5f, 5f, R.color.black);

        /**
         在XML中:
         android:shadowColor="#000000"  阴影的颜色
         android:shadowDx="5"           x轴的偏移量
         android:shadowDy="5"           y轴的偏移量
         android:shadowRadius="10"      阴影的模糊程度
         */
    }

}
