package com.cyq.progressview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.cyq.progressview.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author : ChenYangQi
 * date   : 2020/5/6 14:24
 * desc   : 温度进度控件
 */
public class ThermometerView extends View {
    private Context mContext;
    //控件宽高
    private int width, height;
    //粒子圆环的宽度
    private int mCircleWidth = 30;
    //粒子总个数
    private int pointCount = 100;
    //粒子列表
    private List<AnimPoint> mPointList = new ArrayList<>(pointCount);
    //粒子外层圆环原点坐标和半径长度
    private int centerX, centerY, radius;
    //粒子外层圆环的画笔
    private Paint mCirclePaint;
    //粒子画笔
    private Paint mPointPaint;
    private Paint mLinePaint;
    //白色
    private int whiteColor = Color.parseColor("#FFFFFFFF");
    private int blackColor = Color.parseColor("#FF000000");
    private int redColor = Color.parseColor("#f44336");
    private int yellowColor = Color.parseColor("#4caf50");


    private int beginRadialGradientColor = Color.parseColor("#66001BFF");
    private int endRadialGradientColor = Color.parseColor("#1978FF");
    private int middleRadialGradientColor = Color.parseColor("#1A001BFF");
    private int radialCircleColor = Color.parseColor("#FF0066FF");
    private int[] radialArr = {blackColor, middleRadialGradientColor, beginRadialGradientColor};
    private float[] radialPositionArr = {0F, 0.6F, 1F};

    private Paint mSweptPaint;
    private RadialGradient mRadialGradient;

    private Random mRandom = new Random();

    public ThermometerView(Context context) {
        this(context, null);
    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        width = Utils.dip2px(300, mContext);
        height = Utils.dip2px(300, mContext);
        mCircleWidth = Utils.dip2px(5, mContext);
        centerX = width / 2;
        centerY = width / 2;
        radius = width / 2 - mCircleWidth / 2;

        mCirclePaint = new Paint();
        mCirclePaint.setColor(endRadialGradientColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(40);
        mCirclePaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));

        mPointPaint = new Paint();
        mPointPaint.setColor(whiteColor);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));

        mLinePaint = new Paint();
        mLinePaint.setColor(yellowColor);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setStyle(Paint.Style.FILL);

        mSweptPaint = new Paint();
        mSweptPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSweptPaint.setColor(radialCircleColor);
        mRadialGradient = new RadialGradient(
                0,
                0,
                radius,
                radialArr,
                radialPositionArr,
                Shader.TileMode.CLAMP);
        mSweptPaint.setShader(mRadialGradient);


        mPointList.clear();
        AnimPoint animPoint = new AnimPoint();
        animPoint.setAlpha(1);

        for (int i = 0; i < pointCount; i++) {
            //通过clone创建对象，避免重复创建
            AnimPoint cloneAnimPoint = animPoint.clone();
            cloneAnimPoint.init(mRandom, radius);
            mPointList.add(cloneAnimPoint);
        }

        //画运动粒子
        ValueAnimator animator = ValueAnimator.ofFloat(0F, 1F);
        animator.setDuration(Integer.MAX_VALUE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                for (AnimPoint point : mPointList) {
                    point.updatePoint(mRandom, radius);
                }
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        //画测试坐标线
//        canvas.drawLine(radius, 0, radius, height, mLinePaint);
//        canvas.drawLine(0, radius, width, radius, mLinePaint);

        //画动画粒子
        canvas.translate(centerX, centerY);
        for (AnimPoint animPoint : mPointList) {
            canvas.drawCircle(animPoint.getmX(), animPoint.getmY(),
                    animPoint.getRadius(), mPointPaint);
        }

        //画渐变进度圆
        canvas.drawCircle(0, 0, radius, mSweptPaint);
        //渐变进度圆的外层圆环
        canvas.drawCircle(0, 0, radius-14, mCirclePaint);
    }
}
