package com.example.sunzh.caputuredemo.surfacedemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blankj.ALog;

/**
 * Created by sunzh on 2017/9/12.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private MyThread myThread;

    public MySurfaceView(Context context) {
        super(context);
        ALog.e("constructor");
        //通过surfaceview获得surfaceholder对象
        holder = getHolder();
        //为holder添加回调结构surfaceHolder.CallBack
        holder.addCallback(this);
        //创建一个绘制线程，将holder对象作为参数传进去，这产在绘制线程中就可以获得holder对象了，进而在绘制线程中可以通过holder对象可以获得canvas对象
        myThread = new MyThread(holder);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ALog.e("surfaceCreated");
        myThread.setRun(true);
        myThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ALog.e("surfaceDestroyed");
        onPause();
    }

    public void onPause() {
        //结束线程，当这个方调用时，说明surface即将被销毁了
        myThread.setRun(false);
    }

    @Override
    protected void onDetachedFromWindow() {
        ALog.e("onDetachedFromWindow");
        super.onDetachedFromWindow();
    }
}
