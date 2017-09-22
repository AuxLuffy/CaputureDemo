package com.example.sunzh.caputuredemo.xfermodetest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.sunzh.caputuredemo.utils.ScreenUtils;

/**
 * Created by sunzh on 2017/9/20.
 */

public class PorterDuffView extends View {

    private PorterDuffBO porterDuffBO;//porterduffview类的业务对象
    private PorterDuffXfermode porterDuffXfermode;//图象混合模式
    private int sw, sh;//屏幕尺寸
    private int s_l, s_t;//左上方正方形的原点坐标
    private int d_l, d_t;//右上方正方形的原点坐标
    private int rectX, rectY;//中间正方形的原点坐标
    private Paint mPaint;//画笔

    private static final PorterDuff.Mode MODE = PorterDuff.Mode.DST;

    private static final int RECT_SIZE_SMALL = 400;//左右上方示例渐变正方形大小
    private static final int RECT_SIZE_BIG = 800;//中间示例渐变正方形大小


    public PorterDuffView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //实例化画笔并抗锯齿化
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //实例化业务对象
        porterDuffBO = new PorterDuffBO();

        porterDuffXfermode = new PorterDuffXfermode(MODE);

        //计算坐标
        cacl(context);

    }

    private void cacl(Context context) {
        sw = ScreenUtils.getScreenWidth(context);
        sh = ScreenUtils.getScreenHeight(context);
        Log.e("TAG", "w = " + sw + "; h = " + sh);
        s_l = 0;
        s_t = 0;

        d_l = sw - RECT_SIZE_SMALL;
        d_t = 0;

        //计算中间正方形的原点坐标
        rectX = sw / 2 - RECT_SIZE_BIG / 2;
        rectY = RECT_SIZE_SMALL + (sh - RECT_SIZE_SMALL) / 2 - RECT_SIZE_BIG / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画布颜色为黑色
        canvas.drawColor(Color.BLACK);

        //设置业务对象大小
        porterDuffBO.setSize(RECT_SIZE_SMALL);

        //画出左右上方两个正方形
        //左边的为src  右边的为dst
        canvas.drawBitmap(porterDuffBO.initSrcBitmap(), s_l, s_t, mPaint);
        canvas.drawBitmap(porterDuffBO.initDisBitmap(), d_l, d_t, mPaint);

        //将绘制操作保存到新的图层
        int sc = canvas.saveLayer(0,0,sw,sh,null,Canvas.ALL_SAVE_FLAG);

        //重新设置业务对象尺寸值计算生成中间的渐变方形
        porterDuffBO.setSize(RECT_SIZE_BIG);
        //先绘制des目标图
        canvas.drawBitmap(porterDuffBO.initDisBitmap(),rectX,rectY, mPaint);
        //设置混合模式
        mPaint.setXfermode(porterDuffXfermode);
        //再绘制src源图
        canvas.drawBitmap(porterDuffBO.initSrcBitmap(),rectX,rectY,mPaint);
        //还原混合模式
        mPaint.setXfermode(null);
        //还原画布
        canvas.restoreToCount(sc);
    }
}
