package com.example.sunzh.caputuredemo.surfacedemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.blankj.ALog;

/**
 * Created by sunzh on 2017/9/12.
 */

public class MyThread extends Thread {


    private SurfaceHolder holder;
    private boolean run;

    public MyThread(SurfaceHolder holder) {
        this.holder = holder;
        run = true;
        setName("MySufaceView Thread");
    }

    @Override
    public void run() {
        int counter = 0;
        Canvas canvas = null;
        while (run) {
            try {
                //获取canvas对象，并锁定
                canvas = holder.lockCanvas();
                //设定canvas对象的背景颜色
                canvas.drawColor(Color.RED);

                Paint paint = new Paint();
//            paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                paint.setTextSize(50);
//                ALog.e("time: " + counter + " s");
                canvas.drawText("interval = " + counter++ + " seconds.", 100, 410, paint);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    //解除锁定，并提交修改内容
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }
}
