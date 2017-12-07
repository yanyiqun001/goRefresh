package com.GoRefresh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.GoRefresh.interfaces.IFooterView;
import com.GoRefresh.interfaces.LoadMoreListener;

/**
 * Created by Administrator on 2017/11/28 0028.
 */

public class LoadMoreHelper {
    private LoadMoreListener listener;
    private RvLoadMoreWrapper rvloadMoreWrapper;
    public boolean isLoadingMore;
    private IFooterView mFooter;
    private View mFooterView;
    private int mFooterStatus = 1;
    private int LOADING = 1; //加载状态
    private int FINISH = 2; //完成状态
    private int ERROR = 3; //错误状态
    private boolean hasFooter;
    private View contentView;

    public void setHasFooter(boolean hasFooter, View contentView, IFooterView defaultFooterView) {
        this.hasFooter = hasFooter;
        if (contentView instanceof RecyclerView) {
            if (rvloadMoreWrapper != null) {
                if (mFooter == null) {
                    rvloadMoreWrapper.setFooterView(defaultFooterView);
                } else {
                    rvloadMoreWrapper.setFooterView(mFooter);
                }
                rvloadMoreWrapper.setHasFooter(hasFooter);
            }
        } else if (contentView instanceof AbsListView) {
            if (hasFooter) {
                if (mFooter == null) {
                    setFooterView(defaultFooterView, contentView);
                }
            } else {
                removeFooterView((ListView) contentView);
            }
        }
    }

    public void setFooterView(IFooterView view, View contentView) {
        this.contentView = contentView;
        this.mFooter = view;
        setFooterView();
    }

    private void setFooterView() {
        if (contentView instanceof RecyclerView) {
            if (rvloadMoreWrapper != null) {
                rvloadMoreWrapper.setFooterView(mFooter);
            }
        } else if (contentView instanceof AbsListView) {
            if (hasFooter) {
                addListViewFooterView((ListView) contentView, LOADING);
            }
        }
    }

    public void setListener(LoadMoreListener listener) {
        this.listener = listener;
        if(rvloadMoreWrapper!=null){
            rvloadMoreWrapper.setLoadMoreListener(listener);
    }
    }


    public RvLoadMoreWrapper buildRvAdapter(RecyclerView.Adapter rvLoadMoreAdapter) {
        rvloadMoreWrapper = new RvLoadMoreWrapper(rvLoadMoreAdapter);
        rvloadMoreWrapper.setFooterView(mFooter);
        rvloadMoreWrapper.setHasFooter(hasFooter);
        return rvloadMoreWrapper;
    }


    public void showFooter(boolean isBottom) {
        if (hasFooter) {
            if (isBottom) {
                if (listener != null) {
                    if (!isLoadingMore) {
                        listener.onLoadmore();
                        isLoadingMore = true;
                    }
                }
            }
        }
    }

    public void finishLoadMore(View contentView) {
        if (contentView instanceof RecyclerView) {
            if (rvloadMoreWrapper != null) {
                rvloadMoreWrapper.finishLoadMore();
            }
        }
        isLoadingMore = false;
    }

    public void finishLoadmoreWithNoData(View contentView) {
        if (contentView instanceof RecyclerView) {
            if (valid(FINISH)) {
                if (rvloadMoreWrapper != null) {
                    rvloadMoreWrapper.finishLoadMoreWithNoData();
                }
            }
        } else if (contentView instanceof ListView) {
            if (mFooterStatus != FINISH) {
                switchFooterView((ListView) contentView, FINISH);
            }
        }
    }


    public void finishLoadmoreWithError(final View contentView) {
        if (contentView instanceof RecyclerView) {
            if (valid(ERROR)) {
                if (rvloadMoreWrapper != null) {
                    rvloadMoreWrapper.finishLoadMoreWithError();
                }
            }
        } else if (contentView instanceof ListView) {
            if (mFooterStatus != ERROR) {
                View view = mFooter.getFailureView().findViewById(mFooter.getRetryId());
                if (view != null) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                switchFooterView((ListView) contentView, LOADING);
                                ((ListView) contentView).setSelection(((ListView) contentView).getAdapter().getCount() - 1);
                                listener.onLoadmore();
                                return;
                            }
                        }
                    });
                }
                switchFooterView((ListView) contentView, ERROR);
            }
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

    private void switchFooterView(ListView listView, int flag) {
        if (valid(flag)) {
            mFooterStatus = flag;
            addListViewFooterView(listView, flag);
        }
    }


    public void addListViewFooterView(ListView listView, int flag) {
        if (mFooterView != null && mFooterStatus != flag) {
            return;
        }
        removeFooterView(listView);
        if (flag == LOADING) {
            mFooterView = mFooter.getLoadingView();
        } else if (flag == FINISH) {
            mFooterView = mFooter.getFinishView();
        } else if (flag == ERROR) {
            mFooterView = mFooter.getFailureView();
        }
        listView.addFooterView(mFooterView);
    }

    private void removeFooterView(ListView contentView) {
        if (mFooterView != null) {
            contentView.removeFooterView(mFooterView);
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
            setFooterView();
        }

    }

    public void setLoadingView(View view) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setLoadingView(view);
            setFooterView();
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
            setFooterView();
        }
    }

    public void setFinishWithNodataView(View view) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setFinishView(view);
            setFooterView();
        }
    }

    /**
     * 设置加载失败状态的footerview
     */
    public void setErrorView(int layoutID) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setErrorView(layoutID);
            setFooterView();
        }
    }

    public void setErrorView(View errorView) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setErrorView(errorView);
            setFooterView();
        }
    }

    /**
     * 设置加载失败状态的footerview ，同时添加触发重试的控件
     */
    public void setErrorViewWithRetry(int layoutID, int retryId) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setErrorView(layoutID, retryId);
            setFooterView();
        }
    }

    public void setErrorViewWithRetry(View errorView, int retryId) {
        if (mFooter instanceof DefaultFooterView) {
            ((DefaultFooterView) mFooter).setErrorView(errorView, retryId);
            setFooterView();
        }
    }


}
