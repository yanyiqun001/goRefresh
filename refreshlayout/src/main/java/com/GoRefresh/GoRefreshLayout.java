package com.GoRefresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;

import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Administrator on 2017/9/23 0023.
 */

public class GoRefreshLayout extends ViewGroup {
    //最大下拉高度
    private float mMaxHeight = 500;
    //header高度
    private float mHeaderHeight = 250;
    //footer高度
    private float mFooterHeight = 100;
    //刷新高度
    private float mRefreshHeight = 300;
    //手指拖动距离与下拉距离之比
    private float mDamping = 2f;
    //返回顶部时长
    private int duration_top = 300;
    //返回到刷新高度(头部高度)时长
    private int duration_backtoRefreshHeight = 200;
    //底部弹出时长
    private int duration_footerVisiable = 200;
    //底部隐藏时长
    private int duration_footerHidden = 200;
    //自动刷新弹出时间
    private int duration_autotoRefreshHeight = 500;
    //当前状态
    private int mStatus = 0;
    //正常状态
    private int STATUS_NORMAL = 0;
    //下拉状态
    private int STATUS_PULL = 1;
    //就绪状态
    private int STATUS_READY = 2;
    //刷新状态
    private int STATUS_REFRESH = 3;
    //返回状态
    private int STATUS_BACK = 4;
    //是否固定内容
    private boolean isFixedContent = false;
    //头部接口对象
    private IHeaderView mHeader;
    //底部接口对象
    private IFooterView mFooter;
    //头部view
    private View mHeaderView;
    //底部view
    private View mFooterView;
    //内容view
    private View mContentView;

    private RefreshListener refresrhListener;

    private LoadmoreListener loadmoreListener;

    private AbsListView.OnScrollListener mScrollListener;
    //是否在加载状态
    private boolean isLoadingMore;
    //是否显示hasHeader
    private boolean hasHeader = true;
    //是否显示footer
    private boolean hasFooter;

    //FOOTER是否在显示状态
    private boolean isFooterVisibility = false;
    //Y方向偏移量
    private float offsetY;

    private boolean hasY = false;
    private int mOrignY;
    private int mLastY;
    private float mCurrentLastY;
    private VelocityTracker velocityTracker;
    private float velocity;
    //内容固定状态下当前头部偏移
    private float fixOffset;

    private ValueAnimator valueAnimatorTotop;
    private ValueAnimator valueAnimatorToRefresh;


    private int mFooterStatus = 1;
    private int LOADING = 1; //加载状态
    private int FINISH = 2; //完成状态
    private int ERROR = 3; //错误状态

    private boolean isLoadingMore2;

    private Context context;

