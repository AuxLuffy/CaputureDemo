package com.example.sunzh.caputuredemo.surfacedemo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.example.sunzh.caputuredemo.R;

import java.io.IOException;

public class VideoPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener {

    private SurfaceView mSurfaceView;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private Display currDisplay;
    private int videoHeight;
    private int videoWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mSurfaceView = (SurfaceView) findViewById(R.id.video_surface);
        holder = mSurfaceView.getHolder();
        holder.addCallback(this);
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //实例化mediaplayer对象
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnInfoListener(this);
        player.setOnPreparedListener(this);
        player.setOnSeekCompleteListener(this);
        player.setOnVideoSizeChangedListener(this);

        //指定播放文件的路径，初始化MediaPlayer
        String path = Environment.getExternalStorageDirectory().getPath() + "/juice.mp4";
        try {
            player.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currDisplay = getWindowManager().getDefaultDisplay();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        //当surfaceview创建完成时调用，在这里我们指定Mediaplayer在当前的surface中进行播放
        Log.v("surfaceCreated", "surfaceCreated called");
        player.setDisplay(holder);
        //指定了MediaPlayer播放的容器后，我们就可以prepare或prepareAsyc来准备播放了
        player.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v("surfaceChanged", "surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v("surface destroy::: ", "surfaceDestroyed called");

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //当MediaPlayer播放完成后触发
        Log.v("play over::: ", "MediaPlayer onCompletion");
        this.finish();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("play error::: ", "onError called");
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.v("Play Error:::", "MEDIA_ERROR_SERVER_DIED");
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.v("Play Error:::", "MEDIA_ERROR_UNKNOWN");
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        // 当一些特定信息出现或者警告时触发
        switch(what){
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                break;
            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //当prepare完成后，该方法触发，在这里我们播放视频
        //首先获取video的宽高
        videoHeight = player.getVideoHeight();
        videoWidth = player.getVideoWidth();

        if (videoWidth > currDisplay.getWidth() || videoHeight > currDisplay.getHeight()) {
            float wRatio = videoWidth / (float) currDisplay.getWidth();
            float hRatio = videoHeight / (float) currDisplay.getHeight();

            float ratio = Math.max(wRatio, hRatio);
            videoWidth = (int) Math.ceil(videoWidth / ratio);
            videoHeight = (int) Math.ceil(videoHeight / ratio);

            mSurfaceView.setLayoutParams(new RelativeLayout.LayoutParams(videoWidth, videoHeight));

            player.start();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //seek操作完成时触发
        Log.v("Seek completion", "onSeekComplete called");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        //当video大小变化时触发
        //这个方法在设置player的source后至少触发一次
        Log.v("Video Size Change", "onVideoSizeChanged called");
    }
}
