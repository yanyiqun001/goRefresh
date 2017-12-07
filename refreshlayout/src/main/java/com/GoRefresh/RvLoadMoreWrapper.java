package com.GoRefresh;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.GoRefresh.interfaces.IFooterView;
import com.GoRefresh.interfaces.LoadMoreListener;

/**
 * Created by Administrator on 2017/11/27 0027.
 */

public class RvLoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private View mLoadMoreView;
    private View mFinishView;
    private View mErrorView;
    private boolean isRemove;
    private int mlayoutID;
    private int mFinishlayoutID;
    private int mErrorlayoutID;
    private int mErrorid;
    private boolean isLoadingMore;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private LoadMoreListener mLoadMoreListener;
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    public static final int ITEM_TYPE_LOAD_MORE_END = Integer.MAX_VALUE - 3;
    public static final int ITEM_TYPE_LOAD_MORE_ERROR = Integer.MAX_VALUE - 4;
    public static final int ITEM_TYPE_NONE = Integer.MAX_VALUE - 5;
    private int LOAD_MORE_STATUS;
    private int NONE = 0;
    private int LOADING = 1;
    private int FINISH = 2;
    private int ERROR = 3;
    private boolean hasFooter;
    private IFooterView mFooterView;

    public RvLoadMoreWrapper(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FooterViewHolder holder = null;
        if (viewType == ITEM_TYPE_LOAD_MORE_END) {
            if (mFinishlayoutID != 0) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(mFinishlayoutID, parent,
                        false);
                holder = new FooterViewHolder(itemView);
            } else {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mFinishView.setLayoutParams(layoutParams);
                holder = new FooterViewHolder(mFinishView);
            }
            return holder;
        }
        if (viewType == ITEM_TYPE_LOAD_MORE_ERROR) {
            if (mErrorlayoutID != 0) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(mErrorlayoutID, parent,
                        false);
                holder = new FooterViewHolder(itemView);
            } else {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mErrorView.setLayoutParams(layoutParams);
                holder = new FooterViewHolder(mErrorView);
            }
            return holder;
        }
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            if (mlayoutID != 0) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(mlayoutID, parent,
                        false);
                holder = new FooterViewHolder(itemView);
            } else {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mLoadMoreView.setLayoutParams(layoutParams);
                holder = new FooterViewHolder(mLoadMoreView);
            }
            return holder;
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            if (isShowFooter(position)) {
                if (mLoadMoreListener != null) {
                    if (LOAD_MORE_STATUS == LOADING) {
                        if (!isLoadingMore) {
                            mLoadMoreListener.onLoadmore();
                            isLoadingMore = true;
                        }
                    } else if (LOAD_MORE_STATUS == ERROR) {
                        if (mErrorid == 0) {
                            return;
                        } else {
                            ((FooterViewHolder) holder).getView(mErrorid).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LOAD_MORE_STATUS = LOADING;
                                    notifyItemChanged(adapter.getItemCount() + 1);
                                    mLoadMoreListener.onLoadmore();
                                }
                            });
                        }
                    }
                }
            }
        } else {
            adapter.onBindViewHolder(holder, position);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (isShowFooter(position)) {
            if (LOAD_MORE_STATUS == FINISH) {
                return ITEM_TYPE_LOAD_MORE_END;
            } else if (LOAD_MORE_STATUS == ERROR) {
                return ITEM_TYPE_LOAD_MORE_ERROR;
            } else if (LOAD_MORE_STATUS == LOADING) {
                return ITEM_TYPE_LOAD_MORE;
            }
        }
        return adapter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (LOAD_MORE_STATUS == NONE) {
            return adapter.getItemCount();
        } else {
            return adapter.getItemCount() + 1;
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;
        private View mConvertView;
        private Context mContext;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<View>();
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    public void setFooterView(IFooterView loadMoreView) {
        mFooterView = loadMoreView;
        mLoadMoreView = mFooterView.getLoadingView();
        mFinishView = mFooterView.getFinishView();
        mErrorView = mFooterView.getFailureView();
        mErrorid = mFooterView.getRetryId();
    }


    public RvLoadMoreWrapper setLoadingView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public RvLoadMoreWrapper setLoadingView(int layoutID) {
        this.mlayoutID = layoutID;
        return this;
    }


    public RvLoadMoreWrapper setFinishView(View finishView) {
        mFinishView = finishView;
        return this;
    }

    public RvLoadMoreWrapper setFinishView(int finishlayoutID) {
        mFinishlayoutID = finishlayoutID;
        return this;
    }


    public RvLoadMoreWrapper setErrorView(int errorID, int retryOnclicklayoutid) {
        mErrorlayoutID = errorID;
        mErrorid = retryOnclicklayoutid;
        return this;
    }

    public RvLoadMoreWrapper setErrorView(View errorView, int retryOnclicklayoutid) {
        mErrorid = retryOnclicklayoutid;
        mErrorView = errorView;
        return this;
    }

    public RvLoadMoreWrapper setErrorView(View errorView) {
        mErrorView = errorView;
        return this;
    }

    public RvLoadMoreWrapper setErrorView(int errorID) {
        mErrorlayoutID = errorID;
        return this;
    }


    public void finishLoadMoreWithNoData() {
        LOAD_MORE_STATUS = FINISH;
        notifyItemChanged(adapter.getItemCount() + 1);
    }

    public void finishLoadMore() {
        LOAD_MORE_STATUS = LOADING;
        notifyItemChanged(adapter.getItemCount() + 1);
        isLoadingMore = false;
    }

    public void finishLoadMoreWithError() {
        LOAD_MORE_STATUS = ERROR;
        notifyItemChanged(adapter.getItemCount() + 1);
    }


//    public void finishLoadMore() {
//        if (getItemCount() - adapter.getItemCount() > 0) {
//            isRemove = true;
//            notifyItemRemoved(adapter.getItemCount() + 1);
//        }
//    }


    private boolean isShowFooter(int position) {
        return position >= adapter.getItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == ITEM_TYPE_LOAD_MORE
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == adapter.getItemCount()) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public void setHasFooter(boolean hasFooter) {
        if (hasFooter) {
            LOAD_MORE_STATUS = LOADING;
        } else {
            LOAD_MORE_STATUS = NONE;
        }
    }


}
