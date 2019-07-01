package com.example.animecalendar.base.recycler;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView,
                          BaseListAdapter.OnItemClickListener onItemClickListener,
                          BaseListAdapter.OnItemLongClickListener onItemLongClickListener) {
        super(itemView);
        if (onItemClickListener != null) {
            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
        if (onItemLongClickListener != null) {
            itemView.setOnLongClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    return onItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                }
                return false;
            });
        }
    }

    public abstract void bind(T type);

}