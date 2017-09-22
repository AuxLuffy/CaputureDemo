package com.example.sunzh.caputuredemo.passwordinput;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.example.sunzh.caputuredemo.R;

/**
 * Created by sunzh on 2017/9/22.
 * 密码输入框
 * 需求：
 * 1、绘制外边框
 * <p>
 * 2、绘制密码间的分隔线
 * 3、绘制实心回圆代替输入的字符
 * 4、对输入字符进行监听，便于扩展处理
 * 5、实现一些常用的外部接口方法调用
 */

public class PayPsdInputView extends android.support.v7.widget.AppCompatEditText {
    //获取焦点的密码区的背景颜色
    private int focusColor = 0xFFeeeeee;
    //分隔线的宽
    private int divideLineWidth = 2;
    //密码小圆的颜色
    private int circleColor = Color.BLACK;
    //底部样式时的底边颜色
    private int bottomLineColor = Color.GRAY;
    //样式：微信样式，下划线样式
    private int psdType;
    //密码框的宽高
    private int height;
    private int width;
    //密码的长度
    private int maxCount = 6;
    //第一条分隔线的横坐标
    private int divideLineWStartX;

    //第一个实心圆的x，y坐标
    private int startX, startY;

    //密码下方的短线长度
    private int bottomLineLength;

    RectF rectF = new RectF();
    RectF focusRectF = new RectF();
    //角度
    private float rectAngle = 10f;
    //外边框画笔
    private Paint borderPaint;
    private int borderColor = Color.BLACK;
    //分隔线画笔
    private Paint divideLinePaint;
    private int divideLineColor = Color.GRAY;

    //小圆画笔
    private Paint circlePaint;
    //底部线画笔
    private Paint bottomLinePaint;


    //小圆半径
    private int radius;
    private Context mContext;
    //将要输入的位置索引
    private int position;
    //当前输入密码长度
    private int textLength;
    private String mComparedPwd;
    private onPasswordListener mListener;

    public PayPsdInputView(Context context) {
        this(context, null, 0);
    }

    public PayPsdInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayPsdInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getAtt(context, attrs);
        initPaint();
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setCursorVisible(false);//设置输入指针
        this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCount)});//设置输入规则
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        circlePaint = getPaint(5, Paint.Style.FILL, circleColor);
        bottomLinePaint = getPaint(2, Paint.Style.FILL, bottomLineColor);
        borderPaint = getPaint(3, Paint.Style.STROKE, borderColor);
        divideLinePaint = getPaint(divideLineWidth, Paint.Style.FILL, borderColor);
    }

    /**
     * 初始化画笔
     *
     * @param strokeWidth 画笔宽度
     * @param style       画笔风格
     * @param color       画笔颜色
     * @return
     */
    private Paint getPaint(int strokeWidth, Paint.Style style, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);
        return paint;
    }

    private void getAtt(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PayPsdInputView);
        maxCount = typedArray.getInt(R.styleable.PayPsdInputView_maxCount, 6);
        psdType = typedArray.getInt(R.styleable.PayPsdInputView_psdType, 0);
        borderColor = typedArray.getColor(R.styleable.PayPsdInputView_borderColor, this.borderColor);
        bottomLineColor = typedArray.getColor(R.styleable.PayPsdInputView_bottomLineColor, bottomLineColor);
        circleColor = typedArray.getColor(R.styleable.PayPsdInputView_circleColor, circleColor);
        divideLineColor = typedArray.getColor(R.styleable.PayPsdInputView_divideLineColor, divideLineColor);
        divideLineWidth = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_divideLineWidth, divideLineWidth);
        rectAngle = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_rectAngle, (int) rectAngle);
        focusColor = typedArray.getColor(R.styleable.PayPsdInputView_focusedColor, focusColor);
        int w = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_android_layout_width, 0);
        int h = typedArray.getDimensionPixelOffset(R.styleable.PayPsdInputView_android_layout_height, 0);
        Log.e("tag", "xml def: w," + w + "; h," + h);
        radius = typedArray.getColor(R.styleable.PayPsdInputView_radius, Math.min(h, w / maxCount) / 3);
        typedArray.recycle();
    }

    /*
    * 测试：
    * 通过AttributeSet获取attr必性
     */
    private void getAttrs(AttributeSet attrs) {
        int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String attrName = attrs.getAttributeName(i);
            String attrVal = attrs.getAttributeValue(i);
            Log.e("TAG", "attrName = " + attrName + ", attrVal = " + attrVal);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        divideLineWStartX = w / maxCount;

        startX = w / maxCount / 2;
        startY = h / 2;

        bottomLineLength = w / (maxCount + 2);

//        rectF.set(0, 0, width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //不删除会默认绘制输入的文字
//        super.onDraw(canvas);

        switch (psdType) {
            case 0:
                drawWeChatBorder(canvas);
                drawItemFocused(canvas, position);
                break;
            case 1:
                drawBottomBorder(canvas);
                break;
        }
        drawPsdCircle(canvas);

        //绘制实心圆代替输入的字符

    }

    private void drawPsdCircle(Canvas canvas) {
        for (int i = 0; i < textLength; i++) {
            canvas.drawCircle(startX + i * 2 * startX, startY, radius, circlePaint);
        }
    }

    /**
     * 画底部显示的分隔线
     *
     * @param canvas
     */
    private void drawBottomBorder(Canvas canvas) {
        for (int i = 0; i < maxCount; i++) {
            int centerX = startX * (2 * i + 1);
            canvas.drawLine(centerX - bottomLineLength / 2, height, centerX + bottomLineLength / 2, height, bottomLinePaint);
        }
    }

    /**
     * 画将要输入的框颜色
     *
     * @param canvas
     * @param position
     */
    private void drawItemFocused(Canvas canvas, int position) {
        if (position > maxCount - 1) {
            return;
        }
        focusRectF.set(position * divideLineWStartX, 0, (position + 1) * divideLineWStartX, height);
        canvas.drawRoundRect(focusRectF, rectAngle, rectAngle, getPaint(5, Paint.Style.FILL, focusColor));
    }

    private void drawWeChatBorder(Canvas canvas) {
        //画外边框
        rectF.set(0, 0, width, height);
        canvas.drawRoundRect(rectF, rectAngle, rectAngle, borderPaint);

        //画分隔线
        for (int i = 0; i < maxCount - 1; i++) {
            canvas.drawLine(divideLineWStartX * (i + 1), 0, divideLineWStartX * (i + 1), height, divideLinePaint);
        }
    }

    /**
     * @param text         当前输入的所有字符
     * @param start        从索引start（包括start）开始变化
     * @param lengthBefore 替换了长度为lengthBefore（被替换文本的长度）的文本
     * @param lengthAfter  替换文本的长度
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.position = start + lengthAfter;
        textLength = text.toString().length();
        if (mComparedPwd != null && textLength == maxCount) {
            if (TextUtils.equals(mComparedPwd, getPsdString())) {
                mListener.onEqual(getPsdString());
            } else {
                mListener.onDifference();
            }
        }
        invalidate();
    }

    public void setmComparedPwd(String comparedPwd) {
        this.mComparedPwd = comparedPwd;
    }

    public String getPsdString() {
        return getText().toString().trim();
    }

    public interface onPasswordListener {
        void onDifference();

        void onEqual(String psd);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selStart == selEnd) {
            //保证光标始终在最后
            setSelection(getText().length());
        }
    }
}
