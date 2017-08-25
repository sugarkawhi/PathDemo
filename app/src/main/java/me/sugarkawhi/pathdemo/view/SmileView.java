package me.sugarkawhi.pathdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2017/8/25.
 */

public class SmileView extends View {

    private Paint mPaint;
    private Path mPath;
    private ValueAnimator valueAnimator;

    private float x, y;

    public SmileView(Context context) {
        this(context, null);
    }

    public SmileView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
        valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        x = w / 2;
        y = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#7EC0EE"));
        canvas.drawCircle(x, y, 200, mPaint);
        canvas.drawCircle(x - 50, y - 50, 20, mPaint);
        canvas.drawCircle(x + 50, y - 50, 20, mPaint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float value = (float) valueAnimator.getAnimatedValue();
            mPath.addArc(x - 100, y - 100, x + 100, y + 100, 180, -180 * value);
            canvas.drawPath(mPath, mPaint);
        }
    }
}
