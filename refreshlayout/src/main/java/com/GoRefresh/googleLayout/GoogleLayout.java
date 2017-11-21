package com.GoRefresh.googleLayout;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.GoRefresh.IHeaderView;

/**
 * Created by Administrator on 2017/9/27 0027.
 */

public class GoogleLayout extends FrameLayout implements IHeaderView{
    private int mCircleWidth;
    private int mCircleHeight;
    private static final int CIRCLE_DIAMETER = 40;
    private static final int CIRCLE_DIAMETER_LARGE = 56;
    // Maps to ProgressBar.Large style
    public static final int LARGE = MaterialProgressDrawable.LARGE;
    // Maps to ProgressBar default style
    public static final int DEFAULT = MaterialProgressDrawable.DEFAULT;
    // Default background for the progress spinner
    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    // Default offset in dips from the top of the view to where the progress spinner should stop
    private static final int DEFAULT_CIRCLE_TARGET = 64;
    private static final float MAX_PROGRESS_ANGLE = .8f;

    private static final int MAX_ALPHA = 255;
    private static final int STARTING_PROGRESS_ALPHA = (int) (.3f * MAX_ALPHA);

    private CircleImageView mCircleView;
    private MaterialProgressDrawable mProgress;

    public GoogleLayout(@NonNull Context context) {
        super(context);
        initView();
    }

    public GoogleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GoogleLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        createProgressView();
    }

    private void createProgressView() {
        mCircleView = new CircleImageView(getContext(), CIRCLE_BG_LIGHT);
        mProgress = new MaterialProgressDrawable(getContext(), this);
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        mCircleView.setImageDrawable(mProgress);
        mCircleView.setVisibility(View.VISIBLE);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        mCircleView.setLayoutParams(params);
        addView(mCircleView);

    }
    /**
     * @deprecated Use {@link #setProgressBackgroundColorSchemeResource(int)}
     */
    @Deprecated
    public void setProgressBackgroundColor(int colorRes) {
        setProgressBackgroundColorSchemeResource(colorRes);
    }
    /**
     * Set the background color of the progress spinner disc.
     *
     * @param colorRes Resource id of the color.
     */
    public void setProgressBackgroundColorSchemeResource(@ColorRes int colorRes) {
        setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getContext(), colorRes));
    }

    /**
     * Set the background color of the progress spinner disc.
     *
     * @param color
     */
    public void setProgressBackgroundColorSchemeColor(@ColorInt int color) {
        mCircleView.setBackgroundColor(color);
        mProgress.setBackgroundColor(color);
    }

    /**
     * @deprecated Use {@link #setColorSchemeResources(int...)}
     */
    @Deprecated
    public void setColorScheme(@ColorInt int... colors) {
        setColorSchemeResources(colors);
    }

    /**
     * Set the color resources used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     *
     * @param colorResIds
     */
    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        final Context context = getContext();
        int[] colorRes = new int[colorResIds.length];
        for (int i = 0; i < colorResIds.length; i++) {
            colorRes[i] = ContextCompat.getColor(context, colorResIds[i]);
        }
        setColorSchemeColors(colorRes);
    }

    /**
     * Set the colors used in the progress animation. The first
     * color will also be the color of the bar that grows in response to a user
     * swipe gesture.
     *
     * @param colors
     */
    public void setColorSchemeColors(@ColorInt int... colors) {
     //   ensureTarget();
        mProgress.setColorSchemeColors(colors);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPull(float a) {
        Log.d("pull",a+"");
        if (a <= 1f) {
            mProgress.setAlpha((int) (STARTING_PROGRESS_ALPHA + (MAX_ALPHA - STARTING_PROGRESS_ALPHA) * a));
        }
        float adjustedPercent = (float) Math.max(a - .4, 0) * 5 / 3;
        float strokeStart = adjustedPercent * .8f;
        mProgress.setStartEndTrim(0f, Math.min(MAX_PROGRESS_ANGLE, strokeStart));
        mProgress.setArrowScale(Math.min(1f, adjustedPercent));
        float rotation = (-0.25f + .4f * adjustedPercent) * .5f;
        mProgress.setProgressRotation(rotation);
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onChange(boolean isPull) {

    }

    @Override
    public void onRefresh() { mProgress.setAlpha(STARTING_PROGRESS_ALPHA);
        mCircleView.setVisibility(View.VISIBLE);
        mCircleView.getBackground().setAlpha(MAX_ALPHA);
        mProgress.setAlpha(MAX_ALPHA);
        mProgress.setArrowScale(1f);
        mProgress.start();
    }

    @Override
    public void onRefreshFinish() {

    }

    @Override
    public void onBackFinish() {
        mProgress.stop();
    }
}
