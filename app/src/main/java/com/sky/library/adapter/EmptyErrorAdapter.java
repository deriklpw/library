package com.sky.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 支持网络异常，数据空布局
 * <p>
 * 使用时，必须先判断RecyclerView.ViewHolder派生类的类型，否则会引起异常
 * <p>
 * 未设置空布局和异常布局时，可以当普通Adapter使用
 *
 * @param <T>
 */
public abstract class EmptyErrorAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CustomBaseAdapter";

    public static final int VIEW_TYPE_EMPTY = 0x10000000;

    public static final int VIEW_TYPE_ERROR = 0x10000001;

    public static final int VIEW_TYPE_ITEM = 0x10000002;

    private static final int CLICK_EVENT_TIME = 600;

    private long lastClickTime;

    private final List<T> mList;

    @LayoutRes
    private final int mLayoutId;

    protected Context mAdapterContext;

    private OnItemClickListener<T> mOnItemClickListener = null;

    private OnItemLongClickListener<T> mOnItemLongClickListener = null;

    @LayoutRes
    private int mEmptyViewResId = -1;

    @LayoutRes
    private int mErrorViewResId = -1;

    private boolean isEmpty;

    private boolean isError;

    private OnViewInflatedCallback mOnErrorViewCallback = null;

    private OnViewInflatedCallback mOnEmptyViewCallback = null;

    public interface OnItemClickListener<A> {
        void onItemClick(View view, A a, int position);
    }

    public interface OnItemLongClickListener<A> {
        void onItemLongClick(View view, A a, int position);
    }

    public interface OnViewInflatedCallback {
        void onViewInflated(View view);
    }

    public EmptyErrorAdapter(@LayoutRes int layoutId, List<T> list) {
        this.mList = list;
        this.mLayoutId = layoutId;
    }

    public EmptyErrorAdapter(Context context, @LayoutRes int layoutId, List<T> list) {
        this.mList = list;
        this.mLayoutId = layoutId;
        this.mAdapterContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setErrorResId(@LayoutRes int errorResId) {
        mErrorViewResId = errorResId;
    }

    public void setEmptyResId(@LayoutRes int emptyResId) {
        mEmptyViewResId = emptyResId;
    }

    public void setErrorResId(@LayoutRes int errorResId, OnViewInflatedCallback onViewInflatedCallback) {
        mErrorViewResId = errorResId;
        mOnErrorViewCallback = onViewInflatedCallback;
    }

    public void setEmptyResId(@LayoutRes int emptyResId, OnViewInflatedCallback onViewInflatedCallback) {
        mEmptyViewResId = emptyResId;
        mOnEmptyViewCallback = onViewInflatedCallback;
    }

    @NonNull
    public abstract VH onCreateCustomViewHolder(@NonNull View view);

    public abstract void onBindCustomViewHolder(@NonNull VH viewHolder, T t, int position);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ERROR) {
            View errorView = LayoutInflater.from(parent.getContext()).inflate(mErrorViewResId, parent, false);
            if (mOnErrorViewCallback != null) {
                mOnErrorViewCallback.onViewInflated(errorView);
            }
            return new ErrorViewHolder(errorView);
        } else if (viewType == VIEW_TYPE_EMPTY) {
            View emptyView = LayoutInflater.from(parent.getContext()).inflate(mEmptyViewResId, parent, false);
            if (mOnEmptyViewCallback != null) {
                mOnEmptyViewCallback.onViewInflated(emptyView);
            }
            return new EmptyViewHolder(emptyView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
            VH viewHolder = onCreateCustomViewHolder(view);
            viewHolder.itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    long currentTime = System.currentTimeMillis();
                    if ((currentTime - lastClickTime) >= CLICK_EVENT_TIME) {
                        int position = viewHolder.getAdapterPosition();
                        if (position > 0) {
                            mOnItemClickListener.onItemClick(v, mList.get(position), position);
                        }
                    }
                    lastClickTime = currentTime;
                }
            });
            viewHolder.itemView.setOnLongClickListener(v -> {
                if (mOnItemLongClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    if (position > 0) {
                        mOnItemLongClickListener.onItemLongClick(v, mList.get(position), position);
                    }
                }
                return false;
            });
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == VIEW_TYPE_ITEM) {
            onBindCustomViewHolder((VH) viewHolder, mList.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        //数据为空的情况下，才显示布局，有数据时，弹toast提示
        if (mList == null || mList.size() == 0) {
            if ((isErrorLayoutEnable() && isError) || (isEmptyLayoutEnable() && isEmpty)) {
                return 1;
            } else {
                //均为false时，无Item
                return 0;
            }
        }

        //如果不为0，按正常的流程跑
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList == null || mList.size() == 0) {
            //优先处理error
            if (isErrorLayoutEnable() && isError) {
                return VIEW_TYPE_ERROR;
            } else if (isEmptyLayoutEnable() && isEmpty) {
                return VIEW_TYPE_EMPTY;
            } else {
                //isError和isEmpty都为false，item数为0
                return VIEW_TYPE_ITEM;
            }
        }
        //size不为0，返回ItemType
        return VIEW_TYPE_ITEM;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //如果是脚布局或者头布局,返回合并的列数，否则返回1，表示不合并
                    if (getItemViewType(position) == VIEW_TYPE_ERROR || getItemViewType(position) == VIEW_TYPE_EMPTY) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    private static class ErrorViewHolder extends RecyclerView.ViewHolder {
        public ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private boolean isErrorLayoutEnable() {
        return mErrorViewResId != -1;
    }

    private boolean isEmptyLayoutEnable() {
        return mEmptyViewResId != -1;
    }

    /**
     * 无网络时使用
     */
    public void updateNetError() {
        this.isEmpty = false;
        this.isError = true;
        this.notifyDataSetChanged();
    }

    /**
     * 有网络时，数据变化使用
     */
    public void updateDataChanged() {
        this.isError = false;
        isEmpty = mList == null || mList.size() == 0;
        this.notifyDataSetChanged();
    }

}