    public GoRefreshLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public GoRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GoRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.GoRefreshLayout);
        mMaxHeight = typedArray.getDimension(R.styleable.GoRefreshLayout_maxHeight, mMaxHeight);
        mRefreshHeight = typedArray.getDimension(R.styleable.GoRefreshLayout_refreshHeight, mRefreshHeight);
        mHeaderHeight = typedArray.getDimension(R.styleable.GoRefreshLayout_headerHeight, mHeaderHeight);
        mFooterHeight = typedArray.getDimension(R.styleable.GoRefreshLayout_footerHeight, mFooterHeight);
        isFixedContent = typedArray.getBoolean(R.styleable.GoRefreshLayout_isFixed, isFixedContent);
        mDamping = typedArray.getFloat(R.styleable.GoRefreshLayout_damping, mDamping);
        duration_backtoRefreshHeight = typedArray.getInt(R.styleable.GoRefreshLayout_duration_BacktorefreshHeight, duration_backtoRefreshHeight);
        duration_top = typedArray.getInt(R.styleable.GoRefreshLayout_duration_BacktoTop, duration_top);
        duration_footerVisiable = typedArray.getInt(R.styleable.GoRefreshLayout_duration_FooterVisibility, duration_footerVisiable);
        duration_footerHidden = typedArray.getInt(R.styleable.GoRefreshLayout_duration_FooterHidden, duration_footerHidden);
        duration_autotoRefreshHeight=typedArray.getInt(R.styleable.GoRefreshLayout_duration_autotoRefreshHeight, duration_autotoRefreshHeight);
        typedArray.recycle();
        mHeader = new DefaultHeaderLayout(context);
        mFooter = new DefaultFooterView(context);
        velocityTracker = VelocityTracker.obtain();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mContentView = getChildAt(0);

        //监听view滑动到底部开启自动加载
        if (mContentView instanceof RecyclerView) {
            ((RecyclerView) mContentView).addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (hasFooter)
                        showFooter(ScrollingUtil.isViewToBottom(mContentView));
                }
            });
        } else if (mContentView instanceof AbsListView) {
            ((AbsListView) mContentView).setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {
                    if (mScrollListener != null) {
                        mScrollListener.onScrollStateChanged(absListView, i);
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                    if (mScrollListener != null) {
                        mScrollListener.onScroll(absListView, i, i1, i2);
                    }
                    if (hasFooter)
                        showFooterwithListView(absListView, ScrollingUtil.isViewToBottom(mContentView));
                }
            });
        }

        addHeaderView(mHeader);
    }


    private void addHeaderView(IHeaderView view) {
        mHeaderView = view.getView();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) mHeaderHeight);
        mHeaderView.setLayoutParams(params);
        addView(mHeaderView);
    }


    private void addFooterView(IFooterView view, int flag) {
        if (flag == LOADING) {
            mFooterView = view.getLoadingView();
        } else if (flag == FINISH) {
            mFooterView = view.getFinishView();
        } else if (flag == ERROR) {
            mFooterView = view.getFailureView();
        }
        if (mContentView instanceof AbsListView) {
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, (int) mFooterHeight);
            mFooterView.setLayoutParams(params);
            ((ListView) mContentView).addFooterView(mFooterView);
        } else {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) mFooterHeight);
            mFooterView.setLayoutParams(params);
            addView(mFooterView);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        int widthResult = widthSize;
        int heightResult = heightSize;
        if (widthMode == MeasureSpec.AT_MOST) {
            widthResult = mContentView.getMeasuredWidth();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightResult = mContentView.getMeasuredHeight();
        }
        setMeasuredDimension(widthResult, heightResult);
    }


    @Override
    protected void onLayout(boolean b, int i0, int i1, int i2, int i3) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == mHeaderView) {
                mHeaderHeight = child.getMeasuredHeight();
                if (isFixedContent) {
                    child.layout(0, (int) (-child.getMeasuredHeight() + fixOffset), child.getMeasuredWidth(), (int) fixOffset);
                } else {
                    child.layout(0, -child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
                }
            } else if (child == mFooterView) {
                child.layout(0, getHeight(), child.getMeasuredWidth(), getHeight() + child.getMeasuredHeight());
            } else {
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        velocityTracker.addMovement(ev);
        velocityTracker.computeCurrentVelocity(100);

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                velocity = velocityTracker.getYVelocity();
                if (isLoadingMore2) {
                    if (velocity < 50)
                        return true;
                }
                if (isFooterVisibility) {
                    if (velocity > 10) {
                        finishLoadmoremanual();
                        return true;
                    } else {
                        return true;
                    }
                }
                break;

        }

        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        int y = (int) event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = 0;
                mOrignY = y;
                //有偏移时再次按下 ，计算得到mCurrentLastY
                if ((getScrollY() < 0) && mStatus != STATUS_BACK) {
                    hasY = true;
                    cancelscrollToTop();
                    mCurrentLastY = calculateDistance(-getScrollY());
                    //刷新时点击事件不拦截
                    if (mStatus == STATUS_REFRESH) {
                        return false;
                    }
                    return true;
                } else {
                    hasY = false;
                }
                break;
            case ACTION_MOVE:
                int dy = y - mLastY;
                Log.d("onInterceptTouchEvent", "onInterceptTouchEvent==" + 0);
                // 内容固定时刷新状态不拦截滑动
                // 下拉拦截滑动事件
                // 刷新时Y方向有偏移时上拉拦截滑动事件
                // Y方向无偏移时上拉不拦截滑动事件
                // Math.abs(velocity)>1是由于 在手机上点击操作ACTION_DOWN会伴随无意义的ACTION_MOVE的出现 但不会有滑动速度，不拦截
                if (isFixedContent && mStatus == STATUS_REFRESH) {
                    return false;
                } else if (!canScrollUp() && dy > 0 && mStatus != STATUS_BACK && Math.abs(velocity) > 1) {
                    return true;
                } else if (dy < 0 && getScrollY() >= 0) {
                    return false;
                } else if (mStatus == STATUS_REFRESH && dy < 0) {
                    return true;
                }
                break;
        }
        mLastY = y;
        return super.onInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        if (Math.abs(getScrollY()) > mMaxHeight || !hasHeader) {
            return true;
        }
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                //正在刷新或者加载中时屏蔽对header和footer的点击事件
                if (mStatus == STATUS_REFRESH || isFooterVisibility) {
                    return false;
                }
                mOrignY = y;
                break;
            case ACTION_MOVE:
