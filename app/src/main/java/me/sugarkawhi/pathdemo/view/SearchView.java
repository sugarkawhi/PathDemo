package me.sugarkawhi.pathdemo.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 画一个简单放大镜
 * 思路：
 * 1.放大镜由一个圆圈和斜线组成，先画圆再画线
 * 2.确定圆的半径，和线的长度，以及线开始从哪画
 * 3.借ValueAnimator控制动画，画圆完成再画圈
 * Created by ZhaoZongyao on 2017/8/23.
 */

public class SearchView extends View {

    private enum STATE {
        CIRCLE,
        LINE
    }

    private Paint mPaint;
    private Path mCirclePath;
    private Path mLinePath;

    //圆中心X坐标
    private float centerX;
    //圆中心Y坐标
    private float centerY;

    //放大镜圆的半径
    private int mRadius = 50;
    //圆边框宽度
    private int mStrokeWidth = 5;
    //画圆动画时长
    private int mCircleAnimDuration = 1000;
    //画线动画时长
    private int mLineAnimDuration = 500;
    //线长度 根据勾股定理求出长度
    private int mLineLength = 30;

    //
    private PathMeasure mPathMeasure;
    //画圆动画
    private ValueAnimator mCircleAnim;
    //画线动画
    private ValueAnimator mLineAnim;

    //当前状态：正在画圆还是画线
    private STATE mCurrentState = STATE.CIRCLE;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mCirclePath = new Path();
        mLinePath = new Path();

        mPathMeasure = new PathMeasure();

        initAnimation();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPath(canvas);
    }

    private void initAnimation() {
        mCircleAnim = ValueAnimator.ofFloat(0, 1);
        mCircleAnim.setDuration(mCircleAnimDuration);
        mCircleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        mCircleAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mCurrentState = STATE.CIRCLE;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCurrentState = STATE.LINE;
                mLineAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mCircleAnim.start();

        mLineAnim = ValueAnimator.ofFloat(0, 1)
                .setDuration(mLineAnimDuration);
        mLineAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        mLineAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mCurrentState = STATE.LINE;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void drawPath(Canvas canvas) {

        canvas.drawColor(Color.parseColor("#7EC0EE"));

        switch (mCurrentState) {
            case CIRCLE:
                mCirclePath.reset();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    float value = (float) mCircleAnim.getAnimatedValue();
                    Log.e("TAG", "CIRCLE animatedValue > " + value);
                    float sweepAngle = 360 * value;
                    mCirclePath.addArc(centerX - mRadius, centerY - mRadius, centerX + mRadius, centerY + mRadius, 45, sweepAngle);
                    canvas.drawPath(mCirclePath, mPaint);
                }
                break;
            case LINE:
                canvas.drawCircle(centerX, centerY, mRadius, mPaint);
                mPathMeasure.setPath(mCirclePath, false);
                float pos[] = new float[2];
                float tan[] = new float[2];
                //获取坐标
                boolean posTan = mPathMeasure.getPosTan(0, pos, tan);
                if (posTan) {
                    float startX = pos[0];
                    float startY = pos[1];
                    float value = (float) mLineAnim.getAnimatedValue();
                    Log.e("TAG", "LINE animatedValue > " + value);
                    float length = mLineLength * value;
                    Log.e("TAG", "LINE length > " + length);
                    mLinePath.moveTo(startX, startY);
                    mLinePath.lineTo(startX + length, startY + length);
                    canvas.drawPath(mLinePath, mPaint);
                }
                break;
        }


    }
}
