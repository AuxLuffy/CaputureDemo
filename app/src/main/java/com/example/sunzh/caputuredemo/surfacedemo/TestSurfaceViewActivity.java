package com.example.sunzh.caputuredemo.surfacedemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.ALog;

/**
 * Created by sunzh on 2017/9/12.
 */

public class TestSurfaceViewActivity extends Activity {

    private MySurfaceView mySurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ALog.e("onCreate");
        super.onCreate(savedInstanceState);
        mySurfaceView = new MySurfaceView(this);
        setContentView(mySurfaceView);
    }

    @Override
    protected void onResume() {
        ALog.e("onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        ALog.e("onPause");
        super.onPause();
        mySurfaceView.onPause();
    }

    @Override
    protected void onStop() {
        ALog.e("onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ALog.e("onDestroy");
        super.onDestroy();
    }
}