//                int dy = y - mLastY;
                //刷新时上拉
//                if(dy<0&&mStatus == STATUS_REFRESH&&getScrollY()<=0){
//                    // TODO: 2017/10/2 0002
//                    scrollBy(0, -dy);
//                }
                float distanceY;
                if (hasY) {
                    distanceY = y - mOrignY + mCurrentLastY;
                } else {
                    //ACTION_DOWN失去
                    if (mOrignY == -1) {
                        mOrignY = y;
                    }
                    distanceY = y - mOrignY;
                }
                offsetY = calculateOffsetY(distanceY);
                if (offsetY < 0) {
                    offsetY = 0;
                }
                if (isFixedContent) {
                    mHeaderView.layout(0, (int) (offsetY - mHeaderView.getMeasuredHeight()), mHeaderView.getMeasuredWidth(), (int) offsetY);
                    fixOffset = offsetY;
                } else {
                    scrollTo(0, -(int) offsetY);
                }
                if (mStatus != STATUS_REFRESH && mStatus != STATUS_BACK) {
                    if (offsetY < mRefreshHeight) {
                        if (mStatus == STATUS_READY) {
                            //上拉经过临界点
                            mHeader.onChange(false);
                        }
                        mStatus = STATUS_PULL;
                        mHeader.onPull(offsetY / mRefreshHeight);
                    } else {
                        if (mStatus == STATUS_PULL) {
                            //下拉经过临界点
                            mHeader.onChange(true);
                        }
                        mStatus = STATUS_READY;
                        mHeader.onReady();
                    }
                }

                break;
            case ACTION_UP:
                //松开时如果在刷新状态，下拉时返回偏移高度
                if (mStatus == STATUS_REFRESH) {
                    if (offsetY > mHeaderHeight) {
                        startRefresh((int) offsetY, duration_backtoRefreshHeight);
                    }
                }
                //松开时如果在就绪状态，开启刷新
                else if (mStatus == STATUS_READY) {
                    mStatus = STATUS_REFRESH;
                    startRefresh((int) offsetY, duration_backtoRefreshHeight);
                    mHeader.onRefresh();
                    if (refresrhListener != null) {
                        refresrhListener.onRefresh();
                    }
                    //    postDelayed(runnable, 4000);
                } else if (mStatus == STATUS_BACK) {
                    break;
                }
                //否则回到顶部
                else {
                    if (offsetY >= 0) {
                        cancelscrollToTop();
                        if (isFixedContent) {
                            scrollToTop((int) offsetY);
                        } else {
                            scrollToTop(-getScrollY());
                        }
                    } else {
                        scrollToTop(0);
                    }
                }
                break;
            default:
                break;
        }
        // mLastY = y;

        return true;
    }


    /**
     * 判断是否内容在顶部，可以下拉
     *
     * @return
     */
    private boolean canScrollUp() {
        return getChildAt(0).canScrollVertically(-1);
    }

    /**
     * 计算Y方向偏移
     * 当前百分比计算方式 decelerateInterpolator 公式：(1 - (1- x) * (1 - x))
     *
     * @param distanceY
     * @return
     */
    public float calculateOffsetY(float distanceY) {
        float percent = distanceY / mMaxHeight / mDamping > 1 ? 1 : distanceY / mMaxHeight / mDamping;
        return (1 - (1 - percent) * (1 - percent)) * mMaxHeight;
    }

    /**
     * 计算Y方向滑动距离
     * 通过公式：(1 - (1- x) * (1 - x)) *mMaxHeight=offsetY 推算x的值
     * 通过x=distanceY / mMaxHeight/mDamping 推算distanceY
     *
     * @param offsetY
     * @return
     */
    public float calculateDistance(int offsetY) {
        return (float) (1 - Math.sqrt(1 - offsetY / mMaxHeight)) * mDamping * mMaxHeight;
    }

    /**
     * 从下拉位置回弹至刷新位置
     *
     * @param offsetY
     */
    private void startRefresh(final int offsetY, int duration) {
        if (valueAnimatorToRefresh != null && valueAnimatorToRefresh.isRunning()) {
            valueAnimatorToRefresh.cancel();
        }
        valueAnimatorToRefresh = ValueAnimator.ofInt(offsetY, (int) mHeaderHeight);
        valueAnimatorToRefresh.setDuration(duration);
        valueAnimatorToRefresh.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                Log.d("tag",value+"");
                if (isFixedContent) {
                    mHeaderView.layout(0, value - mHeaderView.getMeasuredHeight(), mHeaderView.getMeasuredWidth(), value);
                    fixOffset = value;
                } else {
                    scrollTo(0, -value);
                }
            }
        });
        valueAnimatorToRefresh.start();
    }

    /**
     * 刷新完成返回顶部
     *
     * @param offset
     */
    private void finishRefresh(int offset) {
        mStatus = STATUS_BACK;
        mHeader.onRefreshFinish();
        scrollToTop(offset);
    }

    /**
     * 任意位置返回至顶部
     *
     * @param offset
     */
    private void scrollToTop(int offset) {
        valueAnimatorTotop = ValueAnimator.ofInt(offset, 0);
        valueAnimatorTotop.setDuration(duration_top);
        valueAnimatorTotop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
//                if (value == 0) {
//                    finishToTop();
//                }
                if (isFixedContent) {
                    mHeaderView.layout(0, value - mHeaderView.getMeasuredHeight(), mHeaderView.getMeasuredWidth(), value);
                    fixOffset = value;
                } else {
                    scrollTo(0, -value);
                }
            }
        });
        valueAnimatorTotop.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finishToTop();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }

        });
        valueAnimatorTotop.start();
    }

    /**
     * 取消返回顶部
     */
    private void cancelscrollToTop() {
        if (valueAnimatorTotop != null && valueAnimatorTotop.isRunning()) {
            valueAnimatorTotop.cancel();
        }
    }

    /**
     * 返回顶部后调用
     */
    private void finishToTop() {
        mStatus = STATUS_NORMAL;
        mHeader.onBackFinish();
        reset();
    }

    //--------------------------------------------------------------------------------
    //------------------------------footer相关--------------------------------------
    //--------------------------------------------------------------------------------

    private void showFooter(boolean isBottom) {
        if (isBottom && !isFooterVisibility && velocity <= 0) {
            showFooter();
            if (!isLoadingMore) {
                isLoadingMore = true;
                if (loadmoreListener != null) {
                    loadmoreListener.onLoadmore();
                }
            }
            isFooterVisibility = true;

        }
    }

    private void showFooterwithListView(AbsListView absListView, boolean viewToBottom) {
        if (viewToBottom) {
            if (loadmoreListener != null) {
                if (!isLoadingMore) {
                    loadmoreListener.onLoadmore();
                    isLoadingMore = true;
                }
            }
        }

    }

    /**
     * 显示footerview
     */
    private void showFooter() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, (int) mFooterHeight);
        valueAnimator.setDuration(duration_footerVisiable);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                scrollTo(0, value);
            }
        });
        valueAnimator.start();
    }

    /**
     * 隐藏footerview
     */
    private void hideFooter() {
        if (duration_footerHidden <= 0) {
            if (mContentView instanceof RecyclerView) {
                mContentView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollTo(0, 0);
                        mContentView.scrollBy(0, (int) mFooterHeight);
                    }
                });
            } else if (mContentView instanceof AbsListView) {
                mContentView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollTo(0, 0);
                        ((AbsListView) mContentView).smoothScrollBy((int) mFooterHeight, 0);
                    }
                });
            }
        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofInt((int) mFooterHeight, 0);
            valueAnimator.setDuration(duration_footerHidden);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    final int value = (int) valueAnimator.getAnimatedValue();
                    final int dy = currentValue - value;
                    if (value == 0) {
                        isLoadingMore2 = false;
                    }
                    if (dy > 0) {
                        if (mContentView instanceof RecyclerView) {
                            mContentView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollTo(0, value);
                                    mContentView.scrollBy(0, dy);
                                }
                            });
                        } else if (mContentView instanceof AbsListView) {
                            mContentView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollTo(0, value);
                                    ((AbsListView) mContentView).smoothScrollBy(dy, 0);
                                }
                            });
                        }
                    }

                    updateCurrentFooterHeight(valueAnimator);
                }
            });
            valueAnimator.start();
        }
    }

    private int currentValue;

    private void updateCurrentFooterHeight(ValueAnimator valueAnimator) {
        currentValue = (int) valueAnimator.getAnimatedValue();
    }

    private void reset() {
        hasY = false;
        mOrignY = -1;
        mCurrentLastY = 0;
    }


    private void removeHeaderView() {
        if (mHeaderView != null) {
            removeView(mHeaderView);
        }
    }

    private void removeFooterView() {
        if (mFooterView != null) {
            if (mContentView instanceof AbsListView) {
                ((ListView) mContentView).removeFooterView(mFooterView);
            } else {
                removeView(mFooterView);
            }
        }
    }

    /**
     * 切换footerview
     *
     * @param flag
     */
    private void switchFooterView(int flag) {
        if (valid(flag)) {
            mFooterStatus = flag;
            removeFooterView();
            addFooterView(mFooter, flag);
        }
    }

    private boolean valid(int flag) {
        if (mFooter != null) {
            if (flag == LOADING) {
                if (mFooter.getLoadingView() != null) {
                    return true;
                }
            } else if (flag == FINISH) {
                if (mFooter.getFinishView() != null) {
                    return true;
                }

            } else if (flag == ERROR) {
                if (mFooter.getFailureView() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    //--------------------------------------------------------------------------------
    //------------------------------对外提供api--------------------------------------
    //--------------------------------------------------------------------------------

    /**
     * 设置header
     */
    public void setHeaderView(IHeaderView view) {
        removeHeaderView();
        mHeader = view;
        addHeaderView(view);
    }

    /**
     * 设置footer
     */
    public void setFooterView(IFooterView view) {
        if (hasFooter) {
            mFooter = view;
            removeFooterView();
            addFooterView(view, LOADING);
        }
    }


    /**
     * 设置是否显示footer
     *
     * @param hasFooter
     */
    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
        if (hasFooter) {
            if (mFooterView == null) {
                setFooterView(mFooter);
            }
        } else {
            removeFooterView();
        }
    }

    /**
     * 设置是否显示header
     */
    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
        if (hasHeader) {
            setHeaderView(mHeader);
        } else {
            removeHeaderView();
        }
    }

    public boolean isHasFooter() {
        return hasFooter;
    }

    /**
     * 设置下拉刷新事件监听
     *
     * @param listener
     */
    public void setOnRefreshListener(RefreshListener listener) {
        this.refresrhListener = listener;
    }

    /**
     * 设置上拉加载事件监听
     *
     * @param listener
     */
    public void setOnLoadmoreListener(LoadmoreListener listener) {
        this.loadmoreListener = listener;
    }


    /**
     * 设置listview的滚动监听
     */
    public void setScrollListener(AbsListView.OnScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }

    /**
     * 自动下拉刷新
     */
    public void startRefresh() {
        post(new Runnable() {
            @Override
            public void run() {
                startRefresh((int) offsetY, duration_autotoRefreshHeight);
                mHeader.onRefresh();
                mStatus = STATUS_REFRESH;
                if (refresrhListener != null)
                    refresrhListener.onRefresh();
            }
        });

    }

    /**
     * 结束刷新，刷新完成调用
     */
    public void finishRefresh() {
        if (isFixedContent) {
            if (mStatus == STATUS_REFRESH)
                finishRefresh((int) mHeaderHeight);

        } else {
            finishRefresh(-getScrollY());
        }
    }

    /**
     * 隐藏footerview，手动下拉时调用
     */
    private void finishLoadmoremanual() {
        if (isFooterVisibility) {
            isLoadingMore2 = true;
            isFooterVisibility = false;
            hideFooter();
        }
    }

    /**
     * 隐藏footerview,加载数据完毕时调用
     */
    public void finishLoadmore() {
        isLoadingMore = false;
        if (isFooterVisibility) {
            isFooterVisibility = false;
            hideFooter();
        }
    }

    /**
     * 加载数据完毕没有更多数据时调用
     */
    public void finishLoadmoreWithNoData() {
        switchFooterView(FINISH);
    }

    /**
     * 加载数据出错时调用
     */
    public void finishLoadmoreWithError() {
        switchFooterView(ERROR);
        if (mFooter.getRetryView() != null) {
            mFooter.getRetryView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loadmoreListener != null) {
                        if (mContentView instanceof AbsListView) {
                            switchFooterView(LOADING);
                            ((ListView) mContentView).setSelection(((ListView) mContentView).getAdapter().getCount() - 1);
                            loadmoreListener.onLoadmore();
                            return;
                        }
                        switchFooterView(LOADING);
                        loadmoreListener.onLoadmore();
                    }
                }
            });
        }
    }

    /**
     * 设置是否内容不随头部下拉
     *
     * @param fixedContent
     */
    public void setFixedContent(boolean fixedContent) {
        isFixedContent = fixedContent;
    }

    public boolean isFixedContent() {
        return isFixedContent;
    }

    public float getMaxHeight() {
        return mMaxHeight;
    }

    /**
     * 设置下拉最大高度
     */
    public void setMaxHeight(float mMaxHeight) {
        this.mMaxHeight = mMaxHeight;
    }

    public float getHeaderHeight() {
        return mHeaderHeight;
    }

    /**
     * 设置header高度
     */
    public void setHeaderHeight(float mHeaderHeight) {
        this.mHeaderHeight = mHeaderHeight;
    }

    public float getFooterHeight() {
        return mFooterHeight;
    }

    /**
     * 设置footer高度
     */
    public void setFooterHeight(float mFooterHeight) {
        this.mFooterHeight = mFooterHeight;
    }

    public float getRefreshHeight() {
        return mRefreshHeight;
    }

    /**
     * 设置刷新高度
     */
    public void setRefreshHeight(float mRefreshHeight) {
        this.mRefreshHeight = mRefreshHeight;
    }

    public float getDamping() {
        return mDamping;
    }

    /**
     * 设置阻尼数值
     */
    public void setDamping(float mDamping) {
        this.mDamping = mDamping;
    }

    public int getDurationtoTop() {
        return duration_top;
    }

    /**
     * 设置下拉回弹时间
     */
    public void setDurationtoTop(int duration_top) {
        this.duration_top = duration_top;
    }


    /**
     * 设置下拉回弹至刷新高度时间
     */
    public void setDurationtoRefreshHeight(int duration_refreshHeight) {
        this.duration_backtoRefreshHeight = duration_refreshHeight;
    }

    public int getDurationtoRefreshHeight() {
        return duration_backtoRefreshHeight;
    }

    public int getDuration_autotoRefreshHeight() {
        return duration_autotoRefreshHeight;
    }

    /**
     * 设置自动弹出时间
     */
    public void setDuration_autotoRefreshHeight(int duration_autotoRefreshHeight) {
        this.duration_autotoRefreshHeight = duration_autotoRefreshHeight;
    }

    /**
     * 设置footerview弹出时长
     */
    public void setDurationFooterVisiable(int duration_footer) {
        this.duration_footerVisiable = duration_footer;
    }

    public int getDurationFooterVisiable() {
        return duration_footerVisiable;
    }

    /**
     * 设置footerview返回底部时间
     */
    public int getDurationFooterHidden() {
        return duration_footerHidden;
    }

    public void setDurationFooterHidden(int duration_footerHidden) {
        this.duration_footerHidden = duration_footerHidden;
    }

    /**
     * 获得fooerview状态
     *
     * @return
     */
    public int getFooterStatus() {
        return mFooterStatus;
    }

    /**
     * 获得默认footerview
     *
     * @return
     */
    public DefaultFooterView getDefaultFooterView() {
        if (mFooter instanceof DefaultFooterView) {
            return (DefaultFooterView) mFooter;
        } else {
            return new DefaultFooterView(context);
        }

    }

    /**
     * 设置加载状态的footerview
     *
     * @param layoutID
     */
    public void setLoadingView(int layoutID) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setLoadingView(layoutID);
            setFooterView(mFooter);
        }
    }

    public void setLoadingView(View view) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setLoadingView(view);
            setFooterView(mFooter);
        }
    }

    /**
     * 设置完成状态的footerview
     *
     * @param layoutID
     */
    public void setFinishWithNodataView(int layoutID) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setFinishView(layoutID);
            setFooterView(mFooter);
        }
    }

    public void setFinishWithNodataView(View view) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setFinishView(view);
            setFooterView(mFooter);
        }
    }

    /**
     * 设置加载失败状态的footerview
     */
    public void setErrorView(int layoutID) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setErrorView(layoutID);
            setFooterView(mFooter);
        }
    }

    public void setErrorView(View errorView) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setErrorView(errorView);
            setFooterView(mFooter);
        }
    }

    /**
     * 设置加载失败状态的footerview ，同时添加触发重试的控件
     */
    public void setErrorViewWithRetry(int layoutID, int retryId) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setErrorView(layoutID, retryId);
            setFooterView(mFooter);
        }
    }

    public void setErrorViewWithRetry(View errorView, View retryView) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setErrorView(errorView, retryView);
            setFooterView(mFooter);
        }
    }
}
