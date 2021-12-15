package com.sky.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class CustomBaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final List<T> list;

    private final int layoutId;

    private static final int CLICK_EVENT_TIME = 600;

    private long lastClickTime;

    private OnItemClickListener<T> onItemClickListener;

    private OnItemLongClickListener<T> onItemLongClickListener;

    public interface OnItemClickListener<A> {
        void onItemClick(View view, A a, int position);
    }

    public interface OnItemLongClickListener<A> {
        boolean onItemLongClick(View view, A a, int position);
    }

    public CustomBaseAdapter(int layoutId, List<T> list) {
        this.list = list;
        this.layoutId = layoutId;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    protected abstract @NonNull
    VH onCreateCustomViewHolder(@NonNull View view);

    protected abstract void onBindCustomViewHolder(@NonNull VH vh, T t);

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        VH vh = onCreateCustomViewHolder(view);
        vh.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime >= CLICK_EVENT_TIME) {
                    int position = vh.getAdapterPosition();
                    if (position >= 0) {
                        onItemClickListener.onItemClick(v, list.get(position), position);
                    }
                }
                lastClickTime = currentTime;
            }
        });
        vh.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                int position = vh.getAdapterPosition();
                if (position >= 0) {
                    return onItemLongClickListener.onItemLongClick(v, list.get(position), position);
                }
            }
            return false;
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        onBindCustomViewHolder(holder, list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}
