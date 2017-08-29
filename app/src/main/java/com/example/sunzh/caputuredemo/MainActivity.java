package com.example.sunzh.caputuredemo;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get width
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int layout_width;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layout_width = outMetrics.widthPixels;
        } else {
            layout_width = outMetrics.widthPixels / 2;
        }
        int button_size = (int) (layout_width / 4.5f);

        CaptureButton btn_capture = new CaptureButton(this, button_size);
        FrameLayout.LayoutParams btn_capture_param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        btn_capture_param.addRule(CENTER_IN_PARENT, TRUE);
        btn_capture_param.gravity = Gravity.CENTER;
//        btn_capture_param.setMargins(0, 152, 0, 0);
        btn_capture.setLayoutParams(btn_capture_param);
        btn_capture.setDuration(10 * 1000);
        btn_capture.setCaptureLisenter(new CaptureLisenter() {
            @Override
            public void takePictures() {
            }

            @Override
            public void recordShort(long time) {
            }

            @Override
            public void recordStart() {
            }

            @Override
            public void recordEnd(long time) {
            }

            @Override
            public void recordZoom(float zoom) {
            }

            @Override
            public void recordError() {
            }
        });

        FrameLayout rootview = (FrameLayout) findViewById(R.id.rootview);
        rootview.addView(btn_capture);
    }
}
