package com.example.sunzh.caputuredemo.surfacedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.sunzh.caputuredemo.R;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView mVv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        mVv = (VideoView) findViewById(R.id.vv);
        mVv.setMediaController(new MediaController(this));

    }
}
