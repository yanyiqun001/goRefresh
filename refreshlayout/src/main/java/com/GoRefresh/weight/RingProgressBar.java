package com.GoRefresh.weight;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Interpolator;

import com.GoRefresh.R;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class RingProgressBar extends View {

    private final static int DEFAULT_BALL_COLOR = 0xFFFF9966;
    private final static int DEFAULT_RING_COLOR = 0xFFFF9966;
    private final static int DEFAULT_BALL_RADIUS = 4;
    private final static int DEFAULT_RING_RADIUS = 20;
    private final static int DEFAULT_RING_STOKEWIDTH = 1;
    private final static int DEFAULT_DURATION = 1300;


    private int mBallColor = DEFAULT_BALL_COLOR;
    private int mRingColor = DEFAULT_RING_COLOR;
    private float mBallRadius = dp2px(DEFAULT_BALL_RADIUS);
    private float mRingRadius = dp2px(DEFAULT_RING_RADIUS);
    private float mStokeWidth = dp2px(DEFAULT_RING_STOKEWIDTH);
    private int mDuration = DEFAULT_DURATION;

    private int mCurrentAngle;
    private ValueAnimator mAnimator;// 旋转动画
    private Paint mPaint;
    private Paint mPaint2;
    private Interpolator pathInterpolatorCompat;

    public RingProgressBar(Context context) {
        this(context, null);
    }

    public RingProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RingProgressBar);
        mBallColor = typedArray.getColor(R.styleable.RingProgressBar_progress_ball_color, mBallColor);
        mRingColor = typedArray.getColor(R.styleable.RingProgressBar_progress_ring_color, mRingColor);
        mBallRadius = typedArray.getDimension(R.styleable.RingProgressBar_progress_ball_radius, mBallRadius);
        mRingRadius = typedArray.getDimension(R.styleable.RingProgressBar_progress_ring_radius, mRingRadius);
        mStokeWidth = typedArray.getDimension(R.styleable.RingProgressBar_progress_ring_stokewidth, mStokeWidth);
        mDuration = typedArray.getInteger(R.styleable.RingProgressBar_progress_duration, mDuration);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mBallColor);
        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(mRingColor);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setStrokeWidth(mStokeWidth);
        initAnimatior();
    }


    private void initAnimatior() {
        pathInterpolatorCompat = PathInterpolatorCompat.create(0.7f, 0f, 0.3f, 1f);
        mAnimator = ValueAnimator.ofInt(0, 359);
        mAnimator.setDuration(mDuration);
        mAnimator.setRepeatCount(-1);
        mAnimator.setInterpolator(pathInterpolatorCompat);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentAngle = (int) animation.getAnimatedValue();

                invalidate();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = measurehight(heightMeasureSpec);
        int width = measurewidth(widthMeasureSpec);
        setMeasuredDimension(width, height);

    }

    private int measurewidth(int widthMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
            mRingRadius = result/2 - mBallRadius;
        } else {
            result = (int) (getPaddingLeft() + getPaddingRight() + mRingRadius * 2 + Math.max(mStokeWidth, mBallRadius * 2));
            if (mode == MeasureSpec.AT_MOST) {
                return Math.min(size, result);
            }
        }
        return result;

    }

    private int measurehight(int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
            mRingRadius = result/2 - mBallRadius;
        } else {
            result = (int) (getPaddingTop() + getPaddingBottom() + mRingRadius * 2 + Math.max(mStokeWidth, mBallRadius * 2));
            if (mode == MeasureSpec.AT_MOST) {
                return Math.min(size, result);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, mRingRadius, mPaint2);
        drawBall(canvas, mCurrentAngle * 2 * Math.PI / 360);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    private void drawBall(Canvas canvas, double angle) {
        // 根据当前角度获取x、y坐标点
        float x = (float) (getWidth() / 2 + mRingRadius * Math.sin(angle));
        float y = (float) (getHeight() / 2 - mRingRadius * Math.cos(angle));

        // 绘制圆
        canvas.drawCircle(x, y, mBallRadius, mPaint);
    }


    public void startAnimation() {
        mAnimator.start();
    }

    public void stopAnimation() {
        mAnimator.end();
    }

    //销毁页面时停止动画
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }
}
