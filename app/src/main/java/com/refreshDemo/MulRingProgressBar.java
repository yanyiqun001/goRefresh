package com.refreshDemo;

import android.animation.Animator;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class MulRingProgressBar extends View {

    private final static int DEFAULT_BALL_COLOR = 0xFFFF9966;
    private final static int DEFAULT_RING_COLOR = 0xFFFF9966;
    private final static int DEFAULT_BALL_RADIUS = 1;
    private final static int DEFAULT_RING_RADIUS = 10;
    private final static int DEFAULT_DURATION = 1600;
    private final static float DEFAULT_INCREMENT = 1;
    private final static int DEFAULT_COUNT = 7;

    private int i = 0;
    private int mBallColor = DEFAULT_BALL_COLOR;
    private int mRingColor = DEFAULT_RING_COLOR;
    private float mBallRadius = dp2px(DEFAULT_BALL_RADIUS);
    private float mRingRadius = dp2px(DEFAULT_RING_RADIUS);
    private int mDuration = DEFAULT_DURATION;
    private int mCount = DEFAULT_COUNT;
    private float increment = DEFAULT_INCREMENT;

    private Paint mPaint;
    private List<ProgressBean> mList;

    public MulRingProgressBar(Context context) {
        this(context, null);
    }

    public MulRingProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MulRingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MulRingProgressBar);
        mBallColor = typedArray.getColor(R.styleable.MulRingProgressBar_progress_ball_color, mBallColor);
        mRingColor = typedArray.getColor(R.styleable.MulRingProgressBar_progress_ring_color, mRingColor);
        mBallRadius = typedArray.getDimension(R.styleable.MulRingProgressBar_progress_ball_radius, mBallRadius);
        mRingRadius = typedArray.getDimension(R.styleable.MulRingProgressBar_progress_ring_radius, mRingRadius);
        mDuration = typedArray.getInteger(R.styleable.MulRingProgressBar_progress_duration, mDuration);
        increment = typedArray.getFloat(R.styleable.MulRingProgressBar_progress_increment, increment);
        mCount = typedArray.getInteger(R.styleable.MulRingProgressBar_progress_count, mCount);
        typedArray.recycle();

        mList = new ArrayList<>();

        for (int i = mCount; i > 0; i--) {
            ProgressBean pb = new ProgressBean();
            pb.setAnimator(initAnimatior(pb));
            pb.setmCurrentAngle(0);
            pb.setmBallRadius(mBallRadius + increment * i);
            mList.add(pb);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);


    }


    private ValueAnimator initAnimatior(final ProgressBean pb) {
        Interpolator pathInterpolatorCompat = PathInterpolatorCompat.create(0.8f, 0f, 0.2f, 1f);
        ValueAnimator mAnimator = ValueAnimator.ofInt(0, 359);
        mAnimator.setDuration(mDuration);
        mAnimator.setRepeatCount(-1);
        mAnimator.setInterpolator(pathInterpolatorCompat);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pb.setmCurrentAngle((int) animation.getAnimatedValue());
                invalidate();
            }
        });
        return mAnimator;

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
            mRingRadius = result / 2 - (mBallRadius + mList.size() * increment);
        } else {
            result = (int) (getPaddingLeft() + getPaddingRight() + mRingRadius * 2 + (mBallRadius + mList.size() * increment) * 2);
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
            mRingRadius = result / 2 - (mBallRadius + mList.size() * increment);
        } else {
            result = (int) (getPaddingTop() + getPaddingBottom() + mRingRadius * 2 + (mBallRadius + mList.size() * increment) * 2);
            if (mode == MeasureSpec.AT_MOST) {
                return Math.min(size, result);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mCount; i++) {
            drawBall(canvas, mList.get(i).getmCurrentAngle() * 2 * Math.PI / 360, i);
        }
    }


//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        startAnimation();
//    }

    private void drawBall(Canvas canvas, double angle, int i) {
        // 根据当前角度获取x、y坐标点
        float x = (float) (getWidth() / 2 + mRingRadius * Math.sin(angle));
        float y = (float) (getHeight() / 2 - mRingRadius * Math.cos(angle));

        // 绘制圆
        canvas.drawCircle(x, y, mList.get(i).getmBallRadius(), mPaint);
    }

    public void startAnimation() {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    if (i < mCount) {
                        mList.get(i).getAnimator().start();
                        postDelayed(this, 60);
                        i = i + 1;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        postDelayed(runnable, 0);

    }

    private void stopAnimation() {
        for (int i = 0; i < mCount; i++) {
            mList.get(i).getAnimator().end();
        }
        i = 0;
    }

    public void start() {
        startAnimation();
    }

    public void stop() {
        stopAnimation();
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


    private class ProgressBean {
        private Animator animator;
        private Paint paint;
        private int mCurrentAngle;
        private float mBallRadius;

        public Animator getAnimator() {
            return animator;
        }

        public void setAnimator(Animator animator) {
            this.animator = animator;
        }

        public float getmBallRadius() {
            return mBallRadius;
        }

        public void setmBallRadius(float mBallRadius) {
            this.mBallRadius = mBallRadius;
        }

        public int getmCurrentAngle() {
            return mCurrentAngle;
        }

        public void setmCurrentAngle(int mCurrentAngle) {
            this.mCurrentAngle = mCurrentAngle;
        }

    }

}
